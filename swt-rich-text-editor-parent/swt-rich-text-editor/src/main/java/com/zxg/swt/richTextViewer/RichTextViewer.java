package com.zxg.swt.richTextViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * 
 * @author Xianguang Zhou
 * 
 */
public class RichTextViewer extends Composite {
	private static final String HTML_PREFIX = "<html><body style='margin:0;padding:0'>";
	private static final String HTML_SUFFIX = "</body></html>";
	private Browser browser;
	private String javaScriptString;
	private static final String getJavaScriptStringFunctionName = "getJavaScriptString";

	public RichTextViewer(Composite parent, int style) {
		super(parent, style);

		setLayout(new FillLayout());

		browser = new Browser(this, SWT.NONE);
		browser.setText(HTML_PREFIX + HTML_SUFFIX, true);
		browser.setJavascriptEnabled(false);

		browser.addLocationListener(new LocationAdapter() {
			@Override
			public void changing(LocationEvent event) {
				event.doit = false;
			}
		});
		browser.addOpenWindowListener(new OpenWindowListener() {
			@Override
			public void open(WindowEvent event) {
				event.required = true;
			}
		});
		browser.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.F5) {
					e.doit = false;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.F5) {
					e.doit = false;
				}
			}
		});

		new BrowserFunction(this.browser, getJavaScriptStringFunctionName) {
			@Override
			public Object function(Object[] arguments) {
				return javaScriptString;
			}
		};

		Menu menu = new Menu(browser);
		browser.setMenu(menu);
		/*MenuItem copyMenuItem = new MenuItem(menu, SWT.PUSH);
		copyMenuItem.setText("Copy");
		copyMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.execute("document.execCommand('copy',false,null)");
			}
		});*/
		MenuItem selectAllMenuItem = new MenuItem(menu, SWT.PUSH);
		selectAllMenuItem.setText("Select All");
		selectAllMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.execute("document.execCommand('selectAll',false,null)");
			}
		});
	}

	public void clear() {
		browser.execute("document.body.innerHTML=''");
	}

	public void appendText(String text) {
		this.javaScriptString = text;
		browser.execute("st=document.createElement('span');st.innerHTML="
				+ getJavaScriptStringFunctionName
				+ "();document.body.appendChild(st);st.scrollIntoView();");
	}

	public Browser getBrowser() {
		return browser;
	}
}
