package org.overture.codegen.trans;

import java.util.List;

import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.SStmCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AClassDeclCG;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;
import org.overture.codegen.cgast.declarations.AFormalParamLocalParamCG;
import org.overture.codegen.cgast.declarations.AInterfaceDeclCG;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.cgast.declarations.ARecordDeclCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.AFieldExpCG;
import org.overture.codegen.cgast.expressions.AMapSeqGetExpCG;
import org.overture.codegen.cgast.expressions.ASelfExpCG;
import org.overture.codegen.cgast.expressions.SVarExpCG;
import org.overture.codegen.cgast.statements.AAssignToExpStmCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.statements.ACallObjectExpStmCG;
import org.overture.codegen.cgast.statements.AInvCheckStmCG;
import org.overture.codegen.cgast.statements.AMapSeqUpdateStmCG;
import org.overture.codegen.cgast.types.AMethodTypeCG;
import org.overture.codegen.cgast.types.AVoidTypeCG;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.trans.assistants.TransAssistantCG;
import org.overture.codegen.trans.conv.StateDesignatorToExpCG;

/**
 * Incorporates the state invariant checks into the IR. It is important that this transformation is applied post the
 * union type transformation. Consider the scope of this transformation. If it takes care of all types it cannot be
 * named 'State' TODO: Why? explain above
 * 
 * @see AssignStmTransformation
 * @see StateDesignatorToExpCG
 * @author pvj
 */
public class StateInvTransformation extends DepthFirstAnalysisAdaptor
{

	// TODO: take atomic statement into account!
	// TODO: Remember to set a type on the self expression
	// TODO: Hvad med invariant check ved construction? mk_A(1,2) eller new MyClass(1,2,3)

	private IRInfo info;
	private List<AClassDeclCG> classes;
	private TransAssistantCG transAssistant;


	public StateInvTransformation(IRInfo info, TransAssistantCG transAssistant, List<AClassDeclCG> classes)
	{
		this.info = info;
		this.transAssistant = transAssistant;
		this.classes = classes;
	}
	

	@Override
	public void inARecordDeclCG(ARecordDeclCG node) throws AnalysisException
	{
		if(!info.getSettings().generateInvariantChecks())
		{
			return;
		}
		
		for(AFieldDeclCG field : node.getFields())
		{
			node.getMethods().add(consSetter(field));
		}
	}

	@Override
	public void inAClassDeclCG(AClassDeclCG node) throws AnalysisException
	{
		if(!info.getSettings().generateInvariantChecks())
		{
			return;
		}
		
		for(AFieldDeclCG field : node.getFields())
		{
			node.getMethods().add(consSetter(field));
		}
		
		for(AClassDeclCG r : node.getInnerClasses())
		{
			r.apply(this);
		}
	}

	//
	// TODO: Does the concurrency transformation consider this as a state change?
	//
	@Override
	public void caseAMapSeqUpdateStmCG(AMapSeqUpdateStmCG node)
			throws AnalysisException
	{
		// Examples below - notice that updates can be both to maps and sequences:
		//
		// m(5) := true;
		//
		// case 1) Either 'm' is local and then there is nothing to check
		// case 2) Or 'm' is a field (self.m) and then the invariant check must be run for 'self'
		//
		// a.b.c(5) := 89;
		//
		// case 3) Determine type of a.b and find and evaluate the invariant for it (if there)
		//
		// m(1)(2) := true (notice that only the last (right-most) application is the 'target' of the assignment)
		//
		// case 4) For self.m(1)(2) := true is a modification to field m so run invariant check on 'self
		// case 5) for the case where 'm' is local m(1)(2) does not require an invariant check to be run
		//
		//
		// a.m(1)(2)
		//
		// case 6) if 'a' is NOT local then run invariant check on 'a'
		//
		// General approach: Find first dot <exp1>.<exp2>(n) := <exp3>
		//
		// Run invariant check on <exp1>
		//

		if (checkNotNeeded(node))
		{
			return;
		}

		// TODO: check if 'self' is cleared out for state designators

		// SExpCG next = node.getCol();
		//
		// while(true)
		// {
		// if(next instanceof SVarExpCG)
		// {
		// break;
		// }
		// if(next instanceof AFieldExpCG)
		// {
		// next = ((AFieldExpCG) next).getObject();
		// }else if(next instanceof AMapSeqGetExpCG)
		// {
		// next = ((AMapSeqGetExpCG) next).getCol();
		// }
		// else
		// {
		// Logger.getLog().printError("Expected field expression, map sequence "
		// + "get expression or variable expression "
		// + "in '" + this.getClass().getSimpleName() + "'. Got: " + node.getClass().getSimpleName());
		// return;
		// }
		// }

		injectInvCheck(node, new ASelfExpCG());
	}

