package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.annotations.properties.FxProperty;
import de.fxdiagram.annotations.properties.Lazy;
import de.fxdiagram.core.ConnectionRouter;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.binding.DoubleExpressionExtensions;
import java.util.Collections;
import java.util.List;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XConnection extends XShape {
  @FxProperty
  @Lazy
  private XConnectionLabel label;
  
  private Group controlPointGroup = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private Group shapeGroup = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private ChangeListener<Number> controlPointListener;
  
  private ConnectionRouter connectionRouter;
  
  private Polyline polyline = new Function0<Polyline>() {
    public Polyline apply() {
      Polyline _polyline = new Polyline();
      return _polyline;
    }
  }.apply();
  
  private QuadCurve quadCurve;
  
  private CubicCurve cubicCurve;
  
  public XConnection(final XNode source, final XNode target) {
    this.setNode(this.shapeGroup);
    ObservableList<Node> _children = this.getChildren();
    final Procedure1<Group> _function = new Procedure1<Group>() {
        public void apply(final Group it) {
          it.setVisible(false);
        }
      };
    Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(this.controlPointGroup, _function);
    _children.add(_doubleArrow);
    this.setSource(source);
    this.setTarget(target);
    final ChangeListener<Number> _function_1 = new ChangeListener<Number>() {
        public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
          XConnection.this.updatePoints();
        }
      };
    this.controlPointListener = _function_1;
    ConnectionRouter _connectionRouter = new ConnectionRouter(this);
    this.connectionRouter = _connectionRouter;
  }
  
  public void doActivate() {
    this.setShape(this.polyline);
    ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
    final ListChangeListener<XControlPoint> _function = new ListChangeListener<XControlPoint>() {
        public void onChanged(final Change<? extends XControlPoint> it) {
          final ObservableList<? extends XControlPoint> points = it.getList();
          XConnection.this.updatePoints();
          boolean _next = it.next();
          boolean _while = _next;
          while (_while) {
            List<? extends XControlPoint> _addedSubList = it.getAddedSubList();
            final Procedure1<XControlPoint> _function = new Procedure1<XControlPoint>() {
                public void apply(final XControlPoint it) {
                  final int index = points.indexOf(it);
                  boolean _and = false;
                  boolean _notEquals = (index != 0);
                  if (!_notEquals) {
                    _and = false;
                  } else {
                    int _size = points.size();
                    boolean _notEquals_1 = (index != _size);
                    _and = (_notEquals && _notEquals_1);
                  }
                  if (_and) {
                    it.activate();
                  }
                  DoubleProperty _layoutXProperty = it.layoutXProperty();
                  _layoutXProperty.addListener(XConnection.this.controlPointListener);
                  DoubleProperty _layoutYProperty = it.layoutYProperty();
                  _layoutYProperty.addListener(XConnection.this.controlPointListener);
                }
              };
            IterableExtensions.forEach(_addedSubList, _function);
            boolean _next_1 = it.next();
            _while = _next_1;
          }
          List<? extends XControlPoint> _removed = it.getRemoved();
          final Procedure1<XControlPoint> _function = new Procedure1<XControlPoint>() {
              public void apply(final XControlPoint it) {
                DoubleProperty _layoutXProperty = it.layoutXProperty();
                _layoutXProperty.removeListener(XConnection.this.controlPointListener);
                DoubleProperty _layoutYProperty = it.layoutYProperty();
                _layoutYProperty.removeListener(XConnection.this.controlPointListener);
              }
            };
          IterableExtensions.forEach(_removed, _function);
        }
      };
    _controlPoints.addListener(_function);
    boolean _notEquals = (!Objects.equal(this.label, null));
    if (_notEquals) {
      this.label.activate();
    }
    BooleanProperty _selectedProperty = this.selectedProperty();
    final ChangeListener<Boolean> _function_1 = new ChangeListener<Boolean>() {
        public void changed(final ObservableValue<? extends Boolean> prop, final Boolean oldVal, final Boolean newVal) {
          XConnection.this.controlPointGroup.setVisible((newVal).booleanValue());
          ObservableList<XControlPoint> _controlPoints = XConnection.this.getControlPoints();
          int _size = _controlPoints.size();
          int _minus = (_size - 1);
          ExclusiveRange _doubleDotLessThan = new ExclusiveRange(1, _minus, true);
          for (final Integer i : _doubleDotLessThan) {
            ObservableList<XControlPoint> _controlPoints_1 = XConnection.this.getControlPoints();
            XControlPoint _get = _controlPoints_1.get((i).intValue());
            _get.setSelected((newVal).booleanValue());
          }
        }
      };
    _selectedProperty.addListener(_function_1);
    this.connectionRouter.activate();
  }
  
  public ConnectionRouter getConnectionRouter() {
    return this.connectionRouter;
  }
  
  public ObservableList<XControlPoint> getControlPoints() {
    ObservableList<XControlPoint> _controlPoints = this.connectionRouter.getControlPoints();
    return _controlPoints;
  }
  
  public boolean updatePoints() {
    boolean _xblockexpression = false;
    {
      ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
      int _size = _controlPoints.size();
      final int _switchValue = _size;
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(_switchValue,3)) {
          _matched=true;
          boolean _equals = Objects.equal(this.quadCurve, null);
          if (_equals) {
            QuadCurve _quadCurve = new QuadCurve();
            this.quadCurve = _quadCurve;
          }
          ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
          XControlPoint _get = _controlPoints_1.get(0);
          double _layoutX = _get.getLayoutX();
          this.quadCurve.setStartX(_layoutX);
          ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
          XControlPoint _get_1 = _controlPoints_2.get(0);
          double _layoutY = _get_1.getLayoutY();
          this.quadCurve.setStartY(_layoutY);
          ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
          XControlPoint _get_2 = _controlPoints_3.get(1);
          double _layoutX_1 = _get_2.getLayoutX();
          this.quadCurve.setControlX(_layoutX_1);
          ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
          XControlPoint _get_3 = _controlPoints_4.get(1);
          double _layoutY_1 = _get_3.getLayoutY();
          this.quadCurve.setControlY(_layoutY_1);
          ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
          XControlPoint _get_4 = _controlPoints_5.get(2);
          double _layoutX_2 = _get_4.getLayoutX();
          this.quadCurve.setEndX(_layoutX_2);
          ObservableList<XControlPoint> _controlPoints_6 = this.getControlPoints();
          XControlPoint _get_5 = _controlPoints_6.get(2);
          double _layoutY_2 = _get_5.getLayoutY();
          this.quadCurve.setEndY(_layoutY_2);
          this.setShape(this.quadCurve);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,4)) {
          _matched=true;
          boolean _equals_1 = Objects.equal(this.cubicCurve, null);
          if (_equals_1) {
            CubicCurve _cubicCurve = new CubicCurve();
            this.cubicCurve = _cubicCurve;
          }
          ObservableList<XControlPoint> _controlPoints_7 = this.getControlPoints();
          XControlPoint _get_6 = _controlPoints_7.get(0);
          double _layoutX_3 = _get_6.getLayoutX();
          this.cubicCurve.setStartX(_layoutX_3);
          ObservableList<XControlPoint> _controlPoints_8 = this.getControlPoints();
          XControlPoint _get_7 = _controlPoints_8.get(0);
          double _layoutY_3 = _get_7.getLayoutY();
          this.cubicCurve.setStartY(_layoutY_3);
          ObservableList<XControlPoint> _controlPoints_9 = this.getControlPoints();
          XControlPoint _get_8 = _controlPoints_9.get(1);
          double _layoutX_4 = _get_8.getLayoutX();
          this.cubicCurve.setControlX1(_layoutX_4);
          ObservableList<XControlPoint> _controlPoints_10 = this.getControlPoints();
          XControlPoint _get_9 = _controlPoints_10.get(1);
          double _layoutY_4 = _get_9.getLayoutY();
          this.cubicCurve.setControlY1(_layoutY_4);
          ObservableList<XControlPoint> _controlPoints_11 = this.getControlPoints();
          XControlPoint _get_10 = _controlPoints_11.get(2);
          double _layoutX_5 = _get_10.getLayoutX();
          this.cubicCurve.setControlX2(_layoutX_5);
          ObservableList<XControlPoint> _controlPoints_12 = this.getControlPoints();
          XControlPoint _get_11 = _controlPoints_12.get(2);
          double _layoutY_5 = _get_11.getLayoutY();
          this.cubicCurve.setControlY2(_layoutY_5);
          ObservableList<XControlPoint> _controlPoints_13 = this.getControlPoints();
          XControlPoint _get_12 = _controlPoints_13.get(3);
          double _layoutX_6 = _get_12.getLayoutX();
          this.cubicCurve.setEndX(_layoutX_6);
          ObservableList<XControlPoint> _controlPoints_14 = this.getControlPoints();
          XControlPoint _get_13 = _controlPoints_14.get(3);
          double _layoutY_6 = _get_13.getLayoutY();
          this.cubicCurve.setEndY(_layoutY_6);
          this.setShape(this.cubicCurve);
        }
      }
      if (!_matched) {
        {
          ObservableList<Double> _points = this.polyline.getPoints();
          ObservableList<XControlPoint> _controlPoints_15 = this.getControlPoints();
          final Function1<XControlPoint,List<Double>> _function = new Function1<XControlPoint,List<Double>>() {
              public List<Double> apply(final XControlPoint it) {
                double _layoutX = it.getLayoutX();
                double _layoutY = it.getLayoutY();
                return Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(_layoutX, _layoutY));
              }
            };
          List<List<Double>> _map = ListExtensions.<XControlPoint, List<Double>>map(_controlPoints_15, _function);
          Iterable<Double> _flatten = Iterables.<Double>concat(_map);
          _points.setAll(((Double[])Conversions.unwrapArray(_flatten, Double.class)));
          ObservableList<Node> _children = this.shapeGroup.getChildren();
          _children.setAll(this.polyline);
        }
      }
      ObservableList<Node> _children = this.controlPointGroup.getChildren();
      ObservableList<XControlPoint> _controlPoints_15 = this.connectionRouter.getControlPoints();
      boolean _setAll = _children.setAll(_controlPoints_15);
      _xblockexpression = (_setAll);
    }
    return _xblockexpression;
  }
  
  protected Shape setShape(final Shape shape) {
    Shape _xblockexpression = null;
    {
      ObservableList<Node> _children = this.shapeGroup.getChildren();
      _children.setAll(shape);
      DoubleProperty _strokeWidthProperty = shape.strokeWidthProperty();
      XRootDiagram _rootDiagram = Extensions.getRootDiagram(this);
      DoubleProperty _scaleProperty = _rootDiagram.scaleProperty();
      DoubleBinding _divide = DoubleExpressionExtensions.operator_divide(this.strokeWidthProperty, _scaleProperty);
      _strokeWidthProperty.bind(_divide);
      final Procedure1<Shape> _function = new Procedure1<Shape>() {
          public void apply(final Shape it) {
            it.setFill(null);
            it.setStroke(Color.BLACK);
          }
        };
      Shape _doubleArrow = ObjectExtensions.<Shape>operator_doubleArrow(shape, _function);
      _xblockexpression = (_doubleArrow);
    }
    return _xblockexpression;
  }
  
  public boolean isSelectable() {
    boolean _isActive = this.getIsActive();
    return _isActive;
  }
  
  public Polyline getPolyline() {
    return this.polyline;
  }
  
  public MoveBehavior getMoveBehavior() {
    return null;
  }
  
  public void layoutChildren() {
    super.layoutChildren();
    this.connectionRouter.calculatePoints();
  }
  
  private SimpleObjectProperty<XNode> sourceProperty = new SimpleObjectProperty<XNode>(this, "source");
  
  public XNode getSource() {
    return this.sourceProperty.get();
    
  }
  
  public void setSource(final XNode source) {
    this.sourceProperty.set(source);
    
  }
  
  public ObjectProperty<XNode> sourceProperty() {
    return this.sourceProperty;
    
  }
  
  private SimpleObjectProperty<XNode> targetProperty = new SimpleObjectProperty<XNode>(this, "target");
  
  public XNode getTarget() {
    return this.targetProperty.get();
    
  }
  
  public void setTarget(final XNode target) {
    this.targetProperty.set(target);
    
  }
  
  public ObjectProperty<XNode> targetProperty() {
    return this.targetProperty;
    
  }
  
  private SimpleObjectProperty<XConnectionLabel> labelProperty;
  
  public XConnectionLabel getLabel() {
    return (this.labelProperty != null)? this.labelProperty.get() : this.label;
    
  }
  
  public void setLabel(final XConnectionLabel label) {
    if (labelProperty != null) {
    	this.labelProperty.set(label);
    } else {
    	this.label = label;
    }
    
  }
  
  public ObjectProperty<XConnectionLabel> labelProperty() {
    if (this.labelProperty == null) { 
    	this.labelProperty = new SimpleObjectProperty<XConnectionLabel>(this, "label", this.label);
    }
    return this.labelProperty;
    
  }
  
  private SimpleDoubleProperty strokeWidthProperty = new SimpleDoubleProperty(this, "strokeWidth",_initStrokeWidth());
  
  private static final double _initStrokeWidth() {
    return 2;
  }
  
  public double getStrokeWidth() {
    return this.strokeWidthProperty.get();
    
  }
  
  public void setStrokeWidth(final double strokeWidth) {
    this.strokeWidthProperty.set(strokeWidth);
    
  }
  
  public DoubleProperty strokeWidthProperty() {
    return this.strokeWidthProperty;
    
  }
}
