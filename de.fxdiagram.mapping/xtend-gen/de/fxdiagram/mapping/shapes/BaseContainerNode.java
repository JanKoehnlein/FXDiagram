package de.fxdiagram.mapping.shapes;

import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDiagramContainer;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.mapping.reconcile.MappingLabelListener;
import de.fxdiagram.mapping.reconcile.NodeReconcileBehavior;
import de.fxdiagram.mapping.shapes.BaseShapeInitializer;
import de.fxdiagram.mapping.shapes.INodeWithLazyMappings;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Base implementation for an {@link XNode} that contains other nodes and belongs to an {@link IMappedElementDescriptor}.
 * 
 * If the descriptor is an {@link AbstractXtextDescriptor}, members are automatically injected using
 * the Xtext language's injector.
 */
@ModelNode
@SuppressWarnings("all")
public class BaseContainerNode<T extends Object> extends XNode implements INodeWithLazyMappings, XDiagramContainer {
  public final static String NODE_HEADING = "containerNodeHeading";
  
  private Group diagramGroup;
  
  public BaseContainerNode() {
    BaseShapeInitializer.initializeLazily(this);
  }
  
  public BaseContainerNode(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
  }
  
  @Override
  public IMappedElementDescriptor<T> getDomainObjectDescriptor() {
    DomainObjectDescriptor _domainObjectDescriptor = super.getDomainObjectDescriptor();
    return ((IMappedElementDescriptor<T>) _domainObjectDescriptor);
  }
  
  @Override
  protected Node createNode() {
    RectangleBorderPane _xblockexpression = null;
    {
      final VBox titleArea = new VBox();
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
        Insets _insets = this.getInsets();
        it.setPadding(_insets);
        ObservableList<Node> _children = it.getChildren();
        VBox _vBox = new VBox();
        final Procedure1<VBox> _function_1 = (VBox it_1) -> {
          it_1.setAlignment(Pos.CENTER);
          it_1.setSpacing(10);
          ObservableList<Node> _children_1 = it_1.getChildren();
          _children_1.add(titleArea);
          ObservableList<Node> _children_2 = it_1.getChildren();
          Group _group = new Group();
          _children_2.add((this.diagramGroup = _group));
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_1);
        _children.add(_doubleArrow);
      };
      final RectangleBorderPane pane = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
      ObservableList<XLabel> _labels = this.getLabels();
      Pair<String, Pane> _mappedTo = Pair.<String, Pane>of(BaseContainerNode.NODE_HEADING, titleArea);
      MappingLabelListener.<XLabel>addMappingLabelListener(_labels, _mappedTo);
      _xblockexpression = pane;
    }
    return _xblockexpression;
  }
  
  @Override
  public void doActivate() {
    ObservableList<Node> _children = this.diagramGroup.getChildren();
    XDiagram _innerDiagram = this.getInnerDiagram();
    final Procedure1<XDiagram> _function = (XDiagram it) -> {
      it.setIsRootDiagram(false);
      XDiagram _diagram = CoreExtensions.getDiagram(this);
      it.setParentDiagram(_diagram);
    };
    XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_innerDiagram, _function);
    _children.add(_doubleArrow);
    XDiagram _innerDiagram_1 = this.getInnerDiagram();
    _innerDiagram_1.activate();
    super.doActivate();
    IMappedElementDescriptor<T> _domainObjectDescriptor = this.getDomainObjectDescriptor();
    LazyConnectionMappingBehavior.<T>addLazyBehavior(this, _domainObjectDescriptor);
    NodeReconcileBehavior<Object> _nodeReconcileBehavior = new NodeReconcileBehavior<Object>(this);
    this.addBehavior(_nodeReconcileBehavior);
  }
  
  @Override
  public List<Side> getButtonSides(final ConnectionMapping<?> mapping) {
    return Collections.<Side>unmodifiableList(CollectionLiterals.<Side>newArrayList(Side.TOP, Side.BOTTOM, Side.LEFT, Side.RIGHT));
  }
  
  @Override
  protected Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 12, 12);
  }
  
  @Override
  public Insets getInsets() {
    return new Insets(10, 20, 20, 20);
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
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
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
