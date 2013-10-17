package org.overture.pog.obligation;

import java.util.List;

import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstExpressionFactory;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.POType;

/**
 * Class representing a LPF-aware proof obligation for a composite
 * predicate with a conjunction.
 * 
 * In LPF, an expression of form 'A and B' is defined if:
 * <br><br>
 * 'A=false' or 'B=false' or 'A is defined <b>and</b> B is defined'
 * 
 * @author ldc
 *
 */
		
public class LPFAndPO extends LPFProofObligation
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
	 * @param lExp the left side of the 'and' expression
	 * @param rExp the right side of the 'and' expression
	 * @param definedPredicates a series of 1 or more predicates enforcing the definedness of the overall expression
	 * @param ctxt contextual information (variable scopes) to help generate the final PO
	 */
	public LPFAndPO(PExp andExp, PExp lExp, PExp rExp, List<PExp> definedPredicates, IPOContextStack ctxt)
	{
		super(andExp, POType.LPF_AND, ctxt, andExp.getLocation());
		
		PExp lfalse_exp = AstExpressionFactory.newAEqualsBinaryExp(lExp.clone(), AstExpressionFactory.newAFalseConstExp());
		PExp rfalse_exp = AstExpressionFactory.newAEqualsBinaryExp(rExp.clone(), AstExpressionFactory.newAFalseConstExp());
		
		PExp defs_exp = chainWithAnd(definedPredicates);
		
		PExp or1_exp = AstExpressionFactory.newAOrBooleanBinaryExp(lfalse_exp, rfalse_exp);
		PExp or2_exp = AstExpressionFactory.newAOrBooleanBinaryExp(or1_exp, defs_exp);
		
		stitch=or2_exp;
		
		valuetree.setPredicate(ctxt.getPredWithContext(or2_exp));
		
		
	}



}
