package de.fxdiagram.core.extensions;

import de.fxdiagram.core.extensions.TooltipExtensions;
import javafx.geometry.Side;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ButtonExtensions {
  public static SVGPath getTriangleButton(final Side side, final String tooltip) {
    SVGPath _sVGPath = new SVGPath();
    final Procedure1<SVGPath> _function = new Procedure1<SVGPath>() {
      public void apply(final SVGPath it) {
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
      }
    };
    return ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function);
  }
  
  public static SVGPath getFilledTriangle(final Side side, final String tooltip) {
    SVGPath _sVGPath = new SVGPath();
    final Procedure1<SVGPath> _function = new Procedure1<SVGPath>() {
      public void apply(final SVGPath it) {
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
      }
    };
    return ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function);
  }
  
  public static SVGPath getArrowButton(final Side side, final String tooltip) {
    SVGPath _sVGPath = new SVGPath();
    final Procedure1<SVGPath> _function = new Procedure1<SVGPath>() {
      public void apply(final SVGPath it) {
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
      }
    };
    return ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function);
  }
}
