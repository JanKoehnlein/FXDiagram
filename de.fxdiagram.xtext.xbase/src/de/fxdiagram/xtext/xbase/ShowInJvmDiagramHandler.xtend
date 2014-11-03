package de.fxdiagram.xtext.xbase

import com.google.inject.Inject
import de.fxdiagram.xtext.glue.FXDiagramView
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig
import org.apache.log4j.Logger
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.core.expressions.IEvaluationContext
import org.eclipse.jdt.core.IJavaElement
import org.eclipse.jdt.core.dom.PackageDeclaration
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor
import org.eclipse.jface.text.ITextSelection
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.ISources
import org.eclipse.ui.handlers.HandlerUtil
import org.eclipse.xtext.IGrammarAccess
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.ui.editor.XtextEditor
import org.eclipse.xtext.util.PolymorphicDispatcher

import static de.fxdiagram.xtext.xbase.ShowInJvmDiagramHandler.*

import static extension org.eclipse.xtext.GrammarUtil.*

class ShowInJvmDiagramHandler extends AbstractHandler {

	static val LOG = Logger.getLogger(ShowInJvmDiagramHandler)

	override setEnabled(Object evaluationContext) {
		if (evaluationContext instanceof IEvaluationContext) {
			val editor = evaluationContext.getVariable(ISources.ACTIVE_EDITOR_NAME)
			if (editor instanceof XtextEditor) {
				val languageService = getLanguageService(editor, IGrammarAccess)
				val usesXbase = languageService.grammar.allUsedGrammars.exists[name == 'org.eclipse.xtext.xbase.Xbase']
				baseEnabled = usesXbase
			} else {
				baseEnabled = editor instanceof CompilationUnitEditor
			}
			return
		}
		super.setEnabled(evaluationContext)
	}

	override Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			val editor = HandlerUtil.getActiveEditor(event)
			if(editor instanceof XtextEditor) {
				val action = editor?.getLanguageService(Action)
				action.run(editor)
			} else if(editor instanceof CompilationUnitEditor) {
				val selection = editor.selectionProvider.selection as ITextSelection
				val IJavaElement javaElement = PolymorphicDispatcher
					.createForSingleTarget("getElementAt", 1, 1, editor)
					.invoke(selection.getOffset())
				val javaElementToShow = javaElement.getAncestor(
						if(javaElement instanceof PackageDeclaration) 
							IJavaElement.PACKAGE_FRAGMENT
						else
							IJavaElement.TYPE)  
				showElement(javaElementToShow, editor)
			}
		} catch (Exception exc) {
			LOG.error("Error opening element in diagram", exc)
		}
		return null
	}

	protected def <T> getLanguageService(XtextEditor editor, Class<T> service) {
		val uri = editor.document.readOnly[URI]
		val resourceServiceProvider = IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(uri)
		return resourceServiceProvider.get(service)
	}
	
	protected static def getDiagramConfig() {
		XDiagramConfig.Registry.instance.getConfigByID('de.fxdiagram.xtext.xbase.JvmClassDiagramConfig')
	}
	
	protected static def showElement(Object element, IEditorPart editor) {
		if(element != null) {
			val mappings = diagramConfig.getEntryCalls(element)
			if (!mappings.empty) {
				val workbench = editor.site.page.workbenchWindow.workbench
				val view = workbench.activeWorkbenchWindow.activePage.showView("org.eclipse.xtext.glue.FXDiagramView")
				if (view instanceof FXDiagramView)
					view.revealElement(element, mappings.head(), editor)
			}
		}
	}

	static class Action {

		@Inject EObjectAtOffsetHelper eObjectAtOffsetHelper

		def run(XtextEditor editor) {
			val selection = editor.selectionProvider.selection as ITextSelection
			editor.document.readOnly [
				val selectedElement = eObjectAtOffsetHelper.resolveElementAt(it, selection.offset)
				if (selectedElement != null) 
					showElement(selectedElement, editor)
				null
			]
		}
		
	}
}
