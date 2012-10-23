package com.zxg.swt.richTextEditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class RichTextEditor extends Composite {

	private Browser browser;

	private BrowserEditor browserEditor;

	private CoolBar coolBar;

	private ToolItem fontToolItem;

	private Menu fontToolItemDropDownMenu;

	private FontData[] fontDataList;

	private List<String> fontNameList;

	public RichTextEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());

		coolBar = new CoolBar(this, SWT.FLAT | SWT.HORIZONTAL);
		coolBar.setLocked(true);

		ToolItem undoToolItem = createToolItem(coolBar,
				"/com/zxg/swt/richTextEditor/image/edit-undo-3.png", "Undo");

		ToolItem redoToolItem = createToolItem(coolBar,
				"/com/zxg/swt/richTextEditor/image/edit-redo-3.png", "Redo");

		ToolItem boldToolItem = createToolItem(coolBar,
				"/com/zxg/swt/richTextEditor/image/format-text-bold-3.png",
				"Bold", SWT.CHECK);

		fontToolItem = createToolItem(coolBar,
				"/com/zxg/swt/richTextEditor/image/format-font.png", "font",
				SWT.DROP_DOWN);
		fontDataList = getDisplay().getFontList(null, true);
		fontNameList = new ArrayList<String>();
		for (FontData fontData : fontDataList) {
			String fontName = fontData.getName();
			if (!fontNameList.contains(fontName)) {
				fontNameList.add(fontName);
			}
		}
		fontToolItemDropDownMenu = new Menu(this);
		for (String fontName : fontNameList) {
			final MenuItem fontNameMenuItem=new MenuItem(fontToolItemDropDownMenu, SWT.PUSH);
			fontNameMenuItem.setText(fontName);
			fontNameMenuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					browserEditor.fontName(fontNameMenuItem.getText());
				}
			});
		}

		if (System.getProperty("os.name").equals("Linux")) {
			browser = new Browser(this, SWT.MOZILLA);
			// browser = new Browser(this, SWT.NONE);
			browserEditor = new MozillaBrowserEditor(browser);
		} else {
			browser = new Browser(this, SWT.NONE);
			browserEditor = new BrowserEditor(browser);
		}
		browser.setText(
				"<html><body designMode='On' contentEditable='true' style='margin:0;padding:0'></body></html>",
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
				browserEditor.undo();
			}
		});

		redoToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEditor.redo();
			}
		});

		boldToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEditor.bold();
			}
		});

		fontToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == SWT.ARROW) {
					Rectangle rect = fontToolItem.getBounds();
					Point pt = new Point(rect.x, rect.y + rect.height);
					pt = fontToolItem.getParent().toDisplay(pt);
					fontToolItemDropDownMenu.setLocation(pt.x, pt.y);
					fontToolItemDropDownMenu.setVisible(true);
				}
				// if(fontData!=null){
				// browserEditor.fontName(fontData.getName());
				// }
			}
		});
	}

	private static ToolItem createToolItem(CoolBar coolBar, String imagePath,
			String toolTipText) {
		return createToolItem(coolBar, imagePath, toolTipText, SWT.NONE);
	}

	private static ToolItem createToolItem(CoolBar coolBar, String imagePath,
			String toolTipText, int toolItemStyle) {
		CoolItem coolItem = new CoolItem(coolBar, SWT.NONE);

		ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT | SWT.RIGHT | SWT.WRAP
				| SWT.HORIZONTAL);
		coolItem.setControl(toolBar);

		ToolItem toolItem = new ToolItem(toolBar, toolItemStyle);
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
