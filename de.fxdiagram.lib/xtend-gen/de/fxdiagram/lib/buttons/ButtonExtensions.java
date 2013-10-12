package de.fxdiagram.lib.buttons;

import com.google.common.base.Objects;
import javafx.geometry.Side;
import javafx.scene.control.Tooltip;
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
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(side,Side.TOP)) {
            _matched=true;
            _switchResult = "m 0,11 9,-11 9,11 z";
          }
        }
        if (!_matched) {
          if (Objects.equal(side,Side.BOTTOM)) {
            _matched=true;
            _switchResult = "m 0,0 9,11 9,-11 z";
          }
        }
        if (!_matched) {
          if (Objects.equal(side,Side.LEFT)) {
            _matched=true;
            _switchResult = "m 11,0 -11,9 11,9 z";
          }
        }
        if (!_matched) {
          if (Objects.equal(side,Side.RIGHT)) {
            _matched=true;
            _switchResult = "m 0,0 11,9 -11,9 z";
          }
        }
        it.setContent(_switchResult);
        it.setFill(Color.WHITE);
        it.setStroke(Color.DARKGREEN);
        it.setStrokeWidth(3.5);
        Tooltip _tooltip = new Tooltip(tooltip);
        Tooltip.install(it, _tooltip);
      }
    };
    SVGPath _doubleArrow = ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function);
    return _doubleArrow;
  }
  
  public static SVGPath getFilledTriangle(final Side side, final String tooltip) {
    SVGPath _sVGPath = new SVGPath();
    final Procedure1<SVGPath> _function = new Procedure1<SVGPath>() {
      public void apply(final SVGPath it) {
        String _switchResult = null;
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(side,Side.TOP)) {
            _matched=true;
            _switchResult = "m 0,7 6,-7 6,7 z";
          }
        }
        if (!_matched) {
          if (Objects.equal(side,Side.BOTTOM)) {
            _matched=true;
            _switchResult = "m 0,0 6,7 6,-7 z";
          }
        }
        if (!_matched) {
          if (Objects.equal(side,Side.LEFT)) {
            _matched=true;
            _switchResult = "m 7,0 -7,6 7,6 z";
          }
        }
        if (!_matched) {
          if (Objects.equal(side,Side.RIGHT)) {
            _matched=true;
            _switchResult = "m 0,0 7,6 -7,6 z";
          }
        }
        it.setContent(_switchResult);
        it.setFill(Color.DARKGREEN);
        it.setStroke(Color.DARKGREEN);
        it.setStrokeWidth(3.5);
        Tooltip _tooltip = new Tooltip(tooltip);
        Tooltip.install(it, _tooltip);
      }
    };
    SVGPath _doubleArrow = ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function);
    return _doubleArrow;
  }
  
  public static SVGPath getArrowButton(final Side side, final String tooltip) {
    SVGPath _sVGPath = new SVGPath();
    final Procedure1<SVGPath> _function = new Procedure1<SVGPath>() {
      public void apply(final SVGPath it) {
        String _switchResult = null;
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(side,Side.TOP)) {
            _matched=true;
            _switchResult = "m 0,9 7,-9 7,9";
          }
        }
        if (!_matched) {
          if (Objects.equal(side,Side.BOTTOM)) {
            _matched=true;
            _switchResult = "m 0,0 7,9 7,-9";
          }
        }
        if (!_matched) {
          if (Objects.equal(side,Side.LEFT)) {
            _matched=true;
            _switchResult = "m 9,0 -9,7 9,7";
          }
        }
        if (!_matched) {
          if (Objects.equal(side,Side.RIGHT)) {
            _matched=true;
            _switchResult = "m 0,0 9,7 -9,7";
          }
        }
        it.setContent(_switchResult);
        it.setStroke(Color.DARKGREEN);
        it.setFill(Color.TRANSPARENT);
        it.setStrokeWidth(3.5);
        it.setStrokeLineCap(StrokeLineCap.ROUND);
        Tooltip _tooltip = new Tooltip(tooltip);
        Tooltip.install(it, _tooltip);
      }
    };
    SVGPath _doubleArrow = ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function);
    return _doubleArrow;
  }
}
