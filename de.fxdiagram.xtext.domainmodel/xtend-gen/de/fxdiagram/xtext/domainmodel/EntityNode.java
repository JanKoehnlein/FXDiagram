package de.fxdiagram.xtext.domainmodel;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.xtext.domainmodel.DomainModelUtil;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.shapes.BaseNode;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.domainmodel.Feature;
import org.eclipse.xtext.example.domainmodel.domainmodel.Property;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class EntityNode extends BaseNode<Entity> {
  @Inject
  @Extension
  private DomainModelUtil util;
  
  public EntityNode(final XtextDomainObjectDescriptor<Entity> descriptor) {
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
                String _name = EntityNode.this.getName();
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
            _children.add((EntityNode.this.label = _doubleArrow));
            Insets _insets_1 = new Insets(0, 0, 10, 0);
            VBox.setMargin(EntityNode.this.label, _insets_1);
            ObservableList<Node> _children_1 = it.getChildren();
            VBox _vBox = new VBox();
            final Procedure1<VBox> _function_1 = new Procedure1<VBox>() {
              public void apply(final VBox attributeCompartment) {
                XtextDomainObjectDescriptor<Entity> _descriptor = EntityNode.this.getDescriptor();
                final Function1<Entity, Object> _function = new Function1<Entity, Object>() {
                  public Object apply(final Entity entity) {
                    Object _xblockexpression = null;
                    {
                      EList<Feature> _features = entity.getFeatures();
                      Iterable<Property> _filter = Iterables.<Property>filter(_features, Property.class);
                      final Function1<Property, Boolean> _function = new Function1<Property, Boolean>() {
                        public Boolean apply(final Property it) {
                          JvmTypeReference _type = it.getType();
                          Entity _referencedEntity = EntityNode.this.util.getReferencedEntity(_type);
                          return Boolean.valueOf(Objects.equal(_referencedEntity, null));
                        }
                      };
                      Iterable<Property> _filter_1 = IterableExtensions.<Property>filter(_filter, _function);
                      final Consumer<Property> _function_1 = new Consumer<Property>() {
                        public void accept(final Property attribute) {
                          ObservableList<Node> _children = attributeCompartment.getChildren();
                          Text _text = new Text();
                          final Procedure1<Text> _function = new Procedure1<Text>() {
                            public void apply(final Text it) {
                              it.setTextOrigin(VPos.TOP);
                              StringConcatenation _builder = new StringConcatenation();
                              String _name = attribute.getName();
                              _builder.append(_name, "");
                              _builder.append(": ");
                              JvmTypeReference _type = attribute.getType();
                              String _simpleName = _type.getSimpleName();
                              _builder.append(_simpleName, "");
                              it.setText(_builder.toString());
                            }
                          };
                          Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
                          _children.add(_doubleArrow);
                        }
                      };
                      _filter_1.forEach(_function_1);
                      _xblockexpression = null;
                    }
                    return _xblockexpression;
                  }
                };
                _descriptor.<Object>withDomainObject(_function);
              }
            };
            VBox _doubleArrow_1 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_1);
            _children_1.add(_doubleArrow_1);
          }
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
        _children.add(_doubleArrow);
      }
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public EntityNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
