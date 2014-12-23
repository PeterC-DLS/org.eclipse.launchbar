package org.eclipse.remote.core.internal.api2.proxy;

import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionChangeEvent;

public class RemoteConnectionChangeEvent implements IRemoteConnectionChangeEvent {

	final org.eclipse.remote.core.IRemoteConnectionChangeEvent event;
	
	public RemoteConnectionChangeEvent(org.eclipse.remote.core.IRemoteConnectionChangeEvent event) {
		this.event = event;
	}
	
	@Override
	public IRemoteConnection getConnection() {
		return new RemoteConnection(event.getConnection());
	}

	@Override
	public int getType() {
		return event.getType();
	}

}
