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
public class CenterAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    return (it.isShortcutDown() && Objects.equal(it.getCode(), KeyCode.C));
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.SELECTION2;
  }
  
  @Override
  public String getTooltip() {
    return "Center selection";
  }
  
  @Override
  public void perform(final XRoot root) {
    final ViewportCommand _function = new ViewportCommand() {
      @Override
      public ViewportTransition createViewportTransiton(final CommandContext it) {
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
        if ((((!Objects.equal(selectionBounds, null)) && (selectionBounds.getWidth() > NumberExpressionExtensions.EPSILON)) && (selectionBounds.getHeight() > NumberExpressionExtensions.EPSILON))) {
          double _width = root.getScene().getWidth();
          double _width_1 = selectionBounds.getWidth();
          double _divide = (_width / _width_1);
          double _height = root.getScene().getHeight();
          double _height_1 = selectionBounds.getHeight();
          double _divide_1 = (_height / _height_1);
          final double targetScale = Math.min(1, 
            Math.min(_divide, _divide_1));
          Point2D _center = BoundsExtensions.center(selectionBounds);
          return new ViewportTransition(root, _center, targetScale);
        } else {
          return null;
        }
      }
    };
    final ViewportCommand command = _function;
    root.getCommandStack().execute(command);
  }
}
