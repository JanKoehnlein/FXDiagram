package de.fxdiagram.eclipse

import javafx.embed.swt.FXCanvas
import javafx.scene.Scene

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.part.ViewPart

import de.fxdiagram.examples.Main

class FXDiagramViewPart extends ViewPart {

	FXCanvas canvas
	
	override createPartControl(Composite parent) {
		canvas = new FXCanvas(parent, SWT.NONE)
		canvas.scene = createFxScene
	}
	
	protected def Scene createFxScene() {
		new Main().createScene
	}
	
	override setFocus() {
		canvas.setFocus
		setFxFocus
	}
	
	protected def void setFxFocus() {
	}

}
