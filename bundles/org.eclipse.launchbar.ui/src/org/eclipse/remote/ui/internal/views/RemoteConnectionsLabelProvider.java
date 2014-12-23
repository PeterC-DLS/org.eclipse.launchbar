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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.remote.core.api2.IRemoteConnection;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.remote.ui.api2.IRemoteServicesUI;
import org.eclipse.swt.graphics.Image;

public class RemoteConnectionsLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof IRemoteConnection) {
			return ((IRemoteConnection) element).getName();
		} else {
			return super.getText(element);
		}
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IRemoteConnection) {
			IRemoteConnection connection = (IRemoteConnection) element;
			IRemoteServices services = connection.getRemoteServices();
			IRemoteServicesUI ui = services.getService(IRemoteServicesUI.class);
			if (ui != null) {
				return ui.getIcon();
			}
		}
		return super.getImage(element);
	}

}
