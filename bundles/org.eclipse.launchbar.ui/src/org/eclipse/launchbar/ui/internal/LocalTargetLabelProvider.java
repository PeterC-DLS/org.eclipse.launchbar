package org.eclipse.launchbar.ui.internal;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.remote.core.api2.IRemoteConnection;


public class LocalTargetLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof IRemoteConnection) {
			return ((IRemoteConnection) element).getName();
		} else {
			return super.getText(element);
		}
	}

}
