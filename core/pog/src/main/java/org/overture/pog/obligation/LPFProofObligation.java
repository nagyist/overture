package org.overture.pog.obligation;

import java.util.List;

import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstExpressionFactory;
import org.overture.ast.intf.lex.ILexLocation;
import org.overture.ast.node.INode;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.POType;

/**
 * Main class to mark LPF-style proof obligations. It's an intermediate
 * class between the instantiable POs and {@link ProofObligation}.
 * 
 * @author ldc
 *
 */
public abstract class LPFProofObligation extends ProofObligation
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**
 * Main constructor. Just passes the arguments along to {@link ProofObligation}.
 * 
 * @param node The ast node generating this PO
 * @param kind The {@link POType} of this PO
 * @param ctxt THe contextual information to be used when constructing the final predicate
 * @param loc The location of the node generating the PO
 */
	public LPFProofObligation(INode node, POType kind,
			IPOContextStack ctxt, ILexLocation loc)
	{
		super(node, kind,ctxt,loc);
	}

	/**
	 * Auxiliary method for constructing the lengthier LPF-style PO predicates.
	 * This method chains a series of definedness predicates PO together into a single one. 
	 * 
	 * Note that this version of the method chains the predicates together by means
	 * of <b>conjunction</b>.
	 * <br><br>
	 * <b>Warning:</b> This method does not clone any of the submitted predicates and
	 * will destroy any existing connections. Pre-clone them if you need to.
	 *
	 * @param defPreds a series of predicates enforcing definedness for a given PO
	 * @return a {@link PExp} prepresenting the chained predicates.
	 */
	protected PExp chainWithAnd(List<PExp> defPreds)
	{
		if (defPreds.size()==1){
			return defPreds.get(0).clone();
		}
		
		else{
			PExp left_exp = defPreds.get(0).clone();
			PExp right_exp = chainWithAnd(defPreds.subList(1, defPreds.size()));
			PExp and_exp = AstExpressionFactory.newAAndBooleanBinaryExp(left_exp, right_exp);
			return and_exp;
		}
	
	}
	
}
