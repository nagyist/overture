package org.overture.ide.plugins.poviewer;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PogPrefInitializer extends AbstractPreferenceInitializer
{

	public PogPrefInitializer()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initializeDefaultPreferences()
	{
	    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	    store.setDefault(PogPrefConstants.POG_LOGIC_CHOICE, PogPrefConstants.POG_MCCARTHY);
	}

}
