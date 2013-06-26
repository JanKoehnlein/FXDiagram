package de.fxdiagram.core.tools.chooser;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.tools.chooser.AbstractXNodeChooser;
import de.fxdiagram.core.transform.TransformExtensions;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
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
  
  public CubeChooser(final XNode host, final Point2D position) {
    super(host, position);
  }
  
  public boolean activate() {
    boolean _activate = super.activate();
    return _activate;
  }
  
  protected void setInterpolatedPosition(final double interpolatedPosition) {
    ArrayList<XNode> _nodes = this.getNodes();
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
    final double maxWidth = ((_fold).doubleValue() + _spacing);
    double _minus = (interpolatedPosition - ((int) interpolatedPosition));
    final double angle = (_minus * 90);
    ArrayList<XNode> _nodes_1 = this.getNodes();
    int _size = _nodes_1.size();
    final int leftNodeIndex = (((int) interpolatedPosition) % _size);
    this.applyTransform(leftNodeIndex, angle, maxWidth);
    int _plus = (((int) interpolatedPosition) + 1);
    ArrayList<XNode> _nodes_2 = this.getNodes();
    int _size_1 = _nodes_2.size();
    final int rightNodeIndex = (_plus % _size_1);
    double _minus_1 = (angle - 90);
    this.applyTransform(rightNodeIndex, _minus_1, maxWidth);
    ArrayList<XNode> _nodes_3 = this.getNodes();
    final Procedure2<XNode,Integer> _function_1 = new Procedure2<XNode,Integer>() {
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
    IterableExtensions.<XNode>forEach(_nodes_3, _function_1);
  }
  
  protected Boolean applyTransform(final int nodeIndex, final double angle, final double maxWidth) {
    boolean _xblockexpression = false;
    {
      ArrayList<XNode> _nodes = this.getNodes();
      final XNode node = _nodes.get(nodeIndex);
      boolean _xifexpression = false;
      double _abs = Math.abs(angle);
      boolean _greaterThan = (_abs > 86);
      if (_greaterThan) {
        node.setVisible(false);
      } else {
        boolean _xblockexpression_1 = false;
        {
          Bounds _layoutBounds = node.getLayoutBounds();
          final double width = _layoutBounds.getWidth();
          Bounds _layoutBounds_1 = node.getLayoutBounds();
          final double height = _layoutBounds_1.getHeight();
          Affine _affine = new Affine();
          final Affine transform = _affine;
          double _minus = (-0.5);
          double _multiply = (_minus * width);
          double _minus_1 = (-0.5);
          double _multiply_1 = (_minus_1 * height);
          double _minus_2 = (-maxWidth);
          double _multiply_2 = (_minus_2 * 0.5);
          TransformExtensions.translate(transform, _multiply, _multiply_1, _multiply_2);
          Point3D _point3D = new Point3D(0, 1, 0);
          TransformExtensions.rotate(transform, angle, _point3D);
          double _multiply_3 = (maxWidth * 0.5);
          TransformExtensions.translate(transform, 0, 0, _multiply_3);
          node.setVisible(true);
          ObservableList<Transform> _transforms = node.getTransforms();
          _transforms.clear();
          ObservableList<Transform> _transforms_1 = node.getTransforms();
          boolean _add = _transforms_1.add(transform);
          _xblockexpression_1 = (_add);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
}
