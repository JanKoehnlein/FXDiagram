package de.fxdiagram.core.export

import javafx.scene.Node
import org.eclipse.xtend.lib.annotations.Data

interface SvgLinkProvider {
	def SvgLink getLink(Node node)
}

@Data
class SvgLink {
	String href
	String title
	String targetFrame
	boolean openInNewWindow

	public static val NONE=new SvgLink(null, null, null, false)
}	