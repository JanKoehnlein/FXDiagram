package de.fxdiagram.lib.tools;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.TransformExtensions;
import de.fxdiagram.core.tools.AbstractXNodeChooser;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class CoverFlowChooser extends AbstractXNodeChooser {
  private double _angle = 60;
  
  public double getAngle() {
    return this._angle;
  }
  
  public void setAngle(final double angle) {
    this._angle = angle;
  }
  
  private double _deltaX = 20;
  
  public double getDeltaX() {
    return this._deltaX;
  }
  
  public void setDeltaX(final double deltaX) {
    this._deltaX = deltaX;
  }
  
  private double gap;
  
  public CoverFlowChooser(final XNode host, final Pos layoutPosition) {
    super(host, layoutPosition, false);
  }
  
  public boolean activate() {
    boolean _activate = super.activate();
    return _activate;
  }
  
  protected void setInterpolatedPosition(final double interpolatedPosition) {
    ArrayList<XNode> _nodes = this.getNodes();
    int _size = _nodes.size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      ArrayList<XNode> _nodes_1 = this.getNodes();
      final Function1<XNode,Double> _function = new Function1<XNode,Double>() {
        public Double apply(final XNode it) {
          Bounds _layoutBounds = it.getLayoutBounds();
          double _width = _layoutBounds.getWidth();
          return Double.valueOf(_width);
        }
      };
      List<Double> _map = ListExtensions.<XNode, Double>map(_nodes_1, _function);
      final Function2<Double,Double,Double> _function_1 = new Function2<Double,Double,Double>() {
        public Double apply(final Double a, final Double b) {
          double _plus = DoubleExtensions.operator_plus(a, b);
          return Double.valueOf(_plus);
        }
      };
      Double _reduce = IterableExtensions.<Double>reduce(_map, _function_1);
      ArrayList<XNode> _nodes_2 = this.getNodes();
      int _size_1 = _nodes_2.size();
      double _divide = ((_reduce).doubleValue() / _size_1);
      this.gap = _divide;
      ArrayList<XNode> _nodes_3 = this.getNodes();
      int _size_2 = _nodes_3.size();
      final int currentIndex = (((int) interpolatedPosition) % _size_2);
      int _xifexpression = (int) 0;
      ArrayList<XNode> _nodes_4 = this.getNodes();
      int _size_3 = _nodes_4.size();
      int _minus = (_size_3 - 1);
      boolean _equals = (currentIndex == _minus);
      if (_equals) {
        int _minus_1 = (currentIndex - 1);
        _xifexpression = _minus_1;
      } else {
        _xifexpression = currentIndex;
      }
      final int leftIndex = _xifexpression;
      int _xifexpression_1 = (int) 0;
      ArrayList<XNode> _nodes_5 = this.getNodes();
      int _size_4 = _nodes_5.size();
      int _minus_2 = (_size_4 - 1);
      boolean _equals_1 = (currentIndex == _minus_2);
      if (_equals_1) {
        _xifexpression_1 = currentIndex;
      } else {
        int _plus = (currentIndex + 1);
        _xifexpression_1 = _plus;
      }
      final int rightIndex = _xifexpression_1;
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, leftIndex, true);
      for (final Integer i : _doubleDotLessThan) {
        this.transformNode((i).intValue(), interpolatedPosition, true, 1);
      }
      ArrayList<XNode> _nodes_6 = this.getNodes();
      int _size_5 = _nodes_6.size();
      int _plus_1 = (rightIndex + 1);
      ExclusiveRange _greaterThanDoubleDot = new ExclusiveRange(_size_5, _plus_1, false);
      for (final Integer i_1 : _greaterThanDoubleDot) {
        this.transformNode((i_1).intValue(), interpolatedPosition, false, 1);
      }
      double _minus_3 = (rightIndex - interpolatedPosition);
      this.transformNode(rightIndex, interpolatedPosition, false, _minus_3);
      double _minus_4 = (interpolatedPosition - leftIndex);
      this.transformNode(leftIndex, interpolatedPosition, true, _minus_4);
      boolean _and = false;
      boolean _greaterThan = (rightIndex > 0);
      if (!_greaterThan) {
        _and = false;
      } else {
        double _minus_5 = (leftIndex - interpolatedPosition);
        double _abs = Math.abs(_minus_5);
        double _minus_6 = (rightIndex - interpolatedPosition);
        double _abs_1 = Math.abs(_minus_6);
        boolean _greaterThan_1 = (_abs > _abs_1);
        _and = (_greaterThan && _greaterThan_1);
      }
      if (_and) {
        ArrayList<XNode> _nodes_7 = this.getNodes();
        XNode _get = _nodes_7.get(rightIndex);
        _get.toFront();
      }
    }
  }
  
  protected XNode transformNode(final int i, final double interpolatedPosition, final boolean isLeft, final double fraction) {
    XNode _xifexpression = null;
    boolean _greaterEqualsThan = (i >= 0);
    if (_greaterEqualsThan) {
      XNode _xblockexpression = null;
      {
        ArrayList<XNode> _nodes = this.getNodes();
        final XNode node = _nodes.get(i);
        double _minus = (i - interpolatedPosition);
        final double distanceFromSelection = Math.abs(_minus);
        double _multiply = (0.2 * distanceFromSelection);
        final double opacity = (1 - _multiply);
        XNode _xifexpression_1 = null;
        boolean _lessThan = (opacity < 0);
        if (_lessThan) {
          node.setVisible(false);
        } else {
          XNode _xblockexpression_1 = null;
          {
            node.setVisible(true);
            Affine _affine = new Affine();
            final Affine trafo = _affine;
            int _xifexpression_2 = (int) 0;
            if (isLeft) {
              int _minus_1 = (-1);
              _xifexpression_2 = _minus_1;
            } else {
              _xifexpression_2 = 1;
            }
            final int direction = _xifexpression_2;
            double _minus_2 = (-0.5);
            Bounds _layoutBounds = node.getLayoutBounds();
            double _width = _layoutBounds.getWidth();
            double _multiply_1 = (_minus_2 * _width);
            double _minus_3 = (-0.5);
            Bounds _layoutBounds_1 = node.getLayoutBounds();
            double _height = _layoutBounds_1.getHeight();
            double _multiply_2 = (_minus_3 * _height);
            TransformExtensions.translate(trafo, _multiply_1, _multiply_2, 0);
            double _angle = this.getAngle();
            double _multiply_3 = (direction * _angle);
            double _multiply_4 = (_multiply_3 * fraction);
            Point3D _point3D = new Point3D(0, 1, 0);
            TransformExtensions.rotate(trafo, _multiply_4, _point3D);
            double _minus_4 = (i - interpolatedPosition);
            double _deltaX = this.getDeltaX();
            double _multiply_5 = (_minus_4 * _deltaX);
            double _multiply_6 = (0.5 * fraction);
            double _multiply_7 = (_multiply_6 * direction);
            double _multiply_8 = (_multiply_7 * this.gap);
            double _plus = (_multiply_5 + _multiply_8);
            double _minus_5 = (-fraction);
            TransformExtensions.translate(trafo, _plus, 0, _minus_5);
            final Procedure1<XNode> _function = new Procedure1<XNode>() {
              public void apply(final XNode it) {
                ObservableList<Transform> _transforms = node.getTransforms();
                _transforms.setAll(trafo);
                node.toFront();
                ColorAdjust _colorAdjust = new ColorAdjust();
                final Procedure1<ColorAdjust> _function = new Procedure1<ColorAdjust>() {
                  public void apply(final ColorAdjust it) {
                    XDiagram _diagram = CoverFlowChooser.this.getDiagram();
                    Paint _backgroundPaint = _diagram.getBackgroundPaint();
                    boolean _equals = Objects.equal(_backgroundPaint, Color.BLACK);
                    if (_equals) {
                      double _minus = (opacity - 1);
                      it.setBrightness(_minus);
                    } else {
                      double _minus_1 = (1 - opacity);
                      it.setBrightness(_minus_1);
                    }
                    Reflection _reflection = new Reflection();
                    it.setInput(_reflection);
                  }
                };
                ColorAdjust _doubleArrow = ObjectExtensions.<ColorAdjust>operator_doubleArrow(_colorAdjust, _function);
                node.setEffect(_doubleArrow);
              }
            };
            XNode _doubleArrow = ObjectExtensions.<XNode>operator_doubleArrow(node, _function);
            _xblockexpression_1 = (_doubleArrow);
          }
          _xifexpression_1 = _xblockexpression_1;
        }
        _xblockexpression = (_xifexpression_1);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
}
