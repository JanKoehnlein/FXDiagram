package de.fxdiagram.lib.tools;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.TransformExtensions;
import de.fxdiagram.core.tools.AbstractChooser;
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
public class CoverFlowChooser extends AbstractChooser {
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
  
  protected void doSetInterpolatedPosition(final double interpolatedPosition) {
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
        _xifexpression = (currentIndex - 1);
      } else {
        _xifexpression = currentIndex;
      }
      final int leftIndex = _xifexpression;
      int _xifexpression_1 = (int) 0;
      ArrayList<XNode> _nodes_5 = this.getNodes();
      int _size_4 = _nodes_5.size();
      int _minus_1 = (_size_4 - 1);
      boolean _equals_1 = (currentIndex == _minus_1);
      if (_equals_1) {
        _xifexpression_1 = currentIndex;
      } else {
        _xifexpression_1 = (currentIndex + 1);
      }
      final int rightIndex = _xifexpression_1;
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, leftIndex, true);
      for (final Integer i : _doubleDotLessThan) {
        this.transformNode((i).intValue(), interpolatedPosition, true, 1);
      }
      ArrayList<XNode> _nodes_6 = this.getNodes();
      int _size_5 = _nodes_6.size();
      ExclusiveRange _greaterThanDoubleDot = new ExclusiveRange(_size_5, (rightIndex + 1), false);
      for (final Integer i_1 : _greaterThanDoubleDot) {
        this.transformNode((i_1).intValue(), interpolatedPosition, false, 1);
      }
      this.transformNode(rightIndex, interpolatedPosition, false, (rightIndex - interpolatedPosition));
      this.transformNode(leftIndex, interpolatedPosition, true, (interpolatedPosition - leftIndex));
      boolean _and = false;
      if (!(rightIndex > 0)) {
        _and = false;
      } else {
        double _abs = Math.abs((leftIndex - interpolatedPosition));
        double _abs_1 = Math.abs((rightIndex - interpolatedPosition));
        boolean _greaterThan = (_abs > _abs_1);
        _and = ((rightIndex > 0) && _greaterThan);
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
    if ((i >= 0)) {
      XNode _xblockexpression = null;
      {
        ArrayList<XNode> _nodes = this.getNodes();
        final XNode node = _nodes.get(i);
        final double distanceFromSelection = Math.abs((i - interpolatedPosition));
        final double opacity = (1 - (0.2 * distanceFromSelection));
        XNode _xifexpression_1 = null;
        if ((opacity < 0)) {
          node.setVisible(false);
        } else {
          XNode _xblockexpression_1 = null;
          {
            node.setVisible(true);
            Affine _affine = new Affine();
            final Affine trafo = _affine;
            int _xifexpression_2 = (int) 0;
            if (isLeft) {
              _xifexpression_2 = (-1);
            } else {
              _xifexpression_2 = 1;
            }
            final int direction = _xifexpression_2;
            Bounds _layoutBounds = node.getLayoutBounds();
            double _width = _layoutBounds.getWidth();
            double _multiply = ((-0.5) * _width);
            Bounds _layoutBounds_1 = node.getLayoutBounds();
            double _height = _layoutBounds_1.getHeight();
            double _multiply_1 = ((-0.5) * _height);
            TransformExtensions.translate(trafo, _multiply, _multiply_1, 0);
            double _angle = this.getAngle();
            double _multiply_2 = (direction * _angle);
            double _multiply_3 = (_multiply_2 * fraction);
            Point3D _point3D = new Point3D(0, 1, 0);
            TransformExtensions.rotate(trafo, _multiply_3, _point3D);
            double _deltaX = this.getDeltaX();
            double _multiply_4 = ((i - interpolatedPosition) * _deltaX);
            double _plus = (_multiply_4 + (((0.5 * fraction) * direction) * this.gap));
            TransformExtensions.translate(trafo, _plus, 0, (-fraction));
            final Procedure1<XNode> _function = new Procedure1<XNode>() {
              public void apply(final XNode it) {
                ObservableList<Transform> _transforms = node.getTransforms();
                _transforms.setAll(trafo);
                node.toFront();
                ColorAdjust _colorAdjust = new ColorAdjust();
                final Procedure1<ColorAdjust> _function = new Procedure1<ColorAdjust>() {
                  public void apply(final ColorAdjust it) {
                    XDiagram _diagram = CoverFlowChooser.this.diagram();
                    Paint _backgroundPaint = _diagram.getBackgroundPaint();
                    boolean _equals = Objects.equal(_backgroundPaint, Color.BLACK);
                    if (_equals) {
                      it.setBrightness((opacity - 1));
                    } else {
                      it.setBrightness((1 - opacity));
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
