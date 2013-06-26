package de.fxdiagram.core.tools.chooser;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.tools.chooser.AbstractXNodeChooser;
import de.fxdiagram.core.transform.TransformExtensions;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Reflection;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
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
  
  private double _gap = 120;
  
  public double getGap() {
    return this._gap;
  }
  
  public void setGap(final double gap) {
    this._gap = gap;
  }
  
  public CoverFlowChooser(final XNode host, final Point2D position) {
    super(host, position);
  }
  
  public boolean activate() {
    boolean _activate = super.activate();
    return _activate;
  }
  
  protected void setInterpolatedPosition(final double interpolatedPosition) {
    ArrayList<XNode> _nodes = this.getNodes();
    int _size = _nodes.size();
    final int leftIndex = (((int) interpolatedPosition) % _size);
    int _plus = (leftIndex + 1);
    ArrayList<XNode> _nodes_1 = this.getNodes();
    int _size_1 = _nodes_1.size();
    final int rightIndex = (_plus % _size_1);
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, leftIndex, true);
    for (final Integer i : _doubleDotLessThan) {
      this.transformNode((i).intValue(), interpolatedPosition, true, 1);
    }
    ArrayList<XNode> _nodes_2 = this.getNodes();
    int _size_2 = _nodes_2.size();
    int _plus_1 = (rightIndex + 1);
    ExclusiveRange _greaterThanDoubleDot = new ExclusiveRange(_size_2, _plus_1, false);
    for (final Integer i_1 : _greaterThanDoubleDot) {
      this.transformNode((i_1).intValue(), interpolatedPosition, false, 1);
    }
    double _minus = (rightIndex - interpolatedPosition);
    this.transformNode(rightIndex, interpolatedPosition, false, _minus);
    double _minus_1 = (interpolatedPosition - leftIndex);
    this.transformNode(leftIndex, interpolatedPosition, true, _minus_1);
  }
  
  protected void transformNode(final int i, final double interpolatedPosition, final boolean isLeft, final double fraction) {
    ArrayList<XNode> _nodes = this.getNodes();
    final XNode node = _nodes.get(i);
    double _minus = (i - interpolatedPosition);
    final double distanceFromSelection = Math.abs(_minus);
    double _multiply = (0.2 * distanceFromSelection);
    final double opacity = (1 - _multiply);
    boolean _lessThan = (opacity < 0);
    if (_lessThan) {
      node.setVisible(false);
    } else {
      node.setVisible(true);
      Affine _affine = new Affine();
      final Affine trafo = _affine;
      int _xifexpression = (int) 0;
      if (isLeft) {
        int _minus_1 = (-1);
        _xifexpression = _minus_1;
      } else {
        _xifexpression = 1;
      }
      final int direction = _xifexpression;
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
      double _gap = this.getGap();
      double _multiply_8 = (_multiply_7 * _gap);
      double _plus = (_multiply_5 + _multiply_8);
      double _minus_5 = (-fraction);
      TransformExtensions.translate(trafo, _plus, 0, _minus_5);
      ObservableList<Transform> _transforms = node.getTransforms();
      _transforms.clear();
      ObservableList<Transform> _transforms_1 = node.getTransforms();
      _transforms_1.add(trafo);
      node.toFront();
      ColorAdjust _colorAdjust = new ColorAdjust();
      final Procedure1<ColorAdjust> _function = new Procedure1<ColorAdjust>() {
          public void apply(final ColorAdjust it) {
            double _minus = (1 - opacity);
            it.setBrightness(_minus);
            Reflection _reflection = new Reflection();
            it.setInput(_reflection);
          }
        };
      ColorAdjust _doubleArrow = ObjectExtensions.<ColorAdjust>operator_doubleArrow(_colorAdjust, _function);
      node.setEffect(_doubleArrow);
    }
  }
}
