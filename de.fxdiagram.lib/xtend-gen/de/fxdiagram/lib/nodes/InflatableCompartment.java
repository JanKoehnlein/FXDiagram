package de.fxdiagram.lib.nodes;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.TextExtensions;
import java.util.Collections;
import java.util.function.Consumer;
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
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SuppressWarnings("all")
public class InflatableCompartment extends Parent {
  @Data
  public static class InflationData {
    private final double deflatedWidth;
    
    private final double inflatedWidth;
    
    private final double inflatedHeight;
    
    private final double startX;
    
    private final double startY;
    
    private final double endX;
    
    private final double endY;
    
    public InflationData(final InflatableCompartment compartment, final double deflatedWidth) {
      this.deflatedWidth = deflatedWidth;
      final Iterable<Text> labels = compartment.getLabels();
      final XNode host = compartment.host;
      final Function1<Text, Dimension2D> _function = new Function1<Text, Dimension2D>() {
        public Dimension2D apply(final Text it) {
          return TextExtensions.getOfflineDimension(it);
        }
      };
      Iterable<Dimension2D> _map = IterableExtensions.<Text, Dimension2D>map(labels, _function);
      Dimension2D _dimension2D = new Dimension2D(deflatedWidth, 0);
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
      double _width = maxLabelSize.getWidth();
      double _left = compartment.padding.getLeft();
      double _plus_1 = (_width + _left);
      double _right = compartment.padding.getRight();
      double _plus_2 = (_plus_1 + _right);
      this.inflatedWidth = _plus_2;
      double _height = maxLabelSize.getHeight();
      int _size = IterableExtensions.size(labels);
      double _multiply = (_height * _size);
      double _top = compartment.padding.getTop();
      double _plus_3 = (_multiply + _top);
      double _bottom = compartment.padding.getBottom();
      double _plus_4 = (_plus_3 + _bottom);
      this.inflatedHeight = _plus_4;
      double _layoutX = host.getLayoutX();
      this.startX = _layoutX;
      double _layoutX_1 = host.getLayoutX();
      double _switchResult = (double) 0;
      Side _placementHint = host.getPlacementHint();
      if (_placementHint != null) {
        switch (_placementHint) {
          case LEFT:
            _switchResult = (this.inflatedWidth - deflatedWidth);
            break;
          case RIGHT:
            _switchResult = 0;
            break;
          default:
            _switchResult = (0.5 * (this.inflatedWidth - deflatedWidth));
            break;
        }
      } else {
        _switchResult = (0.5 * (this.inflatedWidth - deflatedWidth));
      }
      double _minus = (_layoutX_1 - _switchResult);
      this.endX = _minus;
      double _layoutY = host.getLayoutY();
      this.startY = _layoutY;
      double _layoutY_1 = host.getLayoutY();
      double _switchResult_1 = (double) 0;
      Side _placementHint_1 = host.getPlacementHint();
      if (_placementHint_1 != null) {
        switch (_placementHint_1) {
          case TOP:
            _switchResult_1 = this.inflatedHeight;
            break;
          case BOTTOM:
            _switchResult_1 = 0;
            break;
          default:
            _switchResult_1 = (0.5 * this.inflatedHeight);
            break;
        }
      } else {
        _switchResult_1 = (0.5 * this.inflatedHeight);
      }
      double _minus_1 = (_layoutY_1 - _switchResult_1);
      this.endY = _minus_1;
    }
    
    @Override
    @Pure
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (Double.doubleToLongBits(this.deflatedWidth) ^ (Double.doubleToLongBits(this.deflatedWidth) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.inflatedWidth) ^ (Double.doubleToLongBits(this.inflatedWidth) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.inflatedHeight) ^ (Double.doubleToLongBits(this.inflatedHeight) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.startX) ^ (Double.doubleToLongBits(this.startX) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.startY) ^ (Double.doubleToLongBits(this.startY) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.endX) ^ (Double.doubleToLongBits(this.endX) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.endY) ^ (Double.doubleToLongBits(this.endY) >>> 32));
      return result;
    }
    
