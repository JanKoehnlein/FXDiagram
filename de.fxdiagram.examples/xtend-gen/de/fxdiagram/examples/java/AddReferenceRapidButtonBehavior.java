package de.fxdiagram.examples.java;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.examples.java.JavaModelProvider;
import de.fxdiagram.examples.java.JavaProperty;
import de.fxdiagram.examples.java.JavaPropertyDescriptor;
import de.fxdiagram.examples.java.JavaTypeDescriptor;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import de.fxdiagram.lib.chooser.CarusselChoice;
import de.fxdiagram.lib.chooser.ChooserConnectionProvider;
import de.fxdiagram.lib.chooser.ConnectedNodeChooser;
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior;
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
  
  @Override
  protected Iterable<JavaProperty> getInitialModelChoices() {
    return this.getHost().getJavaTypeModel().getReferences();
  }
  
  @Override
  protected JavaPropertyDescriptor getChoiceKey(final JavaProperty property) {
    return this.getDomainObjectProvider().createJavaPropertyDescriptor(property);
  }
  
  @Override
  protected XNode createNode(final JavaPropertyDescriptor key) {
    JavaTypeDescriptor _createJavaTypeDescriptor = this.getDomainObjectProvider().createJavaTypeDescriptor(key.getDomainObject().getType());
    return new JavaTypeNode(_createJavaTypeDescriptor);
  }
  
  protected JavaModelProvider getDomainObjectProvider() {
    return CoreExtensions.getRoot(this.getHost()).<JavaModelProvider>getDomainObjectProvider(JavaModelProvider.class);
  }
  
  @Override
  protected ConnectedNodeChooser createChooser(final RapidButton button, final Set<JavaPropertyDescriptor> availableChoiceKeys, final Set<JavaPropertyDescriptor> unavailableChoiceKeys) {
    ConnectedNodeChooser _xblockexpression = null;
    {
      JavaTypeNode _host = this.getHost();
      Side _position = button.getPosition();
      CarusselChoice _carusselChoice = new CarusselChoice();
      final ConnectedNodeChooser chooser = new ConnectedNodeChooser(_host, _position, _carusselChoice);
      final Consumer<JavaPropertyDescriptor> _function = (JavaPropertyDescriptor it) -> {
        chooser.addChoice(this.createNode(it), it);
      };
      availableChoiceKeys.forEach(_function);
      final ChooserConnectionProvider _function_1 = (XNode host, XNode choice, DomainObjectDescriptor choiceInfo) -> {
        XConnection _xblockexpression_1 = null;
        {
          final JavaPropertyDescriptor reference = ((JavaPropertyDescriptor) choiceInfo);
          XConnection _xConnection = new XConnection(host, choice, reference);
          final Procedure1<XConnection> _function_2 = (XConnection it) -> {
            LineArrowHead _lineArrowHead = new LineArrowHead(it, false);
            it.setTargetArrowHead(_lineArrowHead);
            XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
            final Procedure1<XConnectionLabel> _function_3 = (XConnectionLabel it_1) -> {
              Text _text = it_1.getText();
              _text.setText(reference.getDomainObject().getName());
            };
            ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function_3);
          };
          _xblockexpression_1 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function_2);
        }
        return _xblockexpression_1;
      };
      chooser.setConnectionProvider(_function_1);
      _xblockexpression = chooser;
    }
    return _xblockexpression;
  }
  
  @Override
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
