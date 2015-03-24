package de.fxdiagram.examples.login;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.extensions.StringExpressionExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.nodes.FlipNode;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class LoginNode extends FlipNode {
  public LoginNode(final String name) {
    super(name);
  }
  
  @Override
  protected Node createNode() {
    Node _xblockexpression = null;
    {
      final Node node = super.createNode();
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
        ObservableList<Node> _children = it.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function_1 = (Text it_1) -> {
          it_1.setTextOrigin(VPos.TOP);
          it_1.setText("Login");
          Insets _insets = new Insets(10, 20, 10, 20);
          StackPane.setMargin(it_1, _insets);
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_1);
        _children.add(_doubleArrow);
      };
      RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
      this.setFront(_doubleArrow);
      RectangleBorderPane _createForm = this.createForm();
      this.setBack(_createForm);
      _xblockexpression = node;
    }
    return _xblockexpression;
  }
  
  public RectangleBorderPane createForm() {
    RectangleBorderPane _xblockexpression = null;
    {
      StringExpression _plus = StringExpressionExtensions.operator_plus("Welcome ", this.userNameProperty);
      final StringExpression welcomeMessage = StringExpressionExtensions.operator_plus(_plus, "!");
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
        ObservableList<Node> _children = it.getChildren();
        GridPane _gridPane = new GridPane();
        final Procedure1<GridPane> _function_1 = (GridPane it_1) -> {
          it_1.setHgap(10);
          it_1.setVgap(10);
          Insets _insets = new Insets(25, 25, 25, 25);
          it_1.setPadding(_insets);
          Text _text = new Text();
          final Procedure1<Text> _function_2 = (Text it_2) -> {
            it_2.setTextOrigin(VPos.TOP);
            Font _font = Font.font("Tahoma", FontWeight.NORMAL, 20);
            it_2.setFont(_font);
            StringProperty _textProperty = it_2.textProperty();
            _textProperty.bind(welcomeMessage);
          };
          Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_2);
          it_1.add(_doubleArrow, 0, 0, 2, 1);
          Label _label = new Label("User Name:");
          it_1.add(_label, 0, 1);
          TextField _textField = new TextField();
          final Procedure1<TextField> _function_3 = (TextField it_2) -> {
            StringProperty _textProperty = it_2.textProperty();
            _textProperty.bindBidirectional(this.userNameProperty);
          };
          TextField _doubleArrow_1 = ObjectExtensions.<TextField>operator_doubleArrow(_textField, _function_3);
          it_1.add(_doubleArrow_1, 1, 1);
          Label _label_1 = new Label("Password:");
          it_1.add(_label_1, 0, 2);
          PasswordField _passwordField = new PasswordField();
          final Procedure1<PasswordField> _function_4 = (PasswordField it_2) -> {
            StringProperty _textProperty = it_2.textProperty();
            _textProperty.bindBidirectional(this.passwordProperty);
          };
          PasswordField _doubleArrow_2 = ObjectExtensions.<PasswordField>operator_doubleArrow(_passwordField, _function_4);
          it_1.add(_doubleArrow_2, 1, 2);
          HBox _hBox = new HBox(10);
          final Procedure1<HBox> _function_5 = (HBox it_2) -> {
            it_2.setAlignment(Pos.BOTTOM_RIGHT);
            ObservableList<Node> _children_1 = it_2.getChildren();
            Button _button = new Button("Sign in");
            final Procedure1<Button> _function_6 = (Button it_3) -> {
              final EventHandler<ActionEvent> _function_7 = (ActionEvent it_4) -> {
                this.flip(true);
              };
              it_3.setOnAction(_function_7);
            };
            Button _doubleArrow_3 = ObjectExtensions.<Button>operator_doubleArrow(_button, _function_6);
            _children_1.add(_doubleArrow_3);
          };
          HBox _doubleArrow_3 = ObjectExtensions.<HBox>operator_doubleArrow(_hBox, _function_5);
          it_1.add(_doubleArrow_3, 1, 4);
        };
        GridPane _doubleArrow = ObjectExtensions.<GridPane>operator_doubleArrow(_gridPane, _function_1);
        _children.add(_doubleArrow);
      };
      _xblockexpression = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
    }
    return _xblockexpression;
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public LoginNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  private SimpleStringProperty userNameProperty = new SimpleStringProperty(this, "userName",_initUserName());
  
  private static final String _initUserName() {
    return "";
  }
  
  public String getUserName() {
    return this.userNameProperty.get();
  }
  
  public void setUserName(final String userName) {
    this.userNameProperty.set(userName);
  }
  
  public StringProperty userNameProperty() {
    return this.userNameProperty;
  }
  
  private SimpleStringProperty passwordProperty = new SimpleStringProperty(this, "password");
  
  public String getPassword() {
    return this.passwordProperty.get();
  }
  
  public void setPassword(final String password) {
    this.passwordProperty.set(password);
  }
  
  public StringProperty passwordProperty() {
    return this.passwordProperty;
  }
}
