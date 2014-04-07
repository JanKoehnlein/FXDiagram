package de.fxdiagram.examples.java;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.examples.java.AddReferenceRapidButtonBehavior;
import de.fxdiagram.examples.java.AddSuperTypeRapidButtonBehavior;
import de.fxdiagram.examples.java.JavaProperty;
import de.fxdiagram.examples.java.JavaTypeDescriptor;
import de.fxdiagram.examples.java.JavaTypeModel;
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

@ModelNode({ "layoutX", "layoutY", "domainObject", "width", "height" })
@SuppressWarnings("all")
public class JavaTypeNode extends XNode {
  private final VBox propertyCompartment = new VBox();
  
  private final VBox operationCompartment = new VBox();
  
  private JavaTypeModel model;
  
  public JavaTypeNode(final JavaTypeDescriptor domainObject) {
    super(domainObject);
  }
  
  protected Node createNode() {
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
                Class<?> _javaType = JavaTypeNode.this.getJavaType();
                String _simpleName = _javaType.getSimpleName();
                it.setText(_simpleName);
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
            _children.add(_doubleArrow);
            ObservableList<Node> _children_1 = it.getChildren();
            Separator _separator = new Separator();
            _children_1.add(_separator);
            ObservableList<Node> _children_2 = it.getChildren();
            final Procedure1<VBox> _function_1 = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                Insets _insets = new Insets(5, 10, 5, 10);
                VBox.setMargin(it, _insets);
              }
            };
            VBox _doubleArrow_1 = ObjectExtensions.<VBox>operator_doubleArrow(JavaTypeNode.this.propertyCompartment, _function_1);
            _children_2.add(_doubleArrow_1);
            ObservableList<Node> _children_3 = it.getChildren();
            Separator _separator_1 = new Separator();
            _children_3.add(_separator_1);
            ObservableList<Node> _children_4 = it.getChildren();
            final Procedure1<VBox> _function_2 = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                Insets _insets = new Insets(5, 10, 5, 10);
                VBox.setMargin(it, _insets);
              }
            };
            VBox _doubleArrow_2 = ObjectExtensions.<VBox>operator_doubleArrow(JavaTypeNode.this.operationCompartment, _function_2);
            _children_4.add(_doubleArrow_2);
            it.setAlignment(Pos.CENTER);
          }
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
        _children.add(_doubleArrow);
      }
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
  }
  
  public Class<?> getJavaType() {
    DomainObjectDescriptor _domainObject = this.getDomainObject();
    return ((JavaTypeDescriptor) _domainObject).getDomainObject();
  }
  
  public JavaTypeModel getJavaTypeModel() {
    JavaTypeModel _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.model, null);
      if (_equals) {
        Class<?> _javaType = this.getJavaType();
        JavaTypeModel _javaTypeModel = new JavaTypeModel(_javaType);
        this.model = _javaTypeModel;
      }
      _xblockexpression = this.model;
    }
    return _xblockexpression;
  }
  
  protected Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 12, 12);
  }
  
  public void populateCompartments() {
    JavaTypeModel _javaTypeModel = this.getJavaTypeModel();
    List<JavaProperty> _properties = _javaTypeModel.getProperties();
    List<JavaProperty> _limit = this.<JavaProperty>limit(_properties);
    final Procedure1<JavaProperty> _function = new Procedure1<JavaProperty>() {
      public void apply(final JavaProperty property) {
        ObservableList<Node> _children = JavaTypeNode.this.propertyCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            StringConcatenation _builder = new StringConcatenation();
            String _name = property.getName();
            _builder.append(_name, "");
            _builder.append(": ");
            Class<?> _type = property.getType();
            String _simpleName = _type.getSimpleName();
            _builder.append(_simpleName, "");
            it.setText(_builder.toString());
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
        _children.add(_doubleArrow);
      }
    };
    IterableExtensions.<JavaProperty>forEach(_limit, _function);
    JavaTypeModel _javaTypeModel_1 = this.getJavaTypeModel();
    List<Constructor<?>> _constructors = _javaTypeModel_1.getConstructors();
    final Procedure1<Constructor<?>> _function_1 = new Procedure1<Constructor<?>>() {
      public void apply(final Constructor<?> constructor) {
        ObservableList<Node> _children = JavaTypeNode.this.operationCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            StringConcatenation _builder = new StringConcatenation();
            Class<?> _javaType = JavaTypeNode.this.getJavaType();
            String _simpleName = _javaType.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append("(");
            Class<?>[] _parameterTypes = constructor.getParameterTypes();
            final Function1<Class<?>,String> _function = new Function1<Class<?>,String>() {
              public String apply(final Class<?> it) {
                return it.getSimpleName();
              }
            };
            List<String> _map = ListExtensions.<Class<?>, String>map(((List<Class<?>>)Conversions.doWrapArray(_parameterTypes)), _function);
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
    IterableExtensions.<Constructor<?>>forEach(_constructors, _function_1);
    JavaTypeModel _javaTypeModel_2 = this.getJavaTypeModel();
    List<Method> _operations = _javaTypeModel_2.getOperations();
    List<Method> _limit_1 = this.<Method>limit(_operations);
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
            Class<?>[] _parameterTypes = method.getParameterTypes();
            final Function1<Class<?>,String> _function = new Function1<Class<?>,String>() {
              public String apply(final Class<?> it) {
                return it.getSimpleName();
              }
            };
            List<String> _map = ListExtensions.<Class<?>, String>map(((List<Class<?>>)Conversions.doWrapArray(_parameterTypes)), _function);
            String _join = IterableExtensions.join(_map, ", ");
            _builder.append(_join, "");
            _builder.append("): ");
            Class<?> _returnType = method.getReturnType();
            String _simpleName = _returnType.getSimpleName();
            _builder.append(_simpleName, "");
            it.setText(_builder.toString());
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
        _children.add(_doubleArrow);
      }
    };
    IterableExtensions.<Method>forEach(_limit_1, _function_2);
  }
  
  protected <T extends Object> List<T> limit(final List<T> list) {
    List<T> _xifexpression = null;
    boolean _isEmpty = list.isEmpty();
    if (_isEmpty) {
      _xifexpression = list;
    } else {
      List<T> _xifexpression_1 = null;
      boolean _isActive = this.getIsActive();
      if (_isActive) {
        _xifexpression_1 = list;
      } else {
        int _size = list.size();
        int _min = Math.min(_size, 4);
        _xifexpression_1 = list.subList(0, _min);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public void doActivate() {
    super.doActivate();
    this.populateCompartments();
    AddSuperTypeRapidButtonBehavior _addSuperTypeRapidButtonBehavior = new AddSuperTypeRapidButtonBehavior(this);
    this.addBehavior(_addSuperTypeRapidButtonBehavior);
    AddReferenceRapidButtonBehavior _addReferenceRapidButtonBehavior = new AddReferenceRapidButtonBehavior(this);
    this.addBehavior(_addReferenceRapidButtonBehavior);
  }
  
  public String toString() {
    Class<?> _javaType = this.getJavaType();
    return _javaType.getSimpleName();
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public JavaTypeNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(layoutXProperty(), Double.class);
    modelElement.addProperty(layoutYProperty(), Double.class);
    modelElement.addProperty(domainObjectProperty(), DomainObjectDescriptor.class);
    modelElement.addProperty(widthProperty(), Double.class);
    modelElement.addProperty(heightProperty(), Double.class);
  }
}
