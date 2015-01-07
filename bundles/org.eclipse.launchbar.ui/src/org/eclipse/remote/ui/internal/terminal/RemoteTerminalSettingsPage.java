package org.eclipse.remote.ui.internal.terminal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tm.internal.terminal.provisional.api.ISettingsPage;

@SuppressWarnings("restriction")
public class RemoteTerminalSettingsPage implements ISettingsPage {

	@Override
	public void createControl(Composite parent) {
		new Composite(parent, SWT.NONE);
	}

	@Override
	public void loadSettings() {
	}

	@Override
	public void saveSettings() {
	}

	@Override
	public boolean validateSettings() {
		return false;
	}

	@Override
	public void addListener(Listener listener) {
	}

	@Override
	public void removeListener(Listener listener) {
	}

}
