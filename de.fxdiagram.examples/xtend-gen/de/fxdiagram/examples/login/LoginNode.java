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
      final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
        @Override
        public void apply(final RectangleBorderPane it) {
          ObservableList<Node> _children = it.getChildren();
          Text _text = new Text();
          final Procedure1<Text> _function = new Procedure1<Text>() {
            @Override
            public void apply(final Text it) {
              it.setTextOrigin(VPos.TOP);
              it.setText("Login");
              Insets _insets = new Insets(10, 20, 10, 20);
              StackPane.setMargin(it, _insets);
            }
          };
          Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
          _children.add(_doubleArrow);
        }
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
      final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
        @Override
        public void apply(final RectangleBorderPane it) {
          ObservableList<Node> _children = it.getChildren();
          GridPane _gridPane = new GridPane();
          final Procedure1<GridPane> _function = new Procedure1<GridPane>() {
            @Override
            public void apply(final GridPane it) {
              it.setHgap(10);
              it.setVgap(10);
              Insets _insets = new Insets(25, 25, 25, 25);
              it.setPadding(_insets);
              Text _text = new Text();
              final Procedure1<Text> _function = new Procedure1<Text>() {
                @Override
                public void apply(final Text it) {
                  it.setTextOrigin(VPos.TOP);
                  Font _font = Font.font("Tahoma", FontWeight.NORMAL, 20);
                  it.setFont(_font);
                  StringProperty _textProperty = it.textProperty();
                  _textProperty.bind(welcomeMessage);
                }
              };
              Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
              it.add(_doubleArrow, 0, 0, 2, 1);
              Label _label = new Label("User Name:");
              it.add(_label, 0, 1);
              TextField _textField = new TextField();
              final Procedure1<TextField> _function_1 = new Procedure1<TextField>() {
                @Override
                public void apply(final TextField it) {
                  StringProperty _textProperty = it.textProperty();
                  _textProperty.bindBidirectional(LoginNode.this.userNameProperty);
                }
              };
              TextField _doubleArrow_1 = ObjectExtensions.<TextField>operator_doubleArrow(_textField, _function_1);
              it.add(_doubleArrow_1, 1, 1);
              Label _label_1 = new Label("Password:");
              it.add(_label_1, 0, 2);
              PasswordField _passwordField = new PasswordField();
              final Procedure1<PasswordField> _function_2 = new Procedure1<PasswordField>() {
                @Override
                public void apply(final PasswordField it) {
                  StringProperty _textProperty = it.textProperty();
                  _textProperty.bindBidirectional(LoginNode.this.passwordProperty);
                }
              };
              PasswordField _doubleArrow_2 = ObjectExtensions.<PasswordField>operator_doubleArrow(_passwordField, _function_2);
              it.add(_doubleArrow_2, 1, 2);
              HBox _hBox = new HBox(10);
              final Procedure1<HBox> _function_3 = new Procedure1<HBox>() {
                @Override
                public void apply(final HBox it) {
                  it.setAlignment(Pos.BOTTOM_RIGHT);
                  ObservableList<Node> _children = it.getChildren();
                  Button _button = new Button("Sign in");
                  final Procedure1<Button> _function = new Procedure1<Button>() {
                    @Override
                    public void apply(final Button it) {
                      final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent it) {
                          LoginNode.this.flip(true);
                        }
                      };
                      it.setOnAction(_function);
                    }
                  };
                  Button _doubleArrow = ObjectExtensions.<Button>operator_doubleArrow(_button, _function);
                  _children.add(_doubleArrow);
                }
              };
              HBox _doubleArrow_3 = ObjectExtensions.<HBox>operator_doubleArrow(_hBox, _function_3);
              it.add(_doubleArrow_3, 1, 4);
            }
          };
          GridPane _doubleArrow = ObjectExtensions.<GridPane>operator_doubleArrow(_gridPane, _function);
          _children.add(_doubleArrow);
        }
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
