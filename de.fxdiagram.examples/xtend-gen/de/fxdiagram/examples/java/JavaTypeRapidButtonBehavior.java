package de.fxdiagram.examples.java;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.behavior.AbstractBehavior;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.examples.java.JavaTypeModel;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.examples.java.Property;
import de.fxdiagram.lib.tools.CarusselChooser;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
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
          final Function2<XNode,XNode,XConnection> _function_1 = new Function2<XNode,XNode,XConnection>() {
            public XConnection apply(final XNode host, final XNode choice) {
              XConnection _xConnection = new XConnection(host, choice);
              final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                public void apply(final XConnection it) {
                  ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
                  XDiagram _diagram = CoreExtensions.getDiagram(host);
                  ObjectProperty<Paint> _backgroundPaintProperty = _diagram.backgroundPaintProperty();
                  TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, 10, 15, _strokeProperty, _backgroundPaintProperty, false);
                  it.setTargetArrowHead(_triangleArrowHead);
                }
              };
              XConnection _doubleArrow = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
              return _doubleArrow;
            }
          };
          chooser.setConnectionFactory(_function_1);
          JavaTypeNode _host_1 = JavaTypeRapidButtonBehavior.this.getHost();
          XRoot _root = CoreExtensions.getRoot(_host_1);
          _root.setCurrentTool(chooser);
        }
      };
      final Procedure1<XRapidButton> addSuperTypeAction = _function;
      JavaTypeNode _host_1 = this.getHost();
      XDiagram _diagram = CoreExtensions.getDiagram(_host_1);
      ObservableList<XRapidButton> _buttons = _diagram.getButtons();
      JavaTypeNode _host_2 = this.getHost();
      SVGPath _superImage = this.getSuperImage(true);
      XRapidButton _xRapidButton = new XRapidButton(_host_2, 0.5, 0, _superImage, addSuperTypeAction);
      JavaTypeNode _host_3 = this.getHost();
      SVGPath _superImage_1 = this.getSuperImage(false);
      XRapidButton _xRapidButton_1 = new XRapidButton(_host_3, 0.5, 1, _superImage_1, addSuperTypeAction);
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
                  String _name = reference.getName();
                  it.setReferenceName(_name);
                }
              };
              JavaTypeNode _doubleArrow = ObjectExtensions.<JavaTypeNode>operator_doubleArrow(_javaTypeNode, _function);
              return _doubleArrow;
            }
          };
          List<JavaTypeNode> _map = ListExtensions.<Property, JavaTypeNode>map(_references, _function);
          chooser.operator_add(_map);
          final Function2<XNode,XNode,XConnection> _function_1 = new Function2<XNode,XNode,XConnection>() {
            public XConnection apply(final XNode host, final XNode choice) {
              XConnection _xConnection = new XConnection(host, choice);
              final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                public void apply(final XConnection it) {
                  ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
                  LineArrowHead _lineArrowHead = new LineArrowHead(it, 7, 10, _strokeProperty, false);
                  it.setTargetArrowHead(_lineArrowHead);
                  XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
                  final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
                    public void apply(final XConnectionLabel it) {
                      Text _text = it.getText();
                      String _referenceName = ((JavaTypeNode) choice).getReferenceName();
                      _text.setText(_referenceName);
                    }
                  };
                  ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
                }
              };
              XConnection _doubleArrow = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
              return _doubleArrow;
            }
          };
          chooser.setConnectionFactory(_function_1);
          JavaTypeNode _host_1 = JavaTypeRapidButtonBehavior.this.getHost();
          XRoot _root = CoreExtensions.getRoot(_host_1);
          _root.setCurrentTool(chooser);
        }
      };
      final Procedure1<XRapidButton> addReferencesAction = _function_1;
      JavaTypeNode _host_4 = this.getHost();
      XDiagram _diagram_1 = CoreExtensions.getDiagram(_host_4);
      ObservableList<XRapidButton> _buttons_1 = _diagram_1.getButtons();
      JavaTypeNode _host_5 = this.getHost();
      SVGPath _referenceImage = this.getReferenceImage(true);
      XRapidButton _xRapidButton_2 = new XRapidButton(_host_5, 0, 0.5, _referenceImage, addReferencesAction);
      JavaTypeNode _host_6 = this.getHost();
      SVGPath _referenceImage_1 = this.getReferenceImage(false);
      XRapidButton _xRapidButton_3 = new XRapidButton(_host_6, 1, 0.5, _referenceImage_1, addReferencesAction);
      Iterables.<XRapidButton>addAll(_buttons_1, Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton_2, _xRapidButton_3)));
    }
  }
  
  protected SVGPath getSuperImage(final boolean isUp) {
    SVGPath _sVGPath = new SVGPath();
    final Procedure1<SVGPath> _function = new Procedure1<SVGPath>() {
      public void apply(final SVGPath it) {
        String _xifexpression = null;
        if (isUp) {
          _xifexpression = "m 0,8 7,-8 7,8 z";
        } else {
          _xifexpression = "m 0,0 7,8 7,-8 z";
        }
        it.setContent(_xifexpression);
        it.setFill(Color.WHITE);
        it.setStroke(Color.BLACK);
        it.setStrokeWidth(1.8);
        Tooltip _tooltip = new Tooltip("Discover supertypes");
        Tooltip.install(it, _tooltip);
      }
    };
    SVGPath _doubleArrow = ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function);
    return _doubleArrow;
  }
  
  protected SVGPath getReferenceImage(final boolean isLeft) {
    SVGPath _sVGPath = new SVGPath();
    final Procedure1<SVGPath> _function = new Procedure1<SVGPath>() {
      public void apply(final SVGPath it) {
        String _xifexpression = null;
        if (isLeft) {
          _xifexpression = "m 7,0 -7,6 7,6";
        } else {
          _xifexpression = "m 0,0 7,6 -7,6";
        }
        it.setContent(_xifexpression);
        it.setFill(Color.WHITE);
        it.setStroke(Color.BLACK);
        it.setStrokeWidth(2);
        it.setStrokeLineCap(StrokeLineCap.ROUND);
        Tooltip _tooltip = new Tooltip("Discover properties");
        Tooltip.install(it, _tooltip);
      }
    };
    SVGPath _doubleArrow = ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function);
    return _doubleArrow;
  }
}
