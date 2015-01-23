package de.fxdiagram.xtext.xbase;

import com.google.inject.Inject;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.eclipse.shapes.BaseFlipNode;
import de.fxdiagram.lib.animations.Inflator;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
import java.util.function.Consumer;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.validation.UIStrings;

@ModelNode({ "showPackage", "showAttributes", "showMethods", "bgColor" })
@SuppressWarnings("all")
public class JvmTypeNode extends BaseFlipNode<JvmDeclaredType> {
  @Inject
  @Extension
  private JvmDomainUtil _jvmDomainUtil;
  
  @Inject
  @Extension
  private UIStrings _uIStrings;
  
  private VBox titleArea;
  
  private Text label;
  
  private Text packageLabel;
  
  private CheckBox packageBox;
  
  private CheckBox attributesBox;
  
  private CheckBox methodsBox;
  
  private Pane contentArea;
  
  public JvmTypeNode(final JvmEObjectDescriptor<JvmDeclaredType> descriptor) {
    super(descriptor);
  }
  
  @Override
  public JvmEObjectDescriptor<JvmDeclaredType> getDomainObject() {
    IMappedElementDescriptor<JvmDeclaredType> _domainObject = super.getDomainObject();
    return ((JvmEObjectDescriptor<JvmDeclaredType>) _domainObject);
  }
  
