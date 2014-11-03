package de.fxdiagram.xtext.glue.shapes;

import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.lib.nodes.FlipNode;
import de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
    LazyConnectionMappingBehavior.<Object>addLazyBehavior(this, _domainObject);
    OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(this);
    this.addBehavior(_openElementInEditorBehavior);
  }
}
