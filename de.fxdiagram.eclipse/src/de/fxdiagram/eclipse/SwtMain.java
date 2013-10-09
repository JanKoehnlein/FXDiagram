package de.fxdiagram.eclipse;

import javafx.embed.swt.FXCanvas;
import javafx.scene.Scene;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.fxdiagram.examples.Main;

public class SwtMain {
	private static Scene createScene() {
		return new Main().createScene();
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		FXCanvas canvas = new FXCanvas(shell, SWT.NONE);
		Scene scene = createScene();
		canvas.setScene(scene);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
