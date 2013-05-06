package de.itemis.javafx.diagram.tools.chooser;

import com.google.common.base.Objects;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.tools.chooser.AbstractXNodeChooser;
import de.itemis.javafx.diagram.transform.PerspectiveExtensions;
import de.itemis.javafx.diagram.transform.TransformExtensions;
import java.util.List;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.transform.Affine;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class CubeChooser extends AbstractXNodeChooser {
  private double _spacing = 6.0;
  
  public double getSpacing() {
    return this._spacing;
  }
  
  public void setSpacing(final double spacing) {
    this._spacing = spacing;
  }
  
  private double _distance = 250.0;
  
  public double getDistance() {
    return this._distance;
  }
  
  public void setDistance(final double distance) {
    this._distance = distance;
  }
  
  private double _screenDistance = 250.0;
  
  public double getScreenDistance() {
    return this._screenDistance;
  }
  
  public void setScreenDistance(final double screenDistance) {
    this._screenDistance = screenDistance;
  }
  
  private double maxWidth;
  
  public CubeChooser(final XNode host, final Point2D position) {
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
      double _spacing = this.getSpacing();
      double _plus = ((_fold).doubleValue() + _spacing);
      this.maxWidth = _plus;
      boolean _activate = super.activate();
      _xblockexpression = (_activate);
    }
    return _xblockexpression;
  }
  
  protected void setInterpolatedPosition(final double interpolatedPosition) {
    double _minus = (interpolatedPosition - ((int) interpolatedPosition));
    final double angle = (_minus * 90);
    List<XNode> _nodes = this.getNodes();
    int _size = _nodes.size();
    final int leftNodeIndex = (((int) interpolatedPosition) % _size);
    this.applyTransform(leftNodeIndex, angle);
    int _plus = (((int) interpolatedPosition) + 1);
    List<XNode> _nodes_1 = this.getNodes();
    int _size_1 = _nodes_1.size();
    final int rightNodeIndex = (_plus % _size_1);
    double _minus_1 = (angle - 90);
    this.applyTransform(rightNodeIndex, _minus_1);
    List<XNode> _nodes_2 = this.getNodes();
    final Procedure2<XNode,Integer> _function = new Procedure2<XNode,Integer>() {
        public void apply(final XNode node, final Integer i) {
          boolean _and = false;
          boolean _notEquals = (i != leftNodeIndex);
          if (!_notEquals) {
            _and = false;
          } else {
            boolean _notEquals_1 = (i != rightNodeIndex);
            _and = (_notEquals && _notEquals_1);
          }
          if (_and) {
            node.setVisible(false);
          }
        }
      };
    IterableExtensions.<XNode>forEach(_nodes_2, _function);
  }
  
  protected void applyTransform(final int nodeIndex, final double angle) {
    List<XNode> _nodes = this.getNodes();
    final XNode node = _nodes.get(nodeIndex);
    Affine _affine = new Affine();
    final Affine transform = _affine;
    double _minus = (-this.maxWidth);
    double _multiply = (_minus * 0.5);
    TransformExtensions.translate(transform, 0, 0, _multiply);
    Point3D _point3D = new Point3D(0, 1, 0);
    TransformExtensions.rotate(transform, angle, _point3D);
    double _multiply_1 = (this.maxWidth * 0.5);
    double _plus = (200 + _multiply_1);
    TransformExtensions.translate(transform, 0, 0, _plus);
    Bounds _layoutBounds = node.getLayoutBounds();
    final double width = _layoutBounds.getWidth();
    Bounds _layoutBounds_1 = node.getLayoutBounds();
    final double height = _layoutBounds_1.getHeight();
    double _minus_1 = (-0.5);
    double _multiply_2 = (_minus_1 * width);
    double _minus_2 = (-0.5);
    double _multiply_3 = (_minus_2 * height);
    BoundingBox _boundingBox = new BoundingBox(_multiply_2, _multiply_3, width, height);
    final PerspectiveTransform perspectiveTransform = PerspectiveExtensions.mapPerspective(_boundingBox, transform, 200);
    boolean _notEquals = (!Objects.equal(perspectiveTransform, null));
    node.setVisible(_notEquals);
    node.setEffect(perspectiveTransform);
  }
}
