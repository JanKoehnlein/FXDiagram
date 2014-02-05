package de.fxdiagram.core.tools

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.model.DomainObjectHandle

interface ChooserConnectionProvider {
	
	def XConnection getConnection(XNode host, XNode choice, DomainObjectHandle choiceInfo);
}