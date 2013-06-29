package de.fxdiagram.lib.shapes

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.Lazy
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane

class RectangleBorderPane extends StackPane {
	
	@FxProperty@Lazy double borderWidth = 1.2
	
	@FxProperty@Lazy double borderRadius = 12

	new() {
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE)
		style = '''
				-fx-border-color: gray;
				-fx-border-width: 1.2;
				-fx-border-radius: 12;
				-fx-background-color: linear-gradient(to bottom right, lightgray, darkgray);
				-fx-background-radius: 12;
				-fx-background-insets: 1 1 1 1;
		'''
	}
}