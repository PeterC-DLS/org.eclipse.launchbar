package org.eclipse.remote.core.internal.api2.proxy;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.remote.core.api2.IRemoteConnectionManager;
import org.eclipse.remote.core.api2.IRemoteService;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.core.api2.IRemoteServicesDescriptor;

public class RemoteServices extends PlatformObject implements IRemoteServices {

	public final org.eclipse.remote.core.IRemoteServices remoteServices;
	
	public RemoteServices(org.eclipse.remote.core.IRemoteServices remoteServices) {
		this.remoteServices = remoteServices;
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
	public int compareTo(IRemoteServicesDescriptor o) {
		return remoteServices.compareTo(((RemoteServicesDescriptor) o).remoteServicesDescriptor);
	}

	@Override
	public boolean initialize(IProgressMonitor monitor) {
		return remoteServices.initialize(monitor);
	}

	@Override
	public int getCapabilities() {
		return remoteServices.getCapabilities();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IRemoteService> T getService(Class<T> service) {
		if (IRemoteConnectionManager.class.equals(service)) {
			return (T)new RemoteConnectionManager(this, remoteServices.getConnectionManager());
		} else {
			return (T)getAdapter(service);
		}
	}

}
