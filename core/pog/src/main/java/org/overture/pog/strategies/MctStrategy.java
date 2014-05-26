package org.overture.pog.strategies;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.analysis.QuestionAnswerAdaptor;
import org.overture.ast.expressions.AAndBooleanBinaryExp;
import org.overture.ast.expressions.AExistsExp;
import org.overture.ast.expressions.AForAllExp;
import org.overture.ast.expressions.AImpliesBooleanBinaryExp;
import org.overture.ast.expressions.AOrBooleanBinaryExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstFactory;
import org.overture.ast.patterns.PMultipleBind;
import org.overture.ast.types.AUnionType;
import org.overture.ast.types.PType;
import org.overture.pog.obligation.POForAllContext;
import org.overture.pog.obligation.POImpliesContext;
import org.overture.pog.obligation.PONotImpliesContext;
import org.overture.pog.obligation.ProofObligationList;
import org.overture.pog.obligation.SubTypeObligation;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.IPogAssistantFactory;
import org.overture.pog.pub.IProofObligationList;

public class MctStrategy implements IPogStrategy
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

	@Override
	public IProofObligationList executeOr(
			AOrBooleanBinaryExp node,
			IPOContextStack question,
			QuestionAnswerAdaptor<IPOContextStack, IProofObligationList> visitor,
			IPogAssistantFactory assistantFactory) throws AnalysisException
	{
		IProofObligationList obligations = new ProofObligationList();

		PExp lExp = node.getLeft();
		PExp rExp = node.getRight();
		PType lType = lExp.getType();
		PType rType = rExp.getType();

		if (lType instanceof AUnionType)
		{
			SubTypeObligation sto = SubTypeObligation.newInstance(lExp, AstFactory.newABooleanBasicType(lExp.getLocation()), lType, question, assistantFactory);
			if (sto != null)
			{
				obligations.add(sto);
			}
		}

		if (rType instanceof AUnionType)
		{
			question.push(new PONotImpliesContext(lExp));
			SubTypeObligation sto = SubTypeObligation.newInstance(rExp, AstFactory.newABooleanBasicType(rExp.getLocation()), rType, question, assistantFactory);
			if (sto != null)
			{
				obligations.add(sto);
			}
			question.pop();
		}

		obligations.addAll(lExp.apply(visitor, question));
		question.push(new PONotImpliesContext(lExp));
		obligations.addAll(rExp.apply(visitor, question));
		question.pop();

		return obligations;
	}

	@Override
	public IProofObligationList executeImplies(
			AImpliesBooleanBinaryExp node,
			IPOContextStack question,
			QuestionAnswerAdaptor<IPOContextStack, IProofObligationList> visitor,
			IPogAssistantFactory assistantFactory) throws AnalysisException
	{
		ProofObligationList obligations = new ProofObligationList();

		PExp lExp = node.getLeft();
		PType lType = lExp.getType();
		PExp rExp = node.getRight();
		PType rType = rExp.getType();

		if (assistantFactory.createPTypeAssistant().isUnion(lType))
		{
			obligations.add(SubTypeObligation.newInstance(lExp, AstFactory.newABooleanBasicType(lExp.getLocation()), lType, question, assistantFactory));
		}

		if (assistantFactory.createPTypeAssistant().isUnion(rType))
		{
			obligations.add(SubTypeObligation.newInstance(rExp, AstFactory.newABooleanBasicType(rExp.getLocation()), rType, question, assistantFactory));
		}

		obligations.addAll(lExp.apply(visitor, question));
		question.push(new POImpliesContext(lExp));
		obligations.addAll(rExp.apply(visitor, question));
		question.pop();

		return obligations;

	}

	@Override
	public IProofObligationList executeForall(
			AForAllExp node,
			IPOContextStack question,
			QuestionAnswerAdaptor<IPOContextStack, IProofObligationList> visitor,
			IPogAssistantFactory assistantFactory) throws AnalysisException
	{

		IProofObligationList obligations = new ProofObligationList();

		for (PMultipleBind mb : node.getBindList())
		{
			obligations.addAll(mb.apply(visitor, question));
		}

		question.push(new POForAllContext(node));
		obligations.addAll(node.getPredicate().apply(visitor, question));
		question.pop();
		return obligations;
	}

	@Override
	public IProofObligationList executeExists(
			AExistsExp node,
			IPOContextStack question,
			QuestionAnswerAdaptor<IPOContextStack, IProofObligationList> visitor,
			IPogAssistantFactory assistantFactory) throws AnalysisException
	{
		IProofObligationList obligations = new ProofObligationList();

		for (PMultipleBind mb : node.getBindList())
		{
			obligations.addAll(mb.apply(visitor, question));
		}

		question.push(new POForAllContext(node));
		obligations.addAll(node.getPredicate().apply(visitor, question));
		question.pop();

		return obligations;
	}

}
