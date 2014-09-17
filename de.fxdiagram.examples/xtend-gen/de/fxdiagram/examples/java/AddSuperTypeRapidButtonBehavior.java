package de.fxdiagram.examples.java;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.tools.AbstractChooser;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.java.JavaModelProvider;
import de.fxdiagram.examples.java.JavaSuperTypeDescriptor;
import de.fxdiagram.examples.java.JavaSuperTypeHandle;
import de.fxdiagram.examples.java.JavaTypeDescriptor;
import de.fxdiagram.examples.java.JavaTypeModel;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import javafx.geometry.Side;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddSuperTypeRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<JavaTypeNode, Class<?>, JavaSuperTypeDescriptor> {
  public AddSuperTypeRapidButtonBehavior(final JavaTypeNode host) {
    super(host);
  }
  
  protected Iterable<Class<?>> getInitialModelChoices() {
    JavaTypeNode _host = this.getHost();
    JavaTypeModel _javaTypeModel = _host.getJavaTypeModel();
    return _javaTypeModel.getSuperTypes();
  }
  
  protected JavaSuperTypeDescriptor getChoiceKey(final Class<?> superType) {
    JavaModelProvider _domainObjectProvider = this.getDomainObjectProvider();
    JavaTypeNode _host = this.getHost();
    Class<?> _javaType = _host.getJavaType();
    JavaSuperTypeHandle _javaSuperTypeHandle = new JavaSuperTypeHandle(_javaType, superType);
    return _domainObjectProvider.createJavaSuperClassDescriptor(_javaSuperTypeHandle);
  }
  
  protected XNode createNode(final JavaSuperTypeDescriptor key) {
    JavaModelProvider _domainObjectProvider = this.getDomainObjectProvider();
    JavaSuperTypeHandle _domainObject = key.getDomainObject();
    Class<?> _superType = _domainObject.getSuperType();
    JavaTypeDescriptor _createJavaTypeDescriptor = _domainObjectProvider.createJavaTypeDescriptor(_superType);
    return new JavaTypeNode(_createJavaTypeDescriptor);
  }
  
  protected JavaModelProvider getDomainObjectProvider() {
    JavaTypeNode _host = this.getHost();
    XRoot _root = CoreExtensions.getRoot(_host);
    return _root.<JavaModelProvider>getDomainObjectProvider(JavaModelProvider.class);
  }
  
  protected AbstractChooser createChooser(final RapidButton button, final Set<JavaSuperTypeDescriptor> availableChoiceKeys, final Set<JavaSuperTypeDescriptor> unavailableChoiceKeys) {
    CoverFlowChooser _xblockexpression = null;
    {
      JavaTypeNode _host = this.getHost();
      Side _position = button.getPosition();
      final CoverFlowChooser chooser = new CoverFlowChooser(_host, _position);
      final Consumer<JavaSuperTypeDescriptor> _function = new Consumer<JavaSuperTypeDescriptor>() {
        public void accept(final JavaSuperTypeDescriptor it) {
          XNode _createNode = AddSuperTypeRapidButtonBehavior.this.createNode(it);
          chooser.addChoice(_createNode, it);
        }
      };
      availableChoiceKeys.forEach(_function);
      final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
        public XConnection getConnection(final XNode host, final XNode choice, final DomainObjectDescriptor key) {
          XConnection _xConnection = new XConnection(host, choice, key);
          final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
              XDiagram _diagram = CoreExtensions.getDiagram(host);
              Paint _backgroundPaint = _diagram.getBackgroundPaint();
              TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, 10, 15, 
                null, _backgroundPaint, false);
              it.setTargetArrowHead(_triangleArrowHead);
            }
          };
          return ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
        }
      };
      chooser.setConnectionProvider(_function_1);
      _xblockexpression = chooser;
    }
    return _xblockexpression;
  }
  
  protected Iterable<RapidButton> createButtons(final RapidButtonAction addConnectionAction) {
    JavaTypeNode _host = this.getHost();
    SVGPath _triangleButton = ButtonExtensions.getTriangleButton(Side.TOP, "Discover supertypes");
    RapidButton _rapidButton = new RapidButton(_host, Side.TOP, _triangleButton, addConnectionAction);
    JavaTypeNode _host_1 = this.getHost();
    SVGPath _triangleButton_1 = ButtonExtensions.getTriangleButton(Side.BOTTOM, "Discover supertypes");
    RapidButton _rapidButton_1 = new RapidButton(_host_1, Side.BOTTOM, _triangleButton_1, addConnectionAction);
    return Collections.<RapidButton>unmodifiableList(CollectionLiterals.<RapidButton>newArrayList(_rapidButton, _rapidButton_1));
  }
}