  @Override
  public Node createNode() {
    Node _xblockexpression = null;
    {
      final Node pane = super.createNode();
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
        TooltipExtensions.setTooltip(it, "Right-click to configure");
        ObjectProperty<Paint> _backgroundPaintProperty = it.backgroundPaintProperty();
        _backgroundPaintProperty.bind(this.bgColorProperty);
        ObservableList<Node> _children = it.getChildren();
        VBox _vBox = new VBox();
        final Procedure1<VBox> _function_1 = (VBox it_1) -> {
          Insets _insets = new Insets(10, 20, 10, 20);
          it_1.setPadding(_insets);
          ObservableList<Node> _children_1 = it_1.getChildren();
          VBox _vBox_1 = new VBox();
          final Procedure1<VBox> _function_2 = (VBox it_2) -> {
            it_2.setAlignment(Pos.CENTER);
            ObservableList<Node> _children_2 = it_2.getChildren();
            Text _text = new Text();
            final Procedure1<Text> _function_3 = (Text it_3) -> {
              it_3.setTextOrigin(VPos.TOP);
              String _name = this.getName();
              it_3.setText(_name);
              Font _font = it_3.getFont();
              String _family = _font.getFamily();
              Font _font_1 = it_3.getFont();
              double _size = _font_1.getSize();
              double _multiply = (_size * 1.1);
              Font _font_2 = Font.font(_family, FontWeight.BOLD, _multiply);
              it_3.setFont(_font_2);
            };
            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_3);
            _children_2.add((this.label = _doubleArrow));
          };
          VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_2);
          _children_1.add((this.titleArea = _doubleArrow));
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_1);
        _children.add((this.contentArea = _doubleArrow));
      };
      RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
      this.setFront(_doubleArrow);
      RectangleBorderPane _rectangleBorderPane_1 = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function_1 = (RectangleBorderPane it) -> {
        TooltipExtensions.setTooltip(it, "Right-click to show node");
        ObjectProperty<Paint> _backgroundPaintProperty = it.backgroundPaintProperty();
        _backgroundPaintProperty.bind(this.bgColorProperty);
        ObservableList<Node> _children = it.getChildren();
        VBox _vBox = new VBox();
        final Procedure1<VBox> _function_2 = (VBox it_1) -> {
          Insets _insets = new Insets(10, 20, 10, 20);
          it_1.setPadding(_insets);
          it_1.setSpacing(5);
          ObservableList<Node> _children_1 = it_1.getChildren();
          JvmEObjectDescriptor<JvmDeclaredType> _domainObject = this.getDomainObject();
          String _uri = _domainObject.getUri();
          URI _createURI = URI.createURI(_uri);
          String _lastSegment = _createURI.lastSegment();
          Label _label = new Label(_lastSegment);
          _children_1.add(_label);
          ObservableList<Node> _children_2 = it_1.getChildren();
          CheckBox _checkBox = new CheckBox("Package");
          _children_2.add((this.packageBox = _checkBox));
          ObservableList<Node> _children_3 = it_1.getChildren();
          CheckBox _checkBox_1 = new CheckBox("Attributes");
          _children_3.add((this.attributesBox = _checkBox_1));
          ObservableList<Node> _children_4 = it_1.getChildren();
          CheckBox _checkBox_2 = new CheckBox("Methods");
          _children_4.add((this.methodsBox = _checkBox_2));
          ObservableList<Node> _children_5 = it_1.getChildren();
          ColorPicker _colorPicker = new ColorPicker();
          final Procedure1<ColorPicker> _function_3 = (ColorPicker it_2) -> {
            ObjectProperty<Color> _valueProperty = it_2.valueProperty();
            _valueProperty.bindBidirectional(this.bgColorProperty);
          };
          ColorPicker _doubleArrow_1 = ObjectExtensions.<ColorPicker>operator_doubleArrow(_colorPicker, _function_3);
          _children_5.add(_doubleArrow_1);
        };
        VBox _doubleArrow_1 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_2);
        _children.add(_doubleArrow_1);
      };
      RectangleBorderPane _doubleArrow_1 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane_1, _function_1);
      this.setBack(_doubleArrow_1);
      Text _text = new Text();
      final Procedure1<Text> _function_2 = (Text it) -> {
        it.setTextOrigin(VPos.TOP);
        Font _font = it.getFont();
        String _family = _font.getFamily();
        Font _font_1 = it.getFont();
        double _size = _font_1.getSize();
        double _multiply = (_size * 0.8);
        Font _font_2 = Font.font(_family, _multiply);
        it.setFont(_font_2);
        JvmEObjectDescriptor<JvmDeclaredType> _domainObject = this.getDomainObject();
        String _fqn = _domainObject.getFqn();
        final int lastIndexOf = _fqn.lastIndexOf(".");
        String _xifexpression = null;
        if ((lastIndexOf != (-1))) {
          JvmEObjectDescriptor<JvmDeclaredType> _domainObject_1 = this.getDomainObject();
          String _fqn_1 = _domainObject_1.getFqn();
          _xifexpression = _fqn_1.substring(0, lastIndexOf);
        } else {
          _xifexpression = "<default>";
        }
        it.setText(_xifexpression);
      };
      Text _doubleArrow_2 = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_2);
      this.packageLabel = _doubleArrow_2;
      _xblockexpression = pane;
    }
    return _xblockexpression;
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    boolean _showPackage = this.getShowPackage();
    if (_showPackage) {
      ObservableList<Node> _children = this.titleArea.getChildren();
      _children.add(0, this.packageLabel);
    }
    final Function0<Integer> _function = () -> {
      return Integer.valueOf(0);
    };
    this.bindCheckbox(this.showPackageProperty, this.packageBox, this.packageLabel, this.titleArea, _function);
    final Inflator inflator = new Inflator(this, this.contentArea);
    VBox _vBox = new VBox();
    final Procedure1<VBox> _function_1 = (VBox c) -> {
      JvmEObjectDescriptor<JvmDeclaredType> _domainObject = this.getDomainObject();
      final Function1<JvmDeclaredType, Object> _function_2 = (JvmDeclaredType type) -> {
        Object _xblockexpression = null;
        {
          Iterable<JvmField> _attributes = this._jvmDomainUtil.getAttributes(type);
          final Consumer<JvmField> _function_3 = (JvmField field) -> {
            ObservableList<Node> _children_1 = c.getChildren();
            Text _text = new Text();
            final Procedure1<Text> _function_4 = (Text it) -> {
              it.setTextOrigin(VPos.TOP);
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = field.getSimpleName();
              _builder.append(_simpleName, "");
              _builder.append(": ");
              JvmTypeReference _type = field.getType();
              String _simpleName_1 = _type.getSimpleName();
              _builder.append(_simpleName_1, "");
              it.setText(_builder.toString());
              String _signature = this._jvmDomainUtil.getSignature(field);
              TooltipExtensions.setTooltip(it, _signature);
            };
            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_4);
            _children_1.add(_doubleArrow);
          };
          _attributes.forEach(_function_3);
          _xblockexpression = null;
        }
        return _xblockexpression;
      };
      _domainObject.<Object>withDomainObject(_function_2);
    };
    final VBox attributeCompartment = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_1);
    final Function0<Integer> _function_2 = () -> {
      return Integer.valueOf(1);
    };
    this.activate(attributeCompartment, this.showAttributesProperty, this.attributesBox, _function_2, inflator);
    VBox _vBox_1 = new VBox();
    final Procedure1<VBox> _function_3 = (VBox c) -> {
      JvmEObjectDescriptor<JvmDeclaredType> _domainObject = this.getDomainObject();
      final Function1<JvmDeclaredType, Object> _function_4 = (JvmDeclaredType type) -> {
        Object _xblockexpression = null;
        {
          Iterable<JvmOperation> _methods = this._jvmDomainUtil.getMethods(type);
          final Consumer<JvmOperation> _function_5 = (JvmOperation operation) -> {
            ObservableList<Node> _children_1 = c.getChildren();
            Text _text = new Text();
            final Procedure1<Text> _function_6 = (Text it) -> {
              it.setTextOrigin(VPos.TOP);
              StringConcatenation _builder = new StringConcatenation();
              String _simpleName = operation.getSimpleName();
              _builder.append(_simpleName, "");
              _builder.append(": ");
              JvmTypeReference _returnType = operation.getReturnType();
              String _simpleName_1 = _returnType.getSimpleName();
              _builder.append(_simpleName_1, "");
              it.setText(_builder.toString());
              String _signature = this._uIStrings.signature(operation);
              TooltipExtensions.setTooltip(it, _signature);
            };
            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_6);
            _children_1.add(_doubleArrow);
          };
          _methods.forEach(_function_5);
          _xblockexpression = null;
        }
        return _xblockexpression;
      };
      _domainObject.<Object>withDomainObject(_function_4);
    };
    final VBox methodCompartment = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_3);
    final Function0<Integer> _function_4 = () -> {
      ObservableList<Node> _children_1 = this.contentArea.getChildren();
      return Integer.valueOf(_children_1.size());
    };
    this.activate(methodCompartment, this.showMethodsProperty, this.methodsBox, _function_4, inflator);
    Transition _inflateAnimation = inflator.getInflateAnimation();
    if (_inflateAnimation!=null) {
      _inflateAnimation.play();
    }
  }
  
  protected Rectangle activate(final VBox compartment, final BooleanProperty showProperty, final CheckBox box, final Function0<? extends Integer> index, final Inflator inflator) {
    Rectangle _xblockexpression = null;
    {
      Insets _insets = new Insets(10, 0, 0, 0);
      compartment.setPadding(_insets);
      this.bindCheckbox(showProperty, box, compartment, this.contentArea, index);
      Rectangle _xifexpression = null;
      boolean _get = showProperty.get();
      if (_get) {
        Integer _apply = index.apply();
        _xifexpression = inflator.addInflatable(compartment, (_apply).intValue());
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected void bindCheckbox(final BooleanProperty property, final CheckBox box, final Node node, final Pane container, final Function0<? extends Integer> index) {
    BooleanProperty _selectedProperty = box.selectedProperty();
    _selectedProperty.bindBidirectional(property);
    final ChangeListener<Boolean> _function = (ObservableValue<? extends Boolean> p, Boolean o, Boolean show) -> {
      ObservableList<Node> _children = container.getChildren();
      boolean _contains = _children.contains(node);
      if (_contains) {
        if ((!(show).booleanValue())) {
          ObservableList<Node> _children_1 = container.getChildren();
          _children_1.remove(node);
        }
      } else {
        if ((show).booleanValue()) {
          ObservableList<Node> _children_2 = container.getChildren();
          Integer _apply = index.apply();
          _children_2.add((_apply).intValue(), node);
        }
      }
    };
    property.addListener(_function);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public JvmTypeNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(showPackageProperty, Boolean.class);
    modelElement.addProperty(showAttributesProperty, Boolean.class);
    modelElement.addProperty(showMethodsProperty, Boolean.class);
    modelElement.addProperty(bgColorProperty, Color.class);
  }
  
  private SimpleBooleanProperty showPackageProperty = new SimpleBooleanProperty(this, "showPackage",_initShowPackage());
  
  private static final boolean _initShowPackage() {
    return false;
  }
  
  public boolean getShowPackage() {
    return this.showPackageProperty.get();
  }
  
  public void setShowPackage(final boolean showPackage) {
    this.showPackageProperty.set(showPackage);
  }
  
  public BooleanProperty showPackageProperty() {
    return this.showPackageProperty;
  }
  
  private SimpleBooleanProperty showAttributesProperty = new SimpleBooleanProperty(this, "showAttributes",_initShowAttributes());
  
  private static final boolean _initShowAttributes() {
    return true;
  }
  
  public boolean getShowAttributes() {
    return this.showAttributesProperty.get();
  }
  
  public void setShowAttributes(final boolean showAttributes) {
    this.showAttributesProperty.set(showAttributes);
  }
  
  public BooleanProperty showAttributesProperty() {
    return this.showAttributesProperty;
  }
  
  private SimpleBooleanProperty showMethodsProperty = new SimpleBooleanProperty(this, "showMethods",_initShowMethods());
  
  private static final boolean _initShowMethods() {
    return true;
  }
  
  public boolean getShowMethods() {
    return this.showMethodsProperty.get();
  }
  
  public void setShowMethods(final boolean showMethods) {
    this.showMethodsProperty.set(showMethods);
  }
  
  public BooleanProperty showMethodsProperty() {
    return this.showMethodsProperty;
  }
  
  private SimpleObjectProperty<Color> bgColorProperty = new SimpleObjectProperty<Color>(this, "bgColor",_initBgColor());
  
  private static final Color _initBgColor() {
    Color _web = Color.web("#ffe6cc");
    return _web;
  }
  
  public Color getBgColor() {
    return this.bgColorProperty.get();
  }
  
  public void setBgColor(final Color bgColor) {
    this.bgColorProperty.set(bgColor);
  }
  
  public ObjectProperty<Color> bgColorProperty() {
    return this.bgColorProperty;
  }
}
