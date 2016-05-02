package de.fxdiagram.lib.chooser;

import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.chooser.AbstractChoiceGraphics;
import java.util.ArrayList;
import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * A pseudo 3D effect that projects all choices on a spinning wheel, similar to a slot machine.
 */
@SuppressWarnings("all")
public class CarusselChoice extends AbstractChoiceGraphics {
  @Accessors
  private double spacing = 6;
  
  private Effect currentNodeEffect = new InnerShadow();
  
  private double radius;
  
  @Override
  public void setInterpolatedPosition(final double interpolatedPosition) {
    ArrayList<XNode> _choiceNodes = this.getChoiceNodes();
    final Function2<Double, XNode, Double> _function = (Double a, XNode b) -> {
      Bounds _layoutBounds = b.getLayoutBounds();
      double _height = _layoutBounds.getHeight();
      return Double.valueOf(Math.max((a).doubleValue(), _height));
    };
    Double _fold = IterableExtensions.<XNode, Double>fold(_choiceNodes, Double.valueOf(0.0), _function);
    final double maxHeight = ((_fold).doubleValue() + this.spacing);
    ArrayList<XNode> _choiceNodes_1 = this.getChoiceNodes();
    int _size = _choiceNodes_1.size();
    final double angle = (Math.PI / _size);
    double _sin = Math.sin(angle);
    double _divide = ((maxHeight / 2) / _sin);
    this.radius = _divide;
    ArrayList<XNode> _choiceNodes_2 = this.getChoiceNodes();
    int _size_1 = _choiceNodes_2.size();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size_1, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        ArrayList<XNode> _choiceNodes_3 = this.getChoiceNodes();
        final XNode node = _choiceNodes_3.get((i).intValue());
        ArrayList<XNode> _choiceNodes_4 = this.getChoiceNodes();
        int _size_2 = _choiceNodes_4.size();
        final double nodeCenterAngle = (((2 * Math.PI) * ((i).intValue() - interpolatedPosition)) / _size_2);
        double _cos = Math.cos(nodeCenterAngle);
        boolean _lessThan = (_cos < 0);
        if (_lessThan) {
          node.setVisible(false);
        } else {
          node.setVisible(true);
          final double scaleY = Math.cos(nodeCenterAngle);
          final double scaleX = ((scaleY + 0.5) / 1.5);
          ObservableList<Transform> _transforms = node.getTransforms();
          double _sin_1 = Math.sin((nodeCenterAngle - angle));
          double _multiply = (this.radius * _sin_1);
          Translate _translate = Transform.translate(0, _multiply);
          Scale _scale = Transform.scale(scaleX, scaleY);
          Bounds _layoutBounds = node.getLayoutBounds();
          double _width = _layoutBounds.getWidth();
          double _minus = (-_width);
          double _divide_1 = (_minus / 2);
          Translate _translate_1 = Transform.translate(_divide_1, (this.spacing / 2));
          _transforms.setAll(
            Collections.<Transform>unmodifiableList(CollectionLiterals.<Transform>newArrayList(_translate, _scale, _translate_1)));
          node.setOpacity(((scaleY * scaleY) * scaleY));
          double _abs = Math.abs(nodeCenterAngle);
          boolean _lessThan_1 = (_abs < angle);
          if (_lessThan_1) {
            node.setEffect(this.currentNodeEffect);
          } else {
            node.setEffect(null);
          }
        }
      }
    }
  }
  
  @Override
  public void nodeChosen(final XNode choice) {
    super.nodeChosen(choice);
    choice.setEffect(null);
  }
  
  @Override
  public void relocateButtons(final Node minusButton, final Node plusButton) {
    Group _choiceGroup = this.getChoiceGroup();
    double _layoutX = _choiceGroup.getLayoutX();
    Bounds _layoutBounds = minusButton.getLayoutBounds();
    double _width = _layoutBounds.getWidth();
    double _multiply = (0.5 * _width);
    double _minus = (_layoutX - _multiply);
    minusButton.setLayoutX(_minus);
    Group _choiceGroup_1 = this.getChoiceGroup();
    double _layoutY = _choiceGroup_1.getLayoutY();
    double _plus = (_layoutY + this.radius);
    Bounds _layoutBounds_1 = minusButton.getLayoutBounds();
    double _height = _layoutBounds_1.getHeight();
    double _multiply_1 = (0.5 * _height);
    double _plus_1 = (_plus + _multiply_1);
    minusButton.setLayoutY(_plus_1);
    Group _choiceGroup_2 = this.getChoiceGroup();
    double _layoutX_1 = _choiceGroup_2.getLayoutX();
    Bounds _layoutBounds_2 = plusButton.getLayoutBounds();
    double _width_1 = _layoutBounds_2.getWidth();
    double _multiply_2 = (0.5 * _width_1);
    double _minus_1 = (_layoutX_1 - _multiply_2);
    plusButton.setLayoutX(_minus_1);
    Group _choiceGroup_3 = this.getChoiceGroup();
    double _layoutY_1 = _choiceGroup_3.getLayoutY();
    double _minus_2 = (_layoutY_1 - this.radius);
    Bounds _layoutBounds_3 = plusButton.getLayoutBounds();
    double _height_1 = _layoutBounds_3.getHeight();
    double _minus_3 = (_minus_2 - _height_1);
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
