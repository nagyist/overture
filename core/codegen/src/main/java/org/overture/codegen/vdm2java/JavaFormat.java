package org.overture.codegen.vdm2java;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import org.overture.codegen.assistant.ExpAssistantCG;
import org.overture.codegen.assistant.TypeAssistantCG;
import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.declarations.AClassDeclCG;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;
import org.overture.codegen.cgast.declarations.AFormalParamLocalDeclCG;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.cgast.declarations.ARecordDeclCG;
import org.overture.codegen.cgast.expressions.AElemsUnaryExpCG;
import org.overture.codegen.cgast.expressions.AEnumSeqExpCG;
import org.overture.codegen.cgast.expressions.AEqualsBinaryExpCG;
import org.overture.codegen.cgast.expressions.AFieldExpCG;
import org.overture.codegen.cgast.expressions.AFieldNumberExpCG;
import org.overture.codegen.cgast.expressions.ANewExpCG;
import org.overture.codegen.cgast.expressions.ANotEqualsBinaryExpCG;
import org.overture.codegen.cgast.expressions.ANotUnaryExpCG;
import org.overture.codegen.cgast.expressions.AVariableExpCG;
import org.overture.codegen.cgast.expressions.PExpCG;
import org.overture.codegen.cgast.expressions.SBinaryExpCGBase;
import org.overture.codegen.cgast.statements.AAssignmentStmCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.statements.AIdentifierStateDesignatorCG;
import org.overture.codegen.cgast.statements.AIfStmCG;
import org.overture.codegen.cgast.statements.AReturnStmCG;
import org.overture.codegen.cgast.statements.PStmCG;
import org.overture.codegen.cgast.types.ABoolBasicTypeCG;
import org.overture.codegen.cgast.types.ACharBasicTypeCG;
import org.overture.codegen.cgast.types.AIntNumericBasicTypeCG;
import org.overture.codegen.cgast.types.AObjectTypeCG;
import org.overture.codegen.cgast.types.ARealNumericBasicTypeCG;
import org.overture.codegen.cgast.types.ARecordTypeCG;
import org.overture.codegen.cgast.types.ASetSetTypeCG;
import org.overture.codegen.cgast.types.ATupleTypeCG;
import org.overture.codegen.cgast.types.AVoidTypeCG;
import org.overture.codegen.cgast.types.PTypeCG;
import org.overture.codegen.cgast.types.SBasicTypeCGBase;
import org.overture.codegen.cgast.types.SSeqTypeCGBase;
import org.overture.codegen.merging.MergeVisitor;

public class JavaFormat
{
	public static String format(INode node) throws AnalysisException
	{		
		MergeVisitor mergeVisitor = new MergeVisitor(JavaCodeGen.JAVA_TEMPLATE_STRUCTURE, JavaCodeGen.TEMPLATE_CALLABLES);
		StringWriter writer = new StringWriter();
		node.apply(mergeVisitor, writer);

		return writer.toString();
	}
	
	public static String format(PExpCG exp, boolean leftChild) throws AnalysisException
	{
		MergeVisitor mergeVisitor = new MergeVisitor(JavaCodeGen.JAVA_TEMPLATE_STRUCTURE, JavaCodeGen.TEMPLATE_CALLABLES);
		StringWriter writer = new StringWriter();

		exp.apply(mergeVisitor, writer);
		String formattedExp = writer.toString();
		
		JavaPrecedence precedence = new JavaPrecedence();
		
		INode parent = exp.parent();
		
		if(!(parent instanceof PExpCG))
			return formattedExp;
		
		boolean isolate = precedence.mustIsolate((PExpCG) parent, exp, leftChild);
		
		return isolate ? "(" + formattedExp + ")" : formattedExp;
	}
	
	public static String formatUnary(PExpCG exp) throws AnalysisException
	{
		return format(exp, false);
	}
	
	public static String formatElementType(AElemsUnaryExpCG exp) throws AnalysisException
	{
		PTypeCG type = exp.getType();
		
		if(type instanceof ASetSetTypeCG)
		{
			ASetSetTypeCG seqType = (ASetSetTypeCG) type;
			return formatTemplateParam(seqType.getSetOf());
		}
		
		throw new AnalysisException("Type was not a sequence type!");
	}
	
