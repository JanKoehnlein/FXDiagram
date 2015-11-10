package de.fxdiagram.mapping.reconcile;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
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
    XDiagram _host = this.getHost();
    DomainObjectDescriptor _domainObjectDescriptor = _host.getDomainObjectDescriptor();
    if ((_domainObjectDescriptor instanceof IMappedElementDescriptor<?>)) {
      DirtyState _xblockexpression = null;
      {
        XDiagram _host_1 = this.getHost();
        DomainObjectDescriptor _domainObjectDescriptor_1 = _host_1.getDomainObjectDescriptor();
        final IMappedElementDescriptor<T> descriptor = ((IMappedElementDescriptor<T>) _domainObjectDescriptor_1);
        final Function1<T, DirtyState> _function = (T it) -> {
          XDiagram _host_2 = this.getHost();
          ObservableList<XNode> _nodes = _host_2.getNodes();
          final Function1<XNode, DomainObjectDescriptor> _function_1 = (XNode it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XNode> localNodes = IterableExtensions.<DomainObjectDescriptor, XNode>toMap(_nodes, _function_1);
          XDiagram _host_3 = this.getHost();
          ObservableList<XConnection> _connections = _host_3.getConnections();
          final Function1<XConnection, DomainObjectDescriptor> _function_2 = (XConnection it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XConnection> localConnections = IterableExtensions.<DomainObjectDescriptor, XConnection>toMap(_connections, _function_2);
          final HashMap<DomainObjectDescriptor, XNode> nodesToBeDeleted = new HashMap<DomainObjectDescriptor, XNode>(localNodes);
          final HashMap<DomainObjectDescriptor, XConnection> connectionsToBeDeleted = new HashMap<DomainObjectDescriptor, XConnection>(localConnections);
          XDiagram _host_4 = this.getHost();
          XDiagram _rootDiagram = CoreExtensions.getRootDiagram(_host_4);
          Iterable<XShape> _allShapes = _rootDiagram.getAllShapes();
          final Function1<XShape, Boolean> _function_3 = (XShape it_1) -> {
            XDiagram _diagram = CoreExtensions.getDiagram(it_1);
            XDiagram _host_5 = this.getHost();
            return Boolean.valueOf((!Objects.equal(_diagram, _host_5)));
          };
          final Iterable<XShape> allOtherShapes = IterableExtensions.<XShape>filter(_allShapes, _function_3);
          Iterable<XNode> _filter = Iterables.<XNode>filter(allOtherShapes, XNode.class);
          final Function1<XNode, DomainObjectDescriptor> _function_4 = (XNode it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XNode> otherNodes = IterableExtensions.<DomainObjectDescriptor, XNode>toMap(_filter, _function_4);
          Iterable<XConnection> _filter_1 = Iterables.<XConnection>filter(allOtherShapes, XConnection.class);
          final Function1<XConnection, DomainObjectDescriptor> _function_5 = (XConnection it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XConnection> otherConnectionsNodes = IterableExtensions.<DomainObjectDescriptor, XConnection>toMap(_filter_1, _function_5);
          XDiagram _host_5 = this.getHost();
          final InterpreterContext flatContext = new InterpreterContext(_host_5) {
            @Override
            public XConnection getConnection(final DomainObjectDescriptor descriptor) {
              XConnection _elvis = null;
              XConnection _elvis_1 = null;
              XConnection _remove = connectionsToBeDeleted.remove(descriptor);
              if (_remove != null) {
                _elvis_1 = _remove;
              } else {
                XConnection _get = localConnections.get(descriptor);
                _elvis_1 = _get;
              }
              if (_elvis_1 != null) {
                _elvis = _elvis_1;
              } else {
                XConnection _get_1 = otherConnectionsNodes.get(descriptor);
                _elvis = _get_1;
              }
              return _elvis;
            }
            
            @Override
            public XNode getNode(final DomainObjectDescriptor descriptor) {
              XNode _elvis = null;
              XNode _elvis_1 = null;
              XNode _remove = nodesToBeDeleted.remove(descriptor);
              if (_remove != null) {
                _elvis_1 = _remove;
              } else {
                XNode _get = localNodes.get(descriptor);
                _elvis_1 = _get;
              }
              if (_elvis_1 != null) {
                _elvis = _elvis_1;
              } else {
                XNode _get_1 = otherNodes.get(descriptor);
                _elvis = _get_1;
              }
              return _elvis;
            }
          };
          AbstractMapping<T> _mapping = descriptor.getMapping();
          this.interpreter.<T, Object>createDiagram(it, ((DiagramMapping<T>) _mapping), false, flatContext);
          boolean _or = false;
          boolean _or_1 = false;
          Iterable<XDomainObjectShape> _addedShapes = flatContext.getAddedShapes();
          boolean _isEmpty = IterableExtensions.isEmpty(_addedShapes);
          boolean _not = (!_isEmpty);
          if (_not) {
            _or_1 = true;
          } else {
            boolean _isEmpty_1 = nodesToBeDeleted.isEmpty();
            boolean _not_1 = (!_isEmpty_1);
            _or_1 = _not_1;
          }
          if (_or_1) {
            _or = true;
          } else {
            boolean _isEmpty_2 = connectionsToBeDeleted.isEmpty();
            boolean _not_2 = (!_isEmpty_2);
            _or = _not_2;
          }
          if (_or) {
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
    XDiagram _host = this.getHost();
    DomainObjectDescriptor _domainObjectDescriptor = _host.getDomainObjectDescriptor();
    if ((_domainObjectDescriptor instanceof IMappedElementDescriptor<?>)) {
      XDiagram _host_1 = this.getHost();
      DomainObjectDescriptor _domainObjectDescriptor_1 = _host_1.getDomainObjectDescriptor();
      final IMappedElementDescriptor<T> descriptor = ((IMappedElementDescriptor<T>) _domainObjectDescriptor_1);
      final Function1<T, Object> _function = (T it) -> {
        Object _xblockexpression = null;
        {
          XDiagram _host_2 = this.getHost();
          ObservableList<XNode> _nodes = _host_2.getNodes();
          final Function1<XNode, DomainObjectDescriptor> _function_1 = (XNode it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XNode> localNodes = IterableExtensions.<DomainObjectDescriptor, XNode>toMap(_nodes, _function_1);
          XDiagram _host_3 = this.getHost();
          ObservableList<XConnection> _connections = _host_3.getConnections();
          final Function1<XConnection, DomainObjectDescriptor> _function_2 = (XConnection it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XConnection> localConnections = IterableExtensions.<DomainObjectDescriptor, XConnection>toMap(_connections, _function_2);
          final HashMap<DomainObjectDescriptor, XNode> nodesToBeDeleted = new HashMap<DomainObjectDescriptor, XNode>(localNodes);
          final HashMap<DomainObjectDescriptor, XConnection> connectionsToBeDeleted = new HashMap<DomainObjectDescriptor, XConnection>(localConnections);
          XDiagram _host_4 = this.getHost();
          XDiagram _rootDiagram = CoreExtensions.getRootDiagram(_host_4);
          Iterable<XShape> _allShapes = _rootDiagram.getAllShapes();
          final Function1<XShape, Boolean> _function_3 = (XShape it_1) -> {
            XDiagram _diagram = CoreExtensions.getDiagram(it_1);
            XDiagram _host_5 = this.getHost();
            return Boolean.valueOf((!Objects.equal(_diagram, _host_5)));
          };
          final Iterable<XShape> allOtherShapes = IterableExtensions.<XShape>filter(_allShapes, _function_3);
          Iterable<XNode> _filter = Iterables.<XNode>filter(allOtherShapes, XNode.class);
          final Function1<XNode, DomainObjectDescriptor> _function_4 = (XNode it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XNode> otherNodes = IterableExtensions.<DomainObjectDescriptor, XNode>toMap(_filter, _function_4);
          Iterable<XConnection> _filter_1 = Iterables.<XConnection>filter(allOtherShapes, XConnection.class);
          final Function1<XConnection, DomainObjectDescriptor> _function_5 = (XConnection it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XConnection> otherConnectionsNodes = IterableExtensions.<DomainObjectDescriptor, XConnection>toMap(_filter_1, _function_5);
          XDiagram _host_5 = this.getHost();
          final InterpreterContext flatContext = new InterpreterContext(_host_5) {
            @Override
            public XConnection getConnection(final DomainObjectDescriptor descriptor) {
              XConnection _elvis = null;
              XConnection _elvis_1 = null;
              XConnection _remove = connectionsToBeDeleted.remove(descriptor);
              if (_remove != null) {
                _elvis_1 = _remove;
              } else {
                XConnection _get = localConnections.get(descriptor);
                _elvis_1 = _get;
              }
              if (_elvis_1 != null) {
                _elvis = _elvis_1;
              } else {
                XConnection _get_1 = otherConnectionsNodes.get(descriptor);
                _elvis = _get_1;
              }
              return _elvis;
            }
            
            @Override
            public XNode getNode(final DomainObjectDescriptor descriptor) {
              XNode _elvis = null;
              XNode _elvis_1 = null;
              XNode _remove = nodesToBeDeleted.remove(descriptor);
              if (_remove != null) {
                _elvis_1 = _remove;
              } else {
                XNode _get = localNodes.get(descriptor);
                _elvis_1 = _get;
              }
              if (_elvis_1 != null) {
                _elvis = _elvis_1;
              } else {
                XNode _get_1 = otherNodes.get(descriptor);
                _elvis = _get_1;
              }
              return _elvis;
            }
          };
          AbstractMapping<T> _mapping = descriptor.getMapping();
          this.interpreter.<T, Object>createDiagram(it, ((DiagramMapping<T>) _mapping), false, flatContext);
          Iterable<XDomainObjectShape> _addedShapes = flatContext.getAddedShapes();
          for (final XDomainObjectShape addedShape : _addedShapes) {
            XDiagram _host_6 = this.getHost();
            acceptor.add(addedShape, _host_6);
          }
          Collection<XNode> _values = nodesToBeDeleted.values();
          Collection<XConnection> _values_1 = connectionsToBeDeleted.values();
          Iterable<XDomainObjectShape> _plus = Iterables.<XDomainObjectShape>concat(_values, _values_1);
          final Consumer<XDomainObjectShape> _function_6 = (XDomainObjectShape it_1) -> {
            XDiagram _host_7 = this.getHost();
            acceptor.delete(it_1, _host_7);
          };
          _plus.forEach(_function_6);
          _xblockexpression = null;
        }
        return _xblockexpression;
      };
      descriptor.<Object>withDomainObject(_function);
    }
  }
  
  @Override
  protected void dirtyFeedback(final boolean isDirty) {
    XDiagram _host = this.getHost();
    boolean _isRootDiagram = _host.getIsRootDiagram();
    if (_isRootDiagram) {
      if (isDirty) {
        XDiagram _host_1 = this.getHost();
        XRoot _root = CoreExtensions.getRoot(_host_1);
        HeadsUpDisplay _headsUpDisplay = _root.getHeadsUpDisplay();
        _headsUpDisplay.add(this.repairButton, Pos.TOP_RIGHT);
      } else {
        XDiagram _host_2 = this.getHost();
        XRoot _root_1 = CoreExtensions.getRoot(_host_2);
        HeadsUpDisplay _headsUpDisplay_1 = _root_1.getHeadsUpDisplay();
        ObservableList<Node> _children = _headsUpDisplay_1.getChildren();
        _children.remove(this.repairButton);
      }
    }
  }
  
  @Override
  protected void doActivate() {
    XDiagram _host = this.getHost();
    boolean _isRootDiagram = _host.getIsRootDiagram();
    if (_isRootDiagram) {
      Canvas _symbol = SymbolCanvas.getSymbol(SymbolType.TOOL, 32, Color.GRAY);
      final Procedure1<Canvas> _function = (Canvas it) -> {
        final EventHandler<MouseEvent> _function_1 = (MouseEvent it_1) -> {
          ReconcileAction _reconcileAction = new ReconcileAction();
          XDiagram _host_1 = this.getHost();
          XRoot _root = CoreExtensions.getRoot(_host_1);
          _reconcileAction.perform(_root);
          XDiagram _host_2 = this.getHost();
          XRoot _root_1 = CoreExtensions.getRoot(_host_2);
          HeadsUpDisplay _headsUpDisplay = _root_1.getHeadsUpDisplay();
          ObservableList<Node> _children = _headsUpDisplay.getChildren();
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
