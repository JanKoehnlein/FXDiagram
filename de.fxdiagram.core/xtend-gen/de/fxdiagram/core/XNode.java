package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.anchors.RectangleAnchors;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.extensions.BoundsExtensions;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
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
@SuppressWarnings("all")
public class XNode extends XShape {
  private Effect mouseOverEffect;
  
  private Effect selectionEffect;
  
  private Effect originalEffect;
  
  private Anchors anchors;
  
  public XNode(final String key) {
    this.keyProperty.set(key);
    InnerShadow _createMouseOverEffect = this.createMouseOverEffect();
    this.mouseOverEffect = _createMouseOverEffect;
    DropShadow _createSelectionEffect = this.createSelectionEffect();
    this.selectionEffect = _createSelectionEffect;
    Anchors _createAnchors = this.createAnchors();
    this.anchors = _createAnchors;
  }
  
  protected InnerShadow createMouseOverEffect() {
    InnerShadow _innerShadow = new InnerShadow();
    return _innerShadow;
  }
  
  protected DropShadow createSelectionEffect() {
    DropShadow _dropShadow = new DropShadow();
    final Procedure1<DropShadow> _function = new Procedure1<DropShadow>() {
      public void apply(final DropShadow it) {
        it.setOffsetX(4.0);
        it.setOffsetY(4.0);
      }
    };
    DropShadow _doubleArrow = ObjectExtensions.<DropShadow>operator_doubleArrow(_dropShadow, _function);
    return _doubleArrow;
  }
  
  protected Anchors createAnchors() {
    RectangleAnchors _rectangleAnchors = new RectangleAnchors(this);
    return _rectangleAnchors;
  }
  
  public void doActivate() {
    String _key = this.getKey();
    boolean _equals = Objects.equal(_key, null);
    if (_equals) {
      XNode.LOG.warning("Node\'s key is not set");
    }
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
          _elvis = ObjectExtensions.<Effect>operator_elvis(XNode.this.mouseOverEffect, XNode.this.originalEffect);
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
    final Node n = _node;
    boolean _matched = false;
    if (!_matched) {
      if (n instanceof XActivatable) {
        _matched=true;
        ((XActivatable)n).activate();
      }
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
    BoundingBox _scale = BoundsExtensions.scale(_boundsInParent, _divide, _divide_1);
    return _scale;
  }
  
  public Anchors getAnchors() {
    return this.anchors;
  }
  
  public double minWidth(final double height) {
    double _xifexpression = (double) 0;
    boolean _notEquals = (!Objects.equal(this.widthProperty, null));
    if (_notEquals) {
      double _get = this.widthProperty.get();
      _xifexpression = _get;
    } else {
      double _minWidth = super.minWidth(height);
      _xifexpression = _minWidth;
    }
    return _xifexpression;
  }
  
  public double minHeight(final double width) {
    double _xifexpression = (double) 0;
    boolean _notEquals = (!Objects.equal(this.heightProperty, null));
    if (_notEquals) {
      double _get = this.heightProperty.get();
      _xifexpression = _get;
    } else {
      double _minHeight = super.minHeight(width);
      _xifexpression = _minHeight;
    }
    return _xifexpression;
  }
  
  public double prefWidth(final double height) {
    double _xifexpression = (double) 0;
    boolean _notEquals = (!Objects.equal(this.widthProperty, null));
    if (_notEquals) {
      double _get = this.widthProperty.get();
      _xifexpression = _get;
    } else {
      double _prefWidth = super.prefWidth(height);
      _xifexpression = _prefWidth;
    }
    return _xifexpression;
  }
  
  public double prefHeight(final double width) {
    double _xifexpression = (double) 0;
    boolean _notEquals = (!Objects.equal(this.heightProperty, null));
    if (_notEquals) {
      double _get = this.heightProperty.get();
      _xifexpression = _get;
    } else {
      double _prefHeight = super.prefHeight(width);
      _xifexpression = _prefHeight;
    }
    return _xifexpression;
  }
  
  public double maxWidth(final double height) {
    double _xifexpression = (double) 0;
    boolean _notEquals = (!Objects.equal(this.widthProperty, null));
    if (_notEquals) {
      double _get = this.widthProperty.get();
      _xifexpression = _get;
    } else {
      double _maxWidth = super.maxWidth(height);
      _xifexpression = _maxWidth;
    }
    return _xifexpression;
  }
  
  public double maxHeight(final double width) {
    double _xifexpression = (double) 0;
    boolean _notEquals = (!Objects.equal(this.heightProperty, null));
    if (_notEquals) {
      double _get = this.heightProperty.get();
      _xifexpression = _get;
    } else {
      double _maxHeight = super.maxHeight(width);
      _xifexpression = _maxHeight;
    }
    return _xifexpression;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XNode");
    ;
  
  private final static double DEFAULT_WIDTH = 0d;
  
  private SimpleDoubleProperty widthProperty;
  
  public double getWidth() {
    return (this.widthProperty != null)? this.widthProperty.get() : DEFAULT_WIDTH;
  }
  
  public void setWidth(final double width) {
    this.widthProperty().set(width);
  }
  
  public DoubleProperty widthProperty() {
    if (this.widthProperty == null) { 
    	this.widthProperty = new SimpleDoubleProperty(this, "width", DEFAULT_WIDTH);
    }
    return this.widthProperty;
  }
  
  private final static double DEFAULT_HEIGHT = 0d;
  
  private SimpleDoubleProperty heightProperty;
  
  public double getHeight() {
    return (this.heightProperty != null)? this.heightProperty.get() : DEFAULT_HEIGHT;
  }
  
  public void setHeight(final double height) {
    this.heightProperty().set(height);
  }
  
  public DoubleProperty heightProperty() {
    if (this.heightProperty == null) { 
    	this.heightProperty = new SimpleDoubleProperty(this, "height", DEFAULT_HEIGHT);
    }
    return this.heightProperty;
  }
  
  private ReadOnlyStringWrapper keyProperty = new ReadOnlyStringWrapper(this, "key");
  
  public String getKey() {
    return this.keyProperty.get();
  }
  
  public ReadOnlyStringProperty keyProperty() {
    return this.keyProperty.getReadOnlyProperty();
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
