package de.itemis.javafx.diagram.tools.chooser;

import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.tools.chooser.AbstractXNodeChooser;
import de.itemis.javafx.diagram.transform.PerspectiveExtensions;
import de.itemis.javafx.diagram.transform.TransformExtensions;
import java.util.ArrayList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.transform.Affine;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;

@SuppressWarnings("all")
public class CoverFlowChooser extends AbstractXNodeChooser {
  private double _angle = 60;
  
  public double getAngle() {
    return this._angle;
  }
  
  public void setAngle(final double angle) {
    this._angle = angle;
  }
  
  private double _deltaZ = 70;
  
  public double getDeltaZ() {
    return this._deltaZ;
  }
  
  public void setDeltaZ(final double deltaZ) {
    this._deltaZ = deltaZ;
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
      ArrayList<XNode> _nodes_2 = this.getNodes();
      XNode _get = _nodes_2.get((i).intValue());
      this.transform(_get, (i).intValue(), interpolatedPosition, true, 1);
    }
    ArrayList<XNode> _nodes_3 = this.getNodes();
    int _size_2 = _nodes_3.size();
    int _plus_1 = (rightIndex + 1);
    ExclusiveRange _greaterThanDoubleDot = new ExclusiveRange(_size_2, _plus_1, false);
    for (final Integer i_1 : _greaterThanDoubleDot) {
      ArrayList<XNode> _nodes_4 = this.getNodes();
      XNode _get_1 = _nodes_4.get((i_1).intValue());
      this.transform(_get_1, (i_1).intValue(), interpolatedPosition, false, 1);
    }
    ArrayList<XNode> _nodes_5 = this.getNodes();
    XNode _get_2 = _nodes_5.get(rightIndex);
    double _minus = (rightIndex - interpolatedPosition);
    this.transform(_get_2, rightIndex, interpolatedPosition, false, _minus);
    ArrayList<XNode> _nodes_6 = this.getNodes();
    XNode _get_3 = _nodes_6.get(leftIndex);
    double _minus_1 = (interpolatedPosition - leftIndex);
    this.transform(_get_3, leftIndex, interpolatedPosition, true, _minus_1);
  }
  
  protected void transform(final XNode node, final int i, final double interpolatedPosition, final boolean isLeft, final double fraction) {
    double _minus = (i - interpolatedPosition);
    final double distanceFromSelection = Math.abs(_minus);
    double _multiply = (0.2 * distanceFromSelection);
    final double opacity = (1 - _multiply);
    boolean _lessThan = (opacity < 0);
    if (_lessThan) {
      node.setVisible(false);
    } else {
      node.setOpacity(opacity);
      node.setVisible(true);
    }
    boolean _lessThan_1 = (distanceFromSelection < 1E-5);
    if (_lessThan_1) {
      ArrayList<XNode> _nodes = this.getNodes();
      XNode _get = _nodes.get(i);
      _get.setEffect(null);
      double _minus_1 = (-0.5);
      Bounds _layoutBounds = node.getLayoutBounds();
      double _width = _layoutBounds.getWidth();
      double _multiply_1 = (_minus_1 * _width);
      node.setLayoutX(_multiply_1);
      double _minus_2 = (-0.5);
      Bounds _layoutBounds_1 = node.getLayoutBounds();
      double _height = _layoutBounds_1.getHeight();
      double _multiply_2 = (_minus_2 * _height);
      node.setLayoutY(_multiply_2);
    } else {
      node.setLayoutX(0);
      node.setLayoutY(0);
      Affine _affine = new Affine();
      final Affine trafo = _affine;
      int _xifexpression = (int) 0;
      if (isLeft) {
        int _minus_3 = (-1);
        _xifexpression = _minus_3;
      } else {
        _xifexpression = 1;
      }
      final int direction = _xifexpression;
      double _minus_4 = (-0.5);
      ArrayList<XNode> _nodes_1 = this.getNodes();
      XNode _get_1 = _nodes_1.get(i);
      Bounds _layoutBounds_2 = _get_1.getLayoutBounds();
      double _width_1 = _layoutBounds_2.getWidth();
      double _multiply_3 = (_minus_4 * _width_1);
      double _minus_5 = (-0.5);
      ArrayList<XNode> _nodes_2 = this.getNodes();
      XNode _get_2 = _nodes_2.get(i);
      Bounds _layoutBounds_3 = _get_2.getLayoutBounds();
      double _height_1 = _layoutBounds_3.getHeight();
      double _multiply_4 = (_minus_5 * _height_1);
      TransformExtensions.translate(trafo, _multiply_3, _multiply_4, 0);
      double _angle = this.getAngle();
      double _multiply_5 = (direction * _angle);
      double _multiply_6 = (_multiply_5 * fraction);
      Point3D _point3D = new Point3D(0, 1, 0);
      TransformExtensions.rotate(trafo, _multiply_6, _point3D);
      double _minus_6 = (i - interpolatedPosition);
      double _deltaX = this.getDeltaX();
      double _multiply_7 = (_minus_6 * _deltaX);
      double _multiply_8 = (0.5 * fraction);
      double _multiply_9 = (_multiply_8 * direction);
      double _gap = this.getGap();
      double _multiply_10 = (_multiply_9 * _gap);
      double _plus = (_multiply_7 + _multiply_10);
      double _deltaZ = this.getDeltaZ();
      double _multiply_11 = (fraction * _deltaZ);
      double _plus_1 = (_multiply_11 + 200);
      TransformExtensions.translate(trafo, _plus, 0, _plus_1);
      ArrayList<XNode> _nodes_3 = this.getNodes();
      XNode _get_3 = _nodes_3.get(i);
      ArrayList<XNode> _nodes_4 = this.getNodes();
      XNode _get_4 = _nodes_4.get(i);
      Bounds _layoutBounds_4 = _get_4.getLayoutBounds();
      PerspectiveTransform _mapPerspective = PerspectiveExtensions.mapPerspective(_layoutBounds_4, trafo, 200);
      _get_3.setEffect(_mapPerspective);
    }
    ArrayList<XNode> _nodes_5 = this.getNodes();
    XNode _get_5 = _nodes_5.get(i);
    _get_5.toFront();
  }
}
