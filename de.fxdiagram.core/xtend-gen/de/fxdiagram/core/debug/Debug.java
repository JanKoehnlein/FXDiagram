package de.fxdiagram.core.debug;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.eclipse.xtend2.lib.StringConcatenation;

@Logging
@SuppressWarnings("all")
public class Debug {
  public static void debugTranslation(final Node node) {
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> element, Number oldVal, Number newVal) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Position changed from ");
      _builder.append(oldVal);
      _builder.append(" to ");
      _builder.append(newVal);
      new Exception(_builder.toString()).printStackTrace();
    };
    final ChangeListener<Number> debugger = _function;
    Node currentNode = node;
    while (((!Objects.equal(currentNode, null)) && (!CoreExtensions.isRootDiagram(currentNode)))) {
      {
        currentNode.layoutXProperty().addListener(debugger);
        currentNode.layoutYProperty().addListener(debugger);
        currentNode.translateXProperty().addListener(debugger);
        currentNode.translateYProperty().addListener(debugger);
        currentNode = currentNode.getParent();
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
      new Exception(_builder.toString()).printStackTrace();
    };
    final ChangeListener<Bounds> debugger = _function;
    Node currentNode = node;
    while (((!Objects.equal(currentNode, null)) && (!CoreExtensions.isRootDiagram(currentNode)))) {
      {
        currentNode.layoutBoundsProperty().addListener(debugger);
        currentNode = currentNode.getParent();
      }
    }
  }
  
  public static void dumpLayout(final Node it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Layout of ");
    String _simpleName = it.getClass().getSimpleName();
    _builder.append(_simpleName);
    _builder.append(": (");
    double _layoutX = it.getLayoutX();
    _builder.append(_layoutX);
    _builder.append(":");
    double _layoutY = it.getLayoutY();
    _builder.append(_layoutY);
    _builder.append(") ");
    Bounds _layoutBounds = it.getLayoutBounds();
    _builder.append(_layoutBounds);
    Debug.LOG.info(_builder.toString());
  }
  
  public static void dumpBounds(final Node it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Bounds of ");
    String _simpleName = it.getClass().getSimpleName();
    _builder.append(_simpleName);
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
        currentBounds = current.localToParent(currentBounds);
        currentPosition = current.localToParent(currentPosition);
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("in ");
        String _simpleName_1 = current.getParent().getClass().getSimpleName();
        _builder_1.append(_simpleName_1);
        _builder_1.append(": (");
        double _x = currentPosition.getX();
        _builder_1.append(_x);
        _builder_1.append(":");
        double _y = currentPosition.getY();
        _builder_1.append(_y);
        _builder_1.append(") ");
        _builder_1.append(currentBounds);
        _builder_1.newLineIfNotEmpty();
        String _plus = (message + _builder_1);
        message = _plus;
        current = current.getParent();
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
        final XControlPoint first = connection.getControlPoints().get((i - 1));
        final XControlPoint second = connection.getControlPoints().get(i);
        if (((Math.abs((first.getLayoutX() - second.getLayoutX())) > NumberExpressionExtensions.EPSILON) && (Math.abs((first.getLayoutY() - second.getLayoutY())) > NumberExpressionExtensions.EPSILON))) {
          throw new IllegalStateException("XConnection is no longer rectilinear");
        }
      }
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.debug.Debug");
    ;
}