	public static String generateCloneMethod(ARecordDeclCG record) throws AnalysisException
	{
		AMethodDeclCG method = new AMethodDeclCG();
		method.setAccess("public");

		ARecordTypeCG returnType = new ARecordTypeCG();
		returnType.setRecDecl(record.clone());
		method.setReturnType(returnType);
		
		method.setName("clone");
		
		ANewExpCG newExp = new ANewExpCG();
		newExp.setType(new ARecordTypeCG());
		newExp.setClassName(record.getName());
		LinkedList<PExpCG> args = newExp.getArgs();
		
		
		LinkedList<AFieldDeclCG> fields = record.getFields();
		for (AFieldDeclCG field : fields)
		{
			String name = field.getName();
			
			AVariableExpCG varExp = new AVariableExpCG();
			varExp.setOriginal(name);
			varExp.setType(field.getType().clone());
			args.add(varExp);
		}
		
		AReturnStmCG body = new AReturnStmCG();
		body.setExp(newExp);
		method.setBody(body);
		
		return format(method);
	}
	
	public static String formatRecordConstructor(ARecordDeclCG record) throws AnalysisException
	{
		LinkedList<AFieldDeclCG> fields = record.getFields();
		
		AMethodDeclCG constructor = new AMethodDeclCG();
		//Since Java does not have records but the OO AST does a record is generated as a Java class.
		//To make sure that the record can be instantiated we must explicitly add a constructor.
		constructor.setAccess("public");
		constructor.setIsConstructor(true);
		constructor.setName(record.getName()); //TODO: Should rather be a reference for the class instead
		LinkedList<AFormalParamLocalDeclCG> formalParams = constructor.getFormalParams();
	
		ABlockStmCG body = new ABlockStmCG();
		LinkedList<PStmCG> bodyStms = body.getStatements();
		constructor.setBody(body); 
		
		for (AFieldDeclCG field : fields)
		{
			String name = field.getName();
			PTypeCG type = field.getType().clone();
			
			String paramName = "_" + name;

			//Construct formal parameter of the constructor
			AFormalParamLocalDeclCG formalParam = new AFormalParamLocalDeclCG();
			formalParam.setName(paramName);
			formalParam.setType(type);
			formalParams.add(formalParam);
			
			//Construct the initialization of the record field using the
			//corresponding formal parameter.
			AAssignmentStmCG assignment = new AAssignmentStmCG();
			AIdentifierStateDesignatorCG id = new AIdentifierStateDesignatorCG();
			id.setName(name);
			
			AVariableExpCG varExp = new AVariableExpCG();
			varExp.setType(field.getType());
			varExp.setOriginal(paramName);
			
			assignment.setTarget(id);
			assignment.setExp(varExp);
			
			bodyStms.add(assignment);
		}
		
		return format(constructor);
	}
	
	public static String formatTemplateTypes(LinkedList<PTypeCG> types) throws AnalysisException
	{
		StringWriter writer = new StringWriter();
		
		if(types.size() <= 0)
			return "";
		
		PTypeCG firstType = types.get(0);
		
		if(TypeAssistantCG.isBasicType(firstType))
			firstType = TypeAssistantCG.getWrapperType((SBasicTypeCGBase) firstType);
		
		writer.append(JavaFormat.format(firstType));
		
		for(int i = 1; i < types.size(); i++)
		{
			PTypeCG currentType = types.get(i);
			
			if(TypeAssistantCG.isBasicType(currentType))
				currentType = TypeAssistantCG.getWrapperType((SBasicTypeCGBase) currentType);
			
			writer.append(", " + JavaFormat.format(currentType));
		}
		
		return "<" + writer.toString() + ">";
	}
	
	public static String formatEqualsBinaryExp(AEqualsBinaryExpCG node) throws AnalysisException
	{
		//FIXME: Only works for simple types, i.e. not references
		//Operator pec?
		
		/*
		 * Things to consider:
		 * 
		 * Collections: sets, sequences and maps
		 * Classes: Maps to == 
		 * Type defs: Not supported anyway
		 * Records: Not supported anyway
		 * Primitive types: Maps to == 
		 * 
		 */
		PTypeCG leftNodeType = node.getLeft().getType();
		
		if(leftNodeType instanceof SSeqTypeCGBase)
		{
			return handleSeqComparison(node, false);
		}
		//else if(..)
		
		return JavaFormat.format(node.getLeft()) + " == " + JavaFormat.format(node.getRight());
	}
	
