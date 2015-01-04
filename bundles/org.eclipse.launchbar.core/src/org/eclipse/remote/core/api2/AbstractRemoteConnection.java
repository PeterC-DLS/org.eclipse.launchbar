package org.eclipse.remote.core.api2;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.remote.core.internal.api2.RemoteConnectionChangeEvent;

public abstract class AbstractRemoteConnection extends PlatformObject implements IRemoteConnection {

	private final IRemoteServices remoteServices;
	private final String name;
	private final Map<String, String> attributes;
	private Map<Class<?>, Object> services;
	private List<IRemoteConnectionChangeListener> listeners = new LinkedList<>();

	protected AbstractRemoteConnection(IRemoteServices remoteServices, String name, Properties properties) {
		this.remoteServices = remoteServices;
		this.name = name;
		this.attributes = new HashMap<>();

		// Load the attributes from the properties
		Enumeration<?> propNames = properties.propertyNames();
		while (propNames.hasMoreElements()) {
			String propName = (String)propNames.nextElement();
			String value = properties.getProperty(propName);
			attributes.put(propName, value);
		}
		fireConnectionChangeEvent(IRemoteConnectionChangeEvent.CONNECTION_ADDED);
	}

	protected AbstractRemoteConnection(AbstractRemoteConnectionWorkingCopy workingCopy) {
		remoteServices = workingCopy.getRemoteServices();
		name = workingCopy.getName();
		attributes = workingCopy.getAttributes();
		fireConnectionChangeEvent(IRemoteConnectionChangeEvent.CONNECTION_ADDED);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IRemoteServices getRemoteServices() {
		return remoteServices;
	}

	@Override
	public Map<String, String> getAttributes() {
		return attributes;
	}

	@Override
	public void addConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void fireConnectionChangeEvent(int type) {
		IRemoteConnectionChangeEvent event = new RemoteConnectionChangeEvent(this, type);
		for (IRemoteConnectionChangeListener listener : listeners) {
			listener.connectionChanged(event);
		}
		remoteServices.getManager().fireRemoteConnectionChangeEvent(event);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(Class<T> service) {
		T obj = (T)services.get(service);
		if (obj != null)
			return obj;
		
		obj = remoteServices.getService(this, service);
		if (obj != null) {
			services.put(service, obj);
			return obj;
		}

		return (T)getAdapter(service);
	}

}
