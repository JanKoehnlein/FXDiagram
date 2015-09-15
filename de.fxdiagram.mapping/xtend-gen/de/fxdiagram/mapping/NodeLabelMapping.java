package de.fxdiagram.mapping;

import de.fxdiagram.core.XLabel;
import de.fxdiagram.mapping.AbstractLabelMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class NodeLabelMapping<T extends Object> extends AbstractLabelMapping<T> {
  public NodeLabelMapping(final XDiagramConfig config, final String id, final String displayName) {
    super(config, id, displayName);
  }
  
  @Override
  public XLabel createLabel(final IMappedElementDescriptor<T> descriptor) {
    XLabel _xLabel = new XLabel(descriptor);
    final Procedure1<XLabel> _function = (XLabel it) -> {
      Text _text = it.getText();
      final Function1<T, String> _function_1 = (T it_1) -> {
        return this.getText(it_1);
      };
      String _withDomainObject = descriptor.<String>withDomainObject(_function_1);
      _text.setText(_withDomainObject);
    };
    return ObjectExtensions.<XLabel>operator_doubleArrow(_xLabel, _function);
  }
}
