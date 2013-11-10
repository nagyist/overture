package org.overture.codegen.utils;

import java.util.LinkedList;
import java.util.List;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.ast.intf.lex.ILexLocation;
import org.overture.ast.intf.lex.ILexNameToken;
import org.overture.ast.node.INode;

public class NameAnalysis extends DepthFirstAnalysisAdaptor
{

	private static final long serialVersionUID = -2470709310930692461L;

	private List<NameViolation> nameViolations; 
	private NamingComparison comparison;
	
	public NameAnalysis(NamingComparison comparison)
	{
		this.nameViolations = new LinkedList<NameViolation>();
		this.comparison = comparison;
	}
	
	public List<NameViolation> getNameViolations()
	{
		return nameViolations;
	}
	
	@Override
	public void defaultInINode(INode node) throws AnalysisException
	{
		if(node instanceof ILexNameToken)
		{
			ILexNameToken nameToken = (ILexNameToken) node;
			
			if(comparison.isInvalid(nameToken))
			{
				
				String name = nameToken.getName();
				ILexLocation location = nameToken.getLocation();
				
				NameViolation violation = new NameViolation(name, location);
				
				if(!nameViolations.contains(violation))
					nameViolations.add(violation);
			}
		}
	}
		
}