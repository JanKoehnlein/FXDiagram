package de.fxdiagram.core.debug;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.eclipse.xtend2.lib.StringConcatenation;

@Logging
@SuppressWarnings("all")
public class Debug {
  public static void debugTranslation(final Node node) {
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> element, Number oldVal, Number newVal) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Position changed from ");
      _builder.append(oldVal, "");
      _builder.append(" to ");
      _builder.append(newVal, "");
      Exception _exception = new Exception(_builder.toString());
      _exception.printStackTrace();
    };
    final ChangeListener<Number> debugger = _function;
    Node currentNode = node;
    while (((!Objects.equal(currentNode, null)) && (!CoreExtensions.isRootDiagram(currentNode)))) {
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
    }
  }
  
  public static void debugSize(final Node node) {
    final ChangeListener<Bounds> _function = (ObservableValue<? extends Bounds> element, Bounds oldVal, Bounds newVal) -> {
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
    };
    final ChangeListener<Bounds> debugger = _function;
    Node currentNode = node;
    while (((!Objects.equal(currentNode, null)) && (!CoreExtensions.isRootDiagram(currentNode)))) {
      {
        ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty = currentNode.layoutBoundsProperty();
        _layoutBoundsProperty.addListener(debugger);
        Parent _parent = currentNode.getParent();
        currentNode = _parent;
      }
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
    Point2D currentPosition = new Point2D(_layoutX_1, _layoutY_1);
    Bounds currentBounds = it.getLayoutBounds();
    while ((!Objects.equal(current.getParent(), null))) {
      {
        Bounds _localToParent = current.localToParent(currentBounds);
        currentBounds = _localToParent;
        Point2D _localToParent_1 = current.localToParent(currentPosition);
        currentPosition = _localToParent_1;
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("", "");
        _builder_1.append("\tin ");
        Parent _parent = current.getParent();
        Class<? extends Parent> _class_1 = _parent.getClass();
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
        Parent _parent_1 = current.getParent();
        current = _parent_1;
      }
    }
    Debug.LOG.info(message);
  }
  
  public static void checkRectilinearity(final XConnection connection) {
    XConnection.Kind _kind = connection.getKind();
    boolean _notEquals = (!Objects.equal(_kind, XConnection.Kind.RECTILINEAR));
    if (_notEquals) {
      return;
    }
    for (int i = 1; (i < connection.getControlPoints().size()); i++) {
      {
        ObservableList<XControlPoint> _controlPoints = connection.getControlPoints();
        final XControlPoint first = _controlPoints.get((i - 1));
        ObservableList<XControlPoint> _controlPoints_1 = connection.getControlPoints();
        final XControlPoint second = _controlPoints_1.get(i);
        if (((Math.abs((first.getLayoutX() - second.getLayoutX())) > NumberExpressionExtensions.EPSILON) && (Math.abs((first.getLayoutY() - second.getLayoutY())) > NumberExpressionExtensions.EPSILON))) {
          throw new IllegalStateException("XConnection is no longer rectilinear");
        }
      }
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.debug.Debug");
    ;
}
