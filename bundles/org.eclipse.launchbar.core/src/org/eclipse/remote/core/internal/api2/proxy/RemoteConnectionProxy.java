package org.eclipse.remote.core.internal.api2.proxy;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.Status;
import org.eclipse.remote.core.IRemoteProcess;
import org.eclipse.remote.core.IRemoteProcessBuilder;
import org.eclipse.remote.core.api2.IRemoteCommandShellService;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionChangeListener;
import org.eclipse.remote.core.api2.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.api2.IRemoteFileService;
import org.eclipse.remote.core.api2.IRemotePortForwardService;
import org.eclipse.remote.core.api2.IRemoteProcessService;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.core.exception.RemoteConnectionException;

public class RemoteConnectionProxy extends PlatformObject implements IRemoteConnection,
IRemoteCommandShellService, IRemoteProcessService, IRemotePortForwardService {

	final RemoteServicesProxy services;
	final org.eclipse.remote.core.IRemoteConnection connection;

	public RemoteConnectionProxy(RemoteServicesProxy services,
			org.eclipse.remote.core.IRemoteConnection connection) {
		this.services = services;
		this.connection = connection;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(Class<T> service) {
		if (IRemoteCommandShellService.class.equals(service)
				|| IRemoteProcessService.class.equals(service)
				|| IRemotePortForwardService.class.equals(service)) {
			return (T)this;
		} else if (IRemoteFileService.class.equals(service)) {
			return (T)new RemoteFileServiceProxy(this, connection.getFileManager());
		} else {
			return (T)getAdapter(service);
		}
	}

	@Override
	public IRemoteConnection getConnection() {
		return this;
	}

	@Override
	public IRemoteServices getRemoteServices() {
		return services;
	}

	@Override
	public String getName() {
		return connection.getName();
	}

	@Override
	public Map<String, String> getAttributes() {
		return connection.getAttributes();
	}

	@Override
	public String getProperty(String key) {
		return connection.getProperty(key);
	}

	@Override
	public boolean isOpen() {
		return connection.isOpen();
	}
	
	@Override
	public IStatus getConnectionStatus() {
		if (connection.isOpen()) {
			return Status.OK_STATUS;
		} else {
			return Status.CANCEL_STATUS;
		}
	}

	@Override
	public void open(IProgressMonitor monitor) throws RemoteConnectionException {
		connection.open(monitor);
	}

	@Override
	public void close() {
		connection.close();
	}

	@Override
	public IRemoteConnectionWorkingCopy getWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		connection.addConnectionChangeListener(new RemoteConnectionChangeListenerProxy(services, listener));
	}

	@Override
	public void removeConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		connection.removeConnectionChangeListener(new RemoteConnectionChangeListenerProxy(services, listener));
	}

	@Override
	public void fireConnectionChangeEvent(int type) {
		connection.fireConnectionChangeEvent(type);
	}

	@Override
	public void forwardLocalPort(int localPort, String fwdAddress, int fwdPort) throws RemoteConnectionException {
		connection.forwardLocalPort(localPort, fwdAddress, fwdPort);
	}

	@Override
	public int forwardLocalPort(String fwdAddress, int fwdPort, IProgressMonitor monitor)
			throws RemoteConnectionException {
		return connection.forwardLocalPort(fwdAddress, fwdPort, monitor);
	}

	@Override
	public void forwardRemotePort(int remotePort, String fwdAddress, int fwdPort) throws RemoteConnectionException {
		connection.forwardRemotePort(remotePort, fwdAddress, fwdPort);
	}

	@Override
	public int forwardRemotePort(String fwdAddress, int fwdPort, IProgressMonitor monitor)
			throws RemoteConnectionException {
		return connection.forwardRemotePort(fwdAddress, fwdPort, monitor);
	}

	@Override
	public void removeLocalPortForwarding(int port) throws RemoteConnectionException {
		connection.removeLocalPortForwarding(port);
	}

	@Override
	public void removeRemotePortForwarding(int port) throws RemoteConnectionException {
		connection.removeRemotePortForwarding(port);
	}

	@Override
	public IRemoteProcess getCommandShell(int flags) throws IOException {
		return connection.getCommandShell(flags);
	}

	@Override
	public Map<String, String> getEnv() {
		return connection.getEnv();
	}

	@Override
	public String getEnv(String name) {
		return connection.getEnv(name);
	}

	@Override
	public IRemoteProcessBuilder getProcessBuilder(List<String> command) {
		return connection.getProcessBuilder(command);
	}

	@Override
	public IRemoteProcessBuilder getProcessBuilder(String... command) {
		return connection.getProcessBuilder(command);
	}

	@Override
	public String getWorkingDirectory() {
		return connection.getWorkingDirectory();
	}

	@Override
	public void setWorkingDirectory(String path) {
		connection.setWorkingDirectory(path);
	}

}
