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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * wysiwyg html editor
 * 
 * @author Xianguang Zhou
 * 
 */
public class RichTextEditor extends Composite {

	private Browser browser;

	private BrowserEditor browserEditor;

	// private CoolBar coolBar;

	private ToolItem fontToolItem;

	private Menu fontToolItemDropDownMenu;

	private FontData[] fontDataList;

	private List<String> fontNameList;

	// private Menu browserPopupMenu;
	//
	// private MenuItem copyMenuItem;

	private ToolItem insertImageToolItem;

	public RichTextEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());

		// coolBar = new CoolBar(this, SWT.FLAT | SWT.HORIZONTAL);
		// coolBar.setLocked(true);
		//
		// coolBar.addListener(SWT.Resize, new Listener() {
		//
		// @Override
		// public void handleEvent(Event event) {
		// getShell().layout();
		// }
		// });

		Composite toolsComposite = new Composite(this, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.wrap = true;
		rowLayout.type = SWT.HORIZONTAL;
		rowLayout.spacing = 0;
		rowLayout.marginTop = 0;
		rowLayout.marginBottom = 0;
		rowLayout.marginLeft = 0;
		rowLayout.marginRight = 0;
		toolsComposite.setLayout(rowLayout);

		ToolItem undoToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/edit-undo-3.png", "Undo");

		ToolItem redoToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/edit-redo-3.png", "Redo");

		ToolItem boldToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/format-text-bold-3.png",
				"Bold");

		ToolItem italicToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/format-text-italic-3.png",
				"Italic");

		ToolItem underLineToolItem = createToolItem(
				toolsComposite,
				"/com/zxg/swt/richTextEditor/image/format-text-underline-3.png",
				"Under Line");

		fontToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/format-font.png", "Font",
				SWT.DROP_DOWN);

		insertImageToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/insert-image-3.png",
				"Insert Image");

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
			final MenuItem fontNameMenuItem = new MenuItem(
					fontToolItemDropDownMenu, SWT.PUSH);
			fontNameMenuItem.setText(fontName);
			fontNameMenuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					browserEditor.fontName(fontNameMenuItem.getText());
				}
			});
		}

		Label horizontalLineLabel = new Label(this, SWT.HORIZONTAL
				| SWT.SEPARATOR);

		browser = new Browser(this, SWT.NONE);
		browserEditor = BrowserEditorFactory.createBrowserEditor(browser);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// browserPopupMenu=new Menu(browser);
		// // browser.setMenu(browserPopupMenu);
		// // browser.addMenuDetectListener(new MenuDetectListener() {
		// // @Override
		// // public void menuDetected(MenuDetectEvent e) {
		// // browserPopupMenu.setLocation(e.x,e.y);
		// // browserPopupMenu.setVisible(true);
		// // }
		// // });
		// browserPopupMenu.addMenuListener(new MenuAdapter() {
		// @Override
		// public void menuHidden(MenuEvent e) {
		// browser.forceFocus();
		// }
		// });

		// copyMenuItem=new MenuItem(browserPopupMenu,SWT.PUSH);
		// copyMenuItem.setText("Copy");

		FormData coolBarFormData = new FormData();
		coolBarFormData.top = new FormAttachment(0, 0);
		coolBarFormData.left = new FormAttachment(0, 0);
		coolBarFormData.right = new FormAttachment(100, 0);
		toolsComposite.setLayoutData(coolBarFormData);

		FormData horizontalLineLabelFormData = new FormData();
		horizontalLineLabelFormData.top = new FormAttachment(toolsComposite, 0);
		horizontalLineLabelFormData.left = new FormAttachment(0, 0);
		horizontalLineLabelFormData.right = new FormAttachment(100, 0);
		horizontalLineLabel.setLayoutData(horizontalLineLabelFormData);

		FormData browserFormData = new FormData();
		browserFormData.top = new FormAttachment(horizontalLineLabel, 0);
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

		italicToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEditor.italic();
			}
		});

		underLineToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEditor.underLine();
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
			}
		});
		
		insertImageToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN);
				fileDialog.setText("Open Image");
				String path = fileDialog.open();
				if (path != null) {
					browserEditor.insertImage(path);
				}
			}
		});
	}

	// private static ToolItem createToolItem(CoolBar coolBar, String imagePath,
	// String toolTipText) {
	// return createToolItem(coolBar, imagePath, toolTipText, SWT.NONE);
	// }

	private static ToolItem createToolItem(Composite toolsComposite,
			String imagePath, String toolTipText) {
		return createToolItem(toolsComposite, imagePath, toolTipText, SWT.PUSH);
	}

	private static ToolItem createToolItem(Composite toolsComposite,
			String imagePath, String toolTipText, int toolItemStyle) {

		ToolBar toolBar = new ToolBar(toolsComposite, SWT.FLAT | SWT.HORIZONTAL);

		ToolItem toolItem = new ToolItem(toolBar, toolItemStyle);
		toolItem.setToolTipText(toolTipText);
		toolItem.setImage(SWTResourceManager.getImage(RichTextEditor.class,
				imagePath));

		return toolItem;
	}

	// private static ToolItem createToolItem(CoolBar coolBar, String imagePath,
	// String toolTipText, int toolItemStyle) {
	// CoolItem coolItem = new CoolItem(coolBar, SWT.NONE);
	//
	// ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT | SWT.RIGHT //| SWT.WRAP
	// | SWT.HORIZONTAL);
	// coolItem.setControl(toolBar);
	//
	// ToolItem toolItem = new ToolItem(toolBar, toolItemStyle);
	// toolItem.setToolTipText(toolTipText);
	// toolItem.setImage(SWTResourceManager.getImage(RichTextEditor.class,
	// imagePath));
	//
	// toolBar.setSize(toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	//
	// coolItem.setSize(coolItem.computeSize(toolBar.getSize().x,
	// toolBar.getSize().y));
	//
	// return toolItem;
	// }

	@Override
	public boolean setFocus() {
		super.setFocus();
		browser.setFocus();
		return browser.execute("document.body.focus()");
	}

	public String getText() {
		return browserEditor.getText();
	}

	public void setText(String text) {
		browserEditor.setText(text);
	}

	@Override
	public boolean forceFocus() {
		super.forceFocus();
		browser.forceFocus();
		return browser.execute("document.body.focus()");
	}
}
