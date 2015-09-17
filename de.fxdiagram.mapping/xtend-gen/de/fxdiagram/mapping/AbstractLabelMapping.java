package de.fxdiagram.mapping;

import de.fxdiagram.core.XLabel;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import javafx.scene.text.Text;

@SuppressWarnings("all")
public abstract class AbstractLabelMapping<T extends Object> extends AbstractMapping<T> {
  public AbstractLabelMapping(final XDiagramConfig config, final String id, final String displayName) {
    super(config, id, displayName);
  }
  
  public abstract XLabel createLabel(final IMappedElementDescriptor<T> descriptor);
  
  public void styleText(final Text text, final T element) {
  }
  
  public String getText(final T element) {
    return "";
  }
}
