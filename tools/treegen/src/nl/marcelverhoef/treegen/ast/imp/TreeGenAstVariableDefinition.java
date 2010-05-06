// this file is automatically generated by treegen. do not modify!

package nl.marcelverhoef.treegen.ast.imp;

// import the abstract tree interfaces
import nl.marcelverhoef.treegen.ast.itf.*;

public class TreeGenAstVariableDefinition extends TreeGenAstDefinitions implements ITreeGenAstVariableDefinition
{
	// private member variable (key)
	private String m_key = new String();

	// public operation to retrieve the embedded private field value
	public String getKey()
	{
		return m_key;
	}

	// public operation to set the embedded private field value
	public void setKey(String p_key)
	{
		// consistency check (field must be non null!)
		assert(p_key != null);

		// instantiate the member variable
		m_key = p_key;
	}

	// private member variable (type)
	private TreeGenAstTypeSpecification m_type = null;

	// public operation to retrieve the embedded private field value
	public ITreeGenAstTypeSpecification getType()
	{
		return m_type;
	}

	// public operation to set the embedded private field value
	public void setType(TreeGenAstTypeSpecification p_type)
	{
		// consistency check (field must be non null!)
		assert(p_type != null);

		// instantiate the member variable
		m_type = p_type;
	}

	// private member variable (value)
	private String m_value = new String();

	// public operation to retrieve the embedded private field value
	public String getValue()
	{
		return m_value;
	}

	// public operation to set the embedded private field value
	public void setValue(String p_value)
	{
		// consistency check (field must be non null!)
		assert(p_value != null);

		// instantiate the member variable
		m_value = p_value;
	}

	// default constructor
	public TreeGenAstVariableDefinition()
	{
		super();
		m_key = null;
		m_type = null;
		m_value = null;
	}

	// the identity function
	public String identify() { return "TreeGenAstVariableDefinition"; }
}
