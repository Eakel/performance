package com.easyfun.eclipse.component.sql;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class SQLSegmentLineStyleListener implements LineStyleListener {

	private static final Color KEYWORD_COLOR = new Color(Display.getCurrent(), new RGB(255, 0, 0));

	private List<String> keywords = new ArrayList<String>();

	public SQLSegmentLineStyleListener() {
		super();
		keywords.add("select");
		keywords.add("from");
		keywords.add("where");
	}

	public void lineGetStyle(LineStyleEvent event) {
		List<StyleRange> styles = new ArrayList<StyleRange>();
		int start = 0;
		int length = event.lineText.length();
		while (start < length) {
			if (Character.isLetter(event.lineText.charAt(start))) {
				StringBuffer buf = new StringBuffer();
				int i = start;
				for (; i < length && Character.isLetter(event.lineText.charAt(i)); i++) {
					buf.append(event.lineText.charAt(i));
				}
				if (keywords.contains(buf.toString())) {
					styles.add(new StyleRange(event.lineOffset + start, i - start, KEYWORD_COLOR, null, SWT.BOLD));
				}
				start = i;
			} else {
				start++;
			}
		}
		event.styles = (StyleRange[]) styles.toArray(new StyleRange[0]);
	}

}