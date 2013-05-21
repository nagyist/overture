package org.overture.codegen.mergevisitor;

import java.io.StringWriter;
import java.util.LinkedList;

import org.apache.velocity.Template;
import org.overture.codegen.cgast.AClassCG;
import org.overture.codegen.cgast.AFieldCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.QuestionAdaptor;
import org.overture.codegen.cgast.expressions.ACastUnaryExpCG;
import org.overture.codegen.cgast.expressions.ACharLiteralExpCG;
import org.overture.codegen.cgast.expressions.ADivideNumericBinaryExpCG;
import org.overture.codegen.cgast.expressions.AIntLiteralExpCG;
import org.overture.codegen.cgast.expressions.AIsolationUnaryExpCG;
import org.overture.codegen.cgast.expressions.AMinusUnaryExpCG;
import org.overture.codegen.cgast.expressions.APlusUnaryExpCG;
import org.overture.codegen.cgast.expressions.ARealLiteralExpCG;
import org.overture.codegen.cgast.expressions.PExpCG;
import org.overture.codegen.cgast.expressions.SNumericBinaryExpCG;
import org.overture.codegen.cgast.types.ACharBasicTypeCG;
import org.overture.codegen.cgast.types.AIntNumericBasicTypeCG;
import org.overture.codegen.cgast.types.ARealNumericBasicTypeCG;
import org.overture.codegen.constants.ITextConstants;
import org.overture.codegen.templates.TemplateManager;
import org.overture.codegen.templates.TemplateParameters;
import org.overture.codegen.visitor.CodeGenContext;

public class MergeVisitor extends QuestionAdaptor<StringWriter>
{

	private static final long serialVersionUID = 1356835559835525016L;

	private TemplateManager templates;

	private MergeAssistant mergeAssistant;

	public MergeVisitor()
	{
		this.templates = new TemplateManager();
		this.mergeAssistant = new MergeAssistant(this);
	}

	public TemplateManager getTemplateManager()
	{
		return templates;
	}

	@Override
	public void caseAClassCG(AClassCG node, StringWriter question)
			throws AnalysisException
	{
		CodeGenContext context = new CodeGenContext();

		context.put(TemplateParameters.CLASS_NAME, node.getName());
		context.put(TemplateParameters.CLASS_ACCESS, node.getAccess());

		LinkedList<AFieldCG> fields = node.getFields();
		StringWriter generatedField = new StringWriter();

		for (AFieldCG field : fields)
		{
			field.apply(this, generatedField);
		}

		context.put(TemplateParameters.FIELDS, generatedField.toString());

		Template classTemplate = templates.getTemplate(node.getClass());
		classTemplate.merge(context.getVelocityContext(), question);
	}

	@Override
	public void caseAFieldCG(AFieldCG node, StringWriter question)
			throws AnalysisException
	{
		String access = node.getAccess();
		boolean isStatic = node.getStatic();
		boolean isFinal = node.getFinal();
		String name = node.getName();

		StringWriter expWriter = new StringWriter();
		node.getInitial().apply(this, expWriter);

		StringWriter typeWriter = new StringWriter();
		node.getType().apply(this, typeWriter);

		CodeGenContext context = new CodeGenContext();

		context.put(TemplateParameters.FIELD_ACCESS, access);
		context.put(TemplateParameters.FIELD_STATIC, isStatic);
		context.put(TemplateParameters.FIELD_FINAL, isFinal);
		context.put(TemplateParameters.FIELD_TYPE, typeWriter.toString());
		context.put(TemplateParameters.FIELD_NAME, name);
		context.put(TemplateParameters.FIELD_INITIAL, expWriter.toString());

		Template classTemplate = templates.getTemplate(node.getClass());

		question.append(ITextConstants.INDENT);
		classTemplate.merge(context.getVelocityContext(), question);
		question.append(ITextConstants.NEW_LINE);
	}

	// Numeric binary
	@Override
	public void defaultSNumericBinaryExpCG(SNumericBinaryExpCG node,
			StringWriter question) throws AnalysisException
	{
		mergeAssistant.handleBinaryExp(node, question);
	}
	
