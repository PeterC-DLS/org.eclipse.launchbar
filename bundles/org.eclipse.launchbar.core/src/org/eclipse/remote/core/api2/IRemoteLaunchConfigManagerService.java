package org.eclipse.remote.core.api2;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;

public interface IRemoteLaunchConfigManagerService {

	/**
	 * Set the active remote for the given launch configuration. Any upcoming launches will
	 * be run on the remote.
	 * 
	 * @param configuration launch configuration
	 * @param connection active remote connection
	 */
	void setActiveRemote(ILaunchConfiguration configuration, IRemoteConnection connection);

	/**
	 * Get the active remote for the given launch configuration.
	 * 
	 * @param configuration launch configuration
	 * @return remote connection
	 */
	IRemoteConnection getActiveRemote(ILaunchConfiguration configuration);

	/**
	 * Return the last remote connection used by a connection of the given type.
	 * This can be used as a default connection for new configurations of that type.
	 * 
	 * @param configuration
	 * @return last used remote connection
	 */
	IRemoteConnection getLastActiveRemote(ILaunchConfigurationType configurationType);

}
