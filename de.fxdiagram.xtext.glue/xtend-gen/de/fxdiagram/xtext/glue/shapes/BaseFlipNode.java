package de.fxdiagram.xtext.glue.shapes;

import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.lib.nodes.FlipNode;
import de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

@SuppressWarnings("all")
public class BaseFlipNode<T extends Object> extends FlipNode {
  public BaseFlipNode() {
    ReadOnlyObjectProperty<DomainObjectDescriptor> _domainObjectProperty = this.domainObjectProperty();
    final ChangeListener<DomainObjectDescriptor> _function = new ChangeListener<DomainObjectDescriptor>() {
      public void changed(final ObservableValue<? extends DomainObjectDescriptor> prop, final DomainObjectDescriptor oldVal, final DomainObjectDescriptor newVal) {
        if ((newVal instanceof AbstractXtextDescriptor<?>)) {
          ((AbstractXtextDescriptor<?>)newVal).injectMembers(BaseFlipNode.this);
        }
      }
    };
    _domainObjectProperty.addListener(_function);
  }
  
  public BaseFlipNode(final AbstractXtextDescriptor<T> descriptor) {
    super(descriptor);
    descriptor.injectMembers(this);
  }
  
  protected AbstractXtextDescriptor<T> getDescriptor() {
    DomainObjectDescriptor _domainObject = this.getDomainObject();
    return ((AbstractXtextDescriptor<T>) _domainObject);
  }
  
  public void doActivate() {
    super.doActivate();
    AbstractXtextDescriptor<T> _descriptor = this.getDescriptor();
    LazyConnectionMappingBehavior.<T>addLazyBehavior(this, _descriptor);
    OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(this);
    this.addBehavior(_openElementInEditorBehavior);
  }
}