	@Override
	public void caseAIsolationUnaryExpCG(AIsolationUnaryExpCG node,
			StringWriter question) throws AnalysisException
	{
		CodeGenContext context = new CodeGenContext();
		
		StringWriter expWriter = new StringWriter();
		node.getExp().apply(this, expWriter);
		context.put(TemplateParameters.UNARY_EXP_VALUE, expWriter.toString());
		
		Template isolationTemplate = templates.getTemplate(node.getClass());
		isolationTemplate.merge(context.getVelocityContext(), question);
	}
	
//	@Override
//	public void caseADivideNumericBinaryExpCG(ADivideNumericBinaryExpCG node,
//			StringWriter question) throws AnalysisException
//	{
//		final PExpCG leftNode = node.getLeft();
//		final PExpCG rightNode = node.getRight();
//
//		mergeAssistant.handleBinaryExp(node, question, new IContextManipulator()
//		{
//			@Override
//			public void manipulate(CodeGenContext context)
//			{
//				boolean bothOperandsAreInts = mergeAssistant.isIntegerType(leftNode) && mergeAssistant.isIntegerType(rightNode);
//				context.put(TemplateParameters.DIVIDE_BOTH_OPERANDS_INTS, bothOperandsAreInts);
//			}
//		});
//	}

	// OLD BACKUP OF THE ABOVE:
	// @Override
	// public String caseADivideNumericBinaryExp(ADivideNumericBinaryExp node,
	// CodeGenContextMap question) throws AnalysisException
	// {
	// PExp leftNode = node.getLeft();
	// PExp rightNode = node.getRight();
	//
	// if(expAssistant.isIntegerType(leftNode) && expAssistant.isIntegerType(rightNode))
	// {
	// //We wrap it as a double because expressions like 1/2 equals 0 in Java whereas 1/2 = 0.5 in VDM
	// String left = "new Double(" + expAssistant.formatExp(node, node.getLeft(), question) + ")";
	// String operator = opLookup.find(node.getClass()).getMapping();
	// String right = expAssistant.formatExp(node, node.getRight(), question);
	//
	// return left + " " + operator + " " + right;
	// }
	//
	// return super.caseADivideNumericBinaryExp(node, question);
	// }
	// Unary

	@Override
	public void caseACastUnaryExpCG(ACastUnaryExpCG node, StringWriter question)
			throws AnalysisException
	{
		CodeGenContext context = new CodeGenContext();

		StringWriter typeWriter = new StringWriter();
		node.getType().apply(this, typeWriter);
		context.put(TemplateParameters.CAST_EXP_TYPE, typeWriter.toString());
		
		StringWriter expWriter = new StringWriter();
		node.getExp().apply(this, expWriter);
		context.put(TemplateParameters.UNARY_EXP_VALUE, expWriter.toString());

		Template classTemplate = templates.getTemplate(node.getClass());
		classTemplate.merge(context.getVelocityContext(), question);
	}
	
	@Override
	public void caseAPlusUnaryExpCG(APlusUnaryExpCG node, StringWriter question)
			throws AnalysisException
	{
		mergeAssistant.handleUnaryExp(node, question);
	}

	@Override
	public void caseAMinusUnaryExpCG(AMinusUnaryExpCG node,
			StringWriter question) throws AnalysisException
	{
		mergeAssistant.handleUnaryExp(node, question);
	}

	@Override
	public void caseAIntLiteralExpCG(AIntLiteralExpCG node,
			StringWriter question) throws AnalysisException
	{
		question.append(node.getValue());// FIXME: Formatting of literal expressions
	}

	@Override
	public void caseARealLiteralExpCG(ARealLiteralExpCG node,
			StringWriter question) throws AnalysisException
	{
		question.append(node.getValue());// FIXME: Formatting of literal expressions
	}

	@Override
	public void caseACharLiteralExpCG(ACharLiteralExpCG node,
			StringWriter question) throws AnalysisException
	{
		final char QUOTE = '\'';
		question.append(QUOTE + node.getValue() + QUOTE);// FIXME: Formatting of literal expressions
	}

	// Basic numeric types

	@Override
	public void caseAIntNumericBasicTypeCG(AIntNumericBasicTypeCG node,
			StringWriter question) throws AnalysisException
	{
		mergeAssistant.handleBasicType(node, question);
	}

	@Override
	public void caseARealNumericBasicTypeCG(ARealNumericBasicTypeCG node,
			StringWriter question) throws AnalysisException
	{
		mergeAssistant.handleBasicType(node, question);
	}

	@Override
	public void caseACharBasicTypeCG(ACharBasicTypeCG node,
			StringWriter question) throws AnalysisException
	{
		mergeAssistant.handleBasicType(node, question);
	}

}
