package de.fxdiagram.xtext.glue.shapes;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;

@SuppressWarnings("all")
public class BaseConnection<T extends Object> extends XConnection {
  public BaseConnection(final XtextDomainObjectDescriptor<T> descriptor) {
    super(descriptor);
  }
  
  protected XtextDomainObjectDescriptor<T> getDescriptor() {
    DomainObjectDescriptor _domainObject = this.getDomainObject();
    return ((XtextDomainObjectDescriptor<T>) _domainObject);
  }
}
