package de.fxdiagram.xtext.glue

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
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
import de.fxdiagram.core.tools.actions.SaveAction
import de.fxdiagram.core.tools.actions.SelectAllAction
import de.fxdiagram.core.tools.actions.UndoAction
import de.fxdiagram.core.tools.actions.ZoomToFitAction
import de.fxdiagram.lib.actions.UndoRedoPlayerAction
import de.fxdiagram.swtfx.SwtToFXGestureConverter
import de.fxdiagram.xtext.glue.mapping.DiagramMappingCall
import de.fxdiagram.xtext.glue.mapping.InterpreterContext
import de.fxdiagram.xtext.glue.mapping.MappingCall
import de.fxdiagram.xtext.glue.mapping.NodeMappingCall
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig
import de.fxdiagram.xtext.glue.mapping.XDiagramConfigInterpreter
import java.util.Set
import javafx.embed.swt.FXCanvas
import javafx.scene.PerspectiveCamera
import javafx.scene.Scene
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.IPartListener2
import org.eclipse.ui.IWorkbenchPartReference
import org.eclipse.ui.part.ViewPart
import org.eclipse.xtext.ui.editor.XtextEditor

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import org.eclipse.ui.IEditorPart

class FXDiagramView extends ViewPart {

	FXCanvas canvas
	
	XRoot root
	
	SwtToFXGestureConverter gestureConverter
	
	Set<IEditorPart> contributingEditors = newHashSet
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
		contributingEditors.clear
		changedEditors.clear
		root.diagram = new XDiagram
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
			editor.register
			if(changedEditors.remove(editor)) {
				interpreterContext.isNewDiagram = true
				root.diagram = configInterpreter.execute(mappingCall as DiagramMappingCall<?, T>, element, interpreterContext)
			} 
		} else if(mappingCall instanceof NodeMappingCall<?, ?>) {
			editor.register
			interpreterContext.diagram = root.diagram
			configInterpreter.execute(mappingCall as NodeMappingCall<?, T>, element, interpreterContext, true)		
		}
//		val command = new SequentialAnimationCommand
//		command += interpreterContext.command
//		if(interpreterContext.needsLayout)
//			command += new Layouter().createLayoutCommand(LayoutType.DOT, root.diagram, 200.millis)
//		val descriptor = domainObjectProvider.createDescriptor(element, mapping)
//		command += new SelectAndRevealCommand(root, [
//			switch it {
//				XNode: domainObject == descriptor
//				XConnection: domainObject == descriptor
//				default: false
//			}
//		])
//		root.commandStack.execute(command)
		root.commandStack.execute(interpreterContext.command)
		if(interpreterContext.needsLayout)
			root.commandStack.execute(new Layouter().createLayoutCommand(LayoutType.DOT, root.diagram, 500.millis))
		val descriptor = mappingCall.mapping.config.domainObjectProvider.createMappedDescriptor2(element, mappingCall.mapping)
		root.commandStack.execute(new SelectAndRevealCommand(root, [
			switch it {
				XNode: domainObject == descriptor
				XConnection: domainObject == descriptor
				default: false
			}
		]))
	}
	
	def void register(IEditorPart editor) {
		if(contributingEditors.add(editor)) {
			changedEditors += editor
			if(editor instanceof XtextEditor) 
				editor.document.addModelListener [
					changedEditors += editor
				]
			// TODO handle other editor types
		}
		if(listener == null) {
			listener = new EditorListener(this)
			editor.site.page.addPartListener(listener)
		}
	}
	
	def deregister(IWorkbenchPartReference reference) {
		val part = reference.getPart(false)
		if(part != null) {
			changedEditors.remove(part)
			contributingEditors.remove(part)
		}
	}
	
	override dispose() {
		gestureConverter.dispose
		super.dispose()
	}
}

class EditorListener implements IPartListener2 {
	
	FXDiagramView view
	
	new(FXDiagramView view) {
		this.view = view
	}
	
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