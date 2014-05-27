package org.overture.pog.obligation;

import java.util.LinkedList;
import java.util.List;

import org.overture.ast.expressions.AExists1Exp;
import org.overture.ast.expressions.AExistsExp;
import org.overture.ast.expressions.AForAllExp;
import org.overture.ast.expressions.AOrBooleanBinaryExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstExpressionFactory;
import org.overture.ast.patterns.PBind;
import org.overture.ast.patterns.PMultipleBind;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.IPogAssistantFactory;
import org.overture.pog.pub.IProofObligation;

public class LpfExists1Po extends LpfProofObligation implements
		IProofObligation
{

	private static final long serialVersionUID = 1L;

	public LpfExists1Po(AExists1Exp node, List<PExp> definePredicates,
			IPOContextStack ctxt, IPogAssistantFactory af)
	{
		super(node, POType.LPF_EXISTS, ctxt, node.getLocation());

		AExistsExp exists_exp = new AExistsExp();

		exists_exp.setBindList(af.createPBindAssistant().getMultipleBindList(node.getBind().clone()));
		exists_exp.setPredicate(node.getPredicate().clone());

		AForAllExp forall_exp = new AForAllExp();
		forall_exp.setBindList(af.createPBindAssistant().getMultipleBindList(node.getBind().clone()));
		forall_exp.setPredicate(chainWithAnd(definePredicates));

		AOrBooleanBinaryExp or_exp = AstExpressionFactory.newAOrBooleanBinaryExp(exists_exp, forall_exp);
		stitch = or_exp;
		valuetree.setPredicate(ctxt.getPredWithContext(or_exp));

	}

}
