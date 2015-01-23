package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.mongodb.DBObject;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.tools.actions.LayoutAction;
import de.fxdiagram.examples.lcars.LcarsConnectionDescriptor;
import de.fxdiagram.examples.lcars.LcarsExtensions;
import de.fxdiagram.examples.lcars.LcarsModelProvider;
import de.fxdiagram.examples.lcars.LcarsNode;
import de.fxdiagram.examples.lcars.LcarsQueryTask;
import de.fxdiagram.lib.chooser.ChooserConnectionProvider;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import java.util.List;
import java.util.function.Consumer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LcarsField extends Parent {
  private FlowPane flowPane;
  
  private LcarsNode node;
  
  private Node queryProgress;
  
  public LcarsField(final LcarsNode node, final String name, final String value) {
    this.node = node;
    XRoot _root = CoreExtensions.getRoot(node);
    LcarsModelProvider _domainObjectProvider = _root.<LcarsModelProvider>getDomainObjectProvider(LcarsModelProvider.class);
    final LcarsConnectionDescriptor connectionDescriptor = _domainObjectProvider.createLcarsConnectionDescriptor(name);
    final ChooserConnectionProvider _function = new ChooserConnectionProvider() {
      @Override
      public XConnection getConnection(final XNode host, final XNode choice, final DomainObjectDescriptor choiceInfo) {
        XConnection _xConnection = new XConnection(host, choice, connectionDescriptor);
        final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
          @Override
          public void apply(final XConnection it) {
            TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, true);
            it.setSourceArrowHead(_triangleArrowHead);
            XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
            final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
              @Override
              public void apply(final XConnectionLabel it) {
                Text _text = it.getText();
                String _replace = name.replace("_", " ");
                _text.setText(_replace);
              }
            };
            ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
          }
        };
        return ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
      }
    };
    final ChooserConnectionProvider connectionProvider = _function;
    ObservableList<Node> _children = this.getChildren();
    FlowPane _flowPane = new FlowPane();
    final Procedure1<FlowPane> _function_1 = new Procedure1<FlowPane>() {
      @Override
      public void apply(final FlowPane it) {
        it.setPrefWrapLength(150);
        ObservableList<Node> _children = it.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          @Override
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
            currentWord = (currentWord + Character.valueOf(c));
            boolean _isSplitHere = LcarsField.this.isSplitHere(c);
            if (_isSplitHere) {
              ObservableList<Node> _children_1 = it.getChildren();
              Text _text_1 = new Text(currentWord);
              final Procedure1<Text> _function_1 = new Procedure1<Text>() {
                @Override
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
            @Override
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
          @Override
          public void handle(final MouseEvent event) {
            Iterable<Text> _allTextNodes = LcarsField.this.getAllTextNodes();
            final Consumer<Text> _function = new Consumer<Text>() {
              @Override
              public void accept(final Text it) {
                it.setFill(LcarsExtensions.RED);
              }
            };
            _allTextNodes.forEach(_function);
            RectangleBorderPane _createQueryProgress = LcarsField.this.createQueryProgress();
            LcarsField.this.queryProgress = _createQueryProgress;
            LcarsNode _lcarsNode = LcarsField.this.getLcarsNode();
            Node _node = _lcarsNode.getNode();
            ObservableList<Node> _children = ((Pane) _node).getChildren();
            _children.add(LcarsField.this.queryProgress);
            MouseButton _button = event.getButton();
            boolean _notEquals = (!Objects.equal(_button, MouseButton.PRIMARY));
            if (_notEquals) {
              final Service<Void> _function_1 = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                  return new LcarsQueryTask(LcarsField.this, name, value, connectionProvider);
                }
              };
              final Service<Void> service = _function_1;
              service.start();
            }
          }
        };
        it.setOnMousePressed(_function_2);
        final EventHandler<MouseEvent> _function_3 = new EventHandler<MouseEvent>() {
          @Override
          public void handle(final MouseEvent it) {
            MouseButton _button = it.getButton();
            boolean _equals = Objects.equal(_button, MouseButton.PRIMARY);
            if (_equals) {
              XDiagram _diagram = CoreExtensions.getDiagram(LcarsField.this);
              ObservableList<XNode> _nodes = _diagram.getNodes();
              Iterable<LcarsNode> _filter = Iterables.<LcarsNode>filter(_nodes, LcarsNode.class);
              final Function1<LcarsNode, Boolean> _function = new Function1<LcarsNode, Boolean>() {
                @Override
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
                    _and_2 = _equals;
                  }
                  if (!_and_2) {
                    _and_1 = false;
                  } else {
                    ObservableList<XConnection> _outgoingConnections = it.getOutgoingConnections();
                    final Function1<XConnection, Boolean> _function = new Function1<XConnection, Boolean>() {
                      @Override
                      public Boolean apply(final XConnection it) {
                        boolean _and = false;
                        XNode _target = it.getTarget();
                        boolean _equals = Objects.equal(_target, node);
                        if (!_equals) {
                          _and = false;
                        } else {
                          DomainObjectDescriptor _domainObject = it.getDomainObject();
                          boolean _equals_1 = Objects.equal(_domainObject, connectionDescriptor);
                          _and = _equals_1;
                        }
                        return Boolean.valueOf(_and);
                      }
                    };
                    boolean _exists = IterableExtensions.<XConnection>exists(_outgoingConnections, _function);
                    boolean _not = (!_exists);
                    _and_1 = _not;
                  }
                  if (!_and_1) {
                    _and = false;
                  } else {
                    ObservableList<XConnection> _incomingConnections = it.getIncomingConnections();
                    final Function1<XConnection, Boolean> _function_1 = new Function1<XConnection, Boolean>() {
                      @Override
                      public Boolean apply(final XConnection it) {
                        boolean _and = false;
                        XNode _source = it.getSource();
                        boolean _equals = Objects.equal(_source, node);
                        if (!_equals) {
                          _and = false;
                        } else {
                          DomainObjectDescriptor _domainObject = it.getDomainObject();
                          boolean _equals_1 = Objects.equal(_domainObject, connectionDescriptor);
                          _and = _equals_1;
                        }
                        return Boolean.valueOf(_and);
                      }
                    };
                    boolean _exists_1 = IterableExtensions.<XConnection>exists(_incomingConnections, _function_1);
                    boolean _not_1 = (!_exists_1);
                    _and = _not_1;
                  }
                  return Boolean.valueOf(_and);
                }
              };
              Iterable<LcarsNode> _filter_1 = IterableExtensions.<LcarsNode>filter(_filter, _function);
              final Function1<LcarsNode, XConnection> _function_1 = new Function1<LcarsNode, XConnection>() {
                @Override
                public XConnection apply(final LcarsNode it) {
                  return connectionProvider.getConnection(node, it, null);
                }
              };
              Iterable<XConnection> _map = IterableExtensions.<LcarsNode, XConnection>map(_filter_1, _function_1);
              final List<XConnection> newConnections = IterableExtensions.<XConnection>toList(_map);
              XRoot _root = CoreExtensions.getRoot(LcarsField.this);
              CommandStack _commandStack = _root.getCommandStack();
              XDiagram _diagram_1 = CoreExtensions.getDiagram(LcarsField.this);
              AddRemoveCommand _newAddCommand = AddRemoveCommand.newAddCommand(_diagram_1, ((XShape[])Conversions.unwrapArray(newConnections, XShape.class)));
              _commandStack.execute(_newAddCommand);
              boolean _isEmpty = newConnections.isEmpty();
              boolean _not = (!_isEmpty);
              if (_not) {
                LayoutAction _layoutAction = new LayoutAction(LayoutType.DOT);
                XRoot _root_1 = CoreExtensions.getRoot(LcarsField.this);
                _layoutAction.perform(_root_1);
              }
              LcarsField.this.resetVisuals();
            }
          }
        };
        it.setOnMouseReleased(_function_3);
      }
    };
    FlowPane _doubleArrow = ObjectExtensions.<FlowPane>operator_doubleArrow(_flowPane, _function_1);
    _children.add((this.flowPane = _doubleArrow));
  }
  
  public LcarsNode getLcarsNode() {
    return this.node;
  }
  
  protected Iterable<Text> getAllTextNodes() {
    ObservableList<Node> _children = this.flowPane.getChildren();
    return Iterables.<Text>filter(_children, Text.class);
  }
  
  protected boolean isSplitHere(final char c) {
    return Character.isWhitespace(c);
  }
  
  public Timeline addAnimation(final Timeline timeline) {
    Timeline _xblockexpression = null;
    {
      final Procedure1<Timeline> _function = new Procedure1<Timeline>() {
        @Override
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
      _xblockexpression = timeline;
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
    final Consumer<Text> _function = new Consumer<Text>() {
      @Override
      public void accept(final Text it) {
        it.setFill(LcarsExtensions.ORANGE);
      }
    };
    _tail.forEach(_function);
  }
  
  public RectangleBorderPane createQueryProgress() {
    RectangleBorderPane _xblockexpression = null;
    {
      final Text label = new Text();
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
        @Override
        public void apply(final RectangleBorderPane it) {
          ObservableList<Node> _children = it.getChildren();
          final Procedure1<Text> _function = new Procedure1<Text>() {
            @Override
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
        @Override
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
      _xblockexpression = node;
    }
    return _xblockexpression;
  }
}
