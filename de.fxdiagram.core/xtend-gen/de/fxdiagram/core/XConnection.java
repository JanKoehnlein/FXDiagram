package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.FxProperty;
import de.fxdiagram.annotations.properties.Lazy;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XConnectionKind;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.binding.DoubleExpressionExtensions;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
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

@Logging
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
    ConnectionRouter _connectionRouter = new ConnectionRouter(this);
    this.connectionRouter = _connectionRouter;
  }
  
  public void doActivate() {
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
      public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
        XConnection.this.updateShapes();
      }
    };
    this.controlPointListener = _function;
    ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
    final ListChangeListener<XControlPoint> _function_1 = new ListChangeListener<XControlPoint>() {
      public void onChanged(final Change<? extends XControlPoint> it) {
        final ObservableList<? extends XControlPoint> points = it.getList();
        XConnection.this.updateShapes();
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
    _controlPoints.addListener(_function_1);
    boolean _notEquals = (!Objects.equal(this.label, null));
    if (_notEquals) {
      this.label.activate();
    }
    BooleanProperty _selectedProperty = this.selectedProperty();
    final ChangeListener<Boolean> _function_2 = new ChangeListener<Boolean>() {
      public void changed(final ObservableValue<? extends Boolean> prop, final Boolean oldVal, final Boolean newVal) {
        XConnection.this.controlPointGroup.setVisible((newVal).booleanValue());
      }
    };
    _selectedProperty.addListener(_function_2);
    this.connectionRouter.activate();
    this.updateShapes();
  }
  
  public ConnectionRouter getConnectionRouter() {
    return this.connectionRouter;
  }
  
  public ObservableList<XControlPoint> getControlPoints() {
    ObservableList<XControlPoint> _controlPoints = this.connectionRouter.getControlPoints();
    return _controlPoints;
  }
  
  public void updateShapes() {
    XConnectionKind _kind = this.getKind();
    final XConnectionKind getKind = _kind;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(getKind,XConnectionKind.CUBIC_CURVE)) {
        _matched=true;
        ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
        int _size = _controlPoints.size();
        int _minus = (_size - 1);
        int _modulo = (_minus % 3);
        boolean _notEquals = (_modulo != 0);
        if (_notEquals) {
          this.setKind(XConnectionKind.POLYLINE);
          ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
          int _size_1 = _controlPoints_1.size();
          String _plus = ("Cannot create cubic curve for " + Integer.valueOf(_size_1));
          String _plus_1 = (_plus + " control points. Switching to polyline");
          XConnection.LOG.warning(_plus_1);
          this.updateShapes();
        }
        ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
        int _size_2 = _controlPoints_2.size();
        int _minus_1 = (_size_2 - 1);
        final int numSegments = (_minus_1 / 3);
        ObservableList<Node> _children = this.shapeGroup.getChildren();
        Iterable<CubicCurve> _filter = Iterables.<CubicCurve>filter(_children, CubicCurve.class);
        final List<CubicCurve> curves = IterableExtensions.<CubicCurve>toList(_filter);
        int _size_3 = curves.size();
        boolean _greaterThan = (_size_3 > numSegments);
        boolean _while = _greaterThan;
        while (_while) {
          CubicCurve _last = IterableExtensions.<CubicCurve>last(curves);
          curves.remove(_last);
          int _size_4 = curves.size();
          boolean _greaterThan_1 = (_size_4 > numSegments);
          _while = _greaterThan_1;
        }
        int _size_4 = curves.size();
        boolean _lessThan = (_size_4 < numSegments);
        boolean _while_1 = _lessThan;
        while (_while_1) {
          CubicCurve _cubicCurve = new CubicCurve();
          final Procedure1<CubicCurve> _function = new Procedure1<CubicCurve>() {
            public void apply(final CubicCurve it) {
              it.setFill(null);
              it.setStroke(Color.BLACK);
            }
          };
          CubicCurve _doubleArrow = ObjectExtensions.<CubicCurve>operator_doubleArrow(_cubicCurve, _function);
          curves.add(_doubleArrow);
          int _size_5 = curves.size();
          boolean _lessThan_1 = (_size_5 < numSegments);
          _while_1 = _lessThan_1;
        }
        ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, numSegments, true);
        for (final Integer i : _doubleDotLessThan) {
          {
            final CubicCurve curve = curves.get((i).intValue());
            final int offset = ((i).intValue() * 3);
            ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
            XControlPoint _get = _controlPoints_3.get(offset);
            double _layoutX = _get.getLayoutX();
            curve.setStartX(_layoutX);
            ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
            XControlPoint _get_1 = _controlPoints_4.get(offset);
            double _layoutY = _get_1.getLayoutY();
            curve.setStartY(_layoutY);
            ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
            int _plus_2 = (offset + 1);
            XControlPoint _get_2 = _controlPoints_5.get(_plus_2);
            double _layoutX_1 = _get_2.getLayoutX();
            curve.setControlX1(_layoutX_1);
            ObservableList<XControlPoint> _controlPoints_6 = this.getControlPoints();
            int _plus_3 = (offset + 1);
            XControlPoint _get_3 = _controlPoints_6.get(_plus_3);
            double _layoutY_1 = _get_3.getLayoutY();
            curve.setControlY1(_layoutY_1);
            ObservableList<XControlPoint> _controlPoints_7 = this.getControlPoints();
            int _plus_4 = (offset + 2);
            XControlPoint _get_4 = _controlPoints_7.get(_plus_4);
            double _layoutX_2 = _get_4.getLayoutX();
            curve.setControlX2(_layoutX_2);
            ObservableList<XControlPoint> _controlPoints_8 = this.getControlPoints();
            int _plus_5 = (offset + 2);
            XControlPoint _get_5 = _controlPoints_8.get(_plus_5);
            double _layoutY_2 = _get_5.getLayoutY();
            curve.setControlY2(_layoutY_2);
            ObservableList<XControlPoint> _controlPoints_9 = this.getControlPoints();
            int _plus_6 = (offset + 3);
            XControlPoint _get_6 = _controlPoints_9.get(_plus_6);
            double _layoutX_3 = _get_6.getLayoutX();
            curve.setEndX(_layoutX_3);
            ObservableList<XControlPoint> _controlPoints_10 = this.getControlPoints();
            int _plus_7 = (offset + 3);
            XControlPoint _get_7 = _controlPoints_10.get(_plus_7);
            double _layoutY_3 = _get_7.getLayoutY();
            curve.setEndY(_layoutY_3);
          }
        }
        this.setShapes(curves);
      }
    }
    if (!_matched) {
      if (Objects.equal(getKind,XConnectionKind.QUAD_CURVE)) {
        _matched=true;
        ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
        int _size_5 = _controlPoints_3.size();
        int _minus_2 = (_size_5 - 1);
        int _modulo_1 = (_minus_2 % 2);
        boolean _notEquals_1 = (_modulo_1 != 0);
        if (_notEquals_1) {
          this.setKind(XConnectionKind.POLYLINE);
          ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
          int _size_6 = _controlPoints_4.size();
          String _plus_2 = ("Cannot create quadratic curve for " + Integer.valueOf(_size_6));
          String _plus_3 = (_plus_2 + " control points. Switching to polyline");
          XConnection.LOG.warning(_plus_3);
          this.updateShapes();
        }
        ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
        int _size_7 = _controlPoints_5.size();
        int _minus_3 = (_size_7 - 1);
        final int numSegments_1 = (_minus_3 / 2);
        ObservableList<Node> _children_1 = this.shapeGroup.getChildren();
        Iterable<QuadCurve> _filter_1 = Iterables.<QuadCurve>filter(_children_1, QuadCurve.class);
        final List<QuadCurve> curves_1 = IterableExtensions.<QuadCurve>toList(_filter_1);
        int _size_8 = curves_1.size();
        boolean _greaterThan_1 = (_size_8 > numSegments_1);
        boolean _while_2 = _greaterThan_1;
        while (_while_2) {
          QuadCurve _last = IterableExtensions.<QuadCurve>last(curves_1);
          curves_1.remove(_last);
          int _size_9 = curves_1.size();
          boolean _greaterThan_2 = (_size_9 > numSegments_1);
          _while_2 = _greaterThan_2;
        }
        int _size_9 = curves_1.size();
        boolean _lessThan_1 = (_size_9 < numSegments_1);
        boolean _while_3 = _lessThan_1;
        while (_while_3) {
          QuadCurve _quadCurve = new QuadCurve();
          final Procedure1<QuadCurve> _function = new Procedure1<QuadCurve>() {
            public void apply(final QuadCurve it) {
              it.setFill(null);
              it.setStroke(Color.BLACK);
            }
          };
          QuadCurve _doubleArrow = ObjectExtensions.<QuadCurve>operator_doubleArrow(_quadCurve, _function);
          curves_1.add(_doubleArrow);
          int _size_10 = curves_1.size();
          boolean _lessThan_2 = (_size_10 < numSegments_1);
          _while_3 = _lessThan_2;
        }
        ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, numSegments_1, true);
        for (final Integer i_1 : _doubleDotLessThan_1) {
          {
            final QuadCurve curve = curves_1.get((i_1).intValue());
            final int offset = ((i_1).intValue() * 2);
            ObservableList<XControlPoint> _controlPoints_6 = this.getControlPoints();
            XControlPoint _get = _controlPoints_6.get(offset);
            double _layoutX = _get.getLayoutX();
            curve.setStartX(_layoutX);
            ObservableList<XControlPoint> _controlPoints_7 = this.getControlPoints();
            XControlPoint _get_1 = _controlPoints_7.get(offset);
            double _layoutY = _get_1.getLayoutY();
            curve.setStartY(_layoutY);
            ObservableList<XControlPoint> _controlPoints_8 = this.getControlPoints();
            int _plus_4 = (offset + 1);
            XControlPoint _get_2 = _controlPoints_8.get(_plus_4);
            double _layoutX_1 = _get_2.getLayoutX();
            curve.setControlX(_layoutX_1);
            ObservableList<XControlPoint> _controlPoints_9 = this.getControlPoints();
            int _plus_5 = (offset + 1);
            XControlPoint _get_3 = _controlPoints_9.get(_plus_5);
            double _layoutY_1 = _get_3.getLayoutY();
            curve.setControlY(_layoutY_1);
            ObservableList<XControlPoint> _controlPoints_10 = this.getControlPoints();
            int _plus_6 = (offset + 2);
            XControlPoint _get_4 = _controlPoints_10.get(_plus_6);
            double _layoutX_2 = _get_4.getLayoutX();
            curve.setEndX(_layoutX_2);
            ObservableList<XControlPoint> _controlPoints_11 = this.getControlPoints();
            int _plus_7 = (offset + 2);
            XControlPoint _get_5 = _controlPoints_11.get(_plus_7);
            double _layoutY_2 = _get_5.getLayoutY();
            curve.setEndY(_layoutY_2);
          }
        }
        this.setShapes(curves_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(getKind,XConnectionKind.POLYLINE)) {
        _matched=true;
        Polyline _elvis = null;
        ObservableList<Node> _children_2 = this.shapeGroup.getChildren();
        Iterable<Polyline> _filter_2 = Iterables.<Polyline>filter(_children_2, Polyline.class);
        Polyline _head = IterableExtensions.<Polyline>head(_filter_2);
        if (_head != null) {
          _elvis = _head;
        } else {
          Polyline _polyline = new Polyline();
          _elvis = ObjectExtensions.<Polyline>operator_elvis(_head, _polyline);
        }
        final Procedure1<Polyline> _function = new Procedure1<Polyline>() {
          public void apply(final Polyline it) {
            it.setStroke(Color.BLACK);
          }
        };
        final Polyline polyline = ObjectExtensions.<Polyline>operator_doubleArrow(_elvis, _function);
        ObservableList<Double> _points = polyline.getPoints();
        ObservableList<XControlPoint> _controlPoints_6 = this.getControlPoints();
        final Function1<XControlPoint,List<Double>> _function_1 = new Function1<XControlPoint,List<Double>>() {
          public List<Double> apply(final XControlPoint it) {
            double _layoutX = it.getLayoutX();
            double _layoutY = it.getLayoutY();
            return Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(_layoutX, _layoutY));
          }
        };
        List<List<Double>> _map = ListExtensions.<XControlPoint, List<Double>>map(_controlPoints_6, _function_1);
        Iterable<Double> _flatten = Iterables.<Double>concat(_map);
        _points.setAll(((Double[])Conversions.unwrapArray(_flatten, Double.class)));
        this.setShapes(Collections.<Polyline>unmodifiableList(Lists.<Polyline>newArrayList(polyline)));
      }
    }
    ObservableList<Node> _children_3 = this.controlPointGroup.getChildren();
    ObservableList<XControlPoint> _controlPoints_7 = this.connectionRouter.getControlPoints();
    _children_3.setAll(_controlPoints_7);
  }
  
  protected void setShapes(final List<? extends Shape> shapes) {
    ObservableList<Node> _children = this.shapeGroup.getChildren();
    _children.setAll(shapes);
    final Function1<Shape,SimpleDoubleProperty> _function = new Function1<Shape,SimpleDoubleProperty>() {
      public SimpleDoubleProperty apply(final Shape it) {
        return XConnection.this.strokeWidthProperty;
      }
    };
    List<SimpleDoubleProperty> _map = ListExtensions.map(shapes, _function);
    final Function1<SimpleDoubleProperty,Boolean> _function_1 = new Function1<SimpleDoubleProperty,Boolean>() {
      public Boolean apply(final SimpleDoubleProperty it) {
        boolean _isBound = it.isBound();
        boolean _not = (!_isBound);
        return Boolean.valueOf(_not);
      }
    };
    Iterable<SimpleDoubleProperty> _filter = IterableExtensions.<SimpleDoubleProperty>filter(_map, _function_1);
    final Procedure1<SimpleDoubleProperty> _function_2 = new Procedure1<SimpleDoubleProperty>() {
      public void apply(final SimpleDoubleProperty it) {
        XAbstractDiagram _diagram = Extensions.getDiagram(XConnection.this);
        DoubleProperty _scaleXProperty = _diagram.scaleXProperty();
        final DoubleBinding doubleBinding = DoubleExpressionExtensions.operator_divide(XConnection.this.strokeWidthProperty, _scaleXProperty);
        doubleBinding.getValue();
        it.bind(doubleBinding);
      }
    };
    IterableExtensions.<SimpleDoubleProperty>forEach(_filter, _function_2);
  }
  
  public boolean isSelectable() {
    boolean _isActive = this.getIsActive();
    return _isActive;
  }
  
  public MoveBehavior getMoveBehavior() {
    return null;
  }
  
  public void layoutChildren() {
    super.layoutChildren();
    this.connectionRouter.calculatePoints();
    if (this.label!=null) {
      ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
      this.label.place(_controlPoints);
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XConnection");
    ;
  
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
  
  private SimpleObjectProperty<XConnectionKind> kindProperty = new SimpleObjectProperty<XConnectionKind>(this, "kind",_initKind());
  
  private static final XConnectionKind _initKind() {
    return XConnectionKind.POLYLINE;
  }
  
  public XConnectionKind getKind() {
    return this.kindProperty.get();
    
  }
  
  public void setKind(final XConnectionKind kind) {
    this.kindProperty.set(kind);
    
  }
  
  public ObjectProperty<XConnectionKind> kindProperty() {
    return this.kindProperty;
    
  }
}
