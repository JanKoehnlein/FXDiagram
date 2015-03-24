package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
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
import javafx.scene.Scene;
import javafx.util.Duration;
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
    XDiagram _diagram = root.getDiagram();
    ObservableList<XNode> _nodes = _diagram.getNodes();
    XDiagram _diagram_1 = root.getDiagram();
    ObservableList<XConnection> _connections = _diagram_1.getConnections();
    Iterable<XShape> _plus = Iterables.<XShape>concat(_nodes, _connections);
    final Function1<XShape, XShape> _function = (XShape it) -> {
      Boolean _apply = this.selectionPredicate.apply(((XShape)it));
      if ((_apply).booleanValue()) {
        ((XShape)it).setSelected(true);
        return ((XShape)it);
      } else {
        ((XShape)it).setSelected(false);
        return null;
      }
    };
    Iterable<XShape> _map = IterableExtensions.<XShape, XShape>map(_plus, _function);
    Iterable<XShape> selection = IterableExtensions.<XShape>filterNull(_map);
    Iterable<XShape> _xifexpression = null;
    boolean _isEmpty = IterableExtensions.isEmpty(selection);
    if (_isEmpty) {
      XDiagram _diagram_2 = root.getDiagram();
      ObservableList<XNode> _nodes_1 = _diagram_2.getNodes();
      XDiagram _diagram_3 = root.getDiagram();
      ObservableList<XConnection> _connections_1 = _diagram_3.getConnections();
      _xifexpression = Iterables.<XShape>concat(_nodes_1, _connections_1);
    } else {
      _xifexpression = selection;
    }
    selection = _xifexpression;
    final Function1<XShape, Bounds> _function_1 = (XShape it) -> {
      Bounds _snapBounds = ((XShape)it).getSnapBounds();
      return CoreExtensions.localToRootDiagram(((XShape)it), _snapBounds);
    };
    Iterable<Bounds> _map_1 = IterableExtensions.<XShape, Bounds>map(selection, _function_1);
    final Function2<Bounds, Bounds, Bounds> _function_2 = (Bounds a, Bounds b) -> {
      return BoundsExtensions.operator_plus(a, b);
    };
    final Bounds selectionBounds = IterableExtensions.<Bounds>reduce(_map_1, _function_2);
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
      Scene _scene = root.getScene();
      double _width_1 = _scene.getWidth();
      double _width_2 = selectionBounds.getWidth();
      double _divide = (_width_1 / _width_2);
      Scene _scene_1 = root.getScene();
      double _height_1 = _scene_1.getHeight();
      double _height_2 = selectionBounds.getHeight();
      double _divide_1 = (_height_1 / _height_2);
      double _min = Math.min(_divide, _divide_1);
      final double targetScale = Math.min(1, _min);
      Point2D _center = BoundsExtensions.center(selectionBounds);
      ViewportTransition _viewportTransition = new ViewportTransition(root, _center, targetScale);
      final Procedure1<ViewportTransition> _function_3 = (ViewportTransition it) -> {
        Duration _millis = DurationExtensions.millis(400);
        it.setMaxDuration(_millis);
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
      XRoot _root = context.getRoot();
      XDiagram _diagram = _root.getDiagram();
      ObservableList<XNode> _nodes = _diagram.getNodes();
      XRoot _root_1 = context.getRoot();
      XDiagram _diagram_1 = _root_1.getDiagram();
      ObservableList<XConnection> _connections = _diagram_1.getConnections();
      Iterable<XShape> _plus = Iterables.<XShape>concat(_nodes, _connections);
      final Consumer<XShape> _function = (XShape it) -> {
        boolean _contains = this.originalSelection.contains(((XShape)it));
        ((XShape)it).setSelected(_contains);
      };
      _plus.forEach(_function);
      _xblockexpression = super.getUndoAnimation(context);
    }
    return _xblockexpression;
  }
  
  @Override
  public Animation getRedoAnimation(final CommandContext context) {
    Animation _xblockexpression = null;
    {
      XRoot _root = context.getRoot();
      XDiagram _diagram = _root.getDiagram();
      ObservableList<XNode> _nodes = _diagram.getNodes();
      XRoot _root_1 = context.getRoot();
      XDiagram _diagram_1 = _root_1.getDiagram();
      ObservableList<XConnection> _connections = _diagram_1.getConnections();
      Iterable<XShape> _plus = Iterables.<XShape>concat(_nodes, _connections);
      final Consumer<XShape> _function = (XShape it) -> {
        Boolean _apply = this.selectionPredicate.apply(((XShape)it));
        ((XShape)it).setSelected((_apply).booleanValue());
      };
      _plus.forEach(_function);
      _xblockexpression = super.getRedoAnimation(context);
    }
    return _xblockexpression;
  }
}
