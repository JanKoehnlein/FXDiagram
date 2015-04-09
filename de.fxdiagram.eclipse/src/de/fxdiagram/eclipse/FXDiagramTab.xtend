package de.fxdiagram.eclipse

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.command.ParallelAnimationCommand
import de.fxdiagram.core.command.SelectAndRevealCommand
import de.fxdiagram.core.layout.LayoutType
import de.fxdiagram.core.layout.Layouter
import de.fxdiagram.core.services.ClassLoaderProvider
import de.fxdiagram.core.tools.actions.CenterAction
import de.fxdiagram.core.tools.actions.DeleteAction
import de.fxdiagram.core.tools.actions.ExportSvgAction
import de.fxdiagram.core.tools.actions.FullScreenAction
import de.fxdiagram.core.tools.actions.LayoutAction
import de.fxdiagram.core.tools.actions.LoadAction
import de.fxdiagram.core.tools.actions.NavigateNextAction
import de.fxdiagram.core.tools.actions.NavigatePreviousAction
import de.fxdiagram.core.tools.actions.RedoAction
import de.fxdiagram.core.tools.actions.RevealAction
import de.fxdiagram.core.tools.actions.SaveAction
import de.fxdiagram.core.tools.actions.SelectAllAction
import de.fxdiagram.core.tools.actions.UndoAction
import de.fxdiagram.core.tools.actions.ZoomToFitAction
import de.fxdiagram.lib.actions.UndoRedoPlayerAction
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.ConnectionMappingCall
import de.fxdiagram.mapping.DiagramMappingCall
import de.fxdiagram.mapping.InterpreterContext
import de.fxdiagram.mapping.MappingCall
import de.fxdiagram.mapping.NodeMappingCall
import de.fxdiagram.mapping.XDiagramConfig
import de.fxdiagram.mapping.XDiagramConfigInterpreter
import de.fxdiagram.swtfx.SwtToFXGestureConverter
import java.util.Map
import java.util.Set
import javafx.embed.swt.FXCanvas
import javafx.scene.PerspectiveCamera
import javafx.scene.Scene
import org.eclipse.jface.text.DocumentEvent
import org.eclipse.jface.text.IDocumentListener
import org.eclipse.swt.SWT
import org.eclipse.swt.custom.CTabFolder
import org.eclipse.swt.custom.CTabItem
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.IPartListener2
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.ui.IWorkbenchPartReference
import org.eclipse.ui.texteditor.AbstractTextEditor
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import java.util.ArrayList

class FXDiagramTab {
	val CTabItem tab
	val FXCanvas canvas
	val SwtToFXGestureConverter gestureConverter
	val XRoot root
	val Set<IEditorPart> changedEditors = newHashSet
	val Map<IEditorPart, DocumentListener> contributingEditors = newHashMap
	val listener = new EditorListener(this)
	val configInterpreter = new XDiagramConfigInterpreter
	
	new(FXDiagramView view, CTabFolder tabFolder) {
		canvas = new FXCanvas(tabFolder, SWT.NONE)
		tab = new CTabItem(tabFolder, SWT.CLOSE)
		tab.control = canvas 
		gestureConverter = new SwtToFXGestureConverter(canvas)
		root = createRoot
		tab.text = root.name
		canvas.scene = new Scene(root) => [
			camera = new PerspectiveCamera
			root.activate
		]
		tab.addDisposeListener [
			gestureConverter.dispose
			view.site.page.removePartListener(listener)
			new ArrayList(contributingEditors.keySet).forEach [
				deregister
			]
		]
		root.nameProperty.addListener[p, o, n |
			tab.text = n 
		]
		root.needsSaveProperty.addListener[p, o, n |
			if(n) 
				tab.text = '*' + root.name
			else
				tab.text = root.name
		]
		view.site.page.addPartListener(listener)
	}
	
