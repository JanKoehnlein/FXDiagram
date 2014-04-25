package de.fxdiagram.xtext.glue.mapping;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class InterpreterContext {
  private XDiagram diagram;
  
  private int numAddedShapes;
  
  public InterpreterContext(final XDiagram diagram) {
    this.diagram = diagram;
  }
  
  public int addNode(final XNode node) {
    int _xblockexpression = (int) 0;
    {
      ObservableList<XNode> _nodes = this.diagram.getNodes();
      _nodes.add(node);
      node.layout();
      _xblockexpression = this.numAddedShapes = (this.numAddedShapes + 1);
    }
    return _xblockexpression;
  }
  
  public int addConnection(final XConnection connection) {
    int _xblockexpression = (int) 0;
    {
      ObservableList<XConnection> _connections = this.diagram.getConnections();
      _connections.add(connection);
      _xblockexpression = this.numAddedShapes = (this.numAddedShapes + 1);
    }
    return _xblockexpression;
  }
  
  public <T extends Object> XConnection getConnection(final DomainObjectDescriptor descriptor) {
    ObservableList<XConnection> _connections = this.diagram.getConnections();
    final Function1<XConnection,Boolean> _function = new Function1<XConnection,Boolean>() {
      public Boolean apply(final XConnection it) {
        DomainObjectDescriptor _domainObject = it.getDomainObject();
        return Boolean.valueOf(Objects.equal(_domainObject, descriptor));
      }
    };
    return IterableExtensions.<XConnection>findFirst(_connections, _function);
  }
  
  public <T extends Object> XNode getNode(final DomainObjectDescriptor descriptor) {
    ObservableList<XNode> _nodes = this.diagram.getNodes();
    final Function1<XNode,Boolean> _function = new Function1<XNode,Boolean>() {
      public Boolean apply(final XNode it) {
        DomainObjectDescriptor _domainObject = it.getDomainObject();
        return Boolean.valueOf(Objects.equal(_domainObject, descriptor));
      }
    };
    return IterableExtensions.<XNode>findFirst(_nodes, _function);
  }
  
  public boolean needsLayout() {
    return (this.numAddedShapes > 1);
  }
}