	@Override
	public void caseAAssignToExpStmCG(AAssignToExpStmCG node)
			throws AnalysisException
	{
		if (checkNotNeeded(node))
		{
			return;
		}

		SExpCG target = node.getTarget();

		if (target instanceof SVarExpCG)
		{
			// Example: x := 5;

			SVarExpCG var = (SVarExpCG) target;

			if (var.getIsLocal())
			{
				// The variable is local so there is no invariant to be checked
			} else
			{
				// The variable is NOT local so the invariant (if there) needs to be checked

				injectInvCheck(node, new ASelfExpCG());
			}

		} else if (target instanceof AFieldExpCG)
		{
			// Example: x.f := 5;

			// If x is a record then check the record invariant (if there)

			// If x is a class check the class invariant (if there)

			handleFieldSet(node, (AFieldExpCG) target);

		} else
		{
			// TODO: consider injecting
			Logger.getLog().printErrorln("Expected variable expression or field variable expression in '"
					+ this.getClass().getName() + "'. Got: " + target);
		}
	}
	
	private void handleFieldSet(AAssignToExpStmCG assign, AFieldExpCG fieldExp)
	{
//		AFieldExpCG m = new AFieldExpCG();
//		m.setMemberName("set_" + fieldExp.getMemberName());
//		m.setObject(fieldExp.getObject().clone());
//		m.setSourceNode(fieldExp.getSourceNode());
//		m.setTag(fieldExp.getTag());
//		m.setType(fieldExp.getType().clone());
//		
		if(assign.parent() != null)
		{
			ACallObjectExpStmCG callObj = new ACallObjectExpStmCG();
			callObj.setFieldName("set_" + fieldExp.getMemberName()); // FIXME: do it right
			callObj.getArgs().add(assign.getExp().clone());
			callObj.setObj(fieldExp.getObject().clone());
			callObj.setSourceNode(assign.getSourceNode());
			callObj.setTag(assign.getTag());
			callObj.setType(new AVoidTypeCG());
			
			assign.parent().replaceChild(assign, callObj);
		}
		else
		{
			//TODO: proper error handling
		}
	}
	
	private boolean checkNotNeeded(SStmCG node)
	{
		return !info.getSettings().generateInvariantChecks()
				|| info.getStmAssistant().inAtomic(node);
	}

	private void injectInvCheck(SStmCG node, SExpCG next)
	{

		if (node.parent() != null)
		{
			AInvCheckStmCG check = new AInvCheckStmCG();
			check.setSubject(next.clone());

			ABlockStmCG replacementBlock = new ABlockStmCG();

			node.parent().replaceChild(node, replacementBlock);

			replacementBlock.getStatements().add(node);
			replacementBlock.getStatements().add(check);
		} else
		{
			// FIXME: proper error handling
		}
	}
	
	private AMethodDeclCG consSetter(AFieldDeclCG field)
	{
		AMethodDeclCG setter = new AMethodDeclCG();
		setter.setAbstract(false);
		setter.setAccess("public"); //TODO: improve
		setter.setAsync(false);
		
		AAssignToExpStmCG assignExp = new AAssignToExpStmCG();
		assignExp.setTarget(transAssistant.consIdentifierVar(field.getName(), field.getType().clone()));
		assignExp.setExp(transAssistant.consIdentifierVar("p" + field.getName(), field.getType().clone())); //TODO: Improve
		
		setter.setBody(assignExp);
		setter.setIsConstructor(false);
		
		AMethodTypeCG methodType = new AMethodTypeCG();
		methodType.setResult(new AVoidTypeCG());
		methodType.getParams().add(field.getType().clone());
		
		AFormalParamLocalParamCG p = new AFormalParamLocalParamCG();
		p.setPattern(transAssistant.consIdPattern("p" + field.getName())); //TODO: improve
		p.setType(field.getType().clone());
		setter.getFormalParams().add(p);
		
		setter.setMethodType(methodType);
		setter.setName("set_" + field.getName());
		setter.setPostCond(null);
		setter.setPreCond(null);
		setter.setSourceNode(null);
		setter.setStatic(false);
		setter.setTag(null);
		//setter.setTemplateTypes(value);
		
		// TODO Auto-generated method stub
		return setter;
	}
}
