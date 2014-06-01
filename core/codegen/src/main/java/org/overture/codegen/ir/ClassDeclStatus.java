package org.overture.codegen.ir;

import java.util.Set;

import org.overture.codegen.cgast.declarations.AClassDeclCG;

public class ClassDeclStatus extends IRStatus
{
	private String className;
	private AClassDeclCG classCg;
	
	public ClassDeclStatus(String className, AClassDeclCG classCg, Set<NodeInfo> unsupportedNodes)
	{
		super(unsupportedNodes);
		this.className = className;
		this.classCg = classCg;
	}

	public String getClassName()
	{
		return className;
	}
	
	public AClassDeclCG getClassCg()
	{
		return classCg;
	}
}