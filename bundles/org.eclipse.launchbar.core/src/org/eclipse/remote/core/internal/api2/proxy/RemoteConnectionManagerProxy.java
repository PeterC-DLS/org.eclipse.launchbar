package org.eclipse.remote.core.internal.api2.proxy;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.remote.core.IUserAuthenticator;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionManager;
import org.eclipse.remote.core.api2.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.core.exception.RemoteConnectionException;

public class RemoteConnectionManagerProxy implements IRemoteConnectionManager {

	private final RemoteServicesProxy services;
	final org.eclipse.remote.core.IRemoteConnectionManager manager;

	public RemoteConnectionManagerProxy(RemoteServicesProxy services, org.eclipse.remote.core.IRemoteConnectionManager manager) {
		this.services = services;
		this.manager = manager;
	}
	
	@Override
	public IRemoteConnection getConnection(String name) {
		return new RemoteConnectionProxy(services, manager.getConnection(name));
	}

	@Override
	public IRemoteConnection getConnection(URI uri) {
		return new RemoteConnectionProxy(services, manager.getConnection(uri));
	}

	@Override
	public List<IRemoteConnection> getConnections() {
		List<IRemoteConnection> connections = new ArrayList<>();
		for (org.eclipse.remote.core.IRemoteConnection c : manager.getConnections()) {
			connections.add(new RemoteConnectionProxy(services, c));
		}
		return connections;
	}

	@Override
	public IUserAuthenticator getUserAuthenticator(IRemoteConnection connection) {
		return manager.getUserAuthenticator(((RemoteConnectionProxy) connection).connection);
	}

	@Override
	public IRemoteConnectionWorkingCopy newConnection(String name) throws RemoteConnectionException {
		return new RemoteConnectionWorkingCopyProxy(services, manager.newConnection(name));
	}

	@Override
	public IRemoteConnection loadConnection(String name, Properties properties) throws RemoteConnectionException {
		// This should never be called since API 1 connections have a different storage mechanism.
		return null;
	}
	
	@Override
	public void removeConnection(IRemoteConnection connection) throws RemoteConnectionException {
		manager.removeConnection(((RemoteConnectionProxy) connection).connection);
	}

	@Override
	public IRemoteServices getRemoteServices() {
		return services;
	}

}
