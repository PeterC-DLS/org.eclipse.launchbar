/*******************************************************************************
 * Copyright (c) 2014 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Doug Schaefer
 *******************************************************************************/
package org.eclipse.remote.ui.internal.views;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteConnectionChangeEvent;
import org.eclipse.remote.core.api2.IRemoteConnectionChangeListener;
import org.eclipse.remote.core.api2.IRemoteManager;

public class RemoteConnectionsContentProvider implements ITreeContentProvider, IRemoteConnectionChangeListener {

	private IRemoteManager manager;
	private Viewer viewer;

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;

		if (manager != null)
			manager.removeRemoteConnectionChangeListener(this);
		manager = (IRemoteManager)newInput;
		manager.addRemoteConnectionChangeListener(this);
	}

	@Override
	public void connectionChanged(IRemoteConnectionChangeEvent event) {
		viewer.getControl().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				viewer.refresh();
			}
		});
	}
	
	@Override
	public void dispose() {
		// TODO remove listener
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return manager.getAllRemoteConnections().toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		// connections don't have children by default
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IRemoteConnection) {
			return manager;
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IRemoteManager)
			return true;
		else
			return false;
	}

}
