package org.eclipse.remote.core.internal.api2.proxy;

import java.net.URI;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.remote.core.IRemoteFileManager;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteFileService;

public class RemoteFileService implements IRemoteFileService {

	private final IRemoteConnection connection;
	final IRemoteFileManager manager;
	
	public RemoteFileService(IRemoteConnection connection, IRemoteFileManager manager) {
		this.connection = connection;
		this.manager = manager;
	}
	
	@Override
	public IRemoteConnection getConnection() {
		return connection;
	}

	@Override
	public IFileStore getResource(String path) {
		return manager.getResource(path);
	}

	@Override
	public String getDirectorySeparator() {
		return manager.getDirectorySeparator();
	}

	@Override
	public String toPath(URI uri) {
		return manager.toPath(uri);
	}

	@Override
	public URI toURI(IPath path) {
		return manager.toURI(path);
	}

	@Override
	public URI toURI(String path) {
		return manager.toURI(path);
	}

}
