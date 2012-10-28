package com.zxg.swt.richTextEditor;

import org.eclipse.swt.browser.Browser;

public class BrowserEditorFactory {

	public static BrowserEditor createBrowserEditor(Browser browser){
		BrowserEditor browserEditor;
		if (System.getProperty("os.name").equals("Linux")) {
			// browser = new Browser(this, SWT.WEBKIT);
			// browser = new Browser(this, SWT.NONE);
			browserEditor = new WebkitBrowserEditor(browser);
		} else {
			browserEditor = new DefaultBrowserEditor(browser);
		}
		return browserEditor;
	}
}
