package de.fxdiagram.mapping;

import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.NodeLabelMapping;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.shapes.BaseNodeLabel;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class NodeHeadingMapping<T extends Object> extends NodeLabelMapping<T> {
  public NodeHeadingMapping(final XDiagramConfig config, final String id) {
    super(config, id);
  }
  
  @Override
  public BaseNodeLabel<T> createLabel(final IMappedElementDescriptor<T> descriptor) {
    BaseNodeLabel<T> _createLabel = super.createLabel(descriptor);
    final Procedure1<BaseNodeLabel<T>> _function = (BaseNodeLabel<T> it) -> {
      Insets _insets = new Insets(10, 20, 10, 20);
      StackPane.setMargin(it, _insets);
      Text _text = it.getText();
      Text _text_1 = it.getText();
      Font _font = _text_1.getFont();
      String _family = _font.getFamily();
      Text _text_2 = it.getText();
      Font _font_1 = _text_2.getFont();
      double _size = _font_1.getSize();
      double _multiply = (_size * 1.1);
      Font _font_2 = Font.font(_family, FontWeight.BOLD, _multiply);
      _text.setFont(_font_2);
    };
    return ObjectExtensions.<BaseNodeLabel<T>>operator_doubleArrow(_createLabel, _function);
  }
}
