package de.fxdiagram.lib.media;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.AbstractOpenBehavior;
import de.fxdiagram.core.export.SvgExportable;
import de.fxdiagram.core.export.SvgExporter;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.services.ResourceDescriptor;
import de.fxdiagram.core.viewport.ViewportTransition;
import de.fxdiagram.lib.media.FirstRecursiveImageNode;
import javafx.animation.Interpolator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode({ "x", "y", "scale" })
@SuppressWarnings("all")
public class RecursiveImageNode extends XNode implements SvgExportable {
  private Image image;
  
  private DoubleProperty actualWidthProperty = new SimpleDoubleProperty();
  
  private DoubleProperty actualHeightProperty = new SimpleDoubleProperty();
  
  private FirstRecursiveImageNode pivot;
  
  private boolean isZoomedIn;
  
  public RecursiveImageNode(final ResourceDescriptor imageDescriptor) {
    super(imageDescriptor);
  }
  
  @Override
  protected Node createNode() {
    DomainObjectDescriptor _domainObjectDescriptor = this.getDomainObjectDescriptor();
    String _uRI = ((ResourceDescriptor) _domainObjectDescriptor).toURI();
    Image _image = new Image(_uRI);
    this.image = _image;
    FirstRecursiveImageNode _firstRecursiveImageNode = new FirstRecursiveImageNode(this);
    this.pivot = _firstRecursiveImageNode;
    Group _createPane = this.createPane();
    final Procedure1<Group> _function = (Group it) -> {
      ObservableList<Node> _children = it.getChildren();
      _children.add(this.pivot);
    };
    return ObjectExtensions.<Group>operator_doubleArrow(_createPane, _function);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    TooltipExtensions.setTooltip(this, "Double-click to zoom in");
    final EventHandler<MouseEvent> _function = (MouseEvent it) -> {
      int _clickCount = it.getClickCount();
      boolean _equals = (_clickCount == 2);
      if (_equals) {
        if (this.isZoomedIn) {
          this.zoomOut();
        } else {
          this.zoomIn();
        }
      }
    };
    this.setOnMouseClicked(_function);
    final AbstractOpenBehavior _function_1 = new AbstractOpenBehavior() {
      @Override
      public void open() {
        if (RecursiveImageNode.this.isZoomedIn) {
          RecursiveImageNode.this.zoomOut();
        } else {
          RecursiveImageNode.this.zoomIn();
        }
      }
    };
    final AbstractOpenBehavior openBehavior = _function_1;
    this.addBehavior(openBehavior);
    this.pivot.activate();
  }
  
  protected void zoomIn() {
    if (this.isZoomedIn) {
      return;
    }
    double _get = this.actualWidthProperty.get();
    double _multiply = (_get * 0.5);
    double _scale = this.getScale();
    double _minus = (1 - _scale);
    double _x = this.getX();
    double _multiply_1 = (2 * _x);
    double _get_1 = this.actualWidthProperty.get();
    double _divide = (_multiply_1 / _get_1);
    double _plus = (_minus + _divide);
    double _multiply_2 = (_multiply * _plus);
    double _scale_1 = this.getScale();
    double _minus_1 = (1 - _scale_1);
    double _divide_1 = (1 / _minus_1);
    double _multiply_3 = (_multiply_2 * _divide_1);
    double _get_2 = this.actualHeightProperty.get();
    double _multiply_4 = (_get_2 * 0.5);
    double _scale_2 = this.getScale();
    double _minus_2 = (1 - _scale_2);
    double _y = this.getY();
    double _multiply_5 = (2 * _y);
    double _get_3 = this.actualHeightProperty.get();
    double _divide_2 = (_multiply_5 / _get_3);
    double _plus_1 = (_minus_2 + _divide_2);
    double _multiply_6 = (_multiply_4 * _plus_1);
    double _scale_3 = this.getScale();
    double _minus_3 = (1 - _scale_3);
    double _divide_3 = (1 / _minus_3);
    double _multiply_7 = (_multiply_6 * _divide_3);
    final Point2D centerInDiagram = CoreExtensions.localToRootDiagram(this, _multiply_3, _multiply_7);
    XRoot _root = CoreExtensions.getRoot(this);
    ViewportTransition _viewportTransition = new ViewportTransition(_root, centerInDiagram, 500);
    final Procedure1<ViewportTransition> _function = (ViewportTransition it) -> {
      Duration _seconds = Duration.seconds(5);
      it.setDuration(_seconds);
      final Interpolator _function_1 = new Interpolator() {
        @Override
        protected double curve(final double it) {
          double _log = Math.log(10000);
          double _multiply = (_log * it);
          double _exp = Math.exp(_multiply);
          return (_exp / 10000);
        }
      };
      it.setInterpolator(_function_1);
      it.play();
    };
    ObjectExtensions.<ViewportTransition>operator_doubleArrow(_viewportTransition, _function);
    this.isZoomedIn = true;
  }
  
