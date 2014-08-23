package de.fxdiagram.xtext.glue.mapping;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import java.util.List;
import javafx.collections.ObservableList;
import org.eclipse.xtend.lib.Property;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class InterpreterContext {
  protected XDiagram diagram;
  
  @Property
  private boolean _isNewDiagram;
  
  private List<XNode> addedNodes = CollectionLiterals.<XNode>newArrayList();
  
  private List<XConnection> addedConnections = CollectionLiterals.<XConnection>newArrayList();
  
  public XDiagram setDiagram(final XDiagram diagram) {
    return this.diagram = diagram;
  }
  
  public boolean addNode(final XNode node) {
    boolean _xifexpression = false;
    XRoot _root = CoreExtensions.getRoot(this.diagram);
    boolean _notEquals = (!Objects.equal(_root, null));
    if (_notEquals) {
      _xifexpression = this.addedNodes.add(node);
    } else {
      ObservableList<XNode> _nodes = this.diagram.getNodes();
      _xifexpression = _nodes.add(node);
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
      ObservableList<XConnection> _connections = this.diagram.getConnections();
      _xifexpression = _connections.add(connection);
    }
    return _xifexpression;
  }
  
  public <T extends Object> XConnection getConnection(final DomainObjectDescriptor descriptor) {
    ObservableList<XConnection> _connections = this.diagram.getConnections();
    Iterable<XConnection> _plus = Iterables.<XConnection>concat(this.addedConnections, _connections);
    final Function1<XConnection, Boolean> _function = new Function1<XConnection, Boolean>() {
      public Boolean apply(final XConnection it) {
        DomainObjectDescriptor _domainObject = it.getDomainObject();
        return Boolean.valueOf(Objects.equal(_domainObject, descriptor));
      }
    };
    return IterableExtensions.<XConnection>findFirst(_plus, _function);
  }
  
  public <T extends Object> XNode getNode(final DomainObjectDescriptor descriptor) {
    ObservableList<XNode> _nodes = this.diagram.getNodes();
    Iterable<XNode> _plus = Iterables.<XNode>concat(this.addedNodes, _nodes);
    final Function1<XNode, Boolean> _function = new Function1<XNode, Boolean>() {
      public Boolean apply(final XNode it) {
        DomainObjectDescriptor _domainObject = it.getDomainObject();
        return Boolean.valueOf(Objects.equal(_domainObject, descriptor));
      }
    };
    return IterableExtensions.<XNode>findFirst(_plus, _function);
  }
  
  public boolean needsLayout() {
    boolean _or = false;
    boolean _isIsNewDiagram = this.isIsNewDiagram();
    if (_isIsNewDiagram) {
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
  
  public AddRemoveCommand getCommand() {
    Iterable<XShape> _plus = Iterables.<XShape>concat(this.addedNodes, this.addedConnections);
    return AddRemoveCommand.newAddCommand(this.diagram, ((XShape[])Conversions.unwrapArray(_plus, XShape.class)));
  }
  
  @Pure
  public boolean isIsNewDiagram() {
    return this._isNewDiagram;
  }
  
  public void setIsNewDiagram(final boolean isNewDiagram) {
    this._isNewDiagram = isNewDiagram;
  }
}
