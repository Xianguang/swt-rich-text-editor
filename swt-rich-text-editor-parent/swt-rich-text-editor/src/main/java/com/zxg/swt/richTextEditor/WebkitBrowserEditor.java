package com.zxg.swt.richTextEditor;

import org.eclipse.swt.browser.Browser;

public class WebkitBrowserEditor extends BrowserEditor {
	public WebkitBrowserEditor(Browser browser) {
		super(browser);
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
		browser.execute("document.execCommand('fontName',false,"+getJavaScriptStringFunctionName+"())");
	}

	@Override
	public void insertImage(String uri) {
		javaScriptString = uri;
		browser.execute("document.execCommand('insertImage',false,"+getJavaScriptStringFunctionName+"())");
	}
}
