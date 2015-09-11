package de.fxdiagram.mapping.shapes;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.lib.nodes.AbstractClassNode;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.mapping.shapes.INodeWithLazyMappings;
import java.util.Collections;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public abstract class BaseClassNode<T extends Object> extends AbstractClassNode implements INodeWithLazyMappings {
  public BaseClassNode(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
  }
  
  @Override
  public IMappedElementDescriptor<T> getDomainObjectDescriptor() {
    DomainObjectDescriptor _domainObjectDescriptor = super.getDomainObjectDescriptor();
    return ((IMappedElementDescriptor<T>) _domainObjectDescriptor);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    IMappedElementDescriptor<T> _domainObjectDescriptor = this.getDomainObjectDescriptor();
    LazyConnectionMappingBehavior.<T>addLazyBehavior(this, _domainObjectDescriptor);
  }
  
  @Override
  public void registerOnClick() {
    final EventHandler<MouseEvent> _function = (MouseEvent it) -> {
      MouseButton _button = it.getButton();
      boolean _equals = Objects.equal(_button, MouseButton.SECONDARY);
      if (_equals) {
        boolean _and = false;
        Node _front = this.getFront();
        boolean _notEquals = (!Objects.equal(_front, null));
        if (!_notEquals) {
          _and = false;
        } else {
          Node _back = this.getBack();
          boolean _notEquals_1 = (!Objects.equal(_back, null));
          _and = _notEquals_1;
        }
        if (_and) {
          boolean _isHorizontal = this.isHorizontal(it);
          this.flip(_isHorizontal);
          it.consume();
        }
      }
    };
    this.<MouseEvent>addEventHandler(MouseEvent.MOUSE_CLICKED, _function);
  }
  
  @Override
  public List<Side> getButtonSides(final ConnectionMapping<?> mapping) {
    return Collections.<Side>unmodifiableList(CollectionLiterals.<Side>newArrayList(Side.TOP, Side.BOTTOM, Side.LEFT, Side.RIGHT));
  }
}
