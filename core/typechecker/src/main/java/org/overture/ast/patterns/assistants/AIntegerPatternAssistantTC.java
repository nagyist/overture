package org.overture.ast.patterns.assistants;

import org.overture.ast.expressions.AIntLiteralExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.patterns.AIntegerPattern;
import org.overture.ast.types.PType;
import org.overture.ast.types.assistants.SNumericBasicTypeAssistantTC;
import org.overturetool.vdmj.lex.LexIntegerToken;

public class AIntegerPatternAssistantTC {

	public static PType getPossibleTypes(AIntegerPattern pattern) {
		return SNumericBasicTypeAssistantTC.typeOf(pattern.getValue().value, pattern.getLocation());
	}

	public static PExp getMatchingExpression(AIntegerPattern intptrn) {
		return new AIntLiteralExp(null, intptrn.getLocation(), (LexIntegerToken) intptrn.getValue().clone());
	}

}
