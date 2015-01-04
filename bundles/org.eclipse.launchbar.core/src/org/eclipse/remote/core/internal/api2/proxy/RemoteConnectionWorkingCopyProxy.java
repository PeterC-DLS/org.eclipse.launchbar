package org.eclipse.remote.core.internal.api2.proxy;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.Status;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionChangeListener;
import org.eclipse.remote.core.api2.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.core.exception.RemoteConnectionException;

public class RemoteConnectionWorkingCopyProxy extends PlatformObject implements IRemoteConnectionWorkingCopy {

	final RemoteConnectionProxy original;
	final org.eclipse.remote.core.IRemoteConnectionWorkingCopy workingCopy;

	public RemoteConnectionWorkingCopyProxy(RemoteConnectionProxy original) {
		this.original = original;
		this.workingCopy = original.connection.getWorkingCopy();
	}

	public RemoteConnectionWorkingCopyProxy(RemoteServicesProxy services,
			org.eclipse.remote.core.IRemoteConnectionWorkingCopy workingCopy) {
		this.workingCopy = workingCopy;
		this.original = new RemoteConnectionProxy(services, workingCopy.getOriginal());
	}

	@Override
	public String getName() {
		return workingCopy.getName();
	}

	@Override
	public boolean isOpen() {
		return workingCopy.isOpen();
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
	public <T extends Service> T getService(Class<T> service) {
		return (T)original.getRemoteServices();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends WorkingCopyService> T getWorkingCopyService(Class<T> service) {
		return (T)getAdapter(service);
	}

	@Override
	public IRemoteConnectionWorkingCopy getWorkingCopy() {
		return this;
	}

	@Override
	public void addConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		workingCopy.addConnectionChangeListener(new RemoteConnectionChangeListenerProxy(
				original.services, listener));
	}

	@Override
	public void removeConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		workingCopy.removeConnectionChangeListener(new RemoteConnectionChangeListenerProxy(
				original.services, listener));
	}

	@Override
	public void fireConnectionChangeEvent(int type) {
		workingCopy.fireConnectionChangeEvent(type);
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
		return new RemoteConnectionProxy(original.services, workingCopy.save());
	}

}
