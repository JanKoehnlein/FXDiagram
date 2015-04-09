package de.fxdiagram.mapping;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.ChangeDiagramCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import java.util.Set;
import javafx.collections.ObservableList;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class InterpreterContext {
  private XDiagram diagram;
  
  private XDiagram oldDiagram;
  
  @Accessors
  private boolean isCreateNewDiagram;
  
  private Set<XNode> addedNodes = CollectionLiterals.<XNode>newHashSet();
  
  private Set<XConnection> addedConnections = CollectionLiterals.<XConnection>newHashSet();
  
  public InterpreterContext(final XDiagram diagram) {
    this.diagram = diagram;
  }
  
  public XDiagram getDiagram() {
    return this.diagram;
  }
  
  public XDiagram setNewDiagram(final XDiagram diagram) {
    XDiagram _xblockexpression = null;
    {
      this.oldDiagram = this.diagram;
      _xblockexpression = this.diagram = diagram;
    }
    return _xblockexpression;
  }
  
  public boolean addNode(final XNode node) {
    boolean _xifexpression = false;
    XRoot _root = CoreExtensions.getRoot(this.diagram);
    boolean _notEquals = (!Objects.equal(_root, null));
    if (_notEquals) {
      _xifexpression = this.addedNodes.add(node);
    } else {
      boolean _xifexpression_1 = false;
      ObservableList<XNode> _nodes = this.diagram.getNodes();
      boolean _contains = _nodes.contains(node);
      boolean _not = (!_contains);
      if (_not) {
        ObservableList<XNode> _nodes_1 = this.diagram.getNodes();
        _xifexpression_1 = _nodes_1.add(node);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public boolean addConnection(final XConnection connection) {
    boolean _xifexpression = false;
    XRoot _root = CoreExtensions.getRoot(this.diagram);
    boolean _notEquals = (!Objects.equal(_root, null));
    if (_notEquals) {
      _xifexpression = this.addedConnections.add(connection);
    } else {
      boolean _xifexpression_1 = false;
      ObservableList<XConnection> _connections = this.diagram.getConnections();
      boolean _contains = _connections.contains(connection);
      boolean _not = (!_contains);
      if (_not) {
        ObservableList<XConnection> _connections_1 = this.diagram.getConnections();
        _xifexpression_1 = _connections_1.add(connection);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public <T extends Object> XConnection getConnection(final DomainObjectDescriptor descriptor) {
    ObservableList<XConnection> _connections = this.diagram.getConnections();
    Iterable<XConnection> _plus = Iterables.<XConnection>concat(this.addedConnections, _connections);
    final Function1<XConnection, Boolean> _function = (XConnection it) -> {
      DomainObjectDescriptor _domainObject = it.getDomainObject();
      return Boolean.valueOf(Objects.equal(_domainObject, descriptor));
    };
    return IterableExtensions.<XConnection>findFirst(_plus, _function);
  }
  
  public <T extends Object> XNode getNode(final DomainObjectDescriptor descriptor) {
    ObservableList<XNode> _nodes = this.diagram.getNodes();
    Iterable<XNode> _plus = Iterables.<XNode>concat(this.addedNodes, _nodes);
    final Function1<XNode, Boolean> _function = (XNode it) -> {
      DomainObjectDescriptor _domainObject = it.getDomainObject();
      return Boolean.valueOf(Objects.equal(_domainObject, descriptor));
    };
    return IterableExtensions.<XNode>findFirst(_plus, _function);
  }
  
  public boolean needsLayout() {
    boolean _or = false;
    if (this.isCreateNewDiagram) {
      _or = true;
    } else {
      int _size = this.addedNodes.size();
      int _size_1 = this.addedConnections.size();
      int _plus = (_size + _size_1);
      boolean _greaterThan = (_plus > 1);
      _or = _greaterThan;
    }
    return _or;
  }
  
  public void executeCommands(final CommandStack commandStack) {
    boolean _notEquals = (!Objects.equal(this.oldDiagram, null));
    if (_notEquals) {
      ChangeDiagramCommand _changeDiagramCommand = new ChangeDiagramCommand(this.diagram);
      commandStack.execute(_changeDiagramCommand);
    }
    Iterable<XShape> _plus = Iterables.<XShape>concat(this.addedNodes, this.addedConnections);
    AddRemoveCommand _newAddCommand = AddRemoveCommand.newAddCommand(this.diagram, ((XShape[])Conversions.unwrapArray(_plus, XShape.class)));
    commandStack.execute(_newAddCommand);
  }
  
  @Pure
  public boolean isCreateNewDiagram() {
    return this.isCreateNewDiagram;
  }
  
  public void setIsCreateNewDiagram(final boolean isCreateNewDiagram) {
    this.isCreateNewDiagram = isCreateNewDiagram;
  }
}
