package de.fxdiagram.eclipse

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.XShape
import de.fxdiagram.core.behavior.ReconcileBehavior
import de.fxdiagram.core.command.AbstractCommand
import de.fxdiagram.core.command.AddRemoveCommand
import de.fxdiagram.core.command.CommandContext
import de.fxdiagram.core.command.ParallelAnimationCommand
import de.fxdiagram.core.command.SelectAndRevealCommand
import de.fxdiagram.core.extensions.InitializingListener
import de.fxdiagram.core.layout.Layouter
import de.fxdiagram.eclipse.actions.EclipseSaveAction
import de.fxdiagram.eclipse.changes.IChangeListener
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.XDiagramConfig
import de.fxdiagram.mapping.execution.EntryCall
import de.fxdiagram.mapping.execution.InterpreterContext
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter
import de.fxdiagram.swtfx.SwtToFXGestureConverter
import java.io.File
import java.util.regex.Pattern
import javafx.application.Platform
import javafx.embed.swt.FXCanvas
import javafx.scene.PerspectiveCamera
import javafx.scene.Scene
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.swt.SWT
import org.eclipse.swt.custom.CTabFolder
import org.eclipse.swt.custom.CTabItem
import org.eclipse.swt.events.FocusEvent
import org.eclipse.swt.events.FocusListener
import org.eclipse.swt.widgets.Display
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.keys.IBindingService

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static org.eclipse.jface.dialogs.IDialogConstants.*

@Logging
class FXDiagramTab {
	val CTabItem tab
	val FXCanvas canvas
	val SwtToFXGestureConverter gestureConverter
	val XRoot root
	val configInterpreter = new XDiagramConfigInterpreter

	boolean isLinkWithEditor
	boolean dontSave = false

	IChangeListener changeListener 

	new(FXDiagramView view, CTabFolder tabFolder, XRoot root) {
		canvas = new FXCanvas(tabFolder, SWT.NONE)
		tab = new CTabItem(tabFolder, SWT.CLOSE)
		tab.control = canvas
		gestureConverter = new SwtToFXGestureConverter(canvas)
		changeListener = [
			if (it instanceof IEditorPart) 
				refreshUpdateState
		]
		view.modelChangeBroker.addListener(changeListener)
		this.root = root
		canvas.scene = new Scene(root) => [
			camera = new PerspectiveCamera
			root.activate
		]
		tab.addDisposeListener [
			gestureConverter.dispose
			view.modelChangeBroker.removeListener(changeListener)
			view.removeTab(tab)
		]
		root.fileNameProperty.addInitializingListener(new InitializingListener() => [
			set = [
				if(!tab.disposed)
					tab.text = (if(root.needsSave) '*' else '') + tabName
			]		
		])
		root.needsSaveProperty.addListener [ p, o, n |
			if(!tab.disposed)
				tab.text = (if(root.needsSave) '*' else '') + tabName
		]
		canvas.addFocusListener(new FocusListener {
			override focusGained(FocusEvent e) {
				(view.site.getService(IBindingService) as IBindingService).keyFilterEnabled = false
			}

			override focusLost(FocusEvent e) {
				(view.site.getService(IBindingService) as IBindingService).keyFilterEnabled = true
			}
		})
	}
	
	def boolean confirmClose() {
		if(!root.needsSave|| dontSave) 
			return true
		val dialog = new MessageDialog(
			tab.parent.shell,
			'Save diagram',
			null,
			'''
				'«tabName»' has been modified.
				Save changes?
			''',			
			MessageDialog.QUESTION_WITH_CANCEL,
			#[YES_LABEL, NO_LABEL, CANCEL_LABEL], 
			SWT.NONE)
		switch dialog.open {
			case 0: {
				new EclipseSaveAction().doSave(root)
				return true					
			} 
			case 1:	{
				dontSave = true				
				return true
			}
			default: 
				return false			
		}
 	}
	
	protected def getTabName() {
		val fileName = root.fileName?.split(Pattern.quote(File.separator))?.last
		if(fileName == null)
			return 'Untitled'
		val dotPos = fileName.lastIndexOf('.')
		if(dotPos >= 0) 
			return fileName.substring(0, dotPos)	
		else
			return fileName 
	}

