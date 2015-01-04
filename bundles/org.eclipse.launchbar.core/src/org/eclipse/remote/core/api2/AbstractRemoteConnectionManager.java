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

	private final Map<String, IRemoteConnection> connections = new HashMap<>();

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
		connections.remove(connection.getName());
	}
	
	@Override
	public void connectionChanged(IRemoteConnectionChangeEvent event) {
		if (event.getType() == IRemoteConnectionChangeEvent.CONNECTION_RENAMED) {
			IRemoteConnection connection = event.getConnection(); // this is the original
			connections.remove(connection.getName());
			// Remove the old property file
			IPath stateLoc = Activator.getDefault().getStateLocation();
			IPath connsLoc = stateLoc.append(RemoteManager.CONNECTIONS_DIR);
			File typeDir = connsLoc.append(connection.getRemoteServices().getId()).toFile();
			File propFile = new File(typeDir, connection.getName());
			propFile.delete();
		}
	}
	
}
