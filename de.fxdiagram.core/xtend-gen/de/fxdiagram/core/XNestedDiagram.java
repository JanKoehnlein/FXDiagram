package de.fxdiagram.core;

import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.transform.BoundsExtensions;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * A nested diagram needs a width and a height to be set in order to rescale itself.
 */
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
  
  private ChangeListener<Bounds> boundsInLocalListener;
  
  private ChangeListener<Number> layoutListener;
  
  public XNestedDiagram() {
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.buttonLayer);
    BooleanProperty _visibleProperty = this.visibleProperty();
    final ChangeListener<Boolean> _function = new ChangeListener<Boolean>() {
        public void changed(final ObservableValue<? extends Boolean> property, final Boolean oldVal, final Boolean newVal) {
          List<XConnection> _connections = XNestedDiagram.this.getConnections();
          final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
              public void apply(final XConnection it) {
                it.setVisible((newVal).booleanValue());
              }
            };
          IterableExtensions.<XConnection>forEach(_connections, _function);
        }
      };
    _visibleProperty.addListener(_function);
    final ChangeListener<Bounds> _function_1 = new ChangeListener<Bounds>() {
        public void changed(final ObservableValue<? extends Bounds> prop, final Bounds oldVal, final Bounds newVal) {
          XNestedDiagram.this.scaleToFit();
        }
      };
    this.boundsInLocalListener = _function_1;
    final ChangeListener<Number> _function_2 = new ChangeListener<Number>() {
        public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
          XNestedDiagram.this.scaleToFit();
        }
      };
    this.layoutListener = _function_2;
    final ChangeListener<Number> _function_3 = new ChangeListener<Number>() {
        public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
          XNestedDiagram.this.scaleToFit();
        }
      };
    this.widthProperty.addListener(_function_3);
    final ChangeListener<Number> _function_4 = new ChangeListener<Number>() {
        public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
          XNestedDiagram.this.scaleToFit();
        }
      };
    this.heightProperty.addListener(_function_4);
  }
  
  public void setScale(final double scale) {
    this.setScaleX(scale);
    this.setScaleY(scale);
  }
  
  public void scaleToFit() {
    List<XNode> _nodes = this.getNodes();
    boolean _isEmpty = _nodes.isEmpty();
    if (_isEmpty) {
      this.setScale(1);
      this.setClip(null);
    } else {
      List<XNode> _nodes_1 = this.getNodes();
      final Function1<XNode,BoundingBox> _function = new Function1<XNode,BoundingBox>() {
          public BoundingBox apply(final XNode it) {
            Bounds _layoutBounds = it.getLayoutBounds();
            double _layoutX = it.getLayoutX();
            double _layoutY = it.getLayoutY();
            BoundingBox _translate = BoundsExtensions.translate(_layoutBounds, _layoutX, _layoutY);
            return _translate;
          }
        };
      List<BoundingBox> _map = ListExtensions.<XNode, BoundingBox>map(_nodes_1, _function);
      final Function2<BoundingBox,BoundingBox,BoundingBox> _function_1 = new Function2<BoundingBox,BoundingBox,BoundingBox>() {
          public BoundingBox apply(final BoundingBox b0, final BoundingBox b1) {
            BoundingBox _plus = BoundsExtensions.operator_plus(b0, b1);
            return _plus;
          }
        };
      final BoundingBox myBounds = IterableExtensions.<BoundingBox>reduce(_map, _function_1);
      double _xifexpression = (double) 0;
      double _width = myBounds.getWidth();
      boolean _notEquals = (_width != 0);
      if (_notEquals) {
        double _width_1 = this.getWidth();
        double _width_2 = myBounds.getWidth();
        double _divide = (_width_1 / _width_2);
        double _min = Math.min(1, _divide);
        _xifexpression = _min;
      } else {
        _xifexpression = 1;
      }
      final double newScaleX = _xifexpression;
      double _xifexpression_1 = (double) 0;
      double _height = myBounds.getHeight();
      boolean _notEquals_1 = (_height != 0);
      if (_notEquals_1) {
        double _height_1 = this.getHeight();
        double _height_2 = myBounds.getHeight();
        double _divide_1 = (_height_1 / _height_2);
        double _min_1 = Math.min(1, _divide_1);
        _xifexpression_1 = _min_1;
      } else {
        _xifexpression_1 = 1;
      }
      final double newScaleY = _xifexpression_1;
      final double newScale = Math.min(newScaleX, newScaleY);
      this.setScale(newScale);
      Rectangle _rectangle = new Rectangle();
      final Procedure1<Rectangle> _function_2 = new Procedure1<Rectangle>() {
          public void apply(final Rectangle it) {
            double _minX = myBounds.getMinX();
            it.setX(_minX);
            double _width = myBounds.getWidth();
            it.setWidth(_width);
            boolean _equals = (newScaleX == 1);
            if (_equals) {
              double _width_1 = it.getWidth();
              double _divide = (_width_1 / newScale);
              double _width_2 = myBounds.getWidth();
              final double delta = (_divide - _width_2);
              double _x = it.getX();
              double _multiply = (0.5 * delta);
              double _minus = (_x - _multiply);
              it.setX(_minus);
              double _width_3 = it.getWidth();
              double _plus = (_width_3 + delta);
              it.setWidth(_plus);
            }
            double _minY = myBounds.getMinY();
            it.setY(_minY);
            double _height = myBounds.getHeight();
            it.setHeight(_height);
            boolean _equals_1 = (newScaleY == 1);
            if (_equals_1) {
              double _height_1 = it.getHeight();
              double _divide_1 = (_height_1 / newScale);
              double _height_2 = myBounds.getHeight();
              final double delta_1 = (_divide_1 - _height_2);
              double _y = it.getY();
              double _multiply_1 = (0.5 * delta_1);
              double _minus_1 = (_y - _multiply_1);
              it.setY(_minus_1);
              double _height_3 = it.getHeight();
              double _plus_1 = (_height_3 + delta_1);
              it.setHeight(_plus_1);
            }
          }
        };
      Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_2);
      this.setClip(_doubleArrow);
    }
  }
  
  public Procedure1<? super XNestedDiagram> setContentsInitializer(final Procedure1<? super XNestedDiagram> contentsInitializer) {
    Procedure1<? super XNestedDiagram> _contentsInitializer = this.contentsInitializer = contentsInitializer;
    return _contentsInitializer;
  }
  
  public void doActivate() {
    super.doActivate();
    if (this.contentsInitializer!=null) {
      this.contentsInitializer.apply(this);
    }
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
      ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = node.boundsInLocalProperty();
      _boundsInLocalProperty.addListener(this.boundsInLocalListener);
      DoubleProperty _layoutXProperty = node.layoutXProperty();
      _layoutXProperty.addListener(this.layoutListener);
      DoubleProperty _layoutYProperty = node.layoutYProperty();
      _layoutYProperty.addListener(this.layoutListener);
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
  
  public boolean internalRemoveNode(final XNode node) {
    boolean _xblockexpression = false;
    {
      super.internalRemoveNode(node);
      ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = node.boundsInLocalProperty();
      _boundsInLocalProperty.removeListener(this.boundsInLocalListener);
      DoubleProperty _layoutXProperty = node.layoutXProperty();
      _layoutXProperty.removeListener(this.layoutListener);
      DoubleProperty _layoutYProperty = node.layoutYProperty();
      _layoutYProperty.removeListener(this.layoutListener);
      XAbstractDiagram _parentDiagram = this.getParentDiagram();
      boolean _internalRemoveNode = _parentDiagram.internalRemoveNode(node);
      _xblockexpression = (_internalRemoveNode);
    }
    return _xblockexpression;
  }
  
  public boolean internalRemoveButton(final XRapidButton button) {
    boolean _xblockexpression = false;
    {
      super.internalRemoveButton(button);
      XAbstractDiagram _parentDiagram = this.getParentDiagram();
      boolean _internalRemoveButton = _parentDiagram.internalRemoveButton(button);
      _xblockexpression = (_internalRemoveButton);
    }
    return _xblockexpression;
  }
  
  protected XAbstractDiagram getParentDiagram() {
    XAbstractDiagram _diagram = null;
    Parent _parent = this.getParent();
    if (_parent!=null) {
      _diagram=Extensions.getDiagram(_parent);
    }
    return _diagram;
  }
  
  private SimpleDoubleProperty widthProperty = new SimpleDoubleProperty(this, "width",_initWidth());
  
  private static final double _initWidth() {
    return 80;
  }
  
  public double getWidth() {
    return this.widthProperty.get();
    
  }
  
  public void setWidth(final double width) {
    this.widthProperty.set(width);
    
  }
  
  public DoubleProperty widthProperty() {
    return this.widthProperty;
    
  }
  
  private SimpleDoubleProperty heightProperty = new SimpleDoubleProperty(this, "height",_initHeight());
  
  private static final double _initHeight() {
    return 60;
  }
  
  public double getHeight() {
    return this.heightProperty.get();
    
  }
  
  public void setHeight(final double height) {
    this.heightProperty.set(height);
    
  }
  
  public DoubleProperty heightProperty() {
    return this.heightProperty;
    
  }
}
