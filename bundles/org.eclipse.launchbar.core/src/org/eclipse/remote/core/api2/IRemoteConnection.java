/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.remote.core.api2;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.remote.core.exception.RemoteConnectionException;

/**
 * Abstraction of a connection to a remote system. Clients should use the set methods to provide information on the remote system,
 * then call the {{@link #open(IProgressMonitor)} method. Once the connection is completed, call the {@link #close()} method to
 * terminate the connection.
 */
public interface IRemoteConnection extends Comparable<IRemoteConnection> {

	// Standard properties
	public final static String OS_NAME_PROPERTY = "os.name"; //$NON-NLS-1$
	public final static String OS_VERSION_PROPERTY = "os.version"; //$NON-NLS-1$
	public final static String OS_ARCH_PROPERTY = "os.arch"; //$NON-NLS-1$
	public final static String FILE_SEPARATOR_PROPERTY = "file.separator"; //$NON-NLS-1$
	public final static String PATH_SEPARATOR_PROPERTY = "path.separator"; //$NON-NLS-1$
	public final static String LINE_SEPARATOR_PROPERTY = "line.separator"; //$NON-NLS-1$
	public final static String USER_HOME_PROPERTY = "user.home"; //$NON-NLS-1$

	// Common attributes
	public static final String USERNAME = "remote.username";
	public static final String PASSWORD = "remote.password"; // stored in secure preference store
	public static final String ADDRESS = "remote.address";
	public static final String PORT = "remote.port";

	/**
	 * Get unique name for this connection.
	 * 
	 * @return connection name
	 */
	public String getName();

	/**
	 * Return the connection status.
	 * If it's OK, the connection is open with no issues.
	 * If it's CANCEL, the connection is closed normally.
	 * If it's ERROR, the connection is closed due to some error condition.
	 * If it's INFO or WARNING, the connection is open but there may be problems with it.
	 * Get the message from the status to find out what's really happening.
	 * 
	 * @return true if connection is open.
	 */
	public IStatus getConnectionStatus();

	/**
	 * Open the connection. Must be called before the connection can be used.
	 * 
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the user. It is the caller's responsibility to call done()
	 *            on the given monitor. Accepts null, indicating that no progress should be reported and that the operation cannot
	 *            be cancelled.
	 * @throws RemoteConnectionException
	 */
	public void open(IProgressMonitor monitor) throws RemoteConnectionException;

	/**
	 * Close the connection. Must be called to terminate the connection.
	 */
	public void close();

	/**
	 * Gets the remote system property indicated by the specified key. The connection must be open prior to calling this method.
	 * 
	 * The following keys are supported:
	 * 
	 * <pre>
	 * os.name			Operating system name 
	 * os.arch			Operating system architecture
	 * os.version		Operating system version
	 * file.separator	File separator ("/" on UNIX)
	 * path.separator	Path separator (":" on UNIX)
	 * line.separator	Line separator ("\n" on UNIX)
	 * user.home		Home directory
	 * </pre>
	 * 
	 * @param key
	 *            the name of the property
	 * @return the string value of the property, or null if no property has that key
	 */
	public String getProperty(String key);

	/**
	 * Get the implementation specific attributes for the connection.
	 * 
	 * NOTE: the attributes do not include any security related information (e.g. passwords, keys, etc.)
	 * 
	 * @return a map containing the connection attribute keys and values
	 */
	public Map<String, String> getAttributes();

	/**
	 * Get the remote services provider for this connection.
	 * 
	 * @return remote services provider
	 */
	public IRemoteServices getRemoteServices();

	/**
	 * Return the specified connection service.
	 *  
	 * @param service interface
	 * @return service
	 */
	public <T extends IRemoteConnectionService> T getService(Class<T> service);

	/**
	 * Return a working copy of the connection that will allow changes to the name and attributes
	 * of the connection.
	 * 
	 * @return working copy
	 */
	public IRemoteConnectionWorkingCopy getWorkingCopy();

	/**
	 * Register a listener that will be notified when this connection's status changes.
	 * 
	 * @param listener
	 */
	public void addConnectionChangeListener(IRemoteConnectionChangeListener listener);

	/**
	 * Remove a listener that will be notified when this connection's status changes.
	 * 
	 * @param listener
	 */
	public void removeConnectionChangeListener(IRemoteConnectionChangeListener listener);

	/**
	 * Notify all listeners when this connection's status changes. See {{@link IRemoteConnectionChangeEvent} for a list of event
	 * types.
	 * 
	 * @param event
	 *            event type indicating the nature of the event
	 */
	public void fireConnectionChangeEvent(int type);

}
