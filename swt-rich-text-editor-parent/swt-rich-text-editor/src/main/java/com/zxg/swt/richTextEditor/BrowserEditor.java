package com.zxg.swt.richTextEditor;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.graphics.Color;

public class BrowserEditor {
	private static final String HTML_PREFIX = "<html><body designMode='On' contentEditable='true' style='margin:0;padding:0'>";
	private static final String HTML_SUFFIX = "</body></html>";
	protected Browser browser;
	protected String javaScriptString;
	protected static final String getJavaScriptStringFunctionName = "getJavaScriptString";

	public BrowserEditor(Browser browser) {
		this.browser = browser;
		this.browser.setJavascriptEnabled(true);
		this.browser.setText(HTML_PREFIX + HTML_SUFFIX, true);
		new BrowserFunction(this.browser, getJavaScriptStringFunctionName) {
			@Override
			public Object function(Object[] arguments) {
				return javaScriptString;
			}
		};
	}

	public String getText() {
		return (String) browser.evaluate("return document.body.innerHTML;");
	}

	public void setText(String text) {
		
	}

	public void undo() {
		browser.execute("document.execCommand('undo',false,null)");
	}

	public void redo() {
		browser.execute("document.execCommand('redo',false,null)");
	}

	public void bold() {
		browser.execute("document.execCommand('bold',false,null)");
	}

	public void fontName(String fontName) {
		javaScriptString = fontName;
		browser.execute("document.execCommand('fontName',false,"
				+ getJavaScriptStringFunctionName + "())");
	}

	public void insertImage(String uri) {
		javaScriptString = uri;
		browser.execute("document.execCommand('insertImage',false,"
				+ getJavaScriptStringFunctionName + "())");
	}

	public void italic() {
		browser.execute("document.execCommand('italic',false,null)");
	}

	public void fontSize(int fontSize) {
		browser.execute("document.execCommand('fontSize',false," + fontSize
				+ ")");
	}

	public void underLine() {
		browser.execute("document.execCommand('underline',false,null)");
	}

	public void foreColor(String color) {
		browser.execute("document.execCommand('foreColor',false," + color + ")");
	}

	public void backColor(String color) {
		browser.execute("document.execCommand('backColor',false," + color + ")");
	}

	public void foreColor(Color color) {
		foreColor(colorToString(color));
	}

	public void backColor(Color color) {
		backColor(colorToString(color));
	}

	protected static String colorToString(Color color) {
		return "rgb(" + color.getRed() + "," + color.getGreen() + ","
				+ color.getBlue() + ")";
	}

	// public static String escapeJavaScriptString(String string){
	// return string.replaceAll("'", "\\'").replaceAll("\"",
	// "\\\"").replaceAll("\\n", "\\n");
	// }

}