    @Override
    @Pure
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      InflatableCompartment.InflationData other = (InflatableCompartment.InflationData) obj;
      if (Double.doubleToLongBits(other.deflatedWidth) != Double.doubleToLongBits(this.deflatedWidth))
        return false; 
      if (Double.doubleToLongBits(other.inflatedWidth) != Double.doubleToLongBits(this.inflatedWidth))
        return false; 
      if (Double.doubleToLongBits(other.inflatedHeight) != Double.doubleToLongBits(this.inflatedHeight))
        return false; 
      if (Double.doubleToLongBits(other.startX) != Double.doubleToLongBits(this.startX))
        return false; 
      if (Double.doubleToLongBits(other.startY) != Double.doubleToLongBits(this.startY))
        return false; 
      if (Double.doubleToLongBits(other.endX) != Double.doubleToLongBits(this.endX))
        return false; 
      if (Double.doubleToLongBits(other.endY) != Double.doubleToLongBits(this.endY))
        return false; 
      return true;
    }
    
    @Override
    @Pure
    public String toString() {
      ToStringBuilder b = new ToStringBuilder(this);
      b.add("deflatedWidth", this.deflatedWidth);
      b.add("inflatedWidth", this.inflatedWidth);
      b.add("inflatedHeight", this.inflatedHeight);
      b.add("startX", this.startX);
      b.add("startY", this.startY);
      b.add("endX", this.endX);
      b.add("endY", this.endY);
      return b.toString();
    }
    
    @Pure
    public double getDeflatedWidth() {
      return this.deflatedWidth;
    }
    
    @Pure
    public double getInflatedWidth() {
      return this.inflatedWidth;
    }
    
    @Pure
    public double getInflatedHeight() {
      return this.inflatedHeight;
    }
    
    @Pure
    public double getStartX() {
      return this.startX;
    }
    
    @Pure
    public double getStartY() {
      return this.startY;
    }
    
    @Pure
    public double getEndX() {
      return this.endX;
    }
    
    @Pure
    public double getEndY() {
      return this.endY;
    }
  }
  
  private XNode host;
  
  private VBox labelContainer;
  
  private boolean isInflated = false;
  
  private Insets padding;
  
  public InflatableCompartment(final XNode host, final Insets padding) {
    this.host = host;
    this.padding = padding;
    VBox _vBox = new VBox();
    final Procedure1<VBox> _function = new Procedure1<VBox>() {
      public void apply(final VBox it) {
        it.setPadding(padding);
      }
    };
    VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
    this.labelContainer = _doubleArrow;
    this.setManaged(true);
  }
  
  public boolean add(final Text label) {
    ObservableList<Node> _children = this.labelContainer.getChildren();
    return _children.add(label);
  }
  
  protected Iterable<Text> getLabels() {
    ObservableList<Node> _children = this.labelContainer.getChildren();
    return Iterables.<Text>filter(_children, Text.class);
  }
  
  public SequentialTransition getInflateAnimation(final double deflatedWidth) {
    InflatableCompartment.InflationData _inflationData = new InflatableCompartment.InflationData(this, deflatedWidth);
    return this.getInflateAnimation(_inflationData);
  }
  
  public void populate() {
    if (this.isInflated) {
      return;
    }
    ObservableList<Node> _children = this.getChildren();
    _children.setAll(this.labelContainer);
  }
  
  protected SequentialTransition getInflateAnimation(final InflatableCompartment.InflationData data) {
    Rectangle _rectangle = new Rectangle(0, 0, data.deflatedWidth, 0);
    final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
      public void apply(final Rectangle it) {
        it.setOpacity(0);
      }
    };
    final Rectangle spacer = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
    ObservableList<Node> _children = this.getChildren();
    _children.setAll(spacer);
    SequentialTransition _sequentialTransition = new SequentialTransition();
    final Procedure1<SequentialTransition> _function_1 = new Procedure1<SequentialTransition>() {
      public void apply(final SequentialTransition it) {
        ObservableList<Animation> _children = it.getChildren();
        Timeline _timeline = new Timeline();
        final Procedure1<Timeline> _function = new Procedure1<Timeline>() {
          public void apply(final Timeline it) {
            it.setCycleCount(1);
            it.setAutoReverse(false);
            ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
            Duration _millis = DurationExtensions.millis(200);
            DoubleProperty _layoutXProperty = InflatableCompartment.this.host.layoutXProperty();
            KeyValue _keyValue = new <Number>KeyValue(_layoutXProperty, Double.valueOf(data.endX));
            DoubleProperty _layoutYProperty = InflatableCompartment.this.host.layoutYProperty();
            KeyValue _keyValue_1 = new <Number>KeyValue(_layoutYProperty, Double.valueOf(data.endY));
            DoubleProperty _widthProperty = spacer.widthProperty();
            KeyValue _keyValue_2 = new <Number>KeyValue(_widthProperty, Double.valueOf(data.inflatedWidth));
            DoubleProperty _heightProperty = spacer.heightProperty();
            KeyValue _keyValue_3 = new <Number>KeyValue(_heightProperty, Double.valueOf(data.inflatedHeight));
            KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue, _keyValue_1, _keyValue_2, _keyValue_3);
            _keyFrames.add(_keyFrame);
            final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
              public void handle(final ActionEvent it) {
                Iterable<Text> _labels = InflatableCompartment.this.getLabels();
                final Consumer<Text> _function = new Consumer<Text>() {
                  public void accept(final Text it) {
                    it.setOpacity(0);
                  }
                };
                _labels.forEach(_function);
                InflatableCompartment.this.populate();
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
        Iterable<Text> _labels = InflatableCompartment.this.getLabels();
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
        Iterable<Animation> _map = IterableExtensions.<Text, Animation>map(_labels, _function_1);
        Iterables.<Animation>addAll(_children_1, _map);
        final EventHandler<ActionEvent> _function_2 = new EventHandler<ActionEvent>() {
          public void handle(final ActionEvent it) {
            InflatableCompartment.this.isInflated = true;
          }
        };
        it.setOnFinished(_function_2);
      }
    };
    return ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function_1);
  }
}
