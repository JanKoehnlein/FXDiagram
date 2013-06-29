package de.fxdiagram.core.debug;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRootDiagram;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.eclipse.xtend2.lib.StringConcatenation;

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
      boolean _not = (!(currentNode instanceof XRootDiagram));
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
        boolean _not_1 = (!(currentNode instanceof XRootDiagram));
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
          _builder.append(oldVal, "	");
          _builder.newLineIfNotEmpty();
          _builder.append("to");
          _builder.newLine();
          _builder.append("\t");
          _builder.append(newVal, "	");
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
      boolean _not = (!(currentNode instanceof XRootDiagram));
      _and = (_notEquals && _not);
    }
    boolean _while = _and;
    while (_while) {
      {
        ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = currentNode.boundsInLocalProperty();
        _boundsInLocalProperty.addListener(debugger);
        Parent _parent = currentNode.getParent();
        currentNode = _parent;
      }
      boolean _and_1 = false;
      boolean _notEquals_1 = (!Objects.equal(currentNode, null));
      if (!_notEquals_1) {
        _and_1 = false;
      } else {
        boolean _not_1 = (!(currentNode instanceof XRootDiagram));
        _and_1 = (_notEquals_1 && _not_1);
      }
      _while = _and_1;
    }
  }
}
