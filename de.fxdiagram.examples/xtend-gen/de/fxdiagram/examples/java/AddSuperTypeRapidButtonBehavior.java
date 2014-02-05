package de.fxdiagram.examples.java;

import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.tools.AbstractChooser;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.java.JavaModelProvider;
import de.fxdiagram.examples.java.JavaSuperType;
import de.fxdiagram.examples.java.JavaSuperTypeHandle;
import de.fxdiagram.examples.java.JavaTypeHandle;
import de.fxdiagram.examples.java.JavaTypeModel;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior;
import de.fxdiagram.lib.tools.CoverFlowChooser;
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

@SuppressWarnings("all")
public class AddSuperTypeRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<JavaTypeNode,Class<? extends Object>,JavaSuperTypeHandle> {
  public AddSuperTypeRapidButtonBehavior(final JavaTypeNode host) {
    super(host);
  }
  
  protected Iterable<Class<? extends Object>> getInitialModelChoices() {
    JavaTypeNode _host = this.getHost();
    JavaTypeModel _javaTypeModel = _host.getJavaTypeModel();
    List<Class<? extends Object>> _superTypes = _javaTypeModel.getSuperTypes();
    return _superTypes;
  }
  
  protected JavaSuperTypeHandle getChoiceKey(final Class<? extends Object> superType) {
    JavaModelProvider _domainObjectProvider = this.getDomainObjectProvider();
    JavaTypeNode _host = this.getHost();
    Class<? extends Object> _javaType = _host.getJavaType();
    JavaSuperType _javaSuperType = new JavaSuperType(_javaType, superType);
    JavaSuperTypeHandle _createJavaSuperClassHandle = _domainObjectProvider.createJavaSuperClassHandle(_javaSuperType);
    return _createJavaSuperClassHandle;
  }
  
  protected XNode createNode(final JavaSuperTypeHandle key) {
    JavaTypeNode _javaTypeNode = new JavaTypeNode();
    final Procedure1<JavaTypeNode> _function = new Procedure1<JavaTypeNode>() {
      public void apply(final JavaTypeNode it) {
        JavaModelProvider _domainObjectProvider = AddSuperTypeRapidButtonBehavior.this.getDomainObjectProvider();
        JavaSuperType _domainObject = key.getDomainObject();
        Class<? extends Object> _superType = _domainObject.getSuperType();
        JavaTypeHandle _createJavaTypeHandle = _domainObjectProvider.createJavaTypeHandle(_superType);
        it.setDomainObject(_createJavaTypeHandle);
      }
    };
    JavaTypeNode _doubleArrow = ObjectExtensions.<JavaTypeNode>operator_doubleArrow(_javaTypeNode, _function);
    return _doubleArrow;
  }
  
  protected JavaModelProvider getDomainObjectProvider() {
    JavaTypeNode _host = this.getHost();
    XRoot _root = CoreExtensions.getRoot(_host);
    JavaModelProvider _domainObjectProvider = _root.<JavaModelProvider>getDomainObjectProvider(JavaModelProvider.class);
    return _domainObjectProvider;
  }
  
  protected AbstractChooser createChooser(final XRapidButton button, final Set<JavaSuperTypeHandle> availableChoiceKeys, final Set<JavaSuperTypeHandle> unavailableChoiceKeys) {
    CoverFlowChooser _xblockexpression = null;
    {
      JavaTypeNode _host = this.getHost();
      Pos _chooserPosition = button.getChooserPosition();
      CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(_host, _chooserPosition);
      final CoverFlowChooser chooser = _coverFlowChooser;
      final Procedure1<JavaSuperTypeHandle> _function = new Procedure1<JavaSuperTypeHandle>() {
        public void apply(final JavaSuperTypeHandle it) {
          XNode _createNode = AddSuperTypeRapidButtonBehavior.this.createNode(it);
          chooser.addChoice(_createNode, it);
        }
      };
      IterableExtensions.<JavaSuperTypeHandle>forEach(availableChoiceKeys, _function);
      final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
        public XConnection getConnection(final XNode host, final XNode choice, final DomainObjectHandle key) {
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
