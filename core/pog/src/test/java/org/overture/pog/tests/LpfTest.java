package org.overture.pog.tests;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.node.INode;
import org.overture.config.Release;
import org.overture.config.Settings;
import org.overture.pog.pub.IProofObligationList;
import org.overture.pog.pub.ProofObligationGenerator;

@RunWith(Parameterized.class)
public class LpfTest
{

	private String modelpath;

	public LpfTest(String modelpath)
	{
		this.modelpath = modelpath;
	}

	@Before
	public void setup()
	{
		Settings.release = Release.DEFAULT;
	}

	@Parameters(name = "{index} : {0}")
	public static Collection<Object[]> testData()
	{
		return InputsProvider.allExamples();
	}

	// Run the POG to ensure it does not crash
	@Test
	public void testLpfNoCrash() throws IOException, AnalysisException
	{

		List<INode> ast = TestHelper.getAstFromName(modelpath);
		IProofObligationList ipol = ProofObligationGenerator.generateLPFProofObligations(ast);
		assertNotNull(ipol);
	}

}
