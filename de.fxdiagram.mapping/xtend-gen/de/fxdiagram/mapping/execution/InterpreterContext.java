package de.fxdiagram.mapping.execution;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.ChangeDiagramCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class InterpreterContext {
  @Accessors(AccessorType.PUBLIC_GETTER)
  private XDiagram diagram;
  
  @Accessors
  private boolean isReplaceRootDiagram;
  
  @Accessors
  private boolean isCreateNodes = true;
  
  @Accessors
  private boolean isCreateConnections = true;
  
  @Accessors
  private boolean isCreateDuplicateNodes = false;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private List<InterpreterContext> subContexts = CollectionLiterals.<InterpreterContext>newArrayList();
  
  private InterpreterContext superContext;
  
  @Accessors(AccessorType.PROTECTED_GETTER)
  private Set<XNode> addedNodes = CollectionLiterals.<XNode>newHashSet();
  
  @Accessors(AccessorType.PROTECTED_GETTER)
  private Set<XConnection> addedConnections = CollectionLiterals.<XConnection>newHashSet();
  
  public InterpreterContext(final XDiagram diagram) {
    this.diagram = diagram;
  }
  
  public InterpreterContext(final XDiagram diagram, final InterpreterContext superContext) {
    this(diagram);
    this.superContext = superContext;
    superContext.subContexts.add(this);
  }
  
  public boolean addNode(final XNode node) {
    boolean _xifexpression = false;
    if ((Objects.equal(this.diagram, null) || (!Objects.equal(CoreExtensions.getRoot(this.diagram), null)))) {
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
    if ((Objects.equal(this.diagram, null) || (!Objects.equal(CoreExtensions.getRoot(this.diagram), null)))) {
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
  
  public XConnection getConnection(final DomainObjectDescriptor descriptor) {
    XConnection _xifexpression = null;
    boolean _equals = Objects.equal(this.superContext, null);
    if (_equals) {
      _xifexpression = this.doGetConnection(descriptor);
    } else {
      _xifexpression = this.superContext.getConnection(descriptor);
    }
    return _xifexpression;
  }
  
  protected XConnection doGetConnection(final DomainObjectDescriptor descriptor) {
    XConnection _elvis = null;
    ObservableList<XConnection> _connections = this.diagram.getConnections();
    Iterable<XConnection> _plus = Iterables.<XConnection>concat(this.addedConnections, _connections);
    final Function1<XConnection, Boolean> _function = (XConnection it) -> {
      DomainObjectDescriptor _domainObjectDescriptor = it.getDomainObjectDescriptor();
      return Boolean.valueOf(Objects.equal(_domainObjectDescriptor, descriptor));
    };
    XConnection _findFirst = IterableExtensions.<XConnection>findFirst(_plus, _function);
    if (_findFirst != null) {
      _elvis = _findFirst;
    } else {
      final Function1<InterpreterContext, XConnection> _function_1 = (InterpreterContext it) -> {
        return it.doGetConnection(descriptor);
      };
      List<XConnection> _map = ListExtensions.<InterpreterContext, XConnection>map(this.subContexts, _function_1);
      Iterable<XConnection> _filterNull = IterableExtensions.<XConnection>filterNull(_map);
      XConnection _head = IterableExtensions.<XConnection>head(_filterNull);
      _elvis = _head;
    }
    return _elvis;
  }
  
  public XNode getNode(final DomainObjectDescriptor descriptor) {
    XNode _xifexpression = null;
    boolean _equals = Objects.equal(this.superContext, null);
    if (_equals) {
      _xifexpression = this.doGetNode(descriptor);
    } else {
      _xifexpression = this.superContext.getNode(descriptor);
    }
    return _xifexpression;
  }
  
  protected XNode doGetNode(final DomainObjectDescriptor descriptor) {
    XNode _elvis = null;
    ObservableList<XNode> _nodes = this.diagram.getNodes();
    Iterable<XNode> _plus = Iterables.<XNode>concat(this.addedNodes, _nodes);
    final Function1<XNode, Boolean> _function = (XNode it) -> {
      DomainObjectDescriptor _domainObjectDescriptor = it.getDomainObjectDescriptor();
      return Boolean.valueOf(Objects.equal(_domainObjectDescriptor, descriptor));
    };
    XNode _findFirst = IterableExtensions.<XNode>findFirst(_plus, _function);
    if (_findFirst != null) {
      _elvis = _findFirst;
    } else {
      final Function1<InterpreterContext, XNode> _function_1 = (InterpreterContext it) -> {
        return it.doGetNode(descriptor);
      };
      List<XNode> _map = ListExtensions.<InterpreterContext, XNode>map(this.subContexts, _function_1);
      Iterable<XNode> _filterNull = IterableExtensions.<XNode>filterNull(_map);
      XNode _head = IterableExtensions.<XNode>head(_filterNull);
      _elvis = _head;
    }
    return _elvis;
  }
  
  public boolean needsLayoutCommand() {
    return ((!this.isReplaceRootDiagram()) && ((this.addedNodes.size() + this.addedConnections.size()) > 1));
  }
  
  /**
   * Doesn't involve either command stack nor animations
   */
  public void directlyApplyChanges() {
    if (((this.isReplaceRootDiagram() && (!this.subContexts.isEmpty())) && Objects.equal(this.superContext, null))) {
      XRoot _root = CoreExtensions.getRoot(this.diagram);
      InterpreterContext _head = IterableExtensions.<InterpreterContext>head(this.subContexts);
      _root.setDiagram(_head.diagram);
    }
    ObservableList<XNode> _nodes = this.diagram.getNodes();
    Iterables.<XNode>addAll(_nodes, this.addedNodes);
    ObservableList<XConnection> _connections = this.diagram.getConnections();
    Iterables.<XConnection>addAll(_connections, this.addedConnections);
    final Consumer<InterpreterContext> _function = (InterpreterContext it) -> {
      it.directlyApplyChanges();
    };
    this.subContexts.forEach(_function);
  }
  
  public Iterable<XDomainObjectShape> getAddedShapes() {
    Iterable<XDomainObjectShape> _plus = Iterables.<XDomainObjectShape>concat(this.addedNodes, this.addedConnections);
    final Function1<InterpreterContext, Iterable<XDomainObjectShape>> _function = (InterpreterContext it) -> {
      return it.getAddedShapes();
    };
    List<Iterable<XDomainObjectShape>> _map = ListExtensions.<InterpreterContext, Iterable<XDomainObjectShape>>map(this.subContexts, _function);
    Iterable<XDomainObjectShape> _flatten = Iterables.<XDomainObjectShape>concat(_map);
    return Iterables.<XDomainObjectShape>concat(_plus, _flatten);
  }
  
  public void executeCommands(final CommandStack commandStack) {
    if (((this.isReplaceRootDiagram() && (!this.subContexts.isEmpty())) && Objects.equal(this.superContext, null))) {
      InterpreterContext _head = IterableExtensions.<InterpreterContext>head(this.subContexts);
      ChangeDiagramCommand _changeDiagramCommand = new ChangeDiagramCommand(_head.diagram);
      commandStack.execute(_changeDiagramCommand);
    }
    Iterable<XDomainObjectShape> _plus = Iterables.<XDomainObjectShape>concat(this.addedNodes, this.addedConnections);
    AddRemoveCommand _newAddCommand = AddRemoveCommand.newAddCommand(this.diagram, ((XShape[])Conversions.unwrapArray(_plus, XShape.class)));
    commandStack.execute(_newAddCommand);
    final Consumer<InterpreterContext> _function = (InterpreterContext it) -> {
      it.executeCommands(commandStack);
    };
    this.subContexts.forEach(_function);
  }
  
  @Pure
  public XDiagram getDiagram() {
    return this.diagram;
  }
  
  @Pure
  public boolean isReplaceRootDiagram() {
    return this.isReplaceRootDiagram;
  }
  
  public void setIsReplaceRootDiagram(final boolean isReplaceRootDiagram) {
    this.isReplaceRootDiagram = isReplaceRootDiagram;
  }
  
  @Pure
  public boolean isCreateNodes() {
    return this.isCreateNodes;
  }
  
  public void setIsCreateNodes(final boolean isCreateNodes) {
    this.isCreateNodes = isCreateNodes;
  }
  
  @Pure
  public boolean isCreateConnections() {
    return this.isCreateConnections;
  }
  
  public void setIsCreateConnections(final boolean isCreateConnections) {
    this.isCreateConnections = isCreateConnections;
  }
  
  @Pure
  public boolean isCreateDuplicateNodes() {
    return this.isCreateDuplicateNodes;
  }
  
  public void setIsCreateDuplicateNodes(final boolean isCreateDuplicateNodes) {
    this.isCreateDuplicateNodes = isCreateDuplicateNodes;
  }
  
  @Pure
  public List<InterpreterContext> getSubContexts() {
    return this.subContexts;
  }
  
  @Pure
  protected Set<XNode> getAddedNodes() {
    return this.addedNodes;
  }
  
  @Pure
  protected Set<XConnection> getAddedConnections() {
    return this.addedConnections;
  }
}
