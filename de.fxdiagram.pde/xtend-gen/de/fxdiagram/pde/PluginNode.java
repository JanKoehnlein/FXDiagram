package de.fxdiagram.pde;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.animations.Inflator;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.pde.AddImportPathAction;
import de.fxdiagram.pde.PluginDescriptor;
import de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor;
import de.fxdiagram.xtext.glue.shapes.BaseNode;
import de.fxdiagram.xtext.glue.shapes.INodeWithLazyMappings;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode("inflated")
@SuppressWarnings("all")
public class PluginNode extends BaseNode<IPluginModelBase> implements INodeWithLazyMappings {
  private Pane contentArea;
  
  private VBox titleArea;
  
  private Text nameLabel;
  
  private Text versionLabel;
  
  private VBox detailsArea;
  
  private Inflator titleInflator;
  
  private Inflator detailsInflator;
  
  public PluginNode(final PluginDescriptor descriptor) {
    super(descriptor);
  }
  
  public PluginDescriptor getDomainObject() {
    IMappedElementDescriptor<IPluginModelBase> _domainObject = super.getDomainObject();
    return ((PluginDescriptor) _domainObject);
  }
  
  public Node createNode() {
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
      public void apply(final RectangleBorderPane it) {
        TooltipExtensions.setTooltip(it, "Right-click to toggle details");
        ObservableList<Node> _children = it.getChildren();
        VBox _vBox = new VBox();
        final Procedure1<VBox> _function = new Procedure1<VBox>() {
          public void apply(final VBox it) {
            Insets _insets = new Insets(10, 20, 10, 20);
            it.setPadding(_insets);
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
                    PluginDescriptor _domainObject = PluginNode.this.getDomainObject();
                    String _symbolicName = _domainObject.getSymbolicName();
                    it.setText(_symbolicName);
                    PluginDescriptor _domainObject_1 = PluginNode.this.getDomainObject();
                    final Function1<IPluginModelBase, Boolean> _function = new Function1<IPluginModelBase, Boolean>() {
                      public Boolean apply(final IPluginModelBase it) {
                        BundleDescription _bundleDescription = it.getBundleDescription();
                        return Boolean.valueOf(_bundleDescription.isSingleton());
                      }
                    };
                    final Boolean isSingleton = _domainObject_1.<Boolean>withDomainObject(_function);
                    if ((isSingleton).booleanValue()) {
                      Font _font = it.getFont();
                      String _family = _font.getFamily();
                      Font _font_1 = it.getFont();
                      double _size = _font_1.getSize();
                      double _multiply = (_size * 1.1);
                      Font _font_2 = Font.font(_family, FontWeight.BOLD, FontPosture.ITALIC, _multiply);
                      it.setFont(_font_2);
                    } else {
                      Font _font_3 = it.getFont();
                      String _family_1 = _font_3.getFamily();
                      Font _font_4 = it.getFont();
                      double _size_1 = _font_4.getSize();
                      double _multiply_1 = (_size_1 * 1.1);
                      Font _font_5 = Font.font(_family_1, FontWeight.BOLD, _multiply_1);
                      it.setFont(_font_5);
                    }
                  }
                };
                Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
                _children.add((PluginNode.this.nameLabel = _doubleArrow));
              }
            };
            VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
            _children.add((PluginNode.this.titleArea = _doubleArrow));
          }
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
        _children.add((PluginNode.this.contentArea = _doubleArrow));
        Color _rgb = Color.rgb(158, 188, 227);
        Stop _stop = new Stop(0, _rgb);
        Color _rgb_1 = Color.rgb(220, 230, 255);
        Stop _stop_1 = new Stop(1, _rgb_1);
        LinearGradient _linearGradient = new LinearGradient(
          0, 0, 1, 1, 
          true, CycleMethod.NO_CYCLE, 
          Collections.<Stop>unmodifiableList(CollectionLiterals.<Stop>newArrayList(_stop, _stop_1)));
        it.setBackgroundPaint(_linearGradient);
      }
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
  }
  
  public void doActivate() {
    super.doActivate();
    Inflator _inflator = new Inflator(this, this.titleArea);
    this.titleInflator = _inflator;
    VBox _vBox = new VBox();
    final Procedure1<VBox> _function = new Procedure1<VBox>() {
      public void apply(final VBox it) {
        it.setAlignment(Pos.CENTER);
        ObservableList<Node> _children = it.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            it.setTextOrigin(VPos.TOP);
            PluginDescriptor _domainObject = PluginNode.this.getDomainObject();
            String _version = _domainObject.getVersion();
            it.setText(_version);
            Font _font = it.getFont();
            String _family = _font.getFamily();
            Font _font_1 = it.getFont();
            double _size = _font_1.getSize();
            double _multiply = (_size * 0.8);
            Font _font_2 = Font.font(_family, _multiply);
            it.setFont(_font_2);
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
        _children.add((PluginNode.this.versionLabel = _doubleArrow));
      }
    };
    VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
    this.titleInflator.addInflatable(_doubleArrow, 1);
    Inflator _inflator_1 = new Inflator(this, this.contentArea);
    this.detailsInflator = _inflator_1;
    VBox _vBox_1 = new VBox();
    final Procedure1<VBox> _function_1 = new Procedure1<VBox>() {
      public void apply(final VBox it) {
        Insets _insets = new Insets(10, 0, 0, 0);
        it.setPadding(_insets);
        it.setAlignment(Pos.CENTER);
        ObservableList<Node> _children = it.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            it.setTextOrigin(VPos.TOP);
            PluginDescriptor _domainObject = PluginNode.this.getDomainObject();
            String _name = _domainObject.getName();
            it.setText(_name);
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
        _children.add(_doubleArrow);
        ObservableList<Node> _children_1 = it.getChildren();
        Text _text_1 = new Text();
        final Procedure1<Text> _function_1 = new Procedure1<Text>() {
          public void apply(final Text it) {
            it.setTextOrigin(VPos.TOP);
            PluginDescriptor _domainObject = PluginNode.this.getDomainObject();
            final Function1<IPluginModelBase, String> _function = new Function1<IPluginModelBase, String>() {
              public String apply(final IPluginModelBase it) {
                IPluginBase _pluginBase = it.getPluginBase();
                IPluginBase _pluginBase_1 = it.getPluginBase();
                String _providerName = _pluginBase_1.getProviderName();
                return _pluginBase.getResourceString(_providerName);
              }
            };
            String _withDomainObject = _domainObject.<String>withDomainObject(_function);
            it.setText(_withDomainObject);
          }
        };
        Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_text_1, _function_1);
        _children_1.add(_doubleArrow_1);
        ObservableList<Node> _children_2 = it.getChildren();
        Text _text_2 = new Text();
        final Procedure1<Text> _function_2 = new Procedure1<Text>() {
          public void apply(final Text it) {
            it.setTextOrigin(VPos.TOP);
            PluginDescriptor _domainObject = PluginNode.this.getDomainObject();
            final Function1<IPluginModelBase, String> _function = new Function1<IPluginModelBase, String>() {
              public String apply(final IPluginModelBase it) {
                BundleDescription _bundleDescription = it.getBundleDescription();
                String[] _executionEnvironments = _bundleDescription.getExecutionEnvironments();
                return IterableExtensions.join(((Iterable<?>)Conversions.doWrapArray(_executionEnvironments)), ", ");
              }
            };
            String _withDomainObject = _domainObject.<String>withDomainObject(_function);
            it.setText(_withDomainObject);
          }
        };
        Text _doubleArrow_2 = ObjectExtensions.<Text>operator_doubleArrow(_text_2, _function_2);
        _children_2.add(_doubleArrow_2);
      }
    };
    VBox _doubleArrow_1 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_1);
    this.detailsArea = _doubleArrow_1;
    this.detailsInflator.addInflatable(this.detailsArea, 1);
    final EventHandler<MouseEvent> _function_2 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        MouseButton _button = it.getButton();
        boolean _equals = Objects.equal(_button, MouseButton.SECONDARY);
        if (_equals) {
          PluginNode.this.toggleInflated();
        }
      }
    };
    this.setOnMouseClicked(_function_2);
    final AddImportPathAction importPathAction = new AddImportPathAction(true);
    final LazyConnectionMappingBehavior rapidButtonBehavior = this.<LazyConnectionMappingBehavior>getBehavior(LazyConnectionMappingBehavior.class);
    SVGPath _filledTriangle = ButtonExtensions.getFilledTriangle(Side.TOP, "Add import path");
    RapidButton _rapidButton = new RapidButton(this, Side.TOP, _filledTriangle, importPathAction);
    rapidButtonBehavior.add(_rapidButton);
    SVGPath _filledTriangle_1 = ButtonExtensions.getFilledTriangle(Side.BOTTOM, "Add import path");
    RapidButton _rapidButton_1 = new RapidButton(this, Side.BOTTOM, _filledTriangle_1, importPathAction);
    rapidButtonBehavior.add(_rapidButton_1);
    final AddImportPathAction dependentPathAction = new AddImportPathAction(false);
    SVGPath _filledTriangle_2 = ButtonExtensions.getFilledTriangle(Side.BOTTOM, "Add imported path");
    RapidButton _rapidButton_2 = new RapidButton(this, Side.TOP, _filledTriangle_2, dependentPathAction);
    rapidButtonBehavior.add(_rapidButton_2);
    SVGPath _filledTriangle_3 = ButtonExtensions.getFilledTriangle(Side.TOP, "Add imported path");
    RapidButton _rapidButton_3 = new RapidButton(this, Side.BOTTOM, _filledTriangle_3, dependentPathAction);
    rapidButtonBehavior.add(_rapidButton_3);
  }
  
  protected ParallelTransition toggleInflated() {
    ParallelTransition _xifexpression = null;
    boolean _inflated = this.getInflated();
    boolean _not = (!_inflated);
    if (_not) {
      ParallelTransition _xblockexpression = null;
      {
        this.setInflated(true);
        ParallelTransition _parallelTransition = new ParallelTransition();
        final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
          public void apply(final ParallelTransition it) {
            ObservableList<Animation> _children = it.getChildren();
            Transition _inflateAnimation = PluginNode.this.titleInflator.getInflateAnimation();
            _children.add(_inflateAnimation);
            ObservableList<Animation> _children_1 = it.getChildren();
            Transition _inflateAnimation_1 = PluginNode.this.detailsInflator.getInflateAnimation();
            _children_1.add(_inflateAnimation_1);
            it.play();
          }
        };
        _xblockexpression = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
      }
      _xifexpression = _xblockexpression;
    } else {
      ParallelTransition _xblockexpression_1 = null;
      {
        this.setInflated(false);
        ParallelTransition _parallelTransition = new ParallelTransition();
        final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
          public void apply(final ParallelTransition it) {
            ObservableList<Animation> _children = it.getChildren();
            Transition _deflateAnimation = PluginNode.this.titleInflator.getDeflateAnimation();
            _children.add(_deflateAnimation);
            ObservableList<Animation> _children_1 = it.getChildren();
            Transition _deflateAnimation_1 = PluginNode.this.detailsInflator.getDeflateAnimation();
            _children_1.add(_deflateAnimation_1);
            it.play();
          }
        };
        _xblockexpression_1 = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
      }
      _xifexpression = _xblockexpression_1;
    }
    return _xifexpression;
  }
  
  public List<Side> getButtonSides(final ConnectionMapping<?> mapping) {
    return Collections.<Side>unmodifiableList(CollectionLiterals.<Side>newArrayList(Side.LEFT, Side.RIGHT));
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public PluginNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(inflatedProperty, Boolean.class);
  }
  
  private SimpleBooleanProperty inflatedProperty = new SimpleBooleanProperty(this, "inflated",_initInflated());
  
  private static final boolean _initInflated() {
    return false;
  }
  
  public boolean getInflated() {
    return this.inflatedProperty.get();
  }
  
  public void setInflated(final boolean inflated) {
    this.inflatedProperty.set(inflated);
  }
  
  public BooleanProperty inflatedProperty() {
    return this.inflatedProperty;
  }
}
