package de.fxdiagram.lib.chooser;

import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.chooser.AbstractBaseChooser;
import javafx.scene.Node;

@SuppressWarnings("all")
public interface ChoiceGraphics extends XActivatable {
  public abstract void setInterpolatedPosition(final double interpolatedPosition);
  
  public abstract void nodeChosen(final XNode choice);
  
  public abstract void relocateButtons(final Node minusButton, final Node plusButton);
  
  public abstract boolean hasButtons();
  
  public abstract void setChooser(final AbstractBaseChooser chooser);
}
