package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.XAbstractDiagram;
import de.itemis.javafx.diagram.XConnection;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.XRapidButton;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;

@SuppressWarnings("all")
public class XNestedDiagram extends XAbstractDiagram {
  private XAbstractDiagram _parentDiagram;
  
  public XAbstractDiagram getParentDiagram() {
    return this._parentDiagram;
  }
  
  public void setParentDiagram(final XAbstractDiagram parentDiagram) {
    this._parentDiagram = parentDiagram;
  }
  
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
  
  public XNestedDiagram() {
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.buttonLayer);
    this.setScaleX(0.3);
    this.setScaleY(0.3);
    this.setManaged(false);
    final Procedure3<ObservableValue<? extends Boolean>,Boolean,Boolean> _function = new Procedure3<ObservableValue<? extends Boolean>,Boolean,Boolean>() {
        public void apply(final ObservableValue<? extends Boolean> property, final Boolean oldVal, final Boolean newVal) {
          List<XConnection> _connections = XNestedDiagram.this.getConnections();
          final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
              public void apply(final XConnection it) {
                it.setVisible((newVal).booleanValue());
              }
            };
          IterableExtensions.<XConnection>forEach(_connections, _function);
        }
      };
    final ChangeListener<Boolean> visibilityListener = new ChangeListener<Boolean>() {
        public void changed(ObservableValue<? extends Boolean> observable,Boolean oldValue,Boolean newValue) {
          _function.apply(observable,oldValue,newValue);
        }
    };
    BooleanProperty _visibleProperty = this.visibleProperty();
    _visibleProperty.addListener(visibilityListener);
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
  
  public boolean internalAddNode(final XNode node) {
    XAbstractDiagram _parentDiagram = this.getParentDiagram();
    boolean _internalAddNode = _parentDiagram.internalAddNode(node);
    return _internalAddNode;
  }
  
  public boolean internalAddButton(final XRapidButton button) {
    XAbstractDiagram _parentDiagram = this.getParentDiagram();
    boolean _internalAddButton = _parentDiagram.internalAddButton(button);
    return _internalAddButton;
  }
}
