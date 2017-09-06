package de.fxdiagram.core.auxlines;

import de.fxdiagram.core.XShape;
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
  
  public NodeLine(final double position, final Orientation orientation, final XShape shape, final Bounds boundsInDiagram) {
    super(position, new XShape[] { shape });
    this.orientation = orientation;
    if (orientation != null) {
      switch (orientation) {
        case HORIZONTAL:
          this.min = boundsInDiagram.getMinX();
          this.max = boundsInDiagram.getMaxX();
          break;
        case VERTICAL:
          this.min = boundsInDiagram.getMinY();
          this.max = boundsInDiagram.getMaxY();
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
          final Procedure1<Line> _function = (Line it) -> {
            it.setStartX((this.min - this.overlap));
            it.setStartY(this.getPosition());
            it.setEndX((this.max + this.overlap));
            it.setEndY(this.getPosition());
          };
          _switchResult = ObjectExtensions.<Line>operator_doubleArrow(_line, _function);
          break;
        case VERTICAL:
          Line _line_1 = new Line();
          final Procedure1<Line> _function_1 = (Line it) -> {
            it.setStartX(this.getPosition());
            it.setStartY((this.min - this.overlap));
            it.setEndX(this.getPosition());
            it.setEndY((this.max + this.overlap));
          };
          _switchResult = ObjectExtensions.<Line>operator_doubleArrow(_line_1, _function_1);
          break;
        default:
          break;
      }
    }
    final Procedure1<Line> _function_2 = (Line it) -> {
      it.setStroke(Color.RED);
      it.setStrokeWidth(2);
    };
    return ObjectExtensions.<Line>operator_doubleArrow(_switchResult, _function_2);
  }
}
