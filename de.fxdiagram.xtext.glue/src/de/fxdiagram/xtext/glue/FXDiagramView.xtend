package de.fxdiagram.xtext.glue

import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.layout.LayoutType
import de.fxdiagram.core.services.ResourceProvider
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
import de.fxdiagram.xtext.glue.mapping.BaseMapping
import de.fxdiagram.xtext.glue.mapping.DiagramMapping
import de.fxdiagram.xtext.glue.mapping.XDiagramProvider
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

class FXDiagramView extends ViewPart {

	FXCanvas canvas
	
	XRoot root
	
	SwtToFXGestureConverter gestureConverter
	
	Set<XtextEditor> contributingEditors = newHashSet
	Set<XtextEditor> changedEditors = newHashSet
	
	IPartListener2 listener 
	
	val domainObjectProvider = new XtextDomainObjectProvider
	val diagramProvider = new XDiagramProvider(domainObjectProvider)
	
	override createPartControl(Composite parent) {
		canvas = new FXCanvas(parent, SWT.NONE)
		gestureConverter = new SwtToFXGestureConverter(canvas)
		canvas.scene = createFxScene
	}
	
	protected def Scene createFxScene() {
		new Scene(
			root = new XRoot => [
			 	classLoader = class.classLoader
			 	rootDiagram = new XDiagram()
			 	getDomainObjectProviders += #[
			 		new ResourceProvider(class.classLoader),
			 		domainObjectProvider
			 	]
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
					new UndoRedoPlayerAction ( ) ]
			] 
		) => [
			camera = new PerspectiveCamera
			root.activate
		]
	}
	
	override setFocus() {
		canvas.setFocus
		setFxFocus
	}
	
	protected def void setFxFocus() {
	}

	def <T> void revealElement(T element, BaseMapping<T> mapping, XtextEditor editor) {
		if(mapping instanceof DiagramMapping<?>) {
			editor.register()
			if(changedEditors.remove(editor)) {
				root.diagram = diagramProvider.createDiagram(element, mapping as DiagramMapping<T>)
				new LayoutAction(LayoutType.DOT).perform(root)
			} 
		}
		val descriptor = domainObjectProvider.createDescriptor(element)
		root.diagram.nodes.forEach[selected = domainObject == descriptor]
		root.diagram.connections.forEach[selected = domainObject == descriptor]
		new CenterAction().perform(root)
	}
	
	def void register(XtextEditor editor) {
		if(contributingEditors.add(editor)) {
			changedEditors += editor
			editor.document.addModelListener [
				changedEditors += editor
			]
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