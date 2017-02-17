package de.fxdiagram.mapping.shapes;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDiagramContainer;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListener;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.mapping.reconcile.MappingLabelListener;
import de.fxdiagram.mapping.reconcile.NodeReconcileBehavior;
import de.fxdiagram.mapping.shapes.INodeWithLazyMappings;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
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
@Beta
@ModelNode("innerDiagram")
@SuppressWarnings("all")
public class BaseContainerNode<T extends Object> extends XNode implements INodeWithLazyMappings, XDiagramContainer {
  public static class ReconcileBehavior<T extends Object> extends NodeReconcileBehavior<T> {
    public ReconcileBehavior(final BaseContainerNode<T> host) {
      super(host);
    }
    
    @Override
    public BaseContainerNode<T> getHost() {
      XNode _host = super.getHost();
      return ((BaseContainerNode<T>) _host);
    }
    
    @Override
    public DirtyState getDirtyState() {
      final DirtyState dirtyFromSuper = super.getDirtyState();
      de.fxdiagram.core.behavior.ReconcileBehavior _innerDiagramReconcileBehavior = this.getInnerDiagramReconcileBehavior();
      DirtyState _dirtyState = null;
      if (_innerDiagramReconcileBehavior!=null) {
        _dirtyState=_innerDiagramReconcileBehavior.getDirtyState();
      }
      final DirtyState dirtyFromInnerDiagram = _dirtyState;
      boolean _equals = Objects.equal(dirtyFromSuper, DirtyState.CLEAN);
      if (_equals) {
        return dirtyFromInnerDiagram;
      }
      return null;
    }
    
    @Override
    protected void reconcile(final AbstractMapping<T> mapping, final T domainObject, final de.fxdiagram.core.behavior.ReconcileBehavior.UpdateAcceptor acceptor) {
      super.reconcile(mapping, domainObject, acceptor);
      de.fxdiagram.core.behavior.ReconcileBehavior _innerDiagramReconcileBehavior = this.getInnerDiagramReconcileBehavior();
      if (_innerDiagramReconcileBehavior!=null) {
        _innerDiagramReconcileBehavior.reconcile(acceptor);
      }
    }
    
    @Override
    public void hideDirtyState() {
      super.hideDirtyState();
      de.fxdiagram.core.behavior.ReconcileBehavior _innerDiagramReconcileBehavior = this.getInnerDiagramReconcileBehavior();
      if (_innerDiagramReconcileBehavior!=null) {
        _innerDiagramReconcileBehavior.hideDirtyState();
      }
    }
    
    @Override
    public void showDirtyState(final DirtyState dirtyState) {
      super.showDirtyState(dirtyState);
      de.fxdiagram.core.behavior.ReconcileBehavior _innerDiagramReconcileBehavior = this.getInnerDiagramReconcileBehavior();
      if (_innerDiagramReconcileBehavior!=null) {
        de.fxdiagram.core.behavior.ReconcileBehavior _innerDiagramReconcileBehavior_1 = this.getInnerDiagramReconcileBehavior();
        DirtyState _dirtyState = _innerDiagramReconcileBehavior_1.getDirtyState();
        _innerDiagramReconcileBehavior.showDirtyState(_dirtyState);
      }
    }
    
    protected de.fxdiagram.core.behavior.ReconcileBehavior getInnerDiagramReconcileBehavior() {
      BaseContainerNode<T> _host = this.getHost();
      XDiagram _innerDiagram = _host.getInnerDiagram();
      return _innerDiagram.<de.fxdiagram.core.behavior.ReconcileBehavior>getBehavior(de.fxdiagram.core.behavior.ReconcileBehavior.class);
    }
  }
  
  public final static String NODE_HEADING = "containerNodeHeading";
  
  private Group diagramGroup;
  
  public BaseContainerNode(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
  }
  
  @Override
  public void postLoad() {
    IMappedElementDescriptor<T> _domainObjectDescriptor = this.getDomainObjectDescriptor();
    AbstractMapping<T> _mapping = null;
    if (_domainObjectDescriptor!=null) {
      _mapping=_domainObjectDescriptor.getMapping();
    }
    XDiagramConfig _config = null;
    if (_mapping!=null) {
      _config=_mapping.getConfig();
    }
    if (_config!=null) {
      _config.initialize(this);
    }
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
      VBox _vBox = new VBox();
      final Procedure1<VBox> _function = (VBox it) -> {
        it.setAlignment(Pos.CENTER);
      };
      final VBox titleArea = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function_1 = (RectangleBorderPane it) -> {
        Insets _insets = this.getInsets();
        it.setPadding(_insets);
        ObservableList<Node> _children = it.getChildren();
        VBox _vBox_1 = new VBox();
        final Procedure1<VBox> _function_2 = (VBox it_1) -> {
          it_1.setAlignment(Pos.CENTER);
          it_1.setSpacing(10);
          ObservableList<Node> _children_1 = it_1.getChildren();
          _children_1.add(titleArea);
          ObservableList<Node> _children_2 = it_1.getChildren();
          Group _group = new Group();
          Group _diagramGroup = (this.diagramGroup = _group);
          _children_2.add(_diagramGroup);
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_2);
        _children.add(_doubleArrow);
      };
      final RectangleBorderPane pane = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function_1);
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
    BaseContainerNode.ReconcileBehavior<T> _reconcileBehavior = new BaseContainerNode.ReconcileBehavior<T>(this);
    this.addBehavior(_reconcileBehavior);
    XDiagram _innerDiagram = this.getInnerDiagram();
    ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = _innerDiagram.boundsInLocalProperty();
    final ChangeListener<Bounds> _function_1 = (ObservableValue<? extends Bounds> p, Bounds o, Bounds n) -> {
      if (((!this.layoutXProperty().isBound()) && (!this.layoutYProperty().isBound()))) {
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
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public BaseContainerNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(innerDiagramProperty, XDiagram.class);
  }
  
  public String toString() {
    return ToString.toString(this);
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
