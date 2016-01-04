package de.fxdiagram.core;

import de.fxdiagram.core.model.DomainObjectDescriptor;
import javafx.beans.property.ReadOnlyObjectProperty;

@SuppressWarnings("all")
public interface XDomainObjectOwner {
  public abstract DomainObjectDescriptor getDomainObjectDescriptor();
  
  public abstract ReadOnlyObjectProperty<DomainObjectDescriptor> domainObjectDescriptorProperty();
}
