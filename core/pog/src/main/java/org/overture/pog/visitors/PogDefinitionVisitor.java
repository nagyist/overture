package org.overture.pog.visitors;

import org.overture.ast.analysis.QuestionAnswerAdaptor;
import org.overture.pog.obligation.POContextStack;
import org.overture.pog.obligation.ProofObligationList;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.IProofObligationList;

public class PogDefinitionVisitor extends
		PogParamDefinitionVisitor<POContextStack, ProofObligationList>
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -4015296332732118274L;

    public PogDefinitionVisitor( QuestionAnswerAdaptor<IPOContextStack, ? extends IProofObligationList> parentVisitor){
	super(parentVisitor);
    }
}