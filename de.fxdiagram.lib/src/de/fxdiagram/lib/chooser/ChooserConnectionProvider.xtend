package de.fxdiagram.lib.chooser

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.model.DomainObjectDescriptor

interface ChooserConnectionProvider {
	
	def XConnection getConnection(XNode host, XNode choice, DomainObjectDescriptor choiceInfo);
}