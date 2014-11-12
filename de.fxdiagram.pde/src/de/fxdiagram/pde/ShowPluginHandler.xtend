package de.fxdiagram.pde

import de.fxdiagram.xtext.glue.FXDiagramView
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.core.expressions.IEvaluationContext
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IAdaptable
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.ISources
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.handlers.HandlerUtil

import static extension org.eclipse.pde.core.plugin.PluginRegistry.*

class ShowPluginHandler extends AbstractHandler {
	
	override setEnabled(Object evaluationContext) {
		if (evaluationContext instanceof IEvaluationContext) {
			val selection = evaluationContext.getVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME)
			baseEnabled = !selection.pluginBases.empty
		} else {
			super.setEnabled(evaluationContext)
		}
	}
	
	protected def getPluginBases(Object selection) {
		// TODO selection from plugin-dependencies
		if(selection instanceof IStructuredSelection) 
			return selection.iterator.filter(IAdaptable).map[getAdapter(IProject) as IProject].map[findModel].filterNull.toSet
		else
			return #{}
	}
	
	override execute(ExecutionEvent event) throws ExecutionException {
		val selection = HandlerUtil.getActiveMenuSelection(event)
		val pluginBases = selection.pluginBases
		if(!pluginBases.empty) {
			val config = XDiagramConfig.Registry.getInstance.getConfigByID("de.fxdiagram.pde.PluginDiagramConfig") as PluginDiagramConfig
			val page = PlatformUI.workbench.activeWorkbenchWindow.activePage
			val view = page.showView("org.eclipse.xtext.glue.FXDiagramView")
			if(view instanceof FXDiagramView) {
				pluginBases.forEach[
					val call = config.getEntryCalls(it).head
					view.revealElement(it, call, page.activeEditor)
				]
			}
		}
		null
	}
} 