package com.zxg.swt.richTextEditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class RichTextEditor extends Composite {

	private Browser browser;

	public RichTextEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());

		CoolBar coolBar = new CoolBar(this, SWT.FLAT);

		ToolItem undoToolItem = createToolItem(coolBar,
				"/com/zxg/swt/richTextEditor/image/edit-undo-3.png", "Undo");

		ToolItem redoToolItem = createToolItem(coolBar,
				"/com/zxg/swt/richTextEditor/image/edit-redo-3.png", "Redo");

		if (System.getProperty("os.name").equals("Linux")) {
			browser = new Browser(this, SWT.MOZILLA);
		} else {
			browser = new Browser(this, SWT.NONE);
		}
		browser.setText(
				"<html><body designMode='On' contentEditable='true' style='margin:1;padding:0'></body></html>",
				true);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		FormData coolBarFormData = new FormData();
		coolBarFormData.top = new FormAttachment(0, 0);
		coolBarFormData.left = new FormAttachment(0, 0);
		coolBarFormData.right = new FormAttachment(100, 0);
		coolBar.setLayoutData(coolBarFormData);

		FormData browserFormData = new FormData();
		browserFormData.top = new FormAttachment(coolBar, 0);
		browserFormData.left = new FormAttachment(0, 0);
		browserFormData.right = new FormAttachment(100, 0);
		browserFormData.bottom = new FormAttachment(100, 0);
		browser.setLayoutData(browserFormData);

		browser.addLocationListener(new LocationAdapter() {
			@Override
			public void changing(LocationEvent event) {
				event.doit = false;
			}
		});

		undoToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.execute("document.execCommand('undo',false,null)");
			}
		});

		redoToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.execute("document.execCommand('redo',false,null)");
			}
		});
	}

	private static ToolItem createToolItem(CoolBar coolBar, String imagePath,
			String toolTipText) {
		CoolItem coolItem = new CoolItem(coolBar, SWT.NONE);

		ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT | SWT.RIGHT);
		coolItem.setControl(toolBar);

		ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
		toolItem.setToolTipText(toolTipText);
		toolItem.setImage(SWTResourceManager.getImage(RichTextEditor.class,
				imagePath));

		toolBar.setSize(toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		coolItem.setSize(coolItem.computeSize(toolBar.getSize().x,
				toolBar.getSize().y));

		return toolItem;
	}

	@Override
	public boolean setFocus() {
		super.setFocus();
		browser.setFocus();
		return browser.execute("document.body.focus()");
	}
}
