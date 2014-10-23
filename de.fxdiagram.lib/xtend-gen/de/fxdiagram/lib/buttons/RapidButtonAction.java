package de.fxdiagram.lib.buttons;

import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.buttons.RapidButton;

@SuppressWarnings("all")
public abstract class RapidButtonAction {
  public abstract void perform(final RapidButton button);
  
  public boolean isEnabled(final XNode host) {
    return true;
  }
}
