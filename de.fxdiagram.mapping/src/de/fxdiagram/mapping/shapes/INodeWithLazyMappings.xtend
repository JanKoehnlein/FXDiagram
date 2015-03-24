package de.fxdiagram.mapping.shapes

import de.fxdiagram.mapping.ConnectionMapping
import java.util.List
import javafx.geometry.Side

interface INodeWithLazyMappings {
	
	def List<Side> getButtonSides(ConnectionMapping<?> mapping)
	
}