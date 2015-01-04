package org.eclipse.remote.core.api2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.Status;
import org.eclipse.launchbar.core.internal.Activator;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.eclipse.remote.core.internal.api2.RemoteManager;

public abstract class AbstractRemoteConnectionWorkingCopy extends PlatformObject implements IRemoteConnectionWorkingCopy {

	private IRemoteServices remoteServices;
	private IRemoteConnection original;
	private boolean isDirty;
	private String name;
	private Map<String, String> attributes;
	List<IRemoteConnectionChangeListener> listeners = new LinkedList<>();

	protected AbstractRemoteConnectionWorkingCopy(AbstractRemoteConnection original) {
		this.original = original;
	}

	protected AbstractRemoteConnectionWorkingCopy(IRemoteServices remoteServices, String name) {
		this.remoteServices = remoteServices;
		this.name = name;
	}

	@Override
	public String getName() {
		if (name != null)
			return name;
		if (original != null)
			return original.getName();
		return null;
	}

	@Override
	public boolean isOpen() {
		return original != null ? original.isOpen() : false;
	}

	@Override
	public IStatus getConnectionStatus() {
		return original != null ? original.getConnectionStatus() : Status.CANCEL_STATUS;
	}

	@Override
	public void open(IProgressMonitor monitor) throws RemoteConnectionException {
		if (original != null)
			original.open(monitor);
	}

	@Override
	public void close() {
		if (original != null)
			original.close();
	}

	@Override
	public String getProperty(String key) {
		return original != null ? original.getProperty(key) : null;
	}

	@Override
	public Map<String, String> getAttributes() {
		if (attributes != null)
			return attributes;
		if (original != null)
			return original.getAttributes();
		return null;
	}

	@Override
	public IRemoteServices getRemoteServices() {
		return remoteServices;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(Class<T> service) {
		if (original != null)
			return original.getService(service);
		else
			return (T)getAdapter(service);
	}

	@Override
	public IRemoteConnectionWorkingCopy getWorkingCopy() {
		return this;
	}

	@Override
	public void addConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		if (original != null)
			original.addConnectionChangeListener(listener);
	}

	@Override
	public void removeConnectionChangeListener(IRemoteConnectionChangeListener listener) {
		if (original != null)
			original.removeConnectionChangeListener(listener);
	}

	@Override
	public void fireConnectionChangeEvent(int type) {
		if (original != null)
			original.fireConnectionChangeEvent(type);
	}

	@Override
	public IRemoteConnection getOriginal() {
		return original;
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setAttribute(String key, String value) {
		if (attributes == null) {
			attributes = new HashMap<>(original.getAttributes());
		}
		attributes.put(key, value);
	}

	@Override
	public IRemoteConnection save() {
		if (name != null) {
			original.fireConnectionChangeEvent(IRemoteConnectionChangeEvent.CONNECTION_RENAMED);
		}
		// Save the attributes
		IPath stateLoc = Activator.getDefault().getStateLocation();
		IPath connsLoc = stateLoc.append(RemoteManager.CONNECTIONS_DIR);
		File typeDir = connsLoc.append(getRemoteServices().getId()).toFile();
		typeDir.mkdirs();
		File propFile = new File(typeDir, getName());

		Properties props = new Properties();
		Map<String, String> attributes = getAttributes();
		for (Entry<String, String> entry : attributes.entrySet()) {
			props.setProperty(entry.getKey(), entry.getValue());
		}

		try {
			props.store(new FileOutputStream(propFile), getRemoteServices().getId() + ":" + getName());
			isDirty = false;
		} catch (IOException e) {
			Activator.log(e);
		}

		// Create the new connection object and return it
		return doSave();
	}

	protected abstract IRemoteConnection doSave();
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends WorkingCopyService> T getWorkingCopyService(Class<T> service) {
		return (T)getAdapter(service);
	}

}
