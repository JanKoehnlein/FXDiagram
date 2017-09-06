package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.ViewportCommand;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.core.viewport.ViewportMemento;
import de.fxdiagram.core.viewport.ViewportTransform;
import de.fxdiagram.core.viewport.ViewportTransition;
import eu.hansolo.enzo.radialmenu.SymbolType;
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
    return (it.isShortcutDown() && Objects.equal(it.getCode(), KeyCode.R));
  }
  
  @Override
  public SymbolType getSymbol() {
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
      Iterable<? extends XShape> _xifexpression = null;
      boolean _isEmpty = IterableExtensions.isEmpty(root.getCurrentSelection());
      if (_isEmpty) {
        ObservableList<XNode> _nodes = root.getDiagram().getNodes();
        ObservableList<XConnection> _connections = root.getDiagram().getConnections();
        _xifexpression = Iterables.<XDomainObjectShape>concat(_nodes, _connections);
      } else {
        _xifexpression = root.getCurrentSelection();
      }
      _elvis = _xifexpression;
    }
    final Iterable<? extends XShape> theNodes = _elvis;
    final Function1<XShape, Bounds> _function = (XShape it) -> {
      return it.localToScene(it.getBoundsInLocal());
    };
    final Function2<Bounds, Bounds, Bounds> _function_1 = (Bounds $0, Bounds $1) -> {
      return BoundsExtensions.operator_plus($0, $1);
    };
    return IterableExtensions.<Bounds>reduce(IterableExtensions.map(theNodes, _function), _function_1);
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
        if (((scene.getWidth() < boundsInScene.getWidth()) || (scene.getHeight() < boundsInScene.getHeight()))) {
          final Bounds boundsInDiagram = root.getDiagram().sceneToLocal(boundsInScene);
          if (((boundsInDiagram.getWidth() > NumberExpressionExtensions.EPSILON) && (boundsInDiagram.getHeight() > NumberExpressionExtensions.EPSILON))) {
            double _width = scene.getWidth();
            double _width_1 = boundsInDiagram.getWidth();
            double _divide = (_width / _width_1);
            double _height = scene.getHeight();
            double _height_1 = boundsInDiagram.getHeight();
            double _divide_1 = (_height / _height_1);
            final double targetScale = Math.min(_divide, _divide_1);
            Point2D _center = BoundsExtensions.center(boundsInDiagram);
            return new ViewportTransition(root, _center, targetScale, 0);
          } else {
            return null;
          }
        }
        double _xifexpression = (double) 0;
        double _minX = boundsInScene.getMinX();
        boolean _lessThan = (_minX < 0);
        if (_lessThan) {
          double _minX_1 = boundsInScene.getMinX();
          _xifexpression = (-_minX_1);
        } else {
          double _xifexpression_1 = (double) 0;
          double _maxX = boundsInScene.getMaxX();
          double _width_2 = scene.getWidth();
          boolean _greaterThan = (_maxX > _width_2);
          if (_greaterThan) {
            double _width_3 = scene.getWidth();
            double _maxX_1 = boundsInScene.getMaxX();
            _xifexpression_1 = (_width_3 - _maxX_1);
          }
          _xifexpression = _xifexpression_1;
        }
        final double deltaX = _xifexpression;
        double _xifexpression_2 = (double) 0;
        double _minY = boundsInScene.getMinY();
        boolean _lessThan_1 = (_minY < 0);
        if (_lessThan_1) {
          double _minY_1 = boundsInScene.getMinY();
          _xifexpression_2 = (-_minY_1);
        } else {
          double _xifexpression_3 = (double) 0;
          double _maxY = boundsInScene.getMaxY();
          double _height_2 = scene.getHeight();
          boolean _greaterThan_1 = (_maxY > _height_2);
          if (_greaterThan_1) {
            double _height_3 = scene.getHeight();
            double _maxY_1 = boundsInScene.getMaxY();
            _xifexpression_3 = (_height_3 - _maxY_1);
          }
          _xifexpression_2 = _xifexpression_3;
        }
        final double deltaY = _xifexpression_2;
        final ViewportTransform currentViewport = root.getDiagram().getViewportTransform();
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
    root.getCommandStack().execute(command);
  }
}
