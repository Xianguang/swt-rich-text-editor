package com.zxg.swt.richTextEditor;

import org.eclipse.swt.browser.Browser;

public abstract class BrowserEditor {
	protected Browser browser;
	
	public BrowserEditor(Browser browser) {
		this.browser=browser;
	}
	
	public abstract void undo();
	
	public abstract void redo();
	
	public abstract void bold();
	
	public abstract void fontName(String fontName);
	
	public abstract void insertImage(String uri);
}
