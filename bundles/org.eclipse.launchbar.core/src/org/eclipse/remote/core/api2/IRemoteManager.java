package org.eclipse.remote.core.api2;

import java.net.URI;
import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;

public interface IRemoteManager {

	/**
	 * Retrieve the local services provider. Guaranteed to exist and be initialized.
	 * 
	 * @return local services provider
	 */
	IRemoteServices getLocalServices();

	/**
	 * Get the remote service implementation identified by id and ensure that it is initialized.
	 * 
	 * @param id
	 *            id of the remote service
	 * @return remote service or null if the service cannot be found or failed to initialized
	 */
	IRemoteServices getRemoteServices(String id);

	/**
	 * Get the remote service implementation identified by id and ensure that it is initialized. This method will present the user
	 * with a dialog box that can be canceled.
	 * 
	 * @param id
	 *            id of remote service to retrieve
	 * @param monitor
	 *            progress monitor to allow user to cancel operation
	 * @return remote service, or null if the service cannot be found or failed to initialized
	 */
	IRemoteServices getRemoteServices(String id, IProgressMonitor monitor);

	/**
	 * Get the remote services identified by a URI.
	 * 
	 * @param uri
	 *            URI of remote services to retrieve
	 * @return remote service, or null if the service cannot be found or failed to initialized
	 */
	IRemoteServices getRemoteServices(URI uri);

	/**
	 * Get the remote services implementation identified by URI. This method will present the user
	 * with a dialog box that can be canceled.
	 * 
	 * @param uri
	 *            URI of remote services to retrieve
	 * @param monitor
	 *            progress monitor to allow user to cancel operation
	 * @return remote service, or null if the service cannot be found or failed to initialized
	 */
	IRemoteServices getRemoteServices(URI uri, IProgressMonitor monitor);

	/**
	 * Return the collection of all remote services.
	 * 
	 * @return all remote services
	 */
	Collection<IRemoteServices> getAllRemoteServices();

	/**
	 * Return all connections.
	 * 
	 * @return all connections
	 */
	Collection<IRemoteConnection> getAllRemoteConnections();

}
