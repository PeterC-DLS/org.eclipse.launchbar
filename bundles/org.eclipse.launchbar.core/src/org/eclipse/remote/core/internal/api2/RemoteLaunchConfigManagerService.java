package org.eclipse.remote.core.internal.api2;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.launchbar.core.internal.Activator;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionManager;
import org.eclipse.remote.core.api2.IRemoteLaunchConfigManagerService;
import org.eclipse.remote.core.api2.IRemoteManager;
import org.eclipse.remote.core.api2.IRemoteServices;

public class RemoteLaunchConfigManagerService implements IRemoteLaunchConfigManagerService {

	private static final String CONFIG = "launchConfigRemote";
	private static final String TYPE = "launchConfigTypeRemote";
	private static final String SEPERATOR = ":";
	private IRemoteManager remoteManager = Activator.getService(IRemoteManager.class);

	@Override
	public void setActiveRemote(ILaunchConfiguration configuration, IRemoteConnection connection) {
		String connName = connection.getRemoteServices().getId() + SEPERATOR + connection.getName();
		IEclipsePreferences store = getPreferenceStore();
		store.node(CONFIG).put(configuration.getName(), connName);
		try {
			store.node(TYPE).put(configuration.getType().getIdentifier(), connName);
		} catch (CoreException e) {
			Activator.log(e.getStatus());
		}
	}

	private IEclipsePreferences getPreferenceStore() {
		return InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
	}

	private IRemoteConnection getRemoteConnection(String name) {
		if (name == null)
			return null;

		String[] parts = name.split(SEPERATOR);
		if (parts.length != 2)
			return null;

		IRemoteServices remoteServices = remoteManager.getRemoteServices(parts[0]);
		if (remoteServices == null)
			return null;

		return remoteServices.getService(IRemoteConnectionManager.class).getConnection(parts[1]);
	}

	@Override
	public IRemoteConnection getActiveRemote(ILaunchConfiguration configuration) {
		return getRemoteConnection(getPreferenceStore().node(CONFIG).get(configuration.getName(), null));
	}

	@Override
	public IRemoteConnection getLastActiveRemote(ILaunchConfigurationType configurationType) {
		return getRemoteConnection(getPreferenceStore().node(CONFIG).get(configurationType.getIdentifier(), null));
	}

}
