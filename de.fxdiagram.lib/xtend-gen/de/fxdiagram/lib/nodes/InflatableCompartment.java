package de.fxdiagram.lib.nodes;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.TextExtensions;
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
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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
public class InflatableCompartment extends Parent {
  private XNode containerNode;
  
  private double deflatedWidth;
  
  private List<Text> labels = CollectionLiterals.<Text>newArrayList();
  
  public InflatableCompartment(final XNode containerNode, final double deflatedWidth) {
    this.containerNode = containerNode;
    this.deflatedWidth = deflatedWidth;
    this.setManaged(true);
  }
  
  public boolean add(final Text label) {
    return this.labels.add(label);
  }
  
  public SequentialTransition inflate() {
    SequentialTransition _xblockexpression = null;
    {
      final Function1<Text, Dimension2D> _function = new Function1<Text, Dimension2D>() {
        public Dimension2D apply(final Text it) {
          return TextExtensions.getOfflineDimension(it);
        }
      };
      List<Dimension2D> _map = ListExtensions.<Text, Dimension2D>map(this.labels, _function);
      Dimension2D _dimension2D = new Dimension2D(this.deflatedWidth, 0);
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
      Rectangle _rectangle = new Rectangle(0, 0, this.deflatedWidth, 0);
      final Procedure1<Rectangle> _function_2 = new Procedure1<Rectangle>() {
        public void apply(final Rectangle it) {
          it.setOpacity(0);
        }
      };
      final Rectangle spacer = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_2);
      ObservableList<Node> _children = this.getChildren();
      _children.add(spacer);
      double _height = maxLabelSize.getHeight();
      int _size = this.labels.size();
      final double allChildrenHeight = (_height * _size);
      double _layoutX = this.containerNode.getLayoutX();
      double _switchResult = (double) 0;
      Side _placementHint = this.containerNode.getPlacementHint();
      if (_placementHint != null) {
        switch (_placementHint) {
          case LEFT:
            double _width = maxLabelSize.getWidth();
            _switchResult = (_width - this.deflatedWidth);
            break;
          case RIGHT:
            _switchResult = 0;
            break;
          default:
            double _width_1 = maxLabelSize.getWidth();
            double _minus = (_width_1 - this.deflatedWidth);
            _switchResult = (0.5 * _minus);
            break;
        }
      } else {
        double _width_1 = maxLabelSize.getWidth();
        double _minus = (_width_1 - this.deflatedWidth);
        _switchResult = (0.5 * _minus);
      }
      final double endX = (_layoutX - _switchResult);
      double _layoutY = this.containerNode.getLayoutY();
      double _switchResult_1 = (double) 0;
      Side _placementHint_1 = this.containerNode.getPlacementHint();
      if (_placementHint_1 != null) {
        switch (_placementHint_1) {
          case TOP:
            _switchResult_1 = allChildrenHeight;
            break;
          case BOTTOM:
            _switchResult_1 = 0;
            break;
          default:
            _switchResult_1 = (0.5 * allChildrenHeight);
            break;
        }
      } else {
        _switchResult_1 = (0.5 * allChildrenHeight);
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
              DoubleProperty _layoutXProperty = InflatableCompartment.this.containerNode.layoutXProperty();
              KeyValue _keyValue = new <Number>KeyValue(_layoutXProperty, Double.valueOf(endX));
              DoubleProperty _layoutYProperty = InflatableCompartment.this.containerNode.layoutYProperty();
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
                  ObservableList<Node> _children = InflatableCompartment.this.getChildren();
                  _children.remove(spacer);
                  ObservableList<Node> _children_1 = InflatableCompartment.this.getChildren();
                  VBox _vBox = new VBox();
                  final Procedure1<VBox> _function = new Procedure1<VBox>() {
                    public void apply(final VBox it) {
                      ObservableList<Node> _children = it.getChildren();
                      Iterables.<Node>addAll(_children, InflatableCompartment.this.labels);
                    }
                  };
                  VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
                  _children_1.add(_doubleArrow);
                  Parent _parent = InflatableCompartment.this.getParent();
                  _parent.requestLayout();
                  Parent _parent_1 = InflatableCompartment.this.getParent();
                  _parent_1.layout();
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
          List<Animation> _map = ListExtensions.<Text, Animation>map(InflatableCompartment.this.labels, _function_1);
          Iterables.<Animation>addAll(_children_1, _map);
          it.play();
        }
      };
      _xblockexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function_3);
    }
    return _xblockexpression;
  }
}
