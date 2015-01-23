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
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.ViewportCommand;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.viewport.ViewportTransition;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
    XDiagram _host = this.getHost();
    final ObservableList<XNode> nodes = _host.getNodes();
    final Function1<XNode, Boolean> _function = new Function1<XNode, Boolean>() {
      @Override
      public Boolean apply(final XNode it) {
        return Boolean.valueOf(it.getSelected());
      }
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
    XDiagram _host = this.getHost();
    final ObservableList<XNode> nodes = _host.getNodes();
    final Function1<XNode, Boolean> _function = new Function1<XNode, Boolean>() {
      @Override
      public Boolean apply(final XNode it) {
        return Boolean.valueOf(it.getSelected());
      }
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
        XDiagram _host = DiagramNavigationBehavior.this.getHost();
        XRoot _root = CoreExtensions.getRoot(_host);
        Bounds _boundsInLocal = node.getBoundsInLocal();
        Point2D _center = BoundsExtensions.center(_boundsInLocal);
        Point2D _localToDiagram = CoreExtensions.localToDiagram(node, _center);
        ViewportTransition _viewportTransition = new ViewportTransition(_root, _localToDiagram, 1);
        final Procedure1<ViewportTransition> _function = new Procedure1<ViewportTransition>() {
          @Override
          public void apply(final ViewportTransition it) {
            final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
              @Override
              public void handle(final ActionEvent it) {
                XDiagram _host = DiagramNavigationBehavior.this.getHost();
                XRoot _root = CoreExtensions.getRoot(_host);
                Iterable<XShape> _currentSelection = _root.getCurrentSelection();
                final Consumer<XShape> _function = new Consumer<XShape>() {
                  @Override
                  public void accept(final XShape it) {
                    it.setSelected(false);
                  }
                };
                _currentSelection.forEach(_function);
                node.setSelected(true);
              }
            };
            it.setOnFinished(_function);
          }
        };
        return ObjectExtensions.<ViewportTransition>operator_doubleArrow(_viewportTransition, _function);
      }
    };
    final ViewportCommand command = _function;
    XRoot _root = CoreExtensions.getRoot(node);
    CommandStack _commandStack = _root.getCommandStack();
    _commandStack.execute(command);
  }
}
