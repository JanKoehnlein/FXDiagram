package de.fxdiagram.lib.chooser;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.TransformExtensions;
import de.fxdiagram.lib.chooser.AbstractChoiceGraphics;
import javafx.geometry.Point3D;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * A 3D effect to select a candidate node from a list similar to Apple's cover flow.
 */
@SuppressWarnings("all")
public class CoverFlowChoice extends AbstractChoiceGraphics {
  @Accessors
  private double angle = 60;
  
  @Accessors
  private double deltaX = 20;
  
  private double gap;
  
  @Override
  public void setInterpolatedPosition(final double interpolatedPosition) {
    int _size = this.getChoiceNodes().size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      final Function1<XNode, Double> _function = (XNode it) -> {
        return Double.valueOf(it.getLayoutBounds().getWidth());
      };
      final Function2<Double, Double, Double> _function_1 = (Double a, Double b) -> {
        return Double.valueOf(DoubleExtensions.operator_plus(a, b));
      };
      Double _reduce = IterableExtensions.<Double>reduce(ListExtensions.<XNode, Double>map(this.getChoiceNodes(), _function), _function_1);
      int _size_1 = this.getChoiceNodes().size();
      double _divide = ((_reduce).doubleValue() / _size_1);
      this.gap = _divide;
      int _size_2 = this.getChoiceNodes().size();
      final int currentIndex = (((int) interpolatedPosition) % _size_2);
      int _xifexpression = (int) 0;
      int _size_3 = this.getChoiceNodes().size();
      int _minus = (_size_3 - 1);
      boolean _equals = (currentIndex == _minus);
      if (_equals) {
        _xifexpression = (currentIndex - 1);
      } else {
        _xifexpression = currentIndex;
      }
      final int leftIndex = _xifexpression;
      int _xifexpression_1 = (int) 0;
      int _size_4 = this.getChoiceNodes().size();
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
      int _size_5 = this.getChoiceNodes().size();
      ExclusiveRange _greaterThanDoubleDot = new ExclusiveRange(_size_5, (rightIndex + 1), false);
      for (final Integer i_1 : _greaterThanDoubleDot) {
        this.transformNode((i_1).intValue(), interpolatedPosition, false, 1);
      }
      this.transformNode(rightIndex, interpolatedPosition, false, (rightIndex - interpolatedPosition));
      this.transformNode(leftIndex, interpolatedPosition, true, (interpolatedPosition - leftIndex));
      if (((rightIndex > 0) && (Math.abs((leftIndex - interpolatedPosition)) > Math.abs((rightIndex - interpolatedPosition))))) {
        this.getChoiceNodes().get(rightIndex).toFront();
      }
    }
  }
  
  protected XNode transformNode(final int i, final double interpolatedPosition, final boolean isLeft, final double fraction) {
    XNode _xifexpression = null;
    if ((i >= 0)) {
      XNode _xblockexpression = null;
      {
        final XNode node = this.getChoiceNodes().get(i);
        final double distanceFromSelection = Math.abs((i - interpolatedPosition));
        final double opacity = (1 - (0.2 * distanceFromSelection));
        XNode _xifexpression_1 = null;
        if ((opacity < 0)) {
          node.setVisible(false);
        } else {
          XNode _xblockexpression_1 = null;
          {
            node.setVisible(true);
            final Affine trafo = new Affine();
            int _xifexpression_2 = (int) 0;
            if (isLeft) {
              _xifexpression_2 = (-1);
            } else {
              _xifexpression_2 = 1;
            }
            final int direction = _xifexpression_2;
            double _width = node.getLayoutBounds().getWidth();
            double _multiply = ((-0.5) * _width);
            double _height = node.getLayoutBounds().getHeight();
            double _multiply_1 = ((-0.5) * _height);
            TransformExtensions.translate(trafo, _multiply, _multiply_1, 0);
            Point3D _point3D = new Point3D(0, 1, 0);
            TransformExtensions.rotate(trafo, ((direction * this.angle) * fraction), _point3D);
            TransformExtensions.translate(trafo, (((i - interpolatedPosition) * this.deltaX) + (((0.5 * fraction) * direction) * this.gap)), 0, (-fraction));
            final Procedure1<XNode> _function = (XNode it) -> {
              node.getTransforms().setAll(trafo);
              node.toFront();
              ColorAdjust _colorAdjust = new ColorAdjust();
              final Procedure1<ColorAdjust> _function_1 = (ColorAdjust it_1) -> {
                Paint _backgroundPaint = this.getChooser().getDiagram().getBackgroundPaint();
                boolean _equals = Objects.equal(_backgroundPaint, Color.BLACK);
                if (_equals) {
                  it_1.setBrightness((opacity - 1));
                } else {
                  it_1.setBrightness((1 - opacity));
                }
                Reflection _reflection = new Reflection();
                it_1.setInput(_reflection);
              };
              ColorAdjust _doubleArrow = ObjectExtensions.<ColorAdjust>operator_doubleArrow(_colorAdjust, _function_1);
              node.setEffect(_doubleArrow);
            };
            _xblockexpression_1 = ObjectExtensions.<XNode>operator_doubleArrow(node, _function);
          }
          _xifexpression_1 = _xblockexpression_1;
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  @Override
  public boolean hasButtons() {
    return false;
  }
  
  @Pure
  public double getAngle() {
    return this.angle;
  }
  
  public void setAngle(final double angle) {
    this.angle = angle;
  }
  
  @Pure
  public double getDeltaX() {
    return this.deltaX;
  }
  
  public void setDeltaX(final double deltaX) {
    this.deltaX = deltaX;
  }
}
