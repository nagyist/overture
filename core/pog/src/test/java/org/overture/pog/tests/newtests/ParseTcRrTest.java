package org.overture.pog.tests.newtests;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.overture.ast.node.INode;
import org.overture.core.tests.ParamStandardTest;
import org.overture.core.tests.PathsProvider;

import com.google.gson.reflect.TypeToken;

@RunWith(Parameterized.class)
public class ParseTcRrTest extends ParamStandardTest<String> {

	private static final String UPDATE_PROPERTY = "tests.update.rg.parsetc";

	public ParseTcRrTest(String nameParameter, String inputParameter,
			String resultParameter) {

		super(nameParameter, inputParameter, resultParameter);

	}

	@Override
	public String processModel(List<INode> ast) {
		return "Parse-TC Ok";
	}

	@Override
	public Type getResultType() {
		Type resultType = new TypeToken<String>()
				{
				}.getType();
				return resultType;
	}

	private static final String RG_PARSE_TC_ROOT = "src/test/resources/rg/parsetc";

	@Parameters(name = "{index} : {0}")
	public static Collection<Object[]> testData()
	{
		return PathsProvider.computePaths(RG_PARSE_TC_ROOT);
	}
	
	@Override
	protected String getUpdatePropertyString() {
		return UPDATE_PROPERTY;
	}

	@Override
	public void compareResults(String actual, String expected) {
		assertEquals(actual, expected);
	}

}
