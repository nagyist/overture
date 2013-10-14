package org.overture.pog.obligation;

import java.util.List;

import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstExpressionFactory;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.POType;

public class LPFAndPO extends ProofObligation
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LPFAndPO(PExp andExp, PExp lExp, PExp rExp, List<PExp> definedPredicates, IPOContextStack ctxt)
	{
		super(andExp, POType.NON_ZERO, ctxt, andExp.getLocation());
		
		PExp false_exp = AstExpressionFactory.newAFalseConstExp();
		PExp lfalse_exp = AstExpressionFactory.newAEqualsBinaryExp(lExp.clone(), false_exp.clone());
		PExp rfalse_exp = AstExpressionFactory.newAEqualsBinaryExp(rExp.clone(), false_exp.clone());
		
		PExp defs_exp = makeAnds(definedPredicates);
		
		PExp  or1_exp = AstExpressionFactory.newAOrBooleanBinaryExp(lfalse_exp, rfalse_exp);
		PExp or2_exp = AstExpressionFactory.newAOrBooleanBinaryExp(or1_exp, defs_exp);
		
		valuetree.setPredicate(ctxt.getPredWithContext(or2_exp));
		
		
	}

	private PExp makeAnds(List<PExp> definedPredicates)
	{
		if (definedPredicates.size()==1){
			return definedPredicates.get(0).clone();
		}
		
		else{
			PExp left_exp = definedPredicates.get(0).clone();
			PExp right_exp = makeAnds(definedPredicates.subList(1, definedPredicates.size()));
			PExp and_exp = AstExpressionFactory.newAAndBooleanBinaryExp(left_exp, right_exp);
			return and_exp;
		}
	
	}


}
