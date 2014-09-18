package de.fxdiagram.lib.buttons;

import de.fxdiagram.lib.buttons.RapidButton;

@SuppressWarnings("all")
public abstract class RapidButtonAction {
  public abstract void perform(final RapidButton button);
  
  public boolean isEnabled(final RapidButton button) {
    return true;
  }
}
