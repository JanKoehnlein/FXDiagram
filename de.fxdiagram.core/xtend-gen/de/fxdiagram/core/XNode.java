package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.core.AnchorPoints;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.behavior.SelectionBehavior;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;

@SuppressWarnings("all")
public class XNode extends Parent implements XActivatable {
  private static int instanceCount;
  
  private Node node;
  
  private Effect mouseOverEffect;
  
  private Effect originalEffect;
  
  private SelectionBehavior selectionBehavior;
  
  private MoveBehavior moveBehavior;
  
  private AnchorPoints anchorPoints;
  
  public XNode() {
    InnerShadow _createMouseOverEffect = this.createMouseOverEffect();
    this.mouseOverEffect = _createMouseOverEffect;
    Class<? extends XNode> _class = this.getClass();
    String _simpleName = _class.getSimpleName();
    String _plus = (_simpleName + Integer.valueOf(XNode.instanceCount));
    this.setKey(_plus);
    int _plus_1 = (XNode.instanceCount + 1);
    XNode.instanceCount = _plus_1;
  }
  
  protected InnerShadow createMouseOverEffect() {
    InnerShadow _innerShadow = new InnerShadow();
    return _innerShadow;
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActiveProperty.set(true);
  }
  
  public void doActivate() {
    SelectionBehavior _selectionBehavior = new SelectionBehavior(this);
    this.selectionBehavior = _selectionBehavior;
    MoveBehavior _moveBehavior = new MoveBehavior(this);
    this.moveBehavior = _moveBehavior;
    AnchorPoints _anchorPoints = new AnchorPoints(this);
    this.anchorPoints = _anchorPoints;
    this.selectionBehavior.activate();
    this.moveBehavior.activate();
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent it) {
          Effect _effect = XNode.this.node.getEffect();
          XNode.this.originalEffect = _effect;
          Effect _elvis = null;
          if (XNode.this.mouseOverEffect != null) {
            _elvis = XNode.this.mouseOverEffect;
          } else {
            _elvis = ObjectExtensions.<Effect>operator_elvis(XNode.this.mouseOverEffect, XNode.this.originalEffect);
          }
          XNode.this.node.setEffect(_elvis);
        }
      };
    this.setOnMouseEntered(_function);
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent it) {
          XNode.this.node.setEffect(XNode.this.originalEffect);
        }
      };
    this.setOnMouseExited(_function_1);
    final Node node = this.node;
    boolean _matched = false;
    if (!_matched) {
      if (node instanceof XActivatable) {
        final XActivatable _xActivatable = (XActivatable)node;
        _matched=true;
        ((XActivatable)_xActivatable).activate();
      }
    }
  }
  
  public Node getNode() {
    return this.node;
  }
  
  public void setNode(final Node node) {
    this.node = node;
    ObservableList<Node> _children = this.getChildren();
    _children.add(node);
  }
  
  protected void setKey(final String key) {
    this.keyProperty.set(key);
  }
  
  public SelectionBehavior getSelectionBehavior() {
    return this.selectionBehavior;
  }
  
  public MoveBehavior getMoveBehavior() {
    return this.moveBehavior;
  }
  
  public AnchorPoints getAnchorPoints() {
    return this.anchorPoints;
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
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
    
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
    
  }
  
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
}
