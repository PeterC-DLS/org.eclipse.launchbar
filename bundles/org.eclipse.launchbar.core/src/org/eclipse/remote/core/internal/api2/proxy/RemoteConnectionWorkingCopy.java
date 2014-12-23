package org.eclipse.remote.core.internal.api2.proxy;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionChangeListener;
import org.eclipse.remote.core.api2.IRemoteConnectionService;
import org.eclipse.remote.core.api2.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.core.exception.RemoteConnectionException;

public class RemoteConnectionWorkingCopy implements IRemoteConnectionWorkingCopy {

	final IRemoteConnection original;
	final org.eclipse.remote.core.IRemoteConnectionWorkingCopy workingCopy;

	public RemoteConnectionWorkingCopy(RemoteConnection original) {
		this.original = original;
		this.workingCopy = original.connection.getWorkingCopy();
	}

	public RemoteConnectionWorkingCopy(org.eclipse.remote.core.IRemoteConnectionWorkingCopy workingCopy) {
		this.workingCopy = workingCopy;
		this.original = new RemoteConnection(workingCopy.getOriginal());
	}

	@Override
	public String getName() {
		return workingCopy.getName();
	}

	@Override
	public IStatus getConnectionStatus() {
		if (workingCopy.isOpen()) {
			return Status.OK_STATUS;
		} else {
			return Status.CANCEL_STATUS;
		}
	}

	@Override
	public void open(IProgressMonitor monitor) throws RemoteConnectionException {
		workingCopy.open(monitor);
	}

	@Override
	public void close() {
		workingCopy.close();
	}

	@Override
	public String getProperty(String key) {
		return workingCopy.getProperty(key);
	}

	@Override
	public Map<String, String> getAttributes() {
		return workingCopy.getAttributes();
	}

	@Override
	public IRemoteServices getRemoteServices() {
		return original.getRemoteServices();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IRemoteConnectionService> T getService(Class<T> service) {
		return (T)original.getRemoteServices();
	}

	@Override
	public IRemoteConnectionWorkingCopy getWorkingCopy() {
		return this;
	}

	@Override
	public void addConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		workingCopy.addConnectionChangeListener(new RemoteConnectionChangeListener(listener));
	}

	@Override
	public void removeConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		workingCopy.removeConnectionChangeListener(new RemoteConnectionChangeListener(listener));
	}

	@Override
	public void fireConnectionChangeEvent(int type) {
		workingCopy.fireConnectionChangeEvent(type);
	}

	@Override
	public int compareTo(IRemoteConnection o) {
		return workingCopy.compareTo(((RemoteConnection) o).connection);
	}

	@Override
	public IRemoteConnection getOriginal() {
		return original;
	}

	@Override
	public boolean isDirty() {
		return workingCopy.isDirty();
	}

	@Override
	public void setName(String name) {
		workingCopy.setName(name);
	}

	@Override
	public void setAttribute(String key, String value) {
		if (ADDRESS.equals(key)) {
			workingCopy.setAddress(value);
			workingCopy.setAttribute(key, value);
		} else if (PORT.equals(key)) {
			workingCopy.setPort(Integer.valueOf(value));
			workingCopy.setAttribute(key, value);
		} else if (USERNAME.equals(key)) {
			workingCopy.setUsername(value);
			workingCopy.setAttribute(key, value);
		} else if (PASSWORD.equals(key)) {
			workingCopy.setPassword(value);
		}
	}

	@Override
	public IRemoteConnection save() {
		return new RemoteConnection(workingCopy.save());
	}

}
