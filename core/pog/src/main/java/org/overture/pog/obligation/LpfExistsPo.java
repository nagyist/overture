package org.overture.pog.obligation;

import java.util.List;

import org.overture.ast.expressions.AExistsExp;
import org.overture.ast.expressions.AForAllExp;
import org.overture.ast.expressions.AOrBooleanBinaryExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstExpressionFactory;
import org.overture.pog.pub.IPOContextStack;

public class LpfExistsPo extends LpfProofObligation
{

	private static final long serialVersionUID = 1L;

	public LpfExistsPo(AExistsExp node, List<PExp> definePredicates,
			IPOContextStack ctxt)
	{
		super(node, POType.LPF_EXISTS, ctxt, node.getLocation());

		AExistsExp exists_exp = new AExistsExp();

		exists_exp.setBindList(cloneListMultipleBind(node.getBindList()));
		exists_exp.setPredicate(node.getPredicate().clone());

		AForAllExp forall_exp = new AForAllExp();
		forall_exp.setBindList(cloneListMultipleBind(node.getBindList()));
		forall_exp.setPredicate(chainWithAnd(definePredicates));

		AOrBooleanBinaryExp or_exp = AstExpressionFactory.newAOrBooleanBinaryExp(exists_exp, forall_exp);
		stitch = or_exp;
		valuetree.setPredicate(ctxt.getPredWithContext(or_exp));
	}

}
