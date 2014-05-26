package org.overture.pog.strategies;

import java.util.LinkedList;
import java.util.List;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.analysis.QuestionAnswerAdaptor;
import org.overture.ast.expressions.AAndBooleanBinaryExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstFactory;
import org.overture.ast.types.PType;
import org.overture.pog.obligation.LPFAndPO;
import org.overture.pog.obligation.ProofObligationList;
import org.overture.pog.obligation.SubTypeObligation;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.IPogAssistantFactory;
import org.overture.pog.pub.IProofObligation;
import org.overture.pog.pub.IProofObligationList;

public class LpfStrategy implements IPogStrategy
{

	@Override
	public IProofObligationList executeAnd(AAndBooleanBinaryExp node,
			IPOContextStack question, QuestionAnswerAdaptor<IPOContextStack, IProofObligationList> visitor, IPogAssistantFactory assistantFactory) throws AnalysisException
	{
		IProofObligationList obligations = new ProofObligationList();

		PExp lExp = node.getLeft();
		PType lType = lExp.getType();
		PExp rExp = node.getRight();
		PType rType = rExp.getType();

		if (assistantFactory.createPTypeAssistant().isUnion(lType))
		{
			SubTypeObligation stol = SubTypeObligation.newInstance(lExp, AstFactory.newABooleanBasicType(lExp.getLocation()), lType, question, assistantFactory);
			if (stol != null)
			{
				obligations.add(stol);
			}
			SubTypeObligation stor = SubTypeObligation.newInstance(rExp, AstFactory.newABooleanBasicType(rExp.getLocation()), rType, question, assistantFactory);
			if (stor != null)
			{
				obligations.add(stor);
			}
		}

		IProofObligationList lobligations = new ProofObligationList();
		IProofObligationList robligations = new ProofObligationList();
		
		
		lobligations.addAll(lExp.apply(visitor, question));
		robligations.addAll(rExp.apply(visitor, question));

		if (!lobligations.isEmpty() || !robligations.isEmpty()){
			List<PExp> definedPredicates = new LinkedList<PExp>();
			for (IProofObligation po : lobligations){
				definedPredicates.add(po.getStitch());
			}
			for (IProofObligation po : robligations){
				definedPredicates.add(po.getStitch());
			}
			obligations.add(new LPFAndPO(node, lExp, rExp, definedPredicates, question));
		}
	
		return obligations;
	}

}
