package de.fxdiagram.examples.java;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.AbstractBehavior;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.lib.tools.CarusselChooser;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class JavaTypeRapidButtonBehavior extends AbstractBehavior {
  public JavaTypeRapidButtonBehavior(final JavaTypeNode host) {
    super(host);
  }
  
  protected void doActivate() {
    XShape _host = this.getHost();
    final XNode host = ((XNode) _host);
    final Procedure1<XRapidButton> _function = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          Pos _chooserPosition = button.getChooserPosition();
          CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(host, _chooserPosition);
          final CoverFlowChooser chooser = _coverFlowChooser;
          final Class<? extends Object> javaType = ((JavaTypeNode) host).getJavaType();
          final ArrayList<Class<? extends Object>> supertypes = CollectionLiterals.<Class<?>>newArrayList();
          Class<? extends Object> _superclass = javaType.getSuperclass();
          boolean _notEquals = (!Objects.equal(_superclass, null));
          if (_notEquals) {
            Class<? extends Object> _superclass_1 = javaType.getSuperclass();
            supertypes.add(_superclass_1);
          }
          Class<? extends Object>[] _interfaces = javaType.getInterfaces();
          Iterables.<Class<? extends Object>>addAll(supertypes, ((Iterable<? extends Class<? extends Object>>)Conversions.doWrapArray(_interfaces)));
          final Function1<Class<? extends Object>,JavaTypeNode> _function = new Function1<Class<? extends Object>,JavaTypeNode>() {
              public JavaTypeNode apply(final Class<? extends Object> superType) {
                JavaTypeNode _javaTypeNode = new JavaTypeNode();
                final Procedure1<JavaTypeNode> _function = new Procedure1<JavaTypeNode>() {
                    public void apply(final JavaTypeNode it) {
                      it.setJavaType(superType);
                    }
                  };
                JavaTypeNode _doubleArrow = ObjectExtensions.<JavaTypeNode>operator_doubleArrow(_javaTypeNode, _function);
                return _doubleArrow;
              }
            };
          List<JavaTypeNode> _map = ListExtensions.<Class<? extends Object>, JavaTypeNode>map(supertypes, _function);
          chooser.operator_add(_map);
          XRootDiagram _rootDiagram = Extensions.getRootDiagram(host);
          _rootDiagram.setCurrentTool(chooser);
        }
      };
    final Procedure1<XRapidButton> addSuperTypeAction = _function;
    final Procedure1<XRapidButton> _function_1 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          Pos _chooserPosition = button.getChooserPosition();
          CarusselChooser _carusselChooser = new CarusselChooser(host, _chooserPosition);
          final CarusselChooser chooser = _carusselChooser;
          final Class<? extends Object> javaType = ((JavaTypeNode) host).getJavaType();
          Field[] _declaredFields = javaType.getDeclaredFields();
          final Function1<Field,Boolean> _function = new Function1<Field,Boolean>() {
              public Boolean apply(final Field it) {
                boolean _and = false;
                boolean _and_1 = false;
                int _modifiers = it.getModifiers();
                boolean _isPublic = Modifier.isPublic(_modifiers);
                if (!_isPublic) {
                  _and_1 = false;
                } else {
                  boolean _isPrimitive = Type.class.isPrimitive();
                  boolean _not = (!_isPrimitive);
                  _and_1 = (_isPublic && _not);
                }
                if (!_and_1) {
                  _and = false;
                } else {
                  boolean _equals = String.class.equals(Type.class);
                  boolean _not_1 = (!_equals);
                  _and = (_and_1 && _not_1);
                }
                return Boolean.valueOf(_and);
              }
            };
          final Iterable<Field> references = IterableExtensions.<Field>filter(((Iterable<Field>)Conversions.doWrapArray(_declaredFields)), _function);
          final Function1<Field,JavaTypeNode> _function_1 = new Function1<Field,JavaTypeNode>() {
              public JavaTypeNode apply(final Field reference) {
                JavaTypeNode _javaTypeNode = new JavaTypeNode();
                final Procedure1<JavaTypeNode> _function = new Procedure1<JavaTypeNode>() {
                    public void apply(final JavaTypeNode it) {
                      Class<? extends Object> _type = reference.getType();
                      it.setJavaType(_type);
                    }
                  };
                JavaTypeNode _doubleArrow = ObjectExtensions.<JavaTypeNode>operator_doubleArrow(_javaTypeNode, _function);
                return _doubleArrow;
              }
            };
          Iterable<JavaTypeNode> _map = IterableExtensions.<Field, JavaTypeNode>map(references, _function_1);
          chooser.operator_add(_map);
          XRootDiagram _rootDiagram = Extensions.getRootDiagram(host);
          _rootDiagram.setCurrentTool(chooser);
        }
      };
    final Procedure1<XRapidButton> addReferencesAction = _function_1;
    XRapidButton _xRapidButton = new XRapidButton(host, 0.5, 0, "icons/add_16.png", addSuperTypeAction);
    XRapidButton _xRapidButton_1 = new XRapidButton(host, 0.5, 1, "icons/add_16.png", addSuperTypeAction);
    XRapidButton _xRapidButton_2 = new XRapidButton(host, 0, 0.5, "icons/add_16.png", addReferencesAction);
    XRapidButton _xRapidButton_3 = new XRapidButton(host, 1, 0.5, "icons/add_16.png", addReferencesAction);
    final List<XRapidButton> buttons = Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1, _xRapidButton_2, _xRapidButton_3));
    XAbstractDiagram _diagram = Extensions.getDiagram(host);
    ObservableList<XRapidButton> _buttons = _diagram.getButtons();
    Iterables.<XRapidButton>addAll(_buttons, buttons);
  }
}
