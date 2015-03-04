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
import de.fxdiagram.eclipse.mapping.AbstractMapping
import de.fxdiagram.eclipse.mapping.DiagramMappingCall
import de.fxdiagram.eclipse.mapping.InterpreterContext
import de.fxdiagram.eclipse.mapping.MappingCall
import de.fxdiagram.eclipse.mapping.NodeMappingCall
import de.fxdiagram.eclipse.mapping.XDiagramConfig
import de.fxdiagram.eclipse.mapping.XDiagramConfigInterpreter
import de.fxdiagram.lib.actions.UndoRedoPlayerAction
import de.fxdiagram.swtfx.SwtToFXGestureConverter
import java.util.Map
import java.util.Set
import javafx.embed.swt.FXCanvas
import javafx.scene.PerspectiveCamera
import javafx.scene.Scene
import org.eclipse.jface.text.DocumentEvent
import org.eclipse.jface.text.IDocumentListener
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.IPartListener2
import org.eclipse.ui.IWorkbenchPartReference
import org.eclipse.ui.part.ViewPart
import org.eclipse.ui.texteditor.AbstractTextEditor
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static extension de.fxdiagram.core.extensions.DurationExtensions.*

/**
 * Embeds an {@link FXCanvas} with an {@link XRoot} in an eclipse {@link ViewPart}.
 * 
 * Uses {@link AbstractMapping} API to map domain objects to diagram elements.
 */
class FXDiagramView extends ViewPart {

	FXCanvas canvas
	
	@Accessors(PUBLIC_GETTER) XRoot root
	
	SwtToFXGestureConverter gestureConverter
	
	Map<IEditorPart, DocumentListener> contributingEditors = newHashMap
	Set<IEditorPart> changedEditors = newHashSet
	
	IPartListener2 listener 
	
	val configInterpreter = new XDiagramConfigInterpreter
	
	override createPartControl(Composite parent) {
		canvas = new FXCanvas(parent, SWT.NONE)
		gestureConverter = new SwtToFXGestureConverter(canvas)
		canvas.scene = createFxScene
	}
	
	protected def Scene createFxScene() {
		new Scene(
			root = new XRoot => [
			 	rootDiagram = new XDiagram()
			 	getDomainObjectProviders += new ClassLoaderProvider
			 	getDomainObjectProviders += XDiagramConfig.Registry.getInstance
			 		.configurations.map[domainObjectProvider].toSet
				getDiagramActionRegistry += #[
					new CenterAction,
					new DeleteAction,
					new LayoutAction(LayoutType.DOT),
					new ExportSvgAction,
					new UndoAction,
					new RedoAction,
					new RevealAction,
					new LoadAction,
					new SaveAction,
					new SelectAllAction,
					new ZoomToFitAction,
					new NavigatePreviousAction,
					new NavigateNextAction,
					new FullScreenAction,
					new UndoRedoPlayerAction ]
			]) => [
			camera = new PerspectiveCamera
			root.activate
		]
	}
	
	override setFocus() {
		canvas.setFocus
	}
	
	def clear() {
		// TODO: undo support ?
		contributingEditors.clear
		changedEditors.clear
		root.diagram = new XDiagram
		root.commandStack.clear
	}
	
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
		val interpreterContext = new InterpreterContext
		if(mappingCall instanceof DiagramMappingCall<?, ?>) {
			if(editor?.register || editor == null || changedEditors.remove(editor)) {
				interpreterContext.isNewDiagram = true
				root.diagram = configInterpreter.execute(mappingCall as DiagramMappingCall<?, T>, element, interpreterContext)
				root.commandStack.execute(interpreterContext.command)
			} 
		} else if(mappingCall instanceof NodeMappingCall<?, ?>) {
			editor?.register
			interpreterContext.diagram = root.diagram
			configInterpreter.execute(mappingCall as NodeMappingCall<?, T>, element, interpreterContext, true)
			root.commandStack.execute(interpreterContext.command)		
		}
		val descriptor = createMappedDescriptor(element)
		val centerShape = root.diagram.allShapes.findFirst[
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
					it += new Layouter().createLayoutCommand(LayoutType.DOT, root.diagram, 500.millis, centerShape)
				it += new SelectAndRevealCommand(root, [ it == centerShape ])
			])
	}
	
	protected def <T, U> createMappedDescriptor(T domainObject) {
		val mapping = XDiagramConfig.Registry.instance.configurations.map[getMappings(domainObject)].flatten.head
		mapping.config.domainObjectProvider.createMappedElementDescriptor(domainObject, mapping)
	}
	
	def boolean register(IEditorPart editor) {
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
		if(listener == null) {
			listener = new EditorListener(this)
			editor.site.page.addPartListener(listener)
		}
		return isNew
	}
	
	def deregister(IWorkbenchPartReference reference) {
		val part = reference.getPart(false)
		if(part != null) {
			changedEditors.remove(part)
			if(part instanceof AbstractTextEditor) {
				val documentListener = contributingEditors.get(part)
				val document = part.documentProvider.getDocument(part.editorInput)
				document.removeDocumentListener(documentListener)
			}
			contributingEditors.remove(part)
		}
	}
	
	def editorChanged(IEditorPart editor) {
		changedEditors += editor
	}
	
	override dispose() {
		gestureConverter.dispose
		super.dispose()
	}
}

@FinalFieldsConstructor
class DocumentListener implements IDocumentListener {

	val FXDiagramView view
	val IEditorPart editor
	
	override documentAboutToBeChanged(DocumentEvent event) {
	}
	
	override documentChanged(DocumentEvent event) {
		view.editorChanged(editor)
	}
}

@FinalFieldsConstructor
class EditorListener implements IPartListener2 {
	
	val FXDiagramView view
	
	override partActivated(IWorkbenchPartReference partRef) {
	}
	
	override partBroughtToTop(IWorkbenchPartReference partRef) {
	}
	
	override partClosed(IWorkbenchPartReference partRef) {
		view.deregister(partRef)
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