package com.zxg.swt.richTextEditor.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.zxg.swt.richTextEditor.RichTextEditor;

public class MainWindow extends Shell {

	/**
	 * Create the shell.
	 * @param display
	 */
	public MainWindow(Display display) {
		super(display, SWT.SHELL_TRIM);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("rich text editor test");
		setSize(450, 300);
		setLayout(new FillLayout());
		RichTextEditor editor=new RichTextEditor(this, SWT.NONE);
		editor.setFocus();
		centerShell(this, getDisplay());
	}

	public static void centerShell(Shell shell, Display display) {
		Rectangle displayRectangle = display.getClientArea();
		Rectangle shellRectangle = shell.getBounds();
		int x = (displayRectangle.width - shellRectangle.width) / 2;
		int y = (displayRectangle.height - shellRectangle.height) / 2;
		shell.setLocation(x, y);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
