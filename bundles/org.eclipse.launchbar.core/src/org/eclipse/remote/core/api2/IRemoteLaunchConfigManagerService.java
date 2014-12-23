package org.eclipse.remote.core.api2;

import org.eclipse.debug.core.ILaunchConfiguration;

public interface IRemoteLaunchConfigManagerService extends IRemoteManagerService {

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

}
