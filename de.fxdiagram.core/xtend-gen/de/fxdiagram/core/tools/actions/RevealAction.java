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
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.core.viewport.ViewportMemento;
import de.fxdiagram.core.viewport.ViewportTransform;
import de.fxdiagram.core.viewport.ViewportTransition;
import eu.hansolo.enzo.radialmenu.Symbol;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class RevealAction implements DiagramAction {
  private Iterable<? extends XShape> nodes;
  
  public RevealAction(final Iterable<? extends XShape> nodes) {
    this.nodes = nodes;
  }
  
  public RevealAction() {
  }
  
  @Override
  public boolean matches(final KeyEvent it) {
    boolean _and = false;
    boolean _isShortcutDown = it.isShortcutDown();
    if (!_isShortcutDown) {
      _and = false;
    } else {
      KeyCode _code = it.getCode();
      boolean _equals = Objects.equal(_code, KeyCode.R);
      _and = _equals;
    }
    return _and;
  }
  
  @Override
  public Symbol.Type getSymbol() {
    return null;
  }
  
  @Override
  public String getTooltip() {
    return "Reveal selection";
  }
  
  protected Bounds getBoundsInScene(final XRoot root) {
    Iterable<? extends XShape> _elvis = null;
    if (this.nodes != null) {
      _elvis = this.nodes;
    } else {
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
      _elvis = _xifexpression;
    }
    final Iterable<? extends XShape> theNodes = _elvis;
    final Function1<XShape, Bounds> _function = new Function1<XShape, Bounds>() {
      @Override
      public Bounds apply(final XShape it) {
        Bounds _boundsInLocal = it.getBoundsInLocal();
        return it.localToScene(_boundsInLocal);
      }
    };
    Iterable<Bounds> _map = IterableExtensions.map(theNodes, _function);
    final Function2<Bounds, Bounds, Bounds> _function_1 = new Function2<Bounds, Bounds, Bounds>() {
      @Override
      public Bounds apply(final Bounds $0, final Bounds $1) {
        return BoundsExtensions.operator_plus($0, $1);
      }
    };
    return IterableExtensions.<Bounds>reduce(_map, _function_1);
  }
  
  @Override
  public void perform(final XRoot root) {
    final ViewportCommand _function = new ViewportCommand() {
      @Override
      public ViewportTransition createViewportTransiton(final CommandContext it) {
        final Bounds boundsInScene = RevealAction.this.getBoundsInScene(root);
        boolean _equals = Objects.equal(boundsInScene, null);
        if (_equals) {
          return null;
        }
        final Scene scene = root.getScene();
        boolean _or = false;
        double _width = scene.getWidth();
        double _width_1 = boundsInScene.getWidth();
        boolean _lessThan = (_width < _width_1);
        if (_lessThan) {
          _or = true;
        } else {
          double _height = scene.getHeight();
          double _height_1 = boundsInScene.getHeight();
          boolean _lessThan_1 = (_height < _height_1);
          _or = _lessThan_1;
        }
        if (_or) {
          XDiagram _diagram = root.getDiagram();
          final Bounds boundsInDiagram = _diagram.sceneToLocal(boundsInScene);
          boolean _and = false;
          double _width_2 = boundsInDiagram.getWidth();
          boolean _greaterThan = (_width_2 > NumberExpressionExtensions.EPSILON);
          if (!_greaterThan) {
            _and = false;
          } else {
            double _height_2 = boundsInDiagram.getHeight();
            boolean _greaterThan_1 = (_height_2 > NumberExpressionExtensions.EPSILON);
            _and = _greaterThan_1;
          }
          if (_and) {
            double _width_3 = scene.getWidth();
            double _width_4 = boundsInDiagram.getWidth();
            double _divide = (_width_3 / _width_4);
            double _height_3 = scene.getHeight();
            double _height_4 = boundsInDiagram.getHeight();
            double _divide_1 = (_height_3 / _height_4);
            final double targetScale = Math.min(_divide, _divide_1);
            Point2D _center = BoundsExtensions.center(boundsInDiagram);
            return new ViewportTransition(root, _center, targetScale, 0);
          } else {
            return null;
          }
        }
        double _xifexpression = (double) 0;
        double _minX = boundsInScene.getMinX();
        boolean _lessThan_2 = (_minX < 0);
        if (_lessThan_2) {
          double _minX_1 = boundsInScene.getMinX();
          _xifexpression = (-_minX_1);
        } else {
          double _xifexpression_1 = (double) 0;
          double _maxX = boundsInScene.getMaxX();
          double _width_5 = scene.getWidth();
          boolean _greaterThan_2 = (_maxX > _width_5);
          if (_greaterThan_2) {
            double _width_6 = scene.getWidth();
            double _maxX_1 = boundsInScene.getMaxX();
            _xifexpression_1 = (_width_6 - _maxX_1);
          }
          _xifexpression = _xifexpression_1;
        }
        final double deltaX = _xifexpression;
        double _xifexpression_2 = (double) 0;
        double _minY = boundsInScene.getMinY();
        boolean _lessThan_3 = (_minY < 0);
        if (_lessThan_3) {
          double _minY_1 = boundsInScene.getMinY();
          _xifexpression_2 = (-_minY_1);
        } else {
          double _xifexpression_3 = (double) 0;
          double _maxY = boundsInScene.getMaxY();
          double _height_5 = scene.getHeight();
          boolean _greaterThan_3 = (_maxY > _height_5);
          if (_greaterThan_3) {
            double _height_6 = scene.getHeight();
            double _maxY_1 = boundsInScene.getMaxY();
            _xifexpression_3 = (_height_6 - _maxY_1);
          }
          _xifexpression_2 = _xifexpression_3;
        }
        final double deltaY = _xifexpression_2;
        XDiagram _diagram_1 = root.getDiagram();
        final ViewportTransform currentViewport = _diagram_1.getViewportTransform();
        double _translateX = currentViewport.getTranslateX();
        double _plus = (_translateX + deltaX);
        double _translateY = currentViewport.getTranslateY();
        double _plus_1 = (_translateY + deltaY);
        double _scale = currentViewport.getScale();
        double _rotate = currentViewport.getRotate();
        final ViewportMemento targetViewport = new ViewportMemento(_plus, _plus_1, _scale, _rotate);
        Duration _millis = DurationExtensions.millis(500);
        return new ViewportTransition(root, targetViewport, _millis);
      }
    };
    final ViewportCommand command = _function;
    CommandStack _commandStack = root.getCommandStack();
    _commandStack.execute(command);
  }
}
