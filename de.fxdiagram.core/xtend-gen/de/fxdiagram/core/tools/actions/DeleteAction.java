package de.fxdiagram.core.tools.actions;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.tools.actions.DiagramAction;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DeleteAction implements DiagramAction {
  public void perform(final XRoot root) {
    final Iterable<XShape> elements = root.getCurrentSelection();
    final Iterable<XNode> nodes = Iterables.<XNode>filter(elements, XNode.class);
    final Function1<XNode,ObservableList<XConnection>> _function = new Function1<XNode,ObservableList<XConnection>>() {
      public ObservableList<XConnection> apply(final XNode it) {
        return it.getIncomingConnections();
      }
    };
    Iterable<ObservableList<XConnection>> _map = IterableExtensions.<XNode, ObservableList<XConnection>>map(nodes, _function);
    final Procedure1<ObservableList<XConnection>> _function_1 = new Procedure1<ObservableList<XConnection>>() {
      public void apply(final ObservableList<XConnection> it) {
        XDiagram _diagram = root.getDiagram();
        ObservableList<XConnection> _connections = _diagram.getConnections();
        Iterables.removeAll(_connections, it);
      }
    };
    IterableExtensions.<ObservableList<XConnection>>forEach(_map, _function_1);
    final Function1<XNode,ObservableList<XConnection>> _function_2 = new Function1<XNode,ObservableList<XConnection>>() {
      public ObservableList<XConnection> apply(final XNode it) {
        return it.getOutgoingConnections();
      }
    };
    Iterable<ObservableList<XConnection>> _map_1 = IterableExtensions.<XNode, ObservableList<XConnection>>map(nodes, _function_2);
    final Procedure1<ObservableList<XConnection>> _function_3 = new Procedure1<ObservableList<XConnection>>() {
      public void apply(final ObservableList<XConnection> it) {
        XDiagram _diagram = root.getDiagram();
        ObservableList<XConnection> _connections = _diagram.getConnections();
        Iterables.removeAll(_connections, it);
      }
    };
    IterableExtensions.<ObservableList<XConnection>>forEach(_map_1, _function_3);
    Iterable<XConnection> _filter = Iterables.<XConnection>filter(elements, XConnection.class);
    final Procedure1<XConnection> _function_4 = new Procedure1<XConnection>() {
      public void apply(final XConnection it) {
        XDiagram _diagram = root.getDiagram();
        ObservableList<XConnection> _connections = _diagram.getConnections();
        _connections.remove(it);
      }
    };
    IterableExtensions.<XConnection>forEach(_filter, _function_4);
    final Procedure1<XNode> _function_5 = new Procedure1<XNode>() {
      public void apply(final XNode it) {
        XDiagram _diagram = root.getDiagram();
        ObservableList<XNode> _nodes = _diagram.getNodes();
        _nodes.remove(it);
      }
    };
    IterableExtensions.<XNode>forEach(nodes, _function_5);
  }
}
