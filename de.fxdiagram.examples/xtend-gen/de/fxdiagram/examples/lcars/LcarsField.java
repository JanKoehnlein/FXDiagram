package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.mongodb.DBObject;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.XNodeChooserXConnectionProvider;
import de.fxdiagram.core.tools.actions.LayoutAction;
import de.fxdiagram.examples.lcars.LcarsExtensions;
import de.fxdiagram.examples.lcars.LcarsNode;
import de.fxdiagram.examples.lcars.LcarsQueryTask;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LcarsField extends Parent {
  private FlowPane flowPane;
  
  private LcarsNode node;
  
  public LcarsField(final LcarsNode node, final String name, final String value) {
    this.node = node;
    final XNodeChooserXConnectionProvider _function = new XNodeChooserXConnectionProvider() {
      public XConnection getConnection(final XNode host, final XNode choice, final Object choiceInfo) {
        XConnection _xConnection = new XConnection(host, choice);
        final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
          public void apply(final XConnection it) {
            TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, true);
            it.setSourceArrowHead(_triangleArrowHead);
            XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
            final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
              public void apply(final XConnectionLabel it) {
                Text _text = it.getText();
                String _replace = name.replace("_", " ");
                _text.setText(_replace);
              }
            };
            ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
          }
        };
        XConnection _doubleArrow = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
        return _doubleArrow;
      }
    };
    final XNodeChooserXConnectionProvider connectionProvider = _function;
    ObservableList<Node> _children = this.getChildren();
    FlowPane _flowPane = new FlowPane();
    final Procedure1<FlowPane> _function_1 = new Procedure1<FlowPane>() {
      public void apply(final FlowPane it) {
        it.setPrefWrapLength(150);
        ObservableList<Node> _children = it.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            String _replace = name.replace("_", " ");
            String _plus = (_replace + ": ");
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
            MouseButton _button = it.getButton();
            boolean _notEquals = (!Objects.equal(_button, MouseButton.PRIMARY));
            if (_notEquals) {
              final Service<Void> _function_1 = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                  LcarsQueryTask _lcarsQueryTask = new LcarsQueryTask(LcarsField.this, name, value, connectionProvider);
                  return _lcarsQueryTask;
                }
              };
              final Service<Void> service = _function_1;
              service.start();
            }
          }
        };
        it.setOnMousePressed(_function_2);
        final EventHandler<MouseEvent> _function_3 = new EventHandler<MouseEvent>() {
          public void handle(final MouseEvent it) {
            MouseButton _button = it.getButton();
            boolean _equals = Objects.equal(_button, MouseButton.PRIMARY);
            if (_equals) {
              XDiagram _diagram = CoreExtensions.getDiagram(LcarsField.this);
              ObservableList<XNode> _nodes = _diagram.getNodes();
              Iterable<LcarsNode> _filter = Iterables.<LcarsNode>filter(_nodes, LcarsNode.class);
              final Function1<LcarsNode,Boolean> _function = new Function1<LcarsNode,Boolean>() {
                public Boolean apply(final LcarsNode it) {
                  boolean _and = false;
                  boolean _and_1 = false;
                  boolean _and_2 = false;
                  boolean _notEquals = (!Objects.equal(it, node));
                  if (!_notEquals) {
                    _and_2 = false;
                  } else {
                    DBObject _data = it.getData();
                    Object _get = _data.get(name);
                    boolean _equals = Objects.equal(_get, value);
                    _and_2 = (_notEquals && _equals);
                  }
                  if (!_and_2) {
                    _and_1 = false;
                  } else {
                    ObservableList<XConnection> _outgoingConnections = it.getOutgoingConnections();
                    final Function1<XConnection,Boolean> _function = new Function1<XConnection,Boolean>() {
                      public Boolean apply(final XConnection it) {
                        boolean _and = false;
                        XNode _target = it.getTarget();
                        boolean _equals = Objects.equal(_target, node);
                        if (!_equals) {
                          _and = false;
                        } else {
                          XConnectionLabel _label = it.getLabel();
                          Text _text = null;
                          if (_label!=null) {
                            _text=_label.getText();
                          }
                          String _text_1 = null;
                          if (_text!=null) {
                            _text_1=_text.getText();
                          }
                          boolean _equals_1 = Objects.equal(_text_1, name);
                          _and = (_equals && _equals_1);
                        }
                        return Boolean.valueOf(_and);
                      }
                    };
                    boolean _exists = IterableExtensions.<XConnection>exists(_outgoingConnections, _function);
                    boolean _not = (!_exists);
                    _and_1 = (_and_2 && _not);
                  }
                  if (!_and_1) {
                    _and = false;
                  } else {
                    ObservableList<XConnection> _incomingConnections = it.getIncomingConnections();
                    final Function1<XConnection,Boolean> _function_1 = new Function1<XConnection,Boolean>() {
                      public Boolean apply(final XConnection it) {
                        boolean _and = false;
                        XNode _source = it.getSource();
                        boolean _equals = Objects.equal(_source, node);
                        if (!_equals) {
                          _and = false;
                        } else {
                          XConnectionLabel _label = it.getLabel();
                          Text _text = null;
                          if (_label!=null) {
                            _text=_label.getText();
                          }
                          String _text_1 = null;
                          if (_text!=null) {
                            _text_1=_text.getText();
                          }
                          boolean _equals_1 = Objects.equal(_text_1, name);
                          _and = (_equals && _equals_1);
                        }
                        return Boolean.valueOf(_and);
                      }
                    };
                    boolean _exists_1 = IterableExtensions.<XConnection>exists(_incomingConnections, _function_1);
                    boolean _not_1 = (!_exists_1);
                    _and = (_and_1 && _not_1);
                  }
                  return Boolean.valueOf(_and);
                }
              };
              Iterable<LcarsNode> _filter_1 = IterableExtensions.<LcarsNode>filter(_filter, _function);
              final Function1<LcarsNode,XConnection> _function_1 = new Function1<LcarsNode,XConnection>() {
                public XConnection apply(final LcarsNode it) {
                  XConnection _connection = connectionProvider.getConnection(node, it, null);
                  return _connection;
                }
              };
              Iterable<XConnection> _map = IterableExtensions.<LcarsNode, XConnection>map(_filter_1, _function_1);
              final List<XConnection> newConnections = IterableExtensions.<XConnection>toList(_map);
              XDiagram _diagram_1 = CoreExtensions.getDiagram(LcarsField.this);
              ObservableList<XConnection> _connections = _diagram_1.getConnections();
              Iterables.<XConnection>addAll(_connections, newConnections);
              boolean _isEmpty = newConnections.isEmpty();
              boolean _not = (!_isEmpty);
              if (_not) {
                LayoutAction _layoutAction = new LayoutAction();
                XRoot _root = CoreExtensions.getRoot(LcarsField.this);
                _layoutAction.perform(_root);
              }
              LcarsField.this.resetColors();
            }
          }
        };
        it.setOnMouseReleased(_function_3);
      }
    };
    FlowPane _doubleArrow = ObjectExtensions.<FlowPane>operator_doubleArrow(_flowPane, _function_1);
    FlowPane _flowPane_1 = this.flowPane = _doubleArrow;
    _children.add(_flowPane_1);
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
                int _plus = ((index).intValue() + 1);
                String _substring = _text_1.substring(0, _plus);
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
  
  public void resetColors() {
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
}
