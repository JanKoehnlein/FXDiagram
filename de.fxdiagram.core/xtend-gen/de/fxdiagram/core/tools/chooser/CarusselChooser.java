package de.fxdiagram.core.tools.chooser;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.tools.chooser.AbstractXNodeChooser;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class CarusselChooser extends AbstractXNodeChooser {
  private double _spacing = 6;
  
  public double getSpacing() {
    return this._spacing;
  }
  
  public void setSpacing(final double spacing) {
    this._spacing = spacing;
  }
  
  private Effect currentNodeEffect;
  
  private double radius;
  
  public CarusselChooser(final XNode host, final Pos layoutPosition) {
    super(host, layoutPosition, true);
    InnerShadow _innerShadow = new InnerShadow();
    this.currentNodeEffect = _innerShadow;
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
          double _height = _layoutBounds.getHeight();
          double _max = Math.max((a).doubleValue(), _height);
          return Double.valueOf(_max);
        }
      };
    Double _fold = IterableExtensions.<XNode, Double>fold(_nodes, Double.valueOf(0.0), _function);
    double _spacing = this.getSpacing();
    final double maxHeight = ((_fold).doubleValue() + _spacing);
    ArrayList<XNode> _nodes_1 = this.getNodes();
    int _size = _nodes_1.size();
    final double angle = (Math.PI / _size);
    double _divide = (maxHeight / 2);
    double _sin = Math.sin(angle);
    double _divide_1 = (_divide / _sin);
    this.radius = _divide_1;
    ArrayList<XNode> _nodes_2 = this.getNodes();
    int _size_1 = _nodes_2.size();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size_1, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        ArrayList<XNode> _nodes_3 = this.getNodes();
        final XNode node = _nodes_3.get((i).intValue());
        double _multiply = (2 * Math.PI);
        double _minus = ((i).intValue() - interpolatedPosition);
        double _multiply_1 = (_multiply * _minus);
        ArrayList<XNode> _nodes_4 = this.getNodes();
        int _size_2 = _nodes_4.size();
        final double nodeCenterAngle = (_multiply_1 / _size_2);
        double _cos = Math.cos(nodeCenterAngle);
        boolean _lessThan = (_cos < 0);
        if (_lessThan) {
          node.setVisible(false);
        } else {
          node.setVisible(true);
          ObservableList<Transform> _transforms = node.getTransforms();
          _transforms.clear();
          final double scaleY = Math.cos(nodeCenterAngle);
          double _plus = (scaleY + 0.5);
          final double scaleX = (_plus / 1.5);
          ObservableList<Transform> _transforms_1 = node.getTransforms();
          double _minus_1 = (nodeCenterAngle - angle);
          double _sin_1 = Math.sin(_minus_1);
          double _multiply_2 = (this.radius * _sin_1);
          Translate _translate = Transform.translate(0, _multiply_2);
          _transforms_1.add(_translate);
          ObservableList<Transform> _transforms_2 = node.getTransforms();
          Scale _scale = Transform.scale(scaleX, scaleY);
          _transforms_2.add(_scale);
          ObservableList<Transform> _transforms_3 = node.getTransforms();
          Bounds _layoutBounds = node.getLayoutBounds();
          double _width = _layoutBounds.getWidth();
          double _minus_2 = (-_width);
          double _divide_2 = (_minus_2 / 2);
          double _spacing_1 = this.getSpacing();
          double _divide_3 = (_spacing_1 / 2);
          Translate _translate_1 = Transform.translate(_divide_2, _divide_3);
          _transforms_3.add(_translate_1);
          double _multiply_3 = (scaleY * scaleY);
          double _multiply_4 = (_multiply_3 * scaleY);
          node.setOpacity(_multiply_4);
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
  
  protected void nodeChosen(final XNode choice) {
    choice.setEffect(null);
    super.nodeChosen(choice);
  }
  
  public void relocateButtons(final Button minusButton, final Button plusButton) {
    Group _group = this.getGroup();
    double _layoutX = _group.getLayoutX();
    Bounds _layoutBounds = minusButton.getLayoutBounds();
    double _width = _layoutBounds.getWidth();
    double _multiply = (0.5 * _width);
    double _minus = (_layoutX - _multiply);
    minusButton.setLayoutX(_minus);
    Group _group_1 = this.getGroup();
    double _layoutY = _group_1.getLayoutY();
    double _plus = (_layoutY + this.radius);
    minusButton.setLayoutY(_plus);
    Group _group_2 = this.getGroup();
    double _layoutX_1 = _group_2.getLayoutX();
    Bounds _layoutBounds_1 = plusButton.getLayoutBounds();
    double _width_1 = _layoutBounds_1.getWidth();
    double _multiply_1 = (0.5 * _width_1);
    double _minus_1 = (_layoutX_1 - _multiply_1);
    plusButton.setLayoutX(_minus_1);
    Group _group_3 = this.getGroup();
    double _layoutY_1 = _group_3.getLayoutY();
    double _minus_2 = (_layoutY_1 - this.radius);
    Bounds _layoutBounds_2 = plusButton.getLayoutBounds();
    double _height = _layoutBounds_2.getHeight();
    double _minus_3 = (_minus_2 - _height);
    plusButton.setLayoutY(_minus_3);
  }
}
