package de.fxdiagram.mapping.behavior;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.behavior.AbstractReconcileBehavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.UpdateAcceptor;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import java.util.NoSuchElementException;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class DefaultReconcileBehavior<T extends XDomainObjectShape> extends AbstractReconcileBehavior<T> {
  public DefaultReconcileBehavior(final T host) {
    super(host);
  }
  
  @Override
  public DirtyState getDirtyState() {
    T _host = this.getHost();
    final DomainObjectDescriptor descriptor = _host.getDomainObjectDescriptor();
    if ((descriptor instanceof IMappedElementDescriptor<?>)) {
      try {
        final Function1<Object, Object> _function = (Object it) -> {
          return null;
        };
        ((IMappedElementDescriptor<?>)descriptor).<Object>withDomainObject(_function);
      } catch (final Throwable _t) {
        if (_t instanceof NoSuchElementException) {
          final NoSuchElementException e = (NoSuchElementException)_t;
          return DirtyState.DANGLING;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    return DirtyState.CLEAN;
  }
  
  @Override
  public void reconcile(final UpdateAcceptor acceptor) {
    DirtyState _dirtyState = this.getDirtyState();
    boolean _equals = Objects.equal(_dirtyState, DirtyState.DANGLING);
    if (_equals) {
      T _host = this.getHost();
      acceptor.delete(_host);
    }
  }
}
