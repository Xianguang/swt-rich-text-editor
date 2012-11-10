package com.zxg.swt.richTextEditor;

import org.eclipse.swt.browser.Browser;

public class WebkitBrowserEditor extends BrowserEditor {
	public WebkitBrowserEditor(Browser browser) {
		super(browser);
		browser.execute("document.body.style.webkitTextSizeAdjust='none'");
	}
}
