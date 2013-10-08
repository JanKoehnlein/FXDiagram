package de.fxdiagram.core.debug;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.extensions.CoreExtensions;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.eclipse.xtend2.lib.StringConcatenation;

@Logging
@SuppressWarnings("all")
public class Debug {
  public static void debugTranslation(final Node node) {
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
      public void changed(final ObservableValue<? extends Number> element, final Number oldVal, final Number newVal) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Position changed from ");
        _builder.append(oldVal, "");
        _builder.append(" to ");
        _builder.append(newVal, "");
        Exception _exception = new Exception(_builder.toString());
        _exception.printStackTrace();
      }
    };
    final ChangeListener<Number> debugger = _function;
    Node currentNode = node;
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(currentNode, null));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _isRootDiagram = CoreExtensions.isRootDiagram(currentNode);
      boolean _not = (!_isRootDiagram);
      _and = (_notEquals && _not);
    }
    boolean _while = _and;
    while (_while) {
      {
        DoubleProperty _layoutXProperty = currentNode.layoutXProperty();
        _layoutXProperty.addListener(debugger);
        DoubleProperty _layoutYProperty = currentNode.layoutYProperty();
        _layoutYProperty.addListener(debugger);
        DoubleProperty _translateXProperty = currentNode.translateXProperty();
        _translateXProperty.addListener(debugger);
        DoubleProperty _translateYProperty = currentNode.translateYProperty();
        _translateYProperty.addListener(debugger);
        Parent _parent = currentNode.getParent();
        currentNode = _parent;
      }
      boolean _and_1 = false;
      boolean _notEquals_1 = (!Objects.equal(currentNode, null));
      if (!_notEquals_1) {
        _and_1 = false;
      } else {
        boolean _isRootDiagram_1 = CoreExtensions.isRootDiagram(currentNode);
        boolean _not_1 = (!_isRootDiagram_1);
        _and_1 = (_notEquals_1 && _not_1);
      }
      _while = _and_1;
    }
  }
  
  public static void debugSize(final Node node) {
    final ChangeListener<Bounds> _function = new ChangeListener<Bounds>() {
      public void changed(final ObservableValue<? extends Bounds> element, final Bounds oldVal, final Bounds newVal) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Bounds changed from");
        _builder.newLine();
        _builder.append("\t");
        _builder.append(oldVal, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("to");
        _builder.newLine();
        _builder.append("\t");
        _builder.append(newVal, "\t");
        _builder.newLineIfNotEmpty();
        Exception _exception = new Exception(_builder.toString());
        _exception.printStackTrace();
      }
    };
    final ChangeListener<Bounds> debugger = _function;
    Node currentNode = node;
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(currentNode, null));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _isRootDiagram = CoreExtensions.isRootDiagram(currentNode);
      boolean _not = (!_isRootDiagram);
      _and = (_notEquals && _not);
    }
    boolean _while = _and;
    while (_while) {
      {
        ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty = currentNode.layoutBoundsProperty();
        _layoutBoundsProperty.addListener(debugger);
        Parent _parent = currentNode.getParent();
        currentNode = _parent;
      }
      boolean _and_1 = false;
      boolean _notEquals_1 = (!Objects.equal(currentNode, null));
      if (!_notEquals_1) {
        _and_1 = false;
      } else {
        boolean _isRootDiagram_1 = CoreExtensions.isRootDiagram(currentNode);
        boolean _not_1 = (!_isRootDiagram_1);
        _and_1 = (_notEquals_1 && _not_1);
      }
      _while = _and_1;
    }
  }
  
  public static void dumpLayout(final Node it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Layout of ");
    Class<? extends Node> _class = it.getClass();
    String _simpleName = _class.getSimpleName();
    _builder.append(_simpleName, "");
    _builder.append(": (");
    double _layoutX = it.getLayoutX();
    _builder.append(_layoutX, "");
    _builder.append(":");
    double _layoutY = it.getLayoutY();
    _builder.append(_layoutY, "");
    _builder.append(") ");
    Bounds _layoutBounds = it.getLayoutBounds();
    _builder.append(_layoutBounds, "");
    Debug.LOG.info(_builder.toString());
  }
  
  public static void dumpBounds(final Node it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Bounds of ");
    Class<? extends Node> _class = it.getClass();
    String _simpleName = _class.getSimpleName();
    _builder.append(_simpleName, "");
    _builder.append(":");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("(");
    double _layoutX = it.getLayoutX();
    _builder.append(_layoutX, "\t");
    _builder.append(":");
    double _layoutY = it.getLayoutY();
    _builder.append(_layoutY, "\t");
    _builder.append(") ");
    Bounds _layoutBounds = it.getLayoutBounds();
    _builder.append(_layoutBounds, "\t");
    _builder.newLineIfNotEmpty();
    String message = _builder.toString();
    Node current = it;
    double _layoutX_1 = it.getLayoutX();
    double _layoutY_1 = it.getLayoutY();
    Point2D _point2D = new Point2D(_layoutX_1, _layoutY_1);
    Point2D currentPosition = _point2D;
    Bounds currentBounds = it.getLayoutBounds();
    Parent _parent = current.getParent();
    boolean _notEquals = (!Objects.equal(_parent, null));
    boolean _while = _notEquals;
    while (_while) {
      {
        Bounds _localToParent = current.localToParent(currentBounds);
        currentBounds = _localToParent;
        Point2D _localToParent_1 = current.localToParent(currentPosition);
        currentPosition = _localToParent_1;
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(null, "");
        _builder_1.append("\tin ");
        Parent _parent_1 = current.getParent();
        Class<? extends Parent> _class_1 = _parent_1.getClass();
        String _simpleName_1 = _class_1.getSimpleName();
        _builder_1.append(_simpleName_1, "");
        _builder_1.append(": (");
        double _x = currentPosition.getX();
        _builder_1.append(_x, "");
        _builder_1.append(":");
        double _y = currentPosition.getY();
        _builder_1.append(_y, "");
        _builder_1.append(") ");
        _builder_1.append(currentBounds, "");
        _builder_1.newLineIfNotEmpty();
        String _plus = (message + _builder_1);
        message = _plus;
        Parent _parent_2 = current.getParent();
        current = _parent_2;
      }
      Parent _parent_1 = current.getParent();
      boolean _notEquals_1 = (!Objects.equal(_parent_1, null));
      _while = _notEquals_1;
    }
    Debug.LOG.info(message);
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.debug.Debug");
    ;
}
