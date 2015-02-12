package de.fxdiagram.examples.ecore;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.examples.ecore.EClassDescriptor;
import de.fxdiagram.examples.ecore.EClassNode;
import de.fxdiagram.examples.ecore.ESuperTypeDescriptor;
import de.fxdiagram.examples.ecore.ESuperTypeHandle;
import de.fxdiagram.examples.ecore.EcoreDomainObjectProvider;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import de.fxdiagram.lib.chooser.ChooserConnectionProvider;
import de.fxdiagram.lib.chooser.ConnectedNodeChooser;
import de.fxdiagram.lib.chooser.CoverFlowChoice;
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import javafx.geometry.Side;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddESuperTypeRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<EClassNode, EClass, ESuperTypeDescriptor> {
  public AddESuperTypeRapidButtonBehavior(final EClassNode host) {
    super(host);
  }
  
  @Override
  protected Iterable<EClass> getInitialModelChoices() {
    EClassNode _host = this.getHost();
    EClass _eClass = _host.getEClass();
    return _eClass.getESuperTypes();
  }
  
  @Override
  protected ESuperTypeDescriptor getChoiceKey(final EClass superType) {
    EcoreDomainObjectProvider _domainObjectProvider = this.getDomainObjectProvider();
    EClassNode _host = this.getHost();
    EClass _eClass = _host.getEClass();
    ESuperTypeHandle _eSuperTypeHandle = new ESuperTypeHandle(_eClass, superType);
    return _domainObjectProvider.createESuperClassDescriptor(_eSuperTypeHandle);
  }
  
  @Override
  protected XNode createNode(final ESuperTypeDescriptor key) {
    final Function1<ESuperTypeHandle, EClassDescriptor> _function = new Function1<ESuperTypeHandle, EClassDescriptor>() {
      @Override
      public EClassDescriptor apply(final ESuperTypeHandle it) {
        EcoreDomainObjectProvider _domainObjectProvider = AddESuperTypeRapidButtonBehavior.this.getDomainObjectProvider();
        EClass _superType = it.getSuperType();
        return _domainObjectProvider.createEClassDescriptor(_superType);
      }
    };
    EClassDescriptor _withDomainObject = key.<EClassDescriptor>withDomainObject(_function);
    return new EClassNode(_withDomainObject);
  }
  
  protected EcoreDomainObjectProvider getDomainObjectProvider() {
    EClassNode _host = this.getHost();
    XRoot _root = CoreExtensions.getRoot(_host);
    return _root.<EcoreDomainObjectProvider>getDomainObjectProvider(EcoreDomainObjectProvider.class);
  }
  
  @Override
  protected ConnectedNodeChooser createChooser(final RapidButton button, final Set<ESuperTypeDescriptor> availableChoiceKeys, final Set<ESuperTypeDescriptor> unavailableChoiceKeys) {
    ConnectedNodeChooser _xblockexpression = null;
    {
      EClassNode _host = this.getHost();
      Side _position = button.getPosition();
      CoverFlowChoice _coverFlowChoice = new CoverFlowChoice();
      final ConnectedNodeChooser chooser = new ConnectedNodeChooser(_host, _position, _coverFlowChoice);
      final Consumer<ESuperTypeDescriptor> _function = new Consumer<ESuperTypeDescriptor>() {
        @Override
        public void accept(final ESuperTypeDescriptor it) {
          XNode _createNode = AddESuperTypeRapidButtonBehavior.this.createNode(it);
          chooser.addChoice(_createNode, it);
        }
      };
      availableChoiceKeys.forEach(_function);
      final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
        @Override
        public XConnection getConnection(final XNode host, final XNode choice, final DomainObjectDescriptor descriptor) {
          XConnection _xConnection = new XConnection(host, choice, descriptor);
          final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
            @Override
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
  
  @Override
  protected Iterable<RapidButton> createButtons(final RapidButtonAction addConnectionAction) {
    EClassNode _host = this.getHost();
    SVGPath _triangleButton = ButtonExtensions.getTriangleButton(Side.TOP, "Discover supertypes");
    RapidButton _rapidButton = new RapidButton(_host, Side.TOP, _triangleButton, addConnectionAction);
    EClassNode _host_1 = this.getHost();
    SVGPath _triangleButton_1 = ButtonExtensions.getTriangleButton(Side.BOTTOM, "Discover supertypes");
    RapidButton _rapidButton_1 = new RapidButton(_host_1, Side.BOTTOM, _triangleButton_1, addConnectionAction);
    return Collections.<RapidButton>unmodifiableList(CollectionLiterals.<RapidButton>newArrayList(_rapidButton, _rapidButton_1));
  }
}
