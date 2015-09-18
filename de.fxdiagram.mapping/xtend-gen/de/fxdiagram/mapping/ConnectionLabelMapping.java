package de.fxdiagram.mapping;

import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.mapping.AbstractLabelMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.execution.XDiagramConfig;
import de.fxdiagram.mapping.shapes.BaseConnectionLabel;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor}
 * to a connection's {@link XConnectionLabel}.
 * 
 * @see AbstractMapping
 */
@SuppressWarnings("all")
public class ConnectionLabelMapping<T extends Object> extends AbstractLabelMapping<T> {
  public ConnectionLabelMapping(final XDiagramConfig config, final String id) {
    super(config, id, id);
  }
  
  @Override
  public XConnectionLabel createLabel(final IMappedElementDescriptor<T> descriptor, final T labelElement) {
    BaseConnectionLabel<T> _baseConnectionLabel = new BaseConnectionLabel<T>(descriptor);
    final Procedure1<BaseConnectionLabel<T>> _function = (BaseConnectionLabel<T> it) -> {
      Text _text = it.getText();
      String _text_1 = this.getText(labelElement);
      _text.setText(_text_1);
    };
    return ObjectExtensions.<BaseConnectionLabel<T>>operator_doubleArrow(_baseConnectionLabel, _function);
  }
  
  public String getText(final T labelElement) {
    return null;
  }
}
