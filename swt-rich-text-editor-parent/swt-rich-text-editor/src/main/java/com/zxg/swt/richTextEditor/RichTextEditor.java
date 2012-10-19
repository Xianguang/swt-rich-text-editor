package com.zxg.swt.richTextEditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class RichTextEditor extends Composite{

	public RichTextEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		new Browser(this, SWT.NONE).setText("abc");
	}

	
}
