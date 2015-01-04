package org.eclipse.remote.core.api2;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.launchbar.core.internal.Activator;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.eclipse.remote.core.internal.api2.RemoteManager;

public abstract class AbstractRemoteConnectionManager extends PlatformObject
implements IRemoteConnectionManager, IRemoteConnectionChangeListener {

	private final IRemoteServices remoteServices;
	private Map<String, IRemoteConnection> connections = new HashMap<>();

	protected AbstractRemoteConnectionManager(IRemoteServices remoteServices) {
		this.remoteServices = remoteServices;
		remoteServices.getManager().addRemoteConnectionChangeListener(this);
	}

	@Override
	public IRemoteServices getRemoteServices() {
		return remoteServices;
	}

	@Override
	public IRemoteConnection getConnection(String name) {
		return connections.get(name);
	}

	@Override
	public IRemoteConnection getConnection(URI uri) {
		return getConnection(uri.getHost());
	}

	@Override
	public Collection<IRemoteConnection> getConnections() {
		return connections.values();
	}

	void connectionAdded(IRemoteConnection connection) {
		connections.put(connection.getName(), connection);
	}

	@Override
	public void removeConnection(IRemoteConnection connection) throws RemoteConnectionException {
		connection.fireConnectionChangeEvent(IRemoteConnectionChangeEvent.CONNECTION_REMOVED);
		connections.remove(connection.getName());
	}
	
	@Override
	public void connectionChanged(IRemoteConnectionChangeEvent event) {
		IRemoteConnection connection = event.getConnection();
		
		switch (event.getType()) {
		case IRemoteConnectionChangeEvent.CONNECTION_RENAMED:
		case IRemoteConnectionChangeEvent.CONNECTION_REMOVED:
			connections.remove(connection.getName());
			// Remove the old property file
			IPath stateLoc = Activator.getDefault().getStateLocation();
			IPath connsLoc = stateLoc.append(RemoteManager.CONNECTIONS_DIR);
			File typeDir = connsLoc.append(connection.getRemoteServices().getId()).toFile();
			File propFile = new File(typeDir, connection.getName());
			propFile.delete();
			break;
		case IRemoteConnectionChangeEvent.CONNECTION_ADDED:
			if (!connections.containsKey(connection.getName()))
				connections.put(connection.getName(), connection);
			break;
		}
	}
	
}
