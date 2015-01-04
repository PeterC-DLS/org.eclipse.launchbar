package org.eclipse.remote.core.internal.api2.proxy;

import org.eclipse.remote.core.IRemoteConnectionChangeEvent;
import org.eclipse.remote.core.api2.IRemoteConnectionChangeListener;

public class RemoteConnectionChangeListenerProxy implements org.eclipse.remote.core.IRemoteConnectionChangeListener {

	private final RemoteServicesProxy services;
	final IRemoteConnectionChangeListener listener;
	
	public RemoteConnectionChangeListenerProxy(RemoteServicesProxy services, 
			IRemoteConnectionChangeListener listener) {
		this.services = services;
		this.listener = listener;
	}
	
	@Override
	public void connectionChanged(IRemoteConnectionChangeEvent event) {
		listener.connectionChanged(new RemoteConnectionChangeEventProxy(services, event));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RemoteConnectionChangeListenerProxy) {
			return listener.equals(((RemoteConnectionChangeListenerProxy) obj).listener);
		} else {
			return super.equals(obj);
		}
	}
	
}
