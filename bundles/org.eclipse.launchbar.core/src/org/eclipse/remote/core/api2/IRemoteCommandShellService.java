package org.eclipse.remote.core.api2;

import java.io.IOException;

import org.eclipse.remote.core.IRemoteProcess;
import org.eclipse.remote.core.IRemoteProcessBuilder;

public interface IRemoteCommandShellService extends IRemoteConnection.Service {

	/**
	 * Get a remote process that runs a command shell on the remote system. The shell will be the user's default shell on the remote
	 * system. The flags may be used to modify behavior of the remote process. These flags may only be supported by specific types
	 * of remote service providers. Clients can use {@link IRemoteProcessBuilder#getSupportedFlags()} to find out the flags
	 * supported by the service provider.
	 * 
	 * <pre>
	 * Current flags are:
	 *   {@link IRemoteProcessBuilder#NONE}			- disable any flags
	 *   {@link IRemoteProcessBuilder#ALLOCATE_PTY}	- allocate a pseudo-terminal for the process (RFC-4254 Sec. 6.2)
	 *   {@link IRemoteProcessBuilder#FORWARD_X11}	- enable X11 forwarding (RFC-4254 Sec. 6.3)
	 * </pre>
	 * 
	 * @param flags
	 *            bitwise-or of flags
	 * @return remote process object
	 * @throws IOException
	 * @since 7.0
	 */
	IRemoteProcess getCommandShell(int flags) throws IOException;

}
