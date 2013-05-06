package de.itemis.javafx.diagram.tools.chooser;

import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.tools.chooser.AbstractXNodeChooser;
import de.itemis.javafx.diagram.transform.PerspectiveExtensions;
import de.itemis.javafx.diagram.transform.TransformExtensions;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.transform.Affine;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

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
  
  private double maxWidth;
  
  public CoverFlowChooser(final XNode host, final Point2D position) {
    super(host, position);
  }
  
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      List<XNode> _nodes = this.getNodes();
      final Function2<Double,XNode,Double> _function = new Function2<Double,XNode,Double>() {
          public Double apply(final Double a, final XNode b) {
            Bounds _layoutBounds = b.getLayoutBounds();
            double _width = _layoutBounds.getWidth();
            double _max = Math.max((a).doubleValue(), _width);
            return Double.valueOf(_max);
          }
        };
      Double _fold = IterableExtensions.<XNode, Double>fold(_nodes, Double.valueOf(0.0), _function);
      this.maxWidth = (_fold).doubleValue();
      boolean _activate = super.activate();
      _xblockexpression = (_activate);
    }
    return _xblockexpression;
  }
  
  protected void setInterpolatedPosition(final double interpolatedPosition) {
    List<XNode> _nodes = this.getNodes();
    int _size = _nodes.size();
    final int leftIndex = (((int) interpolatedPosition) % _size);
    int _plus = (leftIndex + 1);
    List<XNode> _nodes_1 = this.getNodes();
    int _size_1 = _nodes_1.size();
    final int rightIndex = (_plus % _size_1);
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, leftIndex, true);
    for (final Integer i : _doubleDotLessThan) {
      List<XNode> _nodes_2 = this.getNodes();
      XNode _get = _nodes_2.get((i).intValue());
      this.transform(_get, (i).intValue(), interpolatedPosition, true, 1);
    }
    List<XNode> _nodes_3 = this.getNodes();
    int _size_2 = _nodes_3.size();
    int _plus_1 = (rightIndex + 1);
    ExclusiveRange _greaterThanDoubleDot = new ExclusiveRange(_size_2, _plus_1, false);
    for (final Integer i_1 : _greaterThanDoubleDot) {
      List<XNode> _nodes_4 = this.getNodes();
      XNode _get_1 = _nodes_4.get((i_1).intValue());
      this.transform(_get_1, (i_1).intValue(), interpolatedPosition, false, 1);
    }
    List<XNode> _nodes_5 = this.getNodes();
    XNode _get_2 = _nodes_5.get(rightIndex);
    double _minus = (rightIndex - interpolatedPosition);
    this.transform(_get_2, rightIndex, interpolatedPosition, false, _minus);
    List<XNode> _nodes_6 = this.getNodes();
    XNode _get_3 = _nodes_6.get(leftIndex);
    double _minus_1 = (interpolatedPosition - leftIndex);
    this.transform(_get_3, leftIndex, interpolatedPosition, true, _minus_1);
  }
  
  protected void transform(final XNode node, final int i, final double interpolatedPosition, final boolean isLeft, final double fraction) {
    double _minus = (i - interpolatedPosition);
    double _abs = Math.abs(_minus);
    double _multiply = (0.2 * _abs);
    final double opacity = (1 - _multiply);
    boolean _lessThan = (opacity < 0);
    if (_lessThan) {
      node.setVisible(false);
    } else {
      node.setOpacity(opacity);
      node.setVisible(true);
    }
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
    List<XNode> _nodes = this.getNodes();
    XNode _get = _nodes.get(i);
    Bounds _layoutBounds = _get.getLayoutBounds();
    double _width = _layoutBounds.getWidth();
    double _multiply_1 = (_minus_2 * _width);
    double _minus_3 = (-0.5);
    List<XNode> _nodes_1 = this.getNodes();
    XNode _get_1 = _nodes_1.get(i);
    Bounds _layoutBounds_1 = _get_1.getLayoutBounds();
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
    double _deltaZ = this.getDeltaZ();
    double _multiply_9 = (fraction * _deltaZ);
    double _plus_1 = (_multiply_9 + 200);
    TransformExtensions.translate(trafo, _plus, 0, _plus_1);
    List<XNode> _nodes_2 = this.getNodes();
    XNode _get_2 = _nodes_2.get(i);
    List<XNode> _nodes_3 = this.getNodes();
    XNode _get_3 = _nodes_3.get(i);
    Bounds _layoutBounds_2 = _get_3.getLayoutBounds();
    PerspectiveTransform _mapPerspective = PerspectiveExtensions.mapPerspective(_layoutBounds_2, trafo, 200);
    _get_2.setEffect(_mapPerspective);
    List<XNode> _nodes_4 = this.getNodes();
    XNode _get_4 = _nodes_4.get(i);
    _get_4.toFront();
  }
}
