package org.eclipse.remote.core.internal.api2;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.launchbar.core.internal.Activator;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionManager;
import org.eclipse.remote.core.api2.IRemoteLaunchConfigManagerService;
import org.eclipse.remote.core.api2.IRemoteManager;
import org.eclipse.remote.core.api2.IRemoteServices;

public class RemoteLaunchConfigManagerService implements IRemoteLaunchConfigManagerService {

	private Map<String, String> configRemoteMap = new HashMap<>();
	private Map<String, String> configTypeRemoteMap = new HashMap<>();
	private IRemoteManager remoteManager = Activator.getService(IRemoteManager.class);

	@Override
	public void setActiveRemote(ILaunchConfiguration configuration, IRemoteConnection connection) {
		String connName = connection.getRemoteServices().getId() + "/" + connection.getName();
		configRemoteMap.put(configuration.getName(), connName);
		try {
			configTypeRemoteMap.put(configuration.getType().getIdentifier(), connName);
		} catch (CoreException e) {
			Activator.log(e.getStatus());
		}
	}

	private IRemoteConnection getRemoteConnection(String name) {
		if (name == null)
			return null;

		String[] parts = name.split("\\/");
		if (parts.length != 2)
			return null;

		IRemoteServices remoteServices = remoteManager.getRemoteServices(parts[0]);
		if (remoteServices == null)
			return null;

		return remoteServices.getService(IRemoteConnectionManager.class).getConnection(parts[1]);
	}

	@Override
	public IRemoteConnection getActiveRemote(ILaunchConfiguration configuration) {
		return getRemoteConnection(configRemoteMap.get(configuration.getName()));
	}

	@Override
	public IRemoteConnection getLastActiveRemote(ILaunchConfigurationType configurationType) {
		return getRemoteConnection(configTypeRemoteMap.get(configurationType));
	}

}
