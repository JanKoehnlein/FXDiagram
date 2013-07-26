package de.fxdiagram.core.auxlines;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.auxlines.AuxiliaryLine;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class NodeLine extends AuxiliaryLine {
  private final int overlap = 5;
  
  private Orientation orientation;
  
  private double min;
  
  private double max;
  
  public NodeLine(final double position, final Orientation orientation, final XNode node, final Bounds boundsInDiagram) {
    super(position, new XNode[] { node });
    this.orientation = orientation;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(orientation,Orientation.HORIZONTAL)) {
        _matched=true;
        double _minX = boundsInDiagram.getMinX();
        this.min = _minX;
        double _maxX = boundsInDiagram.getMaxX();
        this.max = _maxX;
      }
    }
    if (!_matched) {
      if (Objects.equal(orientation,Orientation.VERTICAL)) {
        _matched=true;
        double _minY = boundsInDiagram.getMinY();
        this.min = _minY;
        double _maxY = boundsInDiagram.getMaxY();
        this.max = _maxY;
      }
    }
  }
  
  public Orientation getOrientation() {
    return this.orientation;
  }
  
  public Node createNode() {
    Line _switchResult = null;
    final Orientation orientation = this.orientation;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(orientation,Orientation.HORIZONTAL)) {
        _matched=true;
        Line _line = new Line();
        final Procedure1<Line> _function = new Procedure1<Line>() {
          public void apply(final Line it) {
            double _minus = (NodeLine.this.min - NodeLine.this.overlap);
            it.setStartX(_minus);
            double _position = NodeLine.this.getPosition();
            it.setStartY(_position);
            double _plus = (NodeLine.this.max + NodeLine.this.overlap);
            it.setEndX(_plus);
            double _position_1 = NodeLine.this.getPosition();
            it.setEndY(_position_1);
          }
        };
        Line _doubleArrow = ObjectExtensions.<Line>operator_doubleArrow(_line, _function);
        _switchResult = _doubleArrow;
      }
    }
    if (!_matched) {
      if (Objects.equal(orientation,Orientation.VERTICAL)) {
        _matched=true;
        Line _line_1 = new Line();
        final Procedure1<Line> _function_1 = new Procedure1<Line>() {
          public void apply(final Line it) {
            double _position = NodeLine.this.getPosition();
            it.setStartX(_position);
            double _minus = (NodeLine.this.min - NodeLine.this.overlap);
            it.setStartY(_minus);
            double _position_1 = NodeLine.this.getPosition();
            it.setEndX(_position_1);
            double _plus = (NodeLine.this.max + NodeLine.this.overlap);
            it.setEndY(_plus);
          }
        };
        Line _doubleArrow_1 = ObjectExtensions.<Line>operator_doubleArrow(_line_1, _function_1);
        _switchResult = _doubleArrow_1;
      }
    }
    final Procedure1<Line> _function_2 = new Procedure1<Line>() {
      public void apply(final Line it) {
        it.setStroke(Color.RED);
        it.setStrokeWidth(2);
      }
    };
    Line _doubleArrow_2 = ObjectExtensions.<Line>operator_doubleArrow(_switchResult, _function_2);
    return _doubleArrow_2;
  }
}