	def getRoot() { root }

	def getCTabItem() { tab }

	def <T> void revealElement(T element, EntryCall<? super T> entryCall, IEditorPart editor) {
		Platform.runLater [
			try {// OMG! the scene's width and height is set asynchronously but needed for centering the selection
				if (canvas.scene.width == 0) {
					canvas.scene.widthProperty.addListener [ p, o, n |
						canvas.scene.widthProperty.removeListener(self)
						revealElement(element, entryCall, editor)
					]
				} else if (canvas.scene.height == 0) {
					canvas.scene.heightProperty.addListener [ p, o, n |
						canvas.scene.heightProperty.removeListener(self)
						revealElement(element, entryCall, editor)
					]
				} else {
					doRevealElement(element, entryCall, editor)
				}	
			} catch(Exception exc) {
				exc.printStackTrace
				MessageDialog.openError(Display.current.activeShell, 'Error', '''
					Error showing element in FXDiagram:
					«exc.message»
					See log for details.
				''')
			}
		]
	}

	protected def <T> void doRevealElement(T element, EntryCall<? super T> entryCall, IEditorPart editor) {
		val interpreterContext = new InterpreterContext(root.diagram)
		entryCall.execute(element, configInterpreter, interpreterContext)
		interpreterContext.executeCommands(root.commandStack)
		val centerShape = findShape(interpreterContext.diagram, element)
		root.commandStack.execute(
			new ParallelAnimationCommand =>
				[
					if (interpreterContext.needsLayoutCommand)
						it +=
							new Layouter().createLayoutCommand(root.diagram.layoutParameters, interpreterContext.diagram, 500.millis,
								centerShape)
					it += new SelectAndRevealCommand(root, [it == centerShape])
				])
	}

	def clear() {
		if(root.diagram.domainObjectDescriptor instanceof IMappedElementDescriptor<?>)
			root.commandStack.execute(AddRemoveCommand.newRemoveCommand(root.diagram, root.diagram.allShapes)) 
		else
			root.commandStack.execute(new ClearDiagramCommand)
	}

	def boolean setFocus() {
		canvas.setFocus
	}

	protected def <T, U> findShape(XDiagram diagram, T domainObject) {
		XDiagramConfig.Registry.instance
			.configurations
			.map[mappings]
			.flatten
			.map[
				val descriptor = config.domainObjectProvider.createMappedElementDescriptor(domainObject, it)
				(diagram.nodes + diagram.connections).findFirst[
					domainObjectDescriptor == descriptor
				]
			]
			.filterNull
			.head
	}

	def void setLinkWithEditor(boolean linkWithEditor) {
		isLinkWithEditor = linkWithEditor
		refreshUpdateState
	}

	protected def refreshUpdateState() {
		val behavior = root.diagram.getBehavior(ReconcileBehavior)
		if(behavior != null) 
			behavior.refreshDirtyState
		val allShapes = <XShape>newArrayList
		allShapes += root.diagram.nodes
		allShapes += root.diagram.connections
		allShapes.forEach [
			getBehavior(ReconcileBehavior)?.refreshDirtyState
		]
	}
	
	protected def refreshDirtyState(ReconcileBehavior behavior) {
		Display.^default.asyncExec [
			if (isLinkWithEditor)
				behavior.showDirtyState(behavior.dirtyState)
			else
				behavior.hideDirtyState
		]
	}
}

class ClearDiagramCommand extends AbstractCommand {
	
	XDiagram newDiagram
	XDiagram oldRootDiagram
	XDiagram oldDiagram

	override execute(CommandContext context) {
		oldDiagram = context.root.diagram
		oldRootDiagram = context.root.rootDiagram
		context.root.rootDiagram = newDiagram = new XDiagram
	}
	
	override undo(CommandContext context) {
		context.root.rootDiagram = oldRootDiagram
		context.root.diagram = oldDiagram
	}
	
	override redo(CommandContext context) {
		context.root.rootDiagram = newDiagram
		context.root.diagram = newDiagram
	}
}