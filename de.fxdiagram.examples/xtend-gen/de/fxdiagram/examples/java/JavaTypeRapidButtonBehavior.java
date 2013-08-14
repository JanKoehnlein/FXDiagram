package de.fxdiagram.examples.java;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.AbstractBehavior;
import de.fxdiagram.examples.java.JavaTypeModel;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.examples.java.Property;
import de.fxdiagram.lib.tools.CarusselChooser;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class JavaTypeRapidButtonBehavior extends AbstractBehavior<JavaTypeNode> {
  public JavaTypeRapidButtonBehavior(final JavaTypeNode host) {
    super(host);
  }
  
  protected void doActivate() {
    JavaTypeNode _host = this.getHost();
    final JavaTypeModel model = _host.getJavaTypeModel();
    List<Class<? extends Object>> _superTypes = model.getSuperTypes();
    boolean _isEmpty = _superTypes.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      final Procedure1<XRapidButton> _function = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          JavaTypeNode _host = JavaTypeRapidButtonBehavior.this.getHost();
          Pos _chooserPosition = button.getChooserPosition();
          CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(_host, _chooserPosition);
          final CoverFlowChooser chooser = _coverFlowChooser;
          List<Class<? extends Object>> _superTypes = model.getSuperTypes();
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
          List<JavaTypeNode> _map = ListExtensions.<Class<? extends Object>, JavaTypeNode>map(_superTypes, _function);
          chooser.operator_add(_map);
          JavaTypeNode _host_1 = JavaTypeRapidButtonBehavior.this.getHost();
          XRoot _root = Extensions.getRoot(_host_1);
          _root.setCurrentTool(chooser);
        }
      };
      final Procedure1<XRapidButton> addSuperTypeAction = _function;
      JavaTypeNode _host_1 = this.getHost();
      XAbstractDiagram _diagram = Extensions.getDiagram(_host_1);
      ObservableList<XRapidButton> _buttons = _diagram.getButtons();
      JavaTypeNode _host_2 = this.getHost();
      XRapidButton _xRapidButton = new XRapidButton(_host_2, 0.5, 0, "icons/SuperType.gif", addSuperTypeAction);
      JavaTypeNode _host_3 = this.getHost();
      XRapidButton _xRapidButton_1 = new XRapidButton(_host_3, 0.5, 1, "icons/SuperType.gif", addSuperTypeAction);
      Iterables.<XRapidButton>addAll(_buttons, Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1)));
    }
    List<Property> _references = model.getReferences();
    boolean _isEmpty_1 = _references.isEmpty();
    boolean _not_1 = (!_isEmpty_1);
    if (_not_1) {
      final Procedure1<XRapidButton> _function_1 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          JavaTypeNode _host = JavaTypeRapidButtonBehavior.this.getHost();
          Pos _chooserPosition = button.getChooserPosition();
          CarusselChooser _carusselChooser = new CarusselChooser(_host, _chooserPosition);
          final CarusselChooser chooser = _carusselChooser;
          List<Property> _references = model.getReferences();
          final Function1<Property,JavaTypeNode> _function = new Function1<Property,JavaTypeNode>() {
            public JavaTypeNode apply(final Property reference) {
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
          List<JavaTypeNode> _map = ListExtensions.<Property, JavaTypeNode>map(_references, _function);
          chooser.operator_add(_map);
          JavaTypeNode _host_1 = JavaTypeRapidButtonBehavior.this.getHost();
          XRoot _root = Extensions.getRoot(_host_1);
          _root.setCurrentTool(chooser);
        }
      };
      final Procedure1<XRapidButton> addReferencesAction = _function_1;
      JavaTypeNode _host_4 = this.getHost();
      XAbstractDiagram _diagram_1 = Extensions.getDiagram(_host_4);
      ObservableList<XRapidButton> _buttons_1 = _diagram_1.getButtons();
      JavaTypeNode _host_5 = this.getHost();
      XRapidButton _xRapidButton_2 = new XRapidButton(_host_5, 0, 0.5, "icons/Reference.gif", addReferencesAction);
      JavaTypeNode _host_6 = this.getHost();
      XRapidButton _xRapidButton_3 = new XRapidButton(_host_6, 1, 0.5, "icons/Reference.gif", addReferencesAction);
      Iterables.<XRapidButton>addAll(_buttons_1, Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton_2, _xRapidButton_3)));
    }
  }
}
