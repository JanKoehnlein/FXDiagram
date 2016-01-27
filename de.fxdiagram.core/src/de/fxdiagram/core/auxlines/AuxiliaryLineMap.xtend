package de.fxdiagram.core.auxlines

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import de.fxdiagram.core.XShape
import java.util.Map

class AuxiliaryLineMap<T> {
	
	Multimap<Integer, AuxiliaryLine> store = HashMultimap.create
	Map<XShape, AuxiliaryLine> shape2entry = newHashMap
	
	double threshold
	
	new() {
		this(1)	 
	}

	new(double threshold) {
		this.threshold = threshold
	}
	
	def add(AuxiliaryLine line) {
		line.relatedShapes?.forEach[ removeByShape(it) ]
		store.put(line.position.getKey, line)
		line.relatedShapes?.forEach[ shape2entry.put(it, line) ]
	}

	def removeByShape(XShape shape) {
		val line = getByShape(shape)
		if(line != null) {
			val key = line.position.getKey
			store.remove(key, line)
			shape2entry.remove(shape)
		}
	}
	
	def getByPosition(double position) {
		store.get(position.getKey)
	}
	
	def getByShape(XShape shape) {
		shape2entry.get(shape)
	}
	
	def containsKey(double position) {
		store.containsKey(position.getKey)
	}
	
	protected def getKey(double position) {
		(position / threshold + 0.5) as int
	}
}

