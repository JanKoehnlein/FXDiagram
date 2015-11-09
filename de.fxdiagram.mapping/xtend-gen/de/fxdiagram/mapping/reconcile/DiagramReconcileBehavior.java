package de.fxdiagram.mapping.reconcile;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
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
import java.util.Map;
import java.util.Set;
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
          final BaseDiagram<T> dummyDiagram = new BaseDiagram<T>(descriptor);
          XDiagram _host_2 = this.getHost();
          XDiagram _parentDiagram = _host_2.getParentDiagram();
          final InterpreterContext context = this.createNestedInterpreterContext(dummyDiagram, _parentDiagram);
          AbstractMapping<T> _mapping = descriptor.getMapping();
          this.interpreter.<T, Object>createDiagram(it, ((DiagramMapping<T>) _mapping), false, context);
          XDiagram _host_3 = this.getHost();
          ObservableList<XNode> _nodes = _host_3.getNodes();
          XDiagram _host_4 = this.getHost();
          ObservableList<XConnection> _connections = _host_4.getConnections();
          Iterable<XDomainObjectShape> _plus = Iterables.<XDomainObjectShape>concat(_nodes, _connections);
          final Function1<XDomainObjectShape, DomainObjectDescriptor> _function_1 = (XDomainObjectShape it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          Iterable<DomainObjectDescriptor> _map = IterableExtensions.<XDomainObjectShape, DomainObjectDescriptor>map(_plus, _function_1);
          final Set<DomainObjectDescriptor> descriptors = IterableExtensions.<DomainObjectDescriptor>toSet(_map);
          ObservableList<XNode> _nodes_1 = dummyDiagram.getNodes();
          ObservableList<XConnection> _connections_1 = dummyDiagram.getConnections();
          Iterable<XDomainObjectShape> _plus_1 = Iterables.<XDomainObjectShape>concat(_nodes_1, _connections_1);
          for (final XDomainObjectShape recreatedShape : _plus_1) {
            DomainObjectDescriptor _domainObjectDescriptor_2 = recreatedShape.getDomainObjectDescriptor();
            boolean _remove = descriptors.remove(_domainObjectDescriptor_2);
            boolean _not = (!_remove);
            if (_not) {
              return DirtyState.DIRTY;
            }
          }
          boolean _isEmpty = descriptors.isEmpty();
          if (_isEmpty) {
            return DirtyState.CLEAN;
          } else {
            return DirtyState.DIRTY;
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
          XDiagram _host_3 = this.getHost();
          XDiagram _parentDiagram = _host_3.getParentDiagram();
          final InterpreterContext currentContext = this.createNestedInterpreterContext(_host_2, _parentDiagram);
          AbstractMapping<T> _mapping = descriptor.getMapping();
          this.interpreter.<T, Object>createDiagram(it, ((DiagramMapping<T>) _mapping), false, currentContext);
          Iterable<XDomainObjectShape> _addedShapes = currentContext.getAddedShapes();
          for (final XDomainObjectShape addedShape : _addedShapes) {
            XDiagram _host_4 = this.getHost();
            acceptor.add(addedShape, _host_4);
          }
          final BaseDiagram<T> dummyDiagram = new BaseDiagram<T>(descriptor);
          XDiagram _host_5 = this.getHost();
          XDiagram _parentDiagram_1 = _host_5.getParentDiagram();
          final InterpreterContext context = this.createNestedInterpreterContext(dummyDiagram, _parentDiagram_1);
          AbstractMapping<T> _mapping_1 = descriptor.getMapping();
          this.interpreter.<T, Object>createDiagram(it, ((DiagramMapping<T>) _mapping_1), false, context);
          XDiagram _host_6 = this.getHost();
          ObservableList<XNode> _nodes = _host_6.getNodes();
          XDiagram _host_7 = this.getHost();
          ObservableList<XConnection> _connections = _host_7.getConnections();
          Iterable<XDomainObjectShape> _plus = Iterables.<XDomainObjectShape>concat(_nodes, _connections);
          final Function1<XDomainObjectShape, DomainObjectDescriptor> _function_1 = (XDomainObjectShape it_1) -> {
            return it_1.getDomainObjectDescriptor();
          };
          final Map<DomainObjectDescriptor, XDomainObjectShape> descriptors = IterableExtensions.<DomainObjectDescriptor, XDomainObjectShape>toMap(_plus, _function_1);
          ObservableList<XNode> _nodes_1 = dummyDiagram.getNodes();
          ObservableList<XConnection> _connections_1 = dummyDiagram.getConnections();
          Iterable<XDomainObjectShape> _plus_1 = Iterables.<XDomainObjectShape>concat(_nodes_1, _connections_1);
          final Consumer<XDomainObjectShape> _function_2 = (XDomainObjectShape it_1) -> {
            DomainObjectDescriptor _domainObjectDescriptor_2 = it_1.getDomainObjectDescriptor();
            descriptors.remove(_domainObjectDescriptor_2);
          };
          _plus_1.forEach(_function_2);
          Collection<XDomainObjectShape> _values = descriptors.values();
          final Consumer<XDomainObjectShape> _function_3 = (XDomainObjectShape it_1) -> {
            XDiagram _host_8 = this.getHost();
            acceptor.delete(it_1, _host_8);
          };
          _values.forEach(_function_3);
          _xblockexpression = null;
        }
        return _xblockexpression;
      };
      descriptor.<Object>withDomainObject(_function);
    }
  }
  
  protected InterpreterContext createNestedInterpreterContext(final XDiagram hostOrDummy, final XDiagram parent) {
    InterpreterContext _xifexpression = null;
    boolean _equals = Objects.equal(parent, null);
    if (_equals) {
      return new InterpreterContext(hostOrDummy);
    } else {
      XDiagram _parentDiagram = parent.getParentDiagram();
      InterpreterContext _createNestedInterpreterContext = this.createNestedInterpreterContext(parent, _parentDiagram);
      _xifexpression = new InterpreterContext(hostOrDummy, _createNestedInterpreterContext);
    }
    return _xifexpression;
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
