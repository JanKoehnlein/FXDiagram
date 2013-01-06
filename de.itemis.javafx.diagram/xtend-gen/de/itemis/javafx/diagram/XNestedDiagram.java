package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.Extensions;
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
import javafx.scene.Parent;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;

@SuppressWarnings("all")
public class XNestedDiagram extends XAbstractDiagram {
  private Procedure1<? super XNestedDiagram> contentsInitializer;
  
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
        public void changed(ObservableValue<? extends Boolean> arg0,Boolean arg1,Boolean arg2) {
          _function.apply(arg0,arg1,arg2);
        }
    };
    BooleanProperty _visibleProperty = this.visibleProperty();
    _visibleProperty.addListener(visibilityListener);
  }
  
  public Procedure1<? super XNestedDiagram> setContentsInitializer(final Procedure1<? super XNestedDiagram> contentsInitializer) {
    Procedure1<? super XNestedDiagram> _contentsInitializer = this.contentsInitializer = contentsInitializer;
    return _contentsInitializer;
  }
  
  public void doActivate() {
    if (this.contentsInitializer!=null) this.contentsInitializer.apply(this);
    super.doActivate();
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
    boolean _xblockexpression = false;
    {
      super.internalAddNode(node);
      XAbstractDiagram _parentDiagram = this.getParentDiagram();
      boolean _internalAddNode = _parentDiagram.internalAddNode(node);
      _xblockexpression = (_internalAddNode);
    }
    return _xblockexpression;
  }
  
  public boolean internalAddButton(final XRapidButton button) {
    boolean _xblockexpression = false;
    {
      super.internalAddButton(button);
      XAbstractDiagram _parentDiagram = this.getParentDiagram();
      boolean _internalAddButton = _parentDiagram.internalAddButton(button);
      _xblockexpression = (_internalAddButton);
    }
    return _xblockexpression;
  }
  
  protected XAbstractDiagram getParentDiagram() {
    Parent _parent = this.getParent();
    XAbstractDiagram _diagram = _parent==null?(XAbstractDiagram)null:Extensions.getDiagram(_parent);
    return _diagram;
  }
}
