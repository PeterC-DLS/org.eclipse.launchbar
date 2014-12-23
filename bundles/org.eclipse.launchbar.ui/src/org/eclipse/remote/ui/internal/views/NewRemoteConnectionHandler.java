package org.eclipse.remote.ui.internal.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.remote.ui.internal.api2.dialogs.NewRemoteConnectionWizard;
import org.eclipse.ui.PlatformUI;

public class NewRemoteConnectionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		NewRemoteConnectionWizard wizard = new NewRemoteConnectionWizard();
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		dialog.open();
		return Status.OK_STATUS;
	}

}
