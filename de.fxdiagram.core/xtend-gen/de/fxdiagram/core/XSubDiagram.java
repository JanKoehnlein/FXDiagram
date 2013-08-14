package de.fxdiagram.core;

import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XConnection;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XSubDiagram extends XAbstractDiagram {
  private Procedure1<? super XSubDiagram> contentsInitializer;
  
  private Group nodeLayer = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private Group buttonLayer = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  public XSubDiagram() {
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.buttonLayer);
    BooleanProperty _visibleProperty = this.visibleProperty();
    final ChangeListener<Boolean> _function = new ChangeListener<Boolean>() {
      public void changed(final ObservableValue<? extends Boolean> property, final Boolean oldVal, final Boolean newVal) {
        ObservableList<XConnection> _connections = XSubDiagram.this.getConnections();
        final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
          public void apply(final XConnection it) {
            it.setVisible((newVal).booleanValue());
          }
        };
        IterableExtensions.<XConnection>forEach(_connections, _function);
      }
    };
    _visibleProperty.addListener(_function);
  }
  
  public Procedure1<? super XSubDiagram> setContentsInitializer(final Procedure1<? super XSubDiagram> contentsInitializer) {
    Procedure1<? super XSubDiagram> _contentsInitializer = this.contentsInitializer = contentsInitializer;
    return _contentsInitializer;
  }
  
  public void doActivate() {
    super.doActivate();
    if (this.contentsInitializer!=null) {
      this.contentsInitializer.apply(this);
    }
  }
  
  public Group getNodeLayer() {
    return this.nodeLayer;
  }
  
  public Group getConnectionLayer() {
    XAbstractDiagram _parentDiagram = this.getParentDiagram();
    Group _connectionLayer = _parentDiagram.getConnectionLayer();
    return _connectionLayer;
  }
  
  public Group getButtonLayer() {
    return this.buttonLayer;
  }
  
  protected XAbstractDiagram getParentDiagram() {
    XAbstractDiagram _diagram = null;
    Parent _parent = this.getParent();
    if (_parent!=null) {
      _diagram=Extensions.getDiagram(_parent);
    }
    return _diagram;
  }
}
