package de.fxdiagram.xtext.xbase

import de.fxdiagram.eclipse.selection.ISelectionExtractor
import org.eclipse.jdt.core.IJavaElement
import org.eclipse.jdt.core.dom.PackageDeclaration
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor
import org.eclipse.jface.text.ITextSelection
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.xtext.util.PolymorphicDispatcher

class CompilationUnitSelectionExtractor implements ISelectionExtractor {
	
	override addSelectedElement(IWorkbenchPart activePart, Acceptor acceptor) {
		if(activePart instanceof CompilationUnitEditor) {
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
	
}