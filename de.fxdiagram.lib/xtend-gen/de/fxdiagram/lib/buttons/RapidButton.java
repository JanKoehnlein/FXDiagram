package de.fxdiagram.lib.buttons;

import de.fxdiagram.core.XButton;
import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

/**
 * A button that pops up when the mouse hovers over its {@link #host}.
 * 
 * @see RapidButtonBehavior
 */
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
      this.doActivate();
    }
    this.isActiveProperty.set(true);
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
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
}
