package de.fxdiagram.lib.shapes;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.behavior.AbstractBehavior;
import de.fxdiagram.core.tools.chooser.CarusselChooser;
import de.fxdiagram.core.tools.chooser.CoverFlowChooser;
import de.fxdiagram.lib.shapes.JavaTypeNode;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    final Procedure1<XRapidButton> _function = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          XNode _host = JavaTypeRapidButtonBehavior.this.getHost();
          Pos _chooserPosition = button.getChooserPosition();
          CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(_host, _chooserPosition);
          final CoverFlowChooser chooser = _coverFlowChooser;
          XNode _host_1 = JavaTypeRapidButtonBehavior.this.getHost();
          final Class<? extends Object> javaType = ((JavaTypeNode) _host_1).getJavaType();
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
          XNode _host_2 = JavaTypeRapidButtonBehavior.this.getHost();
          XRootDiagram _rootDiagram = Extensions.getRootDiagram(_host_2);
          _rootDiagram.setCurrentTool(chooser);
        }
      };
    final Procedure1<XRapidButton> addSuperTypeAction = _function;
    final Procedure1<XRapidButton> _function_1 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          XNode _host = JavaTypeRapidButtonBehavior.this.getHost();
          Pos _chooserPosition = button.getChooserPosition();
          CarusselChooser _carusselChooser = new CarusselChooser(_host, _chooserPosition);
          final CarusselChooser chooser = _carusselChooser;
          XNode _host_1 = JavaTypeRapidButtonBehavior.this.getHost();
          final Class<? extends Object> javaType = ((JavaTypeNode) _host_1).getJavaType();
          Field[] _declaredFields = javaType.getDeclaredFields();
          final Function1<Field,Boolean> _function = new Function1<Field,Boolean>() {
              public Boolean apply(final Field it) {
                boolean _and = false;
                Class<? extends Object> _type = it.getType();
                boolean _isPrimitive = _type.isPrimitive();
                boolean _not = (!_isPrimitive);
                if (!_not) {
                  _and = false;
                } else {
                  Class<? extends Object> _type_1 = it.getType();
                  boolean _equals = String.class.equals(_type_1);
                  boolean _not_1 = (!_equals);
                  _and = (_not && _not_1);
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
          XNode _host_2 = JavaTypeRapidButtonBehavior.this.getHost();
          XRootDiagram _rootDiagram = Extensions.getRootDiagram(_host_2);
          _rootDiagram.setCurrentTool(chooser);
        }
      };
    final Procedure1<XRapidButton> addReferencesAction = _function_1;
    XNode _host = this.getHost();
    XRapidButton _xRapidButton = new XRapidButton(_host, 0.5, 0, "icons/add_16.png", addSuperTypeAction);
    XNode _host_1 = this.getHost();
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 0.5, 1, "icons/add_16.png", addSuperTypeAction);
    XNode _host_2 = this.getHost();
    XRapidButton _xRapidButton_2 = new XRapidButton(_host_2, 0, 0.5, "icons/add_16.png", addReferencesAction);
    XNode _host_3 = this.getHost();
    XRapidButton _xRapidButton_3 = new XRapidButton(_host_3, 1, 0.5, "icons/add_16.png", addReferencesAction);
    final List<XRapidButton> buttons = Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1, _xRapidButton_2, _xRapidButton_3));
    final Procedure1<XRapidButton> _function_2 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton it) {
          XNode _host = it.getHost();
          XAbstractDiagram _diagram = Extensions.getDiagram(_host);
          _diagram.addButton(it);
        }
      };
    IterableExtensions.<XRapidButton>forEach(buttons, _function_2);
  }
}
