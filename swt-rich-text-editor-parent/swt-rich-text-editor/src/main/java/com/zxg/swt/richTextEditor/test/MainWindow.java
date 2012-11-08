package com.zxg.swt.richTextEditor.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.zxg.swt.richTextEditor.RichTextEditor;
import com.zxg.swt.richTextViewer.RichTextViewer;

public class MainWindow extends Shell {

	private RichTextEditor editor;

	/**
	 * Create the shell.
	 * 
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
		editor = new RichTextEditor(this, SWT.NONE);
		editor.setText("swt rich text editor");
		editor.setFocus();

		// final RichTextViewer viewer = new RichTextViewer(this, SWT.NONE);
		// viewer.appendText("<b>aaa</b>");
		// this.addShellListener(new ShellAdapter() {
		// @Override
		// public void shellIconified(ShellEvent e) {
		// viewer.appendText("<b>aaa</b>");
		// System.out.println(viewer.getBrowser().getText());
		// // System.out.println(viewer.getBrowser().execute("alert(st);"));
		//
		// }
		// });

		centerShell(this, getDisplay());
		this.addShellListener(new ShellAdapter() {
			@Override
			public void shellIconified(ShellEvent e) {
				final String htmlFragment = editor.getText();
				new Thread() {
					public void run() {
						System.out.print(RichTextEditor
								.getInlineHtmlFragment(htmlFragment));
					}
				}.start();
			}
		});
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
