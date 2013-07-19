package de.fxdiagram.examples.lcars;

import com.google.common.collect.Iterables;
import de.fxdiagram.examples.lcars.LcarsExtensions;
import de.fxdiagram.examples.lcars.LcarsNode;
import de.fxdiagram.examples.lcars.LcarsQueryTask;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
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
  
  public LcarsField(final LcarsNode host, final String name, final String value) {
    ObservableList<Node> _children = this.getChildren();
    FlowPane _flowPane = new FlowPane();
    final Procedure1<FlowPane> _function = new Procedure1<FlowPane>() {
        public void apply(final FlowPane it) {
          it.setPrefWrapLength(150);
          ObservableList<Node> _children = it.getChildren();
          Text _text = new Text();
          final Procedure1<Text> _function = new Procedure1<Text>() {
              public void apply(final Text it) {
                String _plus = (name + ": ");
                it.setText(_plus);
                Font _lcarsFont = LcarsExtensions.lcarsFont(12);
                it.setFont(_lcarsFont);
                it.setFill(LcarsExtensions.FLESH);
              }
            };
          Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
          _children.add(_doubleArrow);
          String currentWord = "";
          char[] _charArray = value.toCharArray();
          for (final char c : _charArray) {
            {
              String _plus = (currentWord + Character.valueOf(c));
              currentWord = _plus;
              boolean _isSplitHere = LcarsField.this.isSplitHere(c);
              if (_isSplitHere) {
                ObservableList<Node> _children_1 = it.getChildren();
                Text _text_1 = new Text(currentWord);
                final Procedure1<Text> _function_1 = new Procedure1<Text>() {
                    public void apply(final Text it) {
                      Font _lcarsFont = LcarsExtensions.lcarsFont(12);
                      it.setFont(_lcarsFont);
                      it.setFill(LcarsExtensions.ORANGE);
                    }
                  };
                Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_text_1, _function_1);
                _children_1.add(_doubleArrow_1);
                currentWord = "";
              }
            }
          }
          boolean _isEmpty = currentWord.isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            ObservableList<Node> _children_1 = it.getChildren();
            Text _text_1 = new Text(currentWord);
            final Procedure1<Text> _function_1 = new Procedure1<Text>() {
                public void apply(final Text it) {
                  Font _lcarsFont = LcarsExtensions.lcarsFont(12);
                  it.setFont(_lcarsFont);
                  it.setFill(LcarsExtensions.ORANGE);
                }
              };
            Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_text_1, _function_1);
            _children_1.add(_doubleArrow_1);
          }
          final EventHandler<MouseEvent> _function_2 = new EventHandler<MouseEvent>() {
              public void handle(final MouseEvent it) {
                Iterable<Text> _allTextNodes = LcarsField.this.getAllTextNodes();
                final Procedure1<Text> _function = new Procedure1<Text>() {
                    public void apply(final Text it) {
                      it.setFill(LcarsExtensions.RED);
                    }
                  };
                IterableExtensions.<Text>forEach(_allTextNodes, _function);
                LcarsQueryTask _lcarsQueryTask = new LcarsQueryTask(host, name, value);
                _lcarsQueryTask.run();
              }
            };
          it.setOnMousePressed(_function_2);
          final EventHandler<MouseEvent> _function_3 = new EventHandler<MouseEvent>() {
              public void handle(final MouseEvent it) {
                Iterable<Text> _allTextNodes = LcarsField.this.getAllTextNodes();
                Text _head = IterableExtensions.<Text>head(_allTextNodes);
                _head.setFill(LcarsExtensions.FLESH);
                Iterable<Text> _allTextNodes_1 = LcarsField.this.getAllTextNodes();
                Iterable<Text> _tail = IterableExtensions.<Text>tail(_allTextNodes_1);
                final Procedure1<Text> _function = new Procedure1<Text>() {
                    public void apply(final Text it) {
                      it.setFill(LcarsExtensions.ORANGE);
                    }
                  };
                IterableExtensions.<Text>forEach(_tail, _function);
              }
            };
          it.setOnMouseReleased(_function_3);
        }
      };
    FlowPane _doubleArrow = ObjectExtensions.<FlowPane>operator_doubleArrow(_flowPane, _function);
    FlowPane _flowPane_1 = this.flowPane = _doubleArrow;
    _children.add(_flowPane_1);
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
                  Duration _millis = Duration.millis(20);
                  Duration _add = _cycleDuration.add(_millis);
                  StringProperty _textProperty = textNode.textProperty();
                  String _text_1 = textNode.getText();
                  int _plus = ((index).intValue() + 1);
                  String _substring = _text_1.substring(0, _plus);
                  KeyValue _keyValue = new KeyValue(_textProperty, _substring);
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
}
