package de.fxdiagram.lib.nodes;

import com.google.common.base.Objects;
import de.fxdiagram.core.behavior.AbstractDirtyStateBehavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.UpdateAcceptor;
import de.fxdiagram.core.command.SequentialAnimationCommand;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.lib.nodes.AbstractClassNode;
import de.fxdiagram.lib.nodes.ClassModel;
import java.util.NoSuchElementException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ModelCompareBehavior extends AbstractDirtyStateBehavior<AbstractClassNode> {
  private Animation dirtyAnimation;
  
  public ModelCompareBehavior(final AbstractClassNode host) {
    super(host);
  }
  
  @Override
  public DirtyState getDirtyState() {
    DirtyState _xtrycatchfinallyexpression = null;
    try {
      DirtyState _xblockexpression = null;
      {
        AbstractClassNode _host = this.getHost();
        final ClassModel newModel = _host.inferClassModel();
        DirtyState _xifexpression = null;
        boolean _equals = Objects.equal(newModel, null);
        if (_equals) {
          _xifexpression = DirtyState.DANGLING;
        } else {
          DirtyState _xifexpression_1 = null;
          AbstractClassNode _host_1 = this.getHost();
          ClassModel _model = _host_1.getModel();
          boolean _notEquals = (!Objects.equal(_model, newModel));
          if (_notEquals) {
            _xifexpression_1 = DirtyState.DIRTY;
          } else {
            _xifexpression_1 = DirtyState.CLEAN;
          }
          _xifexpression = _xifexpression_1;
        }
        _xblockexpression = _xifexpression;
      }
      _xtrycatchfinallyexpression = _xblockexpression;
    } catch (final Throwable _t) {
      if (_t instanceof NoSuchElementException) {
        final NoSuchElementException exc = (NoSuchElementException)_t;
        return DirtyState.DANGLING;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return _xtrycatchfinallyexpression;
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
      AbstractClassNode _host = this.getHost();
      final Procedure1<AbstractClassNode> _function = (AbstractClassNode it) -> {
        DoubleProperty _scaleXProperty = it.scaleXProperty();
        _scaleXProperty.bind(this.dirtyAnimationValueProperty);
        DoubleProperty _scaleYProperty = it.scaleYProperty();
        _scaleYProperty.bind(this.dirtyAnimationValueProperty);
      };
      ObjectExtensions.<AbstractClassNode>operator_doubleArrow(_host, _function);
      this.dirtyAnimation.play();
    } else {
      AbstractClassNode _host_1 = this.getHost();
      final Procedure1<AbstractClassNode> _function_1 = (AbstractClassNode it) -> {
        DoubleProperty _scaleXProperty = it.scaleXProperty();
        _scaleXProperty.unbind();
        DoubleProperty _scaleYProperty = it.scaleYProperty();
        _scaleYProperty.unbind();
        it.setScaleX(1);
        it.setScaleY(1);
      };
      ObjectExtensions.<AbstractClassNode>operator_doubleArrow(_host_1, _function_1);
      this.dirtyAnimation.stop();
    }
  }
  
  @Override
  public void update(final UpdateAcceptor acceptor) {
    try {
      AbstractClassNode _host = this.getHost();
      final ClassModel newModel = _host.inferClassModel();
      boolean _notEquals = (!Objects.equal(newModel, null));
      if (_notEquals) {
        AbstractClassNode _host_1 = this.getHost();
        ClassModel _model = _host_1.getModel();
        boolean _notEquals_1 = (!Objects.equal(_model, newModel));
        if (_notEquals_1) {
          AbstractClassNode _host_2 = this.getHost();
          SequentialAnimationCommand _createMorphCommand = _host_2.createMorphCommand(newModel);
          acceptor.morph(_createMorphCommand);
        }
        return;
      }
    } catch (final Throwable _t) {
      if (_t instanceof NoSuchElementException) {
        final NoSuchElementException exc = (NoSuchElementException)_t;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    AbstractClassNode _host_3 = this.getHost();
    acceptor.delete(_host_3);
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
  
  private SimpleDoubleProperty dirtyAnimationRotateProperty = new SimpleDoubleProperty(this, "dirtyAnimationRotate");
  
  public double getDirtyAnimationRotate() {
    return this.dirtyAnimationRotateProperty.get();
  }
  
  public void setDirtyAnimationRotate(final double dirtyAnimationRotate) {
    this.dirtyAnimationRotateProperty.set(dirtyAnimationRotate);
  }
  
  public DoubleProperty dirtyAnimationRotateProperty() {
    return this.dirtyAnimationRotateProperty;
  }
}
