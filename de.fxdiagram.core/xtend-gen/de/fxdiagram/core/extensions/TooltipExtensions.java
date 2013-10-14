package de.fxdiagram.core.extensions;

import de.fxdiagram.core.extensions.SoftTooltip;
import javafx.scene.Node;

@SuppressWarnings("all")
public class TooltipExtensions {
  public static void setTooltip(final Node host, final String text) {
    SoftTooltip _softTooltip = new SoftTooltip(host, text);
    _softTooltip.install(host);
  }
}
