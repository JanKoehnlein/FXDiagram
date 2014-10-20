package de.fxdiagram.xtext.xbase;

import com.google.common.collect.Iterables;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.DurationExtensions;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class CompartmentInflator {
  public static SequentialTransition inflate(final XNode parent, final List<Text> labels, final Pane contentArea, final double minWidth) {
    SequentialTransition _xblockexpression = null;
    {
      final Function1<Text, Dimension2D> _function = new Function1<Text, Dimension2D>() {
        public Dimension2D apply(final Text it) {
          return CompartmentInflator.getDimension(it);
        }
      };
      List<Dimension2D> _map = ListExtensions.<Text, Dimension2D>map(labels, _function);
      Dimension2D _dimension2D = new Dimension2D(minWidth, 0);
      Iterable<Dimension2D> _plus = Iterables.<Dimension2D>concat(_map, Collections.<Dimension2D>unmodifiableList(CollectionLiterals.<Dimension2D>newArrayList(_dimension2D)));
      final Function2<Dimension2D, Dimension2D, Dimension2D> _function_1 = new Function2<Dimension2D, Dimension2D, Dimension2D>() {
        public Dimension2D apply(final Dimension2D $0, final Dimension2D $1) {
          double _width = $0.getWidth();
          double _width_1 = $1.getWidth();
          double _max = Math.max(_width, _width_1);
          double _height = $0.getHeight();
          double _height_1 = $1.getHeight();
          double _max_1 = Math.max(_height, _height_1);
          return new Dimension2D(_max, _max_1);
        }
      };
      final Dimension2D maxLabelSize = IterableExtensions.<Dimension2D>reduce(_plus, _function_1);
      Rectangle _rectangle = new Rectangle(0, 0, minWidth, 0);
      final Procedure1<Rectangle> _function_2 = new Procedure1<Rectangle>() {
        public void apply(final Rectangle it) {
          it.setOpacity(0);
        }
      };
      final Rectangle spacer = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_2);
      ObservableList<Node> _children = contentArea.getChildren();
      _children.add(spacer);
      double _height = maxLabelSize.getHeight();
      int _size = labels.size();
      final double allChildrenHeight = (_height * _size);
      double _layoutX = parent.getLayoutX();
      double _switchResult = (double) 0;
      Side _placementHint = parent.getPlacementHint();
      if (_placementHint != null) {
        switch (_placementHint) {
          case TOP:
          case BOTTOM:
            double _width = maxLabelSize.getWidth();
            double _minus = (_width - minWidth);
            _switchResult = (0.5 * _minus);
            break;
          case LEFT:
            double _width_1 = maxLabelSize.getWidth();
            _switchResult = (_width_1 - minWidth);
            break;
          default:
            _switchResult = 0;
            break;
        }
      } else {
        _switchResult = 0;
      }
      final double endX = (_layoutX - _switchResult);
      double _layoutY = parent.getLayoutY();
      double _switchResult_1 = (double) 0;
      Side _placementHint_1 = parent.getPlacementHint();
      if (_placementHint_1 != null) {
        switch (_placementHint_1) {
          case LEFT:
          case RIGHT:
            _switchResult_1 = (0.5 * allChildrenHeight);
            break;
          case TOP:
            _switchResult_1 = allChildrenHeight;
            break;
          default:
            _switchResult_1 = 0;
            break;
        }
      } else {
        _switchResult_1 = 0;
      }
      final double endY = (_layoutY - _switchResult_1);
      SequentialTransition _sequentialTransition = new SequentialTransition();
      final Procedure1<SequentialTransition> _function_3 = new Procedure1<SequentialTransition>() {
        public void apply(final SequentialTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          Timeline _timeline = new Timeline();
          final Procedure1<Timeline> _function = new Procedure1<Timeline>() {
            public void apply(final Timeline it) {
              Duration _millis = DurationExtensions.millis(300);
              it.setDelay(_millis);
              it.setCycleCount(1);
              it.setAutoReverse(false);
              ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
              Duration _millis_1 = DurationExtensions.millis(300);
              DoubleProperty _layoutXProperty = parent.layoutXProperty();
              KeyValue _keyValue = new <Number>KeyValue(_layoutXProperty, Double.valueOf(endX));
              DoubleProperty _layoutYProperty = parent.layoutYProperty();
              KeyValue _keyValue_1 = new <Number>KeyValue(_layoutYProperty, Double.valueOf(endY));
              DoubleProperty _widthProperty = spacer.widthProperty();
              double _width = maxLabelSize.getWidth();
              KeyValue _keyValue_2 = new <Number>KeyValue(_widthProperty, Double.valueOf(_width));
              DoubleProperty _heightProperty = spacer.heightProperty();
              KeyValue _keyValue_3 = new <Number>KeyValue(_heightProperty, Double.valueOf(allChildrenHeight));
              KeyFrame _keyFrame = new KeyFrame(_millis_1, _keyValue, _keyValue_1, _keyValue_2, _keyValue_3);
              _keyFrames.add(_keyFrame);
              final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                public void handle(final ActionEvent it) {
                  final Procedure1<Pane> _function = new Procedure1<Pane>() {
                    public void apply(final Pane it) {
                      ObservableList<Node> _children = it.getChildren();
                      _children.remove(spacer);
                      ObservableList<Node> _children_1 = it.getChildren();
                      VBox _vBox = new VBox();
                      final Procedure1<VBox> _function = new Procedure1<VBox>() {
                        public void apply(final VBox it) {
                          ObservableList<Node> _children = it.getChildren();
                          Iterables.<Node>addAll(_children, labels);
                        }
                      };
                      VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
                      _children_1.add(_doubleArrow);
                    }
                  };
                  ObjectExtensions.<Pane>operator_doubleArrow(contentArea, _function);
                }
              };
              it.setOnFinished(_function);
            }
          };
          Timeline _doubleArrow = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function);
          _children.add(_doubleArrow);
          ObservableList<Animation> _children_1 = it.getChildren();
          final Function1<Text, FadeTransition> _function_1 = new Function1<Text, FadeTransition>() {
            public FadeTransition apply(final Text label) {
              FadeTransition _fadeTransition = new FadeTransition();
              final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
                public void apply(final FadeTransition it) {
                  it.setNode(label);
                  Duration _millis = DurationExtensions.millis(30);
                  it.setDuration(_millis);
                  it.setFromValue(0);
                  it.setToValue(1);
                }
              };
              return ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
            }
          };
          List<Animation> _map = ListExtensions.<Text, Animation>map(labels, _function_1);
          Iterables.<Animation>addAll(_children_1, _map);
          it.play();
        }
      };
      _xblockexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function_3);
    }
    return _xblockexpression;
  }
  
  private static FontLoader getFontLoader() {
    Toolkit _toolkit = Toolkit.getToolkit();
    return _toolkit.getFontLoader();
  }
  
  public static Dimension2D getDimension(final Text it) {
    FontLoader _fontLoader = CompartmentInflator.getFontLoader();
    String _text = it.getText();
    Font _font = it.getFont();
    float _computeStringWidth = _fontLoader.computeStringWidth(_text, _font);
    FontLoader _fontLoader_1 = CompartmentInflator.getFontLoader();
    Font _font_1 = it.getFont();
    FontMetrics _fontMetrics = _fontLoader_1.getFontMetrics(_font_1);
    float _lineHeight = _fontMetrics.getLineHeight();
    return new Dimension2D(_computeStringWidth, _lineHeight);
  }
}
