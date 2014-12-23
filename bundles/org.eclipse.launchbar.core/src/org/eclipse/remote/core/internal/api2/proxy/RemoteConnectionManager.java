package org.eclipse.remote.core.internal.api2.proxy;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.remote.core.IUserAuthenticator;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionManager;
import org.eclipse.remote.core.api2.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.core.exception.RemoteConnectionException;

public class RemoteConnectionManager implements IRemoteConnectionManager {

	private final RemoteServices services;
	final org.eclipse.remote.core.IRemoteConnectionManager manager;

	public RemoteConnectionManager(RemoteServices services, org.eclipse.remote.core.IRemoteConnectionManager manager) {
		this.services = services;
		this.manager = manager;
	}
	
	@Override
	public IRemoteConnection getConnection(String name) {
		return new RemoteConnection(manager.getConnection(name));
	}

	@Override
	public IRemoteConnection getConnection(URI uri) {
		return new RemoteConnection(manager.getConnection(uri));
	}

	@Override
	public List<IRemoteConnection> getConnections() {
		List<IRemoteConnection> connections = new ArrayList<>();
		for (org.eclipse.remote.core.IRemoteConnection c : manager.getConnections()) {
			connections.add(new RemoteConnection(c));
		}
		return connections;
	}

	@Override
	public IUserAuthenticator getUserAuthenticator(IRemoteConnection connection) {
		return manager.getUserAuthenticator(((RemoteConnection) connection).connection);
	}

	@Override
	public IRemoteConnectionWorkingCopy newConnection(String name) throws RemoteConnectionException {
		return new RemoteConnectionWorkingCopy(manager.newConnection(name));
	}

	@Override
	public void removeConnection(IRemoteConnection connection) throws RemoteConnectionException {
		manager.removeConnection(((RemoteConnection) connection).connection);
	}

	@Override
	public IRemoteServices getRemoteServices() {
		return services;
	}

	@Override
	public boolean isAutoPopulated() {
		// Only return it if we can
		int caps = services.remoteServices.getCapabilities();
		return (caps & (
				org.eclipse.remote.core.IRemoteServices.CAPABILITY_ADD_CONNECTIONS |
				org.eclipse.remote.core.IRemoteServices.CAPABILITY_REMOVE_CONNECTIONS)) == 0;
	}

}