  protected void zoomOut() {
    if ((!this.isZoomedIn)) {
      return;
    }
    Bounds _boundsInLocal = this.getBoundsInLocal();
    Point2D _center = BoundsExtensions.center(_boundsInLocal);
    final Point2D centerInDiagram = CoreExtensions.localToRootDiagram(this, _center);
    XRoot _root = CoreExtensions.getRoot(this);
    ViewportTransition _viewportTransition = new ViewportTransition(_root, centerInDiagram, 1);
    final Procedure1<ViewportTransition> _function = (ViewportTransition it) -> {
      Duration _seconds = Duration.seconds(5);
      it.setDuration(_seconds);
      final Interpolator _function_1 = new Interpolator() {
        @Override
        protected double curve(final double it) {
          double _log = Math.log((it * 10000));
          double _log_1 = Math.log(10000);
          return (_log / _log_1);
        }
      };
      it.setInterpolator(_function_1);
      it.play();
    };
    ObjectExtensions.<ViewportTransition>operator_doubleArrow(_viewportTransition, _function);
    this.isZoomedIn = false;
  }
  
  protected Group createPane() {
    Group _xblockexpression = null;
    {
      Group _group = new Group();
      final Procedure1<Group> _function = (Group it) -> {
        final ImageView imageView = new ImageView();
        ObservableList<Node> _children = it.getChildren();
        final Procedure1<ImageView> _function_1 = (ImageView it_1) -> {
          it_1.setImage(this.image);
          it_1.setPreserveRatio(true);
          DoubleProperty _fitWidthProperty = it_1.fitWidthProperty();
          DoubleProperty _widthProperty = this.widthProperty();
          _fitWidthProperty.bind(_widthProperty);
          DoubleProperty _fitHeightProperty = it_1.fitHeightProperty();
          DoubleProperty _heightProperty = this.heightProperty();
          _fitHeightProperty.bind(_heightProperty);
        };
        ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(imageView, _function_1);
        _children.add(_doubleArrow);
        Rectangle _rectangle = new Rectangle();
        final Procedure1<Rectangle> _function_2 = (Rectangle it_1) -> {
          it_1.setX(0);
          it_1.setY(0);
          DoubleProperty _widthProperty = it_1.widthProperty();
          _widthProperty.bind(this.actualWidthProperty);
          DoubleProperty _heightProperty = it_1.heightProperty();
          _heightProperty.bind(this.actualHeightProperty);
          it_1.setStrokeType(StrokeType.INSIDE);
        };
        Rectangle _doubleArrow_1 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_2);
        it.setClip(_doubleArrow_1);
        Bounds _boundsInLocal = imageView.getBoundsInLocal();
        this.updateActualDimension(_boundsInLocal);
        ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = imageView.boundsInLocalProperty();
        final ChangeListener<Bounds> _function_3 = (ObservableValue<? extends Bounds> property, Bounds oldValue, Bounds newValue) -> {
          this.updateActualDimension(newValue);
        };
        _boundsInLocalProperty.addListener(_function_3);
      };
      final Group pane = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      _xblockexpression = pane;
    }
    return _xblockexpression;
  }
  
  protected void updateActualDimension(final Bounds newValue) {
    double _width = newValue.getWidth();
    this.actualWidthProperty.set(_width);
    double _height = newValue.getHeight();
    this.actualHeightProperty.set(_height);
  }
  
  @Override
  public CharSequence toSvgElement(@Extension final SvgExporter exporter) {
    Node _node = this.getNode();
    return exporter.snapshotToSvgElement(_node);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public RecursiveImageNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(xProperty, Double.class);
    modelElement.addProperty(yProperty, Double.class);
    modelElement.addProperty(scaleProperty, Double.class);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private SimpleDoubleProperty xProperty = new SimpleDoubleProperty(this, "x");
  
  public double getX() {
    return this.xProperty.get();
  }
  
  public void setX(final double x) {
    this.xProperty.set(x);
  }
  
  public DoubleProperty xProperty() {
    return this.xProperty;
  }
  
  private SimpleDoubleProperty yProperty = new SimpleDoubleProperty(this, "y");
  
  public double getY() {
    return this.yProperty.get();
  }
  
  public void setY(final double y) {
    this.yProperty.set(y);
  }
  
  public DoubleProperty yProperty() {
    return this.yProperty;
  }
  
  private SimpleDoubleProperty scaleProperty = new SimpleDoubleProperty(this, "scale");
  
  public double getScale() {
    return this.scaleProperty.get();
  }
  
  public void setScale(final double scale) {
    this.scaleProperty.set(scale);
  }
  
  public DoubleProperty scaleProperty() {
    return this.scaleProperty;
  }
}
