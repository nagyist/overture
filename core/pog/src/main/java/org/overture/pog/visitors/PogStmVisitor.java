package org.overture.pog.visitors;

import org.overture.ast.analysis.QuestionAnswerAdaptor;
import org.overture.pog.contexts.POContextStack;
import org.overture.pog.obligation.ProofObligationList;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.IProofObligationList;

public class PogStmVisitor extends
		PogParamStmVisitor<POContextStack, ProofObligationList>
{

	public PogStmVisitor(
			QuestionAnswerAdaptor<IPOContextStack, ? extends IProofObligationList> parentVisitor)
	{
		super(parentVisitor);
	}

}
