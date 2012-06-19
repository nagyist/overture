
package org.overture.ast.util.definitions;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.overture.ast.definitions.ABusClassDefinition;
import org.overture.ast.definitions.ACpuClassDefinition;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.expressions.PExp;
import org.overture.ast.lex.LexNameToken;
import org.overture.ast.statements.PStm;
import org.overture.ast.typechecker.NameScope;


/**
 * A class for holding a list of ClassDefinitions.
 */

public class ClassList extends Vector<SClassDefinition>
{
	private static final long serialVersionUID = 1L;

	private static Map<String, SClassDefinition> map =
					new HashMap<String, SClassDefinition>();

	public ClassList()
	{
		super();
	}

	public ClassList(SClassDefinition definition)
	{
		add(definition);
	}

	@Override
	public boolean add(SClassDefinition cdef)
	{
		map.put(cdef.getName().name, cdef); 

		return super.add(cdef);
	}

	@Override
	public boolean addAll(Collection<? extends SClassDefinition> clist)
	{
		for (SClassDefinition cls: clist)
		{
			add(cls);
		}

		return true;
	}

	public void remap()
	{
		map.clear();

		for (SClassDefinition d: this)
		{
			map.put(d.getName().name, d);
		}
	}

	public Set<File> getSourceFiles()
	{
		Set<File> files = new HashSet<File>();

		for (SClassDefinition def: this)
		{
			if (!(def instanceof ACpuClassDefinition ||
				  def instanceof ABusClassDefinition))
			{
				files.add(def.getLocation().file);
			}
		}

		return files;
	}

//	public void implicitDefinitions(Environment env)
//	{
//		for (SClassDefinition d: this)
//		{
//			d.implicitDefinitions(env);
//		}
//	}

//	public void setLoaded()
//	{
//		for (SClassDefinition d: this)
//		{
//			d.typechecked = true;
//		}
//	}

//	public int notLoaded()
//	{
//		int count = 0;
//
//		for (SClassDefinition d: this)
//		{
//			if (!d.typechecked) count++;
//		}
//
//		return count;
//	}

//	public void unusedCheck()
//	{
//		for (SClassDefinition d: this)
//		{
//			d.unusedCheck();
//		}
//	}


	public PDefinition findName(LexNameToken name, NameScope scope)
	{
//		SClassDefinition d = map.get(name.module);
//
//		if (d != null)
//		{
//			PDefinition def = d.findName(name, scope);
//
//			if (def != null)
//			{
//				return def;
//			}
//		}
		//TODO must be implemented else where through assistance or so

		return null;
	}

	public PDefinition findType(LexNameToken name)
	{
//		for (SClassDefinition d: this)
//		{
//			PDefinition def = d.findType(name, null);
//
//			if (def != null)
//			{
//				return def;
//			}
//		}
		//TODO must be implemented else where through assistance or so

		return null;
	}

//	public DefinitionSet findMatches(LexNameToken name)
//	{
//		DefinitionSet set = new DefinitionSet();
//
//		for (SClassDefinition d: this)
//		{
//			set.addAll(d.findMatches(name));
//		}
//
//		return set;
//	}

	public PStm findStatement(File file, int lineno)
	{
//		for (SClassDefinition c: this)
//		{
//			if (c.name.location.file.equals(file))
//			{
//    			Statement stmt = c.findStatement(lineno);
//
//    			if (stmt != null)
//    			{
//    				return stmt;
//    			}
//			}
//		}
		//TODO must be implemented else where through assistance or so

		return null;
	}

	public PExp findExpression(File file, int lineno)
	{
//		for (SClassDefinition c: this)
//		{
//			if (c.name.location.file.equals(file))
//			{
//    			Expression exp = c.findExpression(lineno);
//
//    			if (exp != null)
//    			{
//    				return exp;
//    			}
//			}
//		}
		//TODO must be implemented else where through assistance or so

		return null;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for (SClassDefinition c: this)
		{
			sb.append(c.toString());
			sb.append("\n");
		}

		return sb.toString();
	}

//	public ProofObligationList getProofObligations()
//	{
//		ProofObligationList obligations = new ProofObligationList();
//
//		for (SClassDefinition c: this)
//		{
//			obligations.addAll(c.getProofObligations(new POContextStack()));
//		}
//
//		obligations.trivialCheck();
//		return obligations;
//	}
}