package de.fxdiagram.mapping.reconcile;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.ReconcileBehavior;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.mapping.AbstractLabelMappingCall;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.reconcile.AbstractLabelOwnerReconcileBehavior;
import de.fxdiagram.mapping.reconcile.ConnectionLabelMorphCommand;
import de.fxdiagram.mapping.reconcile.ReconnectMorphCommand;
import java.util.NoSuchElementException;
import java.util.Set;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ConnectionReconcileBehavior<T extends Object> extends AbstractLabelOwnerReconcileBehavior<T, XConnection> {
  public ConnectionReconcileBehavior(final XConnection host) {
    super(host);
  }
  
  @Override
  public DirtyState getDirtyState() {
    DomainObjectDescriptor _domainObjectDescriptor = this.getHost().getDomainObjectDescriptor();
    if ((_domainObjectDescriptor instanceof IMappedElementDescriptor<?>)) {
      try {
        DomainObjectDescriptor _domainObjectDescriptor_1 = this.getHost().getDomainObjectDescriptor();
        final IMappedElementDescriptor<T> descriptor = ((IMappedElementDescriptor<T>) _domainObjectDescriptor_1);
        final Function1<T, DirtyState> _function = (T domainObject) -> {
          AbstractMapping<T> _mapping = descriptor.getMapping();
          final ConnectionMapping<T> connectionMapping = ((ConnectionMapping<T>) _mapping);
          final DomainObjectDescriptor resolvedSourceDescriptor = this.<Object>resolveConnectionEnd(((T) domainObject), connectionMapping, this.getHost().getSource().getDomainObjectDescriptor(), true);
          boolean _equals = Objects.equal(resolvedSourceDescriptor, null);
          if (_equals) {
            return DirtyState.DANGLING;
          } else {
            DomainObjectDescriptor _domainObjectDescriptor_2 = this.getHost().getSource().getDomainObjectDescriptor();
            boolean _equals_1 = Objects.equal(resolvedSourceDescriptor, _domainObjectDescriptor_2);
            if (_equals_1) {
              final DomainObjectDescriptor resolvedTargetDescriptor = this.<Object>resolveConnectionEnd(((T) domainObject), connectionMapping, this.getHost().getTarget().getDomainObjectDescriptor(), false);
              boolean _equals_2 = Objects.equal(resolvedTargetDescriptor, null);
              if (_equals_2) {
                return DirtyState.DANGLING;
              } else {
                DomainObjectDescriptor _domainObjectDescriptor_3 = this.getHost().getTarget().getDomainObjectDescriptor();
                boolean _equals_3 = Objects.equal(resolvedTargetDescriptor, _domainObjectDescriptor_3);
                if (_equals_3) {
                  return DirtyState.CLEAN;
                }
              }
            }
          }
          return DirtyState.DIRTY;
        };
        return descriptor.<DirtyState>withDomainObject(_function);
      } catch (final Throwable _t) {
        if (_t instanceof NoSuchElementException) {
          final NoSuchElementException exc = (NoSuchElementException)_t;
          return DirtyState.DANGLING;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    } else {
      return DirtyState.CLEAN;
    }
  }
  
  protected <U extends Object> DomainObjectDescriptor resolveConnectionEnd(final T domainObject, final ConnectionMapping<T> connectionMapping, final DomainObjectDescriptor nodeDescriptor, final boolean isSource) {
    NodeMappingCall<?, T> _xifexpression = null;
    if (isSource) {
      _xifexpression = connectionMapping.getSource();
    } else {
      _xifexpression = connectionMapping.getTarget();
    }
    final NodeMappingCall<U, T> nodeMappingCall = ((NodeMappingCall<U, T>) _xifexpression);
    boolean _notEquals = (!Objects.equal(nodeMappingCall, null));
    if (_notEquals) {
      U _head = IterableExtensions.<U>head(this.getInterpreter().<U, T>select(nodeMappingCall, domainObject));
      final U nodeObject = ((U) _head);
      boolean _equals = Objects.equal(nodeObject, null);
      if (_equals) {
        return null;
      }
      final IMappedElementDescriptor<U> resolvedNodeDescriptor = this.getInterpreter().<U>getDescriptor(nodeObject, nodeMappingCall.getMapping());
      boolean _notEquals_1 = (!Objects.equal(resolvedNodeDescriptor, nodeDescriptor));
      if (_notEquals_1) {
        return resolvedNodeDescriptor;
      } else {
        return nodeDescriptor;
      }
    } else {
      if ((nodeDescriptor instanceof IMappedElementDescriptor<?>)) {
        AbstractMapping<?> _mapping = ((IMappedElementDescriptor<?>)nodeDescriptor).getMapping();
        if ((_mapping instanceof NodeMapping<?>)) {
          AbstractMapping<?> _mapping_1 = ((IMappedElementDescriptor<?>)nodeDescriptor).getMapping();
          final NodeMapping<U> nodeMappingCasted = ((NodeMapping<U>) _mapping_1);
          Iterable<AbstractConnectionMappingCall<T, U>> _xifexpression_1 = null;
          if (isSource) {
            final Function1<AbstractConnectionMappingCall<?, U>, Boolean> _function = (AbstractConnectionMappingCall<?, U> it) -> {
              AbstractMapping<?> _mapping_2 = it.getMapping();
              return Boolean.valueOf(Objects.equal(_mapping_2, connectionMapping));
            };
            final Function1<AbstractConnectionMappingCall<?, U>, AbstractConnectionMappingCall<T, U>> _function_1 = (AbstractConnectionMappingCall<?, U> it) -> {
              return ((AbstractConnectionMappingCall<T, U>) it);
            };
            _xifexpression_1 = IterableExtensions.<AbstractConnectionMappingCall<?, U>, AbstractConnectionMappingCall<T, U>>map(IterableExtensions.<AbstractConnectionMappingCall<?, U>>filter(nodeMappingCasted.getOutgoing(), _function), _function_1);
          } else {
            final Function1<AbstractConnectionMappingCall<?, U>, Boolean> _function_2 = (AbstractConnectionMappingCall<?, U> it) -> {
              AbstractMapping<?> _mapping_2 = it.getMapping();
              return Boolean.valueOf(Objects.equal(_mapping_2, connectionMapping));
            };
            final Function1<AbstractConnectionMappingCall<?, U>, AbstractConnectionMappingCall<T, U>> _function_3 = (AbstractConnectionMappingCall<?, U> it) -> {
              return ((AbstractConnectionMappingCall<T, U>) it);
            };
            _xifexpression_1 = IterableExtensions.<AbstractConnectionMappingCall<?, U>, AbstractConnectionMappingCall<T, U>>map(IterableExtensions.<AbstractConnectionMappingCall<?, U>>filter(nodeMappingCasted.getIncoming(), _function_2), _function_3);
          }
          final Iterable<AbstractConnectionMappingCall<T, U>> siblingMappingCalls = _xifexpression_1;
          final Function1<Object, IMappedElementDescriptor<?>> _function_4 = (Object nodeDomainObject) -> {
            final U nodeObjectCasted = ((U) nodeDomainObject);
            for (final AbstractConnectionMappingCall<T, U> siblingMappingCall : siblingMappingCalls) {
              {
                final Function1<T, IMappedElementDescriptor<T>> _function_5 = (T it) -> {
                  return this.getInterpreter().<T>getDescriptor(it, siblingMappingCall.getMapping());
                };
                final Set<IMappedElementDescriptor<T>> siblingDescriptors = IterableExtensions.<IMappedElementDescriptor<T>>toSet(IterableExtensions.<T, IMappedElementDescriptor<T>>map(this.getInterpreter().<T, U>select(siblingMappingCall, nodeObjectCasted), _function_5));
                boolean _contains = siblingDescriptors.contains(this.getHost().getDomainObjectDescriptor());
                if (_contains) {
                  return ((IMappedElementDescriptor<?>)nodeDescriptor);
                }
              }
            }
            return null;
          };
          return ((IMappedElementDescriptor<?>)nodeDescriptor).<IMappedElementDescriptor<?>>withDomainObject(_function_4);
        }
      }
    }
    return null;
  }
  
  @Override
  protected void reconcile(final AbstractMapping<T> mapping, final T domainObject, final ReconcileBehavior.UpdateAcceptor acceptor) {
    final ConnectionMapping<T> connectionMapping = ((ConnectionMapping<T>) mapping);
    final DomainObjectDescriptor resolvedSourceDescriptor = this.<Object>resolveConnectionEnd(domainObject, connectionMapping, this.getHost().getSource().getDomainObjectDescriptor(), true);
    DomainObjectDescriptor _domainObjectDescriptor = this.getHost().getSource().getDomainObjectDescriptor();
    boolean _notEquals = (!Objects.equal(resolvedSourceDescriptor, _domainObjectDescriptor));
    if (_notEquals) {
      final XNode newSource = this.findNode(resolvedSourceDescriptor);
      boolean _notEquals_1 = (!Objects.equal(newSource, null));
      if (_notEquals_1) {
        XConnection _host = this.getHost();
        XNode _source = this.getHost().getSource();
        ReconnectMorphCommand _reconnectMorphCommand = new ReconnectMorphCommand(_host, _source, newSource, true);
        acceptor.morph(_reconnectMorphCommand);
      } else {
        acceptor.delete(this.getHost(), CoreExtensions.getDiagram(this.getHost()));
      }
    } else {
      final DomainObjectDescriptor resolvedTarget = this.<Object>resolveConnectionEnd(domainObject, connectionMapping, this.getHost().getTarget().getDomainObjectDescriptor(), false);
      DomainObjectDescriptor _domainObjectDescriptor_1 = this.getHost().getTarget().getDomainObjectDescriptor();
      boolean _notEquals_2 = (!Objects.equal(resolvedTarget, _domainObjectDescriptor_1));
      if (_notEquals_2) {
        final XNode newTarget = this.findNode(resolvedTarget);
        boolean _notEquals_3 = (!Objects.equal(newTarget, null));
        if (_notEquals_3) {
          XConnection _host_1 = this.getHost();
          XNode _target = this.getHost().getTarget();
          ReconnectMorphCommand _reconnectMorphCommand_1 = new ReconnectMorphCommand(_host_1, _target, newTarget, false);
          acceptor.morph(_reconnectMorphCommand_1);
        } else {
          acceptor.delete(this.getHost(), CoreExtensions.getDiagram(this.getHost()));
        }
      }
    }
    XConnection _host_2 = this.getHost();
    final ConnectionLabelMorphCommand labelMorphCommand = new ConnectionLabelMorphCommand(_host_2);
    this.compareLabels(connectionMapping, domainObject, labelMorphCommand);
    boolean _isEmpty = labelMorphCommand.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      acceptor.morph(labelMorphCommand);
    }
  }
  
  protected XNode findNode(final DomainObjectDescriptor descriptor) {
    final Function1<XNode, Boolean> _function = (XNode it) -> {
      DomainObjectDescriptor _domainObjectDescriptor = it.getDomainObjectDescriptor();
      return Boolean.valueOf(Objects.equal(_domainObjectDescriptor, descriptor));
    };
    return IterableExtensions.<XNode>findFirst(Iterables.<XNode>filter(CoreExtensions.getAllChildren(CoreExtensions.getDiagram(this.getHost())), XNode.class), _function);
  }
  
  @Override
  protected Iterable<? extends XLabel> getExistingLabels() {
    return this.getHost().getLabels();
  }
  
  @Override
  protected Iterable<? extends AbstractLabelMappingCall<?, T>> getLabelMappingCalls(final AbstractMapping<T> mapping) {
    return ((ConnectionMapping<T>) mapping).getLabels();
  }
}
