package org.eclipse.remote.ui.api2;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.remote.core.api2.IRemoteServices;
import org.eclipse.swt.graphics.Image;

public interface IRemoteServicesUI extends IRemoteServices.Service {

	/**
	 * Returns the standard icon for this remote services.
	 * 
	 * @return icon
	 */
	Image getIcon();
	
	/**
	 * Returns the icon for this remote services. The icon may be decorated
	 * with overlays for the status.
	 *
	 * @param status status of the connection
	 * @return icon for this type and status
	 */
	Image getIcon(IStatus status);

	/**
	 * Returns the new connection wizard for this connection type.
	 * 
	 * @return new wizard
	 */
	IWizard getNewWizard();

}
