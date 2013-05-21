package org.overture.codegen.templates;

public enum TemplateParameters
{
	//Class definitions
	CLASS_ACCESS,
	CLASS_NAME,
	FIELDS,

	//FIELDS
	FIELD_ACCESS,
	FIELD_STATIC,
	FIELD_FINAL,
	FIELD_TYPE,
	FIELD_NAME,
	FIELD_INITIAL,
	
	//Method definitions
//	METHOD_ACCESS_SPECIFIER,
//	METHOD_RETURN_TYPE,
//	METHOD_NAME,
	METHOD_DEFS,
	
	//Value definition
//	VALUE_ACCESS_SPECIFIER,
//	VALUE_TYPE,
//	VALUE_PATTERN,
	VALUE_DEFS, ////The list with value definitions
	
	
	UNARY_EXP_VALUE,
	
	IF_STM_TEST,
	IF_STM_THEN_STM,
	IF_STM_ELSE_STM,
	
	
	BIN_EXP_LEFT_OPERAND,
	BIN_EXP_RIGHT_OPERAND,
	//Used in template that wrappes expressions (2+3)*5
	BIN_EXP_WRAPPED,
	
	//Used to set a flag indicating whether both arguments in a/b are integers
	//In Java 1/2 equals 0 whereas in VDM it must be 0.5
	DIVIDE_BOTH_OPERANDS_INTS,
	
	
	BASIC_TYPE,
	
	CAST_EXP_TYPE
}
