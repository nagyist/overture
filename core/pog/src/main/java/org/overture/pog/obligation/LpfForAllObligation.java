package org.overture.pog.obligation;

import java.util.List;

import org.overture.ast.expressions.AExistsExp;
import org.overture.ast.expressions.AForAllExp;
import org.overture.ast.expressions.ANotUnaryExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstExpressionFactory;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.IProofObligation;

public class LpfForAllObligation extends LpfProofObligation implements IProofObligation
{

	private static final long serialVersionUID = 1L;
	
	public LpfForAllObligation(AForAllExp node, PExp predExp,
			List<PExp> definedPredicates, IPOContextStack ctxt)
	{
		super(node, POType.LPF_FORALL, ctxt, node.getLocation());

		AExistsExp exists_exp = new AExistsExp();
		exists_exp.setBindList(cloneListMultipleBind(node.getBindList()));
		ANotUnaryExp not_exp = new ANotUnaryExp();
		not_exp.setExp(predExp.clone());
		exists_exp.setPredicate(not_exp);

		AForAllExp forAll_exp = new AForAllExp();
		forAll_exp.setBindList(cloneListMultipleBind(node.getBindList()));
		forAll_exp.setPredicate(chainWithAnd(definedPredicates));

		PExp or_exp = AstExpressionFactory.newAOrBooleanBinaryExp(exists_exp, forAll_exp);

		stitch = or_exp;

		valuetree.setPredicate(ctxt.getPredWithContext(or_exp));
		
	}





}
