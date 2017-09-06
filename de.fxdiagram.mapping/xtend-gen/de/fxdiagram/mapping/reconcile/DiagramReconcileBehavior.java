package de.fxdiagram.mapping.reconcile;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.AbstractReconcileBehavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.ReconcileBehavior;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.tools.actions.ReconcileAction;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.execution.InterpreterContext;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;
import de.fxdiagram.mapping.shapes.BaseDiagram;
import eu.hansolo.enzo.radialmenu.SymbolCanvas;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DiagramReconcileBehavior<T extends Object> extends AbstractReconcileBehavior<XDiagram> {
  private final XDiagramConfigInterpreter interpreter = new XDiagramConfigInterpreter();
  
  private Node repairButton;
  
  public DiagramReconcileBehavior(final BaseDiagram<T> host) {
    super(host);
  }
  
  @Override
  public DirtyState getDirtyState() {
    DirtyState _xifexpression = null;
    DomainObjectDescriptor _domainObjectDescriptor = this.getHost().getDomainObjectDescriptor();
    if ((_domainObjectDescriptor instanceof IMappedElementDescriptor<?>)) {
      DirtyState _xblockexpression = null;
      {
        DomainObjectDescriptor _domainObjectDescriptor_1 = this.getHost().getDomainObjectDescriptor();
        final IMappedElementDescriptor<T> descriptor = ((IMappedElementDescriptor<T>) _domainObjectDescriptor_1);
        final Function1<T, DirtyState> _function = (T it) -> {
          final Function1<XNode, DomainObjectDescriptor> _function_1 = (XNode it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XNode> localNodes = IterableExtensions.<DomainObjectDescriptor, XNode>toMap(this.getHost().getNodes(), _function_1);
          final Function1<XConnection, DomainObjectDescriptor> _function_2 = (XConnection it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XConnection> localConnections = IterableExtensions.<DomainObjectDescriptor, XConnection>toMap(this.getHost().getConnections(), _function_2);
          final HashMap<DomainObjectDescriptor, XNode> nodesToBeDeleted = new HashMap<DomainObjectDescriptor, XNode>(localNodes);
          final HashMap<DomainObjectDescriptor, XConnection> connectionsToBeDeleted = new HashMap<DomainObjectDescriptor, XConnection>(localConnections);
          final Function1<XShape, Boolean> _function_3 = (XShape it_1) -> {
            XDiagram _diagram = CoreExtensions.getDiagram(it_1);
            XDiagram _host = this.getHost();
            return Boolean.valueOf((!Objects.equal(_diagram, _host)));
          };
          final Iterable<XShape> allOtherShapes = IterableExtensions.<XShape>filter(CoreExtensions.getRootDiagram(this.getHost()).getAllShapes(), _function_3);
          final Function1<XNode, DomainObjectDescriptor> _function_4 = (XNode it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XNode> otherNodes = IterableExtensions.<DomainObjectDescriptor, XNode>toMap(Iterables.<XNode>filter(allOtherShapes, XNode.class), _function_4);
          final Function1<XConnection, DomainObjectDescriptor> _function_5 = (XConnection it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XConnection> otherConnections = IterableExtensions.<DomainObjectDescriptor, XConnection>toMap(Iterables.<XConnection>filter(allOtherShapes, XConnection.class), _function_5);
          XDiagram _host = this.getHost();
          final InterpreterContext flatContext = new InterpreterContext(_host) {
            @Override
            public XConnection getConnection(final DomainObjectDescriptor descriptor) {
              XConnection _elvis = null;
              XConnection _elvis_1 = null;
              XConnection _elvis_2 = null;
              XConnection _remove = connectionsToBeDeleted.remove(descriptor);
              if (_remove != null) {
                _elvis_2 = _remove;
              } else {
                XConnection _get = localConnections.get(descriptor);
                _elvis_2 = _get;
              }
              if (_elvis_2 != null) {
                _elvis_1 = _elvis_2;
              } else {
                XConnection _get_1 = otherConnections.get(descriptor);
                _elvis_1 = _get_1;
              }
              if (_elvis_1 != null) {
                _elvis = _elvis_1;
              } else {
                final Function1<XConnection, Boolean> _function = (XConnection it_1) -> {
                  DomainObjectDescriptor _domainObjectDescriptor = it_1.getDomainObjectDescriptor();
                  return Boolean.valueOf(Objects.equal(descriptor, _domainObjectDescriptor));
                };
                XConnection _findFirst = IterableExtensions.<XConnection>findFirst(this.getAddedConnections(), _function);
                _elvis = _findFirst;
              }
              return _elvis;
            }
            
            @Override
            public XNode getNode(final DomainObjectDescriptor descriptor) {
              XNode _elvis = null;
              XNode _elvis_1 = null;
              XNode _elvis_2 = null;
              XNode _remove = nodesToBeDeleted.remove(descriptor);
              if (_remove != null) {
                _elvis_2 = _remove;
              } else {
                XNode _get = localNodes.get(descriptor);
                _elvis_2 = _get;
              }
              if (_elvis_2 != null) {
                _elvis_1 = _elvis_2;
              } else {
                XNode _get_1 = otherNodes.get(descriptor);
                _elvis_1 = _get_1;
              }
              if (_elvis_1 != null) {
                _elvis = _elvis_1;
              } else {
                final Function1<XNode, Boolean> _function = (XNode it_1) -> {
                  DomainObjectDescriptor _domainObjectDescriptor = it_1.getDomainObjectDescriptor();
                  return Boolean.valueOf(Objects.equal(descriptor, _domainObjectDescriptor));
                };
                XNode _findFirst = IterableExtensions.<XNode>findFirst(this.getAddedNodes(), _function);
                _elvis = _findFirst;
              }
              return _elvis;
            }
          };
          AbstractMapping<T> _mapping = descriptor.getMapping();
          this.interpreter.<T, Object>createDiagram(it, ((DiagramMapping<T>) _mapping), false, flatContext);
          if ((((!IterableExtensions.isEmpty(flatContext.getAddedShapes())) || (!nodesToBeDeleted.isEmpty())) || (!connectionsToBeDeleted.isEmpty()))) {
            return DirtyState.DIRTY;
          } else {
            return DirtyState.CLEAN;
          }
        };
        _xblockexpression = descriptor.<DirtyState>withDomainObject(_function);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  @Override
  public void reconcile(final ReconcileBehavior.UpdateAcceptor acceptor) {
    DomainObjectDescriptor _domainObjectDescriptor = this.getHost().getDomainObjectDescriptor();
    if ((_domainObjectDescriptor instanceof IMappedElementDescriptor<?>)) {
      DomainObjectDescriptor _domainObjectDescriptor_1 = this.getHost().getDomainObjectDescriptor();
      final IMappedElementDescriptor<T> descriptor = ((IMappedElementDescriptor<T>) _domainObjectDescriptor_1);
      final Function1<T, Object> _function = (T it) -> {
        Object _xblockexpression = null;
        {
          final Function1<XNode, DomainObjectDescriptor> _function_1 = (XNode it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XNode> localNodes = IterableExtensions.<DomainObjectDescriptor, XNode>toMap(this.getHost().getNodes(), _function_1);
          final Function1<XConnection, DomainObjectDescriptor> _function_2 = (XConnection it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XConnection> localConnections = IterableExtensions.<DomainObjectDescriptor, XConnection>toMap(this.getHost().getConnections(), _function_2);
          final HashMap<DomainObjectDescriptor, XNode> nodesToBeDeleted = new HashMap<DomainObjectDescriptor, XNode>(localNodes);
          final HashMap<DomainObjectDescriptor, XConnection> connectionsToBeDeleted = new HashMap<DomainObjectDescriptor, XConnection>(localConnections);
          final Function1<XShape, Boolean> _function_3 = (XShape it_1) -> {
            XDiagram _diagram = CoreExtensions.getDiagram(it_1);
            XDiagram _host = this.getHost();
            return Boolean.valueOf((!Objects.equal(_diagram, _host)));
          };
          final Iterable<XShape> allOtherShapes = IterableExtensions.<XShape>filter(CoreExtensions.getRootDiagram(this.getHost()).getAllShapes(), _function_3);
          final Function1<XNode, DomainObjectDescriptor> _function_4 = (XNode it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XNode> otherNodes = IterableExtensions.<DomainObjectDescriptor, XNode>toMap(Iterables.<XNode>filter(allOtherShapes, XNode.class), _function_4);
          final Function1<XConnection, DomainObjectDescriptor> _function_5 = (XConnection it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XConnection> otherConnections = IterableExtensions.<DomainObjectDescriptor, XConnection>toMap(Iterables.<XConnection>filter(allOtherShapes, XConnection.class), _function_5);
          XDiagram _host = this.getHost();
          final InterpreterContext flatContext = new InterpreterContext(_host) {
            @Override
            public XConnection getConnection(final DomainObjectDescriptor descriptor) {
              XConnection _elvis = null;
              XConnection _elvis_1 = null;
              XConnection _elvis_2 = null;
              XConnection _remove = connectionsToBeDeleted.remove(descriptor);
              if (_remove != null) {
                _elvis_2 = _remove;
              } else {
                XConnection _get = localConnections.get(descriptor);
                _elvis_2 = _get;
              }
              if (_elvis_2 != null) {
                _elvis_1 = _elvis_2;
              } else {
                XConnection _get_1 = otherConnections.get(descriptor);
                _elvis_1 = _get_1;
              }
              if (_elvis_1 != null) {
                _elvis = _elvis_1;
              } else {
                final Function1<XConnection, Boolean> _function = (XConnection it_1) -> {
                  DomainObjectDescriptor _domainObjectDescriptor = it_1.getDomainObjectDescriptor();
                  return Boolean.valueOf(Objects.equal(descriptor, _domainObjectDescriptor));
                };
                XConnection _findFirst = IterableExtensions.<XConnection>findFirst(this.getAddedConnections(), _function);
                _elvis = _findFirst;
              }
              return _elvis;
            }
            
            @Override
            public XNode getNode(final DomainObjectDescriptor descriptor) {
              XNode _elvis = null;
              XNode _elvis_1 = null;
              XNode _elvis_2 = null;
              XNode _remove = nodesToBeDeleted.remove(descriptor);
              if (_remove != null) {
                _elvis_2 = _remove;
              } else {
                XNode _get = localNodes.get(descriptor);
                _elvis_2 = _get;
              }
              if (_elvis_2 != null) {
                _elvis_1 = _elvis_2;
              } else {
                XNode _get_1 = otherNodes.get(descriptor);
                _elvis_1 = _get_1;
              }
              if (_elvis_1 != null) {
                _elvis = _elvis_1;
              } else {
                final Function1<XNode, Boolean> _function = (XNode it_1) -> {
                  DomainObjectDescriptor _domainObjectDescriptor = it_1.getDomainObjectDescriptor();
                  return Boolean.valueOf(Objects.equal(descriptor, _domainObjectDescriptor));
                };
                XNode _findFirst = IterableExtensions.<XNode>findFirst(this.getAddedNodes(), _function);
                _elvis = _findFirst;
              }
              return _elvis;
            }
          };
          AbstractMapping<T> _mapping = descriptor.getMapping();
          this.interpreter.<T, Object>createDiagram(it, ((DiagramMapping<T>) _mapping), false, flatContext);
          Iterable<XDomainObjectShape> _addedShapes = flatContext.getAddedShapes();
          for (final XDomainObjectShape addedShape : _addedShapes) {
            acceptor.add(addedShape, this.getHost());
          }
          Collection<XNode> _values = nodesToBeDeleted.values();
          Collection<XConnection> _values_1 = connectionsToBeDeleted.values();
          final Consumer<XDomainObjectShape> _function_6 = (XDomainObjectShape it_1) -> {
            acceptor.delete(it_1, this.getHost());
          };
          Iterables.<XDomainObjectShape>concat(_values, _values_1).forEach(_function_6);
          _xblockexpression = null;
        }
        return _xblockexpression;
      };
      descriptor.<Object>withDomainObject(_function);
    }
  }
  
  @Override
  protected void dirtyFeedback(final boolean isDirty) {
    boolean _isRootDiagram = this.getHost().getIsRootDiagram();
    if (_isRootDiagram) {
      if (isDirty) {
        CoreExtensions.getRoot(this.getHost()).getHeadsUpDisplay().add(this.repairButton, Pos.TOP_RIGHT);
      } else {
        ObservableList<Node> _children = CoreExtensions.getRoot(this.getHost()).getHeadsUpDisplay().getChildren();
        _children.remove(this.repairButton);
      }
    }
  }
  
  @Override
  protected void doActivate() {
    boolean _isRootDiagram = this.getHost().getIsRootDiagram();
    if (_isRootDiagram) {
      Canvas _symbol = SymbolCanvas.getSymbol(SymbolType.TOOL, 32, Color.GRAY);
      final Procedure1<Canvas> _function = (Canvas it) -> {
        final EventHandler<MouseEvent> _function_1 = (MouseEvent it_1) -> {
          new ReconcileAction().perform(CoreExtensions.getRoot(this.getHost()));
          ObservableList<Node> _children = CoreExtensions.getRoot(this.getHost()).getHeadsUpDisplay().getChildren();
          _children.remove(this.repairButton);
        };
        it.setOnMouseClicked(_function_1);
        TooltipExtensions.setTooltip(it, "Repair diagram");
      };
      Canvas _doubleArrow = ObjectExtensions.<Canvas>operator_doubleArrow(_symbol, _function);
      this.repairButton = _doubleArrow;
    }
    super.doActivate();
  }
}
