package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
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
public class ZoomToFitAction implements DiagramAction {
  public void perform(final XRoot root) {
    Iterable<XShape> _currentSelection = root.getCurrentSelection();
    final Function1<XShape,Bounds> _function = new Function1<XShape,Bounds>() {
      public Bounds apply(final XShape it) {
        Bounds _snapBounds = it.getSnapBounds();
        Bounds _localToRootDiagram = CoreExtensions.localToRootDiagram(it, _snapBounds);
        return _localToRootDiagram;
      }
    };
    Iterable<Bounds> _map = IterableExtensions.<XShape, Bounds>map(_currentSelection, _function);
    final Function2<Bounds,Bounds,Bounds> _function_1 = new Function2<Bounds,Bounds,Bounds>() {
      public Bounds apply(final Bounds a, final Bounds b) {
        BoundingBox _plus = BoundsExtensions.operator_plus(a, b);
        return _plus;
      }
    };
    final Bounds selectionBounds = IterableExtensions.<Bounds>reduce(_map, _function_1);
    boolean _and = false;
    boolean _and_1 = false;
    boolean _notEquals = (!Objects.equal(selectionBounds, null));
    if (!_notEquals) {
      _and_1 = false;
    } else {
      double _width = selectionBounds.getWidth();
      boolean _greaterThan = (_width > NumberExpressionExtensions.EPSILON);
      _and_1 = (_notEquals && _greaterThan);
    }
    if (!_and_1) {
      _and = false;
    } else {
      double _height = selectionBounds.getHeight();
      boolean _greaterThan_1 = (_height > NumberExpressionExtensions.EPSILON);
      _and = (_and_1 && _greaterThan_1);
    }
    if (_and) {
      Scene _scene = root.getScene();
      double _width_1 = _scene.getWidth();
      double _width_2 = selectionBounds.getWidth();
      double _divide = (_width_1 / _width_2);
      Scene _scene_1 = root.getScene();
      double _height_1 = _scene_1.getHeight();
      double _height_2 = selectionBounds.getHeight();
      double _divide_1 = (_height_1 / _height_2);
      final double targetScale = Math.min(_divide, _divide_1);
      Point2D _center = BoundsExtensions.center(selectionBounds);
      ScrollToAndScaleTransition _scrollToAndScaleTransition = new ScrollToAndScaleTransition(root, _center, targetScale);
      _scrollToAndScaleTransition.play();
    }
  }
}
