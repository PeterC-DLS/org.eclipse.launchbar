package org.eclipse.remote.core.api2;

import java.util.List;
import java.util.Map;

import org.eclipse.remote.core.IRemoteProcessBuilder;

public interface IRemoteProcessService extends IRemoteConnection.Service {

	/**
	 * Get a process builder for creating remote processes
	 * 
	 * @return process builder or null if connection is not open
	 */
	IRemoteProcessBuilder getProcessBuilder(List<String> command);

	/**
	 * Get a process builder for creating remote processes
	 * 
	 * @return process builder or null if connection is not open
	 */
	IRemoteProcessBuilder getProcessBuilder(String... command);

	/**
	 * Returns an unmodifiable string map view of the remote environment. The connection must be open prior to calling this method.
	 * 
	 * @return the remote environment
	 */
	public Map<String, String> getEnv();

	/**
	 * Returns the value of an environment variable. The connection must be open prior to calling this method.
	 * 
	 * @param name
	 *            name of the environment variable
	 * @return value of the environment variable or null if the variable is not defined
	 */
	public String getEnv(String name);

	/**
	 * Get the working directory. Relative paths will be resolved using this path.
	 * 
	 * The remote connection does not need to be open to use this method, however a default directory path, rather than the actual
	 * working directory, may be returned in this case.
	 * 
	 * @return String representing the current working directory
	 */
	public String getWorkingDirectory();

	/**
	 * Set the working directory while the connection is open. The working directory will revert to the default when the connection
	 * is closed then subsequently reopened.
	 * 
	 * Relative paths will be resolved using this path. The path must be valid and absolute for any changes to be made.
	 * 
	 * @param path
	 *            String representing the current working directory
	 * @since 4.0
	 */
	public void setWorkingDirectory(String path);

}
