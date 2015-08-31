package de.fxdiagram.mapping.behavior;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.behavior.AbstractDirtyStateBehavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import java.util.NoSuchElementException;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class NodeDirtyStateBehavior extends AbstractDirtyStateBehavior<XNode> {
  public NodeDirtyStateBehavior(final XNode host) {
    super(host);
  }
  
  @Override
  public DirtyState getDirtyState() {
    XNode _host = this.getHost();
    final DomainObjectDescriptor descriptor = _host.getDomainObject();
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
}
