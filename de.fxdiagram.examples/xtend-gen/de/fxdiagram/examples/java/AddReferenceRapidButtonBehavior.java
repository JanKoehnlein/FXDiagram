package de.fxdiagram.examples.java;

import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRapidButtonAction;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.tools.AbstractChooser;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.java.JavaModelProvider;
import de.fxdiagram.examples.java.JavaProperty;
import de.fxdiagram.examples.java.JavaPropertyDescriptor;
import de.fxdiagram.examples.java.JavaTypeDescriptor;
import de.fxdiagram.examples.java.JavaTypeModel;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior;
import de.fxdiagram.lib.tools.CarusselChooser;
import java.util.Collections;
import java.util.Set;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddReferenceRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<JavaTypeNode, JavaProperty, JavaPropertyDescriptor> {
  public AddReferenceRapidButtonBehavior(final JavaTypeNode host) {
    super(host);
  }
  
  protected Iterable<JavaProperty> getInitialModelChoices() {
    JavaTypeNode _host = this.getHost();
    JavaTypeModel _javaTypeModel = _host.getJavaTypeModel();
    return _javaTypeModel.getReferences();
  }
  
  protected JavaPropertyDescriptor getChoiceKey(final JavaProperty property) {
    JavaModelProvider _domainObjectProvider = this.getDomainObjectProvider();
    return _domainObjectProvider.createJavaPropertyDescriptor(property);
  }
  
  protected XNode createNode(final JavaPropertyDescriptor key) {
    JavaModelProvider _domainObjectProvider = this.getDomainObjectProvider();
    JavaProperty _domainObject = key.getDomainObject();
    Class<?> _type = _domainObject.getType();
    JavaTypeDescriptor _createJavaTypeDescriptor = _domainObjectProvider.createJavaTypeDescriptor(_type);
    return new JavaTypeNode(_createJavaTypeDescriptor);
  }
  
  protected JavaModelProvider getDomainObjectProvider() {
    JavaTypeNode _host = this.getHost();
    XRoot _root = CoreExtensions.getRoot(_host);
    return _root.<JavaModelProvider>getDomainObjectProvider(JavaModelProvider.class);
  }
  
  protected AbstractChooser createChooser(final XRapidButton button, final Set<JavaPropertyDescriptor> availableChoiceKeys, final Set<JavaPropertyDescriptor> unavailableChoiceKeys) {
    CarusselChooser _xblockexpression = null;
    {
      JavaTypeNode _host = this.getHost();
      Pos _chooserPosition = button.getChooserPosition();
      final CarusselChooser chooser = new CarusselChooser(_host, _chooserPosition);
      final Procedure1<JavaPropertyDescriptor> _function = new Procedure1<JavaPropertyDescriptor>() {
        public void apply(final JavaPropertyDescriptor it) {
          XNode _createNode = AddReferenceRapidButtonBehavior.this.createNode(it);
          chooser.addChoice(_createNode, it);
        }
      };
      IterableExtensions.<JavaPropertyDescriptor>forEach(availableChoiceKeys, _function);
      final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
        public XConnection getConnection(final XNode host, final XNode choice, final DomainObjectDescriptor choiceInfo) {
          XConnection _xblockexpression = null;
          {
            final JavaPropertyDescriptor reference = ((JavaPropertyDescriptor) choiceInfo);
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
            _xblockexpression = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
          }
          return _xblockexpression;
        }
      };
      chooser.setConnectionProvider(_function_1);
      _xblockexpression = chooser;
    }
    return _xblockexpression;
  }
  
  protected Iterable<XRapidButton> createButtons(final XRapidButtonAction addConnectionAction) {
    JavaTypeNode _host = this.getHost();
    SVGPath _arrowButton = ButtonExtensions.getArrowButton(Side.LEFT, "Discover properties");
    XRapidButton _xRapidButton = new XRapidButton(_host, 0, 0.5, _arrowButton, addConnectionAction);
    JavaTypeNode _host_1 = this.getHost();
    SVGPath _arrowButton_1 = ButtonExtensions.getArrowButton(Side.RIGHT, "Discover properties");
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 1, 0.5, _arrowButton_1, addConnectionAction);
    return Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1));
  }
}
