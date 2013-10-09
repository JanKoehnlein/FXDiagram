package de.fxdiagram.eclipse;

import javafx.embed.swt.FXCanvas;
import javafx.scene.Scene;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.fxdiagram.examples.Main;

public class FXDiagramViewPart extends ViewPart {

	private FXCanvas canvas;
	
	@Override
	public void createPartControl(Composite parent) {
		canvas = new FXCanvas(parent, SWT.NONE);
		canvas.setScene(createFxScene());
	}
	
	protected Scene createFxScene() {
		return new Main().createScene();
	}
	
	@Override
	public void setFocus() {
		canvas.setFocus();
		setFxFocus();
	}
	
	protected void setFxFocus() {
	}

}