	protected def createRoot() {
		new XRoot => [
	 	rootDiagram = new XDiagram()
	 	getDomainObjectProviders += new ClassLoaderProvider
	 	getDomainObjectProviders += XDiagramConfig.Registry.getInstance
	 		.configurations.map[domainObjectProvider].toSet
		getDiagramActionRegistry += #[
			new CenterAction,
			new DeleteAction,
			new LayoutAction(LayoutType.DOT),
			new ExportSvgAction,
			new RedoAction,
			new UndoRedoPlayerAction,
			new UndoAction,
			new RevealAction,
			new LoadAction,
			new SaveAction,
			new SelectAllAction,
			new ZoomToFitAction,
			new NavigatePreviousAction,
			new NavigateNextAction,
			new FullScreenAction,
			new UndoRedoPlayerAction ]
		]
	}

	def getRoot() { root }
	
	def getCTabItem() { tab }
	
	def <T> void revealElement(T element, MappingCall<?, ? super T> mappingCall, IEditorPart editor) {
		// OMG! the scene's width and height is set asynchronously but needed for centering the selection
		if(canvas.scene.width == 0) {
			canvas.scene.widthProperty.addListener [ p, o, n |
				canvas.scene.widthProperty.removeListener(self)
				revealElement(element, mappingCall, editor)
			]
		} else if(canvas.scene.height == 0) {
			canvas.scene.heightProperty.addListener [ p, o, n |
				canvas.scene.heightProperty.removeListener(self)
				revealElement(element, mappingCall, editor)
			]
		} else {
			doRevealElement(element, mappingCall, editor)
		}
	} 	

	protected def <T> void doRevealElement(T element, MappingCall<?, ? super T> mappingCall, IEditorPart editor) {
		val interpreterContext = new InterpreterContext(root.diagram)
		if(mappingCall instanceof DiagramMappingCall<?, ?>) {
			interpreterContext.isCreateNewDiagram = editor == null || register(editor) || changedEditors.remove(editor)
			configInterpreter.execute(mappingCall as DiagramMappingCall<?, T>, element, interpreterContext)
			interpreterContext.executeCommands(root.commandStack)
		} else if(mappingCall instanceof NodeMappingCall<?, ?>) {
			register(editor)
			configInterpreter.execute(mappingCall as NodeMappingCall<?, T>, element, interpreterContext, true)
			interpreterContext.executeCommands(root.commandStack)
		} else if(mappingCall instanceof ConnectionMappingCall<?, ?>) {
			val mapping = mappingCall.mapping as ConnectionMapping<?>
			if(mapping.source != null && mapping.target != null) {
				register(editor)
				configInterpreter.execute(mappingCall as ConnectionMappingCall<?, T>, element, [], interpreterContext, true)
				interpreterContext.executeCommands(root.commandStack)
			}
		}
		val descriptor = createMappedDescriptor(element)
		val centerShape = (interpreterContext.diagram.nodes + interpreterContext.diagram.connections).findFirst[
			switch it {
				XNode:
					domainObject == descriptor
				XConnection:
					domainObject == descriptor
				default:
					false
			}
		]
		root.commandStack.execute(
			new ParallelAnimationCommand => [
				if(interpreterContext.needsLayout)
					it += new Layouter().createLayoutCommand(LayoutType.DOT, interpreterContext.diagram, 500.millis, centerShape)
				it += new SelectAndRevealCommand(root, [ it == centerShape ])
			])
	}

	def clear() {
		// TODO: undo support
		changedEditors.clear
		root.diagram = new XDiagram
		root.commandStack.clear
	}
	
	def setFocus() {
		canvas.setFocus
	}

	protected def <T, U> createMappedDescriptor(T domainObject) {
		val mapping = XDiagramConfig.Registry.instance.configurations.map[getMappings(domainObject)].flatten.head
		mapping.config.domainObjectProvider.createMappedElementDescriptor(domainObject, mapping)
	}

	protected def boolean register(IEditorPart editor) {
		var isNew = false
		if(!contributingEditors.containsKey(editor)) {
			if(editor instanceof AbstractTextEditor) {
				val documentListener = new DocumentListener(this, editor)
				val document = editor.documentProvider.getDocument(editor.editorInput)
				document.addDocumentListener(documentListener)
				contributingEditors.put(editor, documentListener)
			} else {
				contributingEditors.put(editor, null)
			}
			isNew = true
		}
		return isNew
	}

	protected def deregister(IWorkbenchPartReference reference) {
		reference.getPart(false)?.deregister
	}

	protected def deregister(IWorkbenchPart part) {
		changedEditors.remove(part)
		if(part instanceof AbstractTextEditor) {
			val documentListener = contributingEditors.get(part)
			val document = part.documentProvider.getDocument(part.editorInput)
			document.removeDocumentListener(documentListener)
		}
		contributingEditors.remove(part)
	}		

	protected def editorChanged(IEditorPart editor) {
		changedEditors += editor
	}
	
	@FinalFieldsConstructor
	protected static class DocumentListener implements IDocumentListener {

		val FXDiagramTab diagramTab
		val IEditorPart editor

		override documentAboutToBeChanged(DocumentEvent event) {
		}

		override documentChanged(DocumentEvent event) {
			diagramTab.editorChanged(editor)
		}
	}

	@FinalFieldsConstructor
	protected static class EditorListener implements IPartListener2 {

		val FXDiagramTab diagramTab

		override partActivated(IWorkbenchPartReference partRef) {
		}

		override partBroughtToTop(IWorkbenchPartReference partRef) {
		}

		override partClosed(IWorkbenchPartReference partRef) {
			diagramTab.deregister(partRef)
		}

		override partDeactivated(IWorkbenchPartReference partRef) {
		}

		override partHidden(IWorkbenchPartReference partRef) {
		}
		
		override partInputChanged(IWorkbenchPartReference partRef) {
		}
		
		override partOpened(IWorkbenchPartReference partRef) {
		}
		
		override partVisible(IWorkbenchPartReference partRef) {
		}
	}
}