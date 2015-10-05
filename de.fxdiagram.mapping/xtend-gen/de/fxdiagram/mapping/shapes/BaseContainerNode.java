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
import de.fxdiagram.core.extensions.InitializingListener;
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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
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
    super.doActivate();
    InitializingListener<XDiagram> _initializingListener = new InitializingListener<XDiagram>();
    final Procedure1<InitializingListener<XDiagram>> _function = (InitializingListener<XDiagram> it) -> {
      final Procedure1<XDiagram> _function_1 = (XDiagram newDiagram) -> {
        ObservableList<Node> _children = this.diagramGroup.getChildren();
        _children.clear();
        final Procedure1<XDiagram> _function_2 = (XDiagram it_1) -> {
          it_1.setIsRootDiagram(false);
          XDiagram _diagram = CoreExtensions.getDiagram(this);
          it_1.setParentDiagram(_diagram);
        };
        ObjectExtensions.<XDiagram>operator_doubleArrow(newDiagram, _function_2);
        ObservableList<Node> _children_1 = this.diagramGroup.getChildren();
        _children_1.add(newDiagram);
        newDiagram.activate();
      };
      it.setSet(_function_1);
      final Procedure1<XDiagram> _function_2 = (XDiagram it_1) -> {
        ObservableList<Node> _children = this.diagramGroup.getChildren();
        _children.clear();
      };
      it.setUnset(_function_2);
    };
    InitializingListener<XDiagram> _doubleArrow = ObjectExtensions.<InitializingListener<XDiagram>>operator_doubleArrow(_initializingListener, _function);
    CoreExtensions.<XDiagram>addInitializingListener(this.innerDiagramProperty, _doubleArrow);
    IMappedElementDescriptor<T> _domainObjectDescriptor = this.getDomainObjectDescriptor();
    LazyConnectionMappingBehavior.<T>addLazyBehavior(this, _domainObjectDescriptor);
    NodeReconcileBehavior<Object> _nodeReconcileBehavior = new NodeReconcileBehavior<Object>(this);
    this.addBehavior(_nodeReconcileBehavior);
    XDiagram _innerDiagram = this.getInnerDiagram();
    ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = _innerDiagram.boundsInLocalProperty();
    final ChangeListener<Bounds> _function_1 = (ObservableValue<? extends Bounds> p, Bounds o, Bounds n) -> {
      boolean _and = false;
      DoubleProperty _layoutXProperty = this.layoutXProperty();
      boolean _isBound = _layoutXProperty.isBound();
      boolean _not = (!_isBound);
      if (!_not) {
        _and = false;
      } else {
        DoubleProperty _layoutYProperty = this.layoutYProperty();
        boolean _isBound_1 = _layoutYProperty.isBound();
        boolean _not_1 = (!_isBound_1);
        _and = _not_1;
      }
      if (_and) {
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
      }
    };
    _boundsInLocalProperty.addListener(_function_1);
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
  
  private SimpleObjectProperty<XDiagram> innerDiagramProperty = new SimpleObjectProperty<XDiagram>(this, "innerDiagram");
  
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
