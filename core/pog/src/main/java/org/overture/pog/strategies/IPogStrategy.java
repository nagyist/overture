package org.overture.pog.strategies;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.analysis.QuestionAnswerAdaptor;
import org.overture.ast.expressions.AAndBooleanBinaryExp;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.IPogAssistantFactory;
import org.overture.pog.pub.IProofObligationList;

public interface IPogStrategy
{

	IProofObligationList executeAnd(AAndBooleanBinaryExp node,
			IPOContextStack question, QuestionAnswerAdaptor<IPOContextStack, IProofObligationList> visitor,
			IPogAssistantFactory assistantFactory) throws AnalysisException;
		

}
