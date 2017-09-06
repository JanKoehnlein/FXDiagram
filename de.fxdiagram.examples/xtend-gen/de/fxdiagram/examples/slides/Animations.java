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
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Some fancy animations for elements on a slide.
 */
@SuppressWarnings("all")
public class Animations {
  public static Timeline dangle(final Node creature) {
    Timeline _xblockexpression = null;
    {
      Rotate _rotate = new Rotate();
      final Procedure1<Rotate> _function = (Rotate it) -> {
        Point3D _point3D = new Point3D(0, 0, 1);
        it.setAxis(_point3D);
      };
      final Rotate transform = ObjectExtensions.<Rotate>operator_doubleArrow(_rotate, _function);
      creature.getTransforms().add(transform);
      Timeline _timeline = new Timeline();
      final Procedure1<Timeline> _function_1 = (Timeline it) -> {
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
    final Procedure1<FillTransition> _function = (FillTransition it) -> {
      it.setShape(creature);
      it.setFromValue(fromColor);
      it.setToValue(toColor);
      it.setDuration(DurationExtensions.millis(100));
      it.setCycleCount((-1));
      double _random = Math.random();
      Duration _millis = DurationExtensions.millis(2000);
      Duration _multiply = DurationExtensions.operator_multiply(_random, _millis);
      it.setDelay(_multiply);
      it.play();
    };
    return ObjectExtensions.<FillTransition>operator_doubleArrow(_fillTransition, _function);
  }
  
  public static SequentialTransition breathe(final Shape creature, final Color fromColor, final Color toColor) {
    SequentialTransition _sequentialTransition = new SequentialTransition();
    final Procedure1<SequentialTransition> _function = (SequentialTransition it) -> {
      ObservableList<Animation> _children = it.getChildren();
      ParallelTransition _parallelTransition = new ParallelTransition();
      final Procedure1<ParallelTransition> _function_1 = (ParallelTransition it_1) -> {
        ObservableList<Animation> _children_1 = it_1.getChildren();
        ScaleTransition _scaleTransition = new ScaleTransition();
        final Procedure1<ScaleTransition> _function_2 = (ScaleTransition it_2) -> {
          it_2.setFromX(1);
          it_2.setToX(1.15);
          it_2.setFromY(1);
          it_2.setToY(1.1);
          it_2.setNode(creature);
          it_2.setDuration(DurationExtensions.millis(1800));
          it_2.setDelay(DurationExtensions.millis(250));
        };
        ScaleTransition _doubleArrow = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function_2);
        _children_1.add(_doubleArrow);
        ObservableList<Animation> _children_2 = it_1.getChildren();
        FillTransition _fillTransition = new FillTransition();
        final Procedure1<FillTransition> _function_3 = (FillTransition it_2) -> {
          it_2.setShape(creature);
          it_2.setFromValue(toColor);
          it_2.setToValue(fromColor);
          it_2.setDuration(DurationExtensions.millis(1800));
          it_2.setDelay(DurationExtensions.millis(250));
          it_2.play();
        };
        FillTransition _doubleArrow_1 = ObjectExtensions.<FillTransition>operator_doubleArrow(_fillTransition, _function_3);
        _children_2.add(_doubleArrow_1);
      };
      ParallelTransition _doubleArrow = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_1);
      _children.add(_doubleArrow);
      ObservableList<Animation> _children_1 = it.getChildren();
      ParallelTransition _parallelTransition_1 = new ParallelTransition();
      final Procedure1<ParallelTransition> _function_2 = (ParallelTransition it_1) -> {
        ObservableList<Animation> _children_2 = it_1.getChildren();
        ScaleTransition _scaleTransition = new ScaleTransition();
        final Procedure1<ScaleTransition> _function_3 = (ScaleTransition it_2) -> {
          it_2.setFromX(1.15);
          it_2.setToX(1);
          it_2.setFromY(1.1);
          it_2.setToY(1);
          it_2.setNode(creature);
          it_2.setDuration(DurationExtensions.millis(2500));
          it_2.setDelay(DurationExtensions.millis(300));
        };
        ScaleTransition _doubleArrow_1 = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function_3);
        _children_2.add(_doubleArrow_1);
        ObservableList<Animation> _children_3 = it_1.getChildren();
        FillTransition _fillTransition = new FillTransition();
        final Procedure1<FillTransition> _function_4 = (FillTransition it_2) -> {
          it_2.setShape(creature);
          it_2.setFromValue(fromColor);
          it_2.setToValue(toColor);
          it_2.setDuration(DurationExtensions.millis(2500));
          it_2.setDelay(DurationExtensions.millis(300));
          it_2.play();
        };
        FillTransition _doubleArrow_2 = ObjectExtensions.<FillTransition>operator_doubleArrow(_fillTransition, _function_4);
        _children_3.add(_doubleArrow_2);
      };
      ParallelTransition _doubleArrow_1 = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition_1, _function_2);
      _children_1.add(_doubleArrow_1);
      double _random = Math.random();
      Duration _millis = DurationExtensions.millis(4000);
      Duration _multiply = DurationExtensions.operator_multiply(_random, _millis);
      it.setDelay(_multiply);
      it.setCycleCount((-1));
      it.play();
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
      final Procedure1<SequentialTransition> _function = (SequentialTransition it) -> {
        ObservableList<Animation> _children = it.getChildren();
        SequentialTransition _crawlOneWay = Animations.crawlOneWay(creature, stepSize, numSteps);
        _children.add(_crawlOneWay);
        ObservableList<Animation> _children_1 = it.getChildren();
        SequentialTransition _crawlOneWay_1 = Animations.crawlOneWay(creature, (-stepSize), numSteps);
        final Procedure1<SequentialTransition> _function_1 = (SequentialTransition it_1) -> {
          double _random_2 = Math.random();
          Duration _seconds = DurationExtensions.seconds(1);
          Duration _multiply_2 = DurationExtensions.operator_multiply(_random_2, _seconds);
          it_1.setDelay(_multiply_2);
        };
        SequentialTransition _doubleArrow = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_crawlOneWay_1, _function_1);
        _children_1.add(_doubleArrow);
        it.setCycleCount((-1));
        double _random_2 = Math.random();
        Duration _seconds = DurationExtensions.seconds(4);
        Duration _multiply_2 = DurationExtensions.operator_multiply(_random_2, _seconds);
        it.setDelay(_multiply_2);
        it.play();
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
      final Procedure1<SequentialTransition> _function = (SequentialTransition it) -> {
        IntegerRange _upTo = new IntegerRange(1, numSteps);
        for (final Integer i : _upTo) {
          {
            ObservableList<Animation> _children = it.getChildren();
            ParallelTransition _parallelTransition = new ParallelTransition();
            final Procedure1<ParallelTransition> _function_1 = (ParallelTransition it_1) -> {
              ObservableList<Animation> _children_1 = it_1.getChildren();
              ScaleTransition _scaleTransition = new ScaleTransition();
              final Procedure1<ScaleTransition> _function_2 = (ScaleTransition it_2) -> {
                it_2.setNode(creature);
                it_2.setFromX(1);
                it_2.setToX(1.2);
                it_2.setFromY(1);
                it_2.setToY(0.9);
                Duration _multiply_1 = DurationExtensions.operator_multiply(0.8, stepDuration);
                it_2.setDuration(_multiply_1);
                Duration _multiply_2 = DurationExtensions.operator_multiply(0.2, stepDuration);
                it_2.setDelay(_multiply_2);
              };
              ScaleTransition _doubleArrow = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function_2);
              _children_1.add(_doubleArrow);
              ObservableList<Animation> _children_2 = it_1.getChildren();
              TranslateTransition _translateTransition = new TranslateTransition();
              final Procedure1<TranslateTransition> _function_3 = (TranslateTransition it_2) -> {
                it_2.setNode(creature);
                double _cos = Math.cos(Math.toRadians(creature.getRotate()));
                double _multiply_1 = (stepSize * _cos);
                it_2.setByX(_multiply_1);
                double _sin = Math.sin(Math.toRadians(creature.getRotate()));
                double _multiply_2 = (stepSize * _sin);
                it_2.setByY(_multiply_2);
                Duration _multiply_3 = DurationExtensions.operator_multiply(0.8, stepDuration);
                it_2.setDuration(_multiply_3);
                Duration _multiply_4 = DurationExtensions.operator_multiply(0.2, stepDuration);
                it_2.setDelay(_multiply_4);
              };
              TranslateTransition _doubleArrow_1 = ObjectExtensions.<TranslateTransition>operator_doubleArrow(_translateTransition, _function_3);
              _children_2.add(_doubleArrow_1);
            };
            ParallelTransition _doubleArrow = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_1);
            _children.add(_doubleArrow);
            ObservableList<Animation> _children_1 = it.getChildren();
            ParallelTransition _parallelTransition_1 = new ParallelTransition();
            final Procedure1<ParallelTransition> _function_2 = (ParallelTransition it_1) -> {
              ObservableList<Animation> _children_2 = it_1.getChildren();
              ScaleTransition _scaleTransition = new ScaleTransition();
              final Procedure1<ScaleTransition> _function_3 = (ScaleTransition it_2) -> {
                it_2.setNode(creature);
                it_2.setFromX(1.2);
                it_2.setToX(1);
                it_2.setFromY(0.9);
                it_2.setToY(1);
                Duration _multiply_1 = DurationExtensions.operator_multiply(0.6, stepDuration);
                it_2.setDuration(_multiply_1);
                Duration _multiply_2 = DurationExtensions.operator_multiply(0.4, stepDuration);
                it_2.setDelay(_multiply_2);
              };
              ScaleTransition _doubleArrow_1 = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function_3);
              _children_2.add(_doubleArrow_1);
              ObservableList<Animation> _children_3 = it_1.getChildren();
              TranslateTransition _translateTransition = new TranslateTransition();
              final Procedure1<TranslateTransition> _function_4 = (TranslateTransition it_2) -> {
                it_2.setNode(creature);
                double _cos = Math.cos(Math.toRadians(creature.getRotate()));
                double _multiply_1 = (stepSize * _cos);
                it_2.setByX(_multiply_1);
                double _sin = Math.sin(Math.toRadians(creature.getRotate()));
                double _multiply_2 = (stepSize * _sin);
                it_2.setByY(_multiply_2);
                Duration _multiply_3 = DurationExtensions.operator_multiply(0.6, stepDuration);
                it_2.setDuration(_multiply_3);
                Duration _multiply_4 = DurationExtensions.operator_multiply(0.4, stepDuration);
                it_2.setDelay(_multiply_4);
              };
              TranslateTransition _doubleArrow_2 = ObjectExtensions.<TranslateTransition>operator_doubleArrow(_translateTransition, _function_4);
              _children_3.add(_doubleArrow_2);
            };
            ParallelTransition _doubleArrow_1 = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition_1, _function_2);
            _children_1.add(_doubleArrow_1);
          }
        }
      };
      _xblockexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
    }
    return _xblockexpression;
  }
  
  public static ParallelTransition orbit(final Node creature, final double radiusX, final double radiusY, final Duration cycleTime, final double initialAngle) {
    ParallelTransition _parallelTransition = new ParallelTransition();
    final Procedure1<ParallelTransition> _function = (ParallelTransition it) -> {
      ObservableList<Animation> _children = it.getChildren();
      RotateTransition _rotateTransition = new RotateTransition();
      final Procedure1<RotateTransition> _function_1 = (RotateTransition it_1) -> {
        it_1.setNode(creature);
        it_1.setFromAngle(initialAngle);
        it_1.setToAngle((initialAngle + 360));
        Point3D _point3D = new Point3D(0, 1, 0);
        it_1.setAxis(_point3D);
        it_1.setCycleCount((-1));
        it_1.setDuration(DurationExtensions.seconds(5));
        double _random = Math.random();
        Duration _seconds = DurationExtensions.seconds(5);
        Duration _multiply = DurationExtensions.operator_multiply(_random, _seconds);
        it_1.setDelay(_multiply);
        it_1.setInterpolator(Interpolator.LINEAR);
      };
      RotateTransition _doubleArrow = ObjectExtensions.<RotateTransition>operator_doubleArrow(_rotateTransition, _function_1);
      _children.add(_doubleArrow);
      ObservableList<Animation> _children_1 = it.getChildren();
      PathTransition _pathTransition = new PathTransition();
      final Procedure1<PathTransition> _function_2 = (PathTransition it_1) -> {
        it_1.setNode(creature);
        Path _path = new Path();
        final Procedure1<Path> _function_3 = (Path it_2) -> {
          ObservableList<PathElement> _elements = it_2.getElements();
          MoveTo _moveTo = new MoveTo((-radiusX), 0);
          _elements.add(_moveTo);
          ObservableList<PathElement> _elements_1 = it_2.getElements();
          CubicCurveTo _cubicCurveTo = new CubicCurveTo();
          final Procedure1<CubicCurveTo> _function_4 = (CubicCurveTo it_3) -> {
            it_3.setControlX1((-radiusX));
            it_3.setControlY1((0.5 * radiusY));
            it_3.setControlX2(((-0.5) * radiusX));
            it_3.setControlY2(radiusY);
            it_3.setX(0);
            it_3.setY(radiusY);
          };
          CubicCurveTo _doubleArrow_1 = ObjectExtensions.<CubicCurveTo>operator_doubleArrow(_cubicCurveTo, _function_4);
          _elements_1.add(_doubleArrow_1);
          ObservableList<PathElement> _elements_2 = it_2.getElements();
          CubicCurveTo _cubicCurveTo_1 = new CubicCurveTo();
          final Procedure1<CubicCurveTo> _function_5 = (CubicCurveTo it_3) -> {
            it_3.setControlX1((0.5 * radiusX));
            it_3.setControlY1(radiusY);
            it_3.setControlX2(radiusX);
            it_3.setControlY2((0.5 * radiusY));
            it_3.setX(radiusX);
            it_3.setY(0);
          };
          CubicCurveTo _doubleArrow_2 = ObjectExtensions.<CubicCurveTo>operator_doubleArrow(_cubicCurveTo_1, _function_5);
          _elements_2.add(_doubleArrow_2);
          ObservableList<PathElement> _elements_3 = it_2.getElements();
          CubicCurveTo _cubicCurveTo_2 = new CubicCurveTo();
          final Procedure1<CubicCurveTo> _function_6 = (CubicCurveTo it_3) -> {
            it_3.setControlX1(radiusX);
            it_3.setControlY1(((-0.5) * radiusY));
            it_3.setControlX2((0.5 * radiusX));
            it_3.setControlY2((-radiusY));
            it_3.setX(0);
            it_3.setY((-radiusY));
          };
          CubicCurveTo _doubleArrow_3 = ObjectExtensions.<CubicCurveTo>operator_doubleArrow(_cubicCurveTo_2, _function_6);
          _elements_3.add(_doubleArrow_3);
          ObservableList<PathElement> _elements_4 = it_2.getElements();
          CubicCurveTo _cubicCurveTo_3 = new CubicCurveTo();
          final Procedure1<CubicCurveTo> _function_7 = (CubicCurveTo it_3) -> {
            it_3.setControlX1(((-0.5) * radiusX));
            it_3.setControlY1((-radiusY));
            it_3.setControlX2((-radiusX));
            it_3.setControlY2(((-0.5) * radiusY));
            it_3.setX((-radiusX));
            it_3.setY(0);
          };
          CubicCurveTo _doubleArrow_4 = ObjectExtensions.<CubicCurveTo>operator_doubleArrow(_cubicCurveTo_3, _function_7);
          _elements_4.add(_doubleArrow_4);
        };
        Path _doubleArrow_1 = ObjectExtensions.<Path>operator_doubleArrow(_path, _function_3);
        it_1.setPath(_doubleArrow_1);
        it_1.setDuration(cycleTime);
        Duration _multiply = DurationExtensions.operator_multiply(cycleTime, initialAngle);
        Duration _divide = DurationExtensions.operator_divide(_multiply, 360.0);
        it_1.setDelay(_divide);
        it_1.setInterpolator(Interpolator.LINEAR);
        it_1.setCycleCount((-1));
      };
      PathTransition _doubleArrow_1 = ObjectExtensions.<PathTransition>operator_doubleArrow(_pathTransition, _function_2);
      _children_1.add(_doubleArrow_1);
      it.setCycleCount((-1));
      it.play();
    };
    return ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
  }
  
  public static RotateTransition spin(final Node creature) {
    RotateTransition _rotateTransition = new RotateTransition();
    final Procedure1<RotateTransition> _function = (RotateTransition it) -> {
      it.setNode(creature);
      it.setFromAngle(0);
      it.setToAngle(360);
      Point3D _point3D = new Point3D(0.7, 0.8, 1);
      it.setAxis(_point3D);
      it.setCycleCount((-1));
      it.setDuration(DurationExtensions.seconds(7));
      it.setInterpolator(Interpolator.LINEAR);
      it.play();
    };
    return ObjectExtensions.<RotateTransition>operator_doubleArrow(_rotateTransition, _function);
  }
  
  public static SequentialTransition warpOut(final Node creature, final double stepSize) {
    SequentialTransition _xblockexpression = null;
    {
      double _random = Math.random();
      Duration _millis = DurationExtensions.millis(300);
      Duration _multiply = DurationExtensions.operator_multiply(_random, _millis);
      Duration _millis_1 = DurationExtensions.millis(600);
      final Duration stepDuration = DurationExtensions.operator_plus(_multiply, _millis_1);
      SequentialTransition _sequentialTransition = new SequentialTransition();
      final Procedure1<SequentialTransition> _function = (SequentialTransition it) -> {
        ObservableList<Animation> _children = it.getChildren();
        ParallelTransition _parallelTransition = new ParallelTransition();
        final Procedure1<ParallelTransition> _function_1 = (ParallelTransition it_1) -> {
          ObservableList<Animation> _children_1 = it_1.getChildren();
          ScaleTransition _scaleTransition = new ScaleTransition();
          final Procedure1<ScaleTransition> _function_2 = (ScaleTransition it_2) -> {
            it_2.setNode(creature);
            it_2.setFromX(1);
            it_2.setToX(1.8);
            it_2.setFromY(1);
            it_2.setToY(0.3);
            Duration _multiply_1 = DurationExtensions.operator_multiply(0.8, stepDuration);
            it_2.setDuration(_multiply_1);
            Duration _multiply_2 = DurationExtensions.operator_multiply(0.2, stepDuration);
            it_2.setDelay(_multiply_2);
          };
          ScaleTransition _doubleArrow = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function_2);
          _children_1.add(_doubleArrow);
          ObservableList<Animation> _children_2 = it_1.getChildren();
          TranslateTransition _translateTransition = new TranslateTransition();
          final Procedure1<TranslateTransition> _function_3 = (TranslateTransition it_2) -> {
            it_2.setNode(creature);
            double _signum = Math.signum(stepSize);
            double _multiply_1 = (_signum * 0.8);
            double _prefWidth = creature.prefWidth((-1));
            double _multiply_2 = (_multiply_1 * _prefWidth);
            double _cos = Math.cos(Math.toRadians(creature.getRotate()));
            double _multiply_3 = (_multiply_2 * _cos);
            it_2.setByX(_multiply_3);
            double _signum_1 = Math.signum(stepSize);
            double _multiply_4 = (_signum_1 * 0.8);
            double _prefWidth_1 = creature.prefWidth((-1));
            double _multiply_5 = (_multiply_4 * _prefWidth_1);
            double _sin = Math.sin(Math.toRadians(creature.getRotate()));
            double _multiply_6 = (_multiply_5 * _sin);
            it_2.setByY(_multiply_6);
            Duration _multiply_7 = DurationExtensions.operator_multiply(0.8, stepDuration);
            it_2.setDuration(_multiply_7);
            Duration _multiply_8 = DurationExtensions.operator_multiply(0.2, stepDuration);
            it_2.setDelay(_multiply_8);
          };
          TranslateTransition _doubleArrow_1 = ObjectExtensions.<TranslateTransition>operator_doubleArrow(_translateTransition, _function_3);
          _children_2.add(_doubleArrow_1);
        };
        ParallelTransition _doubleArrow = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_1);
        _children.add(_doubleArrow);
        ObservableList<Animation> _children_1 = it.getChildren();
        TranslateTransition _translateTransition = new TranslateTransition();
        final Procedure1<TranslateTransition> _function_2 = (TranslateTransition it_1) -> {
          it_1.setNode(creature);
          double _cos = Math.cos(Math.toRadians(creature.getRotate()));
          double _multiply_1 = (stepSize * _cos);
          it_1.setByX(_multiply_1);
          double _sin = Math.sin(Math.toRadians(creature.getRotate()));
          double _multiply_2 = (stepSize * _sin);
          it_1.setByY(_multiply_2);
          Duration _multiply_3 = DurationExtensions.operator_multiply(0.2, stepDuration);
          it_1.setDuration(_multiply_3);
          Duration _multiply_4 = DurationExtensions.operator_multiply(0.2, stepDuration);
          it_1.setDelay(_multiply_4);
        };
        TranslateTransition _doubleArrow_1 = ObjectExtensions.<TranslateTransition>operator_doubleArrow(_translateTransition, _function_2);
        _children_1.add(_doubleArrow_1);
        ObservableList<Animation> _children_2 = it.getChildren();
        ScaleTransition _scaleTransition = new ScaleTransition();
        final Procedure1<ScaleTransition> _function_3 = (ScaleTransition it_1) -> {
          it_1.setToX(1);
          it_1.setToY(1);
          it_1.setDuration(DurationExtensions.seconds(0));
        };
        ScaleTransition _doubleArrow_2 = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function_3);
        _children_2.add(_doubleArrow_2);
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
      final Procedure1<SequentialTransition> _function = (SequentialTransition it) -> {
        ObservableList<Animation> _children = it.getChildren();
        ScaleTransition _scaleTransition = new ScaleTransition();
        final Procedure1<ScaleTransition> _function_1 = (ScaleTransition it_1) -> {
          it_1.setFromX(1.8);
          it_1.setToX(1);
          it_1.setFromY(0.3);
          it_1.setToY(1);
          it_1.setDuration(DurationExtensions.seconds(0));
        };
        ScaleTransition _doubleArrow = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function_1);
        _children.add(_doubleArrow);
        ObservableList<Animation> _children_1 = it.getChildren();
        TranslateTransition _translateTransition = new TranslateTransition();
        final Procedure1<TranslateTransition> _function_2 = (TranslateTransition it_1) -> {
          it_1.setNode(creature);
          double _layoutX = creature.getLayoutX();
          double _cos = Math.cos(Math.toRadians(creature.getRotate()));
          double _multiply_1 = (stepSize * _cos);
          double _minus = (_layoutX - _multiply_1);
          it_1.setFromX(_minus);
          double _layoutY = creature.getLayoutY();
          double _sin = Math.sin(Math.toRadians(creature.getRotate()));
          double _multiply_2 = (stepSize * _sin);
          double _minus_1 = (_layoutY - _multiply_2);
          it_1.setFromY(_minus_1);
          double _cos_1 = Math.cos(Math.toRadians(creature.getRotate()));
          double _multiply_3 = (stepSize * _cos_1);
          it_1.setByX(_multiply_3);
          double _sin_1 = Math.sin(Math.toRadians(creature.getRotate()));
          double _multiply_4 = (stepSize * _sin_1);
          it_1.setByY(_multiply_4);
          Duration _multiply_5 = DurationExtensions.operator_multiply(0.2, stepDuration);
          it_1.setDuration(_multiply_5);
          Duration _multiply_6 = DurationExtensions.operator_multiply(0.2, stepDuration);
          it_1.setDelay(_multiply_6);
        };
        TranslateTransition _doubleArrow_1 = ObjectExtensions.<TranslateTransition>operator_doubleArrow(_translateTransition, _function_2);
        _children_1.add(_doubleArrow_1);
        ObservableList<Animation> _children_2 = it.getChildren();
        ParallelTransition _parallelTransition = new ParallelTransition();
        final Procedure1<ParallelTransition> _function_3 = (ParallelTransition it_1) -> {
          ObservableList<Animation> _children_3 = it_1.getChildren();
          ScaleTransition _scaleTransition_1 = new ScaleTransition();
          final Procedure1<ScaleTransition> _function_4 = (ScaleTransition it_2) -> {
            it_2.setNode(creature);
            it_2.setFromX(1.8);
            it_2.setToX(1);
            it_2.setFromY(0.6);
            it_2.setToY(1);
            Duration _multiply_1 = DurationExtensions.operator_multiply(0.8, stepDuration);
            it_2.setDuration(_multiply_1);
          };
          ScaleTransition _doubleArrow_2 = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition_1, _function_4);
          _children_3.add(_doubleArrow_2);
          ObservableList<Animation> _children_4 = it_1.getChildren();
          TranslateTransition _translateTransition_1 = new TranslateTransition();
          final Procedure1<TranslateTransition> _function_5 = (TranslateTransition it_2) -> {
            it_2.setNode(creature);
            double _signum = Math.signum(stepSize);
            double _multiply_1 = (_signum * 0.8);
            double _prefWidth = creature.prefWidth((-1));
            double _multiply_2 = (_multiply_1 * _prefWidth);
            double _cos = Math.cos(Math.toRadians(creature.getRotate()));
            double _multiply_3 = (_multiply_2 * _cos);
            it_2.setByX(_multiply_3);
            double _signum_1 = Math.signum(stepSize);
            double _multiply_4 = (_signum_1 * 0.8);
            double _prefWidth_1 = creature.prefWidth((-1));
            double _multiply_5 = (_multiply_4 * _prefWidth_1);
            double _sin = Math.sin(Math.toRadians(creature.getRotate()));
            double _multiply_6 = (_multiply_5 * _sin);
            it_2.setByY(_multiply_6);
            Duration _multiply_7 = DurationExtensions.operator_multiply(0.8, stepDuration);
            it_2.setDuration(_multiply_7);
          };
          TranslateTransition _doubleArrow_3 = ObjectExtensions.<TranslateTransition>operator_doubleArrow(_translateTransition_1, _function_5);
          _children_4.add(_doubleArrow_3);
        };
        ParallelTransition _doubleArrow_2 = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_3);
        _children_2.add(_doubleArrow_2);
      };
      _xblockexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
    }
    return _xblockexpression;
  }
}
