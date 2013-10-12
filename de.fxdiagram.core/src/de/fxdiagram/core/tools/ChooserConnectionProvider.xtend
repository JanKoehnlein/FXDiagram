package de.fxdiagram.core.tools

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode

interface ChooserConnectionProvider {
	
	def XConnection getConnection(XNode host, XNode choice, Object choiceInfo);
}