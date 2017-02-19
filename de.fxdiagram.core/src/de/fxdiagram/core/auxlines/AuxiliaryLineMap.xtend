package de.fxdiagram.core.auxlines

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import de.fxdiagram.core.XShape
import java.util.Map
import java.util.Set

import static java.lang.Math.*

class AuxiliaryLineMap<T> {
	
	Multimap<Long, AuxiliaryLine> store = HashMultimap.create
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
	
	def getNearestLineDelta(double position, double maxDistance, Set<XShape> excluded) {
		val key = getKey(position)
		val steps = (maxDistance / threshold) as int
		val at = store.get(key)  
		if(at.containsUnskipped(excluded))
			return position - key
		for(var i=1; i< steps; i++) { 
			val before = store.get(key - i)
			if(before.containsUnskipped(excluded)) {
				return position - key - i as double * threshold 
			}
			val after = store.get(key + i)
			if(after.containsUnskipped(excluded)) {
				return position - key + i as double * threshold
			}
		}
		return maxDistance
	}
	
	protected def containsUnskipped(Iterable<AuxiliaryLine> lines, Set<XShape> excluded) {
		lines.exists[relatedShapes.exists[!excluded.contains(it)]]
	}
	
	def getByShape(XShape shape) {
		shape2entry.get(shape)
	}
	
	def containsKey(double position) {
		store.containsKey(position.getKey)
	}
	
	protected def getKey(double position) {
		round(position / threshold)
	}
}

