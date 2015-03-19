package de.fxdiagram.eclipse

import de.fxdiagram.core.XRoot
import de.fxdiagram.eclipse.mapping.AbstractMapping
import de.fxdiagram.eclipse.mapping.MappingCall
import java.util.List
import java.util.Map
import javafx.embed.swt.FXCanvas
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import org.eclipse.swt.SWT
import org.eclipse.swt.custom.CTabFolder
import org.eclipse.swt.custom.CTabItem
import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.part.ViewPart

/**
 * Embeds an {@link FXCanvas} with an {@link XRoot} in an eclipse {@link ViewPart}.
 * 
 * Uses {@link AbstractMapping} API to map domain objects to diagram elements.
 */
class FXDiagramView extends ViewPart {

	CTabFolder tabFolder
	Map<CTabItem, FXDiagramTab> tab2content = newHashMap
	
	List<Pair<EventType<?>, EventHandler<?>>> globalEventHandlers = newArrayList
	
	override createPartControl(Composite parent) {
		tabFolder = new CTabFolder(parent, SWT.BORDER + SWT.BOTTOM)
		tabFolder.background = parent.display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND)
	}

	def createNewTab() {
		val diagramTab = new FXDiagramTab(this, tabFolder)
		tab2content.put(diagramTab.CTabItem, diagramTab)
		tabFolder.selection = diagramTab.CTabItem
		globalEventHandlers.forEach[
			diagramTab.root.addEventHandlerWrapper(key as EventType<? extends Event>, value)
		]
		diagramTab
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
	
	def <T> void revealElement(T element, MappingCall<?, ? super T> mappingCall, IEditorPart editor) {
		(currentDiagramTab ?: createNewTab).revealElement(element, mappingCall, editor)
	}
}

