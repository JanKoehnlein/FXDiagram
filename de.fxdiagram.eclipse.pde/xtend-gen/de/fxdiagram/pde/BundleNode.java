package de.fxdiagram.pde;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.eclipse.mapping.ConnectionMapping;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.eclipse.shapes.BaseNode;
import de.fxdiagram.eclipse.shapes.INodeWithLazyMappings;
import de.fxdiagram.lib.animations.Inflator;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.pde.AddDependencyPathAction;
import de.fxdiagram.pde.BundleDescriptor;
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
public class BundleNode extends BaseNode<BundleDescription> implements INodeWithLazyMappings {
  private Pane contentArea;
  
  private VBox titleArea;
  
  private Text nameLabel;
  
  private Text versionLabel;
  
  private VBox detailsArea;
  
  private Inflator titleInflator;
  
  private Inflator detailsInflator;
  
  public BundleNode(final BundleDescriptor descriptor) {
    super(descriptor);
  }
  
  @Override
  public BundleDescriptor getDomainObject() {
    IMappedElementDescriptor<BundleDescription> _domainObject = super.getDomainObject();
    return ((BundleDescriptor) _domainObject);
  }
  
  @Override
  public Node createNode() {
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
      TooltipExtensions.setTooltip(it, "Right-click to toggle details");
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
            BundleDescriptor _domainObject = this.getDomainObject();
            String _symbolicName = _domainObject.getSymbolicName();
            it_3.setText(_symbolicName);
            BundleDescriptor _domainObject_1 = this.getDomainObject();
            final Function1<BundleDescription, Boolean> _function_4 = (BundleDescription it_4) -> {
              return Boolean.valueOf(it_4.isSingleton());
            };
            final Boolean isSingleton = _domainObject_1.<Boolean>withDomainObject(_function_4);
            if ((isSingleton).booleanValue()) {
              Font _font = it_3.getFont();
              String _family = _font.getFamily();
              Font _font_1 = it_3.getFont();
              double _size = _font_1.getSize();
              double _multiply = (_size * 1.1);
              Font _font_2 = Font.font(_family, FontWeight.BOLD, FontPosture.ITALIC, _multiply);
              it_3.setFont(_font_2);
            } else {
              Font _font_3 = it_3.getFont();
              String _family_1 = _font_3.getFamily();
              Font _font_4 = it_3.getFont();
              double _size_1 = _font_4.getSize();
              double _multiply_1 = (_size_1 * 1.1);
              Font _font_5 = Font.font(_family_1, FontWeight.BOLD, _multiply_1);
              it_3.setFont(_font_5);
            }
          };
          Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_3);
          _children_2.add((this.nameLabel = _doubleArrow));
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_2);
        _children_1.add((this.titleArea = _doubleArrow));
      };
      VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_1);
      _children.add((this.contentArea = _doubleArrow));
      List<Stop> _xifexpression = null;
      BundleDescriptor _domainObject = this.getDomainObject();
      final Function1<IPluginModelBase, Boolean> _function_2 = (IPluginModelBase it_1) -> {
        return Boolean.valueOf(it_1.isFragmentModel());
      };
      Boolean _withPlugin = _domainObject.<Boolean>withPlugin(_function_2);
      if ((_withPlugin).booleanValue()) {
        Color _rgb = Color.rgb(255, 193, 210);
        Stop _stop = new Stop(0, _rgb);
        Color _rgb_1 = Color.rgb(255, 215, 215);
        Stop _stop_1 = new Stop(1, _rgb_1);
        _xifexpression = Collections.<Stop>unmodifiableList(CollectionLiterals.<Stop>newArrayList(_stop, _stop_1));
      } else {
        Color _rgb_2 = Color.rgb(158, 188, 227);
        Stop _stop_2 = new Stop(0, _rgb_2);
        Color _rgb_3 = Color.rgb(220, 230, 255);
        Stop _stop_3 = new Stop(1, _rgb_3);
        _xifexpression = Collections.<Stop>unmodifiableList(CollectionLiterals.<Stop>newArrayList(_stop_2, _stop_3));
      }
      final List<Stop> backgroundStops = _xifexpression;
      LinearGradient _linearGradient = new LinearGradient(
        0, 0, 1, 1, 
        true, CycleMethod.NO_CYCLE, backgroundStops);
      it.setBackgroundPaint(_linearGradient);
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    Inflator _inflator = new Inflator(this, this.titleArea);
    this.titleInflator = _inflator;
    VBox _vBox = new VBox();
    final Procedure1<VBox> _function = (VBox it) -> {
      it.setAlignment(Pos.CENTER);
      ObservableList<Node> _children = it.getChildren();
      Text _text = new Text();
      final Procedure1<Text> _function_1 = (Text it_1) -> {
        it_1.setTextOrigin(VPos.TOP);
        BundleDescriptor _domainObject = this.getDomainObject();
        String _version = _domainObject.getVersion();
        it_1.setText(_version);
        Font _font = it_1.getFont();
        String _family = _font.getFamily();
        Font _font_1 = it_1.getFont();
        double _size = _font_1.getSize();
        double _multiply = (_size * 0.8);
        Font _font_2 = Font.font(_family, _multiply);
        it_1.setFont(_font_2);
      };
      Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_1);
      _children.add((this.versionLabel = _doubleArrow));
    };
    VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
    this.titleInflator.addInflatable(_doubleArrow, 1);
    Inflator _inflator_1 = new Inflator(this, this.contentArea);
    this.detailsInflator = _inflator_1;
    VBox _vBox_1 = new VBox();
    final Procedure1<VBox> _function_1 = (VBox it) -> {
      Insets _insets = new Insets(10, 0, 0, 0);
      it.setPadding(_insets);
      it.setAlignment(Pos.CENTER);
      ObservableList<Node> _children = it.getChildren();
      Text _text = new Text();
      final Procedure1<Text> _function_2 = (Text it_1) -> {
        it_1.setTextOrigin(VPos.TOP);
        BundleDescriptor _domainObject = this.getDomainObject();
        final Function1<IPluginModelBase, String> _function_3 = (IPluginModelBase it_2) -> {
          IPluginBase _pluginBase = it_2.getPluginBase();
          IPluginBase _pluginBase_1 = it_2.getPluginBase();
          String _name = _pluginBase_1.getName();
          return _pluginBase.getResourceString(_name);
        };
        String _withPlugin = _domainObject.<String>withPlugin(_function_3);
        it_1.setText(_withPlugin);
      };
      Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_2);
      _children.add(_doubleArrow_1);
      ObservableList<Node> _children_1 = it.getChildren();
      Text _text_1 = new Text();
      final Procedure1<Text> _function_3 = (Text it_1) -> {
        it_1.setTextOrigin(VPos.TOP);
        BundleDescriptor _domainObject = this.getDomainObject();
        final Function1<IPluginModelBase, String> _function_4 = (IPluginModelBase it_2) -> {
          IPluginBase _pluginBase = it_2.getPluginBase();
          IPluginBase _pluginBase_1 = it_2.getPluginBase();
          String _providerName = _pluginBase_1.getProviderName();
          return _pluginBase.getResourceString(_providerName);
        };
        String _withPlugin = _domainObject.<String>withPlugin(_function_4);
        it_1.setText(_withPlugin);
      };
      Text _doubleArrow_2 = ObjectExtensions.<Text>operator_doubleArrow(_text_1, _function_3);
      _children_1.add(_doubleArrow_2);
      ObservableList<Node> _children_2 = it.getChildren();
      Text _text_2 = new Text();
      final Procedure1<Text> _function_4 = (Text it_1) -> {
        it_1.setTextOrigin(VPos.TOP);
        BundleDescriptor _domainObject = this.getDomainObject();
        final Function1<BundleDescription, String> _function_5 = (BundleDescription it_2) -> {
          String[] _executionEnvironments = it_2.getExecutionEnvironments();
          return IterableExtensions.join(((Iterable<?>)Conversions.doWrapArray(_executionEnvironments)), ", ");
        };
        String _withDomainObject = _domainObject.<String>withDomainObject(_function_5);
        it_1.setText(_withDomainObject);
      };
      Text _doubleArrow_3 = ObjectExtensions.<Text>operator_doubleArrow(_text_2, _function_4);
      _children_2.add(_doubleArrow_3);
    };
    VBox _doubleArrow_1 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_1);
    this.detailsArea = _doubleArrow_1;
    this.detailsInflator.addInflatable(this.detailsArea, 1);
    final EventHandler<MouseEvent> _function_2 = (MouseEvent it) -> {
      MouseButton _button = it.getButton();
      boolean _equals = Objects.equal(_button, MouseButton.SECONDARY);
      if (_equals) {
        this.toggleInflated();
      }
    };
    this.setOnMouseClicked(_function_2);
    final AddDependencyPathAction dependencyPathAction = new AddDependencyPathAction(false);
    final LazyConnectionMappingBehavior rapidButtonBehavior = this.<LazyConnectionMappingBehavior>getBehavior(LazyConnectionMappingBehavior.class);
    SVGPath _filledTriangle = ButtonExtensions.getFilledTriangle(Side.TOP, "Add dependency path");
    RapidButton _rapidButton = new RapidButton(this, Side.TOP, _filledTriangle, dependencyPathAction);
    rapidButtonBehavior.add(_rapidButton);
    SVGPath _filledTriangle_1 = ButtonExtensions.getFilledTriangle(Side.BOTTOM, "Add dependency path");
    RapidButton _rapidButton_1 = new RapidButton(this, Side.BOTTOM, _filledTriangle_1, dependencyPathAction);
    rapidButtonBehavior.add(_rapidButton_1);
    final AddDependencyPathAction inverseDependencyPathAction = new AddDependencyPathAction(true);
    SVGPath _filledTriangle_2 = ButtonExtensions.getFilledTriangle(Side.BOTTOM, "Add inverse dependency path");
    RapidButton _rapidButton_2 = new RapidButton(this, Side.TOP, _filledTriangle_2, inverseDependencyPathAction);
    rapidButtonBehavior.add(_rapidButton_2);
    SVGPath _filledTriangle_3 = ButtonExtensions.getFilledTriangle(Side.TOP, "Add inverse dependency path");
    RapidButton _rapidButton_3 = new RapidButton(this, Side.BOTTOM, _filledTriangle_3, inverseDependencyPathAction);
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
        final Procedure1<ParallelTransition> _function = (ParallelTransition it) -> {
          ObservableList<Animation> _children = it.getChildren();
          Transition _inflateAnimation = this.titleInflator.getInflateAnimation();
          _children.add(_inflateAnimation);
          ObservableList<Animation> _children_1 = it.getChildren();
          Transition _inflateAnimation_1 = this.detailsInflator.getInflateAnimation();
          _children_1.add(_inflateAnimation_1);
          it.play();
        };
        _xblockexpression = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
      }
      _xifexpression = _xblockexpression;
    } else {
      ParallelTransition _xblockexpression_1 = null;
      {
        this.setInflated(false);
        ParallelTransition _parallelTransition = new ParallelTransition();
        final Procedure1<ParallelTransition> _function = (ParallelTransition it) -> {
          ObservableList<Animation> _children = it.getChildren();
          Transition _deflateAnimation = this.titleInflator.getDeflateAnimation();
          _children.add(_deflateAnimation);
          ObservableList<Animation> _children_1 = it.getChildren();
          Transition _deflateAnimation_1 = this.detailsInflator.getDeflateAnimation();
          _children_1.add(_deflateAnimation_1);
          it.play();
        };
        _xblockexpression_1 = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
      }
      _xifexpression = _xblockexpression_1;
    }
    return _xifexpression;
  }
  
  @Override
  public List<Side> getButtonSides(final ConnectionMapping<?> mapping) {
    return Collections.<Side>unmodifiableList(CollectionLiterals.<Side>newArrayList(Side.LEFT, Side.RIGHT));
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public BundleNode() {
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
