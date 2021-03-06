/*******************************************************************************
 * Copyright (c) 2016 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.launchbar.core;

import org.eclipse.debug.core.ILaunchMode;
import org.eclipse.launchbar.core.target.ILaunchTarget;

/**
 * Listener that can be installed on launch bar to listen for state changes.
 * {@link ILaunchBarManager#addListener(ILaunchBarListener)}.
 *
 * <code>
 *    Activator.getService(ILaunchBarManager.class).addListener(new ILaunchBarListener(){
 *             public void activeLaunchTargetChanged(ILaunchTarget target) {
 *                     // do something
 *             }
 *    });
 * </code>
 */
public interface ILaunchBarListener {
	default void activeLaunchDescriptorChanged(ILaunchDescriptor descriptor) {
	}

	default void activeLaunchModeChanged(ILaunchMode mode) {
	}

	default void activeLaunchTargetChanged(ILaunchTarget target) {
	}

	default void launchTargetsChanged() {
	}
}