package org.eclipse.remote.core.internal.api2.proxy;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionManager;
import org.eclipse.remote.core.api2.IRemoteManager;
import org.eclipse.remote.core.api2.IRemoteManagerService;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.internal.core.RemoteCorePlugin;

public class RemoteManager extends PlatformObject implements IRemoteManager {

	private List<IRemoteServices> allServices;
	
	@Override
	public IRemoteServices getLocalServices() {
		return new RemoteServices(org.eclipse.remote.core.RemoteServices.getLocalServices());
	}

	@Override
	public IRemoteServices getRemoteServices(String id) {
		return new RemoteServices(org.eclipse.remote.core.RemoteServices.getRemoteServices(id));
	}

	@Override
	public IRemoteServices getRemoteServices(String id, IProgressMonitor monitor) {
		return new RemoteServices(org.eclipse.remote.core.RemoteServices.getRemoteServices(id, monitor));
	}

	@Override
	public IRemoteServices getRemoteServices(URI uri) {
		return new RemoteServices(org.eclipse.remote.core.RemoteServices.getRemoteServices(uri));
	}

	@Override
	public IRemoteServices getRemoteServices(URI uri, IProgressMonitor monitor) {
		return new RemoteServices(org.eclipse.remote.core.RemoteServices.getRemoteServices(uri, monitor));
	}

	@Override
	public Collection<IRemoteServices> getAllRemoteServices() {
		if (allServices == null) {
			allServices = new ArrayList<>();

			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry.getExtensionPoint(
					RemoteCorePlugin.getUniqueIdentifier(),
					"remoteServices");

			for (IExtension ext : extensionPoint.getExtensions()) {
				for (IConfigurationElement ce : ext.getConfigurationElements()) {
					String id = ce.getAttribute("id");
					IRemoteServices services = getRemoteServices(id);
					if (services != null) {
						allServices.add(services);
					}
				}
			}
		}
		return allServices;
	}

	@Override
	public Collection<IRemoteConnection> getAllRemoteConnections() {
		List<IRemoteConnection> connections = new ArrayList<>();
		for (IRemoteServices services : getAllRemoteServices()) {
			IRemoteConnectionManager manager = services.getService(IRemoteConnectionManager.class);
			if (manager != null) {
				connections.addAll(manager.getConnections());
			}
		}
		return connections;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IRemoteManagerService> T getService(Class<T> service) {
		return (T)getAdapter(service);
	}

}
