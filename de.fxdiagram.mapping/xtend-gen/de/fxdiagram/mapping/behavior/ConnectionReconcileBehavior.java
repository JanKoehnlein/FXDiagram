package de.fxdiagram.mapping.behavior;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.UpdateAcceptor;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.mapping.AbstractLabelMappingCall;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.XDiagramConfigInterpreter;
import de.fxdiagram.mapping.behavior.AbstractLabelOwnerReconcileBehavior;
import de.fxdiagram.mapping.behavior.ConnectionLabelMorphCommand;
import de.fxdiagram.mapping.behavior.ReconnectMorphCommand;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javafx.scene.Node;
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
    XConnection _host = this.getHost();
    DomainObjectDescriptor _domainObjectDescriptor = _host.getDomainObjectDescriptor();
    if ((_domainObjectDescriptor instanceof IMappedElementDescriptor<?>)) {
      try {
        XConnection _host_1 = this.getHost();
        DomainObjectDescriptor _domainObjectDescriptor_1 = _host_1.getDomainObjectDescriptor();
        final IMappedElementDescriptor<T> descriptor = ((IMappedElementDescriptor<T>) _domainObjectDescriptor_1);
        final Function1<T, DirtyState> _function = (T domainObject) -> {
          AbstractMapping<T> _mapping = descriptor.getMapping();
          final ConnectionMapping<T> connectionMapping = ((ConnectionMapping<T>) _mapping);
          XConnection _host_2 = this.getHost();
          XNode _source = _host_2.getSource();
          DomainObjectDescriptor _domainObjectDescriptor_2 = _source.getDomainObjectDescriptor();
          final DomainObjectDescriptor resolvedSourceDescriptor = this.<Object>resolveConnectionEnd(((T) domainObject), connectionMapping, _domainObjectDescriptor_2, true);
          XConnection _host_3 = this.getHost();
          XNode _source_1 = _host_3.getSource();
          DomainObjectDescriptor _domainObjectDescriptor_3 = _source_1.getDomainObjectDescriptor();
          boolean _equals = Objects.equal(resolvedSourceDescriptor, _domainObjectDescriptor_3);
          if (_equals) {
            XConnection _host_4 = this.getHost();
            XNode _target = _host_4.getTarget();
            DomainObjectDescriptor _domainObjectDescriptor_4 = _target.getDomainObjectDescriptor();
            final DomainObjectDescriptor resolvedTarget = this.<Object>resolveConnectionEnd(((T) domainObject), connectionMapping, _domainObjectDescriptor_4, false);
            XConnection _host_5 = this.getHost();
            XNode _target_1 = _host_5.getTarget();
            DomainObjectDescriptor _domainObjectDescriptor_5 = _target_1.getDomainObjectDescriptor();
            boolean _equals_1 = Objects.equal(resolvedTarget, _domainObjectDescriptor_5);
            if (_equals_1) {
              return DirtyState.CLEAN;
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
      XDiagramConfigInterpreter _interpreter = this.getInterpreter();
      Iterable<U> _select = _interpreter.<U, T>select(nodeMappingCall, domainObject);
      U _head = IterableExtensions.<U>head(_select);
      final U nodeObject = ((U) _head);
      boolean _equals = Objects.equal(nodeObject, null);
      if (_equals) {
        return null;
      }
      XDiagramConfigInterpreter _interpreter_1 = this.getInterpreter();
      AbstractMapping<U> _mapping = nodeMappingCall.getMapping();
      final IMappedElementDescriptor<U> resolvedNodeDescriptor = _interpreter_1.<U>getDescriptor(nodeObject, _mapping);
      boolean _notEquals_1 = (!Objects.equal(resolvedNodeDescriptor, nodeDescriptor));
      if (_notEquals_1) {
        return resolvedNodeDescriptor;
      } else {
        return nodeDescriptor;
      }
    } else {
      if ((nodeDescriptor instanceof IMappedElementDescriptor<?>)) {
        AbstractMapping<?> _mapping_1 = ((IMappedElementDescriptor<?>)nodeDescriptor).getMapping();
        if ((_mapping_1 instanceof NodeMapping<?>)) {
          AbstractMapping<?> _mapping_2 = ((IMappedElementDescriptor<?>)nodeDescriptor).getMapping();
          final NodeMapping<U> nodeMappingCasted = ((NodeMapping<U>) _mapping_2);
          Iterable<AbstractConnectionMappingCall<T, U>> _xifexpression_1 = null;
          if (isSource) {
            List<AbstractConnectionMappingCall<?, U>> _outgoing = nodeMappingCasted.getOutgoing();
            final Function1<AbstractConnectionMappingCall<?, U>, Boolean> _function = (AbstractConnectionMappingCall<?, U> it) -> {
              AbstractMapping<?> _mapping_3 = it.getMapping();
              return Boolean.valueOf(Objects.equal(_mapping_3, connectionMapping));
            };
            Iterable<AbstractConnectionMappingCall<?, U>> _filter = IterableExtensions.<AbstractConnectionMappingCall<?, U>>filter(_outgoing, _function);
            final Function1<AbstractConnectionMappingCall<?, U>, AbstractConnectionMappingCall<T, U>> _function_1 = (AbstractConnectionMappingCall<?, U> it) -> {
              return ((AbstractConnectionMappingCall<T, U>) it);
            };
            _xifexpression_1 = IterableExtensions.<AbstractConnectionMappingCall<?, U>, AbstractConnectionMappingCall<T, U>>map(_filter, _function_1);
          } else {
            List<AbstractConnectionMappingCall<?, U>> _incoming = nodeMappingCasted.getIncoming();
            final Function1<AbstractConnectionMappingCall<?, U>, Boolean> _function_2 = (AbstractConnectionMappingCall<?, U> it) -> {
              AbstractMapping<?> _mapping_3 = it.getMapping();
              return Boolean.valueOf(Objects.equal(_mapping_3, connectionMapping));
            };
            Iterable<AbstractConnectionMappingCall<?, U>> _filter_1 = IterableExtensions.<AbstractConnectionMappingCall<?, U>>filter(_incoming, _function_2);
            final Function1<AbstractConnectionMappingCall<?, U>, AbstractConnectionMappingCall<T, U>> _function_3 = (AbstractConnectionMappingCall<?, U> it) -> {
              return ((AbstractConnectionMappingCall<T, U>) it);
            };
            _xifexpression_1 = IterableExtensions.<AbstractConnectionMappingCall<?, U>, AbstractConnectionMappingCall<T, U>>map(_filter_1, _function_3);
          }
          final Iterable<AbstractConnectionMappingCall<T, U>> siblingMappingCalls = _xifexpression_1;
          final Function1<Object, IMappedElementDescriptor<?>> _function_4 = (Object nodeDomainObject) -> {
            final U nodeObjectCasted = ((U) nodeDomainObject);
            for (final AbstractConnectionMappingCall<T, U> siblingMappingCall : siblingMappingCalls) {
              {
                XDiagramConfigInterpreter _interpreter_2 = this.getInterpreter();
                Iterable<T> _select_1 = _interpreter_2.<T, U>select(siblingMappingCall, nodeObjectCasted);
                final Function1<T, IMappedElementDescriptor<T>> _function_5 = (T it) -> {
                  XDiagramConfigInterpreter _interpreter_3 = this.getInterpreter();
                  AbstractMapping<T> _mapping_3 = siblingMappingCall.getMapping();
                  return _interpreter_3.<T>getDescriptor(it, _mapping_3);
                };
                Iterable<IMappedElementDescriptor<T>> _map = IterableExtensions.<T, IMappedElementDescriptor<T>>map(_select_1, _function_5);
                final Set<IMappedElementDescriptor<T>> siblingDescriptors = IterableExtensions.<IMappedElementDescriptor<T>>toSet(_map);
                XConnection _host = this.getHost();
                DomainObjectDescriptor _domainObjectDescriptor = _host.getDomainObjectDescriptor();
                boolean _contains = siblingDescriptors.contains(_domainObjectDescriptor);
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
  protected void reconcile(final AbstractMapping<T> mapping, final T domainObject, final UpdateAcceptor acceptor) {
    final ConnectionMapping<T> connectionMapping = ((ConnectionMapping<T>) mapping);
    XConnection _host = this.getHost();
    XNode _source = _host.getSource();
    DomainObjectDescriptor _domainObjectDescriptor = _source.getDomainObjectDescriptor();
    final DomainObjectDescriptor resolvedSourceDescriptor = this.<Object>resolveConnectionEnd(domainObject, connectionMapping, _domainObjectDescriptor, true);
    XConnection _host_1 = this.getHost();
    XNode _source_1 = _host_1.getSource();
    DomainObjectDescriptor _domainObjectDescriptor_1 = _source_1.getDomainObjectDescriptor();
    boolean _notEquals = (!Objects.equal(resolvedSourceDescriptor, _domainObjectDescriptor_1));
    if (_notEquals) {
      final XNode newSource = this.findNode(resolvedSourceDescriptor);
      boolean _notEquals_1 = (!Objects.equal(newSource, null));
      if (_notEquals_1) {
        XConnection _host_2 = this.getHost();
        XConnection _host_3 = this.getHost();
        XNode _source_2 = _host_3.getSource();
        ReconnectMorphCommand _reconnectMorphCommand = new ReconnectMorphCommand(_host_2, _source_2, newSource, true);
        acceptor.morph(_reconnectMorphCommand);
      } else {
        XConnection _host_4 = this.getHost();
        acceptor.delete(_host_4);
      }
    } else {
      XConnection _host_5 = this.getHost();
      XNode _target = _host_5.getTarget();
      DomainObjectDescriptor _domainObjectDescriptor_2 = _target.getDomainObjectDescriptor();
      final DomainObjectDescriptor resolvedTarget = this.<Object>resolveConnectionEnd(domainObject, connectionMapping, _domainObjectDescriptor_2, false);
      XConnection _host_6 = this.getHost();
      XNode _target_1 = _host_6.getTarget();
      DomainObjectDescriptor _domainObjectDescriptor_3 = _target_1.getDomainObjectDescriptor();
      boolean _notEquals_2 = (!Objects.equal(resolvedTarget, _domainObjectDescriptor_3));
      if (_notEquals_2) {
        final XNode newTarget = this.findNode(resolvedTarget);
        boolean _notEquals_3 = (!Objects.equal(newTarget, null));
        if (_notEquals_3) {
          XConnection _host_7 = this.getHost();
          XConnection _host_8 = this.getHost();
          XNode _target_2 = _host_8.getTarget();
          ReconnectMorphCommand _reconnectMorphCommand_1 = new ReconnectMorphCommand(_host_7, _target_2, newTarget, false);
          acceptor.morph(_reconnectMorphCommand_1);
        } else {
          XConnection _host_9 = this.getHost();
          acceptor.delete(_host_9);
        }
      }
    }
    XConnection _host_10 = this.getHost();
    final ConnectionLabelMorphCommand labelMorphCommand = new ConnectionLabelMorphCommand(_host_10);
    this.compareLabels(connectionMapping, domainObject, labelMorphCommand);
    boolean _isEmpty = labelMorphCommand.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      acceptor.morph(labelMorphCommand);
    }
  }
  
  protected XNode findNode(final DomainObjectDescriptor descriptor) {
    XConnection _host = this.getHost();
    XDiagram _diagram = CoreExtensions.getDiagram(_host);
    Iterable<? extends Node> _allChildren = CoreExtensions.getAllChildren(_diagram);
    Iterable<XNode> _filter = Iterables.<XNode>filter(_allChildren, XNode.class);
    final Function1<XNode, Boolean> _function = (XNode it) -> {
      DomainObjectDescriptor _domainObjectDescriptor = it.getDomainObjectDescriptor();
      return Boolean.valueOf(Objects.equal(_domainObjectDescriptor, descriptor));
    };
    return IterableExtensions.<XNode>findFirst(_filter, _function);
  }
  
  @Override
  protected Iterable<? extends XLabel> getExistingLabels() {
    XConnection _host = this.getHost();
    return _host.getLabels();
  }
  
  @Override
  protected Iterable<? extends AbstractLabelMappingCall<?, T>> getLabelMappingCalls(final AbstractMapping<T> mapping) {
    return ((ConnectionMapping<T>) mapping).getLabels();
  }
}
