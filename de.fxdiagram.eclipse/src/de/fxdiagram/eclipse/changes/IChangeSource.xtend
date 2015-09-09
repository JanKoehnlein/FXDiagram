package de.fxdiagram.eclipse.changes

import org.eclipse.ui.IWorkbenchPart

interface IChangeSource {
	def void addChangeListener(IWorkbenchPart part, ModelChangeBroker broker)
	def void removeChangeListener(IWorkbenchPart part, ModelChangeBroker broker)
}