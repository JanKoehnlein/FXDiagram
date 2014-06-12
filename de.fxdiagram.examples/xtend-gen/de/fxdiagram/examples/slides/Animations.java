package de.fxdiagram.examples.slides;

import de.fxdiagram.core.extensions.DurationExtensions;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Animations {
  public static Timeline dangle(final Node creature) {
    Timeline _xblockexpression = null;
    {
      Rotate _rotate = new Rotate();
      final Procedure1<Rotate> _function = new Procedure1<Rotate>() {
        public void apply(final Rotate it) {
          Point3D _point3D = new Point3D(0, 0, 1);
          it.setAxis(_point3D);
        }
      };
      final Rotate transform = ObjectExtensions.<Rotate>operator_doubleArrow(_rotate, _function);
      ObservableList<Transform> _transforms = creature.getTransforms();
      _transforms.add(transform);
      Timeline _timeline = new Timeline();
      final Procedure1<Timeline> _function_1 = new Procedure1<Timeline>() {
        public void apply(final Timeline it) {
          IntegerRange _upTo = new IntegerRange(0, 10);
          for (final Integer i : _upTo) {
            ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
            Duration _millis = DurationExtensions.millis(130);
            Duration _multiply = DurationExtensions.operator_multiply((i).intValue(), _millis);
            DoubleProperty _angleProperty = transform.angleProperty();
            double _interpolateAngle = Animations.interpolateAngle(((i).intValue() / 10.0));
            KeyValue _keyValue = new <Number>KeyValue(_angleProperty, Double.valueOf(_interpolateAngle));
            KeyFrame _keyFrame = new KeyFrame(_multiply, _keyValue);
            _keyFrames.add(_keyFrame);
          }
          it.setCycleCount((-1));
          it.setAutoReverse(true);
          double _random = Math.random();
          Duration _millis_1 = DurationExtensions.millis(1000);
          Duration _multiply_1 = DurationExtensions.operator_multiply(_random, _millis_1);
          it.setDelay(_multiply_1);
          it.play();
        }
      };
      _xblockexpression = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function_1);
    }
    return _xblockexpression;
  }
  
  protected static double interpolateAngle(final double alpha) {
    double _sin = Math.sin(((alpha * Math.PI) - (Math.PI / 2)));
    double _multiply = (10 * _sin);
    return (90 + _multiply);
  }
  
  public static FillTransition flicker(final Shape creature, final Color fromColor, final Color toColor) {
    FillTransition _fillTransition = new FillTransition();
    final Procedure1<FillTransition> _function = new Procedure1<FillTransition>() {
      public void apply(final FillTransition it) {
        it.setShape(creature);
        it.setFromValue(fromColor);
        it.setToValue(toColor);
        Duration _millis = DurationExtensions.millis(100);
        it.setDuration(_millis);
        it.setCycleCount((-1));
        double _random = Math.random();
        Duration _millis_1 = DurationExtensions.millis(2000);
        Duration _multiply = DurationExtensions.operator_multiply(_random, _millis_1);
        it.setDelay(_multiply);
        it.play();
      }
    };
    return ObjectExtensions.<FillTransition>operator_doubleArrow(_fillTransition, _function);
  }
  
  public static SequentialTransition breathe(final Shape creature, final Color fromColor, final Color toColor) {
    SequentialTransition _sequentialTransition = new SequentialTransition();
    final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
      public void apply(final SequentialTransition it) {
        ObservableList<Animation> _children = it.getChildren();
        ParallelTransition _parallelTransition = new ParallelTransition();
        final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
          public void apply(final ParallelTransition it) {
            ObservableList<Animation> _children = it.getChildren();
            ScaleTransition _scaleTransition = new ScaleTransition();
            final Procedure1<ScaleTransition> _function = new Procedure1<ScaleTransition>() {
              public void apply(final ScaleTransition it) {
                it.setFromX(1);
                it.setToX(1.15);
                it.setFromY(1);
                it.setToY(1.1);
                it.setNode(creature);
                Duration _millis = DurationExtensions.millis(1800);
                it.setDuration(_millis);
                Duration _millis_1 = DurationExtensions.millis(250);
                it.setDelay(_millis_1);
              }
            };
            ScaleTransition _doubleArrow = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function);
            _children.add(_doubleArrow);
            ObservableList<Animation> _children_1 = it.getChildren();
            FillTransition _fillTransition = new FillTransition();
            final Procedure1<FillTransition> _function_1 = new Procedure1<FillTransition>() {
              public void apply(final FillTransition it) {
                it.setShape(creature);
                it.setFromValue(toColor);
                it.setToValue(fromColor);
                Duration _millis = DurationExtensions.millis(1800);
                it.setDuration(_millis);
                Duration _millis_1 = DurationExtensions.millis(250);
                it.setDelay(_millis_1);
                it.play();
              }
            };
            FillTransition _doubleArrow_1 = ObjectExtensions.<FillTransition>operator_doubleArrow(_fillTransition, _function_1);
            _children_1.add(_doubleArrow_1);
          }
        };
        ParallelTransition _doubleArrow = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
        _children.add(_doubleArrow);
        ObservableList<Animation> _children_1 = it.getChildren();
        ParallelTransition _parallelTransition_1 = new ParallelTransition();
        final Procedure1<ParallelTransition> _function_1 = new Procedure1<ParallelTransition>() {
          public void apply(final ParallelTransition it) {
            ObservableList<Animation> _children = it.getChildren();
            ScaleTransition _scaleTransition = new ScaleTransition();
            final Procedure1<ScaleTransition> _function = new Procedure1<ScaleTransition>() {
              public void apply(final ScaleTransition it) {
                it.setFromX(1.15);
                it.setToX(1);
                it.setFromY(1.1);
                it.setToY(1);
                it.setNode(creature);
                Duration _millis = DurationExtensions.millis(2500);
                it.setDuration(_millis);
                Duration _millis_1 = DurationExtensions.millis(300);
                it.setDelay(_millis_1);
              }
            };
            ScaleTransition _doubleArrow = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function);
            _children.add(_doubleArrow);
            ObservableList<Animation> _children_1 = it.getChildren();
            FillTransition _fillTransition = new FillTransition();
            final Procedure1<FillTransition> _function_1 = new Procedure1<FillTransition>() {
              public void apply(final FillTransition it) {
                it.setShape(creature);
                it.setFromValue(fromColor);
                it.setToValue(toColor);
                Duration _millis = DurationExtensions.millis(2500);
                it.setDuration(_millis);
                Duration _millis_1 = DurationExtensions.millis(300);
                it.setDelay(_millis_1);
                it.play();
              }
            };
            FillTransition _doubleArrow_1 = ObjectExtensions.<FillTransition>operator_doubleArrow(_fillTransition, _function_1);
            _children_1.add(_doubleArrow_1);
          }
        };
        ParallelTransition _doubleArrow_1 = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition_1, _function_1);
        _children_1.add(_doubleArrow_1);
        double _random = Math.random();
        Duration _millis = DurationExtensions.millis(4000);
        Duration _multiply = DurationExtensions.operator_multiply(_random, _millis);
        it.setDelay(_multiply);
        it.setCycleCount((-1));
        it.play();
      }
    };
    return ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
  }
  
  public static SequentialTransition crawl(final Node creature) {
    SequentialTransition _xblockexpression = null;
    {
      double _random = Math.random();
      double _multiply = (10 * _random);
      final double stepSize = (20 + _multiply);
      double _random_1 = Math.random();
      double _multiply_1 = (4 * _random_1);
      double _plus = (2 + _multiply_1);
      final int numSteps = ((int) _plus);
      SequentialTransition _sequentialTransition = new SequentialTransition();
      final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
        public void apply(final SequentialTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          SequentialTransition _crawlOneWay = Animations.crawlOneWay(creature, stepSize, numSteps);
          _children.add(_crawlOneWay);
          ObservableList<Animation> _children_1 = it.getChildren();
          SequentialTransition _crawlOneWay_1 = Animations.crawlOneWay(creature, (-stepSize), numSteps);
          final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
            public void apply(final SequentialTransition it) {
              double _random = Math.random();
              Duration _seconds = DurationExtensions.seconds(1);
              Duration _multiply = DurationExtensions.operator_multiply(_random, _seconds);
              it.setDelay(_multiply);
            }
          };
          SequentialTransition _doubleArrow = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_crawlOneWay_1, _function);
          _children_1.add(_doubleArrow);
          it.setCycleCount((-1));
          double _random = Math.random();
          Duration _seconds = DurationExtensions.seconds(4);
          Duration _multiply = DurationExtensions.operator_multiply(_random, _seconds);
          it.setDelay(_multiply);
          it.play();
        }
      };
      _xblockexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
    }
    return _xblockexpression;
  }
  
  public static SequentialTransition crawlOneWay(final Node creature, final double stepSize, final int numSteps) {
    SequentialTransition _xblockexpression = null;
    {
      double _random = Math.random();
      Duration _millis = DurationExtensions.millis(300);
      Duration _multiply = DurationExtensions.operator_multiply(_random, _millis);
      Duration _millis_1 = DurationExtensions.millis(600);
      final Duration stepDuration = DurationExtensions.operator_plus(_multiply, _millis_1);
      SequentialTransition _sequentialTransition = new SequentialTransition();
      final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
        public void apply(final SequentialTransition it) {
          IntegerRange _upTo = new IntegerRange(1, numSteps);
          for (final Integer i : _upTo) {
            {
              ObservableList<Animation> _children = it.getChildren();
              ParallelTransition _parallelTransition = new ParallelTransition();
              final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
                public void apply(final ParallelTransition it) {
                  ObservableList<Animation> _children = it.getChildren();
                  ScaleTransition _scaleTransition = new ScaleTransition();
                  final Procedure1<ScaleTransition> _function = new Procedure1<ScaleTransition>() {
                    public void apply(final ScaleTransition it) {
                      it.setNode(creature);
                      it.setFromX(1);
                      it.setToX(1.2);
                      it.setFromY(1);
                      it.setToY(0.9);
                      Duration _multiply = DurationExtensions.operator_multiply(0.8, stepDuration);
                      it.setDuration(_multiply);
                      Duration _multiply_1 = DurationExtensions.operator_multiply(0.2, stepDuration);
                      it.setDelay(_multiply_1);
                    }
                  };
                  ScaleTransition _doubleArrow = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function);
                  _children.add(_doubleArrow);
                  ObservableList<Animation> _children_1 = it.getChildren();
                  TranslateTransition _translateTransition = new TranslateTransition();
                  final Procedure1<TranslateTransition> _function_1 = new Procedure1<TranslateTransition>() {
                    public void apply(final TranslateTransition it) {
                      it.setNode(creature);
                      double _rotate = creature.getRotate();
                      double _radians = Math.toRadians(_rotate);
                      double _cos = Math.cos(_radians);
                      double _multiply = (stepSize * _cos);
                      it.setByX(_multiply);
                      double _rotate_1 = creature.getRotate();
                      double _radians_1 = Math.toRadians(_rotate_1);
                      double _sin = Math.sin(_radians_1);
                      double _multiply_1 = (stepSize * _sin);
                      it.setByY(_multiply_1);
                      Duration _multiply_2 = DurationExtensions.operator_multiply(0.8, stepDuration);
                      it.setDuration(_multiply_2);
                      Duration _multiply_3 = DurationExtensions.operator_multiply(0.2, stepDuration);
                      it.setDelay(_multiply_3);
                    }
                  };
                  TranslateTransition _doubleArrow_1 = ObjectExtensions.<TranslateTransition>operator_doubleArrow(_translateTransition, _function_1);
                  _children_1.add(_doubleArrow_1);
                }
              };
              ParallelTransition _doubleArrow = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
              _children.add(_doubleArrow);
              ObservableList<Animation> _children_1 = it.getChildren();
              ParallelTransition _parallelTransition_1 = new ParallelTransition();
              final Procedure1<ParallelTransition> _function_1 = new Procedure1<ParallelTransition>() {
                public void apply(final ParallelTransition it) {
                  ObservableList<Animation> _children = it.getChildren();
                  ScaleTransition _scaleTransition = new ScaleTransition();
                  final Procedure1<ScaleTransition> _function = new Procedure1<ScaleTransition>() {
                    public void apply(final ScaleTransition it) {
                      it.setNode(creature);
                      it.setFromX(1.2);
                      it.setToX(1);
                      it.setFromY(0.9);
                      it.setToY(1);
                      Duration _multiply = DurationExtensions.operator_multiply(0.6, stepDuration);
                      it.setDuration(_multiply);
                      Duration _multiply_1 = DurationExtensions.operator_multiply(0.4, stepDuration);
                      it.setDelay(_multiply_1);
                    }
                  };
                  ScaleTransition _doubleArrow = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function);
                  _children.add(_doubleArrow);
                  ObservableList<Animation> _children_1 = it.getChildren();
                  TranslateTransition _translateTransition = new TranslateTransition();
                  final Procedure1<TranslateTransition> _function_1 = new Procedure1<TranslateTransition>() {
                    public void apply(final TranslateTransition it) {
                      it.setNode(creature);
                      double _rotate = creature.getRotate();
                      double _radians = Math.toRadians(_rotate);
                      double _cos = Math.cos(_radians);
                      double _multiply = (stepSize * _cos);
                      it.setByX(_multiply);
                      double _rotate_1 = creature.getRotate();
                      double _radians_1 = Math.toRadians(_rotate_1);
                      double _sin = Math.sin(_radians_1);
                      double _multiply_1 = (stepSize * _sin);
                      it.setByY(_multiply_1);
                      Duration _multiply_2 = DurationExtensions.operator_multiply(0.6, stepDuration);
                      it.setDuration(_multiply_2);
                      Duration _multiply_3 = DurationExtensions.operator_multiply(0.4, stepDuration);
                      it.setDelay(_multiply_3);
                    }
                  };
                  TranslateTransition _doubleArrow_1 = ObjectExtensions.<TranslateTransition>operator_doubleArrow(_translateTransition, _function_1);
                  _children_1.add(_doubleArrow_1);
                }
              };
              ParallelTransition _doubleArrow_1 = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition_1, _function_1);
              _children_1.add(_doubleArrow_1);
            }
          }
        }
      };
      _xblockexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
    }
    return _xblockexpression;
  }
  
  public static ParallelTransition orbit(final Node creature, final double radiusX, final double radiusY) {
    ParallelTransition _parallelTransition = new ParallelTransition();
    final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
      public void apply(final ParallelTransition it) {
        ObservableList<Animation> _children = it.getChildren();
        RotateTransition _rotateTransition = new RotateTransition();
        final Procedure1<RotateTransition> _function = new Procedure1<RotateTransition>() {
          public void apply(final RotateTransition it) {
            it.setNode(creature);
            it.setFromAngle(0);
            it.setToAngle(360);
            Point3D _point3D = new Point3D(0, 1, 0);
            it.setAxis(_point3D);
            it.setCycleCount((-1));
            Duration _seconds = DurationExtensions.seconds(2);
            it.setDuration(_seconds);
            it.setInterpolator(Interpolator.LINEAR);
          }
        };
        RotateTransition _doubleArrow = ObjectExtensions.<RotateTransition>operator_doubleArrow(_rotateTransition, _function);
        _children.add(_doubleArrow);
        ObservableList<Animation> _children_1 = it.getChildren();
        PathTransition _pathTransition = new PathTransition();
        final Procedure1<PathTransition> _function_1 = new Procedure1<PathTransition>() {
          public void apply(final PathTransition it) {
            it.setNode(creature);
            Path _path = new Path();
            final Procedure1<Path> _function = new Procedure1<Path>() {
              public void apply(final Path it) {
                ObservableList<PathElement> _elements = it.getElements();
                MoveTo _moveTo = new MoveTo((-radiusX), 0);
                _elements.add(_moveTo);
                ObservableList<PathElement> _elements_1 = it.getElements();
                CubicCurveTo _cubicCurveTo = new CubicCurveTo();
                final Procedure1<CubicCurveTo> _function = new Procedure1<CubicCurveTo>() {
                  public void apply(final CubicCurveTo it) {
                    it.setControlX1((-radiusX));
                    it.setControlY1((0.5 * radiusY));
                    it.setControlX2(((-0.5) * radiusX));
                    it.setControlY2(radiusY);
                    it.setX(0);
                    it.setY(radiusY);
                  }
                };
                CubicCurveTo _doubleArrow = ObjectExtensions.<CubicCurveTo>operator_doubleArrow(_cubicCurveTo, _function);
                _elements_1.add(_doubleArrow);
                ObservableList<PathElement> _elements_2 = it.getElements();
                CubicCurveTo _cubicCurveTo_1 = new CubicCurveTo();
                final Procedure1<CubicCurveTo> _function_1 = new Procedure1<CubicCurveTo>() {
                  public void apply(final CubicCurveTo it) {
                    it.setControlX1((0.5 * radiusX));
                    it.setControlY1(radiusY);
                    it.setControlX2(radiusX);
                    it.setControlY2((0.5 * radiusY));
                    it.setX(radiusX);
                    it.setY(0);
                  }
                };
                CubicCurveTo _doubleArrow_1 = ObjectExtensions.<CubicCurveTo>operator_doubleArrow(_cubicCurveTo_1, _function_1);
                _elements_2.add(_doubleArrow_1);
                ObservableList<PathElement> _elements_3 = it.getElements();
                CubicCurveTo _cubicCurveTo_2 = new CubicCurveTo();
                final Procedure1<CubicCurveTo> _function_2 = new Procedure1<CubicCurveTo>() {
                  public void apply(final CubicCurveTo it) {
                    it.setControlX1(radiusX);
                    it.setControlY1(((-0.5) * radiusY));
                    it.setControlX2((0.5 * radiusX));
                    it.setControlY2((-radiusY));
                    it.setX(0);
                    it.setY((-radiusY));
                  }
                };
                CubicCurveTo _doubleArrow_2 = ObjectExtensions.<CubicCurveTo>operator_doubleArrow(_cubicCurveTo_2, _function_2);
                _elements_3.add(_doubleArrow_2);
                ObservableList<PathElement> _elements_4 = it.getElements();
                CubicCurveTo _cubicCurveTo_3 = new CubicCurveTo();
                final Procedure1<CubicCurveTo> _function_3 = new Procedure1<CubicCurveTo>() {
                  public void apply(final CubicCurveTo it) {
                    it.setControlX1(((-0.5) * radiusX));
                    it.setControlY1((-radiusY));
                    it.setControlX2((-radiusX));
                    it.setControlY2(((-0.5) * radiusY));
                    it.setX((-radiusX));
                    it.setY(0);
                  }
                };
                CubicCurveTo _doubleArrow_3 = ObjectExtensions.<CubicCurveTo>operator_doubleArrow(_cubicCurveTo_3, _function_3);
                _elements_4.add(_doubleArrow_3);
              }
            };
            Path _doubleArrow = ObjectExtensions.<Path>operator_doubleArrow(_path, _function);
            it.setPath(_doubleArrow);
            Duration _seconds = DurationExtensions.seconds(30);
            it.setDuration(_seconds);
            it.setInterpolator(Interpolator.LINEAR);
            it.setCycleCount((-1));
          }
        };
        PathTransition _doubleArrow_1 = ObjectExtensions.<PathTransition>operator_doubleArrow(_pathTransition, _function_1);
        _children_1.add(_doubleArrow_1);
        it.setCycleCount((-1));
        it.play();
      }
    };
    return ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
  }
  
  public static RotateTransition spin(final Node creature) {
    RotateTransition _rotateTransition = new RotateTransition();
    final Procedure1<RotateTransition> _function = new Procedure1<RotateTransition>() {
      public void apply(final RotateTransition it) {
        it.setNode(creature);
        it.setFromAngle(0);
        it.setToAngle(360);
        Point3D _point3D = new Point3D(0.7, 0.8, 1);
        it.setAxis(_point3D);
        it.setCycleCount((-1));
        Duration _seconds = DurationExtensions.seconds(7);
        it.setDuration(_seconds);
        it.setInterpolator(Interpolator.LINEAR);
        it.play();
      }
    };
    return ObjectExtensions.<RotateTransition>operator_doubleArrow(_rotateTransition, _function);
  }
  
  public static SequentialTransition warp(final Node creature, final double distance) {
    SequentialTransition _sequentialTransition = new SequentialTransition();
    final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
      public void apply(final SequentialTransition it) {
        ObservableList<Animation> _children = it.getChildren();
        SequentialTransition _warpOut = Animations.warpOut(creature, distance);
        final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
          public void apply(final SequentialTransition it) {
            Duration _seconds = DurationExtensions.seconds(2);
            double _random = Math.random();
            Duration _seconds_1 = DurationExtensions.seconds(2);
            Duration _multiply = DurationExtensions.operator_multiply(_random, _seconds_1);
            Duration _plus = DurationExtensions.operator_plus(_seconds, _multiply);
            it.setDelay(_plus);
          }
        };
        SequentialTransition _doubleArrow = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_warpOut, _function);
        _children.add(_doubleArrow);
        ObservableList<Animation> _children_1 = it.getChildren();
        SequentialTransition _warpIn = Animations.warpIn(creature, (-distance));
        final Procedure1<SequentialTransition> _function_1 = new Procedure1<SequentialTransition>() {
          public void apply(final SequentialTransition it) {
            Duration _seconds = DurationExtensions.seconds(4);
            double _random = Math.random();
            Duration _seconds_1 = DurationExtensions.seconds(2);
            Duration _multiply = DurationExtensions.operator_multiply(_random, _seconds_1);
            Duration _plus = DurationExtensions.operator_plus(_seconds, _multiply);
            it.setDelay(_plus);
          }
        };
        SequentialTransition _doubleArrow_1 = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_warpIn, _function_1);
        _children_1.add(_doubleArrow_1);
        it.setCycleCount((-1));
        it.play();
      }
    };
    return ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
  }
  
  protected static SequentialTransition warpOut(final Node creature, final double stepSize) {
    SequentialTransition _xblockexpression = null;
    {
      double _random = Math.random();
      Duration _millis = DurationExtensions.millis(300);
      Duration _multiply = DurationExtensions.operator_multiply(_random, _millis);
      Duration _millis_1 = DurationExtensions.millis(600);
      final Duration stepDuration = DurationExtensions.operator_plus(_multiply, _millis_1);
      SequentialTransition _sequentialTransition = new SequentialTransition();
      final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
        public void apply(final SequentialTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          ParallelTransition _parallelTransition = new ParallelTransition();
          final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
            public void apply(final ParallelTransition it) {
              ObservableList<Animation> _children = it.getChildren();
              ScaleTransition _scaleTransition = new ScaleTransition();
              final Procedure1<ScaleTransition> _function = new Procedure1<ScaleTransition>() {
                public void apply(final ScaleTransition it) {
                  it.setNode(creature);
                  it.setFromX(1);
                  it.setToX(1.8);
                  it.setFromY(1);
                  it.setToY(0.3);
                  Duration _multiply = DurationExtensions.operator_multiply(0.8, stepDuration);
                  it.setDuration(_multiply);
                  Duration _multiply_1 = DurationExtensions.operator_multiply(0.2, stepDuration);
                  it.setDelay(_multiply_1);
                }
              };
              ScaleTransition _doubleArrow = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function);
              _children.add(_doubleArrow);
              ObservableList<Animation> _children_1 = it.getChildren();
              TranslateTransition _translateTransition = new TranslateTransition();
              final Procedure1<TranslateTransition> _function_1 = new Procedure1<TranslateTransition>() {
                public void apply(final TranslateTransition it) {
                  it.setNode(creature);
                  double _signum = Math.signum(stepSize);
                  double _multiply = (_signum * 0.8);
                  double _prefWidth = creature.prefWidth((-1));
                  double _multiply_1 = (_multiply * _prefWidth);
                  double _rotate = creature.getRotate();
                  double _radians = Math.toRadians(_rotate);
                  double _cos = Math.cos(_radians);
                  double _multiply_2 = (_multiply_1 * _cos);
                  it.setByX(_multiply_2);
                  double _signum_1 = Math.signum(stepSize);
                  double _multiply_3 = (_signum_1 * 0.8);
                  double _prefWidth_1 = creature.prefWidth((-1));
                  double _multiply_4 = (_multiply_3 * _prefWidth_1);
                  double _rotate_1 = creature.getRotate();
                  double _radians_1 = Math.toRadians(_rotate_1);
                  double _sin = Math.sin(_radians_1);
                  double _multiply_5 = (_multiply_4 * _sin);
                  it.setByY(_multiply_5);
                  Duration _multiply_6 = DurationExtensions.operator_multiply(0.8, stepDuration);
                  it.setDuration(_multiply_6);
                  Duration _multiply_7 = DurationExtensions.operator_multiply(0.2, stepDuration);
                  it.setDelay(_multiply_7);
                }
              };
              TranslateTransition _doubleArrow_1 = ObjectExtensions.<TranslateTransition>operator_doubleArrow(_translateTransition, _function_1);
              _children_1.add(_doubleArrow_1);
            }
          };
          ParallelTransition _doubleArrow = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
          _children.add(_doubleArrow);
          ObservableList<Animation> _children_1 = it.getChildren();
          TranslateTransition _translateTransition = new TranslateTransition();
          final Procedure1<TranslateTransition> _function_1 = new Procedure1<TranslateTransition>() {
            public void apply(final TranslateTransition it) {
              it.setNode(creature);
              double _rotate = creature.getRotate();
              double _radians = Math.toRadians(_rotate);
              double _cos = Math.cos(_radians);
              double _multiply = (stepSize * _cos);
              it.setByX(_multiply);
              double _rotate_1 = creature.getRotate();
              double _radians_1 = Math.toRadians(_rotate_1);
              double _sin = Math.sin(_radians_1);
              double _multiply_1 = (stepSize * _sin);
              it.setByY(_multiply_1);
              Duration _multiply_2 = DurationExtensions.operator_multiply(0.2, stepDuration);
              it.setDuration(_multiply_2);
              Duration _multiply_3 = DurationExtensions.operator_multiply(0.2, stepDuration);
              it.setDelay(_multiply_3);
            }
          };
          TranslateTransition _doubleArrow_1 = ObjectExtensions.<TranslateTransition>operator_doubleArrow(_translateTransition, _function_1);
          _children_1.add(_doubleArrow_1);
          ObservableList<Animation> _children_2 = it.getChildren();
          ScaleTransition _scaleTransition = new ScaleTransition();
          final Procedure1<ScaleTransition> _function_2 = new Procedure1<ScaleTransition>() {
            public void apply(final ScaleTransition it) {
              it.setToX(1);
              it.setToY(1);
              Duration _seconds = DurationExtensions.seconds(0);
              it.setDuration(_seconds);
            }
          };
          ScaleTransition _doubleArrow_2 = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function_2);
          _children_2.add(_doubleArrow_2);
        }
      };
      _xblockexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
    }
    return _xblockexpression;
  }
  
  protected static SequentialTransition warpIn(final Node creature, final double stepSize) {
    SequentialTransition _xblockexpression = null;
    {
      double _random = Math.random();
      Duration _millis = DurationExtensions.millis(300);
      Duration _multiply = DurationExtensions.operator_multiply(_random, _millis);
      Duration _millis_1 = DurationExtensions.millis(600);
      final Duration stepDuration = DurationExtensions.operator_plus(_multiply, _millis_1);
      SequentialTransition _sequentialTransition = new SequentialTransition();
      final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
        public void apply(final SequentialTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          ScaleTransition _scaleTransition = new ScaleTransition();
          final Procedure1<ScaleTransition> _function = new Procedure1<ScaleTransition>() {
            public void apply(final ScaleTransition it) {
              it.setToX(1.8);
              it.setToY(0.3);
              Duration _seconds = DurationExtensions.seconds(0);
              it.setDuration(_seconds);
            }
          };
          ScaleTransition _doubleArrow = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function);
          _children.add(_doubleArrow);
          ObservableList<Animation> _children_1 = it.getChildren();
          TranslateTransition _translateTransition = new TranslateTransition();
          final Procedure1<TranslateTransition> _function_1 = new Procedure1<TranslateTransition>() {
            public void apply(final TranslateTransition it) {
              it.setNode(creature);
              double _rotate = creature.getRotate();
              double _radians = Math.toRadians(_rotate);
              double _cos = Math.cos(_radians);
              double _multiply = (stepSize * _cos);
              it.setByX(_multiply);
              double _rotate_1 = creature.getRotate();
              double _radians_1 = Math.toRadians(_rotate_1);
              double _sin = Math.sin(_radians_1);
              double _multiply_1 = (stepSize * _sin);
              it.setByY(_multiply_1);
              Duration _multiply_2 = DurationExtensions.operator_multiply(0.2, stepDuration);
              it.setDuration(_multiply_2);
              Duration _multiply_3 = DurationExtensions.operator_multiply(0.2, stepDuration);
              it.setDelay(_multiply_3);
            }
          };
          TranslateTransition _doubleArrow_1 = ObjectExtensions.<TranslateTransition>operator_doubleArrow(_translateTransition, _function_1);
          _children_1.add(_doubleArrow_1);
          ObservableList<Animation> _children_2 = it.getChildren();
          ParallelTransition _parallelTransition = new ParallelTransition();
          final Procedure1<ParallelTransition> _function_2 = new Procedure1<ParallelTransition>() {
            public void apply(final ParallelTransition it) {
              ObservableList<Animation> _children = it.getChildren();
              ScaleTransition _scaleTransition = new ScaleTransition();
              final Procedure1<ScaleTransition> _function = new Procedure1<ScaleTransition>() {
                public void apply(final ScaleTransition it) {
                  it.setNode(creature);
                  it.setFromX(1.8);
                  it.setToX(1);
                  it.setFromY(0.6);
                  it.setToY(1);
                  Duration _multiply = DurationExtensions.operator_multiply(0.8, stepDuration);
                  it.setDuration(_multiply);
                }
              };
              ScaleTransition _doubleArrow = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function);
              _children.add(_doubleArrow);
              ObservableList<Animation> _children_1 = it.getChildren();
              TranslateTransition _translateTransition = new TranslateTransition();
              final Procedure1<TranslateTransition> _function_1 = new Procedure1<TranslateTransition>() {
                public void apply(final TranslateTransition it) {
                  it.setNode(creature);
                  double _signum = Math.signum(stepSize);
                  double _multiply = (_signum * 0.8);
                  double _prefWidth = creature.prefWidth((-1));
                  double _multiply_1 = (_multiply * _prefWidth);
                  double _rotate = creature.getRotate();
                  double _radians = Math.toRadians(_rotate);
                  double _cos = Math.cos(_radians);
                  double _multiply_2 = (_multiply_1 * _cos);
                  it.setByX(_multiply_2);
                  double _signum_1 = Math.signum(stepSize);
                  double _multiply_3 = (_signum_1 * 0.8);
                  double _prefWidth_1 = creature.prefWidth((-1));
                  double _multiply_4 = (_multiply_3 * _prefWidth_1);
                  double _rotate_1 = creature.getRotate();
                  double _radians_1 = Math.toRadians(_rotate_1);
                  double _sin = Math.sin(_radians_1);
                  double _multiply_5 = (_multiply_4 * _sin);
                  it.setByY(_multiply_5);
                  Duration _multiply_6 = DurationExtensions.operator_multiply(0.8, stepDuration);
                  it.setDuration(_multiply_6);
                }
              };
              TranslateTransition _doubleArrow_1 = ObjectExtensions.<TranslateTransition>operator_doubleArrow(_translateTransition, _function_1);
              _children_1.add(_doubleArrow_1);
            }
          };
          ParallelTransition _doubleArrow_2 = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_2);
          _children_2.add(_doubleArrow_2);
        }
      };
      _xblockexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
    }
    return _xblockexpression;
  }
}
