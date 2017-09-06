package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XButton;
import de.fxdiagram.core.extensions.TooltipExtensions;
import javafx.event.EventTarget;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ButtonExtensions {
  public static SVGPath getTriangleButton(final Side side, final String tooltip) {
    SVGPath _sVGPath = new SVGPath();
    final Procedure1<SVGPath> _function = (SVGPath it) -> {
      String _switchResult = null;
      if (side != null) {
        switch (side) {
          case TOP:
            _switchResult = "m 0,11 9,-11 9,11 z";
            break;
          case BOTTOM:
            _switchResult = "m 0,0 9,11 9,-11 z";
            break;
          case LEFT:
            _switchResult = "m 11,0 -11,9 11,9 z";
            break;
          case RIGHT:
            _switchResult = "m 0,0 11,9 -11,9 z";
            break;
          default:
            break;
        }
      }
      it.setContent(_switchResult);
      it.setFill(Color.WHITE);
      it.setStroke(Color.DARKGREEN);
      it.setStrokeWidth(3.5);
      TooltipExtensions.setTooltip(it, tooltip);
    };
    return ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function);
  }
  
  public static SVGPath getFilledTriangle(final Side side, final String tooltip) {
    SVGPath _sVGPath = new SVGPath();
    final Procedure1<SVGPath> _function = (SVGPath it) -> {
      String _switchResult = null;
      if (side != null) {
        switch (side) {
          case TOP:
            _switchResult = "m 0,7 6,-7 6,7 z";
            break;
          case BOTTOM:
            _switchResult = "m 0,0 6,7 6,-7 z";
            break;
          case LEFT:
            _switchResult = "m 7,0 -7,6 7,6 z";
            break;
          case RIGHT:
            _switchResult = "m 0,0 7,6 -7,6 z";
            break;
          default:
            break;
        }
      }
      it.setContent(_switchResult);
      it.setFill(Color.DARKGREEN);
      it.setStroke(Color.DARKGREEN);
      it.setStrokeWidth(3.5);
      TooltipExtensions.setTooltip(it, tooltip);
    };
    return ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function);
  }
  
  public static SVGPath getArrowButton(final Side side, final String tooltip) {
    SVGPath _sVGPath = new SVGPath();
    final Procedure1<SVGPath> _function = (SVGPath it) -> {
      String _switchResult = null;
      if (side != null) {
        switch (side) {
          case TOP:
            _switchResult = "m 0,9 7,-9 7,9";
            break;
          case BOTTOM:
            _switchResult = "m 0,0 7,9 7,-9";
            break;
          case LEFT:
            _switchResult = "m 9,0 -9,7 9,7";
            break;
          case RIGHT:
            _switchResult = "m 0,0 9,7 -9,7";
            break;
          default:
            break;
        }
      }
      it.setContent(_switchResult);
      it.setStroke(Color.DARKGREEN);
      it.setFill(Color.TRANSPARENT);
      it.setStrokeWidth(3.5);
      it.setStrokeLineCap(StrokeLineCap.ROUND);
      TooltipExtensions.setTooltip(it, tooltip);
    };
    return ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function);
  }
  
  public static XButton getTargetButton(final MouseEvent event) {
    XButton _xifexpression = null;
    EventTarget _target = event.getTarget();
    if ((_target instanceof Node)) {
      EventTarget _target_1 = event.getTarget();
      _xifexpression = ButtonExtensions.getContainerButton(((Node) _target_1));
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public static XButton getContainerButton(final Node it) {
    XButton _switchResult = null;
    boolean _matched = false;
    if (Objects.equal(it, null)) {
      _matched=true;
      _switchResult = null;
    }
    if (!_matched) {
      if (it instanceof XButton) {
        _matched=true;
        _switchResult = ((XButton)it);
      }
    }
    if (!_matched) {
      _switchResult = ButtonExtensions.getContainerButton(it.getParent());
    }
    return _switchResult;
  }
  
  public static Side invert(final Side side) {
    Side _switchResult = null;
    if (side != null) {
      switch (side) {
        case TOP:
          _switchResult = Side.BOTTOM;
          break;
        case BOTTOM:
          _switchResult = Side.TOP;
          break;
        case LEFT:
          _switchResult = Side.RIGHT;
          break;
        case RIGHT:
          _switchResult = Side.LEFT;
          break;
        default:
          _switchResult = null;
          break;
      }
    } else {
      _switchResult = null;
    }
    return _switchResult;
  }
}
