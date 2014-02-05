package de.fxdiagram.examples.ecore;

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
import de.fxdiagram.examples.ecore.EClassNode;
import de.fxdiagram.examples.ecore.ESuperType;
import de.fxdiagram.examples.ecore.ESuperTypeHandle;
import de.fxdiagram.examples.ecore.EcoreDomainObjectProvider;
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.util.Collections;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddESuperTypeRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<EClassNode,EClass,ESuperTypeHandle> {
  public AddESuperTypeRapidButtonBehavior(final EClassNode host) {
    super(host);
  }
  
  protected Iterable<EClass> getInitialModelChoices() {
    EClassNode _host = this.getHost();
    EClass _eClass = _host.getEClass();
    EList<EClass> _eSuperTypes = _eClass.getESuperTypes();
    return _eSuperTypes;
  }
  
  protected ESuperTypeHandle getChoiceKey(final EClass superType) {
    EcoreDomainObjectProvider _domainObjectProvider = this.getDomainObjectProvider();
    EClassNode _host = this.getHost();
    EClass _eClass = _host.getEClass();
    ESuperType _eSuperType = new ESuperType(_eClass, superType);
    ESuperTypeHandle _createESuperClassHandle = _domainObjectProvider.createESuperClassHandle(_eSuperType);
    return _createESuperClassHandle;
  }
  
  protected XNode createNode(final ESuperTypeHandle key) {
    EClassNode _eClassNode = new EClassNode();
    final Procedure1<EClassNode> _function = new Procedure1<EClassNode>() {
      public void apply(final EClassNode it) {
        EcoreDomainObjectProvider _domainObjectProvider = AddESuperTypeRapidButtonBehavior.this.getDomainObjectProvider();
        ESuperType _domainObject = key.getDomainObject();
        EClass _superType = _domainObject.getSuperType();
        DomainObjectHandle _createDomainObjectHandle = _domainObjectProvider.createDomainObjectHandle(_superType);
        it.setDomainObject(_createDomainObjectHandle);
      }
    };
    EClassNode _doubleArrow = ObjectExtensions.<EClassNode>operator_doubleArrow(_eClassNode, _function);
    return _doubleArrow;
  }
  
  protected EcoreDomainObjectProvider getDomainObjectProvider() {
    EClassNode _host = this.getHost();
    XRoot _root = CoreExtensions.getRoot(_host);
    EcoreDomainObjectProvider _domainObjectProvider = _root.<EcoreDomainObjectProvider>getDomainObjectProvider(EcoreDomainObjectProvider.class);
    return _domainObjectProvider;
  }
  
  protected AbstractChooser createChooser(final XRapidButton button, final Set<ESuperTypeHandle> availableChoiceKeys, final Set<ESuperTypeHandle> unavailableChoiceKeys) {
    CoverFlowChooser _xblockexpression = null;
    {
      EClassNode _host = this.getHost();
      Pos _chooserPosition = button.getChooserPosition();
      CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(_host, _chooserPosition);
      final CoverFlowChooser chooser = _coverFlowChooser;
      final Procedure1<ESuperTypeHandle> _function = new Procedure1<ESuperTypeHandle>() {
        public void apply(final ESuperTypeHandle it) {
          XNode _createNode = AddESuperTypeRapidButtonBehavior.this.createNode(it);
          chooser.addChoice(_createNode, it);
        }
      };
      IterableExtensions.<ESuperTypeHandle>forEach(availableChoiceKeys, _function);
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
    EClassNode _host = this.getHost();
    SVGPath _triangleButton = ButtonExtensions.getTriangleButton(Side.TOP, "Discover supertypes");
    XRapidButton _xRapidButton = new XRapidButton(_host, 0.5, 0, _triangleButton, addConnectionAction);
    EClassNode _host_1 = this.getHost();
    SVGPath _triangleButton_1 = ButtonExtensions.getTriangleButton(Side.BOTTOM, "Discover supertypes");
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 0.5, 1, _triangleButton_1, addConnectionAction);
    return Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1));
  }
}
