package org.overture.ide.plugins.poviewer;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PogPrefPage1 extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage
{
	public void createFieldEditors()
	{
		addField(new RadioGroupFieldEditor(PogPrefConstants.POG_LOGIC_CHOICE, "Underlying logic of Proof Obligation Generator", 1, new String[][] {
				{ "&McCarthy (classic)", PogPrefConstants.POG_MCCARTHY },
				{ "&LPF (experimental)", PogPrefConstants.POG_LPF } }, getFieldEditorParent()));
	}


	@Override
	public void init(IWorkbench workbench)
	{
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

}
