package de.fxdiagram.eclipse.changes

import de.fxdiagram.eclipse.selection.ISelectionExtractor
import java.util.List
import org.eclipse.ui.IPartListener2
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.ui.IWorkbenchPartReference
import org.eclipse.ui.IWorkbench
import org.eclipse.ui.IPageListener
import org.eclipse.ui.IWorkbenchPage

class ModelChangeBroker {

	val IPartListener2 partListener = new IPartListener2() {
		override partOpened(IWorkbenchPartReference partRef) {
			val part = partRef.getPart(false)
			if (part != null)
				ISelectionExtractor.Registry.instance.selectionExtractors.filter(IChangeSource).forEach [
					addChangeListener(part, ModelChangeBroker.this)
				]
		}

		override partClosed(IWorkbenchPartReference partRef) {
			val part = partRef.getPart(false)
			if (part != null)
				ISelectionExtractor.Registry.instance.selectionExtractors.filter(IChangeSource).forEach [
					removeChangeListener(part, ModelChangeBroker.this)
				]
		}

		override partActivated(IWorkbenchPartReference partRef) {
		}

		override partBroughtToTop(IWorkbenchPartReference partRef) {
		}

		override partDeactivated(IWorkbenchPartReference partRef) {
		}

		override partHidden(IWorkbenchPartReference partRef) {
		}

		override partInputChanged(IWorkbenchPartReference partRef) {
		}

		override partVisible(IWorkbenchPartReference partRef) {
		}
	}

	val pageListener = new IPageListener() {
		override pageActivated(IWorkbenchPage page) {
		}

		override pageClosed(IWorkbenchPage page) {
			page.removePartListener(partListener)
		}

		override pageOpened(IWorkbenchPage page) {
			page.addPartListener(partListener)
		}
	}

	List<IChangeListener> listeners = newArrayList

	def addListener(IChangeListener listener) {
		listeners += listener
	}

	def removeListener(IChangeListener listener) {
		listeners -= listener
	}

	new(IWorkbench workbench) {
		workbench.workbenchWindows.forEach [
			pages.forEach [
				editorReferences.forEach [
					partListener.partOpened(it)
				]
				addPartListener(partListener)
			]
			addPageListener(pageListener)
		]
	}

	def partChanged(IWorkbenchPart part) {
		listeners.forEach [ listener |
			listener.partChanged(part)
		]
	}

}