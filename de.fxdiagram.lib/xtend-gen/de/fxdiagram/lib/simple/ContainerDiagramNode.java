package de.fxdiagram.lib.simple;

import com.google.common.annotations.Beta;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDiagramContainer;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * An {@link XNode} containing another diagram.
 * 
 * The node's border automatically scales around its contents.
 */
@Beta
@Logging
@ModelNode("innerDiagram")
@SuppressWarnings("all")
public class ContainerDiagramNode extends XNode implements XDiagramContainer {
  private RectangleBorderPane pane = new RectangleBorderPane();
  
  private final Insets padding = new Insets(20, 20, 20, 20);
  
  public ContainerDiagramNode(final String name) {
    super(name);
  }
  
  public ContainerDiagramNode(final DomainObjectDescriptor domainObject) {
    super(domainObject);
  }
  
  @Override
  protected Node createNode() {
    final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
      it.setPadding(this.padding);
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(
      this.pane, _function);
  }
  
  @Override
  public void initializeGraphics() {
    super.initializeGraphics();
    final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
      ObservableList<Node> _children = it.getChildren();
      Group _group = new Group();
      final Procedure1<Group> _function_1 = (Group it_1) -> {
        ObservableList<Node> _children_1 = it_1.getChildren();
        XDiagram _innerDiagram = this.getInnerDiagram();
        final Procedure1<XDiagram> _function_2 = (XDiagram it_2) -> {
          it_2.setIsRootDiagram(false);
          XDiagram _diagram = CoreExtensions.getDiagram(this);
          it_2.setParentDiagram(_diagram);
        };
        XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_innerDiagram, _function_2);
        _children_1.add(_doubleArrow);
      };
      Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_1);
      _children.add(_doubleArrow);
    };
    ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(
      this.pane, _function);
  }
  
  @Override
  protected Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 12, 12);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    XDiagram _innerDiagram = this.getInnerDiagram();
    _innerDiagram.activate();
    XDiagram _innerDiagram_1 = this.getInnerDiagram();
    ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = _innerDiagram_1.boundsInLocalProperty();
    final ChangeListener<Bounds> _function = (ObservableValue<? extends Bounds> p, Bounds o, Bounds n) -> {
      double _layoutX = this.getLayoutX();
      double _minX = n.getMinX();
      double _minX_1 = o.getMinX();
      double _minus = (_minX - _minX_1);
      double _plus = (_layoutX + _minus);
      this.setLayoutX(_plus);
      double _layoutY = this.getLayoutY();
      double _minY = n.getMinY();
      double _minY_1 = o.getMinY();
      double _minus_1 = (_minY - _minY_1);
      double _plus_1 = (_layoutY + _minus_1);
      this.setLayoutY(_plus_1);
    };
    _boundsInLocalProperty.addListener(_function);
  }
  
  @Override
  public Insets getInsets() {
    return this.padding;
  }
  
  @Override
  public void toFront() {
    super.toFront();
    XDiagram _innerDiagram = this.getInnerDiagram();
    ObservableList<XNode> _nodes = _innerDiagram.getNodes();
    XDiagram _innerDiagram_1 = this.getInnerDiagram();
    ObservableList<XConnection> _connections = _innerDiagram_1.getConnections();
    Iterable<XDomainObjectShape> _plus = Iterables.<XDomainObjectShape>concat(_nodes, _connections);
    final Consumer<XDomainObjectShape> _function = (XDomainObjectShape it) -> {
      it.toFront();
    };
    _plus.forEach(_function);
  }
  
  @Override
  public boolean isInnerDiagramActive() {
    return this.getIsActive();
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.lib.simple.ContainerDiagramNode");
    ;
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public ContainerDiagramNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(innerDiagramProperty, XDiagram.class);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private SimpleObjectProperty<XDiagram> innerDiagramProperty = new SimpleObjectProperty<XDiagram>(this, "innerDiagram",_initInnerDiagram());
  
  private static final XDiagram _initInnerDiagram() {
    XDiagram _xDiagram = new XDiagram();
    return _xDiagram;
  }
  
  public XDiagram getInnerDiagram() {
    return this.innerDiagramProperty.get();
  }
  
  public void setInnerDiagram(final XDiagram innerDiagram) {
    this.innerDiagramProperty.set(innerDiagram);
  }
  
  public ObjectProperty<XDiagram> innerDiagramProperty() {
    return this.innerDiagramProperty;
  }
}
