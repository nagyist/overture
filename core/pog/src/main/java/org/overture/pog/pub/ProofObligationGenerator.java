package org.overture.pog.pub;

import java.util.List;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.node.INode;
import org.overture.pog.obligation.POContextStack;
import org.overture.pog.obligation.ProofObligationList;
import org.overture.pog.strategies.LpfStrategy;
import org.overture.pog.strategies.MccStrategy;
import org.overture.pog.visitors.PogVisitor;

public class ProofObligationGenerator
{
	public static IProofObligationList generateMcCarthyProofObligations(INode root)
			throws AnalysisException
	{
		PogVisitor pog = new PogVisitor(new MccStrategy());
		IProofObligationList r = root.apply(pog, new POContextStack());
		return r;
	}

	public static IProofObligationList generateMcCarthyProofObligations(
			List<INode> sources) throws AnalysisException
	{
		IProofObligationList r = new ProofObligationList();
		for (INode node : sources)
		{
			r.addAll(node.apply(new PogVisitor(new LpfStrategy()), new POContextStack()));
		}

		return r;
	}
	
	public static IProofObligationList generateLPFProofObligations(INode root)
			throws AnalysisException
	{
		PogVisitor pog = new PogVisitor(new MccStrategy());
		IProofObligationList r = root.apply(pog, new POContextStack());
		return r;
	}

	public static IProofObligationList generateLPFProofObligations(
			List<INode> sources) throws AnalysisException
	{
		IProofObligationList r = new ProofObligationList();
		for (INode node : sources)
		{
			r.addAll(node.apply(new PogVisitor(new LpfStrategy()), new POContextStack()));
		}

		return r;
	}

}
