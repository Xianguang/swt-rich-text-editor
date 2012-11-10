package com.zxg.swt.richTextEditor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

	private Clipboard clipboard;

	private Menu fontSizeToolItemDropDownMenu;

	private ToolItem fontSizeToolItem;

	private ToolItem foreColorToolItem;

	private ToolItem backColorToolItem;

	private ToolItem removeFormatToolItem;

	private ToolItem pasteImageToolItem;

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
				"/com/zxg/swt/richTextEditor/image/edit-undo-6.png", "Undo");

		ToolItem redoToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/edit-redo-6.png", "Redo");

		fontToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/format-font-name-2.png",
				"Font", SWT.DROP_DOWN);

		fontSizeToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/format-font-size.png",
				"Font Size", SWT.DROP_DOWN);

		foreColorToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/format-font-color-2.png",
				"Font Color");

		backColorToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/format-back-color.png",
				"Background Color");

		ToolItem boldToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/format-font-bold.png",
				"Bold");

		ToolItem italicToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/format-font-italic.png",
				"Italic");

		ToolItem underLineToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/format-font-underline.png",
				"Under Line");

		removeFormatToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/remove-format.png",
				"Remove Format");

		insertImageToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/insert-image-3.png",
				"Insert Image");

		pasteImageToolItem = createToolItem(toolsComposite,
				"/com/zxg/swt/richTextEditor/image/paste-image.png",
				"Paste Image");

		// init font name menu
		{
			// fontDataList = getDisplay().getFontList(null, true);
			// fontNameList = new ArrayList<String>();
			// for (FontData fontData : fontDataList) {
			// String fontName = fontData.getName();
			// if (!fontNameList.contains(fontName)) {
			// fontNameList.add(fontName);
			// }
			// }
			fontToolItemDropDownMenu = new Menu(this);
			// for (String fontName : fontNameList) {
			// final MenuItem fontNameMenuItem = new MenuItem(
			// fontToolItemDropDownMenu, SWT.PUSH);
			// fontNameMenuItem.setText(fontName);
			// fontNameMenuItem.addSelectionListener(new SelectionAdapter() {
			// @Override
			// public void widgetSelected(SelectionEvent e) {
			// browserEditor.fontName(fontNameMenuItem.getText());
			// }
			// });
			// }
			List<EditorFont> editorFontList = new ArrayList<RichTextEditor.EditorFont>();
			editorFontList.add(new EditorFont("宋体", "SimSun"));
			editorFontList.add(new EditorFont("新宋体", "NSimSun"));
			editorFontList.add(new EditorFont("仿宋_GB2312", "FangSong_GB2312"));
			editorFontList.add(new EditorFont("楷体_GB2312", "KaiTi_GB2312"));
			editorFontList.add(new EditorFont("黑体", "SimHei"));
			editorFontList.add(new EditorFont("微软雅黑", "'Microsoft YaHei'"));
			editorFontList.add(new EditorFont("Arial", "Arial"));
			editorFontList.add(new EditorFont("Arial Black", "'Arial Black'"));
			editorFontList.add(new EditorFont("Times New Roman",
					"'Times New Roman'"));
			editorFontList.add(new EditorFont("Courier New", "'Courier New'"));
			editorFontList.add(new EditorFont("Tahoma", "Tahoma"));
			editorFontList.add(new EditorFont("Verdana", "Verdana"));
			for (final EditorFont editorFont : editorFontList) {
				final MenuItem fontNameMenuItem = new MenuItem(
						fontToolItemDropDownMenu, SWT.PUSH);
				fontNameMenuItem.setText(editorFont.displayName);
				fontNameMenuItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						browserEditor.fontName(editorFont.name);
					}
				});
			}
		}

		// init font size menu
		{
			fontSizeToolItemDropDownMenu = new Menu(this);
			for (int i = 1; i < 8; i++) {
				final int fontSize = i;
				final MenuItem fontSizeMenuItem = new MenuItem(
						fontSizeToolItemDropDownMenu, SWT.PUSH);
				fontSizeMenuItem.setText(fontSize + "");
				fontSizeMenuItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						browserEditor.fontSize(fontSize);
					}
				});
			}
			// String[] fontSizeStringArray = new String[] { "1", "2",
			// "3", "4", "5", "6", "7", "8" };
			// for (final String fontSizeString : fontSizeStringArray) {
			// final MenuItem fontSizeMenuItem = new MenuItem(
			// fontSizeToolItemDropDownMenu, SWT.PUSH);
			// fontSizeMenuItem.setText(fontSizeString);
			// fontSizeMenuItem.addSelectionListener(new SelectionAdapter() {
			// @Override
			// public void widgetSelected(SelectionEvent e) {
			// browserEditor.fontSize(fontSizeString);
			// }
			// });
			// }
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
//				if (event.detail == SWT.ARROW) {
					Rectangle rect = fontToolItem.getBounds();
					Point pt = new Point(rect.x, rect.y + rect.height);
					pt = fontToolItem.getParent().toDisplay(pt);
					fontToolItemDropDownMenu.setLocation(pt.x, pt.y);
					fontToolItemDropDownMenu.setVisible(true);
