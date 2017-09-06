package de.fxdiagram.core.command;

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
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.viewport.ViewportTransition;
import java.util.Set;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class SelectAndRevealCommand extends ViewportCommand {
  private Set<XShape> originalSelection = CollectionLiterals.<XShape>newHashSet();
  
  private Function1<? super XShape, ? extends Boolean> selectionPredicate;
  
  public SelectAndRevealCommand(final XRoot root, final Function1<? super XShape, ? extends Boolean> selectionPredicate) {
    this.selectionPredicate = selectionPredicate;
  }
  
  @Override
  public ViewportTransition createViewportTransiton(final CommandContext context) {
    final XRoot root = context.getRoot();
    Iterable<XShape> _currentSelection = root.getCurrentSelection();
    Iterables.<XShape>addAll(this.originalSelection, _currentSelection);
    ObservableList<XNode> _nodes = root.getDiagram().getNodes();
    ObservableList<XConnection> _connections = root.getDiagram().getConnections();
    final Function1<XDomainObjectShape, XDomainObjectShape> _function = (XDomainObjectShape it) -> {
      Boolean _apply = this.selectionPredicate.apply(it);
      if ((_apply).booleanValue()) {
        it.setSelected(true);
        return it;
      } else {
        it.setSelected(false);
        return null;
      }
    };
    Iterable<XDomainObjectShape> selection = IterableExtensions.<XDomainObjectShape>filterNull(IterableExtensions.<XDomainObjectShape, XDomainObjectShape>map(Iterables.<XDomainObjectShape>concat(_nodes, _connections), _function));
    Iterable<XDomainObjectShape> _xifexpression = null;
    boolean _isEmpty = IterableExtensions.isEmpty(selection);
    if (_isEmpty) {
      ObservableList<XNode> _nodes_1 = root.getDiagram().getNodes();
      ObservableList<XConnection> _connections_1 = root.getDiagram().getConnections();
      _xifexpression = Iterables.<XDomainObjectShape>concat(_nodes_1, _connections_1);
    } else {
      _xifexpression = selection;
    }
    selection = _xifexpression;
    final Function1<XDomainObjectShape, Bounds> _function_1 = (XDomainObjectShape it) -> {
      return CoreExtensions.localToRootDiagram(it, it.getSnapBounds());
    };
    final Function2<Bounds, Bounds, Bounds> _function_2 = (Bounds a, Bounds b) -> {
      return BoundsExtensions.operator_plus(a, b);
    };
    final Bounds selectionBounds = IterableExtensions.<Bounds>reduce(IterableExtensions.<Bounds>filterNull(IterableExtensions.<XDomainObjectShape, Bounds>map(selection, _function_1)), _function_2);
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
      ViewportTransition _viewportTransition = new ViewportTransition(root, _center, targetScale);
      final Procedure1<ViewportTransition> _function_3 = (ViewportTransition it) -> {
        it.setMaxDuration(DurationExtensions.millis(400));
      };
      return ObjectExtensions.<ViewportTransition>operator_doubleArrow(_viewportTransition, _function_3);
    } else {
      return null;
    }
  }
  
  @Override
  public Animation getUndoAnimation(final CommandContext context) {
    Animation _xblockexpression = null;
    {
      ObservableList<XNode> _nodes = context.getRoot().getDiagram().getNodes();
      ObservableList<XConnection> _connections = context.getRoot().getDiagram().getConnections();
      final Consumer<XDomainObjectShape> _function = (XDomainObjectShape it) -> {
        it.setSelected(this.originalSelection.contains(it));
      };
      Iterables.<XDomainObjectShape>concat(_nodes, _connections).forEach(_function);
      _xblockexpression = super.getUndoAnimation(context);
    }
    return _xblockexpression;
  }
  
  @Override
  public Animation getRedoAnimation(final CommandContext context) {
    Animation _xblockexpression = null;
    {
      ObservableList<XNode> _nodes = context.getRoot().getDiagram().getNodes();
      ObservableList<XConnection> _connections = context.getRoot().getDiagram().getConnections();
      final Consumer<XDomainObjectShape> _function = (XDomainObjectShape it) -> {
        it.setSelected((this.selectionPredicate.apply(it)).booleanValue());
      };
      Iterables.<XDomainObjectShape>concat(_nodes, _connections).forEach(_function);
      _xblockexpression = super.getRedoAnimation(context);
    }
    return _xblockexpression;
  }
}
