package de.fxdiagram.xtext.glue.shapes;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.lib.nodes.FlipNode;
import de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

@SuppressWarnings("all")
public class BaseFlipNode<T extends Object> extends FlipNode {
  public BaseFlipNode() {
    ReadOnlyObjectProperty<DomainObjectDescriptor> _domainObjectProperty = this.domainObjectProperty();
    final ChangeListener<DomainObjectDescriptor> _function = new ChangeListener<DomainObjectDescriptor>() {
      public void changed(final ObservableValue<? extends DomainObjectDescriptor> prop, final DomainObjectDescriptor oldVal, final DomainObjectDescriptor newVal) {
        BaseFlipNode.this.injectMembers();
      }
    };
    _domainObjectProperty.addListener(_function);
  }
  
  public BaseFlipNode(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
    this.injectMembers();
  }
  
  protected void injectMembers() {
    final IMappedElementDescriptor<T> domainObject = this.getDomainObject();
    if ((domainObject instanceof AbstractXtextDescriptor<?>)) {
      ((AbstractXtextDescriptor<?>)domainObject).injectMembers(this);
    }
  }
  
  public IMappedElementDescriptor<T> getDomainObject() {
    DomainObjectDescriptor _domainObject = super.getDomainObject();
    return ((IMappedElementDescriptor<T>) _domainObject);
  }
  
  public void doActivate() {
    super.doActivate();
    IMappedElementDescriptor<T> _domainObject = this.getDomainObject();
    LazyConnectionMappingBehavior.<T>addLazyBehavior(this, _domainObject);
    OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(this);
    this.addBehavior(_openElementInEditorBehavior);
  }
  
  public void registerOnClick() {
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        MouseButton _button = it.getButton();
        boolean _equals = Objects.equal(_button, MouseButton.SECONDARY);
        if (_equals) {
          boolean _and = false;
          Node _front = BaseFlipNode.this.getFront();
          boolean _notEquals = (!Objects.equal(_front, null));
          if (!_notEquals) {
            _and = false;
          } else {
            Node _back = BaseFlipNode.this.getBack();
            boolean _notEquals_1 = (!Objects.equal(_back, null));
            _and = _notEquals_1;
          }
          if (_and) {
            boolean _isHorizontal = BaseFlipNode.this.isHorizontal(it);
            BaseFlipNode.this.flip(_isHorizontal);
          }
        }
      }
    };
    this.setOnMouseClicked(_function);
  }
}
