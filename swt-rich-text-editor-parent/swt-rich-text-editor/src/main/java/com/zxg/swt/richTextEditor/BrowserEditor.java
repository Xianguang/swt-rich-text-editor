package com.zxg.swt.richTextEditor;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

public abstract class BrowserEditor {
	private static final String HTML_PREFIX = "<html><body designMode='On' contentEditable='true' style='margin:0;padding:0'>";
	private static final String HTML_SUFFIX = "</body></html>";
	protected Browser browser;
	protected String javaScriptString;
	protected static final String getJavaScriptStringFunctionName="getJavaScriptString";

	public BrowserEditor(Browser browser) {
		this.browser = browser;
		this.browser.setJavascriptEnabled(true);
		this.setText("");
		new BrowserFunction(this.browser, getJavaScriptStringFunctionName){
			@Override
			public Object function(Object[] arguments) {
				return javaScriptString;
			}
		};
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

	
//	public static String escapeJavaScriptString(String string){
//		return string.replaceAll("'",  "\\'").replaceAll("\"",  "\\\"").replaceAll("\\n", "\\n");
//	}
}
