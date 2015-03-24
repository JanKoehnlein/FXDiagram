package de.fxdiagram.xtext.domainmodel;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.shapes.BaseNode;
import de.fxdiagram.xtext.domainmodel.DomainModelUtil;
import java.util.Collections;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
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
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
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
  
  public EntityNode(final IMappedElementDescriptor<Entity> descriptor) {
    super(descriptor);
  }
  
  @Override
  protected Node createNode() {
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
      Color _rgb = Color.rgb(158, 188, 227);
      Stop _stop = new Stop(0, _rgb);
      Color _rgb_1 = Color.rgb(220, 230, 255);
      Stop _stop_1 = new Stop(1, _rgb_1);
      LinearGradient _linearGradient = new LinearGradient(
        0, 0, 1, 1, 
        true, CycleMethod.NO_CYCLE, 
        Collections.<Stop>unmodifiableList(CollectionLiterals.<Stop>newArrayList(_stop, _stop_1)));
      it.setBackgroundPaint(_linearGradient);
      ObservableList<Node> _children = it.getChildren();
      VBox _vBox = new VBox();
      final Procedure1<VBox> _function_1 = (VBox it_1) -> {
        Insets _insets = new Insets(10, 20, 10, 20);
        it_1.setPadding(_insets);
        it_1.setAlignment(Pos.CENTER);
        ObservableList<Node> _children_1 = it_1.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function_2 = (Text it_2) -> {
          it_2.setTextOrigin(VPos.TOP);
          String _name = this.getName();
          it_2.setText(_name);
          Font _font = it_2.getFont();
          String _family = _font.getFamily();
          Font _font_1 = it_2.getFont();
          double _size = _font_1.getSize();
          double _multiply = (_size * 1.1);
          Font _font_2 = Font.font(_family, FontWeight.BOLD, _multiply);
          it_2.setFont(_font_2);
          Insets _insets_1 = new Insets(0, 0, 10, 0);
          VBox.setMargin(it_2, _insets_1);
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_2);
        _children_1.add(_doubleArrow);
        ObservableList<Node> _children_2 = it_1.getChildren();
        VBox _vBox_1 = new VBox();
        final Procedure1<VBox> _function_3 = (VBox attributeCompartment) -> {
          IMappedElementDescriptor<Entity> _domainObject = this.getDomainObject();
          final Function1<Entity, Object> _function_4 = (Entity entity) -> {
            Object _xblockexpression = null;
            {
              EList<Feature> _features = entity.getFeatures();
              Iterable<Property> _filter = Iterables.<Property>filter(_features, Property.class);
              final Function1<Property, Boolean> _function_5 = (Property it_2) -> {
                JvmTypeReference _type = it_2.getType();
                Entity _referencedEntity = this.util.getReferencedEntity(_type);
                return Boolean.valueOf(Objects.equal(_referencedEntity, null));
              };
              Iterable<Property> _filter_1 = IterableExtensions.<Property>filter(_filter, _function_5);
              final Consumer<Property> _function_6 = (Property attribute) -> {
                ObservableList<Node> _children_3 = attributeCompartment.getChildren();
                Text _text_1 = new Text();
                final Procedure1<Text> _function_7 = (Text it_2) -> {
                  it_2.setTextOrigin(VPos.TOP);
                  StringConcatenation _builder = new StringConcatenation();
                  String _name = attribute.getName();
                  _builder.append(_name, "");
                  _builder.append(": ");
                  JvmTypeReference _type = attribute.getType();
                  String _simpleName = _type.getSimpleName();
                  _builder.append(_simpleName, "");
                  it_2.setText(_builder.toString());
                };
                Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_text_1, _function_7);
                _children_3.add(_doubleArrow_1);
              };
              _filter_1.forEach(_function_6);
              _xblockexpression = null;
            }
            return _xblockexpression;
          };
          _domainObject.<Object>withDomainObject(_function_4);
        };
        VBox _doubleArrow_1 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_3);
        _children_2.add(_doubleArrow_1);
      };
      VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_1);
      _children.add(_doubleArrow);
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
