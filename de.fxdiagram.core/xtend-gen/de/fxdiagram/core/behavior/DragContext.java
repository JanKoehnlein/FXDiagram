package de.fxdiagram.core.behavior;

import de.fxdiagram.core.command.MoveCommand;
import javafx.geometry.Point2D;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class DragContext {
  private final MoveCommand _moveCommand;
  
  public MoveCommand getMoveCommand() {
    return this._moveCommand;
  }
  
  private final double _mouseAnchorX;
  
  public double getMouseAnchorX() {
    return this._mouseAnchorX;
  }
  
  private final double _mouseAnchorY;
  
  public double getMouseAnchorY() {
    return this._mouseAnchorY;
  }
  
  private final Point2D _initialPosInScene;
  
  public Point2D getInitialPosInScene() {
    return this._initialPosInScene;
  }
  
  public DragContext(final MoveCommand moveCommand, final double mouseAnchorX, final double mouseAnchorY, final Point2D initialPosInScene) {
    super();
    this._moveCommand = moveCommand;
    this._mouseAnchorX = mouseAnchorX;
    this._mouseAnchorY = mouseAnchorY;
    this._initialPosInScene = initialPosInScene;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_moveCommand== null) ? 0 : _moveCommand.hashCode());
    result = prime * result + (int) (Double.doubleToLongBits(_mouseAnchorX) ^ (Double.doubleToLongBits(_mouseAnchorX) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(_mouseAnchorY) ^ (Double.doubleToLongBits(_mouseAnchorY) >>> 32));
    result = prime * result + ((_initialPosInScene== null) ? 0 : _initialPosInScene.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DragContext other = (DragContext) obj;
    if (_moveCommand == null) {
      if (other._moveCommand != null)
        return false;
    } else if (!_moveCommand.equals(other._moveCommand))
      return false;
    if (Double.doubleToLongBits(other._mouseAnchorX) != Double.doubleToLongBits(_mouseAnchorX))
      return false;
    if (Double.doubleToLongBits(other._mouseAnchorY) != Double.doubleToLongBits(_mouseAnchorY))
      return false;
    if (_initialPosInScene == null) {
      if (other._initialPosInScene != null)
        return false;
    } else if (!_initialPosInScene.equals(other._initialPosInScene))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
