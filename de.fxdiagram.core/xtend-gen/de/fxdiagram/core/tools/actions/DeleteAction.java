package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.Symbol;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class DeleteAction implements DiagramAction {
  public boolean matches(final KeyEvent event) {
    boolean _or = false;
    KeyCode _code = event.getCode();
    boolean _equals = Objects.equal(_code, KeyCode.DELETE);
    if (_equals) {
      _or = true;
    } else {
      KeyCode _code_1 = event.getCode();
      boolean _equals_1 = Objects.equal(_code_1, KeyCode.BACK_SPACE);
      _or = _equals_1;
    }
    return _or;
  }
  
  public Symbol.Type getSymbol() {
    return Symbol.Type.TRASH;
  }
  
  public String getTooltip() {
    return "Delete selection";
  }
  
  public void perform(final XRoot root) {
    final Iterable<XShape> elements = root.getCurrentSelection();
    final Iterable<XNode> nodes = Iterables.<XNode>filter(elements, XNode.class);
    final Function1<XNode, ObservableList<XConnection>> _function = new Function1<XNode, ObservableList<XConnection>>() {
      public ObservableList<XConnection> apply(final XNode it) {
        return it.getIncomingConnections();
      }
    };
    Iterable<ObservableList<XConnection>> _map = IterableExtensions.<XNode, ObservableList<XConnection>>map(nodes, _function);
    Iterable<XConnection> _flatten = Iterables.<XConnection>concat(_map);
    final Function1<XNode, ObservableList<XConnection>> _function_1 = new Function1<XNode, ObservableList<XConnection>>() {
      public ObservableList<XConnection> apply(final XNode it) {
        return it.getOutgoingConnections();
      }
    };
    Iterable<ObservableList<XConnection>> _map_1 = IterableExtensions.<XNode, ObservableList<XConnection>>map(nodes, _function_1);
    Iterable<XConnection> _flatten_1 = Iterables.<XConnection>concat(_map_1);
    Iterable<XConnection> _plus = Iterables.<XConnection>concat(_flatten, _flatten_1);
    final Iterable<XShape> deleteThem = Iterables.<XShape>concat(_plus, elements);
    CommandStack _commandStack = root.getCommandStack();
    XDiagram _diagram = root.getDiagram();
    AddRemoveCommand _newRemoveCommand = AddRemoveCommand.newRemoveCommand(_diagram, ((XShape[])Conversions.unwrapArray(deleteThem, XShape.class)));
    _commandStack.execute(_newRemoveCommand);
  }
}
