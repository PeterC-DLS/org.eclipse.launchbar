package org.eclipse.remote.ui.internal.api2;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.core.internal.api2.proxy.RemoteServicesProxy;
import org.eclipse.remote.ui.IRemoteUIConnectionManager;
import org.eclipse.remote.ui.IRemoteUIConnectionWizard;
import org.eclipse.remote.ui.IRemoteUIServices;
import org.eclipse.remote.ui.RemoteUIServices;
import org.eclipse.remote.ui.api2.IRemoteServicesUI;
import org.eclipse.swt.graphics.Image;

public class RemoteServicesUI implements IRemoteServicesUI {

	private final RemoteServicesProxy remoteServices;
	
	public RemoteServicesUI(RemoteServicesProxy remoteServices) {
		this.remoteServices = remoteServices;
	}
	
	@Override
	public IRemoteServices getRemoteServices() {
		return remoteServices;
	}

	@Override
	public Image getIcon() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Image getIcon(IStatus status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWizard getNewWizard() {
		IRemoteUIServices ui = RemoteUIServices.getRemoteUIServices(remoteServices.remoteServices);
		IRemoteUIConnectionManager manager = ui.getUIConnectionManager();
		if (manager != null) {
			IRemoteUIConnectionWizard wizard = manager.getConnectionWizard(null);
			if (wizard instanceof IWizard) {
				return (IWizard) wizard;
			}
		}
		return null;
	}

}
