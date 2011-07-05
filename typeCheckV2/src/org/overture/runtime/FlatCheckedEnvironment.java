/*******************************************************************************
 *
 *	Copyright (c) 2008 Fujitsu Services Ltd.
 *
 *	Author: Nick Battle
 *
 *	This file is part of VDMJ.
 *
 *	VDMJ is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	VDMJ is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with VDMJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package org.overture.runtime;

import java.util.List;

import org.overture.ast.definitions.PDefinition;
import org.overturetool.vdmj.typechecker.NameScope;



/**
 * Define the type checking environment for a list of definitions, including
 * a check for duplicates and name hiding.
 */

public class FlatCheckedEnvironment extends FlatEnvironment
{
	private boolean isStatic = false;

	public FlatCheckedEnvironment(
		List<PDefinition> definitions, NameScope scope)
	{
		super(definitions);
		dupHideCheck(definitions, scope);
	}

	public FlatCheckedEnvironment(
		List<PDefinition> definitions, Environment env, NameScope scope)
	{
		super(definitions, env);
		dupHideCheck(definitions, scope);
		setStatic(env.isStatic());
	}

	public FlatCheckedEnvironment(
		PDefinition one, Environment env, NameScope scope)
	{
		super(one, env);
		dupHideCheck(definitions, scope);
		setStatic(env.isStatic());
	}

	
	//TODO: AccessSpecifier not defined
//	public void setStatic(AccessSpecifier access)
//	{
//		isStatic = access.isStatic;
//	}

	public void setStatic(boolean access)
	{
		isStatic = access;
	}

	@Override
	public boolean isStatic()
	{
		return isStatic;
	}
}
