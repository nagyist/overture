package org.overture.codegen.transform;

import java.util.List;

import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;
import org.overture.codegen.cgast.expressions.PExpCG;
import org.overture.codegen.cgast.pattern.AIdentifierPatternCG;
import org.overture.codegen.cgast.statements.PStmCG;
import org.overture.codegen.cgast.types.PTypeCG;
import org.overture.codegen.constants.TempVarPrefixes;
import org.overture.codegen.transform.iterator.ILanguageIterator;
import org.overture.codegen.utils.ITempVarGen;

public class SeqCompStrategy extends CompStrategy
{
	protected PExpCG first;
	
	public SeqCompStrategy(ITransformationConfig config, TransformationAssistantCG transformationAssitant,
			PExpCG first, PExpCG predicate, String var, PTypeCG compType, ILanguageIterator langIterator, ITempVarGen tempGen,
			TempVarPrefixes varPrefixes)
	{
		super(config, transformationAssitant, predicate, var, compType, langIterator, tempGen, varPrefixes);
		
		this.first = first;
	}

	@Override
	public String getClassName()
	{
		return config.seqUtilFile();
	}

	@Override
	public String getMemberName()
	{
		return config.seqUtilEmptySeqCall();
	}

	@Override
	public PTypeCG getCollectionType() throws AnalysisException
	{
		return transformationAssistant.getSeqTypeCloned(compType);
	}

	@Override
	public List<PStmCG> getForLoopStms(AIdentifierVarExpCG setVar, List<AIdentifierPatternCG> ids, AIdentifierPatternCG id)
	{
		return packStm(transformationAssistant.consConditionalAdd(config.addElementToSeq(), var, predicate, first));
	}
}