	public static String formatNotEqualsBinaryExp(ANotEqualsBinaryExpCG node) throws AnalysisException
	{
		//FIXME: Same problems as for equals. In addition, this method lacks support for collections
		
		PTypeCG leftNodeType = node.getLeft().getType();

		if (leftNodeType instanceof SSeqTypeCGBase)
		{
			return handleSeqComparison(node, true);
		}
		
		return JavaFormat.format(node.getLeft()) + " != " + JavaFormat.format(node.getRight());
	}
	
	private static String handleSeqComparison(SBinaryExpCGBase node, boolean notEquals) throws AnalysisException
	{
		String prefix = notEquals ? "!" : "";
		
		//In VDM the types of the equals are compatible when the AST passes the type check
		PExpCG leftNode = node.getLeft();
		PExpCG rightNode = node.getRight();
		
		if(isEmptySeq(leftNode))
		{
			return prefix + JavaFormat.format(node.getRight()) + ".isEmpty()";
		}
		else if(isEmptySeq(rightNode))
		{
			return prefix + JavaFormat.format(node.getLeft()) + ".isEmpty()";
		}
	
		return prefix + "Utils.seqEquals(" + JavaFormat.format(node.getLeft()) + ", " + JavaFormat.format(node.getRight()) + ")";

	}
	
	private static boolean isEmptySeq(PExpCG exp)
	{
		if(exp instanceof AEnumSeqExpCG)
		{
			AEnumSeqExpCG v = (AEnumSeqExpCG) exp;

			return v.getMembers().size() == 0;
		}
		
		return false;
	}
	
	public static String format(List<AFormalParamLocalDeclCG> params) throws AnalysisException
	{
		StringWriter writer = new StringWriter();
		
		if(params.size() <= 0)
			return "";
		
		AFormalParamLocalDeclCG firstParam = params.get(0);
		writer.append(format(firstParam));
		
		for(int i = 1; i < params.size(); i++)
		{
			AFormalParamLocalDeclCG param = params.get(i);
			writer.append(", " + format(param));
		}
		return writer.toString();
	}
	
	public static String formatSuperType(AClassDeclCG classDecl)
	{
		if(classDecl.getSuperName() == null)
			return "";
		else
			return "extends " + classDecl.getSuperName();
	}
	
	public static String formatArgs(List<PExpCG> exps) throws AnalysisException
	{
		StringWriter writer = new StringWriter();
		
		if(exps.size() <= 0)
			return "";
		
		PExpCG firstExp = exps.get(0);
		writer.append(JavaFormat.format(firstExp));
		
		for(int i = 1; i < exps.size(); i++)
		{
			PExpCG exp = exps.get(i);
			writer.append(", " + JavaFormat.format(exp));
		}
		
		return writer.toString();
	}
	
	public static boolean isNull(INode node)
	{
		return node == null;
	}
	
	public static boolean isVoidType(PTypeCG node)
	{
		return node instanceof AVoidTypeCG;
	}
	
	public static String formatInitialExp(PExpCG exp) throws AnalysisException
	{
		//private int a = 2; (when exp != null)
		//private int a; (when exp == null)
		
		if(exp == null)
			return "";
		else
			return " = " + JavaFormat.format(exp).toString();
		
	}
	
	public static String formatOperationBody(PStmCG body) throws AnalysisException
	{
		if(body == null)
			return ";";
		
		StringWriter generatedBody = new StringWriter();
		
		generatedBody.append("{\r\n\r\n");//TODO: USE PROPER CONSTANT
		generatedBody.append(JavaFormat.format(body));
		generatedBody.append("\r\n}");//TODO: USE PROPER CONSTANT
		
		return generatedBody.toString();
	}
	
	public static String formatTemplateParam(INode potentialBasicType) throws AnalysisException
	{
		if(potentialBasicType == null)
			return "";
		
		if(potentialBasicType instanceof AIntNumericBasicTypeCG)
			return "Long";
		else if(potentialBasicType instanceof ARealNumericBasicTypeCG)
			return "Double";
		else if(potentialBasicType instanceof ABoolBasicTypeCG)
			return "Boolean";
		else if(potentialBasicType instanceof ACharBasicTypeCG)
			return "Character";
		else
			return JavaFormat.format(potentialBasicType);
	}