//				}
			}
		});
		System.out.println(fontToolItem.getControl());

		fontSizeToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
//				if (event.detail == SWT.ARROW) {
					Rectangle rect = fontSizeToolItem.getBounds();
					Point pt = new Point(rect.x, rect.y + rect.height);
					pt = fontSizeToolItem.getParent().toDisplay(pt);
					fontSizeToolItemDropDownMenu.setLocation(pt.x, pt.y);
					fontSizeToolItemDropDownMenu.setVisible(true);
//				}
			}
		});

		foreColorToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog colorDialog = new ColorDialog(getShell());
				colorDialog.setText("Select Font Color");
				RGB rgb = colorDialog.open();
				if (rgb != null) {
					browserEditor.foreColor(rgb);
				}
			}
		});

		backColorToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog colorDialog = new ColorDialog(getShell());
				colorDialog.setText("Select Background Color");
				RGB rgb = colorDialog.open();
				if (rgb != null) {
					browserEditor.backColor(rgb);
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

		clipboard = new Clipboard(this.getDisplay());
		pasteImageToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ImageData imageData = getImageDataFromClipboard();
				if (imageData != null) {
					byte[] bytes = imageDataToBytes(imageData);
					String base64String = bytesToBase64String(bytes);
					browserEditor.insertImage(getBase64ImageSrc(base64String));
				}
			}
		});

		removeFormatToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEditor.removeFormat();
			}
		});
	}

	private static String getBase64ImageSrc(String base64ImageString) {
		return "data:image/png;base64," + base64ImageString;
	}

	private static String bytesToBase64String(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	private ImageData getImageDataFromClipboard() {
		return (ImageData) (clipboard.getContents(ImageTransfer.getInstance()));
	}

	private static byte[] imageDataToBytes(ImageData imageData) {
		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] { imageData };

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		imageLoader.save(byteArrayOutputStream, SWT.IMAGE_PNG);
		byte[] imageBytes = byteArrayOutputStream.toByteArray();

		try {
			byteArrayOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return imageBytes;
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

	@Override
	public void dispose() {
		clipboard.dispose();
		super.dispose();
	}

	public static String getInlineHtmlFragment(String htmlFragment) {
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		Elements imgElements = document.getElementsByTag("img");
		for (Element element : imgElements) {
			String imgSrc = element.attr("src");
			if (!imgSrc.startsWith("data:")) {
				if (!imgSrc.startsWith("http://")
						&& !imgSrc.startsWith("https://")
						&& !imgSrc.startsWith("file://")) {
					imgSrc = "file://" + imgSrc;
				}
				try {
					URL url = new URL(imgSrc);
					InputStream inputStream = url.openStream();
					try {
						ImageLoader imageLoader = new ImageLoader();
						ImageData[] imageDatas = imageLoader.load(inputStream);
						if (imageDatas.length > 0) {
							byte[] imageBytes = imageDataToBytes(imageDatas[0]);
							String base64String = bytesToBase64String(imageBytes);
							String newImgSrc = getBase64ImageSrc(base64String);
							element.attr("src", newImgSrc);
						}
					} finally {
						inputStream.close();
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return document.body().html();
	}

	static class EditorFont {
		public String displayName;
		public String name;

		public EditorFont(String displayName, String name) {
			this.displayName = displayName;
			this.name = name;
		}
	}
}
