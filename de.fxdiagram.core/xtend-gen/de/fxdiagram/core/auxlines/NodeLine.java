package de.fxdiagram.core.auxlines;

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
    if (orientation != null) {
      switch (orientation) {
        case HORIZONTAL:
          double _minX = boundsInDiagram.getMinX();
          this.min = _minX;
          double _maxX = boundsInDiagram.getMaxX();
          this.max = _maxX;
          break;
        case VERTICAL:
          double _minY = boundsInDiagram.getMinY();
          this.min = _minY;
          double _maxY = boundsInDiagram.getMaxY();
          this.max = _maxY;
          break;
        default:
          break;
      }
    }
  }
  
  public Orientation getOrientation() {
    return this.orientation;
  }
  
  @Override
  public Node createNode() {
    Line _switchResult = null;
    final Orientation orientation = this.orientation;
    if (orientation != null) {
      switch (orientation) {
        case HORIZONTAL:
          Line _line = new Line();
          final Procedure1<Line> _function = new Procedure1<Line>() {
            @Override
            public void apply(final Line it) {
              it.setStartX((NodeLine.this.min - NodeLine.this.overlap));
              double _position = NodeLine.this.getPosition();
              it.setStartY(_position);
              it.setEndX((NodeLine.this.max + NodeLine.this.overlap));
              double _position_1 = NodeLine.this.getPosition();
              it.setEndY(_position_1);
            }
          };
          _switchResult = ObjectExtensions.<Line>operator_doubleArrow(_line, _function);
          break;
        case VERTICAL:
          Line _line_1 = new Line();
          final Procedure1<Line> _function_1 = new Procedure1<Line>() {
            @Override
            public void apply(final Line it) {
              double _position = NodeLine.this.getPosition();
              it.setStartX(_position);
              it.setStartY((NodeLine.this.min - NodeLine.this.overlap));
              double _position_1 = NodeLine.this.getPosition();
              it.setEndX(_position_1);
              it.setEndY((NodeLine.this.max + NodeLine.this.overlap));
            }
          };
          _switchResult = ObjectExtensions.<Line>operator_doubleArrow(_line_1, _function_1);
          break;
        default:
          break;
      }
    }
    final Procedure1<Line> _function_2 = new Procedure1<Line>() {
      @Override
      public void apply(final Line it) {
        it.setStroke(Color.RED);
        it.setStrokeWidth(2);
      }
    };
    return ObjectExtensions.<Line>operator_doubleArrow(_switchResult, _function_2);
  }
}
