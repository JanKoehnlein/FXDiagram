package de.fxdiagram.lib.animations;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.DurationExtensions;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Inflator {
  private XNode host;
  
  private Pane container;
  
  private Map<VBox, Rectangle> inflatable2spacer = CollectionLiterals.<VBox, Rectangle>newLinkedHashMap();
  
  private double deflatedWidth;
  
  private double deflatedHeight;
  
  private double inflatedWidth;
  
  private double inflatedHeight;
  
  private boolean isInflated = false;
  
  public Inflator(final XNode host, final Pane container) {
    this.host = host;
    this.container = container;
  }
  
  public Rectangle addInflatable(final VBox inflatable, final int index) {
    Rectangle _xblockexpression = null;
    {
      Rectangle _rectangle = new Rectangle(0, 0, 0, 0);
      final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
        public void apply(final Rectangle it) {
          it.setOpacity(0);
        }
      };
      final Rectangle spacer = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
      ObservableList<Node> _children = this.container.getChildren();
      _children.add(index, spacer);
      _xblockexpression = this.inflatable2spacer.put(inflatable, spacer);
    }
    return _xblockexpression;
  }
  
  public Transition getInflateAnimation() {
    SequentialTransition _xblockexpression = null;
    {
      boolean _or = false;
      boolean _isEmpty = this.inflatable2spacer.isEmpty();
      if (_isEmpty) {
        _or = true;
      } else {
        _or = this.isInflated;
      }
      if (_or) {
        return this.createEmptyTransition();
      }
      final Dimension2D containerSize = this.calculateSize(this.container);
      final Insets padding = this.container.getPadding();
      double _width = containerSize.getWidth();
      double _left = padding.getLeft();
      double _minus = (_width - _left);
      double _right = padding.getRight();
      double _minus_1 = (_minus - _right);
      this.deflatedWidth = _minus_1;
      double _height = containerSize.getHeight();
      double _top = padding.getTop();
      double _minus_2 = (_height - _top);
      double _bottom = padding.getBottom();
      double _minus_3 = (_minus_2 - _bottom);
      this.deflatedHeight = _minus_3;
      SequentialTransition _sequentialTransition = new SequentialTransition();
      final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
        public void apply(final SequentialTransition it) {
          Duration _millis = DurationExtensions.millis(200);
          it.setDelay(_millis);
          ObservableList<Animation> _children = it.getChildren();
          ParallelTransition _inflate = Inflator.this.inflate();
          _children.add(_inflate);
          ObservableList<Animation> _children_1 = it.getChildren();
          Transition _appear = Inflator.this.appear();
          _children_1.add(_appear);
          final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent it) {
              Inflator.this.isInflated = true;
            }
          };
          it.setOnFinished(_function);
        }
      };
      _xblockexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
    }
    return _xblockexpression;
  }
  
  public Transition getDeflateAnimation() {
    SequentialTransition _xblockexpression = null;
    {
      boolean _or = false;
      boolean _isEmpty = this.inflatable2spacer.isEmpty();
      if (_isEmpty) {
        _or = true;
      } else {
        _or = (!this.isInflated);
      }
      if (_or) {
        return this.createEmptyTransition();
      }
      SequentialTransition _sequentialTransition = new SequentialTransition();
      final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
        public void apply(final SequentialTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          Transition _disappear = Inflator.this.disappear();
          _children.add(_disappear);
          ObservableList<Animation> _children_1 = it.getChildren();
          ParallelTransition _deflate = Inflator.this.deflate();
          _children_1.add(_deflate);
          final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent it) {
              Inflator.this.isInflated = false;
              Inflator.this.host.layout();
            }
          };
          it.setOnFinished(_function);
        }
      };
      _xblockexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
    }
    return _xblockexpression;
  }
  
  protected ParallelTransition inflate() {
    ParallelTransition _parallelTransition = new ParallelTransition();
    final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
      public void apply(final ParallelTransition pt) {
        Inflator.this.inflatedWidth = Inflator.this.deflatedWidth;
        Inflator.this.inflatedHeight = 0.0;
        Set<Map.Entry<VBox, Rectangle>> _entrySet = Inflator.this.inflatable2spacer.entrySet();
        for (final Map.Entry<VBox, Rectangle> it : _entrySet) {
          {
            final VBox inflatable = it.getKey();
            final Rectangle spacer = it.getValue();
            final Dimension2D size = Inflator.this.calculateSize(inflatable);
            double _width = size.getWidth();
            double _max = Math.max(Inflator.this.inflatedWidth, _width);
            Inflator.this.inflatedWidth = _max;
            double _inflatedHeight = Inflator.this.inflatedHeight;
            double _height = size.getHeight();
            Inflator.this.inflatedHeight = (_inflatedHeight + _height);
            ObservableList<Animation> _children = pt.getChildren();
            Timeline _timeline = new Timeline();
            final Procedure1<Timeline> _function = new Procedure1<Timeline>() {
              public void apply(final Timeline it) {
                it.setCycleCount(1);
                it.setAutoReverse(false);
                ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
                Duration _millis = DurationExtensions.millis(0);
                DoubleProperty _widthProperty = spacer.widthProperty();
                KeyValue _keyValue = new <Number>KeyValue(_widthProperty, Double.valueOf(Inflator.this.deflatedWidth));
                DoubleProperty _heightProperty = spacer.heightProperty();
                KeyValue _keyValue_1 = new <Number>KeyValue(_heightProperty, Integer.valueOf(0));
                KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue, _keyValue_1);
                _keyFrames.add(_keyFrame);
                ObservableList<KeyFrame> _keyFrames_1 = it.getKeyFrames();
                Duration _millis_1 = DurationExtensions.millis(200);
                DoubleProperty _widthProperty_1 = spacer.widthProperty();
                double _width = size.getWidth();
                KeyValue _keyValue_2 = new <Number>KeyValue(_widthProperty_1, Double.valueOf(_width));
                DoubleProperty _heightProperty_1 = spacer.heightProperty();
                double _height = size.getHeight();
                KeyValue _keyValue_3 = new <Number>KeyValue(_heightProperty_1, Double.valueOf(_height));
                KeyFrame _keyFrame_1 = new KeyFrame(_millis_1, _keyValue_2, _keyValue_3);
                _keyFrames_1.add(_keyFrame_1);
              }
            };
            Timeline _doubleArrow = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function);
            _children.add(_doubleArrow);
          }
        }
        final Point2D inflatedHostPos = Inflator.this.getInflatedHostPosition();
        ObservableList<Animation> _children = pt.getChildren();
        Timeline _timeline = new Timeline();
        final Procedure1<Timeline> _function = new Procedure1<Timeline>() {
          public void apply(final Timeline it) {
            it.setAutoReverse(false);
            ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
            Duration _millis = DurationExtensions.millis(200);
            DoubleProperty _layoutXProperty = Inflator.this.host.layoutXProperty();
            double _x = inflatedHostPos.getX();
            KeyValue _keyValue = new <Number>KeyValue(_layoutXProperty, Double.valueOf(_x));
            DoubleProperty _layoutYProperty = Inflator.this.host.layoutYProperty();
            double _y = inflatedHostPos.getY();
            KeyValue _keyValue_1 = new <Number>KeyValue(_layoutYProperty, Double.valueOf(_y));
            KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue, _keyValue_1);
            _keyFrames.add(_keyFrame);
          }
        };
        Timeline _doubleArrow = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function);
        _children.add(_doubleArrow);
        final EventHandler<ActionEvent> _function_1 = new EventHandler<ActionEvent>() {
          public void handle(final ActionEvent it) {
            Set<Map.Entry<VBox, Rectangle>> _entrySet = Inflator.this.inflatable2spacer.entrySet();
            for (final Map.Entry<VBox, Rectangle> it_1 : _entrySet) {
              {
                final VBox inflatable = it_1.getKey();
                final Rectangle spacer = it_1.getValue();
                ObservableList<Node> _children = inflatable.getChildren();
                final Consumer<Node> _function = new Consumer<Node>() {
                  public void accept(final Node it) {
                    it.setOpacity(0);
                  }
                };
                _children.forEach(_function);
                final ObservableList<Node> siblings = Inflator.this.container.getChildren();
                int _indexOf = siblings.indexOf(spacer);
                siblings.set(_indexOf, inflatable);
              }
            }
          }
        };
        pt.setOnFinished(_function_1);
      }
    };
    return ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
  }
  
  protected ParallelTransition deflate() {
    ParallelTransition _parallelTransition = new ParallelTransition();
    final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
      public void apply(final ParallelTransition pt) {
        Collection<Rectangle> _values = Inflator.this.inflatable2spacer.values();
        for (final Rectangle spacer : _values) {
          ObservableList<Animation> _children = pt.getChildren();
          Timeline _timeline = new Timeline();
          final Procedure1<Timeline> _function = new Procedure1<Timeline>() {
            public void apply(final Timeline it) {
              it.setCycleCount(1);
              it.setAutoReverse(false);
              ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
              Duration _millis = DurationExtensions.millis(200);
              DoubleProperty _widthProperty = spacer.widthProperty();
              KeyValue _keyValue = new <Number>KeyValue(_widthProperty, Double.valueOf(Inflator.this.deflatedWidth));
              DoubleProperty _heightProperty = spacer.heightProperty();
              KeyValue _keyValue_1 = new <Number>KeyValue(_heightProperty, Integer.valueOf(0));
              KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue, _keyValue_1);
              _keyFrames.add(_keyFrame);
            }
          };
          Timeline _doubleArrow = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function);
          _children.add(_doubleArrow);
        }
        final Point2D deflatedHostPos = Inflator.this.getDeflatedHostPosition();
        ObservableList<Animation> _children_1 = pt.getChildren();
        Timeline _timeline_1 = new Timeline();
        final Procedure1<Timeline> _function_1 = new Procedure1<Timeline>() {
          public void apply(final Timeline it) {
            it.setAutoReverse(false);
            ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
            Duration _millis = DurationExtensions.millis(200);
            DoubleProperty _layoutXProperty = Inflator.this.host.layoutXProperty();
            double _x = deflatedHostPos.getX();
            KeyValue _keyValue = new <Number>KeyValue(_layoutXProperty, Double.valueOf(_x));
            DoubleProperty _layoutYProperty = Inflator.this.host.layoutYProperty();
            double _y = deflatedHostPos.getY();
            KeyValue _keyValue_1 = new <Number>KeyValue(_layoutYProperty, Double.valueOf(_y));
            KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue, _keyValue_1);
            _keyFrames.add(_keyFrame);
          }
        };
        Timeline _doubleArrow_1 = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline_1, _function_1);
        _children_1.add(_doubleArrow_1);
      }
    };
    return ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
  }
  
  protected Transition appear() {
    Transition _xblockexpression = null;
    {
      Set<VBox> _keySet = this.inflatable2spacer.keySet();
      final Function1<VBox, ObservableList<Node>> _function = new Function1<VBox, ObservableList<Node>>() {
        public ObservableList<Node> apply(final VBox it) {
          return it.getChildren();
        }
      };
      Iterable<ObservableList<Node>> _map = IterableExtensions.<VBox, ObservableList<Node>>map(_keySet, _function);
      final Iterable<Node> contents = Iterables.<Node>concat(_map);
      Transition _xifexpression = null;
      boolean _isEmpty = IterableExtensions.isEmpty(contents);
      if (_isEmpty) {
        _xifexpression = this.createEmptyTransition();
      } else {
        SequentialTransition _sequentialTransition = new SequentialTransition();
        final Procedure1<SequentialTransition> _function_1 = new Procedure1<SequentialTransition>() {
          public void apply(final SequentialTransition it) {
            ObservableList<Animation> _children = it.getChildren();
            final Function1<Node, FadeTransition> _function = new Function1<Node, FadeTransition>() {
              public FadeTransition apply(final Node child) {
                FadeTransition _fadeTransition = new FadeTransition();
                final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
                  public void apply(final FadeTransition it) {
                    it.setNode(child);
                    Duration _millis = DurationExtensions.millis(30);
                    it.setDuration(_millis);
                    it.setFromValue(0);
                    it.setToValue(1);
                  }
                };
                return ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
              }
            };
            Iterable<FadeTransition> _map = IterableExtensions.<Node, FadeTransition>map(contents, _function);
            Iterables.<Animation>addAll(_children, _map);
          }
        };
        _xifexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function_1);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected Transition createEmptyTransition() {
    return new Transition() {
      protected void interpolate(final double frac) {
      }
    };
  }
  
  protected Transition disappear() {
    Transition _xblockexpression = null;
    {
      Set<VBox> _keySet = this.inflatable2spacer.keySet();
      final Function1<VBox, ObservableList<Node>> _function = new Function1<VBox, ObservableList<Node>>() {
        public ObservableList<Node> apply(final VBox it) {
          return it.getChildren();
        }
      };
      Iterable<ObservableList<Node>> _map = IterableExtensions.<VBox, ObservableList<Node>>map(_keySet, _function);
      final Iterable<Node> contents = Iterables.<Node>concat(_map);
      Transition _xifexpression = null;
      boolean _isEmpty = IterableExtensions.isEmpty(contents);
      if (_isEmpty) {
        _xifexpression = new Transition() {
          protected void interpolate(final double frac) {
          }
        };
      } else {
        ParallelTransition _parallelTransition = new ParallelTransition();
        final Procedure1<ParallelTransition> _function_1 = new Procedure1<ParallelTransition>() {
          public void apply(final ParallelTransition it) {
            ObservableList<Animation> _children = it.getChildren();
            final Function1<Node, FadeTransition> _function = new Function1<Node, FadeTransition>() {
              public FadeTransition apply(final Node child) {
                FadeTransition _fadeTransition = new FadeTransition();
                final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
                  public void apply(final FadeTransition it) {
                    it.setNode(child);
                    Duration _millis = DurationExtensions.millis(30);
                    it.setDuration(_millis);
                    it.setFromValue(1);
                    it.setToValue(0);
                  }
                };
                return ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
              }
            };
            Iterable<FadeTransition> _map = IterableExtensions.<Node, FadeTransition>map(contents, _function);
            Iterables.<Animation>addAll(_children, _map);
            final EventHandler<ActionEvent> _function_1 = new EventHandler<ActionEvent>() {
              public void handle(final ActionEvent it) {
                Set<Map.Entry<VBox, Rectangle>> _entrySet = Inflator.this.inflatable2spacer.entrySet();
                final Consumer<Map.Entry<VBox, Rectangle>> _function = new Consumer<Map.Entry<VBox, Rectangle>>() {
                  public void accept(final Map.Entry<VBox, Rectangle> it) {
                    ObservableList<Node> _children = Inflator.this.container.getChildren();
                    VBox _key = it.getKey();
                    final int index = _children.indexOf(_key);
                    Rectangle _value = it.getValue();
                    VBox _key_1 = it.getKey();
                    double _width = _key_1.getWidth();
                    _value.setWidth(_width);
                    Rectangle _value_1 = it.getValue();
                    VBox _key_2 = it.getKey();
                    double _height = _key_2.getHeight();
                    _value_1.setHeight(_height);
                    ObservableList<Node> _children_1 = Inflator.this.container.getChildren();
                    Rectangle _value_2 = it.getValue();
                    _children_1.set(index, _value_2);
                  }
                };
                _entrySet.forEach(_function);
              }
            };
            it.setOnFinished(_function_1);
          }
        };
        _xifexpression = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_1);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected Point2D getInflatedHostPosition() {
    double _layoutX = this.host.getLayoutX();
    double _switchResult = (double) 0;
    Side _placementHint = this.host.getPlacementHint();
    if (_placementHint != null) {
      switch (_placementHint) {
        case LEFT:
          _switchResult = (this.inflatedWidth - this.deflatedWidth);
          break;
        case RIGHT:
          _switchResult = 0;
          break;
        case TOP:
        case BOTTOM:
          _switchResult = (0.5 * (this.inflatedWidth - this.deflatedWidth));
          break;
        default:
          _switchResult = 0;
          break;
      }
    } else {
      _switchResult = 0;
    }
    double _minus = (_layoutX - _switchResult);
    double _layoutY = this.host.getLayoutY();
    double _switchResult_1 = (double) 0;
    Side _placementHint_1 = this.host.getPlacementHint();
    if (_placementHint_1 != null) {
      switch (_placementHint_1) {
        case TOP:
          _switchResult_1 = this.inflatedHeight;
          break;
        case BOTTOM:
          _switchResult_1 = 0;
          break;
        case LEFT:
        case RIGHT:
          _switchResult_1 = (0.5 * this.inflatedHeight);
          break;
        default:
          _switchResult_1 = 0;
          break;
      }
    } else {
      _switchResult_1 = 0;
    }
    double _minus_1 = (_layoutY - _switchResult_1);
    return new Point2D(_minus, _minus_1);
  }
  
  protected Point2D getDeflatedHostPosition() {
    double _layoutX = this.host.getLayoutX();
    double _switchResult = (double) 0;
    Side _placementHint = this.host.getPlacementHint();
    if (_placementHint != null) {
      switch (_placementHint) {
        case LEFT:
          _switchResult = (this.inflatedWidth - this.deflatedWidth);
          break;
        case RIGHT:
          _switchResult = 0;
          break;
        case TOP:
        case BOTTOM:
          _switchResult = (0.5 * (this.inflatedWidth - this.deflatedWidth));
          break;
        default:
          _switchResult = 0;
          break;
      }
    } else {
      _switchResult = 0;
    }
    double _plus = (_layoutX + _switchResult);
    double _layoutY = this.host.getLayoutY();
    double _switchResult_1 = (double) 0;
    Side _placementHint_1 = this.host.getPlacementHint();
    if (_placementHint_1 != null) {
      switch (_placementHint_1) {
        case TOP:
          _switchResult_1 = this.inflatedHeight;
          break;
        case BOTTOM:
          _switchResult_1 = 0;
          break;
        case LEFT:
        case RIGHT:
          _switchResult_1 = (0.5 * this.inflatedHeight);
          break;
        default:
          _switchResult_1 = 0;
          break;
      }
    } else {
      _switchResult_1 = 0;
    }
    double _plus_1 = (_layoutY + _switchResult_1);
    return new Point2D(_plus, _plus_1);
  }
  
  protected Dimension2D calculateSize(final Pane node) {
    Dimension2D _xblockexpression = null;
    {
      node.autosize();
      double _width = node.getWidth();
      double _height = node.getHeight();
      _xblockexpression = new Dimension2D(_width, _height);
    }
    return _xblockexpression;
  }
}
