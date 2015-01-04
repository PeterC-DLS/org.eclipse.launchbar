package org.eclipse.remote.core.internal.api2.proxy;

import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionChangeEvent;

public class RemoteConnectionChangeEventProxy implements IRemoteConnectionChangeEvent {

	private final RemoteServicesProxy services;
	final org.eclipse.remote.core.IRemoteConnectionChangeEvent event;
	
	public RemoteConnectionChangeEventProxy(RemoteServicesProxy services,
			org.eclipse.remote.core.IRemoteConnectionChangeEvent event) {
		this.services = services;
		this.event = event;
	}
	
	@Override
	public IRemoteConnection getConnection() {
		return new RemoteConnectionProxy(services, event.getConnection());
	}

	@Override
	public int getType() {
		return event.getType();
	}

}
