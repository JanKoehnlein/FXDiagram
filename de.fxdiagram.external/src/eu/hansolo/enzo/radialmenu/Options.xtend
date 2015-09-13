package eu.hansolo.enzo.radialmenu

import de.fxdiagram.annotations.properties.FxProperty
import javafx.scene.paint.Color

import static javafx.scene.paint.Color.*

class Options {
	
   	@FxProperty double degrees = 360
    @FxProperty double offset = -90
    @FxProperty double radius = 100
    @FxProperty double buttonSize = 44
    @FxProperty Color buttonInnerColor = hsb(0, 0.1, 0.1)
    @FxProperty Color buttonFrameColor = WHITE
    @FxProperty Color buttonForegroundColor = WHITE 
   	@FxProperty double buttonAlpha = 0.5
    @FxProperty boolean buttonVisible = true
    @FxProperty boolean buttonHideOnSelect = true
    @FxProperty boolean tooltipsEnabled = true
}