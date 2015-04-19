package de.fxdiagram.eclipse.commands

import de.fxdiagram.mapping.XDiagramConfig
import org.eclipse.core.expressions.PropertyTester
import org.eclipse.ui.IWorkbenchPart
import de.fxdiagram.eclipse.selection.ISelectionExtractor

class HasMappingPropertyTester extends PropertyTester {
	
	override test(Object receiver, String property, Object[] args, Object expectedValue) {
		val activePart = receiver as IWorkbenchPart
		val acceptor = new ISelectionExtractor.Acceptor() {
			override accept(Object selectedElement) {
				if (selectedElement != null) {
					val hasMapping = XDiagramConfig.Registry.instance.configurations.exists [
						!getEntryCalls(selectedElement).empty
					]
					return hasMapping
				}
				return false
			}
		}
		return ISelectionExtractor.Registry.instance.selectionExtractors.exists [
			addSelectedElement(activePart, acceptor)
		]
	}
}