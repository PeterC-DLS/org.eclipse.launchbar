package org.eclipse.remote.core.internal.api2.proxy;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionManager;
import org.eclipse.remote.core.api2.IRemoteManager;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.core.internal.api2.RemoteManager;

public class RemoteServicesProxy extends PlatformObject implements IRemoteServices {

	private final RemoteManager manager;
	public final org.eclipse.remote.core.IRemoteServices remoteServices;
	
	public RemoteServicesProxy(RemoteManager manager, org.eclipse.remote.core.IRemoteServices remoteServices) {
		this.manager = manager;
		this.remoteServices = remoteServices;
	}

	@Override
	public IRemoteManager getManager() {
		return manager;
	}
	
	@Override
	public String getId() {
		return remoteServices.getId();
	}

	@Override
	public String getName() {
		return remoteServices.getName();
	}

	@Override
	public String getScheme() {
		return remoteServices.getScheme();
	}

	@Override
	public boolean isAutoPopulated() {
		return (remoteServices.getCapabilities() & (
				org.eclipse.remote.core.IRemoteServices.CAPABILITY_ADD_CONNECTIONS |
				org.eclipse.remote.core.IRemoteServices.CAPABILITY_REMOVE_CONNECTIONS)) == 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(Class<T> service) {
		if (IRemoteConnectionManager.class.equals(service)) {
			return (T)new RemoteConnectionManagerProxy(this, remoteServices.getConnectionManager());
		} else {
			return (T)getAdapter(service);
		}
	}

	@Override
	public <T extends IRemoteConnection.Service> T getService(IRemoteConnection connection, Class<T> service) {
		return null;
	}

}
