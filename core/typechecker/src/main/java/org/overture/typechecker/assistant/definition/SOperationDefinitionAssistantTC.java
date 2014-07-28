package org.overture.typechecker.assistant.definition;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.overture.ast.definitions.AExplicitFunctionDefinition;
import org.overture.ast.definitions.AExplicitOperationDefinition;
import org.overture.ast.definitions.AImplicitOperationDefinition;
import org.overture.ast.definitions.AStateDefinition;
import org.overture.ast.definitions.SOperationDefinition;
import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstFactory;
import org.overture.ast.intf.lex.ILexLocation;
import org.overture.ast.intf.lex.ILexNameToken;
import org.overture.ast.lex.LexNameToken;
import org.overture.ast.patterns.APatternListTypePair;
import org.overture.ast.patterns.PPattern;
import org.overture.ast.typechecker.NameScope;
import org.overture.ast.types.AOperationType;
import org.overture.ast.types.AVoidType;
import org.overture.typechecker.Environment;
import org.overture.typechecker.assistant.ITypeCheckerAssistantFactory;

public class SOperationDefinitionAssistantTC
{
	protected ITypeCheckerAssistantFactory af;

	public SOperationDefinitionAssistantTC(ITypeCheckerAssistantFactory af)
	{
		this.af = af;
	}

	public AExplicitFunctionDefinition getRelyDefinition(
			AImplicitOperationDefinition d, Environment base)
	{

		List<PPattern> plist = new Vector<PPattern>();

		for (APatternListTypePair pl : (LinkedList<APatternListTypePair>) d.getParameterPatterns())
		{
			for (PPattern pp : pl.getPatterns())
			{
				plist.add(pp.clone());
			}
		}

		if (d.getResult() != null)
		{
			plist.add(d.getResult().getPattern().clone());
		}

		PExp body = d.getRelycondition().clone();
		ILexNameToken name = makeImplicitName(d.getName(), "rely_", d.getLocation());

		return getImplicitDefinition(d, base, plist, body, name);
	}

	public AExplicitFunctionDefinition getRelyDefinition(
			AExplicitOperationDefinition d, Environment base)
	{

		List<PPattern> plist = new Vector<PPattern>();

		for (PPattern pp : d.getParameterPatterns())
		{
			plist.add(pp.clone());
		}

		if (!(((AOperationType) d.getType()).getResult() instanceof AVoidType))
		{
			LexNameToken result = new LexNameToken(d.getName().getModule(), "RESULT", d.getLocation());
			plist.add(AstFactory.newAIdentifierPattern(result));
		}

		PExp body = d.getRelycondition();
		ILexNameToken name = makeImplicitName(d.getName(), "rely_", d.getLocation());

		return getImplicitDefinition(d, base, plist, body, name);
	}

	/**
	 * Construct a name for an implict function definition. Override if not using default {@link LexNameToken}.
	 * 
	 * @param opName
	 * @param implicit
	 * @param l
	 * @return
	 */
	protected ILexNameToken makeImplicitName(ILexNameToken opName,
			String implicit, ILexLocation l)
	{
		ILexNameToken name = new LexNameToken(opName.getModule(), implicit
				+ opName.getName(), l);
		return name;

	}

	private AExplicitFunctionDefinition getImplicitDefinition(
			SOperationDefinition d, Environment base, List<PPattern> plist,
			PExp body, ILexNameToken name)
	{

		List<List<PPattern>> parameters = new Vector<List<PPattern>>();

		AStateDefinition state = d.getState();

		if (state != null) // Two args, called Sigma~ and Sigma
		{
			plist.add(AstFactory.newAIdentifierPattern(state.getName().getOldName()));
			plist.add(AstFactory.newAIdentifierPattern(state.getName().clone()));
		} else if (base.isVDMPP())
		{
			// Two arguments called "self~" and "self"
			plist.add(AstFactory.newAIdentifierPattern(d.getName().getSelfName().getOldName()));

			if (!af.createPAccessSpecifierAssistant().isStatic(d.getAccess()))
			{
				plist.add(AstFactory.newAIdentifierPattern(d.getName().getSelfName()));
			}
		}

		parameters.add(plist);

		AExplicitFunctionDefinition def = AstFactory.newAExplicitFunctionDefinition(name, NameScope.GLOBAL, null, af.createAOperationTypeAssistant().getPostType((AOperationType) d.getType(), state, d.getClassDefinition(), af.createPAccessSpecifierAssistant().isStatic(d.getAccess())), parameters, body, null, null, false, null);

		def.setAccess(af.createPAccessSpecifierAssistant().getStatic(d, false));
		def.setClassDefinition(d.getClassDefinition());
		return def;

	}

}
