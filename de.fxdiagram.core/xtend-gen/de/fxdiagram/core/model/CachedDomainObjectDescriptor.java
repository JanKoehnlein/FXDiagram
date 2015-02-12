package de.fxdiagram.core.model;

import de.fxdiagram.core.model.DomainObjectDescriptorImpl;
import de.fxdiagram.core.model.DomainObjectProvider;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

/**
 * Base class for {@link DomainObjectDescriptor}s whose domain object is constant and can
 * be cached.
 */
@SuppressWarnings("all")
public abstract class CachedDomainObjectDescriptor<T extends Object> extends DomainObjectDescriptorImpl<T> {
  private T cachedDomainObject;
  
  public CachedDomainObjectDescriptor() {
  }
  
  public CachedDomainObjectDescriptor(final T domainObject, final String id, final String name, final DomainObjectProvider provider) {
    super(id, name, provider);
    this.cachedDomainObject = domainObject;
  }
  
  public T getDomainObject() {
    T _elvis = null;
    if (this.cachedDomainObject != null) {
      _elvis = this.cachedDomainObject;
    } else {
      T _resolveDomainObject = this.resolveDomainObject();
      _elvis = (this.cachedDomainObject = _resolveDomainObject);
    }
    return _elvis;
  }
  
  @Override
  public <U extends Object> U withDomainObject(final Function1<? super T, ? extends U> lambda) {
    T _domainObject = this.getDomainObject();
    return lambda.apply(_domainObject);
  }
  
  public abstract T resolveDomainObject();
}