	public static boolean cloneMember(AFieldNumberExpCG exp)
	{
		//Generally tuples need to be cloned, for example, if they
		//contain a record field (that must be cloned)
		
		if(exp.parent() instanceof AFieldNumberExpCG)
			return false;
		
		PTypeCG type = exp.getTuple().getType();
		
		if(type instanceof ATupleTypeCG)
		{
			
			ATupleTypeCG tupleType = (ATupleTypeCG) type;
			
			long field = exp.getField();
			PTypeCG fieldType = tupleType.getTypes().get((int) (field - 1));
			
			if(usesStructuralEquivalence(fieldType))
				return true;
		}
		
		return false;
	}
	
	public static boolean cloneMember(AFieldExpCG exp)
	{
		if(exp.parent() instanceof AFieldExpCG)
			return false;
		
		PTypeCG type = exp.getObject().getType();
		
		if(type instanceof ARecordTypeCG)
		{
			ARecordTypeCG recordType = (ARecordTypeCG) type;
			
			String memberName = exp.getMemberName();
			
			LinkedList<AFieldDeclCG> recFields = recordType.getRecDecl().getFields();
			
			AFieldDeclCG memberField = null;
			for (AFieldDeclCG field : recFields)
			{
				if(field.getName().equals(memberName))
				{
					memberField = field;
					break;
				}
			}
			
			if(memberField != null)
			{
				if(usesStructuralEquivalence(memberField.getType()))
					return true;
			}
		}
		
		return false;
	}
	
	public static boolean shouldClone(AVariableExpCG exp)
	{
		INode parent = exp.parent();
		if(parent instanceof AFieldExpCG || parent instanceof AFieldNumberExpCG)
		{
			return false;
		}
		
		PTypeCG type = exp.getType();
		if(usesStructuralEquivalence(type))
		{
			if(parent instanceof ANewExpCG)
			{
				ANewExpCG newExp = (ANewExpCG) parent;
				PTypeCG newExpType = newExp.getType();
				
				if(usesStructuralEquivalence(newExpType))
					return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	private static boolean usesStructuralEquivalence(PTypeCG type)
	{
		return type instanceof ARecordTypeCG || type instanceof ATupleTypeCG;
	}
	
	public static String generateEqualsMethod(ARecordDeclCG record) throws AnalysisException
	{
		//Construct equals method to be used for comparing records using
		//"structural" equivalence
		AMethodDeclCG equalsMethod = new AMethodDeclCG();
		equalsMethod.setAccess("public");
		equalsMethod.setName("equals");
		equalsMethod.setReturnType(new ABoolBasicTypeCG());
		
		//Add the formal parameter "Object obj" to the method
		AFormalParamLocalDeclCG formalParam = new AFormalParamLocalDeclCG();
		String paramName = "obj";
		formalParam.setName(paramName);
		AObjectTypeCG paramType = new AObjectTypeCG();
		formalParam.setType(paramType);
		equalsMethod.getFormalParams().add(formalParam);
		
		//Construct the initial check:
		//if ((!obj instanceof RecordType))
		//	return false;
		AIfStmCG ifStm = new AIfStmCG();
		ANotUnaryExpCG negated = new ANotUnaryExpCG();
		negated.setType(new ABoolBasicTypeCG());
		negated.setExp(JavaFormatAssistant.constructInstanceOf(record, paramName));
		//FIXME: Should be handled by the "not" template
		ifStm.setIfExp(ExpAssistantCG.isolateExpression(negated));
		AReturnStmCG returnIncompatibleTypes = new AReturnStmCG();
		returnIncompatibleTypes.setExp(JavaFormatAssistant.constructBoolLiteral(false));
		ifStm.setThenStm(returnIncompatibleTypes);
		
		//Next compare the fields of the instance with the fields of the formal parameter "obj":
		//return (field1 == obj.field1) && (field2 == obj.field2)...
		LinkedList<AFieldDeclCG> fields = record.getFields();
		PExpCG previousComparisons = JavaFormatAssistant.constructFieldComparison(record, fields.get(0), paramName); 

		for (int i = 1; i < fields.size(); i++)
		{
			previousComparisons = JavaFormatAssistant.extendAndExp(record, fields.get(i), previousComparisons, paramName);
		}

		AReturnStmCG fieldsComparison = new AReturnStmCG();
		fieldsComparison.setExp(previousComparisons);

		//Finally add the constructed statements to the equals method body
		ABlockStmCG equalsMethodBody = new ABlockStmCG();
		LinkedList<PStmCG> equalsStms = equalsMethodBody.getStatements();
		equalsStms.add(ifStm);
		equalsStms.add(fieldsComparison);
		equalsMethod.setBody(equalsMethodBody);
		
		return format(equalsMethod);
	}
}
