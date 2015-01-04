package org.eclipse.remote.core.internal.api2;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.launchbar.core.internal.Activator;
import org.eclipse.remote.core.RemoteServices;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionChangeEvent;
import org.eclipse.remote.core.api2.IRemoteConnectionChangeListener;
import org.eclipse.remote.core.api2.IRemoteConnectionManager;
import org.eclipse.remote.core.api2.IRemoteManager;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.eclipse.remote.core.internal.api2.proxy.RemoteServicesProxy;

public class RemoteManager implements IRemoteManager {

	public static final String CONNECTIONS_DIR = "connections";

	// From id to services
	private Map<String, IRemoteServices> remoteServicesMap;
	// From scheme to service
	private Map<String, IRemoteServices> remoteServicesSchemeMap;
	// Services map from id to interface name to IConfigurationElement
	private Map<String, Map<String, IConfigurationElement>> servicesMap;
	
	private List<IRemoteConnectionChangeListener> listeners = new LinkedList<>();
	private List<IRemoteConnection> connections = new LinkedList<>();

	public synchronized void init() {
		if (remoteServicesMap != null)
			return;

		remoteServicesMap = new HashMap<>();
		remoteServicesSchemeMap = new HashMap<>();
		servicesMap = new HashMap<>();
		
		IExtensionRegistry registry = Platform.getExtensionRegistry();

		// Load up proxies for the old remote services
		IExtensionPoint extensionPoint = registry.getExtensionPoint("org.eclipse.remote.core", "remoteServices");

		for (IExtension ext : extensionPoint.getExtensions()) {
			for (IConfigurationElement ce : ext.getConfigurationElements()) {
				if (ce.getName().equals("remoteServices")) {
					String id = ce.getAttribute("id");
					String scheme = ce.getAttribute("scheme");
					org.eclipse.remote.core.IRemoteServices oldServices = RemoteServices.getRemoteServices(id);
					if (oldServices != null) {
						IRemoteServices proxy = new RemoteServicesProxy(this, oldServices);
						remoteServicesMap.put(id, proxy);
						if (scheme != null) {
							remoteServicesSchemeMap.put(scheme, proxy);
						}
						connections.addAll(proxy.getService(IRemoteConnectionManager.class).getConnections());
					}
				}
			}
		}

		// Now load up the new remote services
		extensionPoint = registry.getExtensionPoint(Activator.PLUGIN_ID, "remoteServices2");

		// load up the service factories to pass later to the remote services objects
		for (IExtension ext : extensionPoint.getExtensions()) {
			for (IConfigurationElement ce : ext.getConfigurationElements()) {
				String name = ce.getName();
				if (name.equals("service") || name.equals("connectionService")) {
					String id = ce.getAttribute("remoteServicesId");
					String service = ce.getAttribute("service");
					
					Map<String, IConfigurationElement> ceMap = servicesMap.get(id);
					if (ceMap == null) {
						ceMap = new HashMap<>();
						servicesMap.put(id, ceMap);
					}
					
					ceMap.put(service, ce);
				}
			}
		}

		for (IExtension ext : extensionPoint.getExtensions()) {
			for (IConfigurationElement ce : ext.getConfigurationElements()) {
				String name = ce.getName();
				if (name.equals("remoteServices")) {
					IRemoteServices remoteServices = new RemoteServicesImpl(this, ce);
					remoteServicesMap.put(remoteServices.getId(), remoteServices);
					if (remoteServices.getScheme() != null) {
						remoteServicesSchemeMap.put(remoteServices.getScheme(), remoteServices);
					}
				}
			}
		}

		// Load up saved connections
		File stateDir = Activator.getDefault().getStateLocation().toFile();
		if (stateDir.isDirectory()) {
			File connsDir = new File(stateDir, CONNECTIONS_DIR);
			if (connsDir.isDirectory()) {
				for (File typeDir : connsDir.listFiles()) {
					IRemoteServices remoteServices = getRemoteServices(typeDir.getName());
					if (remoteServices != null) {
						IRemoteConnectionManager connectionManager = remoteServices.getService(IRemoteConnectionManager.class);
						for (File propsFile : typeDir.listFiles()) {
							Properties props = new Properties();
							Reader reader = null;
							try {
								reader = new FileReader(propsFile);
								props.load(reader);
								connections.add(connectionManager.loadConnection(propsFile.getName(), props));
							} catch (IOException e) {
								Activator.log(e);
							} catch (RemoteConnectionException e) {
								Activator.log(e.getStatus());
							} finally {
								if (reader != null) {
									try {
										reader.close();
									} catch (IOException e) {
										Activator.log(e);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public IRemoteServices getLocalServices() {
		init();
		String localServicesId = "org.eclipse.remote.LocalServices";
		return remoteServicesMap.get(localServicesId);
	}

	@Override
	public IRemoteServices getRemoteServices(String id) {
		init();
		return remoteServicesMap.get(id);
	}

	@Override
	public IRemoteServices getRemoteServices(String id, IProgressMonitor monitor) {
		return getRemoteServices(id);
	}

	@Override
	public IRemoteServices getRemoteServices(URI uri) {
		init();
		return remoteServicesSchemeMap.get(uri.getScheme());
	}

	@Override
	public IRemoteServices getRemoteServices(URI uri, IProgressMonitor monitor) {
		return getRemoteServices(uri);
	}

	@Override
	public Collection<IRemoteServices> getAllRemoteServices() {
		init();
		return remoteServicesMap.values();
	}

	@Override
	public Collection<IRemoteConnection> getAllRemoteConnections() {
		return connections;
	}

	@SuppressWarnings("unchecked")
	public <T extends IRemoteServices.Service> T getService(IRemoteServices remoteServices, Class<T> service) {
		Map<String, IConfigurationElement> elementMap = servicesMap.get(remoteServices.getId());
		if (elementMap == null)
			return null;
		IConfigurationElement fe = elementMap.get(service.getName());
		if (fe == null)
			return null;
		try {
			IRemoteServices.Service.Factory factory = (IRemoteServices.Service.Factory)
					fe.createExecutableExtension("factory");
			return (T)factory.getService(remoteServices);
		} catch (CoreException e) {
			Activator.log(e.getStatus());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends IRemoteConnection.Service> T getService(IRemoteConnection connection, Class<T> service) {
		IRemoteServices remoteServices = connection.getRemoteServices();
		Map<String, IConfigurationElement> elementMap = servicesMap.get(remoteServices.getId());
		if (elementMap == null)
			return null;
		IConfigurationElement fe = elementMap.get(service.getName());
		if (fe == null)
			return null;
		try {
			IRemoteConnection.Service.Factory factory = (IRemoteConnection.Service.Factory)
					fe.createExecutableExtension("factory");
			return (T)factory.getService(connection);
		} catch (CoreException e) {
			Activator.log(e.getStatus());
		}
		return null;
	}

	@Override
	public void addRemoteConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeRemoteConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void fireRemoteConnectionChangeEvent(IRemoteConnectionChangeEvent event) {
		IRemoteConnection connection = event.getConnection();
		// Manage list of connections
		switch (event.getType()) {
		case IRemoteConnectionChangeEvent.CONNECTION_ADDED:
			if (!connections.contains(connection)) {
				connections.add(connection);
			}
			break;
		case IRemoteConnectionChangeEvent.CONNECTION_REMOVED:
			connections.remove(connection);
			break;
		}
		
		// pass on to the listeners
		for (IRemoteConnectionChangeListener listener : listeners) {
			listener.connectionChanged(event);
		}
	}

}
