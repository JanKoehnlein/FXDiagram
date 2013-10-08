package de.fxdiagram.examples.login

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XNode
import de.fxdiagram.lib.nodes.FlipNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text

import static extension de.fxdiagram.core.extensions.StringExpressionExtensions.*

class LoginNode extends XNode {

	@FxProperty String userName = ""
	@FxProperty String password

	new() {
		super("Login")
		node = new FlipNode => [
			front = createForm
			back = new RectangleBorderPane => [
				children += new Text => [
					textOrigin = VPos.TOP
					text = key
					StackPane.setMargin(it, new Insets(10, 20, 10, 20))
				]
			]
		]
	}

	def createForm() {
		val welcomeMessage = "Welcome " + userNameProperty + "!"
		new RectangleBorderPane => [
			children += new GridPane => [
				hgap = 10
				vgap = 10
				padding = new Insets(25, 25, 25, 25)
				add(new Text => [
						textOrigin = VPos.TOP
						font = Font.font("Tahoma", FontWeight.NORMAL, 20)
						textProperty.bind(welcomeMessage)
					], 0, 0, 2, 1)
				add(new Label("User Name:"), 0, 1)
				add(new TextField => [
						textProperty.bindBidirectional(userNameProperty)
					], 1, 1)
				add(new Label("Password:"), 0, 2)
				add(new PasswordField => [
						textProperty.bindBidirectional(passwordProperty)
					], 1, 2)
				add(new HBox(10) => [
						alignment = Pos.BOTTOM_RIGHT
						children += new Button("Sign in") => [
							onAction = [
								flipNode.flip(true)
								flipNode.flipOnDoubleClick = true
							]
						]
					], 1, 4)
			]
		]
	}

	protected def getFlipNode() {
		node as FlipNode
	}
}
