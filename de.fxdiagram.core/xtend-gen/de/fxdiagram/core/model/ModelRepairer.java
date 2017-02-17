package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDiagramContainer;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@Logging
@SuppressWarnings("all")
public class ModelRepairer {
  protected boolean _repair(final Object node) {
    return false;
  }
  
  protected boolean _repair(final XRoot root) {
    XDiagram _diagram = root.getDiagram();
    return this.repair(_diagram);
  }
  
  protected boolean _repair(final XDiagram diagram) {
    boolean _xblockexpression = false;
    {
      final HashSet<XConnection> deleteThem = CollectionLiterals.<XConnection>newHashSet();
      ObservableList<XConnection> _connections = diagram.getConnections();
      final Consumer<XConnection> _function = (XConnection it) -> {
        boolean _repair = this.repair(it);
        if (_repair) {
          deleteThem.add(it);
        }
      };
      _connections.forEach(_function);
      ObservableList<XConnection> _connections_1 = diagram.getConnections();
      Iterables.removeAll(_connections_1, deleteThem);
      ObservableList<XNode> _nodes = diagram.getNodes();
      final Consumer<XNode> _function_1 = (XNode it) -> {
        this.repair(it);
      };
      _nodes.forEach(_function_1);
      _xblockexpression = false;
    }
    return _xblockexpression;
  }
  
  protected boolean _repair(final XNode node) {
    boolean _xblockexpression = false;
    {
      ObservableList<XConnection> _outgoingConnections = node.getOutgoingConnections();
      final Consumer<XConnection> _function = (XConnection it) -> {
        XNode _source = it.getSource();
        boolean _notEquals = (!Objects.equal(_source, node));
        if (_notEquals) {
          ModelRepairer.LOG.severe(((("Node " + it) + " is not source of outgoing connection ") + it));
          it.setSource(node);
        }
      };
      _outgoingConnections.forEach(_function);
      ObservableList<XConnection> _incomingConnections = node.getIncomingConnections();
      final Consumer<XConnection> _function_1 = (XConnection it) -> {
        XNode _target = it.getTarget();
        boolean _notEquals = (!Objects.equal(_target, node));
        if (_notEquals) {
          ModelRepairer.LOG.severe(((("Node " + it) + " is not target of incoming connection ") + it));
          it.setTarget(node);
        }
      };
      _incomingConnections.forEach(_function_1);
      if ((node instanceof XDiagramContainer)) {
        XDiagram _innerDiagram = ((XDiagramContainer)node).getInnerDiagram();
        this.repair(_innerDiagram);
      }
      _xblockexpression = false;
    }
    return _xblockexpression;
  }
  
  protected boolean _repair(final XConnection it) {
    boolean deleteIt = false;
    XNode _source = it.getSource();
    boolean _equals = Objects.equal(_source, null);
    if (_equals) {
      ModelRepairer.LOG.severe((("Connection " + it) + " lacks source node"));
      deleteIt = true;
    } else {
      XNode _source_1 = it.getSource();
      ObservableList<XConnection> _outgoingConnections = _source_1.getOutgoingConnections();
      boolean _contains = _outgoingConnections.contains(it);
      boolean _not = (!_contains);
      if (_not) {
        ModelRepairer.LOG.severe((("Connection " + it) + " not contained in outgoing connections of source node"));
        XNode _source_2 = it.getSource();
        ObservableList<XConnection> _outgoingConnections_1 = _source_2.getOutgoingConnections();
        _outgoingConnections_1.add(it);
      }
    }
    XNode _target = it.getTarget();
    boolean _equals_1 = Objects.equal(_target, null);
    if (_equals_1) {
      ModelRepairer.LOG.severe((("Connection " + it) + " lacks target node"));
      deleteIt = true;
    } else {
      XNode _target_1 = it.getTarget();
      ObservableList<XConnection> _incomingConnections = _target_1.getIncomingConnections();
      boolean _contains_1 = _incomingConnections.contains(it);
      boolean _not_1 = (!_contains_1);
      if (_not_1) {
        ModelRepairer.LOG.severe((("Connection " + it) + " not contained in incoming connections of target node"));
        XNode _target_2 = it.getTarget();
        ObservableList<XConnection> _incomingConnections_1 = _target_2.getIncomingConnections();
        _incomingConnections_1.add(it);
      }
    }
    return deleteIt;
  }
  
  public boolean repair(final Object it) {
    if (it instanceof XConnection) {
      return _repair((XConnection)it);
    } else if (it instanceof XNode) {
      return _repair((XNode)it);
    } else if (it instanceof XDiagram) {
      return _repair((XDiagram)it);
    } else if (it instanceof XRoot) {
      return _repair((XRoot)it);
    } else if (it != null) {
      return _repair(it);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(it).toString());
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.model.ModelRepairer");
    ;
}
