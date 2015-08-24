package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.DomainObjectDescriptorImpl;
import de.fxdiagram.core.model.DomainObjectProvider;
import java.util.NoSuchElementException;
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
      T _xblockexpression = null;
      {
        T _resolveDomainObject = this.resolveDomainObject();
        this.cachedDomainObject = _resolveDomainObject;
        boolean _equals = Objects.equal(this.cachedDomainObject, null);
        if (_equals) {
          String _id = this.getId();
          String _plus = ("Element " + _id);
          String _plus_1 = (_plus + " not found");
          throw new NoSuchElementException(_plus_1);
        }
        _xblockexpression = this.cachedDomainObject;
      }
      _elvis = _xblockexpression;
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
