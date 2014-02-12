package de.fxdiagram.core;

import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.extensions.InitializingListListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XDiagramChildrenListener<T extends Node & XActivatable> extends InitializingListListener<T> {
  private Group layer;
  
  private XDiagram diagram;
  
  public XDiagramChildrenListener(final XDiagram diagram, final Group layer) {
    this.layer = layer;
    this.diagram = diagram;
    final Procedure1<T> _function = new Procedure1<T>() {
      public void apply(final T it) {
        if ((it instanceof XShape)) {
          ((XShape)it).initializeGraphics();
        }
        ObservableList<Node> _children = layer.getChildren();
        _children.add(it);
        boolean _isActive = diagram.getIsActive();
        if (_isActive) {
          it.activate();
        }
      }
    };
    this.setAdd(_function);
    final Procedure1<T> _function_1 = new Procedure1<T>() {
      public void apply(final T it) {
        ObservableList<Node> _children = layer.getChildren();
        _children.remove(it);
      }
    };
    this.setRemove(_function_1);
  }
}
