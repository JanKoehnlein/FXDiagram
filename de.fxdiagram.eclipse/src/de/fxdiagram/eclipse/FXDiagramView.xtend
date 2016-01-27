package de.fxdiagram.eclipse

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.layout.LayoutType
import de.fxdiagram.core.model.ModelLoad
import de.fxdiagram.core.services.ClassLoaderProvider
import de.fxdiagram.core.tools.actions.CenterAction
import de.fxdiagram.core.tools.actions.DeleteAction
import de.fxdiagram.core.tools.actions.ExportSvgAction
import de.fxdiagram.core.tools.actions.FullScreenAction
import de.fxdiagram.core.tools.actions.LayoutAction
import de.fxdiagram.core.tools.actions.NavigateNextAction
import de.fxdiagram.core.tools.actions.NavigatePreviousAction
import de.fxdiagram.core.tools.actions.ReconcileAction
import de.fxdiagram.core.tools.actions.RedoAction
import de.fxdiagram.core.tools.actions.RevealAction
import de.fxdiagram.core.tools.actions.SelectAllAction
import de.fxdiagram.core.tools.actions.UndoAction
import de.fxdiagram.core.tools.actions.ZoomToFitAction
import de.fxdiagram.eclipse.actions.EclipseLoadAction
import de.fxdiagram.eclipse.actions.EclipseSaveAction
import de.fxdiagram.eclipse.changes.ModelChangeBroker
import de.fxdiagram.lib.actions.UndoRedoPlayerAction
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.DiagramMapping
import de.fxdiagram.mapping.XDiagramConfig
import de.fxdiagram.mapping.execution.DiagramEntryCall
import de.fxdiagram.mapping.execution.EntryCall
import java.io.InputStreamReader
import java.util.List
import java.util.Map
import javafx.application.Platform
import javafx.embed.swt.FXCanvas
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.swt.SWT
import org.eclipse.swt.custom.CTabFolder
import org.eclipse.swt.custom.CTabFolder2Adapter
import org.eclipse.swt.custom.CTabFolderEvent
import org.eclipse.swt.custom.CTabItem
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Display
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.commands.ICommandService
import org.eclipse.ui.handlers.RegistryToggleState
import org.eclipse.ui.part.ViewPart
import org.eclipse.xtend.lib.annotations.Accessors

/**
 * Embeds an {@link FXCanvas} with an {@link XRoot} in an eclipse {@link ViewPart}.
 * 
 * Uses {@link AbstractMapping} API to map domain objects to diagram elements.
 */
@Logging
class FXDiagramView extends ViewPart {

	CTabFolder tabFolder
	Map<CTabItem, FXDiagramTab> tab2content = newHashMap
	
	List<Pair<EventType<?>, EventHandler<?>>> globalEventHandlers = newArrayList
	
	@Accessors boolean linkWithEditor
	
	@Accessors(PUBLIC_GETTER) ModelChangeBroker modelChangeBroker
	
