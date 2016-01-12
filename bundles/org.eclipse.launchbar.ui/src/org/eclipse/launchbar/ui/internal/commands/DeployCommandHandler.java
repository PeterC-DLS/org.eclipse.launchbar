/*******************************************************************************
 * Copyright (c) 2016 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Torkild U. Resheim - initial API and implementation
 *******************************************************************************/
package org.eclipse.launchbar.ui.internal.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.debug.core.ILaunchMode;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.build.site.QualifierReplacer;
import org.eclipse.pde.internal.core.ICoreConstants;
import org.eclipse.pde.internal.core.PDECoreMessages;
import org.eclipse.pde.internal.core.WorkspaceModelManager;
import org.eclipse.pde.internal.core.exports.FeatureExportInfo;
import org.eclipse.pde.internal.core.exports.PluginExportOperation;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.pde.internal.ui.build.RuntimeInstallJob;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressConstants;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.launchbar.ui.internal.Activator;

/**
 * Command to build and install all <b>open</b> Eclipse plug-in projects into
 * the running Eclipse instance. PDE build will be used for the operation.
 * 
 * @author Torkild U. Resheim
 */
@SuppressWarnings("restriction")
public class DeployCommandHandler extends AbstractHandler {

	protected class AntErrorDialog extends MessageDialog {
		private File fLogLocation;

		public AntErrorDialog(File logLocation) {
			super(PlatformUI.getWorkbench().getDisplay().getActiveShell(), PDECoreMessages.FeatureBasedExportOperation_ProblemDuringExport, null, null, MessageDialog.ERROR, new String[] {IDialogConstants.OK_LABEL}, 0);
			fLogLocation = logLocation;
		}

		protected Control createMessageArea(Composite composite) {
			Link link = new Link(composite, SWT.WRAP);
			try {
				link.setText(NLS.bind(PDEUIMessages.PluginExportWizard_Ant_errors_during_export_logs_generated, "<a>" + fLogLocation.getCanonicalPath() + "</a>")); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (IOException e) {
				Activator.log(e);
			}
			GridData data = new GridData();
			data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
			link.setLayoutData(data);
			link.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					try {
						Program.launch(fLogLocation.getCanonicalPath());
					} catch (IOException ex) {
						Activator.log(ex);
					}
				}
			});
			return link;
		}
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new UIJob(Display.getDefault(), "Installing plug-ins into running Eclipse instance") {
			public IStatus runInUIThread(IProgressMonitor monitor) {
				try {
					scheduleExportJob();
				} catch (IOException e) {
					return new Status(IStatus.ERROR, "", "Could not install plug-in",e);
				}
				return Status.OK_STATUS;
			};
		}.schedule();

		return Status.OK_STATUS;
	}

	protected String getMode(ILaunchMode launchMode) {
		return launchMode.getIdentifier(); //$NON-NLS-1$
	}
	
	protected boolean isValidModel(IModel model) {
		return model != null && model instanceof IPluginModelBase;
	}
	private boolean hasBuildProperties(IPluginModelBase model) {
		File file = new File(model.getInstallLocation(), ICoreConstants.BUILD_FILENAME_DESCRIPTOR);
		return file.exists();
	}
	public Object[] getListElements() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		ArrayList<IModel> result = new ArrayList<IModel>();
		for (int i = 0; i < projects.length; i++) {
			if (!WorkspaceModelManager.isBinaryProject(projects[i]) && WorkspaceModelManager.isPluginProject(projects[i])) {
				IModel model = PluginRegistry.findModel(projects[i]);
				if (model != null && isValidModel(model) && hasBuildProperties((IPluginModelBase) model)) {
					result.add(model);
				}
			}
		}
		return result.toArray();
	}
	
	protected void scheduleExportJob() throws IOException {
		
		Path folder = Files.createTempDirectory("eclipse-export", new FileAttribute<?>[0]);
		final FeatureExportInfo info = new FeatureExportInfo();
		info.toDirectory = true; // in order to install from the repository
		info.useJarFormat = true; 
		info.exportSource = false; 
		info.exportSourceBundle = false; 
		info.allowBinaryCycles = true; 
		info.useWorkspaceCompiledClasses = false; 
		info.destinationDirectory = folder.toString();
		info.zipFileName = "dawn-plugin-deployment.zip"; 
		info.items = getListElements(); // all open plug-in projects
		info.signingInfo = null; // 
		info.qualifier = QualifierReplacer.getDateQualifier(); 

		final boolean installAfterExport = true; 
		if (installAfterExport) {
			RuntimeInstallJob.modifyInfoForInstall(info);
		}

		final PluginExportOperation job = new PluginExportOperation(info, PDEUIMessages.PluginExportJob_name);
		job.setUser(true);
		job.setRule(ResourcesPlugin.getWorkspace().getRoot());
		job.setProperty(IProgressConstants.ICON_PROPERTY, PDEPluginImages.DESC_PLUGIN_OBJ);
		job.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				if (job.hasAntErrors()) {
					// If there were errors when running the ant scripts, inform the user where the logs can be found.
					final File logLocation = new File(info.destinationDirectory, "logs.zip"); //$NON-NLS-1$
					if (logLocation.exists()) {
						PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
							public void run() {
								AntErrorDialog dialog = new AntErrorDialog(logLocation);
								dialog.open();
							}
						});
					}
				} else if (event.getResult().isOK() && installAfterExport) {
					// install the export into the current running platform
					RuntimeInstallJob installJob = new RuntimeInstallJob(PDEUIMessages.PluginExportWizard_InstallJobName, info);
					installJob.setUser(true);
					installJob.setProperty(IProgressConstants.ICON_PROPERTY, PDEPluginImages.DESC_FEATURE_OBJ);
					installJob.schedule();
				}
			}
		});
		job.schedule();
	}
}
