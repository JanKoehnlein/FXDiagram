package de.fxdiagram.xtext.glue.shapes;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor;
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
        BaseConnection.this.injectMembers();
      }
    };
    _domainObjectProperty.addListener(_function);
  }
  
  public BaseConnection(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
    this.injectMembers();
  }
  
  public DomainObjectDescriptor getDomainObject() {
    DomainObjectDescriptor _domainObject = super.getDomainObject();
    return ((IMappedElementDescriptor<T>) _domainObject);
  }
  
  protected void injectMembers() {
    final DomainObjectDescriptor descriptor = this.getDomainObject();
    if ((descriptor instanceof AbstractXtextDescriptor<?>)) {
      ((AbstractXtextDescriptor<?>)descriptor).injectMembers(this);
    }
  }
  
  public void doActivate() {
    super.doActivate();
    OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(this);
    this.addBehavior(_openElementInEditorBehavior);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
