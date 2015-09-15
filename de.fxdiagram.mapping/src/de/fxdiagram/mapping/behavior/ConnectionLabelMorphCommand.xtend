package de.fxdiagram.mapping.behavior

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.command.AbstractCommand
import de.fxdiagram.core.command.CommandContext
import java.util.List
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

@FinalFieldsConstructor
class ConnectionLabelMorphCommand extends AbstractCommand implements AddRemoveAcceptor {
	
	val XConnection connection
	
	List<XConnectionLabel> added 
	List<XConnectionLabel> removed 
	
	override add(XConnectionLabel label) {
		(added ?: (added = newArrayList)).add(label)
	}
	
	override remove(XConnectionLabel label) {
		(removed ?: (removed = newArrayList)).add(label)
	}
	
	override keep(XConnectionLabel label) {
	}
	
	def isEmpty() {
		added == null && removed == null
	}
	
	override execute(CommandContext context) {
		added?.forEach[
			connection.labels += it
		]
		removed?.forEach[
			connection.labels -= it
		]
	}
	
	override undo(CommandContext context) {
		added?.forEach[
			connection.labels -= it
		]
		removed?.forEach[
			connection.labels += it
		]
	}
	
	override redo(CommandContext context) {
		execute(context)
	}
}