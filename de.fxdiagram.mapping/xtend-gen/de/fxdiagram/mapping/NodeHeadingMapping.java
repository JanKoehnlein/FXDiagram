package de.fxdiagram.mapping;

import de.fxdiagram.core.XLabel;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.NodeLabelMapping;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.shapes.BaseNodeHeadingLabel;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor}
 * to a node's {@link XLabel}.
 * 
 * As opposed to {@link NodeLabelMapping} this label will use a bigger, bold font.
 * 
 * @see AbstractMapping
 */
@SuppressWarnings("all")
public class NodeHeadingMapping<T extends Object> extends NodeLabelMapping<T> {
  public NodeHeadingMapping(final XDiagramConfig config, final String id) {
    super(config, id);
  }
  
  @Override
  public BaseNodeHeadingLabel<T> createLabel(final IMappedElementDescriptor<T> descriptor, final T labelElement) {
    BaseNodeHeadingLabel<T> _baseNodeHeadingLabel = new BaseNodeHeadingLabel<T>(descriptor);
    final Procedure1<BaseNodeHeadingLabel<T>> _function = (BaseNodeHeadingLabel<T> it) -> {
      Text _text = it.getText();
      _text.setText(this.getText(labelElement));
    };
    return ObjectExtensions.<BaseNodeHeadingLabel<T>>operator_doubleArrow(_baseNodeHeadingLabel, _function);
  }
}
