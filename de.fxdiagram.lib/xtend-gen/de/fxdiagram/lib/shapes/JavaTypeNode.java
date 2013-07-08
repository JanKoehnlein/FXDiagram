package de.fxdiagram.lib.shapes;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.shapes.JavaTypeRapidButtonBehavior;
import de.fxdiagram.lib.shapes.RectangleBorderPane;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
  
  private VBox attributeCompartment;
  
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
                      double _multiply = (_size * 1.5);
                      Font _font_2 = Font.font(_family, FontWeight.BOLD, _multiply);
                      it.setFont(_font_2);
                      Insets _insets = new Insets(20, 20, 20, 15);
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
                      Insets _insets = new Insets(10, 10, 10, 10);
                      VBox.setMargin(it, _insets);
                    }
                  };
                VBox _doubleArrow_1 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_1);
                VBox _attributeCompartment = JavaTypeNode.this.attributeCompartment = _doubleArrow_1;
                _children_2.add(_attributeCompartment);
                ObservableList<Node> _children_3 = it.getChildren();
                Separator _separator_1 = new Separator();
                _children_3.add(_separator_1);
                ObservableList<Node> _children_4 = it.getChildren();
                VBox _vBox_1 = new VBox();
                final Procedure1<VBox> _function_2 = new Procedure1<VBox>() {
                    public void apply(final VBox it) {
                      Insets _insets = new Insets(10, 10, 10, 10);
                      VBox.setMargin(it, _insets);
                    }
                  };
                VBox _doubleArrow_2 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_2);
                VBox _operationCompartment = JavaTypeNode.this.operationCompartment = _doubleArrow_2;
                _children_4.add(_operationCompartment);
                it.setAlignment(Pos.CENTER);
                it.setSpacing(5);
              }
            };
          VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
          _children.add(_doubleArrow);
        }
      };
    RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
    this.setNode(_doubleArrow);
  }
  
  public void setJavaType(final Class<? extends Object> javaType) {
    this.javaType = javaType;
    String _simpleName = javaType.getSimpleName();
    this.name.setText(_simpleName);
    Field[] _declaredFields = javaType.getDeclaredFields();
    final Function1<Field,Boolean> _function = new Function1<Field,Boolean>() {
        public Boolean apply(final Field it) {
          boolean _and = false;
          int _modifiers = it.getModifiers();
          boolean _isPublic = Modifier.isPublic(_modifiers);
          if (!_isPublic) {
            _and = false;
          } else {
            boolean _or = false;
            Class<? extends Object> _type = it.getType();
            boolean _isPrimitive = _type.isPrimitive();
            if (_isPrimitive) {
              _or = true;
            } else {
              Class<? extends Object> _type_1 = it.getType();
              boolean _equals = String.class.equals(_type_1);
              _or = (_isPrimitive || _equals);
            }
            _and = (_isPublic && _or);
          }
          return Boolean.valueOf(_and);
        }
      };
    Iterable<Field> _filter = IterableExtensions.<Field>filter(((Iterable<Field>)Conversions.doWrapArray(_declaredFields)), _function);
    final Procedure1<Field> _function_1 = new Procedure1<Field>() {
        public void apply(final Field attribute) {
          ObservableList<Node> _children = JavaTypeNode.this.attributeCompartment.getChildren();
          Text _text = new Text();
          final Procedure1<Text> _function = new Procedure1<Text>() {
              public void apply(final Text it) {
                String _name = attribute.getName();
                String _plus = (_name + ": ");
                Class<? extends Object> _type = attribute.getType();
                String _simpleName = _type.getSimpleName();
                String _plus_1 = (_plus + _simpleName);
                it.setText(_plus_1);
                it.setTextOrigin(VPos.TOP);
              }
            };
          Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
          _children.add(_doubleArrow);
        }
      };
    IterableExtensions.<Field>forEach(_filter, _function_1);
    Constructor<? extends Object>[] _declaredConstructors = javaType.getDeclaredConstructors();
    final Function1<Constructor<? extends Object>,Boolean> _function_2 = new Function1<Constructor<? extends Object>,Boolean>() {
        public Boolean apply(final Constructor<? extends Object> it) {
          int _modifiers = it.getModifiers();
          boolean _isPublic = Modifier.isPublic(_modifiers);
          return Boolean.valueOf(_isPublic);
        }
      };
    Iterable<Constructor<? extends Object>> _filter_1 = IterableExtensions.<Constructor<? extends Object>>filter(((Iterable<Constructor<? extends Object>>)Conversions.doWrapArray(_declaredConstructors)), _function_2);
    final Procedure1<Constructor<? extends Object>> _function_3 = new Procedure1<Constructor<? extends Object>>() {
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
    IterableExtensions.<Constructor<? extends Object>>forEach(_filter_1, _function_3);
    Method[] _declaredMethods = javaType.getDeclaredMethods();
    final Function1<Method,Boolean> _function_4 = new Function1<Method,Boolean>() {
        public Boolean apply(final Method it) {
          int _modifiers = it.getModifiers();
          boolean _isPublic = Modifier.isPublic(_modifiers);
          return Boolean.valueOf(_isPublic);
        }
      };
    Iterable<Method> _filter_2 = IterableExtensions.<Method>filter(((Iterable<Method>)Conversions.doWrapArray(_declaredMethods)), _function_4);
    final Procedure1<Method> _function_5 = new Procedure1<Method>() {
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
    IterableExtensions.<Method>forEach(_filter_2, _function_5);
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
