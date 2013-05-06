package de.itemis.javafx.diagram.tools.chooser;

import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.tools.chooser.AbstractXNodeChooser;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
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
  
  private double angle;
  
  private double radius;
  
  private Effect currentNodeEffect;
  
  public CarusselChooser(final XNode host, final Point2D position) {
    super(host, position);
    InnerShadow _innerShadow = new InnerShadow();
    this.currentNodeEffect = _innerShadow;
  }
  
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      List<XNode> _nodes = this.getNodes();
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
      List<XNode> _nodes_1 = this.getNodes();
      int _size = _nodes_1.size();
      double _divide = (Math.PI / _size);
      this.angle = _divide;
      double _divide_1 = (maxHeight / 2);
      double _sin = Math.sin(this.angle);
      double _divide_2 = (_divide_1 / _sin);
      this.radius = _divide_2;
      boolean _activate = super.activate();
      _xblockexpression = (_activate);
    }
    return _xblockexpression;
  }
  
  protected void setInterpolatedPosition(final double interpolatedPosition) {
    List<XNode> _nodes = this.getNodes();
    int _size = _nodes.size();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        List<XNode> _nodes_1 = this.getNodes();
        final XNode node = _nodes_1.get((i).intValue());
        double _multiply = (2 * Math.PI);
        double _minus = ((i).intValue() - interpolatedPosition);
        double _multiply_1 = (_multiply * _minus);
        List<XNode> _nodes_2 = this.getNodes();
        int _size_1 = _nodes_2.size();
        final double nodeCenterAngle = (_multiply_1 / _size_1);
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
          double _minus_1 = (nodeCenterAngle - this.angle);
          double _sin = Math.sin(_minus_1);
          double _multiply_2 = (this.radius * _sin);
          Translate _translate = Transform.translate(0, _multiply_2);
          _transforms_1.add(_translate);
          ObservableList<Transform> _transforms_2 = node.getTransforms();
          Scale _scale = Transform.scale(scaleX, scaleY);
          _transforms_2.add(_scale);
          ObservableList<Transform> _transforms_3 = node.getTransforms();
          Bounds _layoutBounds = node.getLayoutBounds();
          double _width = _layoutBounds.getWidth();
          double _minus_2 = (-_width);
          double _divide = (_minus_2 / 2);
          double _spacing = this.getSpacing();
          double _divide_1 = (_spacing / 2);
          Translate _translate_1 = Transform.translate(_divide, _divide_1);
          _transforms_3.add(_translate_1);
          double _multiply_3 = (scaleY * scaleY);
          double _multiply_4 = (_multiply_3 * scaleY);
          node.setOpacity(_multiply_4);
          double _abs = Math.abs(nodeCenterAngle);
          boolean _lessThan_1 = (_abs < this.angle);
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
}
