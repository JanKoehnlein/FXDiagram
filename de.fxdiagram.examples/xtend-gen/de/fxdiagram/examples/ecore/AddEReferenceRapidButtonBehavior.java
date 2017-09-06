package de.fxdiagram.examples.ecore;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.anchors.DiamondArrowHead;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.examples.ecore.EClassDescriptor;
import de.fxdiagram.examples.ecore.EClassNode;
import de.fxdiagram.examples.ecore.EReferenceDescriptor;
import de.fxdiagram.examples.ecore.EcoreDomainObjectProvider;
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
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddEReferenceRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<EClassNode, EReference, EReferenceDescriptor> {
  public AddEReferenceRapidButtonBehavior(final EClassNode host) {
    super(host);
  }
  
  @Override
  protected Iterable<EReference> getInitialModelChoices() {
    return this.getHost().getEClass().getEReferences();
  }
  
  @Override
  protected EReferenceDescriptor getChoiceKey(final EReference model) {
    return this.getDomainObjectProvider().createEReferenceDescriptor(model);
  }
  
  @Override
  protected XNode createNode(final EReferenceDescriptor handle) {
    EClassDescriptor _createEClassDescriptor = this.getDomainObjectProvider().createEClassDescriptor(handle.getDomainObject().getEReferenceType());
    return new EClassNode(_createEClassDescriptor);
  }
  
  protected EcoreDomainObjectProvider getDomainObjectProvider() {
    return CoreExtensions.getRoot(this.getHost()).<EcoreDomainObjectProvider>getDomainObjectProvider(EcoreDomainObjectProvider.class);
  }
  
  @Override
  protected ConnectedNodeChooser createChooser(final RapidButton button, final Set<EReferenceDescriptor> availableChoiceKeys, final Set<EReferenceDescriptor> unavailableChoiceKeys) {
    ConnectedNodeChooser _xblockexpression = null;
    {
      EClassNode _host = this.getHost();
      Side _position = button.getPosition();
      CarusselChoice _carusselChoice = new CarusselChoice();
      final ConnectedNodeChooser chooser = new ConnectedNodeChooser(_host, _position, _carusselChoice);
      final Consumer<EReferenceDescriptor> _function = (EReferenceDescriptor it) -> {
        chooser.addChoice(this.createNode(it), it);
      };
      availableChoiceKeys.forEach(_function);
      final ChooserConnectionProvider _function_1 = (XNode host, XNode choice, DomainObjectDescriptor descriptor) -> {
        XConnection _xblockexpression_1 = null;
        {
          final EReference reference = ((EReferenceDescriptor) descriptor).getDomainObject();
          XConnection _xConnection = new XConnection(host, choice, descriptor);
          final Procedure1<XConnection> _function_2 = (XConnection it) -> {
            ArrowHead _xifexpression = null;
            boolean _isContainer = reference.isContainer();
            if (_isContainer) {
              _xifexpression = new DiamondArrowHead(it, false);
            } else {
              _xifexpression = new LineArrowHead(it, false);
            }
            it.setTargetArrowHead(_xifexpression);
            ArrowHead _xifexpression_1 = null;
            boolean _isContainment = reference.isContainment();
            if (_isContainment) {
              _xifexpression_1 = new DiamondArrowHead(it, true);
            } else {
              LineArrowHead _xifexpression_2 = null;
              if (((!reference.isContainer()) && (reference.getEOpposite() != null))) {
                _xifexpression_2 = new LineArrowHead(it, true);
              }
              _xifexpression_1 = _xifexpression_2;
            }
            it.setSourceArrowHead(_xifexpression_1);
            XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
            final Procedure1<XConnectionLabel> _function_3 = (XConnectionLabel it_1) -> {
              Text _text = it_1.getText();
              _text.setText(reference.getName());
              it_1.setPosition(0.8);
            };
            ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function_3);
            EReference _eOpposite = reference.getEOpposite();
            boolean _tripleNotEquals = (_eOpposite != null);
            if (_tripleNotEquals) {
              XConnectionLabel _xConnectionLabel_1 = new XConnectionLabel(it);
              final Procedure1<XConnectionLabel> _function_4 = (XConnectionLabel it_1) -> {
                Text _text = it_1.getText();
                _text.setText(reference.getEOpposite().getName());
                it_1.setPosition(0.2);
              };
              ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel_1, _function_4);
            }
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
    EClassNode _host = this.getHost();
    SVGPath _arrowButton = ButtonExtensions.getArrowButton(Side.LEFT, "Discover references");
    RapidButton _rapidButton = new RapidButton(_host, Side.LEFT, _arrowButton, addConnectionAction);
    EClassNode _host_1 = this.getHost();
    SVGPath _arrowButton_1 = ButtonExtensions.getArrowButton(Side.RIGHT, "Discover references");
    RapidButton _rapidButton_1 = new RapidButton(_host_1, Side.RIGHT, _arrowButton_1, addConnectionAction);
    return Collections.<RapidButton>unmodifiableList(CollectionLiterals.<RapidButton>newArrayList(_rapidButton, _rapidButton_1));
  }
}
