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
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.core.viewport.ViewportTransition;
import eu.hansolo.enzo.radialmenu.SymbolType;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ZoomToFitAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    return ((it.isShortcutDown() && (!it.isShiftDown())) && Objects.equal(it.getCode(), KeyCode.F));
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.ZOOM_IN;
  }
  
  @Override
  public String getTooltip() {
    return "Fit selection";
  }
  
  @Override
  public void perform(final XRoot root) {
    final ViewportCommand _function = new ViewportCommand() {
      @Override
      public ViewportTransition createViewportTransiton(final CommandContext it) {
        ViewportTransition _xblockexpression = null;
        {
          Iterable<? extends XShape> _xifexpression = null;
          boolean _isEmpty = IterableExtensions.isEmpty(root.getCurrentSelection());
          if (_isEmpty) {
            ObservableList<XNode> _nodes = root.getDiagram().getNodes();
            ObservableList<XConnection> _connections = root.getDiagram().getConnections();
            _xifexpression = Iterables.<XDomainObjectShape>concat(_nodes, _connections);
          } else {
            _xifexpression = root.getCurrentSelection();
          }
          final Iterable<? extends XShape> elements = _xifexpression;
          final Function1<XShape, Bounds> _function = (XShape it_1) -> {
            return CoreExtensions.localToRootDiagram(it_1, it_1.getSnapBounds());
          };
          final Function2<Bounds, Bounds, Bounds> _function_1 = (Bounds a, Bounds b) -> {
            return BoundsExtensions.operator_plus(a, b);
          };
          final Bounds selectionBounds = IterableExtensions.<Bounds>reduce(IterableExtensions.map(elements, _function), _function_1);
          ViewportTransition _xifexpression_1 = null;
          if ((((!Objects.equal(selectionBounds, null)) && (selectionBounds.getWidth() > NumberExpressionExtensions.EPSILON)) && (selectionBounds.getHeight() > NumberExpressionExtensions.EPSILON))) {
            ViewportTransition _xblockexpression_1 = null;
            {
              double _width = root.getScene().getWidth();
              double _width_1 = selectionBounds.getWidth();
              double _divide = (_width / _width_1);
              double _height = root.getScene().getHeight();
              double _height_1 = selectionBounds.getHeight();
              double _divide_1 = (_height / _height_1);
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
    root.getCommandStack().execute(command);
  }
}
