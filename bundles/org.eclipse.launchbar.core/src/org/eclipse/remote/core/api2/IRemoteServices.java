/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.remote.core.api2;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.remote.core.internal.api2.proxy.RemoteServicesProxy;

/**
 * Abstraction of a remote services provider. Clients obtain this interface using one of the static methods in
 * {@link RemoteServicesProxy}. The methods on this interface can then be used to access the full range of remote services provided.
 * 
 * @noimplement register services to provide features for this remote type
 */
public interface IRemoteServices extends IAdaptable {

	/**
	 * Get the remote manager, the root object of the remote framework
	 * 
	 * @return remote manager
	 */
	IRemoteManager getManager();

	/**
	 * Get unique ID of this service. Can be used as a lookup key.
	 * 
	 * @return unique ID
	 */
	String getId();

	/**
	 * Get display name of this service.
	 * 
	 * @return display name
	 */
	String getName();

	/**
	 * Get the EFS scheme provided by this service.
	 * 
	 * @return display name
	 */
	String getScheme();

	interface Service {
		IRemoteServices getRemoteServices();
		
		interface Factory {
			Service getService(IRemoteServices remoteServices);
		}
	}

	/**
	 * Return one of the remote services that this provider provides.
	 * 
	 * @param service interface
	 * @return the service
	 */
	<T extends Service> T getService(Class<T> service);

	/**
	 * Return a remote service associated with the connection.
	 * 
	 * @param connection
	 * @param service
	 * @return the connection service
	 */
	<T extends IRemoteConnection.Service> T getService(IRemoteConnection connection, Class<T> service);

	/**
	 * Are connections with this service auto populated. If so, the connection manager
	 * can not be used to add or remove connections.
	 * 
	 * @return isAutoPopulated
	 */
	boolean isAutoPopulated();

}
