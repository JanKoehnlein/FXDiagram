package eu.hansolo.enzo.radialmenu

import de.fxdiagram.annotations.properties.FxProperty
import javafx.scene.paint.Color

import static javafx.scene.paint.Color.*

class MenuItem {
	
	@FxProperty String tooltip = ''
    @FxProperty double size = 32
    @FxProperty Color innerColor = Color.rgb(68, 64, 60)
    @FxProperty Color frameColor = WHITE
    @FxProperty Color foregroundColor = WHITE
    @FxProperty SymbolType symbol = SymbolType.NONE
    @FxProperty String thumbnailImageName = ''
}