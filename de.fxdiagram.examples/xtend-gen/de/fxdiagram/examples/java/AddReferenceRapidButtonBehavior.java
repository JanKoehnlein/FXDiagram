package de.fxdiagram.examples.java;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
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
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior;
import de.fxdiagram.lib.tools.CarusselChooser;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import javafx.geometry.Side;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
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
  
  protected AbstractChooser createChooser(final RapidButton button, final Set<JavaPropertyDescriptor> availableChoiceKeys, final Set<JavaPropertyDescriptor> unavailableChoiceKeys) {
    CarusselChooser _xblockexpression = null;
    {
      JavaTypeNode _host = this.getHost();
      Side _position = button.getPosition();
      final CarusselChooser chooser = new CarusselChooser(_host, _position);
      final Consumer<JavaPropertyDescriptor> _function = new Consumer<JavaPropertyDescriptor>() {
        public void accept(final JavaPropertyDescriptor it) {
          XNode _createNode = AddReferenceRapidButtonBehavior.this.createNode(it);
          chooser.addChoice(_createNode, it);
        }
      };
      availableChoiceKeys.forEach(_function);
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
  
  protected Iterable<RapidButton> createButtons(final RapidButtonAction addConnectionAction) {
    JavaTypeNode _host = this.getHost();
    SVGPath _arrowButton = ButtonExtensions.getArrowButton(Side.LEFT, "Discover properties");
    RapidButton _rapidButton = new RapidButton(_host, Side.LEFT, _arrowButton, addConnectionAction);
    JavaTypeNode _host_1 = this.getHost();
    SVGPath _arrowButton_1 = ButtonExtensions.getArrowButton(Side.RIGHT, "Discover properties");
    RapidButton _rapidButton_1 = new RapidButton(_host_1, Side.RIGHT, _arrowButton_1, addConnectionAction);
    return Collections.<RapidButton>unmodifiableList(CollectionLiterals.<RapidButton>newArrayList(_rapidButton, _rapidButton_1));
  }
}
