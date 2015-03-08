/*******************************************************************************
 *
 *	Copyright (C) 2008, 2009 Fujitsu Services Ltd.
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

package org.overture.codegen.runtime.traces;

import java.util.Vector;

@SuppressWarnings("serial")
public class CallSequence extends Vector<CallStatement>
{
	private int filtered = 0;

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		String sep = "";

		for (CallStatement stmt : this)
		{
			sb.append(sep);
			sb.append(stmt.toString());
			sep = "; ";
		}

		return sb.toString();

//		StringBuilder sb = new StringBuilder();
//		String sep = "";
//
//		for (PStm stmt : this)
//		{
//			if (!(stmt instanceof TraceVariableStatement))
//			{
//				sb.append(sep);
//				sb.append(stmt.toString());
//				sep = "; ";
//			}
//		}
//
//		return sb.toString();
	}

	public String toShape(TraceReductionType type)
	{
		//FIXME: How to do this
		throw new RuntimeException("Not yet implemented");
		
//		StringBuilder sb = new StringBuilder();
//		String sep = "";
//
//		for (PStm stmt : this)
//		{
//			if (stmt instanceof TraceVariableStatement)
//			{
//				TraceVariableStatement tvs = (TraceVariableStatement) stmt;
//
//				switch (type)
//				{
//					case SHAPES_NOVARS:
//						break;
//
//					case SHAPES_VARNAMES:
//						sb.append(sep);
//						sb.append(tvs.var.name);
//						sep = "; ";
//						break;
//
//					case SHAPES_VARVALUES:
//						sb.append(sep);
//						sb.append(tvs.toString());
//						sep = "; ";
//						break;
//
//					default:
//						break;
//				}
//			} else if (stmt instanceof ACallStm)
//			{
//				ACallStm cs = (ACallStm) stmt;
//				sb.append(sep);
//				sb.append(cs.getName().getName());
//				sep = "; ";
//			} else if (stmt instanceof ACallObjectStm)
//			{
//				ACallObjectStm cs = (ACallObjectStm) stmt;
//				sb.append(sep);
//				sb.append(cs.getFieldname());
//				sep = "; ";
//			}
//		}
//
//		return sb.toString();
	}

	public boolean compareStem(CallSequence other, int upto)
	{
		// Note that the upto count does not include the variable statements
		// that may be in the sequences, but those variables do need to be
		// included in the stem match. "count" is the position ignoring any
		// variable statements.

		int i = 0;

		for (int count = 0; count < upto;)
		{
			if (i >= size())
			{
				return false;
			}
			
			if (!compareItem(other, i))
			{
				return false;
			}

			// START CHANGE
			// pvj: will always be true as long as there are no "trace variable statements"
			// if (!(get(i) instanceof TraceVariableStatement))
			// {
				count++; // Only increment for non-variable statements
			// }
			// END CHANGE

			i++;
		}

		return true;
	}

	private boolean compareItem(CallSequence other, int i)
	{
		return get(i).toString().equals(other.get(i).toString());
	}

	public void setFilter(int n)
	{
		filtered = n;
	}

	public int getFilter()
	{
		return filtered;
	}
}
