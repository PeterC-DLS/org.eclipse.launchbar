package org.eclipse.remote.ui.internal.api2.dialogs;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.launchbar.core.internal.Activator;
import org.eclipse.remote.core.api2.IRemoteManager;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.ui.api2.IRemoteServicesUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class NewRemoteConnectionTypePage extends WizardPage {

	private Table table;

	public NewRemoteConnectionTypePage() {
		super("NewLaunchTargetTypePage");
		setTitle("Launch Target Type");
		setDescription("Select type of launch target to create.");
	}

	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());

		table = new Table(comp, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);

		setPageComplete(false);
		
		IRemoteManager remoteManager = Activator.getService(IRemoteManager.class);
		for (IRemoteServices remoteServices : remoteManager.getAllRemoteServices()) {
			IRemoteServicesUI ui = remoteServices.getService(IRemoteServicesUI.class);
			if (ui != null) {
				IWizard wizard = ui.getNewWizard();
				if (wizard != null) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(remoteServices.getName());
					Image icon = ui.getIcon();
					if (icon != null) {
						item.setImage(icon);
					}
					item.setData(wizard);
				}
				table.select(0);
				setPageComplete(true);
			}
		}

		setControl(comp);
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}

	@Override
	public IWizardPage getNextPage() {
		IWizard nextWizard = (IWizard)table.getSelection()[0].getData();
		if (nextWizard != null) {
			nextWizard.addPages();
			IWizardPage [] pages = nextWizard.getPages();
			if (pages.length > 0) {
				return pages[0];
			}
		}

		return super.getNextPage();
	}

}
