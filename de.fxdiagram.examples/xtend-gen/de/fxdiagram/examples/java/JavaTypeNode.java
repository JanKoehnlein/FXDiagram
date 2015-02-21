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
import de.fxdiagram.lib.animations.Inflator;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
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

@ModelNode
@SuppressWarnings("all")
public class JavaTypeNode extends XNode {
  private final VBox propertyCompartment = new VBox();
  
  private final VBox operationCompartment = new VBox();
  
  private final VBox contentArea = new VBox();
  
  private JavaTypeModel model;
  
  public JavaTypeNode(final JavaTypeDescriptor domainObject) {
    super(domainObject);
  }
  
  @Override
  protected Node createNode() {
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
      @Override
      public void apply(final RectangleBorderPane it) {
        ObservableList<Node> _children = it.getChildren();
        final Procedure1<VBox> _function = new Procedure1<VBox>() {
          @Override
          public void apply(final VBox it) {
            Insets _insets = new Insets(15, 20, 15, 20);
            it.setPadding(_insets);
            ObservableList<Node> _children = it.getChildren();
            Text _text = new Text();
            final Procedure1<Text> _function = new Procedure1<Text>() {
              @Override
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
              }
            };
            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
            _children.add(_doubleArrow);
            it.setAlignment(Pos.CENTER);
          }
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(JavaTypeNode.this.contentArea, _function);
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
  
  @Override
  protected Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 12, 12);
  }
  
  public void populateCompartments() {
    JavaTypeModel _javaTypeModel = this.getJavaTypeModel();
    List<JavaProperty> _properties = _javaTypeModel.getProperties();
    List<JavaProperty> _limit = this.<JavaProperty>limit(_properties);
    final Consumer<JavaProperty> _function = new Consumer<JavaProperty>() {
      @Override
      public void accept(final JavaProperty property) {
        ObservableList<Node> _children = JavaTypeNode.this.propertyCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          @Override
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
    _limit.forEach(_function);
    JavaTypeModel _javaTypeModel_1 = this.getJavaTypeModel();
    List<Constructor<?>> _constructors = _javaTypeModel_1.getConstructors();
    final Consumer<Constructor<?>> _function_1 = new Consumer<Constructor<?>>() {
      @Override
      public void accept(final Constructor<?> constructor) {
        ObservableList<Node> _children = JavaTypeNode.this.operationCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          @Override
          public void apply(final Text it) {
            StringConcatenation _builder = new StringConcatenation();
            Class<?> _javaType = JavaTypeNode.this.getJavaType();
            String _simpleName = _javaType.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append("(");
            Class<?>[] _parameterTypes = constructor.getParameterTypes();
            final Function1<Class<?>, String> _function = new Function1<Class<?>, String>() {
              @Override
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
    _constructors.forEach(_function_1);
    JavaTypeModel _javaTypeModel_2 = this.getJavaTypeModel();
    List<Method> _operations = _javaTypeModel_2.getOperations();
    List<Method> _limit_1 = this.<Method>limit(_operations);
    final Consumer<Method> _function_2 = new Consumer<Method>() {
      @Override
      public void accept(final Method method) {
        ObservableList<Node> _children = JavaTypeNode.this.operationCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          @Override
          public void apply(final Text it) {
            StringConcatenation _builder = new StringConcatenation();
            String _name = method.getName();
            _builder.append(_name, "");
            _builder.append("(");
            Class<?>[] _parameterTypes = method.getParameterTypes();
            final Function1<Class<?>, String> _function = new Function1<Class<?>, String>() {
              @Override
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
    _limit_1.forEach(_function_2);
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
  
  @Override
  public void doActivate() {
    super.doActivate();
    Insets _insets = new Insets(15, 0, 0, 0);
    this.propertyCompartment.setPadding(_insets);
    Insets _insets_1 = new Insets(10, 0, 0, 0);
    this.operationCompartment.setPadding(_insets_1);
    this.populateCompartments();
    final Inflator inflator = new Inflator(this, this.contentArea);
    inflator.addInflatable(this.propertyCompartment, 1);
    inflator.addInflatable(this.operationCompartment, 2);
    Transition _inflateAnimation = inflator.getInflateAnimation();
    if (_inflateAnimation!=null) {
      _inflateAnimation.play();
    }
    AddSuperTypeRapidButtonBehavior _addSuperTypeRapidButtonBehavior = new AddSuperTypeRapidButtonBehavior(this);
    this.addBehavior(_addSuperTypeRapidButtonBehavior);
    AddReferenceRapidButtonBehavior _addReferenceRapidButtonBehavior = new AddReferenceRapidButtonBehavior(this);
    this.addBehavior(_addReferenceRapidButtonBehavior);
  }
  
  @Override
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
    super.populate(modelElement);
  }
}
