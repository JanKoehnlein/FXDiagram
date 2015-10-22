package de.fxdiagram.core.export;

import de.fxdiagram.core.export.SvgLink;
import javafx.scene.Node;

@SuppressWarnings("all")
public interface SvgLinkProvider {
  public abstract SvgLink getLink(final Node node);
}
