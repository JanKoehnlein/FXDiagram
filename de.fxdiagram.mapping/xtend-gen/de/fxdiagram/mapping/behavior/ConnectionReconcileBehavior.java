package de.fxdiagram.mapping.behavior;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.behavior.AbstractReconcileBehavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.UpdateAcceptor;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.XDiagramConfigInterpreter;
import de.fxdiagram.mapping.behavior.ReconnectMorphCommand;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ConnectionReconcileBehavior<T extends Object> extends AbstractReconcileBehavior<XConnection> {
  private Animation dirtyAnimation;
  
  public ConnectionReconcileBehavior(final XConnection host) {
    super(host);
  }
  
  @Override
  public DirtyState getDirtyState() {
    XConnection _host = this.getHost();
    final DomainObjectDescriptor descriptor = _host.getDomainObjectDescriptor();
    if ((descriptor instanceof IMappedElementDescriptor<?>)) {
      try {
        final Function1<Object, DirtyState> _function = (Object domainObject) -> {
          AbstractMapping<?> _mapping = ((IMappedElementDescriptor<?>)descriptor).getMapping();
          final ConnectionMapping<T> connectionMapping = ((ConnectionMapping<T>) _mapping);
          XConnection _host_1 = this.getHost();
          XNode _source = _host_1.getSource();
          DomainObjectDescriptor _domainObjectDescriptor = _source.getDomainObjectDescriptor();
          final DomainObjectDescriptor resolvedSourceDescriptor = this.<Object>resolveConnectionEnd(((T) domainObject), connectionMapping, _domainObjectDescriptor, true);
          XConnection _host_2 = this.getHost();
          XNode _source_1 = _host_2.getSource();
          DomainObjectDescriptor _domainObjectDescriptor_1 = _source_1.getDomainObjectDescriptor();
          boolean _equals = Objects.equal(resolvedSourceDescriptor, _domainObjectDescriptor_1);
          if (_equals) {
            XConnection _host_3 = this.getHost();
            XNode _target = _host_3.getTarget();
            DomainObjectDescriptor _domainObjectDescriptor_2 = _target.getDomainObjectDescriptor();
            final DomainObjectDescriptor resolvedTarget = this.<Object>resolveConnectionEnd(((T) domainObject), connectionMapping, _domainObjectDescriptor_2, false);
            XConnection _host_4 = this.getHost();
            XNode _target_1 = _host_4.getTarget();
            DomainObjectDescriptor _domainObjectDescriptor_3 = _target_1.getDomainObjectDescriptor();
            boolean _equals_1 = Objects.equal(resolvedTarget, _domainObjectDescriptor_3);
            if (_equals_1) {
              return DirtyState.CLEAN;
            }
          }
          return DirtyState.DIRTY;
        };
        return ((IMappedElementDescriptor<?>)descriptor).<DirtyState>withDomainObject(_function);
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
    final XDiagramConfigInterpreter interpreter = new XDiagramConfigInterpreter();
    NodeMappingCall<?, T> _xifexpression = null;
    if (isSource) {
      _xifexpression = connectionMapping.getSource();
    } else {
      _xifexpression = connectionMapping.getTarget();
    }
    final NodeMappingCall<U, T> nodeMappingCall = ((NodeMappingCall<U, T>) _xifexpression);
    boolean _notEquals = (!Objects.equal(nodeMappingCall, null));
    if (_notEquals) {
      Iterable<U> _select = interpreter.<U, T>select(nodeMappingCall, domainObject);
      U _head = IterableExtensions.<U>head(_select);
      final U nodeObject = ((U) _head);
      boolean _equals = Objects.equal(nodeObject, null);
      if (_equals) {
        return null;
      }
      AbstractMapping<U> _mapping = nodeMappingCall.getMapping();
      final IMappedElementDescriptor<U> resolvedNodeDescriptor = interpreter.<U>getDescriptor(nodeObject, _mapping);
      boolean _notEquals_1 = (!Objects.equal(resolvedNodeDescriptor, nodeDescriptor));
      if (_notEquals_1) {
        return resolvedNodeDescriptor;
      } else {
        return nodeDescriptor;
      }
    } else {
      if ((nodeDescriptor instanceof IMappedElementDescriptor<?>)) {
        final AbstractMapping<?> nodeMapping = ((IMappedElementDescriptor<?>)nodeDescriptor).getMapping();
        if ((nodeMapping instanceof NodeMapping<?>)) {
          final NodeMapping<U> nodeMappingCasted = ((NodeMapping<U>) nodeMapping);
          Iterable<AbstractConnectionMappingCall<T, U>> _xifexpression_1 = null;
          if (isSource) {
            List<AbstractConnectionMappingCall<?, U>> _outgoing = nodeMappingCasted.getOutgoing();
            final Function1<AbstractConnectionMappingCall<?, U>, Boolean> _function = (AbstractConnectionMappingCall<?, U> it) -> {
              AbstractMapping<?> _mapping_1 = it.getMapping();
              return Boolean.valueOf(Objects.equal(_mapping_1, connectionMapping));
            };
            Iterable<AbstractConnectionMappingCall<?, U>> _filter = IterableExtensions.<AbstractConnectionMappingCall<?, U>>filter(_outgoing, _function);
            final Function1<AbstractConnectionMappingCall<?, U>, AbstractConnectionMappingCall<T, U>> _function_1 = (AbstractConnectionMappingCall<?, U> it) -> {
              return ((AbstractConnectionMappingCall<T, U>) it);
            };
            _xifexpression_1 = IterableExtensions.<AbstractConnectionMappingCall<?, U>, AbstractConnectionMappingCall<T, U>>map(_filter, _function_1);
          } else {
            List<AbstractConnectionMappingCall<?, U>> _incoming = nodeMappingCasted.getIncoming();
            final Function1<AbstractConnectionMappingCall<?, U>, Boolean> _function_2 = (AbstractConnectionMappingCall<?, U> it) -> {
              AbstractMapping<?> _mapping_1 = it.getMapping();
              return Boolean.valueOf(Objects.equal(_mapping_1, connectionMapping));
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
                Iterable<T> _select_1 = interpreter.<T, U>select(siblingMappingCall, nodeObjectCasted);
                final Function1<T, IMappedElementDescriptor<T>> _function_5 = (T it) -> {
                  AbstractMapping<T> _mapping_1 = siblingMappingCall.getMapping();
                  return interpreter.<T>getDescriptor(it, _mapping_1);
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
  protected void doActivate() {
    Timeline _timeline = new Timeline();
    final Procedure1<Timeline> _function = (Timeline it) -> {
      ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
      Duration _millis = DurationExtensions.millis(0);
      KeyValue _keyValue = new <Number>KeyValue(this.dirtyAnimationValueProperty, Integer.valueOf(1));
      KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue);
      _keyFrames.add(_keyFrame);
      ObservableList<KeyFrame> _keyFrames_1 = it.getKeyFrames();
      Duration _millis_1 = DurationExtensions.millis(300);
      KeyValue _keyValue_1 = new <Number>KeyValue(this.dirtyAnimationValueProperty, Double.valueOf(0.2));
      KeyFrame _keyFrame_1 = new KeyFrame(_millis_1, _keyValue_1);
      _keyFrames_1.add(_keyFrame_1);
      ObservableList<KeyFrame> _keyFrames_2 = it.getKeyFrames();
      Duration _millis_2 = DurationExtensions.millis(900);
      KeyValue _keyValue_2 = new <Number>KeyValue(this.dirtyAnimationValueProperty, Integer.valueOf(1));
      KeyFrame _keyFrame_2 = new KeyFrame(_millis_2, _keyValue_2);
      _keyFrames_2.add(_keyFrame_2);
      it.setCycleCount(Animation.INDEFINITE);
    };
    Timeline _doubleArrow = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function);
    this.dirtyAnimation = _doubleArrow;
  }
  
  @Override
  protected void dirtyFeedback(final boolean isDirty) {
    if (isDirty) {
      XConnection _host = this.getHost();
      ObservableList<XConnectionLabel> _labels = _host.getLabels();
      XConnection _host_1 = this.getHost();
      ArrowHead _sourceArrowHead = _host_1.getSourceArrowHead();
      Node _node = null;
      if (_sourceArrowHead!=null) {
        _node=_sourceArrowHead.getNode();
      }
      XConnection _host_2 = this.getHost();
      ArrowHead _targetArrowHead = _host_2.getTargetArrowHead();
      Node _node_1 = null;
      if (_targetArrowHead!=null) {
        _node_1=_targetArrowHead.getNode();
      }
      Iterable<Node> _plus = Iterables.<Node>concat(_labels, Collections.<Node>unmodifiableList(CollectionLiterals.<Node>newArrayList(_node, _node_1)));
      Iterable<Node> _filterNull = IterableExtensions.<Node>filterNull(_plus);
      final Consumer<Node> _function = (Node it) -> {
        DoubleProperty _opacityProperty = it.opacityProperty();
        _opacityProperty.bind(this.dirtyAnimationValueProperty);
      };
      _filterNull.forEach(_function);
      this.dirtyAnimation.play();
    } else {
      XConnection _host_3 = this.getHost();
      ObservableList<XConnectionLabel> _labels_1 = _host_3.getLabels();
      XConnection _host_4 = this.getHost();
      ArrowHead _sourceArrowHead_1 = _host_4.getSourceArrowHead();
      Node _node_2 = null;
      if (_sourceArrowHead_1!=null) {
        _node_2=_sourceArrowHead_1.getNode();
      }
      XConnection _host_5 = this.getHost();
      ArrowHead _targetArrowHead_1 = _host_5.getTargetArrowHead();
      Node _node_3 = null;
      if (_targetArrowHead_1!=null) {
        _node_3=_targetArrowHead_1.getNode();
      }
      Iterable<Node> _plus_1 = Iterables.<Node>concat(_labels_1, Collections.<Node>unmodifiableList(CollectionLiterals.<Node>newArrayList(_node_2, _node_3)));
      Iterable<Node> _filterNull_1 = IterableExtensions.<Node>filterNull(_plus_1);
      final Consumer<Node> _function_1 = (Node it) -> {
        DoubleProperty _opacityProperty = it.opacityProperty();
        _opacityProperty.unbind();
        it.setOpacity(1);
      };
      _filterNull_1.forEach(_function_1);
      this.dirtyAnimation.stop();
    }
  }
  
  @Override
  public void reconcile(final UpdateAcceptor acceptor) {
    XConnection _host = this.getHost();
    final DomainObjectDescriptor descriptor = _host.getDomainObjectDescriptor();
    if ((descriptor instanceof IMappedElementDescriptor<?>)) {
      try {
        final Function1<Object, Object> _function = (Object domainObject) -> {
          Object _xblockexpression = null;
          {
            AbstractMapping<?> _mapping = ((IMappedElementDescriptor<?>)descriptor).getMapping();
            final ConnectionMapping<T> connectionMapping = ((ConnectionMapping<T>) _mapping);
            XConnection _host_1 = this.getHost();
            XNode _source = _host_1.getSource();
            DomainObjectDescriptor _domainObjectDescriptor = _source.getDomainObjectDescriptor();
            final DomainObjectDescriptor resolvedSourceDescriptor = this.<Object>resolveConnectionEnd(((T) domainObject), connectionMapping, _domainObjectDescriptor, true);
            XConnection _host_2 = this.getHost();
            XNode _source_1 = _host_2.getSource();
            DomainObjectDescriptor _domainObjectDescriptor_1 = _source_1.getDomainObjectDescriptor();
            boolean _notEquals = (!Objects.equal(resolvedSourceDescriptor, _domainObjectDescriptor_1));
            if (_notEquals) {
              final XNode newSource = this.findNode(resolvedSourceDescriptor);
              boolean _notEquals_1 = (!Objects.equal(newSource, null));
              if (_notEquals_1) {
                XConnection _host_3 = this.getHost();
                XConnection _host_4 = this.getHost();
                XNode _source_2 = _host_4.getSource();
                ReconnectMorphCommand _reconnectMorphCommand = new ReconnectMorphCommand(_host_3, _source_2, newSource, true);
                acceptor.morph(_reconnectMorphCommand);
              } else {
                XConnection _host_5 = this.getHost();
                acceptor.delete(_host_5);
              }
            } else {
              XConnection _host_6 = this.getHost();
              XNode _target = _host_6.getTarget();
              DomainObjectDescriptor _domainObjectDescriptor_2 = _target.getDomainObjectDescriptor();
              final DomainObjectDescriptor resolvedTarget = this.<Object>resolveConnectionEnd(((T) domainObject), connectionMapping, _domainObjectDescriptor_2, false);
              XConnection _host_7 = this.getHost();
              XNode _target_1 = _host_7.getTarget();
              DomainObjectDescriptor _domainObjectDescriptor_3 = _target_1.getDomainObjectDescriptor();
              boolean _notEquals_2 = (!Objects.equal(resolvedTarget, _domainObjectDescriptor_3));
              if (_notEquals_2) {
                final XNode newTarget = this.findNode(resolvedTarget);
                boolean _notEquals_3 = (!Objects.equal(newTarget, null));
                if (_notEquals_3) {
                  XConnection _host_8 = this.getHost();
                  XConnection _host_9 = this.getHost();
                  XNode _target_2 = _host_9.getTarget();
                  ReconnectMorphCommand _reconnectMorphCommand_1 = new ReconnectMorphCommand(_host_8, _target_2, newTarget, false);
                  acceptor.morph(_reconnectMorphCommand_1);
                } else {
                  XConnection _host_10 = this.getHost();
                  acceptor.delete(_host_10);
                }
              }
            }
            _xblockexpression = null;
          }
          return _xblockexpression;
        };
        ((IMappedElementDescriptor<?>)descriptor).<Object>withDomainObject(_function);
      } catch (final Throwable _t) {
        if (_t instanceof NoSuchElementException) {
          final NoSuchElementException exc = (NoSuchElementException)_t;
          XConnection _host_1 = this.getHost();
          acceptor.delete(_host_1);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
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
  
  private SimpleDoubleProperty dirtyAnimationValueProperty = new SimpleDoubleProperty(this, "dirtyAnimationValue");
  
  public double getDirtyAnimationValue() {
    return this.dirtyAnimationValueProperty.get();
  }
  
  public void setDirtyAnimationValue(final double dirtyAnimationValue) {
    this.dirtyAnimationValueProperty.set(dirtyAnimationValue);
  }
  
  public DoubleProperty dirtyAnimationValueProperty() {
    return this.dirtyAnimationValueProperty;
  }
}
