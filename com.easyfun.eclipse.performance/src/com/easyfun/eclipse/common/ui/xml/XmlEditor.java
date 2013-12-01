package com.easyfun.eclipse.common.ui.xml;

import org.eclipse.debug.internal.ui.ColorManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class XmlEditor {
	public static SourceViewer getSourceViewer(Composite parent) {
		SourceViewerConfiguration viewerConfiguration = new XMLConfiguration(ColorManager.getDefault());
		SourceViewer sourceViewer = new SourceViewer(parent, null, null, false, SWT.BORDER | SWT.H_SCROLL |SWT.V_SCROLL | SWT.MULTI );
		sourceViewer.getTextWidget().setFont(JFaceResources.getFont("org.eclipse.wst.sse.ui.textfont")); //$NON-NLS-1$
		IDocument document = new org.eclipse.jface.text.Document();
		IDocumentPartitioner partitioner =
			new FastPartitioner(
				new XMLPartitionScanner(),
				new String[] {
					XMLPartitionScanner.XML_TAG,
					XMLPartitionScanner.XML_COMMENT });
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
		sourceViewer.configure(viewerConfiguration);
		sourceViewer.setDocument(document);
		sourceViewer.setEditable(true);
		
		return sourceViewer;
	}
}
