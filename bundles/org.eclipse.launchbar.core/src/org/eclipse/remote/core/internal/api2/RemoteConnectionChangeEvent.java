package org.eclipse.remote.core.internal.api2;

import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionChangeEvent;

public class RemoteConnectionChangeEvent implements IRemoteConnectionChangeEvent {

	private final IRemoteConnection connection;
	private final int type;

	public RemoteConnectionChangeEvent(IRemoteConnection connection, int type) {
		this.connection = connection;
		this.type = type;
	}

	@Override
	public IRemoteConnection getConnection() {
		return connection;
	}

	@Override
	public int getType() {
		return type;
	}

}
