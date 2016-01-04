package de.fxdiagram.mapping.behavior

import de.fxdiagram.core.XDomainObjectShape
import de.fxdiagram.core.XShape
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.behavior.OpenBehavior
import de.fxdiagram.mapping.IMappedElementDescriptor
import javafx.scene.input.MouseEvent

/**
 * Opens the domain object of this {@link XShape} in the respective Eclipse editor. 
 */
class OpenElementInEditorBehavior extends AbstractHostBehavior<XDomainObjectShape> implements OpenBehavior {
	
	new(XDomainObjectShape host) {
		super(host)
	}
	
	override getBehaviorKey() {
		OpenElementInEditorBehavior
	}
	
	override protected doActivate() {
		host.addEventHandler(MouseEvent.MOUSE_CLICKED, [
			if (clickCount == 2) {
				open
				consume
			}
		])
	}
	
	override open() {
		val descriptor = host.domainObjectDescriptor
		if(descriptor instanceof IMappedElementDescriptor<?>)
			descriptor.openInEditor(true)
	}
}