package de.fxdiagram.examples.java;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

import com.google.common.collect.Lists;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.AbstractChooser;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior;
import de.fxdiagram.lib.tools.CoverFlowChooser;

@SuppressWarnings("all")
public class AddSuperTypeRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<JavaTypeNode,Class<? extends Object>,SuperTypeKey> {
  public AddSuperTypeRapidButtonBehavior(final JavaTypeNode host) {
    super(host);
  }
  
  protected Iterable<Class<? extends Object>> getInitialModelChoices() {
    JavaTypeNode _host = this.getHost();
    JavaTypeModel _javaTypeModel = _host.getJavaTypeModel();
    List<Class<? extends Object>> _superTypes = _javaTypeModel.getSuperTypes();
    return _superTypes;
  }
  
  protected SuperTypeKey getChoiceKey(final Class<? extends Object> superType) {
    JavaTypeNode _host = this.getHost();
    Class<? extends Object> _javaType = ((JavaTypeNode) _host).getJavaType();
    SuperTypeKey _superTypeKey = new SuperTypeKey(_javaType, superType);
    return _superTypeKey;
  }
  
  protected XNode createNode(final SuperTypeKey key) {
    Class<? extends Object> _superType = key.getSuperType();
    JavaTypeNode _javaTypeNode = new JavaTypeNode(_superType);
    return _javaTypeNode;
  }
  
  protected AbstractChooser createChooser(final XRapidButton button, final Set<SuperTypeKey> availableChoiceKeys, final Set<SuperTypeKey> unavailableChoiceKeys) {
    CoverFlowChooser _xblockexpression = null;
    {
      JavaTypeNode _host = this.getHost();
      Pos _chooserPosition = button.getChooserPosition();
      CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(_host, _chooserPosition);
      final CoverFlowChooser chooser = _coverFlowChooser;
      final Procedure1<SuperTypeKey> _function = new Procedure1<SuperTypeKey>() {
        public void apply(final SuperTypeKey it) {
          XNode _createNode = AddSuperTypeRapidButtonBehavior.this.createNode(it);
          chooser.addChoice(_createNode, it);
        }
      };
      IterableExtensions.<SuperTypeKey>forEach(availableChoiceKeys, _function);
      final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
        public XConnection getConnection(final XNode host, final XNode choice, final Object key) {
          XConnection _xConnection = new XConnection(host, choice, key);
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
      chooser.setConnectionProvider(_function_1);
      _xblockexpression = (chooser);
    }
    return _xblockexpression;
  }
  
  protected Iterable<XRapidButton> createButtons(final Procedure1<? super XRapidButton> addConnectionAction) {
    JavaTypeNode _host = this.getHost();
    SVGPath _triangleButton = ButtonExtensions.getTriangleButton(Side.TOP, "Discover supertypes");
    XRapidButton _xRapidButton = new XRapidButton(_host, 0.5, 0, _triangleButton, addConnectionAction);
    JavaTypeNode _host_1 = this.getHost();
    SVGPath _triangleButton_1 = ButtonExtensions.getTriangleButton(Side.BOTTOM, "Discover supertypes");
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 0.5, 1, _triangleButton_1, addConnectionAction);
    return Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1));
  }
}
