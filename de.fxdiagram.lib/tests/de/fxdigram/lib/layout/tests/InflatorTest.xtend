package de.fxdigram.lib.layout.tests

import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.command.AnimationCommand
import de.fxdiagram.core.command.CommandContext
import de.fxdiagram.core.command.SequentialAnimationCommand
import de.fxdiagram.lib.nodes.AbstractClassNode
import de.fxdiagram.lib.nodes.ClassModel
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class InflatorTest extends Application {
	def static void main(String[] args) {
		launch
	}

	override start(Stage stage) throws Exception {
		val root = new XRoot
		stage.scene = new Scene(root, 640, 480)
		val node = new AbstractClassNode() {
			override inferClassModel() {
				new ClassModel => [
					name = 'Foo'
					attributes += #['a', 'b']
					operations += #['a', 'b']
				]
			}
		}
		root => [
			diagram = new XDiagram => [
				activate
				nodes += node
			]
		]
		stage.show
		root.onMouseClicked = [
			root.commandStack.execute(new SequentialAnimationCommand => [
				it += node.inflator.deflateCommand
				it += new AnimationCommand() {
					override getExecuteAnimation(CommandContext context) {
						node.model = new ClassModel => [
							name ='Bar'	
							attributes += node.model.attributes
							attributes += 'foo'
							operations += node.model.operations
							operations += 'foo'
						]
						null
					}
					
					override getUndoAnimation(CommandContext context) {
						null
					}
					
					override getRedoAnimation(CommandContext context) {
						null
					}
					
					override clearRedoStackOnExecute() {
						false
					}
					
					override skipViewportRestore() {
					}
				}
				it += node.inflator.inflateCommand
			])
		]
	}

}