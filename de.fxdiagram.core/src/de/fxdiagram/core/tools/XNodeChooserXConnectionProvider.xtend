package de.fxdiagram.core.tools

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode

interface XNodeChooserXConnectionProvider {
	
	def XConnection getConnection(XNode host, XNode choice, Object choiceInfo);
}