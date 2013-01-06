package de.itemis.javafx.diagram

import java.util.List
import javafx.beans.value.ChangeListener
import javafx.collections.ListChangeListener
import javafx.geometry.Bounds
import javafx.scene.control.Label
import javafx.scene.transform.Affine

import static extension de.itemis.javafx.diagram.transform.TransformExtensions.*

class XConnectionLabel extends Label implements XActivatable {
	
	XConnection connection
	
	boolean isActive
	
	new(XConnection connection) {
		this.connection = connection
		connection.label = this
	}
	
	override activate() {
		if(!isActive)
			doActivate
		isActive = true
	}
	
	def doActivate() {
		place(connection.points)
		val ListChangeListener<Double> listChangeListener = [
			place(list)
		] 
		connection.points.addListener(listChangeListener)
		val ChangeListener<Bounds> boundsChangeListener = [
			element, oldVlaue, newValue |
			place(connection.points)
		] 
		boundsInLocalProperty.addListener(boundsChangeListener) 
	}

	def protected place(List<? extends Double> list) {
		if(list.size >= 4) {
			val transform = new Affine
			transform.translate(-boundsInLocal.width / 2, -boundsInLocal.height - 2)
			
			val dx = list.get(list.size - 2) - list.get(0)
			val dy = list.get(list.size - 1) - list.get(1)
			val angle = Math::atan2(dy, dx)
			transform.rotate(angle * 180 / Math::PI)

			val xPos = (list.get(0) + list.get(list.size - 2)) / 2
			val yPos = (list.get(1) + list.get(list.size - 1)) / 2
			transform.translate(xPos, yPos)
			
			transforms.clear
			transforms += transform
		}
	}
}

