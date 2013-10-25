package de.fxdiagram.examples.slides;

import de.fxdiagram.core.extensions.DurationExtensions;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Styles {
  public static Text createText(final String text, final int fontSize) {
    Text _createText = Styles.createText(text, "Gill Sans", fontSize);
    return _createText;
  }
  
  public static Text createJungleText(final String text, final int fontSize) {
    Text _createText = Styles.createText(text, "Chalkduster", fontSize);
    return _createText;
  }
  
  public static Text createText(final String text, final String fontName, final int fontSize) {
    Text _text = new Text();
    final Procedure1<Text> _function = new Procedure1<Text>() {
      public void apply(final Text it) {
        String _trim = text.trim();
        it.setText(_trim);
        it.setTextAlignment(TextAlignment.CENTER);
        Font _font = new Font(fontName, fontSize);
        it.setFont(_font);
        Color _jungleGreen = Styles.jungleGreen();
        it.setFill(_jungleGreen);
      }
    };
    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
    return _doubleArrow;
  }
  
  public static Color jungleGreen() {
    Color _rgb = Color.rgb(224, 237, 214);
    return _rgb;
  }
  
  public static Color jungleDarkGreen() {
    Color _rgb = Color.rgb(161, 171, 74);
    return _rgb;
  }
  
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
            double _divide = ((i).intValue() / 10.0);
            double _interpolateAngle = Styles.interpolateAngle(_divide);
            KeyValue _keyValue = new <Number>KeyValue(_angleProperty, Double.valueOf(_interpolateAngle));
            KeyFrame _keyFrame = new KeyFrame(_multiply, _keyValue);
            _keyFrames.add(_keyFrame);
          }
          int _minus = (-1);
          it.setCycleCount(_minus);
          it.setAutoReverse(true);
          double _random = Math.random();
          Duration _millis_1 = DurationExtensions.millis(1000);
          Duration _multiply_1 = DurationExtensions.operator_multiply(_random, _millis_1);
          it.setDelay(_multiply_1);
          it.play();
        }
      };
      Timeline _doubleArrow = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function_1);
      _xblockexpression = (_doubleArrow);
    }
    return _xblockexpression;
  }
  
  protected static double interpolateAngle(final double alpha) {
    double _multiply = (alpha * Math.PI);
    double _divide = (Math.PI / 2);
    double _minus = (_multiply - _divide);
    double _sin = Math.sin(_minus);
    double _multiply_1 = (10 * _sin);
    double _plus = (90 + _multiply_1);
    return _plus;
  }
  
  public static FillTransition flicker(final Shape creature) {
    FillTransition _fillTransition = new FillTransition();
    final Procedure1<FillTransition> _function = new Procedure1<FillTransition>() {
      public void apply(final FillTransition it) {
        it.setShape(creature);
        Color _jungleDarkGreen = Styles.jungleDarkGreen();
        it.setFromValue(_jungleDarkGreen);
        Color _rgb = Color.rgb(107, 114, 51);
        it.setToValue(_rgb);
        Duration _millis = DurationExtensions.millis(100);
        it.setDuration(_millis);
        int _minus = (-1);
        it.setCycleCount(_minus);
        double _random = Math.random();
        Duration _millis_1 = DurationExtensions.millis(2000);
        Duration _multiply = DurationExtensions.operator_multiply(_random, _millis_1);
        it.setDelay(_multiply);
        it.play();
      }
    };
    FillTransition _doubleArrow = ObjectExtensions.<FillTransition>operator_doubleArrow(_fillTransition, _function);
    return _doubleArrow;
  }
  
  public static SequentialTransition breathe(final Node creature) {
    SequentialTransition _sequentialTransition = new SequentialTransition();
    final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
      public void apply(final SequentialTransition it) {
        ObservableList<Animation> _children = it.getChildren();
        ScaleTransition _scaleTransition = new ScaleTransition();
        final Procedure1<ScaleTransition> _function = new Procedure1<ScaleTransition>() {
          public void apply(final ScaleTransition it) {
            it.setFromX(1);
            it.setToX(1.15);
            it.setFromY(1);
            it.setToY(1.1);
            it.setNode(creature);
            Duration _millis = DurationExtensions.millis(1000);
            it.setDuration(_millis);
            Duration _millis_1 = DurationExtensions.millis(200);
            it.setDelay(_millis_1);
          }
        };
        ScaleTransition _doubleArrow = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition, _function);
        _children.add(_doubleArrow);
        ObservableList<Animation> _children_1 = it.getChildren();
        ScaleTransition _scaleTransition_1 = new ScaleTransition();
        final Procedure1<ScaleTransition> _function_1 = new Procedure1<ScaleTransition>() {
          public void apply(final ScaleTransition it) {
            it.setFromX(1.15);
            it.setToX(1);
            it.setFromY(1.1);
            it.setToY(1);
            it.setNode(creature);
            Duration _millis = DurationExtensions.millis(1500);
            it.setDuration(_millis);
            Duration _millis_1 = DurationExtensions.millis(300);
            it.setDelay(_millis_1);
          }
        };
        ScaleTransition _doubleArrow_1 = ObjectExtensions.<ScaleTransition>operator_doubleArrow(_scaleTransition_1, _function_1);
        _children_1.add(_doubleArrow_1);
        double _random = Math.random();
        Duration _millis = DurationExtensions.millis(1000);
        Duration _multiply = DurationExtensions.operator_multiply(_random, _millis);
        it.setDelay(_multiply);
        int _minus = (-1);
        it.setCycleCount(_minus);
        it.play();
      }
    };
    SequentialTransition _doubleArrow = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
    return _doubleArrow;
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
          SequentialTransition _crawlOneWay = Styles.crawlOneWay(creature, stepSize, numSteps);
          _children.add(_crawlOneWay);
          ObservableList<Animation> _children_1 = it.getChildren();
          double _minus = (-stepSize);
          SequentialTransition _crawlOneWay_1 = Styles.crawlOneWay(creature, _minus, numSteps);
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
          int _minus_1 = (-1);
          it.setCycleCount(_minus_1);
          double _random = Math.random();
          Duration _seconds = DurationExtensions.seconds(4);
          Duration _multiply = DurationExtensions.operator_multiply(_random, _seconds);
          it.setDelay(_multiply);
          it.play();
        }
      };
      SequentialTransition _doubleArrow = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
      _xblockexpression = (_doubleArrow);
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
      SequentialTransition _doubleArrow = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
      _xblockexpression = (_doubleArrow);
    }
    return _xblockexpression;
  }
}
