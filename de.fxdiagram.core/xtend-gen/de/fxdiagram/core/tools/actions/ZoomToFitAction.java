package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.ViewportCommand;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.core.viewport.ViewportTransition;
import eu.hansolo.enzo.radialmenu.Symbol;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ZoomToFitAction implements DiagramAction {
  public boolean matches(final KeyEvent it) {
    boolean _and = false;
    boolean _and_1 = false;
    boolean _isShortcutDown = it.isShortcutDown();
    if (!_isShortcutDown) {
      _and_1 = false;
    } else {
      boolean _isShiftDown = it.isShiftDown();
      boolean _not = (!_isShiftDown);
      _and_1 = _not;
    }
    if (!_and_1) {
      _and = false;
    } else {
      KeyCode _code = it.getCode();
      boolean _equals = Objects.equal(_code, KeyCode.F);
      _and = _equals;
    }
    return _and;
  }
  
  public Symbol.Type getSymbol() {
    return Symbol.Type.ZOOM_IN;
  }
  
  public String getTooltip() {
    return "Fit selection";
  }
  
  public void perform(final XRoot root) {
    final ViewportCommand _function = new ViewportCommand() {
      @Override
      public ViewportTransition createViewportTransiton(final CommandContext it) {
        ViewportTransition _xblockexpression = null;
        {
          Iterable<XShape> _xifexpression = null;
          Iterable<XShape> _currentSelection = root.getCurrentSelection();
          boolean _isEmpty = IterableExtensions.isEmpty(_currentSelection);
          if (_isEmpty) {
            XDiagram _diagram = root.getDiagram();
            ObservableList<XNode> _nodes = _diagram.getNodes();
            XDiagram _diagram_1 = root.getDiagram();
            ObservableList<XConnection> _connections = _diagram_1.getConnections();
            _xifexpression = Iterables.<XShape>concat(_nodes, _connections);
          } else {
            _xifexpression = root.getCurrentSelection();
          }
          final Iterable<XShape> elements = _xifexpression;
          final Function1<XShape, Bounds> _function = new Function1<XShape, Bounds>() {
            public Bounds apply(final XShape it) {
              Bounds _snapBounds = it.getSnapBounds();
              return CoreExtensions.localToRootDiagram(it, _snapBounds);
            }
          };
          Iterable<Bounds> _map = IterableExtensions.<XShape, Bounds>map(elements, _function);
          final Function2<Bounds, Bounds, Bounds> _function_1 = new Function2<Bounds, Bounds, Bounds>() {
            public Bounds apply(final Bounds a, final Bounds b) {
              return BoundsExtensions.operator_plus(a, b);
            }
          };
          final Bounds selectionBounds = IterableExtensions.<Bounds>reduce(_map, _function_1);
          ViewportTransition _xifexpression_1 = null;
          boolean _and = false;
          boolean _and_1 = false;
          boolean _notEquals = (!Objects.equal(selectionBounds, null));
          if (!_notEquals) {
            _and_1 = false;
          } else {
            double _width = selectionBounds.getWidth();
            boolean _greaterThan = (_width > NumberExpressionExtensions.EPSILON);
            _and_1 = _greaterThan;
          }
          if (!_and_1) {
            _and = false;
          } else {
            double _height = selectionBounds.getHeight();
            boolean _greaterThan_1 = (_height > NumberExpressionExtensions.EPSILON);
            _and = _greaterThan_1;
          }
          if (_and) {
            ViewportTransition _xblockexpression_1 = null;
            {
              Scene _scene = root.getScene();
              double _width_1 = _scene.getWidth();
              double _width_2 = selectionBounds.getWidth();
              double _divide = (_width_1 / _width_2);
              Scene _scene_1 = root.getScene();
              double _height_1 = _scene_1.getHeight();
              double _height_2 = selectionBounds.getHeight();
              double _divide_1 = (_height_1 / _height_2);
              final double targetScale = Math.min(_divide, _divide_1);
              Point2D _center = BoundsExtensions.center(selectionBounds);
              _xblockexpression_1 = new ViewportTransition(root, _center, targetScale, 0);
            }
            _xifexpression_1 = _xblockexpression_1;
          } else {
            _xifexpression_1 = null;
          }
          _xblockexpression = _xifexpression_1;
        }
        return _xblockexpression;
      }
    };
    final ViewportCommand command = _function;
    CommandStack _commandStack = root.getCommandStack();
    _commandStack.execute(command);
  }
}
