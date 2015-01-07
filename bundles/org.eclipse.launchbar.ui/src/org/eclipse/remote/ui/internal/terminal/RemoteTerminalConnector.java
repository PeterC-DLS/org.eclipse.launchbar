package org.eclipse.remote.ui.internal.terminal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.launchbar.ui.internal.Activator;
import org.eclipse.remote.core.IRemoteProcess;
import org.eclipse.remote.core.api2.IRemoteCommandShellService;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionManager;
import org.eclipse.remote.core.api2.IRemoteManager;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.tm.internal.terminal.provisional.api.ISettingsPage;
import org.eclipse.tm.internal.terminal.provisional.api.ISettingsStore;
import org.eclipse.tm.internal.terminal.provisional.api.ITerminalControl;
import org.eclipse.tm.internal.terminal.provisional.api.TerminalState;
import org.eclipse.tm.internal.terminal.provisional.api.provider.TerminalConnectorImpl;

@SuppressWarnings("restriction")
public class RemoteTerminalConnector extends TerminalConnectorImpl {

	public static final String ID = "org.eclipse.remote.ui.terminalConnector";
	public static final String PROP_REMOTE_SERVICES = "remote.services";
	public static final String PROP_REMOTE_NAME = "remote.name";

	private IRemoteConnection remoteConnection;
	private IRemoteProcess remoteProcess;

	@Override
	public void connect(final ITerminalControl control) {
		super.connect(control);

		IRemoteCommandShellService commandShellService = remoteConnection.getService(IRemoteCommandShellService.class);
		try {
			remoteProcess = commandShellService.getCommandShell(0);
		} catch (IOException e) {
			Activator.log(e);
		}

		if (remoteProcess == null) {
			disconnect();
		} else {
			new Thread("Remote To Terminal") {
				public void run() {
					control.setState(TerminalState.CONNECTED);
					OutputStream terminalOut = fControl.getRemoteToTerminalOutputStream();
					InputStream remoteIn = remoteProcess.getInputStream();
					try {
						for (int c = remoteIn.read(); c >= 0; c = remoteIn.read()) {
							terminalOut.write(c);
						}
					} catch (IOException e) {
						Activator.log(e);
					}
				}
			}.start();
		}
	}
	
	@Override
	protected void doDisconnect() {
		if (remoteProcess != null) {
			remoteProcess.destroy();
			remoteProcess = null;
		}
	}

	@Override
	public OutputStream getTerminalToRemoteStream() {
		return remoteProcess != null ? remoteProcess.getOutputStream() : null;
	}

	@Override
	public ISettingsPage makeSettingsPage() {
		return new RemoteTerminalSettingsPage();
	}
	
	@Override
	public String getSettingsSummary() {
		return remoteConnection.getRemoteServices().getName() + " - " + remoteConnection.getName();
	}

	@Override
	public void load(ISettingsStore store) {
		String remoteServicesId = store.get(PROP_REMOTE_SERVICES);
		String name = store.get(PROP_REMOTE_NAME);
		
		IRemoteManager manager = Activator.getService(IRemoteManager.class);
		IRemoteServices remoteServices = manager.getRemoteServices(remoteServicesId);
		if (remoteServices != null) {
			remoteConnection = remoteServices.getService(IRemoteConnectionManager.class).getConnection(name);
		}
	}

	@Override
	public void save(ISettingsStore store) {
		store.put(PROP_REMOTE_SERVICES, remoteConnection.getRemoteServices().getId());
		store.put(PROP_REMOTE_NAME, remoteConnection.getName());
	}
	
}
