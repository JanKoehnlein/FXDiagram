package de.fxdiagram.eclipse.changes

import org.eclipse.ui.IWorkbenchPart

interface IChangeListener {
	def void partChanged(IWorkbenchPart part)
}