package org.eclipse.remote.core.internal.api2;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteManager;
import org.eclipse.remote.core.api2.IRemoteServices;

public class RemoteServicesImpl extends PlatformObject implements IRemoteServices {

	private final RemoteManager manager;
	private final String id;
	private final String name;
	private final String scheme;
	private final boolean isAutoPopulated;
	private Map<Class<?>, Object> services = new HashMap<>();

	public RemoteServicesImpl(RemoteManager manager, IConfigurationElement ce) {
		this.manager = manager;
		this.id = ce.getAttribute("id");
		this.name = ce.getAttribute("name");
		this.scheme = ce.getAttribute("scheme");
		String isAutoPopulatedStr = ce.getAttribute("isAutoPopulated");
		this.isAutoPopulated = isAutoPopulatedStr != null ? Boolean.valueOf(isAutoPopulatedStr) : false;
	}

	@Override
	public IRemoteManager getManager() {
		return manager;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getScheme() {
		return scheme;
	}

	@Override
	public boolean isAutoPopulated() {
		return isAutoPopulated;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(Class<T> service) {
		T obj = (T)services.get(service);
		if (obj != null)
			return obj;

		obj = manager.getService(this, service);
		if (obj != null) {
			services.put(service, obj);
			return obj;
		}

		return (T)getAdapter(service);
	}

	@Override
	public <T extends IRemoteConnection.Service> T getService(IRemoteConnection connection, Class<T> service) {
		// The connection should cache this so we don't
		return (T)manager.getService(connection, service);
	}

}