	override createPartControl(Composite parent) {
		tabFolder = new CTabFolder(parent, SWT.BORDER + SWT.BOTTOM)
		tabFolder.background = parent.display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND)
		modelChangeBroker = new ModelChangeBroker(PlatformUI.workbench)
		tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			override close(CTabFolderEvent event) {
				val tab = tab2content.get(event.item)
				event.doit = tab.confirmClose 
			}
		})
		val command = site.getService(ICommandService).getCommand('de.fxdiagram.eclipse.LinkWithEditor')
		linkWithEditor = command.getState(RegistryToggleState.STATE_ID)?.value as Boolean ?: false
	}

	def createNewTab() {
		val diagramTab = new FXDiagramTab(this, tabFolder, createRoot)
		tab2content.put(diagramTab.CTabItem, diagramTab)
		tabFolder.selection = diagramTab.CTabItem
		globalEventHandlers.forEach[
			diagramTab.root.addEventHandlerWrapper(key as EventType<? extends Event>, value)
		]
		diagramTab.linkWithEditor = linkWithEditor
		diagramTab
	}
	
	public def removeTab(CTabItem tab) {
		tab2content.get(tab).confirmClose
		tab2content.remove(tab)
	}
	
	protected def createRoot() {
		new XRoot => [
			rootDiagram = new XDiagram()
			getDomainObjectProviders += new ClassLoaderProvider
			getDomainObjectProviders +=
				XDiagramConfig.Registry.getInstance.configurations.map[domainObjectProvider].toSet
			getDiagramActionRegistry +=
				#[new CenterAction, new DeleteAction, new LayoutAction(LayoutType.DOT), new ExportSvgAction,
					new RedoAction, new UndoRedoPlayerAction, new UndoAction, new RevealAction, new EclipseLoadAction,
					new EclipseSaveAction, new ReconcileAction, new SelectAllAction, new ZoomToFitAction, new NavigatePreviousAction,
					new NavigateNextAction, new FullScreenAction]
		]
	}
	
	def void setLinkWithEditor(boolean linkWithEditor) {
		this.linkWithEditor = linkWithEditor
		tab2content.values.forEach[it.linkWithEditor = linkWithEditor]
	}
	
	private def <T extends Event> addEventHandlerWrapper(XRoot root, EventType<T> eventType, EventHandler<?> handler) {
		root.addEventHandler(eventType, handler as EventHandler<? super T>)
	}
	
	def <T extends Event> addGlobalEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
		globalEventHandlers.add(eventType -> eventHandler)		
		tab2content.values.forEach[
			root.addEventHandler(eventType, eventHandler)
		]
	}

	def <T extends Event> removeGlobalEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
		globalEventHandlers.remove(eventType -> eventHandler)		
		tab2content.values.forEach[
			root.removeEventHandler(eventType, eventHandler)
		]
	}
	
	protected def getCurrentDiagramTab() {
		val currentTab = tabFolder.selection
		if(currentTab != null) 
			tab2content.get(currentTab)
		else
			null
	}
	
	def getCurrentRoot() {
		(currentDiagramTab ?: createNewTab).root
	}
	
	override setFocus() {
		currentDiagramTab?.setFocus
	}
	
	def clear() {
		currentDiagramTab?.clear
	}
	
	def <T> void revealElement(T element, EntryCall<? super T> entryCall, IEditorPart editor) {
		if(entryCall instanceof DiagramEntryCall<?, ?>) {
			val mappingCall = (entryCall as DiagramEntryCall<?,T>).mappingCall
			val diagramElement = mappingCall.selector.apply(element)
			val diagramDescriptor = entryCall.config.domainObjectProvider.createMappedElementDescriptor(diagramElement, mappingCall.mapping)
			val tab = tab2content.entrySet.findFirst[
				value.root.diagram.domainObjectDescriptor == diagramDescriptor
			] 
			if(tab != null) {
				tab.value.revealElement(element, entryCall, editor)
				tabFolder.selection = tab.key
				return
			}			
			val newTab = createNewTab
			val filePath = (mappingCall.mapping as DiagramMapping<Object>).getDefaultFilePath(diagramElement)
			if(filePath != null) {
				val file = ResourcesPlugin.workspace.root.getFile(new Path(filePath))
				file.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor)
				if(file.exists) {
					val node = new ModelLoad().load(new InputStreamReader(file.contents, file.charset))
					if(node instanceof XRoot) {
						Platform.runLater [
							try {
								newTab.root.replaceDomainObjectProviders(node.domainObjectProviders)
								newTab.root.rootDiagram = node.diagram
								newTab.root.fileName = filePath
							} catch(Exception exc) {
								exc.printStackTrace
								MessageDialog.openError(Display.current.activeShell, 'Error', '''
									Error showing element in FXDiagram:
									«exc.message»
									See log for details.
								''')
							}
						]
						return		
					}
				}
			}
			newTab => [
				root.fileName = filePath
				revealElement(element, entryCall, editor)
			]
			return
		}
		(currentDiagramTab ?: createNewTab).revealElement(element, entryCall, editor)
	}
	
	override dispose() {
		super.dispose
		tab2content.clear
	}
}

