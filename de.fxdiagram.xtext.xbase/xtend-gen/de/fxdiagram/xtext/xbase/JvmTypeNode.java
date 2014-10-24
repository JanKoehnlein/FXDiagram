package de.fxdiagram.xtext.xbase;

import com.google.inject.Inject;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.extensions.TextExtensions;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.nodes.InflatableCompartment;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import de.fxdiagram.xtext.glue.shapes.BaseFlipNode;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
import java.util.Collections;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.validation.UIStrings;

@ModelNode({ "showPackage", "showAttributes", "showMethods" })
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
  
  public Node createNode() {
    Node _xblockexpression = null;
    {
      final Node pane = super.createNode();
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
        public void apply(final RectangleBorderPane it) {
          Color _rgb = Color.rgb(225, 158, 168);
          Stop _stop = new Stop(0, _rgb);
          Color _rgb_1 = Color.rgb(255, 193, 201);
          Stop _stop_1 = new Stop(1, _rgb_1);
          LinearGradient _linearGradient = new LinearGradient(
            0, 0, 1, 1, 
            true, CycleMethod.NO_CYCLE, 
            Collections.<Stop>unmodifiableList(CollectionLiterals.<Stop>newArrayList(_stop, _stop_1)));
          it.setBackgroundPaint(_linearGradient);
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
              _children.add((JvmTypeNode.this.titleArea = _doubleArrow));
            }
          };
          VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
          _children.add((JvmTypeNode.this.contentArea = _doubleArrow));
        }
      };
      RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
      this.setFront(_doubleArrow);
      RectangleBorderPane _rectangleBorderPane_1 = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function_1 = new Procedure1<RectangleBorderPane>() {
        public void apply(final RectangleBorderPane it) {
          ObservableList<Node> _children = it.getChildren();
          VBox _vBox = new VBox();
          final Procedure1<VBox> _function = new Procedure1<VBox>() {
            public void apply(final VBox it) {
              Insets _insets = new Insets(10, 20, 10, 20);
              it.setPadding(_insets);
              it.setSpacing(5);
              ObservableList<Node> _children = it.getChildren();
              AbstractXtextDescriptor<JvmDeclaredType> _descriptor = JvmTypeNode.this.getDescriptor();
              String _uri = _descriptor.getUri();
              URI _createURI = URI.createURI(_uri);
              String _lastSegment = _createURI.lastSegment();
              Label _label = new Label(_lastSegment);
              _children.add(_label);
              ObservableList<Node> _children_1 = it.getChildren();
              CheckBox _checkBox = new CheckBox("Package");
              _children_1.add((JvmTypeNode.this.packageBox = _checkBox));
              ObservableList<Node> _children_2 = it.getChildren();
              CheckBox _checkBox_1 = new CheckBox("Attributes");
              _children_2.add((JvmTypeNode.this.attributesBox = _checkBox_1));
              ObservableList<Node> _children_3 = it.getChildren();
              CheckBox _checkBox_2 = new CheckBox("Methods");
              _children_3.add((JvmTypeNode.this.methodsBox = _checkBox_2));
            }
          };
          VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
          _children.add(_doubleArrow);
        }
      };
      RectangleBorderPane _doubleArrow_1 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane_1, _function_1);
      this.setBack(_doubleArrow_1);
      Text _text = new Text();
      final Procedure1<Text> _function_2 = new Procedure1<Text>() {
        public void apply(final Text it) {
          it.setTextOrigin(VPos.TOP);
          Font _font = it.getFont();
          String _family = _font.getFamily();
          Font _font_1 = it.getFont();
          double _size = _font_1.getSize();
          double _multiply = (_size * 0.8);
          Font _font_2 = Font.font(_family, _multiply);
          it.setFont(_font_2);
          AbstractXtextDescriptor<JvmDeclaredType> _descriptor = JvmTypeNode.this.getDescriptor();
          String _fqn = _descriptor.getFqn();
          AbstractXtextDescriptor<JvmDeclaredType> _descriptor_1 = JvmTypeNode.this.getDescriptor();
          String _fqn_1 = _descriptor_1.getFqn();
          int _lastIndexOf = _fqn_1.lastIndexOf(".");
          String _substring = _fqn.substring(0, _lastIndexOf);
          it.setText(_substring);
        }
      };
      Text _doubleArrow_2 = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_2);
      this.packageLabel = _doubleArrow_2;
      _xblockexpression = pane;
    }
    return _xblockexpression;
  }
  
  public void activate() {
    super.activate();
    boolean _showPackage = this.getShowPackage();
    if (_showPackage) {
      ObservableList<Node> _children = this.titleArea.getChildren();
      _children.add(0, this.packageLabel);
    }
    final Function0<Integer> _function = new Function0<Integer>() {
      public Integer apply() {
        return Integer.valueOf(0);
      }
    };
    this.bindCheckbox(this.showPackageProperty, this.packageBox, this.packageLabel, this.titleArea, _function);
    float _offlineWidth = TextExtensions.getOfflineWidth(this.label);
    final InflatableCompartment attributeCompartment = new InflatableCompartment(this, _offlineWidth);
    AbstractXtextDescriptor<JvmDeclaredType> _descriptor = this.getDescriptor();
    final Function1<JvmDeclaredType, Object> _function_1 = new Function1<JvmDeclaredType, Object>() {
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
    _descriptor.<Object>withDomainObject(_function_1);
    final Function0<Integer> _function_2 = new Function0<Integer>() {
      public Integer apply() {
        return Integer.valueOf(1);
      }
    };
    this.activate(attributeCompartment, this.showAttributesProperty, this.attributesBox, _function_2);
    float _offlineWidth_1 = TextExtensions.getOfflineWidth(this.label);
    final InflatableCompartment methodCompartment = new InflatableCompartment(this, _offlineWidth_1);
    AbstractXtextDescriptor<JvmDeclaredType> _descriptor_1 = this.getDescriptor();
    final Function1<JvmDeclaredType, Object> _function_3 = new Function1<JvmDeclaredType, Object>() {
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
                  String _signature = JvmTypeNode.this._uIStrings.signature(operation);
                  TooltipExtensions.setTooltip(it, _signature);
                }
              };
              Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
              methodCompartment.add(_doubleArrow);
            }
          };
          _methods.forEach(_function);
          _xblockexpression = null;
        }
        return _xblockexpression;
      }
    };
    _descriptor_1.<Object>withDomainObject(_function_3);
    final Function0<Integer> _function_4 = new Function0<Integer>() {
      public Integer apply() {
        ObservableList<Node> _children = JvmTypeNode.this.contentArea.getChildren();
        return Integer.valueOf(_children.size());
      }
    };
    this.activate(methodCompartment, this.showMethodsProperty, this.methodsBox, _function_4);
  }
  
  protected void activate(final InflatableCompartment compartment, final BooleanProperty showProperty, final CheckBox box, final Function0<? extends Integer> index) {
    boolean _get = showProperty.get();
    if (_get) {
      ObservableList<Node> _children = this.contentArea.getChildren();
      _children.add(compartment);
      compartment.inflate();
    } else {
      compartment.populate();
    }
    this.bindCheckbox(showProperty, box, compartment, this.contentArea, index);
  }
  
  protected void bindCheckbox(final BooleanProperty property, final CheckBox box, final Node node, final Pane container, final Function0<? extends Integer> index) {
    BooleanProperty _selectedProperty = box.selectedProperty();
    _selectedProperty.bindBidirectional(property);
    final ChangeListener<Boolean> _function = new ChangeListener<Boolean>() {
      public void changed(final ObservableValue<? extends Boolean> p, final Boolean o, final Boolean show) {
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
}
