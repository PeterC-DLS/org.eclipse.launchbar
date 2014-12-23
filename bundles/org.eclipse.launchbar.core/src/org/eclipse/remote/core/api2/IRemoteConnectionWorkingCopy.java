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

public interface IRemoteConnectionWorkingCopy extends IRemoteConnection {
	/**
	 * Returns the original connection this working copy was created from.
	 * 
	 * @return original connection
	 */
	public IRemoteConnection getOriginal();

	/**
	 * Returns whether this connection has been modified since it was last saved or created.
	 * 
	 * @return true if the connection has been modified
	 */
	public boolean isDirty();

	/**
	 * Set the name for this connection
	 * 
	 * @param name
	 */
	public void setName(String name);

	/**
	 * Set an implementation dependent attribute for the connection. Attributes keys supported by the connection can be obtained
	 * using {@link #getAttributes()}. Attributes are persisted along with connection information.
	 * 
	 * @param key
	 *            attribute key
	 * @param value
	 *            attribute value
	 * @since 5.0
	 */
	public void setAttribute(String key, String value);

	/**
	 * Saves this working copy to its original connection and returns a handle to the resulting connection. Has no effect if this
	 * connection does not need saving.
	 * 
	 * @return saved connection
	 */
	public IRemoteConnection save();

}
