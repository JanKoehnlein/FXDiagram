package de.fxdiagram.lib.tools;

import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.tools.AbstractChooser;
import java.util.ArrayList;
import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class CarusselChooser extends AbstractChooser {
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
    return super.activate();
  }
  
  protected void doSetInterpolatedPosition(final double interpolatedPosition) {
    ArrayList<XNode> _nodes = this.getNodes();
    final Function2<Double, XNode, Double> _function = new Function2<Double, XNode, Double>() {
      public Double apply(final Double a, final XNode b) {
        Bounds _layoutBounds = b.getLayoutBounds();
        double _height = _layoutBounds.getHeight();
        return Double.valueOf(Math.max((a).doubleValue(), _height));
      }
    };
    Double _fold = IterableExtensions.<XNode, Double>fold(_nodes, Double.valueOf(0.0), _function);
    double _spacing = this.getSpacing();
    final double maxHeight = ((_fold).doubleValue() + _spacing);
    ArrayList<XNode> _nodes_1 = this.getNodes();
    int _size = _nodes_1.size();
    final double angle = (Math.PI / _size);
    double _sin = Math.sin(angle);
    double _divide = ((maxHeight / 2) / _sin);
    this.radius = _divide;
    ArrayList<XNode> _nodes_2 = this.getNodes();
    int _size_1 = _nodes_2.size();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size_1, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        ArrayList<XNode> _nodes_3 = this.getNodes();
        final XNode node = _nodes_3.get((i).intValue());
        ArrayList<XNode> _nodes_4 = this.getNodes();
        int _size_2 = _nodes_4.size();
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
          double _spacing_1 = this.getSpacing();
          double _divide_2 = (_spacing_1 / 2);
          Translate _translate_1 = Transform.translate(_divide_1, _divide_2);
          _transforms.setAll(
            Collections.<Transform>unmodifiableList(Lists.<Transform>newArrayList(_translate, _scale, _translate_1)));
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
  
  protected XConnection nodeChosen(final XNode choice) {
    XConnection _xblockexpression = null;
    {
      choice.setEffect(null);
      _xblockexpression = super.nodeChosen(choice);
    }
    return _xblockexpression;
  }
  
  public void relocateButtons(final Node minusButton, final Node plusButton) {
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
