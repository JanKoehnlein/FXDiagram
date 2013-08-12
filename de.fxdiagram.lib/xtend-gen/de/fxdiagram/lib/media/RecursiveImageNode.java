package de.fxdiagram.lib.media;

import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.export.SvgExportable;
import de.fxdiagram.core.export.SvgExporter;
import de.fxdiagram.core.tools.actions.ScrollToAndScaleTransition;
import de.fxdiagram.lib.media.FirstRecursiveImageNode;
import javafx.animation.Interpolator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class RecursiveImageNode extends XNode implements SvgExportable {
  private DoubleProperty actualWidthProperty = new Function0<DoubleProperty>() {
    public DoubleProperty apply() {
      SimpleDoubleProperty _simpleDoubleProperty = new SimpleDoubleProperty();
      return _simpleDoubleProperty;
    }
  }.apply();
  
  private DoubleProperty actualHeightProperty = new Function0<DoubleProperty>() {
    public DoubleProperty apply() {
      SimpleDoubleProperty _simpleDoubleProperty = new SimpleDoubleProperty();
      return _simpleDoubleProperty;
    }
  }.apply();
  
  private FirstRecursiveImageNode pivot;
  
  public RecursiveImageNode(final Image image, final double x, final double y, final double scale) {
    this.setImage(image);
    this.setX(x);
    this.setY(y);
    this.setScale(scale);
    FirstRecursiveImageNode _firstRecursiveImageNode = new FirstRecursiveImageNode(this);
    this.pivot = _firstRecursiveImageNode;
    Group _createPane = this.createPane();
    final Procedure1<Group> _function = new Procedure1<Group>() {
      public void apply(final Group it) {
        ObservableList<Node> _children = it.getChildren();
        _children.add(RecursiveImageNode.this.pivot);
      }
    };
    Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_createPane, _function);
    this.setNode(_doubleArrow);
  }
  
  public void doActivate() {
    super.doActivate();
    this.pivot.activate();
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        int _clickCount = it.getClickCount();
        boolean _equals = (_clickCount == 2);
        if (_equals) {
          double _get = RecursiveImageNode.this.actualWidthProperty.get();
          double _multiply = (_get * 0.5);
          double _scale = RecursiveImageNode.this.getScale();
          double _minus = (1 - _scale);
          double _x = RecursiveImageNode.this.getX();
          double _multiply_1 = (2 * _x);
          double _get_1 = RecursiveImageNode.this.actualWidthProperty.get();
          double _divide = (_multiply_1 / _get_1);
          double _plus = (_minus + _divide);
          double _multiply_2 = (_multiply * _plus);
          double _scale_1 = RecursiveImageNode.this.getScale();
          double _minus_1 = (1 - _scale_1);
          double _divide_1 = (1 / _minus_1);
          double _multiply_3 = (_multiply_2 * _divide_1);
          double _get_2 = RecursiveImageNode.this.actualHeightProperty.get();
          double _multiply_4 = (_get_2 * 0.5);
          double _scale_2 = RecursiveImageNode.this.getScale();
          double _minus_2 = (1 - _scale_2);
          double _y = RecursiveImageNode.this.getY();
          double _multiply_5 = (2 * _y);
          double _get_3 = RecursiveImageNode.this.actualHeightProperty.get();
          double _divide_2 = (_multiply_5 / _get_3);
          double _plus_1 = (_minus_2 + _divide_2);
          double _multiply_6 = (_multiply_4 * _plus_1);
          double _scale_3 = RecursiveImageNode.this.getScale();
          double _minus_3 = (1 - _scale_3);
          double _divide_3 = (1 / _minus_3);
          double _multiply_7 = (_multiply_6 * _divide_3);
          final Point2D centerInDiagram = Extensions.localToRootDiagram(RecursiveImageNode.this, _multiply_3, _multiply_7);
          XRootDiagram _rootDiagram = Extensions.getRootDiagram(RecursiveImageNode.this);
          ScrollToAndScaleTransition _scrollToAndScaleTransition = new ScrollToAndScaleTransition(_rootDiagram, centerInDiagram, 10000);
          final Procedure1<ScrollToAndScaleTransition> _function = new Procedure1<ScrollToAndScaleTransition>() {
            public void apply(final ScrollToAndScaleTransition it) {
              Duration _seconds = Duration.seconds(5);
              it.setDuration(_seconds);
              Interpolator _SPLINE = Interpolator.SPLINE(0.5, 0, 1, 0.5);
              it.setInterpolator(_SPLINE);
              it.play();
            }
          };
          ObjectExtensions.<ScrollToAndScaleTransition>operator_doubleArrow(_scrollToAndScaleTransition, _function);
        }
      }
    };
    this.setOnMouseClicked(_function);
  }
  
  protected Group createPane() {
    Group _xblockexpression = null;
    {
      Group _group = new Group();
      final Procedure1<Group> _function = new Procedure1<Group>() {
        public void apply(final Group it) {
          ImageView _imageView = new ImageView();
          final ImageView imageView = _imageView;
          ObservableList<Node> _children = it.getChildren();
          final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
            public void apply(final ImageView it) {
              ObjectProperty<Image> _imageProperty = it.imageProperty();
              _imageProperty.bind(RecursiveImageNode.this.imageProperty);
              it.setPreserveRatio(true);
              DoubleProperty _fitWidthProperty = it.fitWidthProperty();
              DoubleProperty _widthProperty = RecursiveImageNode.this.widthProperty();
              _fitWidthProperty.bind(_widthProperty);
              DoubleProperty _fitHeightProperty = it.fitHeightProperty();
              DoubleProperty _heightProperty = RecursiveImageNode.this.heightProperty();
              _fitHeightProperty.bind(_heightProperty);
            }
          };
          ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(imageView, _function);
          _children.add(_doubleArrow);
          Rectangle _rectangle = new Rectangle();
          final Procedure1<Rectangle> _function_1 = new Procedure1<Rectangle>() {
            public void apply(final Rectangle it) {
              it.setX(0);
              it.setY(0);
              DoubleProperty _widthProperty = it.widthProperty();
              _widthProperty.bind(RecursiveImageNode.this.actualWidthProperty);
              DoubleProperty _heightProperty = it.heightProperty();
              _heightProperty.bind(RecursiveImageNode.this.actualHeightProperty);
              it.setStrokeType(StrokeType.INSIDE);
            }
          };
          Rectangle _doubleArrow_1 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_1);
          it.setClip(_doubleArrow_1);
          Bounds _boundsInLocal = imageView.getBoundsInLocal();
          RecursiveImageNode.this.updateActualDimension(_boundsInLocal);
          ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = imageView.boundsInLocalProperty();
          final ChangeListener<Bounds> _function_2 = new ChangeListener<Bounds>() {
            public void changed(final ObservableValue<? extends Bounds> property, final Bounds oldValue, final Bounds newValue) {
              RecursiveImageNode.this.updateActualDimension(newValue);
            }
          };
          _boundsInLocalProperty.addListener(_function_2);
        }
      };
      final Group pane = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      _xblockexpression = (pane);
    }
    return _xblockexpression;
  }
  
  protected void updateActualDimension(final Bounds newValue) {
    double _width = newValue.getWidth();
    this.actualWidthProperty.set(_width);
    double _height = newValue.getHeight();
    this.actualHeightProperty.set(_height);
  }
  
  public CharSequence toSvgElement(@Extension final SvgExporter exporter) {
    Node _node = this.getNode();
    CharSequence _snapshotToSvgElement = exporter.snapshotToSvgElement(_node);
    return _snapshotToSvgElement;
  }
  
  private SimpleObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>(this, "image");
  
  public Image getImage() {
    return this.imageProperty.get();
  }
  
  public void setImage(final Image image) {
    this.imageProperty.set(image);
  }
  
  public ObjectProperty<Image> imageProperty() {
    return this.imageProperty;
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
