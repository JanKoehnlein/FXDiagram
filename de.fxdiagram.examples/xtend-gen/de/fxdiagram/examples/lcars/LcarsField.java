package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.examples.lcars.LcarsExtensions;
import de.fxdiagram.examples.lcars.LcarsNode;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LcarsField extends Parent {
  private FlowPane flowPane;
  
  private LcarsNode node;
  
  private Node queryProgress;
  
  public LcarsField(final LcarsNode node, final String name, final String value) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field key is undefined for the type LcarsField"
      + "\nThe method or field key is undefined for the type LcarsField"
      + "\nType mismatch: cannot convert from String to DomainObjectHandle"
      + "\n== cannot be resolved"
      + "\n== cannot be resolved");
  }
  
  public LcarsNode getLcarsNode() {
    return this.node;
  }
  
  protected Iterable<Text> getAllTextNodes() {
    ObservableList<Node> _children = this.flowPane.getChildren();
    Iterable<Text> _filter = Iterables.<Text>filter(_children, Text.class);
    return _filter;
  }
  
  protected boolean isSplitHere(final char c) {
    boolean _isWhitespace = Character.isWhitespace(c);
    return _isWhitespace;
  }
  
  public Timeline addAnimation(final Timeline timeline) {
    Timeline _xblockexpression = null;
    {
      final Procedure1<Timeline> _function = new Procedure1<Timeline>() {
        public void apply(final Timeline it) {
          Iterable<Text> _allTextNodes = LcarsField.this.getAllTextNodes();
          for (final Text textNode : _allTextNodes) {
            {
              String _text = textNode.getText();
              int _length = _text.length();
              ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
              for (final Integer index : _doubleDotLessThan) {
                ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
                Duration _cycleDuration = it.getCycleDuration();
                Duration _millis = Duration.millis(15);
                Duration _add = _cycleDuration.add(_millis);
                StringProperty _textProperty = textNode.textProperty();
                String _text_1 = textNode.getText();
                String _substring = _text_1.substring(0, ((index).intValue() + 1));
                KeyValue _keyValue = new <String>KeyValue(_textProperty, _substring);
                KeyFrame _keyFrame = new KeyFrame(_add, _keyValue);
                _keyFrames.add(_keyFrame);
              }
              textNode.setText("");
            }
          }
        }
      };
      ObjectExtensions.<Timeline>operator_doubleArrow(timeline, _function);
      _xblockexpression = (timeline);
    }
    return _xblockexpression;
  }
  
  public void resetVisuals() {
    boolean _notEquals = (!Objects.equal(this.queryProgress, null));
    if (_notEquals) {
      LcarsNode _lcarsNode = this.getLcarsNode();
      Node _node = _lcarsNode.getNode();
      ObservableList<Node> _children = ((Pane) _node).getChildren();
      _children.remove(this.queryProgress);
      this.queryProgress = null;
    }
    Iterable<Text> _allTextNodes = this.getAllTextNodes();
    Text _head = IterableExtensions.<Text>head(_allTextNodes);
    _head.setFill(LcarsExtensions.FLESH);
    Iterable<Text> _allTextNodes_1 = this.getAllTextNodes();
    Iterable<Text> _tail = IterableExtensions.<Text>tail(_allTextNodes_1);
    final Procedure1<Text> _function = new Procedure1<Text>() {
      public void apply(final Text it) {
        it.setFill(LcarsExtensions.ORANGE);
      }
    };
    IterableExtensions.<Text>forEach(_tail, _function);
  }
  
  public RectangleBorderPane createQueryProgress() {
    RectangleBorderPane _xblockexpression = null;
    {
      Text _text = new Text();
      final Text label = _text;
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
        public void apply(final RectangleBorderPane it) {
          ObservableList<Node> _children = it.getChildren();
          final Procedure1<Text> _function = new Procedure1<Text>() {
            public void apply(final Text it) {
              it.setText("querying...");
              Font _lcarsFont = LcarsExtensions.lcarsFont(42);
              it.setFont(_lcarsFont);
              it.setFill(LcarsExtensions.ORANGE);
              Insets _insets = new Insets(5, 5, 5, 5);
              StackPane.setMargin(it, _insets);
            }
          };
          Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(label, _function);
          _children.add(_doubleArrow);
          it.setBackgroundPaint(Color.BLACK);
          it.setBackgroundRadius(10);
          it.setBorderWidth(0);
        }
      };
      final RectangleBorderPane node = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
      Timeline _timeline = new Timeline();
      final Procedure1<Timeline> _function_1 = new Procedure1<Timeline>() {
        public void apply(final Timeline it) {
          ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
          Duration _millis = Duration.millis(0);
          DoubleProperty _opacityProperty = label.opacityProperty();
          KeyValue _keyValue = new <Number>KeyValue(_opacityProperty, Integer.valueOf(0));
          KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue);
          _keyFrames.add(_keyFrame);
          ObservableList<KeyFrame> _keyFrames_1 = it.getKeyFrames();
          Duration _millis_1 = Duration.millis(700);
          DoubleProperty _opacityProperty_1 = label.opacityProperty();
          KeyValue _keyValue_1 = new <Number>KeyValue(_opacityProperty_1, Integer.valueOf(1));
          KeyFrame _keyFrame_1 = new KeyFrame(_millis_1, _keyValue_1);
          _keyFrames_1.add(_keyFrame_1);
          ObservableList<KeyFrame> _keyFrames_2 = it.getKeyFrames();
          Duration _millis_2 = Duration.millis(750);
          DoubleProperty _opacityProperty_2 = label.opacityProperty();
          KeyValue _keyValue_2 = new <Number>KeyValue(_opacityProperty_2, Integer.valueOf(1));
          KeyFrame _keyFrame_2 = new KeyFrame(_millis_2, _keyValue_2);
          _keyFrames_2.add(_keyFrame_2);
          ObservableList<KeyFrame> _keyFrames_3 = it.getKeyFrames();
          Duration _millis_3 = Duration.millis(770);
          DoubleProperty _opacityProperty_3 = label.opacityProperty();
          KeyValue _keyValue_3 = new <Number>KeyValue(_opacityProperty_3, Integer.valueOf(0));
          KeyFrame _keyFrame_3 = new KeyFrame(_millis_3, _keyValue_3);
          _keyFrames_3.add(_keyFrame_3);
          it.setCycleCount((-1));
          it.play();
        }
      };
      ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function_1);
      _xblockexpression = (node);
    }
    return _xblockexpression;
  }
}
