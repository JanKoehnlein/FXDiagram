package de.fxdiagram.eclipse.shapes

import de.fxdiagram.eclipse.mapping.ConnectionMapping
import java.util.List
import javafx.geometry.Side

interface INodeWithLazyMappings {
	
	def List<Side> getButtonSides(ConnectionMapping<?> mapping)
	
}