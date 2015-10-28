package de.fxdiagram.lib.buttons;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XButton;
import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Exceptions;

/**
 * A button that pops up when the mouse hovers over its {@link #host}.
 * 
 * @see RapidButtonBehavior
 */
@Logging
@SuppressWarnings("all")
public class RapidButton extends Parent implements XButton {
  private XNode host;
  
  private RapidButtonAction action;
  
  private Side position;
  
  public RapidButton(final XNode host, final Side position, final Node image, final RapidButtonAction action) {
    this.host = host;
    this.action = action;
    this.position = position;
    ObservableList<Node> _children = this.getChildren();
    _children.add(image);
  }
  
  @Override
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      try {
        this.doActivate();
        this.isActiveProperty.set(true);
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception exc = (Exception)_t;
          String _message = exc.getMessage();
          RapidButton.LOG.severe(_message);
          exc.printStackTrace();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
  }
  
  public void doActivate() {
    final EventHandler<MouseEvent> _function = (MouseEvent it) -> {
      this.action.perform(this);
      it.consume();
    };
    this.setOnMousePressed(_function);
  }
  
  public Side getPosition() {
    return this.position;
  }
  
  public XNode getHost() {
    return this.host;
  }
  
  public RapidButtonAction getAction() {
    return this.action;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.lib.buttons.RapidButton");
    ;
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
}
