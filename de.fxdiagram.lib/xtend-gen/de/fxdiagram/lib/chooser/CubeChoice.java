package de.fxdiagram.lib.chooser;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.TransformExtensions;
import de.fxdiagram.lib.chooser.AbstractChoiceGraphics;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * A 3D effect to choose a candidate from a list by rotating a cube.
 */
@SuppressWarnings("all")
public class CubeChoice extends AbstractChoiceGraphics {
  @Accessors
  private double spacing = 6.0;
  
  private double maxWidth;
  
  @Override
  public void setInterpolatedPosition(final double interpolatedPosition) {
    ArrayList<XNode> _choiceNodes = this.getChoiceNodes();
    final Function2<Double, XNode, Double> _function = (Double a, XNode b) -> {
      Bounds _layoutBounds = b.getLayoutBounds();
      double _width = _layoutBounds.getWidth();
      return Double.valueOf(Math.max((a).doubleValue(), _width));
    };
    Double _fold = IterableExtensions.<XNode, Double>fold(_choiceNodes, Double.valueOf(0.0), _function);
    double _plus = ((_fold).doubleValue() + this.spacing);
    this.maxWidth = _plus;
    final double angle = ((interpolatedPosition - ((int) interpolatedPosition)) * 90);
    ArrayList<XNode> _choiceNodes_1 = this.getChoiceNodes();
    int _size = _choiceNodes_1.size();
    final int leftNodeIndex = (((int) interpolatedPosition) % _size);
    this.applyTransform(leftNodeIndex, angle);
    int _plus_1 = (((int) interpolatedPosition) + 1);
    ArrayList<XNode> _choiceNodes_2 = this.getChoiceNodes();
    int _size_1 = _choiceNodes_2.size();
    final int rightNodeIndex = (_plus_1 % _size_1);
    this.applyTransform(rightNodeIndex, (angle - 90));
    ArrayList<XNode> _choiceNodes_3 = this.getChoiceNodes();
    final Procedure2<XNode, Integer> _function_1 = (XNode node, Integer i) -> {
      if (((i != leftNodeIndex) && (i != rightNodeIndex))) {
        node.setVisible(false);
      }
    };
    IterableExtensions.<XNode>forEach(_choiceNodes_3, _function_1);
  }
  
  protected Boolean applyTransform(final int nodeIndex, final double angle) {
    boolean _xblockexpression = false;
    {
      ArrayList<XNode> _choiceNodes = this.getChoiceNodes();
      final XNode node = _choiceNodes.get(nodeIndex);
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
          final Affine transform = new Affine();
          TransformExtensions.translate(transform, ((-0.5) * width), ((-0.5) * height), ((-this.maxWidth) * 0.5));
          Point3D _point3D = new Point3D(0, 1, 0);
          TransformExtensions.rotate(transform, angle, _point3D);
          TransformExtensions.translate(transform, 0, 0, (this.maxWidth * 0.5));
          node.setVisible(true);
          ObservableList<Transform> _transforms = node.getTransforms();
          _xblockexpression_1 = _transforms.setAll(transform);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return Boolean.valueOf(_xblockexpression);
  }
  
  @Override
  public void relocateButtons(final Node minusButton, final Node plusButton) {
    double _sqrt = Math.sqrt(2);
    final double groupMaxWidthHalf = ((0.5 * this.maxWidth) * _sqrt);
    Group _choiceGroup = this.getChoiceGroup();
    double _layoutX = _choiceGroup.getLayoutX();
    double _plus = (_layoutX + groupMaxWidthHalf);
    minusButton.setLayoutX(_plus);
    Group _choiceGroup_1 = this.getChoiceGroup();
    double _layoutY = _choiceGroup_1.getLayoutY();
    Bounds _layoutBounds = minusButton.getLayoutBounds();
    double _height = _layoutBounds.getHeight();
    double _multiply = (0.5 * _height);
    double _minus = (_layoutY - _multiply);
    minusButton.setLayoutY(_minus);
    Group _choiceGroup_2 = this.getChoiceGroup();
    double _layoutX_1 = _choiceGroup_2.getLayoutX();
    double _minus_1 = (_layoutX_1 - groupMaxWidthHalf);
    Bounds _layoutBounds_1 = plusButton.getLayoutBounds();
    double _width = _layoutBounds_1.getWidth();
    double _minus_2 = (_minus_1 - _width);
    plusButton.setLayoutX(_minus_2);
    Group _choiceGroup_3 = this.getChoiceGroup();
    double _layoutY_1 = _choiceGroup_3.getLayoutY();
    Bounds _layoutBounds_2 = plusButton.getLayoutBounds();
    double _height_1 = _layoutBounds_2.getHeight();
    double _multiply_1 = (0.5 * _height_1);
    double _minus_3 = (_layoutY_1 - _multiply_1);
    plusButton.setLayoutY(_minus_3);
  }
  
  @Pure
  public double getSpacing() {
    return this.spacing;
  }
  
  public void setSpacing(final double spacing) {
    this.spacing = spacing;
  }
}
