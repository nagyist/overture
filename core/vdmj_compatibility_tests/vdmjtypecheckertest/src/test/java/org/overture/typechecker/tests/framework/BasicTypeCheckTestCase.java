package org.overture.typechecker.tests.framework;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.overture.vdmjUtils.VdmjCompatibilityUtils;
import org.overturetool.test.util.XmlResultReaderWritter;
import org.overturetool.vdmj.Release;
import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexException;
import org.overturetool.vdmj.lex.LexTokenReader;
import org.overturetool.vdmj.modules.ModuleList;
import org.overturetool.vdmj.syntax.BindReader;
import org.overturetool.vdmj.syntax.ClassReader;
import org.overturetool.vdmj.syntax.ExpressionReader;
import org.overturetool.vdmj.syntax.ModuleReader;
import org.overturetool.vdmj.syntax.ParserException;
import org.overturetool.vdmj.syntax.PatternReader;
import org.overturetool.vdmj.syntax.StatementReader;
import org.overturetool.vdmj.syntax.SyntaxReader;
import org.overturetool.vdmj.syntax.TypeReader;
import org.overturetool.vdmj.typechecker.ModuleTypeChecker;
import org.overturetool.vdmj.typechecker.TypeChecker;
import org.overturetool.vdmj.types.BracketType;
import org.overturetool.vdmj.types.Type;

public abstract class BasicTypeCheckTestCase extends TestCase {
	public final static boolean DEBUG = false;

	public enum ParserType {
		Expression, Expressions, Module, Class, Pattern, Type, Statement, Bind
	}

	private File file;

	public BasicTypeCheckTestCase(String string) {
		super(string);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Settings.dialect = Dialect.VDM_PP;
		Settings.release = Release.VDM_10;
	}

	public <T> T parse(ParserType type, File file) throws ParserException,
			LexException {
		this.file = file;
		return internal(type, new LexTokenReader(file, Settings.dialect));
	}

	public <T> T parse(ParserType type, String content) throws ParserException,
			LexException {
		return internal(type, new LexTokenReader(content, Settings.dialect));
	}

	private <T> T internal(ParserType type, LexTokenReader ltr)
			throws ParserException, LexException {
		SyntaxReader reader = null;
		T result = null;
		String errorMessages = "";
		try {
			reader = getReader(type, ltr);
			result = read(type, reader);
			XmlResultReaderWritter xmlResult = new XmlResultReaderWritter(file);
			if (reader != null && reader.getErrorCount() > 0) {
				xmlResult.setResult("parser", VdmjCompatibilityUtils.convertToResult(reader,file,"vdmj type checker"));
			}
			else
			{
				if(reader instanceof ModuleReader)
				{
					ModuleTypeChecker moduleTC = new ModuleTypeChecker((ModuleList) result);
					moduleTC.typeCheck();
					xmlResult.setResult("type_checker", VdmjCompatibilityUtils.convertToResult(moduleTC,file,"vdmj type checker"));					
				}
			}
			try {
				xmlResult.saveInXml();
				
				xmlResult.loadFromXml();
			} catch (Exception e)
			{
				//e.printStackTrace();
			} 
			
			return result;	
		} finally {
			// if (!hasRunBefore())
			// {
			// setHasRunBefore(true);
			// System.out.println("============================================================================================================");
			//
			// System.out.println("|");
			// System.out.println("|\t\t" + getReaderTypeName() + "s");
			// // System.out.println("|");
			// System.out.println("|___________________________________________________________________________________________________________");
			//
			// }
			// System.out.println(pad("Parsed " + getReaderTypeName(), 20) +
			// " - "
			// + pad(getReturnName(result), 35) + ": "
			// + pad(result + "", 35).replace('\n', ' ') + " from \""
			// + (content + "").replace('\n', ' ') + "\"");
			// System.out.flush();
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T read(ParserType type, SyntaxReader reader)
			throws ParserException, LexException {
		switch (type) {
		case Class:
			return (T) ((ClassReader) reader).readClasses();
		case Expression:
			return (T) ((ExpressionReader) reader).readExpression();
		case Expressions:
			return (T) ((ExpressionReader) reader).readExpressionList();
		case Module:
			return (T) ((ModuleReader) reader).readModules();
		case Pattern:
			return (T) ((PatternReader) reader).readPatternList();
		case Statement:
			return (T) ((StatementReader) reader).readStatement();
		case Type:
			return (T) ((TypeReader) reader).readType();
		case Bind:
			return (T) ((BindReader) reader).readBindList();

		}
		return null;
	}

	private SyntaxReader getReader(ParserType type, LexTokenReader ltr) {
		switch (type) {
		case Class:
			return new ClassReader(ltr);
		case Expression:
			return new ExpressionReader(ltr);
		case Module:
			return new ModuleReader(ltr);
		case Pattern:
			return new PatternReader(ltr);
		case Statement:
			return new StatementReader(ltr);
		case Type:
			return new TypeReader(ltr);
		case Bind:
			return new BindReader(ltr);

		}
		return null;
	}

	protected Type getResultType(String expectedTypeString)
			throws ParserException, LexException {
		if (expectedTypeString != null && !expectedTypeString.trim().isEmpty()) {
			Type expectedType = parse(ParserType.Type, expectedTypeString);
			expectedType = removeBrackets(expectedType);
			return expectedType;
		}
		return null;
	}

	private Type removeBrackets(Type type) {
		if (type instanceof BracketType) {
			return removeBrackets(((BracketType) type).type);
		}
		return type;
	}
}