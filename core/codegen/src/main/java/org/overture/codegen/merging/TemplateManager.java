package org.overture.codegen.merging;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;

import org.apache.velocity.Template;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.overture.codegen.cgast.AClassCG;
import org.overture.codegen.cgast.AFieldCG;
import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.expressions.ACastUnaryExpCG;
import org.overture.codegen.cgast.expressions.ACharLiteralExpCG;
import org.overture.codegen.cgast.expressions.ADivideNumericBinaryExpCG;
import org.overture.codegen.cgast.expressions.AIntLiteralExpCG;
import org.overture.codegen.cgast.expressions.AIsolationUnaryExpCG;
import org.overture.codegen.cgast.expressions.AMinusUnaryExpCG;
import org.overture.codegen.cgast.expressions.APlusNumericBinaryExpCG;
import org.overture.codegen.cgast.expressions.APlusUnaryExpCG;
import org.overture.codegen.cgast.expressions.ARealLiteralExpCG;
import org.overture.codegen.cgast.expressions.ASubtractNumericBinaryExpCG;
import org.overture.codegen.cgast.expressions.ATimesNumericBinaryExpCG;
import org.overture.codegen.cgast.types.ACharBasicTypeCG;
import org.overture.codegen.cgast.types.AIntNumericBasicTypeCG;
import org.overture.codegen.cgast.types.ARealNumericBasicTypeCG;
import org.overture.codegen.constants.IText;

public class TemplateManager
{
	private HashMap<Class<? extends INode>, String> nodeTemplateFileNames;

	public TemplateManager()
	{
		initNodeTemplateFileNames();
	}

	private void initNodeTemplateFileNames()
	{
		nodeTemplateFileNames = new HashMap<Class<? extends INode>, String>();

		nodeTemplateFileNames.put(AClassCG.class, IText.ROOT + "Class");
		nodeTemplateFileNames.put(AFieldCG.class, IText.ROOT + "Field");

		nodeTemplateFileNames.put(APlusUnaryExpCG.class, IText.UNARY_EXPS_PATH
				+ "Plus");
		nodeTemplateFileNames.put(AMinusUnaryExpCG.class, IText.UNARY_EXPS_PATH
				+ "Minus");

		nodeTemplateFileNames.put(ACastUnaryExpCG.class, IText.UNARY_EXPS_PATH
				+ "Cast");

		nodeTemplateFileNames.put(AIsolationUnaryExpCG.class, IText.UNARY_EXPS_PATH
				+ "Isolation");

		nodeTemplateFileNames.put(ATimesNumericBinaryExpCG.class, IText.NUMERIC_BINARY_EXPS_PATH
				+ "Mul");
		nodeTemplateFileNames.put(APlusNumericBinaryExpCG.class, IText.NUMERIC_BINARY_EXPS_PATH
				+ "Plus");
		nodeTemplateFileNames.put(ASubtractNumericBinaryExpCG.class, IText.NUMERIC_BINARY_EXPS_PATH
				+ "Minus");

		nodeTemplateFileNames.put(AIntLiteralExpCG.class, IText.EXPS_PATH
				+ "IntLiteral");
		nodeTemplateFileNames.put(ARealLiteralExpCG.class, IText.EXPS_PATH
				+ "RealLiteral");
		nodeTemplateFileNames.put(ACharLiteralExpCG.class, IText.EXPS_PATH
				+ "CharLiteral");

		nodeTemplateFileNames.put(ADivideNumericBinaryExpCG.class, IText.NUMERIC_BINARY_EXPS_PATH
				+ "Divide");

		// Basic types
		nodeTemplateFileNames.put(ACharBasicTypeCG.class, IText.BASIC_TYPE_PATH
				+ "Char");

		// Basic numeric types
		nodeTemplateFileNames.put(AIntNumericBasicTypeCG.class, IText.BASIC_TYPE_PATH
				+ "Integer");
		nodeTemplateFileNames.put(ARealNumericBasicTypeCG.class, IText.BASIC_TYPE_PATH
				+ "Real");
	}

	public Template getTemplate(Class<? extends INode> nodeClass)
	{
		try
		{
			StringBuffer buffer = readFromFile(getTemplateFileRelativePath(nodeClass));

			if (buffer == null)
				return null;

			return constructTemplate(buffer);

		} catch (IOException e)
		{
			return null;
		}
	}

	private Template constructTemplate(StringBuffer buffer)
	{
		Template template = new Template();
		RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
		StringReader reader = new StringReader(buffer.toString());

		try
		{
			SimpleNode simpleNode = runtimeServices.parse(reader, "Template name");
			template.setRuntimeServices(runtimeServices);
			template.setData(simpleNode);
			template.initDocument();

			return template;

		} catch (ParseException e)
		{
			return null;
		}
	}

	private String getTemplateFileRelativePath(Class<? extends INode> nodeClass)
	{
		return nodeTemplateFileNames.get(nodeClass) + IText.TEMPLATE_FILE_EXTENSION;
	}

	private StringBuffer readFromFile(String relativepath) throws IOException
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(relativepath);

		if (input == null)
			return null;

		StringBuffer data = new StringBuffer();
		int c = 0;
		while ((c = input.read()) != -1)
		{
			data.append((char) c);
		}
		input.close();

		return data;
	}
}