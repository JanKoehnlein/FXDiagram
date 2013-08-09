package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.geometry.BoundsExtensions;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.core.tools.actions.ScrollToAndScaleTransition;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class CenterAction implements DiagramAction {
  public void perform(final XRootDiagram diagram) {
    Iterable<XShape> _selection = this.getSelection(diagram);
    final Function1<XShape,Bounds> _function = new Function1<XShape,Bounds>() {
      public Bounds apply(final XShape it) {
        Bounds _boundsInLocal = it.getBoundsInLocal();
        Bounds _localToRootDiagram = Extensions.localToRootDiagram(it, _boundsInLocal);
        return _localToRootDiagram;
      }
    };
    Iterable<Bounds> _map = IterableExtensions.<XShape, Bounds>map(_selection, _function);
    final Function2<Bounds,Bounds,Bounds> _function_1 = new Function2<Bounds,Bounds,Bounds>() {
      public Bounds apply(final Bounds a, final Bounds b) {
        BoundingBox _plus = BoundsExtensions.operator_plus(a, b);
        return _plus;
      }
    };
    final Bounds selectionBounds = IterableExtensions.<Bounds>reduce(_map, _function_1);
    boolean _notEquals = (!Objects.equal(selectionBounds, null));
    if (_notEquals) {
      Scene _scene = diagram.getScene();
      double _width = _scene.getWidth();
      double _width_1 = selectionBounds.getWidth();
      double _divide = (_width / _width_1);
      Scene _scene_1 = diagram.getScene();
      double _height = _scene_1.getHeight();
      double _height_1 = selectionBounds.getHeight();
      double _divide_1 = (_height / _height_1);
      double _min = Math.min(_divide, _divide_1);
      final double targetScale = Math.min(1, _min);
      double _minX = selectionBounds.getMinX();
      double _maxX = selectionBounds.getMaxX();
      double _plus = (_minX + _maxX);
      double _multiply = (0.5 * _plus);
      double _minY = selectionBounds.getMinY();
      double _maxY = selectionBounds.getMaxY();
      double _plus_1 = (_minY + _maxY);
      double _multiply_1 = (0.5 * _plus_1);
      Point2D _point2D = new Point2D(_multiply, _multiply_1);
      final Point2D selectionCenter = _point2D;
      ScrollToAndScaleTransition _scrollToAndScaleTransition = new ScrollToAndScaleTransition(diagram, selectionCenter, targetScale);
      _scrollToAndScaleTransition.play();
    }
  }
  
  protected Iterable<XShape> getSelection(final XRootDiagram diagram) {
    Iterable<XShape> _allShapes = diagram.getAllShapes();
    final Function1<XShape,Boolean> _function = new Function1<XShape,Boolean>() {
      public Boolean apply(final XShape it) {
        boolean _and = false;
        boolean _isSelectable = it.isSelectable();
        if (!_isSelectable) {
          _and = false;
        } else {
          boolean _selected = it.getSelected();
          _and = (_isSelectable && _selected);
        }
        return Boolean.valueOf(_and);
      }
    };
    Iterable<XShape> _filter = IterableExtensions.<XShape>filter(_allShapes, _function);
    return _filter;
  }
}
