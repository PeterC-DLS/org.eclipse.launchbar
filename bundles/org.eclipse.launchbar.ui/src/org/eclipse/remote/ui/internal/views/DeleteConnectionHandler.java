package org.eclipse.remote.ui.internal.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.launchbar.core.internal.Activator;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionManager;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class DeleteConnectionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection != null && selection instanceof IStructuredSelection) {
			// Get the managable connections from the selection
			List<IRemoteConnection> connections = new ArrayList<IRemoteConnection>();
			@SuppressWarnings("unchecked")
			Iterator<Object> i = ((IStructuredSelection) selection).iterator();
			while (i.hasNext()) {
				Object obj = i.next();
				if (obj instanceof IRemoteConnection) {
					IRemoteConnection connection = (IRemoteConnection)obj;
					IRemoteServices services = connection.getRemoteServices();
					if (!services.isAutoPopulated()) {
						connections.add(connection);
					}
				}
			}

			// Confirm the delete
			String message = "Delete connection";
			for (IRemoteConnection connection : connections) {
				message += " " + connection.getName(); //$NON-NLS-1
			}
			message += "?"; //$NON-NLS-1
			if (MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Delete Connection", message)) {
				for (IRemoteConnection connection : connections) {
					IRemoteServices services = connection.getRemoteServices();
					IRemoteConnectionManager manager = services.getService(IRemoteConnectionManager.class);
					if (manager != null) {
						try {
							manager.removeConnection(connection);
						} catch (RemoteConnectionException e) {
							Activator.log(e.getStatus());
						}
					}
				}
			}
		}
		return Status.OK_STATUS;
	}

}
