package de.fxdiagram.core.auxlines

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import de.fxdiagram.core.XNode
import java.util.Map

class AuxiliaryLineMap<T> {
	
	Multimap<Integer, AuxiliaryLine> store = HashMultimap.create
	Map<XNode, AuxiliaryLine> node2entry = newHashMap
	
	double threshold
	
	new() {
		this(1)	 
	}

	new(double threshold) {
		this.threshold = threshold
	}
	
	def add(AuxiliaryLine line) {
		line.relatedNodes?.forEach[ removeByNode(it) ]
		store.put(line.position.getKey, line)
		line.relatedNodes?.forEach[ node2entry.put(it, line) ]
	}

	def removeByNode(XNode node) {
		val line = getByNode(node)
		if(line != null) {
			val key = line.position.getKey
			store.remove(key, line)
			node2entry.remove(node)
		}
	}
	
	def getByPosition(double position) {
		store.get(position.getKey)
	}
	
	def getByNode(XNode node) {
		node2entry.get(node)
	}
	
	def containsKey(double position) {
		store.containsKey(position.getKey)
	}
	
	protected def getKey(double position) {
		(position / threshold + 0.5) as int
	}
}

