package de.fxdiagram.pde

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.layout.Layouter
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.buttons.RapidButtonAction
import de.fxdiagram.lib.chooser.CarusselChoice
import de.fxdiagram.lib.chooser.ConnectedNodeChooser
import de.fxdiagram.lib.chooser.CoverFlowChoice
import de.fxdiagram.mapping.XDiagramConfig
import de.fxdiagram.mapping.execution.InterpreterContext
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter
import java.util.NoSuchElementException
import org.eclipse.osgi.service.resolver.BundleDescription

import static de.fxdiagram.core.layout.LayoutType.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.pde.BundleUtil.*

class AddDependencyPathAction extends RapidButtonAction {

	boolean isInverse
	
	new(boolean isInverse) {
		this.isInverse = isInverse 
	}

	override isEnabled(XNode host) {
		(host.domainObjectDescriptor as BundleDescriptor).withDomainObject[
			if(isInverse) 
				!dependentBundles.empty
			else 
				!dependencyBundles.empty
		]
	}
	
	override perform(RapidButton button) {
		val descriptor = button.host.domainObjectDescriptor as BundleDescriptor
		descriptor.withDomainObject [
			doPerform(button, it)
		]
	}
	
	def doPerform(RapidButton button, BundleDescription hostBundle) {
		val root = button.host.root
		val config = XDiagramConfig.Registry.getInstance.getConfigByID('de.fxdiagram.pde.BundleDiagramConfig') as BundleDiagramConfig
		val host = button.host
		val choiceGraphics = if (button.position.vertical)
				new CarusselChoice
			else
				new CoverFlowChoice

		val interpreter = new XDiagramConfigInterpreter
		val chooser = new ConnectedNodeChooser(host, button.position, choiceGraphics) {
			override getAdditionalShapesToAdd(XNode choice, DomainObjectDescriptor choiceInfo) {
				try {
					// throw away direct connection
					for (it : super.getAdditionalShapesToAdd(choice, choiceInfo).filter(XConnection))
						removeConnection
					val diagram = host.diagram
					val context = new InterpreterContext(diagram) 
					context.addNode(choice)
					(choice.domainObjectDescriptor as BundleDescriptor).withDomainObject [
						chosenBundle |
						if (isInverse)
							chosenBundle.getAllBundleDependencies(hostBundle)
						else
							hostBundle.getAllBundleDependencies(chosenBundle)
					].forEach [ bundleDependency |
						context.isCreateConnections = false
						val source = interpreter.createNode(bundleDependency.owner, config.pluginNode, context)
						val target = interpreter.createNode(bundleDependency.dependency, config.pluginNode, context)
						context.isCreateConnections = true
						interpreter.createConnection(bundleDependency, config.dependencyConnection, [
							it.source = source
							it.target = target
						], context)
					]
					return context.addedShapes
				} catch(NoSuchElementException exc) {
					return #[]
				}
			}

			override protected nodeChosen(XNode choice) {
				super.nodeChosen(choice)
				if (choice != null)
					root.commandStack.execute(
						new Layouter().createLayoutCommand(DOT, host.root.diagram, 500.millis))
			}
		}
		if(isInverse) {
			chooser.connectionProvider = [
				source, target, choiceInfo |
				new XConnection(target, source)
			]
		}
		
		val candidates = 
			if(isInverse) 
				hostBundle.allDependentBundles
			else 
				hostBundle.allDependencyBundles
		val context = new InterpreterContext(host.diagram) => [
			isCreateConnections = false
			isCreateDuplicateNodes = true
		]
		candidates.forEach [
			val candidateNode = interpreter.createNode(it, config.pluginNode, context)
			chooser.addChoice(candidateNode)
		]		
		root.currentTool = chooser
		null
	}

}
