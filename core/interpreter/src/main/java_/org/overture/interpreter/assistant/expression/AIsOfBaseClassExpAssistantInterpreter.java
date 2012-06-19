package org.overture.interpreter.assistant.expression;

import org.overture.ast.expressions.AIsOfBaseClassExp;
import org.overture.ast.expressions.PExp;
import org.overture.interpreter.runtime.ObjectContext;
import org.overture.interpreter.values.ValueList;
import org.overture.typechecker.assistant.expression.AIsOfBaseClassExpAssistantTC;

public class AIsOfBaseClassExpAssistantInterpreter extends
		AIsOfBaseClassExpAssistantTC
{

	public static ValueList getValues(AIsOfBaseClassExp exp, ObjectContext ctxt)
	{
		return PExpAssistantInterpreter.getValues(exp.getExp(),ctxt);
	}

	public static PExp findExpression(AIsOfBaseClassExp exp, int lineno)
	{
		PExp found = PExpAssistantInterpreter.findExpression(exp,lineno);
		if (found != null) return found;

		return PExpAssistantInterpreter.findExpression(exp.getExp(),lineno);
	}

}