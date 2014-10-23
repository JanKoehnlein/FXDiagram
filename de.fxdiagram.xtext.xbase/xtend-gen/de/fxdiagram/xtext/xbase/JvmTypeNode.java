package de.fxdiagram.xtext.xbase;

import com.google.inject.Inject;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.extensions.TextExtensions;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import de.fxdiagram.xtext.glue.shapes.BaseNode;
import de.fxdiagram.xtext.xbase.InflatableCompartment;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
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
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.validation.UIStrings;

@ModelNode
@SuppressWarnings("all")
public class JvmTypeNode extends BaseNode<JvmDeclaredType> {
  @Inject
  @Extension
  private JvmDomainUtil _jvmDomainUtil;
  
  @Inject
  @Extension
  private UIStrings _uIStrings;
  
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
            it.setSpacing(10);
            ObservableList<Node> _children = it.getChildren();
            VBox _vBox = new VBox();
            final Procedure1<VBox> _function = new Procedure1<VBox>() {
              public void apply(final VBox it) {
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
            _children.add(_doubleArrow);
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
    float _offlineWidth = TextExtensions.getOfflineWidth(this.label);
    final InflatableCompartment attributeCompartment = new InflatableCompartment(this, _offlineWidth);
    ObservableList<Node> _children = this.contentArea.getChildren();
    _children.add(attributeCompartment);
    AbstractXtextDescriptor<JvmDeclaredType> _descriptor = this.getDescriptor();
    final Function1<JvmDeclaredType, Object> _function = new Function1<JvmDeclaredType, Object>() {
      public Object apply(final JvmDeclaredType type) {
        Object _xblockexpression = null;
        {
          Iterable<JvmField> _attributes = JvmTypeNode.this._jvmDomainUtil.getAttributes(type);
          final Consumer<JvmField> _function = new Consumer<JvmField>() {
            public void accept(final JvmField field) {
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
                  String _signature = JvmTypeNode.this._jvmDomainUtil.getSignature(field);
                  TooltipExtensions.setTooltip(it, _signature);
                }
              };
              Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
              attributeCompartment.add(_doubleArrow);
            }
          };
          _attributes.forEach(_function);
          _xblockexpression = null;
        }
        return _xblockexpression;
      }
    };
    _descriptor.<Object>withDomainObject(_function);
    attributeCompartment.inflate();
    float _offlineWidth_1 = TextExtensions.getOfflineWidth(this.label);
    final InflatableCompartment operationCompartment = new InflatableCompartment(this, _offlineWidth_1);
    ObservableList<Node> _children_1 = this.contentArea.getChildren();
    _children_1.add(operationCompartment);
    AbstractXtextDescriptor<JvmDeclaredType> _descriptor_1 = this.getDescriptor();
    final Function1<JvmDeclaredType, Object> _function_1 = new Function1<JvmDeclaredType, Object>() {
      public Object apply(final JvmDeclaredType type) {
        Object _xblockexpression = null;
        {
          Iterable<JvmOperation> _methods = JvmTypeNode.this._jvmDomainUtil.getMethods(type);
          final Consumer<JvmOperation> _function = new Consumer<JvmOperation>() {
            public void accept(final JvmOperation operation) {
              Text _text = new Text();
              final Procedure1<Text> _function = new Procedure1<Text>() {
                public void apply(final Text it) {
                  it.setTextOrigin(VPos.TOP);
                  StringConcatenation _builder = new StringConcatenation();
                  String _simpleName = operation.getSimpleName();
                  _builder.append(_simpleName, "");
                  _builder.append(": ");
                  JvmTypeReference _returnType = operation.getReturnType();
                  String _simpleName_1 = _returnType.getSimpleName();
                  _builder.append(_simpleName_1, "");
                  it.setText(_builder.toString());
                  it.setOpacity(0);
                  String _signature = JvmTypeNode.this._uIStrings.signature(operation);
                  TooltipExtensions.setTooltip(it, _signature);
                }
              };
              Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
              operationCompartment.add(_doubleArrow);
            }
          };
          _methods.forEach(_function);
          _xblockexpression = null;
        }
        return _xblockexpression;
      }
    };
    _descriptor_1.<Object>withDomainObject(_function_1);
    operationCompartment.inflate();
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
