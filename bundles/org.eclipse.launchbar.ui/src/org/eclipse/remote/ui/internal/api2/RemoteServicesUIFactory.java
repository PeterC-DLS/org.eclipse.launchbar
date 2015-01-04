package org.eclipse.remote.ui.internal.api2;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.remote.core.internal.api2.proxy.RemoteServicesProxy;
import org.eclipse.remote.ui.api2.IRemoteServicesUI;

public class RemoteServicesUIFactory implements IAdapterFactory {

	@Override
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof RemoteServicesProxy)
			return new RemoteServicesUI((RemoteServicesProxy) adaptableObject);
		else
			return null;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Class[] getAdapterList() {
		return new Class[] { IRemoteServicesUI.class };
	}

}
