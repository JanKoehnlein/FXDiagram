package de.fxdiagram.xtext.xbase

import de.fxdiagram.eclipse.changes.IChangeSource
import de.fxdiagram.eclipse.changes.ModelChangeBroker
import de.fxdiagram.eclipse.selection.ISelectionExtractor
import org.eclipse.jdt.core.IElementChangedListener
import org.eclipse.jdt.core.IJavaElement
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.dom.PackageDeclaration
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor
import org.eclipse.jface.text.ITextSelection
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.xtext.util.PolymorphicDispatcher

class JavaEditorSelectionExtractor implements ISelectionExtractor, IChangeSource {
	
	int numJavaEditors = 0
	IElementChangedListener listener
	
	override addSelectedElement(IWorkbenchPart activePart, Acceptor acceptor) {
		if(activePart instanceof JavaEditor) {
			val selection = activePart.selectionProvider.selection as ITextSelection
			val IJavaElement javaElement = PolymorphicDispatcher
				.createForSingleTarget("getElementAt", 1, 1, activePart)
				.invoke(selection.getOffset())
			val javaElementToShow = javaElement.getAncestor(
					if(javaElement instanceof PackageDeclaration) 
						IJavaElement.PACKAGE_FRAGMENT
					else
						IJavaElement.TYPE) 
			return acceptor.accept(javaElementToShow)
		}
		return false
	}
	
	override addChangeListener(IWorkbenchPart part, ModelChangeBroker broker) {
		if(part instanceof JavaEditor) {
			if(numJavaEditors++ == 0) {
				listener = [
					broker.partChanged(part)
				]
				JavaCore.addElementChangedListener(listener)
			}
		} 
	}
	
	override removeChangeListener(IWorkbenchPart part, ModelChangeBroker broker) {
		if(part instanceof JavaEditor) {
			numJavaEditors--
			if(numJavaEditors == 0) {
				JavaCore.removeElementChangedListener(listener)
			}
		}
	}
}