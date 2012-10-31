package com.zxg.swt.richTextEditor;

import org.eclipse.swt.browser.Browser;

public abstract class BrowserEditor {
	protected Browser browser;
	private static final String HTML_PREFIX = "<html><body designMode='On' contentEditable='true' style='margin:0;padding:0'>";
	private static final String HTML_SUFFIX = "</body></html>";

	public BrowserEditor(Browser browser) {
		this.browser = browser;
		this.browser.setJavascriptEnabled(true);
		this.setText("");
	}

	public final String getText() {
		return (String) browser.evaluate("return document.body.innerHTML;");
	}

	public final void setText(String text) {
		browser.setText(HTML_PREFIX +text+ HTML_SUFFIX, true);
	}
	
	public abstract void undo();

	public abstract void redo();

	public abstract void bold();

	public abstract void fontName(String fontName);

	public abstract void insertImage(String uri);

}
