package org.eclipse.remote.core.internal.api2.proxy;

import org.eclipse.remote.core.IRemoteConnectionChangeEvent;
import org.eclipse.remote.core.api2.IRemoteConnectionChangeListener;

public class RemoteConnectionChangeListener implements org.eclipse.remote.core.IRemoteConnectionChangeListener {

	final IRemoteConnectionChangeListener listener;
	
	public RemoteConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void connectionChanged(IRemoteConnectionChangeEvent event) {
		listener.connectionChanged(new RemoteConnectionChangeEvent(event));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RemoteConnectionChangeListener) {
			return listener.equals(((RemoteConnectionChangeListener) obj).listener);
		} else {
			return super.equals(obj);
		}
	}
	
}
