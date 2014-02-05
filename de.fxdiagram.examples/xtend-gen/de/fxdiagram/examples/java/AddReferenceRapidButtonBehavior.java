package de.fxdiagram.examples.java;

import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.tools.AbstractChooser;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.java.JavaModelProvider;
import de.fxdiagram.examples.java.JavaProperty;
import de.fxdiagram.examples.java.JavaPropertyHandle;
import de.fxdiagram.examples.java.JavaTypeHandle;
import de.fxdiagram.examples.java.JavaTypeModel;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior;
import de.fxdiagram.lib.tools.CarusselChooser;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddReferenceRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<JavaTypeNode,JavaProperty,JavaPropertyHandle> {
  public AddReferenceRapidButtonBehavior(final JavaTypeNode host) {
    super(host);
  }
  
  protected Iterable<JavaProperty> getInitialModelChoices() {
    JavaTypeNode _host = this.getHost();
    JavaTypeModel _javaTypeModel = _host.getJavaTypeModel();
    List<JavaProperty> _references = _javaTypeModel.getReferences();
    return _references;
  }
  
  protected JavaPropertyHandle getChoiceKey(final JavaProperty property) {
    JavaModelProvider _domainObjectProvider = this.getDomainObjectProvider();
    JavaPropertyHandle _createJavaPropertyHandle = _domainObjectProvider.createJavaPropertyHandle(property);
    return _createJavaPropertyHandle;
  }
  
  protected XNode createNode(final JavaPropertyHandle key) {
    JavaTypeNode _javaTypeNode = new JavaTypeNode();
    final Procedure1<JavaTypeNode> _function = new Procedure1<JavaTypeNode>() {
      public void apply(final JavaTypeNode it) {
        JavaModelProvider _domainObjectProvider = AddReferenceRapidButtonBehavior.this.getDomainObjectProvider();
        JavaProperty _domainObject = key.getDomainObject();
        Class<? extends Object> _type = _domainObject.getType();
        JavaTypeHandle _createJavaTypeHandle = _domainObjectProvider.createJavaTypeHandle(_type);
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
  
  protected AbstractChooser createChooser(final XRapidButton button, final Set<JavaPropertyHandle> availableChoiceKeys, final Set<JavaPropertyHandle> unavailableChoiceKeys) {
    CarusselChooser _xblockexpression = null;
    {
      JavaTypeNode _host = this.getHost();
      Pos _chooserPosition = button.getChooserPosition();
      CarusselChooser _carusselChooser = new CarusselChooser(_host, _chooserPosition);
      final CarusselChooser chooser = _carusselChooser;
      final Procedure1<JavaPropertyHandle> _function = new Procedure1<JavaPropertyHandle>() {
        public void apply(final JavaPropertyHandle it) {
          XNode _createNode = AddReferenceRapidButtonBehavior.this.createNode(it);
          chooser.addChoice(_createNode, it);
        }
      };
      IterableExtensions.<JavaPropertyHandle>forEach(availableChoiceKeys, _function);
      final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
        public XConnection getConnection(final XNode host, final XNode choice, final DomainObjectHandle choiceInfo) {
          XConnection _xblockexpression = null;
          {
            final JavaPropertyHandle reference = ((JavaPropertyHandle) choiceInfo);
            XConnection _xConnection = new XConnection(host, choice, reference);
            final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
              public void apply(final XConnection it) {
                LineArrowHead _lineArrowHead = new LineArrowHead(it, false);
                it.setTargetArrowHead(_lineArrowHead);
                XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
                final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
                  public void apply(final XConnectionLabel it) {
                    Text _text = it.getText();
                    JavaProperty _domainObject = reference.getDomainObject();
                    String _name = _domainObject.getName();
                    _text.setText(_name);
                  }
                };
                ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
              }
            };
            XConnection _doubleArrow = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
            _xblockexpression = (_doubleArrow);
          }
          return _xblockexpression;
        }
      };
      chooser.setConnectionProvider(_function_1);
      _xblockexpression = (chooser);
    }
    return _xblockexpression;
  }
  
  protected Iterable<XRapidButton> createButtons(final Procedure1<? super XRapidButton> addConnectionAction) {
    JavaTypeNode _host = this.getHost();
    SVGPath _arrowButton = ButtonExtensions.getArrowButton(Side.LEFT, "Discover properties");
    XRapidButton _xRapidButton = new XRapidButton(_host, 0, 0.5, _arrowButton, addConnectionAction);
    JavaTypeNode _host_1 = this.getHost();
    SVGPath _arrowButton_1 = ButtonExtensions.getArrowButton(Side.RIGHT, "Discover properties");
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 1, 0.5, _arrowButton_1, addConnectionAction);
    return Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1));
  }
}
