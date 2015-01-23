package de.fxdiagram.lib.chooser;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.TransformExtensions;
import de.fxdiagram.lib.chooser.AbstractBaseChooser;
import de.fxdiagram.lib.chooser.AbstractChoiceGraphics;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
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

@SuppressWarnings("all")
public class CoverFlowChoice extends AbstractChoiceGraphics {
  @Accessors
  private double angle = 60;
  
  @Accessors
  private double deltaX = 20;
  
  private double gap;
  
  @Override
  public void setInterpolatedPosition(final double interpolatedPosition) {
    ArrayList<XNode> _choiceNodes = this.getChoiceNodes();
    int _size = _choiceNodes.size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      ArrayList<XNode> _choiceNodes_1 = this.getChoiceNodes();
      final Function1<XNode, Double> _function = new Function1<XNode, Double>() {
        @Override
        public Double apply(final XNode it) {
          Bounds _layoutBounds = it.getLayoutBounds();
          return Double.valueOf(_layoutBounds.getWidth());
        }
      };
      List<Double> _map = ListExtensions.<XNode, Double>map(_choiceNodes_1, _function);
      final Function2<Double, Double, Double> _function_1 = new Function2<Double, Double, Double>() {
        @Override
        public Double apply(final Double a, final Double b) {
          return Double.valueOf(DoubleExtensions.operator_plus(a, b));
        }
      };
      Double _reduce = IterableExtensions.<Double>reduce(_map, _function_1);
      ArrayList<XNode> _choiceNodes_2 = this.getChoiceNodes();
      int _size_1 = _choiceNodes_2.size();
      double _divide = ((_reduce).doubleValue() / _size_1);
      this.gap = _divide;
      ArrayList<XNode> _choiceNodes_3 = this.getChoiceNodes();
      int _size_2 = _choiceNodes_3.size();
      final int currentIndex = (((int) interpolatedPosition) % _size_2);
      int _xifexpression = (int) 0;
      ArrayList<XNode> _choiceNodes_4 = this.getChoiceNodes();
      int _size_3 = _choiceNodes_4.size();
      int _minus = (_size_3 - 1);
      boolean _equals = (currentIndex == _minus);
      if (_equals) {
        _xifexpression = (currentIndex - 1);
      } else {
        _xifexpression = currentIndex;
      }
      final int leftIndex = _xifexpression;
      int _xifexpression_1 = (int) 0;
      ArrayList<XNode> _choiceNodes_5 = this.getChoiceNodes();
      int _size_4 = _choiceNodes_5.size();
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
      ArrayList<XNode> _choiceNodes_6 = this.getChoiceNodes();
      int _size_5 = _choiceNodes_6.size();
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
        _and = _greaterThan;
      }
      if (_and) {
        ArrayList<XNode> _choiceNodes_7 = this.getChoiceNodes();
        XNode _get = _choiceNodes_7.get(rightIndex);
        _get.toFront();
      }
    }
  }
  
  protected XNode transformNode(final int i, final double interpolatedPosition, final boolean isLeft, final double fraction) {
    XNode _xifexpression = null;
    if ((i >= 0)) {
      XNode _xblockexpression = null;
      {
        ArrayList<XNode> _choiceNodes = this.getChoiceNodes();
        final XNode node = _choiceNodes.get(i);
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
            Bounds _layoutBounds = node.getLayoutBounds();
            double _width = _layoutBounds.getWidth();
            double _multiply = ((-0.5) * _width);
            Bounds _layoutBounds_1 = node.getLayoutBounds();
            double _height = _layoutBounds_1.getHeight();
            double _multiply_1 = ((-0.5) * _height);
            TransformExtensions.translate(trafo, _multiply, _multiply_1, 0);
            Point3D _point3D = new Point3D(0, 1, 0);
            TransformExtensions.rotate(trafo, ((direction * this.angle) * fraction), _point3D);
            TransformExtensions.translate(trafo, (((i - interpolatedPosition) * this.deltaX) + (((0.5 * fraction) * direction) * this.gap)), 0, (-fraction));
            final Procedure1<XNode> _function = new Procedure1<XNode>() {
              @Override
              public void apply(final XNode it) {
                ObservableList<Transform> _transforms = node.getTransforms();
                _transforms.setAll(trafo);
                node.toFront();
                ColorAdjust _colorAdjust = new ColorAdjust();
                final Procedure1<ColorAdjust> _function = new Procedure1<ColorAdjust>() {
                  @Override
                  public void apply(final ColorAdjust it) {
                    AbstractBaseChooser _chooser = CoverFlowChoice.this.getChooser();
                    XDiagram _diagram = _chooser.getDiagram();
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
