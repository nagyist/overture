package org.overture.codegen.runtime.traces;

import java.io.Serializable;
import java.util.List;

public interface TestAccumulator extends Serializable
{
	void registerTest(TraceTest test);
	boolean hasNext();
	TraceTest getNext();
	List<TraceTest> getAllTests();
}
