package de.fxdiagram.mapping.behavior;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.behavior.AbstractReconcileBehavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.UpdateAcceptor;
import de.fxdiagram.core.extensions.DoubleExpressionExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.XDiagramConfigInterpreter;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
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
  
  private double strokeWidth;
  
  public ConnectionReconcileBehavior(final XConnection host) {
    super(host);
  }
  
  @Override
  public DirtyState getDirtyState() {
    XConnection _host = this.getHost();
    final DomainObjectDescriptor descriptor = _host.getDomainObject();
    if ((descriptor instanceof IMappedElementDescriptor<?>)) {
      try {
        final Function1<Object, DirtyState> _function = (Object domainObject) -> {
          AbstractMapping<?> _mapping = ((IMappedElementDescriptor<?>)descriptor).getMapping();
          final ConnectionMapping<T> connectionMapping = ((ConnectionMapping<T>) _mapping);
          XConnection _host_1 = this.getHost();
          XNode _source = _host_1.getSource();
          DomainObjectDescriptor _domainObject = _source.getDomainObject();
          final Object resolvedSourceDescriptor = this.<Object>resolveConnectionEnd(((T) domainObject), connectionMapping, _domainObject, true);
          XConnection _host_2 = this.getHost();
          XNode _source_1 = _host_2.getSource();
          DomainObjectDescriptor _domainObject_1 = _source_1.getDomainObject();
          boolean _equals = Objects.equal(resolvedSourceDescriptor, _domainObject_1);
          if (_equals) {
            XConnection _host_3 = this.getHost();
            XNode _target = _host_3.getTarget();
            DomainObjectDescriptor _domainObject_2 = _target.getDomainObject();
            final Object resolvedTarget = this.<Object>resolveConnectionEnd(((T) domainObject), connectionMapping, _domainObject_2, false);
            XConnection _host_4 = this.getHost();
            XNode _target_1 = _host_4.getTarget();
            DomainObjectDescriptor _domainObject_3 = _target_1.getDomainObject();
            boolean _equals_1 = Objects.equal(resolvedTarget, _domainObject_3);
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
  
  protected <U extends Object> Object resolveConnectionEnd(final T domainObject, final ConnectionMapping<T> connectionMapping, final DomainObjectDescriptor nodeDescriptor, final boolean isSource) {
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
              Iterable<T> _select_1 = interpreter.<T, U>select(siblingMappingCall, nodeObjectCasted);
              final Function1<T, Boolean> _function_5 = (T it) -> {
                AbstractMapping<T> _mapping_1 = siblingMappingCall.getMapping();
                IMappedElementDescriptor<T> _descriptor = interpreter.<T>getDescriptor(it, _mapping_1);
                XConnection _host = this.getHost();
                DomainObjectDescriptor _domainObject = _host.getDomainObject();
                return Boolean.valueOf(Objects.equal(_descriptor, _domainObject));
              };
              boolean _exists = IterableExtensions.<T>exists(_select_1, _function_5);
              if (_exists) {
                return ((IMappedElementDescriptor<?>)nodeDescriptor);
              }
            }
            return null;
          };
          return ((IMappedElementDescriptor<?>)nodeDescriptor).<IMappedElementDescriptor<?>>withDomainObject(_function_4);
        }
      }
    }
    return DirtyState.DIRTY;
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
      KeyValue _keyValue_1 = new <Number>KeyValue(this.dirtyAnimationValueProperty, Double.valueOf(0.96));
      KeyFrame _keyFrame_1 = new KeyFrame(_millis_1, _keyValue_1);
      _keyFrames_1.add(_keyFrame_1);
      ObservableList<KeyFrame> _keyFrames_2 = it.getKeyFrames();
      Duration _millis_2 = DurationExtensions.millis(900);
      KeyValue _keyValue_2 = new <Number>KeyValue(this.dirtyAnimationValueProperty, Double.valueOf(1.04));
      KeyFrame _keyFrame_2 = new KeyFrame(_millis_2, _keyValue_2);
      _keyFrames_2.add(_keyFrame_2);
      ObservableList<KeyFrame> _keyFrames_3 = it.getKeyFrames();
      Duration _millis_3 = DurationExtensions.millis(1200);
      KeyValue _keyValue_3 = new <Number>KeyValue(this.dirtyAnimationValueProperty, Integer.valueOf(1));
      KeyFrame _keyFrame_3 = new KeyFrame(_millis_3, _keyValue_3);
      _keyFrames_3.add(_keyFrame_3);
      it.setAutoReverse(true);
      it.setCycleCount(Animation.INDEFINITE);
    };
    Timeline _doubleArrow = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function);
    this.dirtyAnimation = _doubleArrow;
  }
  
  @Override
  protected void dirtyFeedback(final boolean isDirty) {
    if (isDirty) {
      XConnection _host = this.getHost();
      double _strokeWidth = _host.getStrokeWidth();
      this.strokeWidth = _strokeWidth;
      XConnection _host_1 = this.getHost();
      ObservableList<XConnectionLabel> _labels = _host_1.getLabels();
      XConnection _host_2 = this.getHost();
      ArrowHead _sourceArrowHead = _host_2.getSourceArrowHead();
      Node _node = null;
      if (_sourceArrowHead!=null) {
        _node=_sourceArrowHead.getNode();
      }
      XConnection _host_3 = this.getHost();
      ArrowHead _targetArrowHead = _host_3.getTargetArrowHead();
      Node _node_1 = null;
      if (_targetArrowHead!=null) {
        _node_1=_targetArrowHead.getNode();
      }
      Iterable<Node> _plus = Iterables.<Node>concat(_labels, Collections.<Node>unmodifiableList(CollectionLiterals.<Node>newArrayList(_node, _node_1)));
      Iterable<Node> _filterNull = IterableExtensions.<Node>filterNull(_plus);
      final Consumer<Node> _function = (Node it) -> {
        DoubleProperty _scaleXProperty = it.scaleXProperty();
        _scaleXProperty.bind(this.dirtyAnimationValueProperty);
        DoubleProperty _scaleYProperty = it.scaleYProperty();
        _scaleYProperty.bind(this.dirtyAnimationValueProperty);
      };
      _filterNull.forEach(_function);
      XConnection _host_4 = this.getHost();
      DoubleProperty _strokeWidthProperty = _host_4.strokeWidthProperty();
      DoubleBinding _minus = DoubleExpressionExtensions.operator_minus(this.dirtyAnimationValueProperty, 1);
      DoubleBinding _multiply = DoubleExpressionExtensions.operator_multiply(_minus, 40);
      DoubleBinding _plus_1 = DoubleExpressionExtensions.operator_plus(_multiply, this.strokeWidth);
      _strokeWidthProperty.bind(_plus_1);
      this.dirtyAnimation.play();
    } else {
      XConnection _host_5 = this.getHost();
      ObservableList<XConnectionLabel> _labels_1 = _host_5.getLabels();
      XConnection _host_6 = this.getHost();
      ArrowHead _sourceArrowHead_1 = _host_6.getSourceArrowHead();
      Node _node_2 = null;
      if (_sourceArrowHead_1!=null) {
        _node_2=_sourceArrowHead_1.getNode();
      }
      XConnection _host_7 = this.getHost();
      ArrowHead _targetArrowHead_1 = _host_7.getTargetArrowHead();
      Node _node_3 = null;
      if (_targetArrowHead_1!=null) {
        _node_3=_targetArrowHead_1.getNode();
      }
      Iterable<Node> _plus_2 = Iterables.<Node>concat(_labels_1, Collections.<Node>unmodifiableList(CollectionLiterals.<Node>newArrayList(_node_2, _node_3)));
      Iterable<Node> _filterNull_1 = IterableExtensions.<Node>filterNull(_plus_2);
      final Consumer<Node> _function_1 = (Node it) -> {
        DoubleProperty _scaleXProperty = it.scaleXProperty();
        _scaleXProperty.unbind();
        DoubleProperty _scaleYProperty = it.scaleYProperty();
        _scaleYProperty.unbind();
        it.setScaleX(1);
        it.setScaleY(1);
      };
      _filterNull_1.forEach(_function_1);
      XConnection _host_8 = this.getHost();
      DoubleProperty _strokeWidthProperty_1 = _host_8.strokeWidthProperty();
      _strokeWidthProperty_1.unbind();
      XConnection _host_9 = this.getHost();
      _host_9.setStrokeWidth(this.strokeWidth);
      this.dirtyAnimation.stop();
    }
  }
  
  @Override
  public void reconcile(final UpdateAcceptor acceptor) {
    XConnection _host = this.getHost();
    final DomainObjectDescriptor descriptor = _host.getDomainObject();
    if ((descriptor instanceof IMappedElementDescriptor<?>)) {
      try {
        final Function1<Object, Object> _function = (Object domainObject) -> {
          Object _xblockexpression = null;
          {
            AbstractMapping<?> _mapping = ((IMappedElementDescriptor<?>)descriptor).getMapping();
            final ConnectionMapping<T> connectionMapping = ((ConnectionMapping<T>) _mapping);
            XConnection _host_1 = this.getHost();
            XNode _source = _host_1.getSource();
            DomainObjectDescriptor _domainObject = _source.getDomainObject();
            final Object resolvedSourceDescriptor = this.<Object>resolveConnectionEnd(((T) domainObject), connectionMapping, _domainObject, true);
            XConnection _host_2 = this.getHost();
            XNode _source_1 = _host_2.getSource();
            DomainObjectDescriptor _domainObject_1 = _source_1.getDomainObject();
            boolean _notEquals = (!Objects.equal(resolvedSourceDescriptor, _domainObject_1));
            if (_notEquals) {
              XConnection _host_3 = this.getHost();
              acceptor.delete(_host_3);
            } else {
              XConnection _host_4 = this.getHost();
              XNode _target = _host_4.getTarget();
              DomainObjectDescriptor _domainObject_2 = _target.getDomainObject();
              final Object resolvedTarget = this.<Object>resolveConnectionEnd(((T) domainObject), connectionMapping, _domainObject_2, false);
              XConnection _host_5 = this.getHost();
              XNode _target_1 = _host_5.getTarget();
              DomainObjectDescriptor _domainObject_3 = _target_1.getDomainObject();
              boolean _notEquals_1 = (!Objects.equal(resolvedTarget, _domainObject_3));
              if (_notEquals_1) {
                XConnection _host_6 = this.getHost();
                acceptor.delete(_host_6);
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
