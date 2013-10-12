package de.fxdiagram.examples.java;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.examples.java.AddReferenceRapidButtonBehavior;
import de.fxdiagram.examples.java.AddSuperTypeRapidButtonBehavior;
import de.fxdiagram.examples.java.JavaTypeModel;
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
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class JavaTypeNode extends XNode {
  private Class<? extends Object> javaType;
  
  private final Text name = new Function0<Text>() {
    public Text apply() {
      Text _text = new Text();
      return _text;
    }
  }.apply();
  
  private final VBox propertyCompartment = new Function0<VBox>() {
    public VBox apply() {
      VBox _vBox = new VBox();
      return _vBox;
    }
  }.apply();
  
  private final VBox operationCompartment = new Function0<VBox>() {
    public VBox apply() {
      VBox _vBox = new VBox();
      return _vBox;
    }
  }.apply();
  
  private JavaTypeModel model;
  
  public JavaTypeNode(final Class<? extends Object> javaType) {
    super(new Function0<String>() {
      public String apply() {
        String _simpleName = javaType.getSimpleName();
        return _simpleName;
      }
    }.apply());
    this.javaType = javaType;
    String _simpleName = javaType.getSimpleName();
    this.name.setText(_simpleName);
    JavaTypeModel _javaTypeModel = new JavaTypeModel(javaType);
    this.model = _javaTypeModel;
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
      public void apply(final RectangleBorderPane it) {
        ObservableList<Node> _children = it.getChildren();
        VBox _vBox = new VBox();
        final Procedure1<VBox> _function = new Procedure1<VBox>() {
          public void apply(final VBox it) {
            ObservableList<Node> _children = it.getChildren();
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
            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(JavaTypeNode.this.name, _function);
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
    RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
    this.setNode(_doubleArrow);
  }
  
  protected Anchors createAnchors() {
    RoundedRectangleAnchors _roundedRectangleAnchors = new RoundedRectangleAnchors(this, 12, 12);
    return _roundedRectangleAnchors;
  }
  
  public void populateComprtments() {
    List<Property> _properties = this.model.getProperties();
    List<Property> _limit = this.<Property>limit(_properties);
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
    IterableExtensions.<Property>forEach(_limit, _function);
    List<Constructor<? extends Object>> _constructors = this.model.getConstructors();
    final Procedure1<Constructor<? extends Object>> _function_1 = new Procedure1<Constructor<? extends Object>>() {
      public void apply(final Constructor<? extends Object> constructor) {
        ObservableList<Node> _children = JavaTypeNode.this.operationCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            StringConcatenation _builder = new StringConcatenation();
            String _simpleName = JavaTypeNode.this.javaType.getSimpleName();
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
    List<Method> _operations = this.model.getOperations();
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
        List<T> _subList = list.subList(0, _min);
        _xifexpression_1 = _subList;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public Class<? extends Object> getJavaType() {
    return this.javaType;
  }
  
  public JavaTypeModel getJavaTypeModel() {
    return this.model;
  }
  
  public void activate() {
    super.activate();
    this.populateComprtments();
    AddSuperTypeRapidButtonBehavior _addSuperTypeRapidButtonBehavior = new AddSuperTypeRapidButtonBehavior(this);
    _addSuperTypeRapidButtonBehavior.activate();
    AddReferenceRapidButtonBehavior _addReferenceRapidButtonBehavior = new AddReferenceRapidButtonBehavior(this);
    _addReferenceRapidButtonBehavior.activate();
  }
}
