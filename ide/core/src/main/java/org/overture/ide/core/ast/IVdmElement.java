package org.overture.ide.core.ast;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.overturetool.vdmj.definitions.ClassList;
import org.overturetool.vdmj.modules.ModuleList;

public interface IVdmElement<T> {

	public abstract void setRootElementList(List<T> rootElementList);

	public abstract List<T> getRootElementList();

	public abstract Date getCheckedTime();

	public abstract void setChecked(boolean checked);

	public abstract boolean isChecked();

	/***
	 * Updates the local list with a new Definition if it already exists the old
	 * one is replaced
	 * 
	 * @param module
	 *            the new definition
	 */
	@SuppressWarnings("unchecked")
	public abstract void update(List<T> modules);

	/***
	 * Check if any definition in the list has the file as source location
	 * 
	 * @param file
	 *            The file which should be tested against all definitions in the
	 *            list
	 * @return true if the file has a definition in the list
	 */
	public abstract boolean hasFile(File file);

	public abstract ModuleList getModuleList() throws NotAllowedException;

	public abstract ClassList getClassList() throws NotAllowedException;

	public abstract boolean hasClassList();

	public abstract boolean hasModuleList();

	public abstract void setParseCorrect(String file, Boolean isParseCorrect);

	public abstract boolean isParseCorrect();

	public abstract boolean exists();

	public abstract IVdmElement filter(IFile file);

}