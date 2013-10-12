package de.fxdiagram.lib.tools;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.TransformExtensions;
import de.fxdiagram.core.tools.AbstractChooser;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class CubeChooser extends AbstractChooser {
  private double _spacing = 6.0;
  
  public double getSpacing() {
    return this._spacing;
  }
  
  public void setSpacing(final double spacing) {
    this._spacing = spacing;
  }
  
  private double maxWidth;
  
  public CubeChooser(final XNode host, final Pos layoutPosition) {
    super(host, layoutPosition, true);
  }
  
  public boolean activate() {
    boolean _activate = super.activate();
    return _activate;
  }
  
  protected void doSetInterpolatedPosition(final double interpolatedPosition) {
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
    double _plus = ((_fold).doubleValue() + _spacing);
    this.maxWidth = _plus;
    double _minus = (interpolatedPosition - ((int) interpolatedPosition));
    final double angle = (_minus * 90);
    ArrayList<XNode> _nodes_1 = this.getNodes();
    int _size = _nodes_1.size();
    final int leftNodeIndex = (((int) interpolatedPosition) % _size);
    this.applyTransform(leftNodeIndex, angle);
    int _plus_1 = (((int) interpolatedPosition) + 1);
    ArrayList<XNode> _nodes_2 = this.getNodes();
    int _size_1 = _nodes_2.size();
    final int rightNodeIndex = (_plus_1 % _size_1);
    double _minus_1 = (angle - 90);
    this.applyTransform(rightNodeIndex, _minus_1);
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
  
  protected Boolean applyTransform(final int nodeIndex, final double angle) {
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
          double _minus_2 = (-this.maxWidth);
          double _multiply_2 = (_minus_2 * 0.5);
          TransformExtensions.translate(transform, _multiply, _multiply_1, _multiply_2);
          Point3D _point3D = new Point3D(0, 1, 0);
          TransformExtensions.rotate(transform, angle, _point3D);
          double _multiply_3 = (this.maxWidth * 0.5);
          TransformExtensions.translate(transform, 0, 0, _multiply_3);
          node.setVisible(true);
          ObservableList<Transform> _transforms = node.getTransforms();
          boolean _setAll = _transforms.setAll(transform);
          _xblockexpression_1 = (_setAll);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public void relocateButtons(final Node minusButton, final Node plusButton) {
    double _multiply = (0.5 * this.maxWidth);
    double _sqrt = Math.sqrt(2);
    final double groupMaxWidthHalf = (_multiply * _sqrt);
    Group _group = this.getGroup();
    double _layoutX = _group.getLayoutX();
    double _plus = (_layoutX + groupMaxWidthHalf);
    minusButton.setLayoutX(_plus);
    Group _group_1 = this.getGroup();
    double _layoutY = _group_1.getLayoutY();
    Bounds _layoutBounds = minusButton.getLayoutBounds();
    double _height = _layoutBounds.getHeight();
    double _multiply_1 = (0.5 * _height);
    double _minus = (_layoutY - _multiply_1);
    minusButton.setLayoutY(_minus);
    Group _group_2 = this.getGroup();
    double _layoutX_1 = _group_2.getLayoutX();
    double _minus_1 = (_layoutX_1 - groupMaxWidthHalf);
    Bounds _layoutBounds_1 = plusButton.getLayoutBounds();
    double _width = _layoutBounds_1.getWidth();
    double _minus_2 = (_minus_1 - _width);
    plusButton.setLayoutX(_minus_2);
    Group _group_3 = this.getGroup();
    double _layoutY_1 = _group_3.getLayoutY();
    Bounds _layoutBounds_2 = plusButton.getLayoutBounds();
    double _height_1 = _layoutBounds_2.getHeight();
    double _multiply_2 = (0.5 * _height_1);
    double _minus_3 = (_layoutY_1 - _multiply_2);
    plusButton.setLayoutY(_minus_3);
  }
}
