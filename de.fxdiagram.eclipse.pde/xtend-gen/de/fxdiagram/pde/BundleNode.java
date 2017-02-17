package de.fxdiagram.pde;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.lib.animations.Inflator;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.mapping.reconcile.MappingLabelListener;
import de.fxdiagram.mapping.shapes.BaseNode;
import de.fxdiagram.mapping.shapes.INodeWithLazyMappings;
import de.fxdiagram.pde.AddDependencyPathAction;
import de.fxdiagram.pde.BundleDescriptor;
import java.util.Collections;
import java.util.List;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
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
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode("inflated")
@SuppressWarnings("all")
public class BundleNode extends BaseNode<BundleDescription> implements INodeWithLazyMappings {
  public final static String BUNDLE_SYMBOLIC_NAME = "bundleSymbolicName";
  
  public final static String BUNDLE_VERSION = "bundleVersion";
  
  public final static String BUNDLE_NAME = "bundleName";
  
  public final static String BUNDLE_PROVIDER = "bundleProvider";
  
  public final static String BUNDLE_EXECUTION_ENVIRONMENT = "bundleExecutionEnvironment";
  
  private Inflator detailsInflator;
  
  public BundleNode(final BundleDescriptor descriptor) {
    super(descriptor);
    this.setPlacementHint(Side.BOTTOM);
  }
  
  @Override
  public BundleDescriptor getDomainObjectDescriptor() {
    IMappedElementDescriptor<BundleDescription> _domainObjectDescriptor = super.getDomainObjectDescriptor();
    return ((BundleDescriptor) _domainObjectDescriptor);
  }
  
  @Override
  public Node createNode() {
    RectangleBorderPane _xblockexpression = null;
    {
      VBox _vBox = new VBox();
      final Procedure1<VBox> _function = (VBox it) -> {
        it.setAlignment(Pos.CENTER);
      };
      final VBox titleArea = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
      VBox _vBox_1 = new VBox();
      final Procedure1<VBox> _function_1 = (VBox it) -> {
        Insets _insets = new Insets(10, 20, 10, 20);
        it.setPadding(_insets);
        ObservableList<Node> _children = it.getChildren();
        _children.add(titleArea);
      };
      final VBox contentArea = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_1);
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function_2 = (RectangleBorderPane it) -> {
        TooltipExtensions.setTooltip(it, "Right-click to toggle details");
        ObservableList<Node> _children = it.getChildren();
        _children.add(contentArea);
        List<Stop> _xifexpression = null;
        BundleDescriptor _domainObjectDescriptor = this.getDomainObjectDescriptor();
        final Function1<IPluginModelBase, Boolean> _function_3 = (IPluginModelBase it_1) -> {
          return Boolean.valueOf(it_1.isFragmentModel());
        };
        Boolean _withPlugin = _domainObjectDescriptor.<Boolean>withPlugin(_function_3);
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
      final RectangleBorderPane pane = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function_2);
      Inflator _inflator = new Inflator(this, contentArea);
      this.detailsInflator = _inflator;
      VBox _vBox_2 = new VBox();
      final Procedure1<VBox> _function_3 = (VBox it) -> {
        Insets _insets = new Insets(10, 0, 0, 0);
        it.setPadding(_insets);
        it.setAlignment(Pos.CENTER);
      };
      final VBox detailsArea = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_2, _function_3);
      this.detailsInflator.addInflatable(detailsArea, 1);
      ObservableList<XLabel> _labels = this.getLabels();
      Pair<String, Pane> _mappedTo = Pair.<String, Pane>of(BundleNode.BUNDLE_SYMBOLIC_NAME, titleArea);
      Pair<String, Pane> _mappedTo_1 = Pair.<String, Pane>of(BundleNode.BUNDLE_VERSION, detailsArea);
      Pair<String, Pane> _mappedTo_2 = Pair.<String, Pane>of(BundleNode.BUNDLE_NAME, detailsArea);
      Pair<String, Pane> _mappedTo_3 = Pair.<String, Pane>of(BundleNode.BUNDLE_PROVIDER, detailsArea);
      Pair<String, Pane> _mappedTo_4 = Pair.<String, Pane>of(BundleNode.BUNDLE_EXECUTION_ENVIRONMENT, detailsArea);
      MappingLabelListener.<XLabel>addMappingLabelListener(_labels, _mappedTo, _mappedTo_1, _mappedTo_2, _mappedTo_3, _mappedTo_4);
      _xblockexpression = pane;
    }
    return _xblockexpression;
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    final EventHandler<MouseEvent> _function = (MouseEvent it) -> {
      MouseButton _button = it.getButton();
      boolean _equals = Objects.equal(_button, MouseButton.SECONDARY);
      if (_equals) {
        this.toggleInflated();
      }
    };
    this.setOnMouseClicked(_function);
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
  
  protected void toggleInflated() {
    boolean _inflated = this.getInflated();
    boolean _not = (!_inflated);
    if (_not) {
      this.setInflated(true);
      Transition _inflateAnimation = this.detailsInflator.getInflateAnimation();
      _inflateAnimation.play();
    } else {
      this.setInflated(false);
      Transition _deflateAnimation = this.detailsInflator.getDeflateAnimation();
      _deflateAnimation.play();
    }
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
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
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
