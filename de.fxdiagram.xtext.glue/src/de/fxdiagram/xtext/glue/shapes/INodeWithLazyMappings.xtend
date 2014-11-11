package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.xtext.glue.mapping.ConnectionMapping
import java.util.List
import javafx.geometry.Side

interface INodeWithLazyMappings {
	
	def List<Side> getButtonSides(ConnectionMapping<?> mapping)
	
}