package de.fxdiagram.mapping;

import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.mapping.AbstractLabelMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.shapes.BaseConnectionLabel;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ConnectionLabelMapping<T extends Object> extends AbstractLabelMapping<T> {
  public ConnectionLabelMapping(final XDiagramConfig config, final String id, final String displayName) {
    super(config, id, displayName);
  }
  
  @Override
  public XConnectionLabel createLabel(final IMappedElementDescriptor<T> descriptor) {
    BaseConnectionLabel<T> _baseConnectionLabel = new BaseConnectionLabel<T>(descriptor);
    final Procedure1<BaseConnectionLabel<T>> _function = (BaseConnectionLabel<T> it) -> {
      Text _text = it.getText();
      final Function1<T, String> _function_1 = (T it_1) -> {
        return this.getText(it_1);
      };
      String _withDomainObject = descriptor.<String>withDomainObject(_function_1);
      _text.setText(_withDomainObject);
    };
    return ObjectExtensions.<BaseConnectionLabel<T>>operator_doubleArrow(_baseConnectionLabel, _function);
  }
}
