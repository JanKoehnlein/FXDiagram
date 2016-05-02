package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.anchors.RectangleAnchors;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.StringDescriptor;
import de.fxdiagram.core.model.ToString;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * A node in an {@link XDiagram} that can be connected to other {@link XNode}s via
 * {@link XConnections}.
 * 
 * Clients should inherit from this class and usually adapt
 * <ul>
 * <li>{@link #createNode} to customize the node's initial apperance</li>
 * <li>{@link #initializeGraphics} to customize the node's appearance after activation</li>
 * <li>{@link #doActivate} to register event listeners and initialize behaviors</li>
 * </ul>
 * and pass the nderlying semantic element as {@link #domainObject} in the super constructor.
 * 
 * An {@link XNode} knows its {@link outgoingConnections} and {incomingConnections}. These
 * properties are automatically kept in sync with their counterparts {@link XConnection#source}
 * and {@link XConnection#target}.
 * 
 * The {@link anchors} describe how {@link XConnection}s should connect graphically with
 * the node.
 */
@Logging
@ModelNode({ "layoutX", "layoutY", "width", "height", "labels", "placementHint" })
@SuppressWarnings("all")
public class XNode extends XDomainObjectShape {
  @Accessors(AccessorType.PUBLIC_GETTER)
  private Group placementGroup = new Group();
  
  private Effect mouseOverEffect;
  
  private Effect selectionEffect;
  
  private Effect originalEffect;
  
  private Anchors anchors;
  
  public XNode(final DomainObjectDescriptor domainObject) {
    super(domainObject);
  }
  
  public XNode(final String name) {
    this(new StringDescriptor(name));
  }
  
  public String getName() {
    String _xblockexpression = null;
    {
      DomainObjectDescriptor _domainObjectDescriptor = this.getDomainObjectDescriptor();
      String _name = null;
      if (_domainObjectDescriptor!=null) {
        _name=_domainObjectDescriptor.getName();
      }
      final String name = _name;
      boolean _equals = Objects.equal(name, null);
      if (_equals) {
        XNode.LOG.severe("XNode\'s name is null");
      }
      _xblockexpression = name;
    }
    return _xblockexpression;
  }
  
  protected InnerShadow createMouseOverEffect() {
    return new InnerShadow();
  }
  
  protected DropShadow createSelectionEffect() {
    DropShadow _dropShadow = new DropShadow();
    final Procedure1<DropShadow> _function = (DropShadow it) -> {
      it.setOffsetX(4.0);
      it.setOffsetY(4.0);
    };
    return ObjectExtensions.<DropShadow>operator_doubleArrow(_dropShadow, _function);
  }
  
  protected Anchors createAnchors() {
    return new RectangleAnchors(this);
  }
  
  @Override
  public Node getNode() {
    Node _xblockexpression = null;
    {
      final Node node = super.getNode();
      InnerShadow _createMouseOverEffect = this.createMouseOverEffect();
      this.mouseOverEffect = _createMouseOverEffect;
      DropShadow _createSelectionEffect = this.createSelectionEffect();
      this.selectionEffect = _createSelectionEffect;
      Anchors _createAnchors = this.createAnchors();
      this.anchors = _createAnchors;
      _xblockexpression = node;
    }
    return _xblockexpression;
  }
  
  @Override
  protected boolean addNodeAsChild(final Node newNode) {
    ObservableList<Node> _children = this.getChildren();
    final Procedure1<Group> _function = (Group it) -> {
      ObservableList<Node> _children_1 = it.getChildren();
      _children_1.add(newNode);
    };
    Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(this.placementGroup, _function);
    return _children.add(_doubleArrow);
  }
  
  @Override
  protected Node createNode() {
    return null;
  }
  
  @Override
  public void initializeGraphics() {
    super.initializeGraphics();
  }
  
  @Override
  public void doActivate() {
    MoveBehavior<XNode> _moveBehavior = new MoveBehavior<XNode>(this);
    this.addBehavior(_moveBehavior);
    final EventHandler<MouseEvent> _function = (MouseEvent it) -> {
      Node _node = this.getNode();
      Effect _effect = _node.getEffect();
      this.originalEffect = _effect;
      Node _node_1 = this.getNode();
      Effect _elvis = null;
      if (this.mouseOverEffect != null) {
        _elvis = this.mouseOverEffect;
      } else {
        _elvis = this.originalEffect;
      }
      _node_1.setEffect(_elvis);
    };
    this.setOnMouseEntered(_function);
    final EventHandler<MouseEvent> _function_1 = (MouseEvent it) -> {
      Node _node = this.getNode();
      _node.setEffect(this.originalEffect);
    };
    this.setOnMouseExited(_function_1);
    ObservableList<XLabel> _labels = this.getLabels();
    InitializingListListener<XLabel> _initializingListListener = new InitializingListListener<XLabel>();
    final Procedure1<InitializingListListener<XLabel>> _function_2 = (InitializingListListener<XLabel> it) -> {
      final Procedure1<XLabel> _function_3 = (XLabel it_1) -> {
        it_1.activate();
      };
      it.setAdd(_function_3);
    };
    InitializingListListener<XLabel> _doubleArrow = ObjectExtensions.<InitializingListListener<XLabel>>operator_doubleArrow(_initializingListListener, _function_2);
    CoreExtensions.<XLabel>addInitializingListener(_labels, _doubleArrow);
    Node _node = this.getNode();
    if ((_node instanceof XActivatable)) {
      Node _node_1 = this.getNode();
      ((XActivatable) _node_1).activate();
    }
  }
  
  @Override
  public void selectionFeedback(final boolean isSelected) {
    if (isSelected) {
      this.setEffect(this.selectionEffect);
      DoubleProperty _scaleXProperty = this.scaleXProperty();
      boolean _isBound = _scaleXProperty.isBound();
      boolean _not = (!_isBound);
      if (_not) {
        this.setScaleX(1.05);
        this.setScaleY(1.05);
      }
      this.toFront();
    } else {
      this.setEffect(null);
      DoubleProperty _scaleXProperty_1 = this.scaleXProperty();
      boolean _isBound_1 = _scaleXProperty_1.isBound();
      boolean _not_1 = (!_isBound_1);
      if (_not_1) {
        this.setScaleX(1.0);
        this.setScaleY(1.0);
      }
    }
  }
  
  @Override
  public void toFront() {
    super.toFront();
    ObservableList<XConnection> _outgoingConnections = this.getOutgoingConnections();
    ObservableList<XConnection> _incomingConnections = this.getIncomingConnections();
    Iterable<XConnection> _plus = Iterables.<XConnection>concat(_outgoingConnections, _incomingConnections);
    final Consumer<XConnection> _function = (XConnection it) -> {
      it.toFront();
    };
    _plus.forEach(_function);
  }
  
  @Override
  public Bounds getSnapBounds() {
    Node _node = this.getNode();
    Bounds _boundsInParent = _node.getBoundsInParent();
    double _layoutX = this.placementGroup.getLayoutX();
    double _layoutY = this.placementGroup.getLayoutY();
    BoundingBox _translate = BoundsExtensions.translate(_boundsInParent, _layoutX, _layoutY);
    double _scaleX = this.getScaleX();
    double _divide = (1 / _scaleX);
    double _scaleY = this.getScaleY();
    double _divide_1 = (1 / _scaleY);
    return BoundsExtensions.scale(_translate, _divide, _divide_1);
  }
  
  public Anchors getAnchors() {
    Anchors _xblockexpression = null;
    {
      this.getNode();
      _xblockexpression = this.anchors;
    }
    return _xblockexpression;
  }
  
  @Override
  public double minWidth(final double height) {
    double _xifexpression = (double) 0;
    boolean _notEquals = (!Objects.equal(this.widthProperty, null));
    if (_notEquals) {
      _xifexpression = this.widthProperty.get();
    } else {
      _xifexpression = super.minWidth(height);
    }
    return _xifexpression;
  }
  
  @Override
  public double minHeight(final double width) {
    double _xifexpression = (double) 0;
    boolean _notEquals = (!Objects.equal(this.heightProperty, null));
    if (_notEquals) {
      _xifexpression = this.heightProperty.get();
    } else {
      _xifexpression = super.minHeight(width);
    }
    return _xifexpression;
  }
  
  @Override
  public double prefWidth(final double height) {
    double _xifexpression = (double) 0;
    boolean _notEquals = (!Objects.equal(this.widthProperty, null));
    if (_notEquals) {
      _xifexpression = this.widthProperty.get();
    } else {
      _xifexpression = super.prefWidth(height);
    }
    return _xifexpression;
  }
  
  @Override
  public double prefHeight(final double width) {
    double _xifexpression = (double) 0;
    boolean _notEquals = (!Objects.equal(this.heightProperty, null));
    if (_notEquals) {
      _xifexpression = this.heightProperty.get();
    } else {
      _xifexpression = super.prefHeight(width);
    }
    return _xifexpression;
  }
  
  @Override
  public double maxWidth(final double height) {
    double _xifexpression = (double) 0;
    boolean _notEquals = (!Objects.equal(this.widthProperty, null));
    if (_notEquals) {
      _xifexpression = this.widthProperty.get();
    } else {
      _xifexpression = super.maxWidth(height);
    }
    return _xifexpression;
  }
  
  @Override
  public double maxHeight(final double width) {
    double _xifexpression = (double) 0;
    boolean _notEquals = (!Objects.equal(this.heightProperty, null));
    if (_notEquals) {
      _xifexpression = this.heightProperty.get();
    } else {
      _xifexpression = super.maxHeight(width);
    }
    return _xifexpression;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XNode");
    ;
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public XNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(layoutXProperty(), Double.class);
    modelElement.addProperty(layoutYProperty(), Double.class);
    modelElement.addProperty(widthProperty, Double.class);
    modelElement.addProperty(heightProperty, Double.class);
    modelElement.addProperty(labelsProperty, XLabel.class);
    modelElement.addProperty(placementHintProperty, Side.class);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private SimpleDoubleProperty widthProperty = new SimpleDoubleProperty(this, "width");
  
  public double getWidth() {
    return this.widthProperty.get();
  }
  
  public void setWidth(final double width) {
    this.widthProperty.set(width);
  }
  
  public DoubleProperty widthProperty() {
    return this.widthProperty;
  }
  
  private SimpleDoubleProperty heightProperty = new SimpleDoubleProperty(this, "height");
  
  public double getHeight() {
    return this.heightProperty.get();
  }
  
  public void setHeight(final double height) {
    this.heightProperty.set(height);
  }
  
  public DoubleProperty heightProperty() {
    return this.heightProperty;
  }
  
  private SimpleListProperty<XConnection> incomingConnectionsProperty = new SimpleListProperty<XConnection>(this, "incomingConnections",_initIncomingConnections());
  
  private static final ObservableList<XConnection> _initIncomingConnections() {
    ObservableList<XConnection> _observableArrayList = FXCollections.<XConnection>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XConnection> getIncomingConnections() {
    return this.incomingConnectionsProperty.get();
  }
  
  public ListProperty<XConnection> incomingConnectionsProperty() {
    return this.incomingConnectionsProperty;
  }
  
  private SimpleListProperty<XConnection> outgoingConnectionsProperty = new SimpleListProperty<XConnection>(this, "outgoingConnections",_initOutgoingConnections());
  
  private static final ObservableList<XConnection> _initOutgoingConnections() {
    ObservableList<XConnection> _observableArrayList = FXCollections.<XConnection>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XConnection> getOutgoingConnections() {
    return this.outgoingConnectionsProperty.get();
  }
  
  public ListProperty<XConnection> outgoingConnectionsProperty() {
    return this.outgoingConnectionsProperty;
  }
  
  private SimpleListProperty<XLabel> labelsProperty = new SimpleListProperty<XLabel>(this, "labels",_initLabels());
  
  private static final ObservableList<XLabel> _initLabels() {
    ObservableList<XLabel> _observableArrayList = FXCollections.<XLabel>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XLabel> getLabels() {
    return this.labelsProperty.get();
  }
  
  public ListProperty<XLabel> labelsProperty() {
    return this.labelsProperty;
  }
  
  private SimpleObjectProperty<Side> placementHintProperty = new SimpleObjectProperty<Side>(this, "placementHint",_initPlacementHint());
  
  private static final Side _initPlacementHint() {
    return Side.BOTTOM;
  }
  
  public Side getPlacementHint() {
    return this.placementHintProperty.get();
  }
  
  public void setPlacementHint(final Side placementHint) {
    this.placementHintProperty.set(placementHint);
  }
  
  public ObjectProperty<Side> placementHintProperty() {
    return this.placementHintProperty;
  }
  
  @Pure
  public Group getPlacementGroup() {
    return this.placementGroup;
  }
}
