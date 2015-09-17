package de.fxdiagram.mapping.behavior

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XLabel
import de.fxdiagram.core.XNode
import de.fxdiagram.core.command.AbstractCommand
import de.fxdiagram.core.command.CommandContext
import java.util.List
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

@FinalFieldsConstructor
class NodeLabelMorphCommand extends AbstractCommand implements AddRemoveAcceptor {
	val XNode host

	List<XLabel> oldLabels
	List<XLabel> newLabels = newArrayList
	
	override add(XLabel label) {
		newLabels.add(label)
	}
	
	override remove(XLabel label) {
	}
	
	override keep(XLabel label) {
		newLabels.add(label)
	}
	
	def boolean isEmpty() {
		newLabels == (oldLabels ?: host.labels)
	}
	
	override execute(CommandContext context) {
		oldLabels = host.labels
		host.labels.setAll(newLabels)
	}
	
	override undo(CommandContext context) {
		host.labels.setAll(oldLabels)
	}
	
	override redo(CommandContext context) {
		host.labels.setAll(newLabels)
	}
}

@FinalFieldsConstructor
class ConnectionLabelMorphCommand extends AbstractCommand implements AddRemoveAcceptor {
	val XConnection host
	
	List<XConnectionLabel> oldLabels
	List<XConnectionLabel> newLabels = newArrayList
	
	override add(XLabel label) {
		newLabels.add(label as XConnectionLabel)
	}
	
	override remove(XLabel label) {
	}
	
	override keep(XLabel label) {
		newLabels.add(label as XConnectionLabel)
	}
	
	def boolean isEmpty() {
		newLabels == (oldLabels ?: host.labels)
	}
	
	override execute(CommandContext context) {
		oldLabels = host.labels
		host.labels.setAll(newLabels)
	}
	
	override undo(CommandContext context) {
		host.labels.setAll(oldLabels)
	}
	
	override redo(CommandContext context) {
		host.labels.setAll(newLabels)
	}
}

