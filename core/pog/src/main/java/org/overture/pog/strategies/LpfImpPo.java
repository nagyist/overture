package org.overture.pog.strategies;

import java.util.List;

import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstExpressionFactory;
import org.overture.pog.obligation.LPFProofObligation;
import org.overture.pog.obligation.POType;
import org.overture.pog.obligation.ProofObligation;
import org.overture.pog.pub.IPOContextStack;

public class LpfImpPo extends LPFProofObligation 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Main constructor. Invokes the super constructor from {@link ProofObligation}, builds the stitch point and sets 
	 * the complete predicate.
	 * 
	 * @param andExp The top level predicate and the node generating the PO
	 * @param lExp the left side of the '=>' expression
	 * @param rExp the right side of the '=>' expression
	 * @param definedPredicates a series of 1 or more predicates enforcing the definedness of the overall expression
	 * @param ctxt contextual information (variable scopes) to help generate the final PO
	 */
	public LpfImpPo(PExp andExp, PExp lExp, PExp rExp, List<PExp> definedPredicates, IPOContextStack ctxt)
	{
		super(andExp, POType.LPF_AND, ctxt, andExp.getLocation());
		
		PExp lfalse_exp = AstExpressionFactory.newAEqualsBinaryExp(lExp.clone(), AstExpressionFactory.newAFalseConstExp());
		PExp rtrue_exp= AstExpressionFactory.newAEqualsBinaryExp(rExp.clone(), AstExpressionFactory.newATrueConstExp());
		
		PExp defs_exp = chainWithAnd(definedPredicates);
		
		PExp or1_exp = AstExpressionFactory.newAOrBooleanBinaryExp(lfalse_exp, rtrue_exp);
		PExp or2_exp = AstExpressionFactory.newAOrBooleanBinaryExp(or1_exp, defs_exp);
		
		stitch=or2_exp;
		
		valuetree.setPredicate(ctxt.getPredWithContext(or2_exp));
		
		
	}
	
}
