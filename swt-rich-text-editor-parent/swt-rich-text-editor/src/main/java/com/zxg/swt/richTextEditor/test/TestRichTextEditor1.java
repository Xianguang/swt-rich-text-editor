package com.zxg.swt.richTextEditor.test;

import org.eclipse.swt.widgets.Display;

public class TestRichTextEditor1 {

//	public void setUp(){
//		test1();
//	}
//	public void tearDown(){
//		
//	}
//	
//	public void test1(){
//		main(null);
//	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			MainWindow shell = new MainWindow(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
