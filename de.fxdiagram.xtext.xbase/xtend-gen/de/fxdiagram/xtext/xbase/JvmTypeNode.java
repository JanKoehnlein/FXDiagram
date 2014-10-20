package de.fxdiagram.xtext.xbase;

import com.google.inject.Inject;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import de.fxdiagram.xtext.glue.shapes.BaseNode;
import de.fxdiagram.xtext.xbase.CompartmentInflator;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class JvmTypeNode extends BaseNode<JvmDeclaredType> {
  @Inject
  @Extension
  private JvmDomainUtil _jvmDomainUtil;
  
  private Pane contentArea;
  
  public JvmTypeNode(final JvmEObjectDescriptor<JvmDeclaredType> descriptor) {
    super(descriptor);
  }
  
  protected Node createNode() {
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
      public void apply(final RectangleBorderPane it) {
        it.setBorderRadius(6);
        it.setBackgroundRadius(6);
        ObservableList<Node> _children = it.getChildren();
        VBox _vBox = new VBox();
        final Procedure1<VBox> _function = new Procedure1<VBox>() {
          public void apply(final VBox it) {
            Insets _insets = new Insets(10, 20, 10, 20);
            it.setPadding(_insets);
            it.setAlignment(Pos.CENTER);
            ObservableList<Node> _children = it.getChildren();
            Text _text = new Text();
            final Procedure1<Text> _function = new Procedure1<Text>() {
              public void apply(final Text it) {
                it.setTextOrigin(VPos.TOP);
                String _name = JvmTypeNode.this.getName();
                it.setText(_name);
                Font _font = it.getFont();
                String _family = _font.getFamily();
                Font _font_1 = it.getFont();
                double _size = _font_1.getSize();
                double _multiply = (_size * 1.1);
                Font _font_2 = Font.font(_family, FontWeight.BOLD, _multiply);
                it.setFont(_font_2);
              }
            };
            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
            _children.add((JvmTypeNode.this.label = _doubleArrow));
          }
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
        _children.add((JvmTypeNode.this.contentArea = _doubleArrow));
      }
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
  }
  
  public void activate() {
    super.activate();
    Insets _insets = new Insets(0, 0, 10, 0);
    VBox.setMargin(this.label, _insets);
    AbstractXtextDescriptor<JvmDeclaredType> _descriptor = this.getDescriptor();
    final Function1<JvmDeclaredType, Iterable<Text>> _function = new Function1<JvmDeclaredType, Iterable<Text>>() {
      public Iterable<Text> apply(final JvmDeclaredType type) {
        Iterable<JvmField> _attributes = JvmTypeNode.this._jvmDomainUtil.getAttributes(type);
        final Function1<JvmField, Text> _function = new Function1<JvmField, Text>() {
          public Text apply(final JvmField field) {
            Text _text = new Text();
            final Procedure1<Text> _function = new Procedure1<Text>() {
              public void apply(final Text it) {
                it.setTextOrigin(VPos.TOP);
                StringConcatenation _builder = new StringConcatenation();
                String _simpleName = field.getSimpleName();
                _builder.append(_simpleName, "");
                _builder.append(": ");
                JvmTypeReference _type = field.getType();
                String _simpleName_1 = _type.getSimpleName();
                _builder.append(_simpleName_1, "");
                it.setText(_builder.toString());
                it.setOpacity(0);
              }
            };
            return ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
          }
        };
        return IterableExtensions.<JvmField, Text>map(_attributes, _function);
      }
    };
    Iterable<Text> _withDomainObject = _descriptor.<Iterable<Text>>withDomainObject(_function);
    List<Text> _list = IterableExtensions.<Text>toList(_withDomainObject);
    final ArrayList<Text> fields = new ArrayList<Text>(_list);
    Dimension2D _dimension = CompartmentInflator.getDimension(this.label);
    double _width = _dimension.getWidth();
    CompartmentInflator.inflate(this, fields, this.contentArea, _width);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public JvmTypeNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
