package de.fxdiagram.eclipse

import de.fxdiagram.examples.Demo
import de.fxdiagram.swtfx.SwtToFXGestureConverter
import javafx.embed.swt.FXCanvas
import javafx.scene.Scene
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.part.ViewPart

class FXDiagramViewPart extends ViewPart {

	FXCanvas canvas
	
	SwtToFXGestureConverter gestureConverter
	
	override createPartControl(Composite parent) {
		canvas = new FXCanvas(parent, SWT.NONE)
		gestureConverter = new SwtToFXGestureConverter(canvas)
		canvas.scene = createFxScene
	}
	
	override dispose() {
		gestureConverter.dispose()
		super.dispose()
	}
	
	protected def Scene createFxScene() {
		new Demo().createScene
	}
	
	override setFocus() {
		canvas.setFocus
		setFxFocus
	}
	
	protected def void setFxFocus() {
	}

}
