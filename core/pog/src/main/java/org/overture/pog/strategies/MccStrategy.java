package org.overture.pog.strategies;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.analysis.QuestionAnswerAdaptor;
import org.overture.ast.expressions.AAndBooleanBinaryExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstFactory;
import org.overture.ast.types.PType;
import org.overture.pog.obligation.POImpliesContext;
import org.overture.pog.obligation.ProofObligationList;
import org.overture.pog.obligation.SubTypeObligation;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.IPogAssistantFactory;
import org.overture.pog.pub.IProofObligationList;

public class MccStrategy implements IPogStrategy
{

	@Override
	public IProofObligationList executeAnd(
			AAndBooleanBinaryExp node,
			IPOContextStack question,
			QuestionAnswerAdaptor<IPOContextStack, IProofObligationList> visitor,
			IPogAssistantFactory assistantFactory) throws AnalysisException
	{
		IProofObligationList obligations = new ProofObligationList();

		PExp lExp = node.getLeft();
		PType lType = lExp.getType();
		PExp rExp = node.getRight();
		PType rType = rExp.getType();

		if (assistantFactory.createPTypeAssistant().isUnion(lType))
		{
			SubTypeObligation sto = SubTypeObligation.newInstance(lExp, AstFactory.newABooleanBasicType(lExp.getLocation()), lType, question, assistantFactory);
			if (sto != null)
			{
				obligations.add(sto);
			}
		}

		if (assistantFactory.createPTypeAssistant().isUnion(rType))
		{
			question.push(new POImpliesContext(lExp));

			SubTypeObligation sto = SubTypeObligation.newInstance(rExp, AstFactory.newABooleanBasicType(rExp.getLocation()), rType, question, assistantFactory);
			if (sto != null)
			{
				obligations.add(sto);
			}
			question.pop();
		}

		obligations.addAll(lExp.apply(visitor, question));

		question.push(new POImpliesContext(lExp));
		obligations.addAll(rExp.apply(visitor, question));
		question.pop();

		return obligations;
	}

}
