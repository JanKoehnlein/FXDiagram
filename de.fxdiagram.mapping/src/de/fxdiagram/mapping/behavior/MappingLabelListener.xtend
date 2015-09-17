package de.fxdiagram.mapping.behavior

import de.fxdiagram.core.XLabel
import de.fxdiagram.core.extensions.InitializingListListener
import de.fxdiagram.mapping.IMappedElementDescriptor
import java.util.Map
import javafx.collections.ObservableList
import javafx.scene.layout.Pane

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class MappingLabelListener<T extends XLabel> extends InitializingListListener<T> {

	Map<String, Pane> map = newHashMap

	protected new(Pair<String, Pane>... labelMappings) {
		labelMappings.forEach [
			map.put(key, value)
		]
		add = [ node ; pane?.children?.add(it) ]
		remove = [ pane?.children?.remove(it) ]
	}
	
	protected def getPane(XLabel label) {
		val descriptor = label.domainObjectDescriptor
		if(descriptor instanceof IMappedElementDescriptor<?>) 
			return map.get(descriptor.mapping.ID)
		else
			return null
	}
	
	static def <T> addMappingListener(ObservableList<T> labelList, Pair<String, Pane>... labelMappings) {
		labelList.addInitializingListener(new MappingLabelListener(labelMappings))
	}
	
	
}