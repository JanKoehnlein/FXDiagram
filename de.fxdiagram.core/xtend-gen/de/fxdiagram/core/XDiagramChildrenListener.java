package de.fxdiagram.core;

import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XActivatable;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XDiagramChildrenListener<T extends Node & XActivatable> implements ListChangeListener<T> {
  private Group layer;
  
  private XAbstractDiagram diagram;
  
  public XDiagramChildrenListener(final XAbstractDiagram diagram, final Group layer) {
    this.layer = layer;
    this.diagram = diagram;
  }
  
  public void onChanged(final Change<? extends T> change) {
    boolean _next = change.next();
    boolean _while = _next;
    while (_while) {
      {
        boolean _wasAdded = change.wasAdded();
        if (_wasAdded) {
          List<? extends T> _addedSubList = change.getAddedSubList();
          final Procedure1<Node> _function = new Procedure1<Node>() {
            public void apply(final Node it) {
              ObservableList<Node> _children = XDiagramChildrenListener.this.layer.getChildren();
              _children.add(it);
              boolean _isActive = XDiagramChildrenListener.this.diagram.getIsActive();
              if (_isActive) {
                ((XActivatable) it).activate();
              }
            }
          };
          IterableExtensions.forEach(_addedSubList, _function);
        }
        boolean _wasRemoved = change.wasRemoved();
        if (_wasRemoved) {
          List<? extends T> _removed = change.getRemoved();
          final Procedure1<Node> _function_1 = new Procedure1<Node>() {
            public void apply(final Node it) {
              ObservableList<Node> _children = XDiagramChildrenListener.this.layer.getChildren();
              _children.remove(it);
            }
          };
          IterableExtensions.forEach(_removed, _function_1);
        }
      }
      boolean _next_1 = change.next();
      _while = _next_1;
    }
  }
}
