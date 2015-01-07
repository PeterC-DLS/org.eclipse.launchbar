package org.eclipse.remote.ui.internal.terminal;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.swt.SWT;
import org.eclipse.tm.internal.terminal.provisional.api.ISettingsStore;
import org.eclipse.tm.internal.terminal.provisional.api.ITerminalConnector;
import org.eclipse.tm.internal.terminal.provisional.api.TerminalConnectorExtension;
import org.eclipse.tm.internal.terminal.view.ITerminalView;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

@SuppressWarnings("restriction")
public class OpenTerminalHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		ISelection selection = page.getSelection();
		if (selection != null && selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			Object obj = ss.getFirstElement();
			if (obj instanceof IRemoteConnection) {
				try {
					final IRemoteConnection connection = (IRemoteConnection) obj;
					IViewPart newTerminalView = page.showView(
							"org.eclipse.tm.terminal.view.TerminalView",//$NON-NLS-1$
							"SecondaryTerminal" + System.currentTimeMillis(), //$NON-NLS-1$
							IWorkbenchPage.VIEW_ACTIVATE);
					if (newTerminalView instanceof ITerminalView) {
						ITerminalConnector connector = TerminalConnectorExtension.makeTerminalConnector(RemoteTerminalConnector.ID);
						connector.load(new ISettingsStore() {
							@Override
							public String get(String key) {
								if (RemoteTerminalConnector.PROP_REMOTE_NAME.equals(key)) {
									return connection.getName();
								} else if (RemoteTerminalConnector.PROP_REMOTE_SERVICES.equals(key)) {
									return connection.getRemoteServices().getId();
								}
								return null;
							}
							@Override
							public String get(String key, String defaultValue) {
								return get(key);
							}
							@Override
							public void put(String key, String value) {
							}
						});
						((ITerminalView) newTerminalView).newTerminal(connector);
					}
				} catch (PartInitException e) {
					MessageDialog.open(MessageDialog.ERROR,
							window.getShell(),
							"Open Terminal",
							e.getLocalizedMessage(), SWT.NONE);
					return Status.CANCEL_STATUS;
				}
			}
		}
		return Status.OK_STATUS;
	}

}
