package de.fxdiagram.core.model;

import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.model.XModelProvider;

@SuppressWarnings("all")
public interface DomainObjectProvider extends XModelProvider {
  public abstract Object resolveDomainObject(final DomainObjectHandle handle);
  
  public abstract DomainObjectHandle createDomainObjectHandle(final Object object);
}
