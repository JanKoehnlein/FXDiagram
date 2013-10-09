package de.fxdiagram.eclipse

import de.fxdiagram.examples.Main
import javafx.embed.swt.FXCanvas
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell

class SwtMain {

	private static def createScene() {
		new Main().createScene
	}

	static def void main(String[] args) {
		val display = new Display
		val shell = new Shell(display)
		shell.layout = new FillLayout
		val canvas = new FXCanvas(shell, SWT.NONE)
		val scene = createScene
		canvas.scene = scene
		shell.open
		while (!shell.disposed) {
			if (!display.readAndDispatch)
				display.sleep
		}
		display.dispose
	}
	
}