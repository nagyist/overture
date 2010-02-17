package org.overture.ide.ui.editor.core;

import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DefaultUndoManager;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.overture.ide.ui.VdmUIPlugin;
import org.overture.ide.ui.editor.autoedit.VdmAutoEditStrategy;
import org.overture.ide.ui.editor.partitioning.VdmPartitionScanner;

public abstract class VdmSourceViewerConfiguration extends SourceViewerConfiguration {

	private Object fScanner;

	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {		
		return VdmUIPlugin.VDM_PARTITIONING;
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		
		return new String[]{IDocument.DEFAULT_CONTENT_TYPE,VdmPartitionScanner.SINGLELINE_COMMENT, 
				VdmPartitionScanner.MULTILINE_COMMENT, VdmPartitionScanner.STRING};
	}
	
	@Override
	public IUndoManager getUndoManager(ISourceViewer sourceViewer) {
		return new DefaultUndoManager(25);		
	}
	
	@Override 
	public IReconciler getReconciler(ISourceViewer sourceViewer) { 
		MonoReconciler reconciler = new MonoReconciler(new VdmReconcilingStrategy(),false); 
		reconciler.install(sourceViewer); 

		return reconciler; 
	} 
	
	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr =
			new DefaultDamagerRepairer(getVdmCodeScanner());
		reconciler.setDamager(dr, VdmPartitionScanner.SINGLELINE_COMMENT);
		reconciler.setRepairer(dr, VdmPartitionScanner.SINGLELINE_COMMENT);

		dr =
			new DefaultDamagerRepairer(getVdmCodeScanner());
		reconciler.setDamager(dr, VdmPartitionScanner.MULTILINE_COMMENT);
		reconciler.setRepairer(dr, VdmPartitionScanner.MULTILINE_COMMENT);

		dr =
			new DefaultDamagerRepairer(getVdmCodeScanner());
		reconciler.setDamager(dr, VdmPartitionScanner.STRING);
		reconciler.setRepairer(dr, VdmPartitionScanner.STRING);

		dr =
			new DefaultDamagerRepairer(getVdmCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}

	protected abstract ITokenScanner getVdmCodeScanner();
	
	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(
			ISourceViewer sourceViewer, String contentType) {
		IAutoEditStrategy strategy = (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType)) ?
				new VdmAutoEditStrategy() : new DefaultIndentLineAutoEditStrategy(); 
		return new IAutoEditStrategy[]{strategy};
	}
	
}
