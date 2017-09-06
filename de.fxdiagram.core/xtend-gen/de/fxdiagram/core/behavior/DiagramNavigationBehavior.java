package de.fxdiagram.core.behavior;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.NavigationBehavior;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.ViewportCommand;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.viewport.ViewportTransition;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DiagramNavigationBehavior extends AbstractHostBehavior<XDiagram> implements NavigationBehavior {
  public DiagramNavigationBehavior(final XDiagram host) {
    super(host);
  }
  
  @Override
  protected void doActivate() {
  }
  
  @Override
  public Class<? extends Behavior> getBehaviorKey() {
    return NavigationBehavior.class;
  }
  
  @Override
  public boolean next() {
    final ObservableList<XNode> nodes = this.getHost().getNodes();
    final Function1<XNode, Boolean> _function = (XNode it) -> {
      return Boolean.valueOf(it.getSelected());
    };
    final XNode current = IterableExtensions.<XNode>findFirst(nodes, _function);
    XNode _xifexpression = null;
    boolean _notEquals = (!Objects.equal(current, null));
    if (_notEquals) {
      int _indexOf = nodes.indexOf(current);
      int _plus = (_indexOf + 1);
      int _size = nodes.size();
      int _modulo = (_plus % _size);
      _xifexpression = nodes.get(_modulo);
    } else {
      _xifexpression = IterableExtensions.<XNode>head(nodes);
    }
    final XNode next = _xifexpression;
    if (next!=null) {
      this.reveal(next);
    }
    return (!Objects.equal(next, null));
  }
  
  @Override
  public boolean previous() {
    final ObservableList<XNode> nodes = this.getHost().getNodes();
    final Function1<XNode, Boolean> _function = (XNode it) -> {
      return Boolean.valueOf(it.getSelected());
    };
    final XNode current = IterableExtensions.<XNode>findLast(nodes, _function);
    XNode _xifexpression = null;
    boolean _notEquals = (!Objects.equal(current, null));
    if (_notEquals) {
      int _indexOf = nodes.indexOf(current);
      int _size = nodes.size();
      int _plus = (_indexOf + _size);
      int _minus = (_plus - 1);
      int _size_1 = nodes.size();
      int _modulo = (_minus % _size_1);
      _xifexpression = nodes.get(_modulo);
    } else {
      _xifexpression = IterableExtensions.<XNode>last(nodes);
    }
    final XNode previous = _xifexpression;
    if (previous!=null) {
      this.reveal(previous);
    }
    return (!Objects.equal(previous, null));
  }
  
  protected void reveal(final XShape node) {
    final ViewportCommand _function = new ViewportCommand() {
      @Override
      public ViewportTransition createViewportTransiton(final CommandContext it) {
        XRoot _root = CoreExtensions.getRoot(DiagramNavigationBehavior.this.getHost());
        Point2D _localToDiagram = CoreExtensions.localToDiagram(node, BoundsExtensions.center(node.getBoundsInLocal()));
        ViewportTransition _viewportTransition = new ViewportTransition(_root, _localToDiagram, 1);
        final Procedure1<ViewportTransition> _function = (ViewportTransition it_1) -> {
          final EventHandler<ActionEvent> _function_1 = (ActionEvent it_2) -> {
            final Consumer<XShape> _function_2 = (XShape it_3) -> {
              it_3.setSelected(false);
            };
            CoreExtensions.getRoot(DiagramNavigationBehavior.this.getHost()).getCurrentSelection().forEach(_function_2);
            node.setSelected(true);
          };
          it_1.setOnFinished(_function_1);
        };
        return ObjectExtensions.<ViewportTransition>operator_doubleArrow(_viewportTransition, _function);
      }
    };
    final ViewportCommand command = _function;
    CoreExtensions.getRoot(node).getCommandStack().execute(command);
  }
}
