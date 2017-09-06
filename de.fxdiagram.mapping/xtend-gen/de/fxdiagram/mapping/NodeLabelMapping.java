package de.fxdiagram.mapping;

import de.fxdiagram.core.XLabel;
import de.fxdiagram.mapping.AbstractLabelMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.shapes.BaseNodeLabel;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor}
 * to a node's {@link XLabel}.
 * 
 * @see AbstractMapping
 */
@SuppressWarnings("all")
public class NodeLabelMapping<T extends Object> extends AbstractLabelMapping<T> {
  public NodeLabelMapping(final XDiagramConfig config, final String id) {
    super(config, id, id);
  }
  
  @Override
  public BaseNodeLabel<T> createLabel(final IMappedElementDescriptor<T> descriptor, final T labelElement) {
    BaseNodeLabel<T> _baseNodeLabel = new BaseNodeLabel<T>(descriptor);
    final Procedure1<BaseNodeLabel<T>> _function = (BaseNodeLabel<T> it) -> {
      Text _text = it.getText();
      _text.setText(this.getText(labelElement));
    };
    return ObjectExtensions.<BaseNodeLabel<T>>operator_doubleArrow(_baseNodeLabel, _function);
  }
  
  public String getText(final T labelElement) {
    return null;
  }
}
