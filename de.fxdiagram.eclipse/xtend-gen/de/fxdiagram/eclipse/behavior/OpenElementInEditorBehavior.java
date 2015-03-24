package de.fxdiagram.eclipse.behavior;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.OpenBehavior;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Opens the domain object of this {@link XShape} in th respective Eclipse editor.
 */
@SuppressWarnings("all")
public class OpenElementInEditorBehavior extends AbstractHostBehavior<XShape> implements OpenBehavior {
  public OpenElementInEditorBehavior(final XShape host) {
    super(host);
  }
  
  @Override
  public Class<? extends Behavior> getBehaviorKey() {
    return OpenElementInEditorBehavior.class;
  }
  
  @Override
  protected void doActivate() {
    XShape _host = this.getHost();
    final EventHandler<MouseEvent> _function = (MouseEvent it) -> {
      int _clickCount = it.getClickCount();
      boolean _equals = (_clickCount == 2);
      if (_equals) {
        this.open();
        it.consume();
      }
    };
    _host.<MouseEvent>addEventHandler(MouseEvent.MOUSE_CLICKED, _function);
  }
  
  @Override
  public void open() {
    IMappedElementDescriptor<?> _domainObject = this.getDomainObject();
    _domainObject.openInEditor(true);
  }
  
  private IMappedElementDescriptor<?> getDomainObject() {
    DomainObjectDescriptor _switchResult = null;
    XShape _host = this.getHost();
    final XShape it = _host;
    boolean _matched = false;
    if (!_matched) {
      if (it instanceof XNode) {
        _matched=true;
        _switchResult = ((XNode)it).getDomainObject();
      }
    }
    if (!_matched) {
      if (it instanceof XConnection) {
        _matched=true;
        _switchResult = ((XConnection)it).getDomainObject();
      }
    }
    if (!_matched) {
      _switchResult = null;
    }
    return ((IMappedElementDescriptor<?>) _switchResult);
  }
}
