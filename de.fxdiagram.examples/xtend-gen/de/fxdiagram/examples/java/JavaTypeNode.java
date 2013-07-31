package de.fxdiagram.examples.java;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.examples.java.JavaTypeModel;
import de.fxdiagram.examples.java.JavaTypeRapidButtonBehavior;
import de.fxdiagram.examples.java.Property;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class JavaTypeNode extends XNode {
  private Class<? extends Object> javaType;
  
  private Text name;
  
  private VBox propertyCompartment;
  
  private VBox operationCompartment;
  
  public JavaTypeNode() {
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
      public void apply(final RectangleBorderPane it) {
        ObservableList<Node> _children = it.getChildren();
        VBox _vBox = new VBox();
        final Procedure1<VBox> _function = new Procedure1<VBox>() {
          public void apply(final VBox it) {
            ObservableList<Node> _children = it.getChildren();
            Text _text = new Text();
            final Procedure1<Text> _function = new Procedure1<Text>() {
              public void apply(final Text it) {
                it.setTextOrigin(VPos.TOP);
                Font _font = it.getFont();
                String _family = _font.getFamily();
                Font _font_1 = it.getFont();
                double _size = _font_1.getSize();
                double _multiply = (_size * 1.1);
                Font _font_2 = Font.font(_family, FontWeight.BOLD, _multiply);
                it.setFont(_font_2);
                Insets _insets = new Insets(12, 12, 12, 12);
                VBox.setMargin(it, _insets);
              }
            };
            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
            Text _name = JavaTypeNode.this.name = _doubleArrow;
            _children.add(_name);
            ObservableList<Node> _children_1 = it.getChildren();
            Separator _separator = new Separator();
            _children_1.add(_separator);
            ObservableList<Node> _children_2 = it.getChildren();
            VBox _vBox = new VBox();
            final Procedure1<VBox> _function_1 = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                Insets _insets = new Insets(5, 10, 5, 10);
                VBox.setMargin(it, _insets);
              }
            };
            VBox _doubleArrow_1 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_1);
            VBox _propertyCompartment = JavaTypeNode.this.propertyCompartment = _doubleArrow_1;
            _children_2.add(_propertyCompartment);
            ObservableList<Node> _children_3 = it.getChildren();
            Separator _separator_1 = new Separator();
            _children_3.add(_separator_1);
            ObservableList<Node> _children_4 = it.getChildren();
            VBox _vBox_1 = new VBox();
            final Procedure1<VBox> _function_2 = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                Insets _insets = new Insets(5, 10, 5, 10);
                VBox.setMargin(it, _insets);
              }
            };
            VBox _doubleArrow_2 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_2);
            VBox _operationCompartment = JavaTypeNode.this.operationCompartment = _doubleArrow_2;
            _children_4.add(_operationCompartment);
            it.setAlignment(Pos.CENTER);
          }
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
        _children.add(_doubleArrow);
      }
    };
    RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
    this.setNode(_doubleArrow);
  }
  
  protected Anchors createAnchors() {
    RoundedRectangleAnchors _roundedRectangleAnchors = new RoundedRectangleAnchors(this, 12, 12);
    return _roundedRectangleAnchors;
  }
  
  public void setJavaType(final Class<? extends Object> javaType) {
    this.javaType = javaType;
    String _simpleName = javaType.getSimpleName();
    this.name.setText(_simpleName);
    JavaTypeModel _javaTypeModel = new JavaTypeModel(javaType);
    final JavaTypeModel model = _javaTypeModel;
    List<Property> _properties = model.getProperties();
    final Procedure1<Property> _function = new Procedure1<Property>() {
      public void apply(final Property property) {
        ObservableList<Node> _children = JavaTypeNode.this.propertyCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            StringConcatenation _builder = new StringConcatenation();
            String _name = property.getName();
            _builder.append(_name, "");
            _builder.append(": ");
            Class<? extends Object> _type = property.getType();
            String _simpleName = _type.getSimpleName();
            _builder.append(_simpleName, "");
            it.setText(_builder.toString());
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
        _children.add(_doubleArrow);
      }
    };
    IterableExtensions.<Property>forEach(_properties, _function);
    List<Constructor<? extends Object>> _constructors = model.getConstructors();
    final Procedure1<Constructor<? extends Object>> _function_1 = new Procedure1<Constructor<? extends Object>>() {
      public void apply(final Constructor<? extends Object> constructor) {
        ObservableList<Node> _children = JavaTypeNode.this.operationCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = javaType.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append("(");
            Class<? extends Object>[] _parameterTypes = constructor.getParameterTypes();
            final Function1<Class<? extends Object>,String> _function = new Function1<Class<? extends Object>,String>() {
              public String apply(final Class<? extends Object> it) {
                String _simpleName = it.getSimpleName();
                return _simpleName;
              }
            };
            List<String> _map = ListExtensions.<Class<? extends Object>, String>map(((List<Class<? extends Object>>)Conversions.doWrapArray(_parameterTypes)), _function);
            String _join = IterableExtensions.join(_map, ", ");
            _builder.append(_join, "");
            _builder.append(")");
            it.setText(_builder.toString());
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
        _children.add(_doubleArrow);
      }
    };
    IterableExtensions.<Constructor<? extends Object>>forEach(_constructors, _function_1);
    List<Method> _operations = model.getOperations();
    final Procedure1<Method> _function_2 = new Procedure1<Method>() {
      public void apply(final Method method) {
        ObservableList<Node> _children = JavaTypeNode.this.operationCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            StringConcatenation _builder = new StringConcatenation();
            String _name = method.getName();
            _builder.append(_name, "");
            _builder.append("(");
            Class<? extends Object>[] _parameterTypes = method.getParameterTypes();
            final Function1<Class<? extends Object>,String> _function = new Function1<Class<? extends Object>,String>() {
              public String apply(final Class<? extends Object> it) {
                String _simpleName = it.getSimpleName();
                return _simpleName;
              }
            };
            List<String> _map = ListExtensions.<Class<? extends Object>, String>map(((List<Class<? extends Object>>)Conversions.doWrapArray(_parameterTypes)), _function);
            String _join = IterableExtensions.join(_map, ", ");
            _builder.append(_join, "");
            _builder.append("): ");
            Class<? extends Object> _returnType = method.getReturnType();
            String _simpleName = _returnType.getSimpleName();
            _builder.append(_simpleName, "");
            it.setText(_builder.toString());
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
        _children.add(_doubleArrow);
      }
    };
    IterableExtensions.<Method>forEach(_operations, _function_2);
  }
  
  public Class<? extends Object> getJavaType() {
    return this.javaType;
  }
  
  public void activate() {
    boolean _notEquals = (!Objects.equal(this.javaType, null));
    if (_notEquals) {
      super.activate();
      JavaTypeRapidButtonBehavior _javaTypeRapidButtonBehavior = new JavaTypeRapidButtonBehavior(this);
      _javaTypeRapidButtonBehavior.activate();
    }
  }
}
