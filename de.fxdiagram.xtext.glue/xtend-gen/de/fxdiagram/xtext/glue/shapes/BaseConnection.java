package de.fxdiagram.xtext.glue.shapes;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

@ModelNode
@SuppressWarnings("all")
public class BaseConnection<T extends Object> extends XConnection {
  public BaseConnection() {
    ReadOnlyObjectProperty<DomainObjectDescriptor> _domainObjectProperty = this.domainObjectProperty();
    final ChangeListener<DomainObjectDescriptor> _function = new ChangeListener<DomainObjectDescriptor>() {
      public void changed(final ObservableValue<? extends DomainObjectDescriptor> prop, final DomainObjectDescriptor oldVal, final DomainObjectDescriptor newVal) {
        if ((newVal instanceof AbstractXtextDescriptor<?>)) {
          ((AbstractXtextDescriptor<?>)newVal).injectMembers(BaseConnection.this);
        }
      }
    };
    _domainObjectProperty.addListener(_function);
  }
  
  public BaseConnection(final AbstractXtextDescriptor<T> descriptor) {
    super(descriptor);
    descriptor.injectMembers(this);
  }
  
  protected AbstractXtextDescriptor<T> getDescriptor() {
    DomainObjectDescriptor _domainObject = this.getDomainObject();
    return ((AbstractXtextDescriptor<T>) _domainObject);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
