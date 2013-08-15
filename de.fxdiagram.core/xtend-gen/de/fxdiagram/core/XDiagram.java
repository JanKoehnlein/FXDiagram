package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XConnection;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XDiagram extends XAbstractDiagram {
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
  
  private XAbstractDiagram parentDiagram;
  
  private Procedure1<? super XDiagram> contentsInitializer;
  
  public XDiagram() {
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.buttonLayer);
    ReadOnlyObjectProperty<Parent> _parentProperty = this.parentProperty();
    final ChangeListener<Parent> _function = new ChangeListener<Parent>() {
      public void changed(final ObservableValue<? extends Parent> property, final Parent oldValue, final Parent newValue) {
        XAbstractDiagram _diagram = Extensions.getDiagram(newValue);
        XDiagram.this.parentDiagram = _diagram;
        boolean _equals = Objects.equal(XDiagram.this.parentDiagram, null);
        XDiagram.this.isRootDiagramProperty.set(_equals);
      }
    };
    _parentProperty.addListener(_function);
    final ChangeListener<Boolean> _function_1 = new ChangeListener<Boolean>() {
      public void changed(final ObservableValue<? extends Boolean> property, final Boolean oldValue, final Boolean newValue) {
        if ((newValue).booleanValue()) {
          ObservableList<Node> _children = XDiagram.this.nodeLayer.getChildren();
          ObservableList<XConnection> _connections = XDiagram.this.getConnections();
          Iterables.<Node>addAll(_children, _connections);
        } else {
          ObservableList<Node> _children_1 = XDiagram.this.nodeLayer.getChildren();
          ObservableList<XConnection> _connections_1 = XDiagram.this.getConnections();
          Iterables.removeAll(_children_1, _connections_1);
        }
      }
    };
    this.isRootDiagramProperty.addListener(_function_1);
  }
  
  public void doActivate() {
    super.doActivate();
    if (this.contentsInitializer!=null) {
      this.contentsInitializer.apply(this);
    }
  }
  
  public Procedure1<? super XDiagram> setContentsInitializer(final Procedure1<? super XDiagram> contentsInitializer) {
    Procedure1<? super XDiagram> _contentsInitializer = this.contentsInitializer = contentsInitializer;
    return _contentsInitializer;
  }
  
  public XAbstractDiagram getParentDiagram() {
    return this.parentDiagram;
  }
  
  public Group getNodeLayer() {
    return this.nodeLayer;
  }
  
  public Group getConnectionLayer() {
    Group _xifexpression = null;
    boolean _isRootDiagram = this.getIsRootDiagram();
    if (_isRootDiagram) {
      _xifexpression = this.nodeLayer;
    } else {
      Group _nodeLayer = this.parentDiagram.getNodeLayer();
      _xifexpression = _nodeLayer;
    }
    return _xifexpression;
  }
  
  public Group getButtonLayer() {
    return this.buttonLayer;
  }
  
  private ReadOnlyBooleanWrapper isRootDiagramProperty = new ReadOnlyBooleanWrapper(this, "isRootDiagram");
  
  public boolean getIsRootDiagram() {
    return this.isRootDiagramProperty.get();
  }
  
  public ReadOnlyBooleanProperty isRootDiagramProperty() {
    return this.isRootDiagramProperty.getReadOnlyProperty();
  }
}
