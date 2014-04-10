package de.fxdiagram.core;

import de.fxdiagram.core.XRapidButton;

@SuppressWarnings("all")
public abstract class XRapidButtonAction {
  public abstract void perform(final XRapidButton button);
  
  public boolean isEnabled(final XRapidButton button) {
    return true;
  }
}
