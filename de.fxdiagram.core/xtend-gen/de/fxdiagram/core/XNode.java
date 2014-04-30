package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.anchors.RectangleAnchors;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.StringDescriptor;
import de.fxdiagram.core.model.XModelProvider;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@ModelNode({ "layoutX", "layoutY", "domainObject", "width", "height" })
@SuppressWarnings("all")
public class XNode extends XShape implements XModelProvider {
  private Effect mouseOverEffect;
  
  private Effect selectionEffect;
  
  private Effect originalEffect;
  
  private Anchors anchors;
  
  public XNode(final DomainObjectDescriptor domainObject) {
    this.domainObjectProperty.set(domainObject);
  }
  
  public XNode(final String name) {
    this(new StringDescriptor(name));
  }
  
  public String getName() {
    String _xblockexpression = null;
    {
      DomainObjectDescriptor _domainObject = this.getDomainObject();
      String _name = null;
      if (_domainObject!=null) {
        _name=_domainObject.getName();
      }
      final String name = _name;
      boolean _equals = Objects.equal(name, null);
      if (_equals) {
        XNode.LOG.severe("XNodes key is null");
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
    final Procedure1<DropShadow> _function = new Procedure1<DropShadow>() {
      public void apply(final DropShadow it) {
        it.setOffsetX(4.0);
        it.setOffsetY(4.0);
      }
    };
    return ObjectExtensions.<DropShadow>operator_doubleArrow(_dropShadow, _function);
  }
  
  protected Anchors createAnchors() {
    return new RectangleAnchors(this);
  }
  
  public void initializeGraphics() {
    super.initializeGraphics();
    InnerShadow _createMouseOverEffect = this.createMouseOverEffect();
    this.mouseOverEffect = _createMouseOverEffect;
    DropShadow _createSelectionEffect = this.createSelectionEffect();
    this.selectionEffect = _createSelectionEffect;
    Anchors _createAnchors = this.createAnchors();
    this.anchors = _createAnchors;
  }
  
  protected Node createNode() {
    return null;
  }
  
  public void doActivate() {
    MoveBehavior<XNode> _moveBehavior = new MoveBehavior<XNode>(this);
    this.addBehavior(_moveBehavior);
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        Node _node = XNode.this.getNode();
        Effect _effect = _node.getEffect();
        XNode.this.originalEffect = _effect;
        Node _node_1 = XNode.this.getNode();
        Effect _elvis = null;
        if (XNode.this.mouseOverEffect != null) {
          _elvis = XNode.this.mouseOverEffect;
        } else {
          _elvis = XNode.this.originalEffect;
        }
        _node_1.setEffect(_elvis);
      }
    };
    this.setOnMouseEntered(_function);
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        Node _node = XNode.this.getNode();
        _node.setEffect(XNode.this.originalEffect);
      }
    };
    this.setOnMouseExited(_function_1);
    Node _node = this.getNode();
    if ((_node instanceof XActivatable)) {
      Node _node_1 = this.getNode();
      ((XActivatable) _node_1).activate();
    }
  }
  
  public void selectionFeedback(final boolean isSelected) {
    if (isSelected) {
      this.setEffect(this.selectionEffect);
      this.setScaleX(1.05);
      this.setScaleY(1.05);
      ObservableList<XConnection> _outgoingConnections = this.getOutgoingConnections();
      ObservableList<XConnection> _incomingConnections = this.getIncomingConnections();
      Iterable<XConnection> _plus = Iterables.<XConnection>concat(_outgoingConnections, _incomingConnections);
      final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
        public void apply(final XConnection it) {
          it.toFront();
        }
      };
      IterableExtensions.<XConnection>forEach(_plus, _function);
    } else {
      this.setEffect(null);
      this.setScaleX(1.0);
      this.setScaleY(1.0);
    }
  }
  
  public Bounds getSnapBounds() {
    Node _node = this.getNode();
    Bounds _boundsInParent = _node.getBoundsInParent();
    double _scaleX = this.getScaleX();
    double _divide = (1 / _scaleX);
    double _scaleY = this.getScaleY();
    double _divide_1 = (1 / _scaleY);
    return BoundsExtensions.scale(_boundsInParent, _divide, _divide_1);
  }
  
  public Anchors getAnchors() {
    return this.anchors;
  }
  
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
  
  public String toString() {
    Class<? extends XNode> _class = this.getClass();
    String _name = _class.getName();
    String _plus = (_name + " (");
    String _name_1 = this.getName();
    String _plus_1 = (_plus + _name_1);
    return (_plus_1 + ")");
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XNode");
    ;
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public XNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(layoutXProperty(), Double.class);
    modelElement.addProperty(layoutYProperty(), Double.class);
    modelElement.addProperty(domainObjectProperty, DomainObjectDescriptor.class);
    modelElement.addProperty(widthProperty, Double.class);
    modelElement.addProperty(heightProperty, Double.class);
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
  
  private ReadOnlyObjectWrapper<DomainObjectDescriptor> domainObjectProperty = new ReadOnlyObjectWrapper<DomainObjectDescriptor>(this, "domainObject");
  
  public DomainObjectDescriptor getDomainObject() {
    return this.domainObjectProperty.get();
  }
  
  public ReadOnlyObjectProperty<DomainObjectDescriptor> domainObjectProperty() {
    return this.domainObjectProperty.getReadOnlyProperty();
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
}
