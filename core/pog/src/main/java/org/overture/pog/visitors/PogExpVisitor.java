package org.overture.pog.visitors;

import org.overture.ast.analysis.QuestionAnswerAdaptor;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.IProofObligationList;
import org.overture.pog.strategies.IPogStrategy;

public class PogExpVisitor extends
		PogParamExpVisitor<IPOContextStack, IProofObligationList>
{

	public PogExpVisitor(
			QuestionAnswerAdaptor<IPOContextStack, ? extends IProofObligationList> parentVisitor, IPogStrategy strats)
	{
		super(parentVisitor, strats);
	}

}
