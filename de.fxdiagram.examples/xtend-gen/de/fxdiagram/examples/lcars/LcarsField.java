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
    final ChooserConnectionProvider _function = (XNode host, XNode choice, DomainObjectDescriptor choiceInfo) -> {
      XConnection _xConnection = new XConnection(host, choice, connectionDescriptor);
      final Procedure1<XConnection> _function_1 = (XConnection it) -> {
        TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, true);
        it.setSourceArrowHead(_triangleArrowHead);
        XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
        final Procedure1<XConnectionLabel> _function_2 = (XConnectionLabel it_1) -> {
          Text _text = it_1.getText();
          String _replace = name.replace("_", " ");
          _text.setText(_replace);
        };
        ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function_2);
      };
      return ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function_1);
    };
    final ChooserConnectionProvider connectionProvider = _function;
    ObservableList<Node> _children = this.getChildren();
    FlowPane _flowPane = new FlowPane();
    final Procedure1<FlowPane> _function_1 = (FlowPane it) -> {
      it.setPrefWrapLength(150);
      ObservableList<Node> _children_1 = it.getChildren();
      Text _text = new Text();
      final Procedure1<Text> _function_2 = (Text it_1) -> {
        String _replace = name.replace("_", " ");
        String _plus = (_replace + ": ");
        it_1.setText(_plus);
        Font _lcarsFont = LcarsExtensions.lcarsFont(12);
        it_1.setFont(_lcarsFont);
        it_1.setFill(LcarsExtensions.FLESH);
      };
      Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_2);
      _children_1.add(_doubleArrow);
      String currentWord = "";
      char[] _charArray = value.toCharArray();
      for (final char c : _charArray) {
        {
          currentWord = (currentWord + Character.valueOf(c));
          boolean _isSplitHere = this.isSplitHere(c);
          if (_isSplitHere) {
            ObservableList<Node> _children_2 = it.getChildren();
            Text _text_1 = new Text(currentWord);
            final Procedure1<Text> _function_3 = (Text it_1) -> {
              Font _lcarsFont = LcarsExtensions.lcarsFont(12);
              it_1.setFont(_lcarsFont);
              it_1.setFill(LcarsExtensions.ORANGE);
            };
            Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_text_1, _function_3);
            _children_2.add(_doubleArrow_1);
            currentWord = "";
          }
        }
      }
      boolean _isEmpty = currentWord.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        ObservableList<Node> _children_2 = it.getChildren();
        Text _text_1 = new Text(currentWord);
        final Procedure1<Text> _function_3 = (Text it_1) -> {
          Font _lcarsFont = LcarsExtensions.lcarsFont(12);
          it_1.setFont(_lcarsFont);
          it_1.setFill(LcarsExtensions.ORANGE);
        };
        Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_text_1, _function_3);
        _children_2.add(_doubleArrow_1);
      }
      final EventHandler<MouseEvent> _function_4 = (MouseEvent event) -> {
        Iterable<Text> _allTextNodes = this.getAllTextNodes();
        final Consumer<Text> _function_5 = (Text it_1) -> {
          it_1.setFill(LcarsExtensions.RED);
        };
        _allTextNodes.forEach(_function_5);
        RectangleBorderPane _createQueryProgress = this.createQueryProgress();
        this.queryProgress = _createQueryProgress;
        LcarsNode _lcarsNode = this.getLcarsNode();
        Node _node = _lcarsNode.getNode();
        ObservableList<Node> _children_3 = ((Pane) _node).getChildren();
        _children_3.add(this.queryProgress);
        MouseButton _button = event.getButton();
        boolean _notEquals = (!Objects.equal(_button, MouseButton.PRIMARY));
        if (_notEquals) {
          final Service<Void> _function_6 = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
              return new LcarsQueryTask(LcarsField.this, name, value, connectionProvider);
            }
          };
          final Service<Void> service = _function_6;
          service.start();
        }
      };
      it.setOnMousePressed(_function_4);
      final EventHandler<MouseEvent> _function_5 = (MouseEvent it_1) -> {
        MouseButton _button = it_1.getButton();
        boolean _equals = Objects.equal(_button, MouseButton.PRIMARY);
        if (_equals) {
          XDiagram _diagram = CoreExtensions.getDiagram(this);
          ObservableList<XNode> _nodes = _diagram.getNodes();
          Iterable<LcarsNode> _filter = Iterables.<LcarsNode>filter(_nodes, LcarsNode.class);
          final Function1<LcarsNode, Boolean> _function_6 = (LcarsNode it_2) -> {
            boolean _and = false;
            boolean _and_1 = false;
            boolean _and_2 = false;
            boolean _notEquals = (!Objects.equal(it_2, node));
            if (!_notEquals) {
              _and_2 = false;
            } else {
              DBObject _data = it_2.getData();
              Object _get = _data.get(name);
              boolean _equals_1 = Objects.equal(_get, value);
              _and_2 = _equals_1;
            }
            if (!_and_2) {
              _and_1 = false;
            } else {
              ObservableList<XConnection> _outgoingConnections = it_2.getOutgoingConnections();
              final Function1<XConnection, Boolean> _function_7 = (XConnection it_3) -> {
                boolean _and_3 = false;
                XNode _target = it_3.getTarget();
                boolean _equals_2 = Objects.equal(_target, node);
                if (!_equals_2) {
                  _and_3 = false;
                } else {
                  DomainObjectDescriptor _domainObject = it_3.getDomainObject();
                  boolean _equals_3 = Objects.equal(_domainObject, connectionDescriptor);
                  _and_3 = _equals_3;
                }
                return Boolean.valueOf(_and_3);
              };
              boolean _exists = IterableExtensions.<XConnection>exists(_outgoingConnections, _function_7);
              boolean _not_1 = (!_exists);
              _and_1 = _not_1;
            }
            if (!_and_1) {
              _and = false;
            } else {
              ObservableList<XConnection> _incomingConnections = it_2.getIncomingConnections();
              final Function1<XConnection, Boolean> _function_8 = (XConnection it_3) -> {
                boolean _and_3 = false;
                XNode _source = it_3.getSource();
                boolean _equals_2 = Objects.equal(_source, node);
                if (!_equals_2) {
                  _and_3 = false;
                } else {
                  DomainObjectDescriptor _domainObject = it_3.getDomainObject();
                  boolean _equals_3 = Objects.equal(_domainObject, connectionDescriptor);
                  _and_3 = _equals_3;
                }
                return Boolean.valueOf(_and_3);
              };
              boolean _exists_1 = IterableExtensions.<XConnection>exists(_incomingConnections, _function_8);
              boolean _not_2 = (!_exists_1);
              _and = _not_2;
            }
            return Boolean.valueOf(_and);
          };
          Iterable<LcarsNode> _filter_1 = IterableExtensions.<LcarsNode>filter(_filter, _function_6);
          final Function1<LcarsNode, XConnection> _function_7 = (LcarsNode it_2) -> {
            return connectionProvider.getConnection(node, it_2, null);
          };
          Iterable<XConnection> _map = IterableExtensions.<LcarsNode, XConnection>map(_filter_1, _function_7);
          final List<XConnection> newConnections = IterableExtensions.<XConnection>toList(_map);
          XRoot _root_1 = CoreExtensions.getRoot(this);
          CommandStack _commandStack = _root_1.getCommandStack();
          XDiagram _diagram_1 = CoreExtensions.getDiagram(this);
          AddRemoveCommand _newAddCommand = AddRemoveCommand.newAddCommand(_diagram_1, ((XShape[])Conversions.unwrapArray(newConnections, XShape.class)));
          _commandStack.execute(_newAddCommand);
          boolean _isEmpty_1 = newConnections.isEmpty();
          boolean _not_1 = (!_isEmpty_1);
          if (_not_1) {
            LayoutAction _layoutAction = new LayoutAction(LayoutType.DOT);
            XRoot _root_2 = CoreExtensions.getRoot(this);
            _layoutAction.perform(_root_2);
          }
          this.resetVisuals();
        }
      };
      it.setOnMouseReleased(_function_5);
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
      final Procedure1<Timeline> _function = (Timeline it) -> {
        Iterable<Text> _allTextNodes = this.getAllTextNodes();
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
    final Consumer<Text> _function = (Text it) -> {
      it.setFill(LcarsExtensions.ORANGE);
    };
    _tail.forEach(_function);
  }
  
  public RectangleBorderPane createQueryProgress() {
    RectangleBorderPane _xblockexpression = null;
    {
      final Text label = new Text();
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
        ObservableList<Node> _children = it.getChildren();
        final Procedure1<Text> _function_1 = (Text it_1) -> {
          it_1.setText("querying...");
          Font _lcarsFont = LcarsExtensions.lcarsFont(42);
          it_1.setFont(_lcarsFont);
          it_1.setFill(LcarsExtensions.ORANGE);
          Insets _insets = new Insets(5, 5, 5, 5);
          StackPane.setMargin(it_1, _insets);
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(label, _function_1);
        _children.add(_doubleArrow);
        it.setBackgroundPaint(Color.BLACK);
        it.setBackgroundRadius(10);
        it.setBorderWidth(0);
      };
      final RectangleBorderPane node = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
      Timeline _timeline = new Timeline();
      final Procedure1<Timeline> _function_1 = (Timeline it) -> {
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
      };
      ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function_1);
      _xblockexpression = node;
    }
    return _xblockexpression;
  }
}
