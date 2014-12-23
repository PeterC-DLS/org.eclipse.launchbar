package org.eclipse.remote.core.internal.api2.proxy;

import org.eclipse.remote.core.api2.IRemoteServicesDescriptor;

public class RemoteServicesDescriptor implements IRemoteServicesDescriptor {

	final org.eclipse.remote.core.IRemoteServicesDescriptor remoteServicesDescriptor;

	public RemoteServicesDescriptor(org.eclipse.remote.core.IRemoteServicesDescriptor remoteServicesDescriptor) {
		this.remoteServicesDescriptor = remoteServicesDescriptor;
	}
	
	@Override
	public int compareTo(IRemoteServicesDescriptor o) {
		return remoteServicesDescriptor.compareTo(((RemoteServicesDescriptor) o).remoteServicesDescriptor);
	}

	@Override
	public String getId() {
		return remoteServicesDescriptor.getId();
	}

	@Override
	public String getName() {
		return remoteServicesDescriptor.getName();
	}

	@Override
	public String getScheme() {
		return remoteServicesDescriptor.getScheme();
	}

}
