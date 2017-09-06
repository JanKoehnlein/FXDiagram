package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.anchors.CachedAnchors;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.extensions.CoreExtensions;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class ManhattanRouter {
  private final int STANDARD_DISTANCE = 20;
  
  private XConnection connection;
  
  private CachedAnchors sourceAnchors;
  
  private CachedAnchors targetAnchors;
  
  @Accessors
  private boolean reroutingEnabled = false;
  
  public ManhattanRouter(final XConnection connection) {
    this.connection = connection;
  }
  
  public void calculatePoints() {
    if ((((this.connection.getControlPoints().size() == 0) && (!Objects.equal(this.sourceAnchors, null))) || (!(this.connection.getSource().getIsActive() && this.connection.getTarget().getIsActive())))) {
      return;
    }
    XNode _source = this.connection.getSource();
    final CachedAnchors newSourceAnchors = new CachedAnchors(_source);
    XNode _target = this.connection.getTarget();
    final CachedAnchors newTargetAnchors = new CachedAnchors(_target);
    final Function1<XControlPoint, Boolean> _function = (XControlPoint it) -> {
      boolean _or = false;
      boolean _manuallyPlaced = it.getManuallyPlaced();
      if (_manuallyPlaced) {
        _or = true;
      } else {
        MoveBehavior _behavior = it.<MoveBehavior>getBehavior(MoveBehavior.class);
        boolean _hasMoved = false;
        if (_behavior!=null) {
          _hasMoved=_behavior.hasMoved();
        }
        _or = _hasMoved;
      }
      return Boolean.valueOf(_or);
    };
    final boolean manuallyMoved = IterableExtensions.<XControlPoint>exists(this.connection.getControlPoints(), _function);
    if ((((!Objects.equal(this.sourceAnchors, null)) && (!Objects.equal(this.targetAnchors, null))) && manuallyMoved)) {
      this.sourceAnchors = newSourceAnchors;
      this.targetAnchors = newTargetAnchors;
      this.partiallyRerouteIfNecessary(this.sourceAnchors, IterableExtensions.<XControlPoint>head(this.connection.getControlPoints()), true);
      this.partiallyRerouteIfNecessary(this.targetAnchors, IterableExtensions.<XControlPoint>last(this.connection.getControlPoints()), false);
      return;
    } else {
      this.sourceAnchors = newSourceAnchors;
      this.targetAnchors = newTargetAnchors;
      if ((!manuallyMoved)) {
        final ArrayList<XControlPoint> newControlPoints = this.getDefaultPoints();
        this.connection.getControlPoints().setAll(newControlPoints);
      }
      this.reroutingEnabled = true;
    }
  }
  
  public ArrayList<XControlPoint> getDefaultPoints() {
    final Pair<Side, Side> connectionDir = this.getConnectionDirection();
    final ArrayList<Point2D> points = this.calculateDefaultPoints(connectionDir.getKey(), connectionDir.getValue());
    ArrowHead _sourceArrowHead = this.connection.getSourceArrowHead();
    boolean _notEquals = (!Objects.equal(_sourceArrowHead, null));
    if (_notEquals) {
      points.set(0, this.connection.getSourceArrowHead().correctAnchor(points.get(1).getX(), points.get(1).getY(), IterableExtensions.<Point2D>head(points)));
    }
    ArrowHead _targetArrowHead = this.connection.getTargetArrowHead();
    boolean _notEquals_1 = (!Objects.equal(_targetArrowHead, null));
    if (_notEquals_1) {
      int _size = points.size();
      int _minus = (_size - 1);
      int _size_1 = points.size();
      int _minus_1 = (_size_1 - 2);
      int _size_2 = points.size();
      int _minus_2 = (_size_2 - 2);
      points.set(_minus, this.connection.getTargetArrowHead().correctAnchor(points.get(_minus_1).getX(), points.get(_minus_2).getY(), IterableExtensions.<Point2D>last(points)));
    }
    final ArrayList<XControlPoint> newControlPoints = CollectionLiterals.<XControlPoint>newArrayList();
    final Procedure2<Point2D, Integer> _function = (Point2D point, Integer i) -> {
      final boolean isAnchorPoint = (((i).intValue() == 0) || ((i).intValue() == (points.size() - 1)));
      XControlPoint _xControlPoint = new XControlPoint();
      final Procedure1<XControlPoint> _function_1 = (XControlPoint it) -> {
        if (isAnchorPoint) {
          it.setLayoutX(point.getX());
          it.setLayoutY(point.getY());
          it.setType(XControlPoint.Type.ANCHOR);
        } else {
          final Point2D snappedPoint = CoreExtensions.getDiagram(this.connection).getSnappedPosition(point);
          it.setLayoutX(snappedPoint.getX());
          it.setLayoutY(snappedPoint.getY());
          it.setType(XControlPoint.Type.INTERPOLATED);
        }
      };
      XControlPoint _doubleArrow = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function_1);
      newControlPoints.add(_doubleArrow);
    };
    IterableExtensions.<Point2D>forEach(points, _function);
    XControlPoint _head = IterableExtensions.<XControlPoint>head(newControlPoints);
    _head.setSide(connectionDir.getKey());
    XControlPoint _last = IterableExtensions.<XControlPoint>last(newControlPoints);
    _last.setSide(connectionDir.getValue());
    return newControlPoints;
  }
  
  protected void partiallyRerouteIfNecessary(final CachedAnchors connected, final XControlPoint anchor, final boolean isSource) {
    final Side lastSide = anchor.getSide();
    XControlPoint _xifexpression = null;
    if (isSource) {
      _xifexpression = this.connection.getControlPoints().get(1);
    } else {
      ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
      int _size = this.connection.getControlPoints().size();
      int _minus = (_size - 2);
      _xifexpression = _controlPoints.get(_minus);
    }
    final XControlPoint referencePoint = _xifexpression;
    XControlPoint _xifexpression_1 = null;
    int _size_1 = this.connection.getControlPoints().size();
    boolean _greaterThan = (_size_1 > 3);
    if (_greaterThan) {
      XControlPoint _xifexpression_2 = null;
      if (isSource) {
        _xifexpression_2 = this.connection.getControlPoints().get(2);
      } else {
        ObservableList<XControlPoint> _controlPoints_1 = this.connection.getControlPoints();
        int _size_2 = this.connection.getControlPoints().size();
        int _minus_1 = (_size_2 - 3);
        _xifexpression_2 = _controlPoints_1.get(_minus_1);
      }
      _xifexpression_1 = _xifexpression_2;
    } else {
      _xifexpression_1 = null;
    }
    final XControlPoint refPoint2 = _xifexpression_1;
    double _xifexpression_3 = (double) 0;
    if (isSource) {
      ArrowHead _sourceArrowHead = this.connection.getSourceArrowHead();
      double _lineCut = 0.0;
      if (_sourceArrowHead!=null) {
        _lineCut=_sourceArrowHead.getLineCut();
      }
      _xifexpression_3 = _lineCut;
    } else {
      ArrowHead _targetArrowHead = this.connection.getTargetArrowHead();
      double _lineCut_1 = 0.0;
      if (_targetArrowHead!=null) {
        _lineCut_1=_targetArrowHead.getLineCut();
      }
      _xifexpression_3 = _lineCut_1;
    }
    final double lineCut = _xifexpression_3;
    final boolean doReroute = ((this.reroutingEnabled && (!referencePoint.layoutXProperty().isBound())) && (!referencePoint.layoutYProperty().isBound()));
    if (lastSide != null) {
      switch (lastSide) {
        case TOP:
        case BOTTOM:
          if (doReroute) {
            double _layoutX = anchor.getLayoutX();
            double _x = connected.get(Side.LEFT).getX();
            boolean _lessThan = (_layoutX < _x);
            if (_lessThan) {
              this.addCorner(connected, anchor, isSource, Side.LEFT);
              return;
            } else {
              double _layoutX_1 = anchor.getLayoutX();
              double _x_1 = connected.get(Side.RIGHT).getX();
              boolean _greaterThan_1 = (_layoutX_1 > _x_1);
              if (_greaterThan_1) {
                this.addCorner(connected, anchor, isSource, Side.RIGHT);
                return;
              } else {
                if ((((!Objects.equal(refPoint2, null)) && (refPoint2.getLayoutY() > connected.get(Side.TOP).getY())) && (refPoint2.getLayoutY() < connected.get(Side.BOTTOM).getY()))) {
                  double _layoutX_2 = refPoint2.getLayoutX();
                  double _x_2 = connected.get(lastSide).getX();
                  boolean _lessThan_1 = (_layoutX_2 < _x_2);
                  if (_lessThan_1) {
                    this.removeCorner(connected, anchor, isSource, Side.LEFT);
                  } else {
                    this.removeCorner(connected, anchor, isSource, Side.RIGHT);
                  }
                  return;
                } else {
                  if ((Objects.equal(lastSide, Side.TOP) && ((referencePoint.getLayoutY() > (connected.get(Side.BOTTOM).getY() + lineCut)) || (Math.abs((referencePoint.getLayoutY() - connected.get(Side.TOP).getY())) < lineCut)))) {
                    this.switchSide(connected, anchor, isSource, Side.BOTTOM);
                    return;
                  } else {
                    if ((Objects.equal(lastSide, Side.BOTTOM) && ((referencePoint.getLayoutY() < (connected.get(Side.TOP).getY() - lineCut)) || (Math.abs((referencePoint.getLayoutY() - connected.get(Side.BOTTOM).getY())) < lineCut)))) {
                      this.switchSide(connected, anchor, isSource, Side.TOP);
                      return;
                    }
                  }
                }
              }
            }
          }
          break;
        case LEFT:
        case RIGHT:
          if (doReroute) {
            double _layoutY = anchor.getLayoutY();
            double _y = connected.get(Side.TOP).getY();
            boolean _lessThan_2 = (_layoutY < _y);
            if (_lessThan_2) {
              this.addCorner(connected, anchor, isSource, Side.TOP);
              return;
            } else {
              double _layoutY_1 = anchor.getLayoutY();
              double _y_1 = connected.get(Side.BOTTOM).getY();
              boolean _greaterThan_2 = (_layoutY_1 > _y_1);
              if (_greaterThan_2) {
                this.addCorner(connected, anchor, isSource, Side.BOTTOM);
                return;
              } else {
                if ((((!Objects.equal(refPoint2, null)) && (refPoint2.getLayoutX() > connected.get(Side.LEFT).getX())) && (refPoint2.getLayoutX() < connected.get(Side.RIGHT).getX()))) {
                  double _layoutY_2 = refPoint2.getLayoutY();
                  double _y_2 = connected.get(lastSide).getY();
                  boolean _lessThan_3 = (_layoutY_2 < _y_2);
                  if (_lessThan_3) {
                    this.removeCorner(connected, anchor, isSource, Side.TOP);
                  } else {
                    this.removeCorner(connected, anchor, isSource, Side.BOTTOM);
                  }
                  return;
                } else {
                  if ((Objects.equal(lastSide, Side.LEFT) && ((referencePoint.getLayoutX() > (connected.get(Side.RIGHT).getX() + lineCut)) || (Math.abs((referencePoint.getLayoutX() - connected.get(Side.LEFT).getX())) < lineCut)))) {
                    this.switchSide(connected, anchor, isSource, Side.RIGHT);
                    return;
                  } else {
                    if ((Objects.equal(lastSide, Side.RIGHT) && ((referencePoint.getLayoutX() < (connected.get(Side.LEFT).getX() - lineCut)) || (Math.abs((referencePoint.getLayoutX() - connected.get(Side.RIGHT).getX())) < lineCut)))) {
                      this.switchSide(connected, anchor, isSource, Side.LEFT);
                      return;
                    }
                  }
                }
              }
            }
          }
          break;
        default:
          break;
      }
    }
    this.setAnchorPoint(connected, anchor, connected.get(referencePoint, lastSide), isSource, lastSide, referencePoint);
  }
  
  protected void switchSide(final CachedAnchors connected, final XControlPoint anchor, final boolean isSource, final Side newSide) {
    XControlPoint _xifexpression = null;
    if (isSource) {
      _xifexpression = this.connection.getControlPoints().get(1);
    } else {
      ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
      int _size = this.connection.getControlPoints().size();
      int _minus = (_size - 2);
      _xifexpression = _controlPoints.get(_minus);
    }
    final XControlPoint referencePoint = _xifexpression;
    this.setAnchorPoint(connected, anchor, connected.get(newSide), isSource, newSide, referencePoint);
  }
  
  protected void addCorner(final CachedAnchors connected, final XControlPoint anchor, final boolean isSource, final Side newSide) {
    int _xifexpression = (int) 0;
    if (isSource) {
      _xifexpression = 1;
    } else {
      int _size = this.connection.getControlPoints().size();
      _xifexpression = (_size - 1);
    }
    final int index = _xifexpression;
    final double cpX = anchor.getLayoutX();
    final double cpY = anchor.getLayoutY();
    XControlPoint _xControlPoint = new XControlPoint();
    final Procedure1<XControlPoint> _function = (XControlPoint it) -> {
      double _xifexpression_1 = (double) 0;
      boolean _isVertical = newSide.isVertical();
      if (_isVertical) {
        _xifexpression_1 = cpX;
      } else {
        _xifexpression_1 = connected.get(newSide).getX();
      }
      it.setLayoutX(_xifexpression_1);
      double _xifexpression_2 = (double) 0;
      boolean _isVertical_1 = newSide.isVertical();
      if (_isVertical_1) {
        _xifexpression_2 = connected.get(newSide).getY();
      } else {
        _xifexpression_2 = cpY;
      }
      it.setLayoutY(_xifexpression_2);
      it.setType(XControlPoint.Type.INTERPOLATED);
    };
    final XControlPoint newPoint = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function);
    this.connection.getControlPoints().add(index, newPoint);
    this.setAnchorPoint(connected, anchor, connected.get(newSide), isSource, newSide, newPoint);
  }
  
  protected void removeCorner(final CachedAnchors connected, final XControlPoint anchor, final boolean isSource, final Side newSide) {
    XControlPoint _xifexpression = null;
    if (isSource) {
      XControlPoint _xblockexpression = null;
      {
        this.connection.getControlPoints().remove(1);
        _xblockexpression = this.connection.getControlPoints().get(1);
      }
      _xifexpression = _xblockexpression;
    } else {
      XControlPoint _xblockexpression_1 = null;
      {
        ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
        int _size = this.connection.getControlPoints().size();
        int _minus = (_size - 2);
        _controlPoints.remove(_minus);
        ObservableList<XControlPoint> _controlPoints_1 = this.connection.getControlPoints();
        int _size_1 = this.connection.getControlPoints().size();
        int _minus_1 = (_size_1 - 2);
        _xblockexpression_1 = _controlPoints_1.get(_minus_1);
      }
      _xifexpression = _xblockexpression_1;
    }
    final XControlPoint referencePoint = _xifexpression;
    Point2D _xifexpression_1 = null;
    boolean _isHorizontal = newSide.isHorizontal();
    if (_isHorizontal) {
      double _layoutX = referencePoint.getLayoutX();
      double _y = connected.get(newSide).getY();
      _xifexpression_1 = new Point2D(_layoutX, _y);
    } else {
      double _x = connected.get(newSide).getX();
      double _layoutY = referencePoint.getLayoutY();
      _xifexpression_1 = new Point2D(_x, _layoutY);
    }
    final Point2D anchorPoint = _xifexpression_1;
    this.setAnchorPoint(connected, anchor, anchorPoint, isSource, newSide, referencePoint);
  }
  
  protected void setAnchorPoint(final CachedAnchors connected, final XControlPoint anchor, final Point2D newAnchorPoint, final boolean isSource, final Side newSide, final XControlPoint referencePoint) {
    Point2D anchorPoint = newAnchorPoint;
    if (isSource) {
      ArrowHead _sourceArrowHead = this.connection.getSourceArrowHead();
      boolean _notEquals = (!Objects.equal(_sourceArrowHead, null));
      if (_notEquals) {
        anchorPoint = this.connection.getSourceArrowHead().correctAnchor(referencePoint.getLayoutX(), referencePoint.getLayoutY(), newAnchorPoint);
      }
    } else {
      ArrowHead _targetArrowHead = this.connection.getTargetArrowHead();
      boolean _notEquals_1 = (!Objects.equal(_targetArrowHead, null));
      if (_notEquals_1) {
        anchorPoint = this.connection.getTargetArrowHead().correctAnchor(referencePoint.getLayoutX(), referencePoint.getLayoutY(), newAnchorPoint);
      }
    }
    anchor.setSide(newSide);
    anchor.setLayoutX(anchorPoint.getX());
    anchor.setLayoutY(anchorPoint.getY());
  }
  
  protected ArrayList<Point2D> calculateDefaultPoints(final Side sourceSide, final Side targetSide) {
    final ArrayList<Point2D> points = CollectionLiterals.<Point2D>newArrayList();
    final Point2D startPoint = this.sourceAnchors.get(sourceSide);
    Point2D endPoint = this.targetAnchors.get(targetSide);
    if (sourceSide != null) {
      switch (sourceSide) {
        case RIGHT:
          points.add(startPoint);
          if (targetSide != null) {
            switch (targetSide) {
              case BOTTOM:
                double _x = endPoint.getX();
                double _y = startPoint.getY();
                Point2D _point2D = new Point2D(_x, _y);
                points.add(_point2D);
                break;
              case TOP:
                double _x_1 = endPoint.getX();
                double _y_1 = startPoint.getY();
                Point2D _point2D_1 = new Point2D(_x_1, _y_1);
                points.add(_point2D_1);
                break;
              case RIGHT:
                double _max = Math.max(startPoint.getX(), endPoint.getX());
                double _plus = (_max + (1.5 * this.STANDARD_DISTANCE));
                double _y_2 = startPoint.getY();
                Point2D _point2D_2 = new Point2D(_plus, _y_2);
                points.add(_point2D_2);
                double _max_1 = Math.max(startPoint.getX(), endPoint.getX());
                double _plus_1 = (_max_1 + (1.5 * this.STANDARD_DISTANCE));
                double _y_3 = endPoint.getY();
                Point2D _point2D_3 = new Point2D(_plus_1, _y_3);
                points.add(_point2D_3);
                break;
              case LEFT:
                double _y_4 = endPoint.getY();
                double _y_5 = startPoint.getY();
                boolean _notEquals = (_y_4 != _y_5);
                if (_notEquals) {
                  double _x_2 = startPoint.getX();
                  double _x_3 = endPoint.getX();
                  double _plus_2 = (_x_2 + _x_3);
                  double _divide = (_plus_2 / 2);
                  double _y_6 = startPoint.getY();
                  Point2D _point2D_4 = new Point2D(_divide, _y_6);
                  points.add(_point2D_4);
                  double _x_4 = startPoint.getX();
                  double _x_5 = endPoint.getX();
                  double _plus_3 = (_x_4 + _x_5);
                  double _divide_1 = (_plus_3 / 2);
                  double _y_7 = endPoint.getY();
                  Point2D _point2D_5 = new Point2D(_divide_1, _y_7);
                  points.add(_point2D_5);
                }
                break;
              default:
                break;
            }
          }
          break;
        case LEFT:
          points.add(startPoint);
          if (targetSide != null) {
            switch (targetSide) {
              case BOTTOM:
                double _x_6 = endPoint.getX();
                double _y_8 = startPoint.getY();
                Point2D _point2D_6 = new Point2D(_x_6, _y_8);
                points.add(_point2D_6);
                break;
              case TOP:
                double _x_7 = endPoint.getX();
                double _y_9 = startPoint.getY();
                Point2D _point2D_7 = new Point2D(_x_7, _y_9);
                points.add(_point2D_7);
                break;
              default:
                {
                  endPoint = this.targetAnchors.get(Side.RIGHT);
                  double _y_10 = endPoint.getY();
                  double _y_11 = startPoint.getY();
                  boolean _notEquals_1 = (_y_10 != _y_11);
                  if (_notEquals_1) {
                    double _x_8 = startPoint.getX();
                    double _x_9 = endPoint.getX();
                    double _plus_4 = (_x_8 + _x_9);
                    double _divide_2 = (_plus_4 / 2);
                    double _y_12 = startPoint.getY();
                    Point2D _point2D_8 = new Point2D(_divide_2, _y_12);
                    points.add(_point2D_8);
                    double _x_10 = startPoint.getX();
                    double _x_11 = endPoint.getX();
                    double _plus_5 = (_x_10 + _x_11);
                    double _divide_3 = (_plus_5 / 2);
                    double _y_13 = endPoint.getY();
                    Point2D _point2D_9 = new Point2D(_divide_3, _y_13);
                    points.add(_point2D_9);
                  }
                }
                break;
            }
          } else {
            {
              endPoint = this.targetAnchors.get(Side.RIGHT);
              double _y_10 = endPoint.getY();
              double _y_11 = startPoint.getY();
              boolean _notEquals_1 = (_y_10 != _y_11);
              if (_notEquals_1) {
                double _x_8 = startPoint.getX();
                double _x_9 = endPoint.getX();
                double _plus_4 = (_x_8 + _x_9);
                double _divide_2 = (_plus_4 / 2);
                double _y_12 = startPoint.getY();
                Point2D _point2D_8 = new Point2D(_divide_2, _y_12);
                points.add(_point2D_8);
                double _x_10 = startPoint.getX();
                double _x_11 = endPoint.getX();
                double _plus_5 = (_x_10 + _x_11);
                double _divide_3 = (_plus_5 / 2);
                double _y_13 = endPoint.getY();
                Point2D _point2D_9 = new Point2D(_divide_3, _y_13);
                points.add(_point2D_9);
              }
            }
          }
          break;
        case TOP:
          points.add(startPoint);
          if (targetSide != null) {
            switch (targetSide) {
              case RIGHT:
                double _x_8 = endPoint.getX();
                double _x_9 = startPoint.getX();
                double _minus = (_x_8 - _x_9);
                boolean _greaterThan = (_minus > 0);
                if (_greaterThan) {
                  double _x_10 = startPoint.getX();
                  double _y_10 = startPoint.getY();
                  double _minus_1 = (_y_10 - this.STANDARD_DISTANCE);
                  Point2D _point2D_8 = new Point2D(_x_10, _minus_1);
                  points.add(_point2D_8);
                  double _x_11 = endPoint.getX();
                  double _plus_4 = (_x_11 + (1.5 * this.STANDARD_DISTANCE));
                  double _y_11 = startPoint.getY();
                  double _minus_2 = (_y_11 - this.STANDARD_DISTANCE);
                  Point2D _point2D_9 = new Point2D(_plus_4, _minus_2);
                  points.add(_point2D_9);
                  double _x_12 = endPoint.getX();
                  double _plus_5 = (_x_12 + (1.5 * this.STANDARD_DISTANCE));
                  double _y_12 = endPoint.getY();
                  Point2D _point2D_10 = new Point2D(_plus_5, _y_12);
                  points.add(_point2D_10);
                } else {
                  double _x_13 = startPoint.getX();
                  double _y_13 = endPoint.getY();
                  Point2D _point2D_11 = new Point2D(_x_13, _y_13);
                  points.add(_point2D_11);
                }
                break;
              case LEFT:
                double _x_14 = endPoint.getX();
                double _x_15 = startPoint.getX();
                double _minus_3 = (_x_14 - _x_15);
                boolean _lessThan = (_minus_3 < 0);
                if (_lessThan) {
                  double _x_16 = startPoint.getX();
                  double _y_14 = startPoint.getY();
                  double _minus_4 = (_y_14 - this.STANDARD_DISTANCE);
                  Point2D _point2D_12 = new Point2D(_x_16, _minus_4);
                  points.add(_point2D_12);
                  double _x_17 = endPoint.getX();
                  double _minus_5 = (_x_17 - (1.5 * this.STANDARD_DISTANCE));
                  double _y_15 = startPoint.getY();
                  double _minus_6 = (_y_15 - this.STANDARD_DISTANCE);
                  Point2D _point2D_13 = new Point2D(_minus_5, _minus_6);
                  points.add(_point2D_13);
                  double _x_18 = endPoint.getX();
                  double _minus_7 = (_x_18 - (1.5 * this.STANDARD_DISTANCE));
                  double _y_16 = endPoint.getY();
                  Point2D _point2D_14 = new Point2D(_minus_7, _y_16);
                  points.add(_point2D_14);
                } else {
                  double _x_19 = startPoint.getX();
                  double _y_17 = endPoint.getY();
                  Point2D _point2D_15 = new Point2D(_x_19, _y_17);
                  points.add(_point2D_15);
                }
                break;
              case TOP:
                double _x_20 = startPoint.getX();
                double _min = Math.min(startPoint.getY(), endPoint.getY());
                double _minus_8 = (_min - (1.5 * this.STANDARD_DISTANCE));
                Point2D _point2D_16 = new Point2D(_x_20, _minus_8);
                points.add(_point2D_16);
                double _x_21 = endPoint.getX();
                double _min_1 = Math.min(startPoint.getY(), endPoint.getY());
                double _minus_9 = (_min_1 - (1.5 * this.STANDARD_DISTANCE));
                Point2D _point2D_17 = new Point2D(_x_21, _minus_9);
                points.add(_point2D_17);
                break;
              case BOTTOM:
                double _x_22 = endPoint.getX();
                double _x_23 = startPoint.getX();
                boolean _notEquals_1 = (_x_22 != _x_23);
                if (_notEquals_1) {
                  double _x_24 = startPoint.getX();
                  double _y_18 = startPoint.getY();
                  double _y_19 = endPoint.getY();
                  double _plus_6 = (_y_18 + _y_19);
                  double _divide_2 = (_plus_6 / 2);
                  Point2D _point2D_18 = new Point2D(_x_24, _divide_2);
                  points.add(_point2D_18);
                  double _x_25 = endPoint.getX();
                  double _y_20 = startPoint.getY();
                  double _y_21 = endPoint.getY();
                  double _plus_7 = (_y_20 + _y_21);
                  double _divide_3 = (_plus_7 / 2);
                  Point2D _point2D_19 = new Point2D(_x_25, _divide_3);
                  points.add(_point2D_19);
                }
                break;
              default:
                break;
            }
          }
          break;
        case BOTTOM:
          points.add(startPoint);
          if (targetSide != null) {
            switch (targetSide) {
              case RIGHT:
                double _x_26 = endPoint.getX();
                double _x_27 = startPoint.getX();
                double _minus_10 = (_x_26 - _x_27);
                boolean _greaterThan_1 = (_minus_10 > 0);
                if (_greaterThan_1) {
                  double _x_28 = startPoint.getX();
                  double _y_22 = startPoint.getY();
                  double _plus_8 = (_y_22 + this.STANDARD_DISTANCE);
                  Point2D _point2D_20 = new Point2D(_x_28, _plus_8);
                  points.add(_point2D_20);
                  double _x_29 = endPoint.getX();
                  double _plus_9 = (_x_29 + (1.5 * this.STANDARD_DISTANCE));
                  double _y_23 = startPoint.getY();
                  double _plus_10 = (_y_23 + this.STANDARD_DISTANCE);
                  Point2D _point2D_21 = new Point2D(_plus_9, _plus_10);
                  points.add(_point2D_21);
                  double _x_30 = endPoint.getX();
                  double _plus_11 = (_x_30 + (1.5 * this.STANDARD_DISTANCE));
                  double _y_24 = endPoint.getY();
                  Point2D _point2D_22 = new Point2D(_plus_11, _y_24);
                  points.add(_point2D_22);
                } else {
                  double _x_31 = startPoint.getX();
                  double _y_25 = endPoint.getY();
                  Point2D _point2D_23 = new Point2D(_x_31, _y_25);
                  points.add(_point2D_23);
                }
                break;
              case LEFT:
                double _x_32 = endPoint.getX();
                double _x_33 = startPoint.getX();
                double _minus_11 = (_x_32 - _x_33);
                boolean _lessThan_1 = (_minus_11 < 0);
                if (_lessThan_1) {
                  double _x_34 = startPoint.getX();
                  double _y_26 = startPoint.getY();
                  double _plus_12 = (_y_26 + this.STANDARD_DISTANCE);
                  Point2D _point2D_24 = new Point2D(_x_34, _plus_12);
                  points.add(_point2D_24);
                  double _x_35 = endPoint.getX();
                  double _minus_12 = (_x_35 - (1.5 * this.STANDARD_DISTANCE));
                  double _y_27 = startPoint.getY();
                  double _plus_13 = (_y_27 + this.STANDARD_DISTANCE);
                  Point2D _point2D_25 = new Point2D(_minus_12, _plus_13);
                  points.add(_point2D_25);
                  double _x_36 = endPoint.getX();
                  double _minus_13 = (_x_36 - (1.5 * this.STANDARD_DISTANCE));
                  double _y_28 = endPoint.getY();
                  Point2D _point2D_26 = new Point2D(_minus_13, _y_28);
                  points.add(_point2D_26);
                } else {
                  double _x_37 = startPoint.getX();
                  double _y_29 = endPoint.getY();
                  Point2D _point2D_27 = new Point2D(_x_37, _y_29);
                  points.add(_point2D_27);
                }
                break;
              default:
                {
                  endPoint = this.targetAnchors.get(Side.TOP);
                  double _x_38 = endPoint.getX();
                  double _x_39 = startPoint.getX();
                  boolean _notEquals_2 = (_x_38 != _x_39);
                  if (_notEquals_2) {
                    double _x_40 = startPoint.getX();
                    double _y_30 = startPoint.getY();
                    double _y_31 = endPoint.getY();
                    double _plus_14 = (_y_30 + _y_31);
                    double _divide_4 = (_plus_14 / 2);
                    Point2D _point2D_28 = new Point2D(_x_40, _divide_4);
                    points.add(_point2D_28);
                    double _x_41 = endPoint.getX();
                    double _y_32 = startPoint.getY();
                    double _y_33 = endPoint.getY();
                    double _plus_15 = (_y_32 + _y_33);
                    double _divide_5 = (_plus_15 / 2);
                    Point2D _point2D_29 = new Point2D(_x_41, _divide_5);
                    points.add(_point2D_29);
                  }
                }
                break;
            }
          } else {
            {
              endPoint = this.targetAnchors.get(Side.TOP);
              double _x_38 = endPoint.getX();
              double _x_39 = startPoint.getX();
              boolean _notEquals_2 = (_x_38 != _x_39);
              if (_notEquals_2) {
                double _x_40 = startPoint.getX();
                double _y_30 = startPoint.getY();
                double _y_31 = endPoint.getY();
                double _plus_14 = (_y_30 + _y_31);
                double _divide_4 = (_plus_14 / 2);
                Point2D _point2D_28 = new Point2D(_x_40, _divide_4);
                points.add(_point2D_28);
                double _x_41 = endPoint.getX();
                double _y_32 = startPoint.getY();
                double _y_33 = endPoint.getY();
                double _plus_15 = (_y_32 + _y_33);
                double _divide_5 = (_plus_15 / 2);
                Point2D _point2D_29 = new Point2D(_x_41, _divide_5);
                points.add(_point2D_29);
              }
            }
          }
          break;
        default:
          break;
      }
    }
    points.add(endPoint);
    return points;
  }
  
  protected Pair<Side, Side> getConnectionDirection() {
    Point2D sourcePoint = this.sourceAnchors.getUnselected(Side.RIGHT);
    Point2D targetPoint = this.targetAnchors.getUnselected(Side.LEFT);
    double _x = targetPoint.getX();
    double _x_1 = sourcePoint.getX();
    double _minus = (_x - _x_1);
    boolean _greaterThan = (_minus > this.STANDARD_DISTANCE);
    if (_greaterThan) {
      return Pair.<Side, Side>of(Side.RIGHT, Side.LEFT);
    }
    sourcePoint = this.sourceAnchors.getUnselected(Side.LEFT);
    targetPoint = this.targetAnchors.getUnselected(Side.RIGHT);
    double _x_2 = sourcePoint.getX();
    double _x_3 = targetPoint.getX();
    double _minus_1 = (_x_2 - _x_3);
    boolean _greaterThan_1 = (_minus_1 > this.STANDARD_DISTANCE);
    if (_greaterThan_1) {
      return Pair.<Side, Side>of(Side.LEFT, Side.RIGHT);
    }
    sourcePoint = this.sourceAnchors.getUnselected(Side.TOP);
    targetPoint = this.targetAnchors.getUnselected(Side.BOTTOM);
    double _y = sourcePoint.getY();
    double _y_1 = targetPoint.getY();
    double _minus_2 = (_y - _y_1);
    boolean _greaterThan_2 = (_minus_2 > this.STANDARD_DISTANCE);
    if (_greaterThan_2) {
      return Pair.<Side, Side>of(Side.TOP, Side.BOTTOM);
    }
    sourcePoint = this.sourceAnchors.getUnselected(Side.BOTTOM);
    targetPoint = this.targetAnchors.getUnselected(Side.TOP);
    double _y_2 = targetPoint.getY();
    double _y_3 = sourcePoint.getY();
    double _minus_3 = (_y_2 - _y_3);
    boolean _greaterThan_3 = (_minus_3 > this.STANDARD_DISTANCE);
    if (_greaterThan_3) {
      return Pair.<Side, Side>of(Side.BOTTOM, Side.TOP);
    }
    sourcePoint = this.sourceAnchors.getUnselected(Side.RIGHT);
    targetPoint = this.targetAnchors.getUnselected(Side.TOP);
    if ((((targetPoint.getX() - sourcePoint.getX()) > (0.5 * this.STANDARD_DISTANCE)) && ((targetPoint.getY() - sourcePoint.getY()) > this.STANDARD_DISTANCE))) {
      return Pair.<Side, Side>of(Side.RIGHT, Side.TOP);
    }
    targetPoint = this.targetAnchors.getUnselected(Side.BOTTOM);
    if ((((targetPoint.getX() - sourcePoint.getX()) > (0.5 * this.STANDARD_DISTANCE)) && ((sourcePoint.getY() - targetPoint.getY()) > this.STANDARD_DISTANCE))) {
      return Pair.<Side, Side>of(Side.RIGHT, Side.BOTTOM);
    }
    sourcePoint = this.sourceAnchors.getUnselected(Side.LEFT);
    targetPoint = this.targetAnchors.getUnselected(Side.BOTTOM);
    if ((((sourcePoint.getX() - targetPoint.getX()) > (0.5 * this.STANDARD_DISTANCE)) && ((sourcePoint.getY() - targetPoint.getY()) > this.STANDARD_DISTANCE))) {
      return Pair.<Side, Side>of(Side.LEFT, Side.BOTTOM);
    }
    targetPoint = this.targetAnchors.getUnselected(Side.TOP);
    if ((((sourcePoint.getX() - targetPoint.getX()) > (0.5 * this.STANDARD_DISTANCE)) && ((targetPoint.getY() - sourcePoint.getY()) > this.STANDARD_DISTANCE))) {
      return Pair.<Side, Side>of(Side.LEFT, Side.TOP);
    }
    sourcePoint = this.sourceAnchors.getUnselected(Side.TOP);
    targetPoint = this.targetAnchors.getUnselected(Side.RIGHT);
    if ((((sourcePoint.getY() - targetPoint.getY()) > (0.5 * this.STANDARD_DISTANCE)) && ((sourcePoint.getX() - targetPoint.getX()) > this.STANDARD_DISTANCE))) {
      return Pair.<Side, Side>of(Side.TOP, Side.RIGHT);
    }
    targetPoint = this.targetAnchors.getUnselected(Side.LEFT);
    if ((((sourcePoint.getY() - targetPoint.getY()) > (0.5 * this.STANDARD_DISTANCE)) && ((targetPoint.getX() - sourcePoint.getX()) > this.STANDARD_DISTANCE))) {
      return Pair.<Side, Side>of(Side.TOP, Side.LEFT);
    }
    sourcePoint = this.sourceAnchors.getUnselected(Side.BOTTOM);
    targetPoint = this.targetAnchors.getUnselected(Side.RIGHT);
    if ((((targetPoint.getY() - sourcePoint.getY()) > (0.5 * this.STANDARD_DISTANCE)) && ((sourcePoint.getX() - targetPoint.getX()) > this.STANDARD_DISTANCE))) {
      return Pair.<Side, Side>of(Side.BOTTOM, Side.RIGHT);
    }
    targetPoint = this.targetAnchors.getUnselected(Side.LEFT);
    if ((((targetPoint.getY() - sourcePoint.getY()) > (0.5 * this.STANDARD_DISTANCE)) && ((targetPoint.getX() - sourcePoint.getX()) > this.STANDARD_DISTANCE))) {
      return Pair.<Side, Side>of(Side.BOTTOM, Side.LEFT);
    }
    sourcePoint = this.sourceAnchors.getUnselected(Side.TOP);
    targetPoint = this.targetAnchors.getUnselected(Side.TOP);
    if (((!this.targetAnchors.contains(sourcePoint)) && (!this.sourceAnchors.contains(targetPoint)))) {
      double _y_4 = sourcePoint.getY();
      double _y_5 = targetPoint.getY();
      double _minus_4 = (_y_4 - _y_5);
      boolean _lessThan = (_minus_4 < 0);
      if (_lessThan) {
        double _x_4 = sourcePoint.getX();
        double _x_5 = targetPoint.getX();
        double _minus_5 = (_x_4 - _x_5);
        double _abs = Math.abs(_minus_5);
        double _width = this.sourceAnchors.getWidth();
        double _plus = (_width + this.STANDARD_DISTANCE);
        double _divide = (_plus / 2);
        boolean _greaterThan_4 = (_abs > _divide);
        if (_greaterThan_4) {
          return Pair.<Side, Side>of(Side.TOP, Side.TOP);
        }
      } else {
        double _x_6 = sourcePoint.getX();
        double _x_7 = targetPoint.getX();
        double _minus_6 = (_x_6 - _x_7);
        double _abs_1 = Math.abs(_minus_6);
        double _width_1 = this.targetAnchors.getWidth();
        double _divide_1 = (_width_1 / 2);
        boolean _greaterThan_5 = (_abs_1 > _divide_1);
        if (_greaterThan_5) {
          return Pair.<Side, Side>of(Side.TOP, Side.TOP);
        }
      }
    }
    sourcePoint = this.sourceAnchors.getUnselected(Side.RIGHT);
    targetPoint = this.targetAnchors.getUnselected(Side.RIGHT);
    if (((!this.targetAnchors.contains(sourcePoint)) && (!this.sourceAnchors.contains(targetPoint)))) {
      double _x_8 = sourcePoint.getX();
      double _x_9 = targetPoint.getX();
      double _minus_7 = (_x_8 - _x_9);
      boolean _greaterThan_6 = (_minus_7 > 0);
      if (_greaterThan_6) {
        double _y_6 = sourcePoint.getY();
        double _y_7 = targetPoint.getY();
        double _minus_8 = (_y_6 - _y_7);
        double _abs_2 = Math.abs(_minus_8);
        double _height = this.sourceAnchors.getHeight();
        double _plus_1 = (_height + this.STANDARD_DISTANCE);
        double _divide_2 = (_plus_1 / 2);
        boolean _greaterThan_7 = (_abs_2 > _divide_2);
        if (_greaterThan_7) {
          return Pair.<Side, Side>of(Side.RIGHT, Side.RIGHT);
        }
      } else {
        double _y_8 = sourcePoint.getY();
        double _y_9 = targetPoint.getY();
        double _minus_9 = (_y_8 - _y_9);
        double _abs_3 = Math.abs(_minus_9);
        double _height_1 = this.targetAnchors.getHeight();
        double _divide_3 = (_height_1 / 2);
        boolean _greaterThan_8 = (_abs_3 > _divide_3);
        if (_greaterThan_8) {
          return Pair.<Side, Side>of(Side.RIGHT, Side.RIGHT);
        }
      }
    }
    sourcePoint = this.sourceAnchors.getUnselected(Side.TOP);
    targetPoint = this.targetAnchors.getUnselected(Side.RIGHT);
    if (((!this.targetAnchors.contains(sourcePoint)) && (!this.sourceAnchors.contains(targetPoint)))) {
      return Pair.<Side, Side>of(Side.TOP, Side.RIGHT);
    }
    targetPoint = this.targetAnchors.getUnselected(Side.LEFT);
    if (((!this.targetAnchors.contains(sourcePoint)) && (!this.sourceAnchors.contains(targetPoint)))) {
      return Pair.<Side, Side>of(Side.TOP, Side.LEFT);
    }
    sourcePoint = this.sourceAnchors.getUnselected(Side.BOTTOM);
    targetPoint = this.targetAnchors.getUnselected(Side.RIGHT);
    if (((!this.targetAnchors.contains(sourcePoint)) && (!this.sourceAnchors.contains(targetPoint)))) {
      return Pair.<Side, Side>of(Side.BOTTOM, Side.RIGHT);
    }
    targetPoint = this.targetAnchors.getUnselected(Side.LEFT);
    if (((!this.targetAnchors.contains(sourcePoint)) && (!this.sourceAnchors.contains(targetPoint)))) {
      return Pair.<Side, Side>of(Side.BOTTOM, Side.LEFT);
    }
    return Pair.<Side, Side>of(Side.RIGHT, Side.LEFT);
  }
  
  @Pure
  public boolean isReroutingEnabled() {
    return this.reroutingEnabled;
  }
  
  public void setReroutingEnabled(final boolean reroutingEnabled) {
    this.reroutingEnabled = reroutingEnabled;
  }
}
