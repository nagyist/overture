package org.overture.pog.visitors;

import java.util.LinkedList;
import java.util.List;

import org.overture.ast.expressions.AEqualsBinaryExp;
import org.overture.ast.expressions.AExistsExp;
import org.overture.ast.expressions.AForAllExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstExpressionFactory;
import org.overture.ast.patterns.PMultipleBind;
import org.overture.pog.obligation.LPFProofObligation;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.IProofObligation;
import org.overture.pog.pub.POType;

public class LPFForAllPO extends LPFProofObligation implements IProofObligation
{

	public LPFForAllPO(AForAllExp node, PExp predExp,
			List<PExp> definedPredicates, IPOContextStack ctxt)
	{
		super(node, POType.LPF_FORALL, ctxt, node.getLocation());

		
		
		AExistsExp exists_exp = new AExistsExp();
		exists_exp.setBindList(clonePMBindList(node.getBindList()));
		AEqualsBinaryExp equals_exp = AstExpressionFactory.newAEqualsBinaryExp(predExp.clone(), AstExpressionFactory.newAFalseConstExp());
		exists_exp.setPredicate(equals_exp);

		AForAllExp forAll_exp = new AForAllExp();
		forAll_exp.setBindList(clonePMBindList(node.getBindList()));
		forAll_exp.setPredicate(chainWithAnd(definedPredicates));

		PExp or_exp = AstExpressionFactory.newAOrBooleanBinaryExp(exists_exp, forAll_exp);

		stitch = or_exp;

		valuetree.setPredicate(ctxt.getPredWithContext(or_exp));
		
	}

	private List<? extends PMultipleBind> clonePMBindList(
			LinkedList<PMultipleBind> bindList)
	{
		List<PMultipleBind> r = new LinkedList<PMultipleBind>();
		for (PMultipleBind bind : bindList)
		{
			r.add(bind.clone());
		}
		return r;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
