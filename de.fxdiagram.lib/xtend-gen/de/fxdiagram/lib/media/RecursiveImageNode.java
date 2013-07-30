package de.fxdiagram.lib.media;

import com.google.common.base.Objects;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.export.SvgExportable;
import de.fxdiagram.core.export.SvgExporter;
import de.fxdiagram.lib.simple.AddRapidButtonBehavior;
import java.util.Deque;
import java.util.LinkedList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class RecursiveImageNode extends XNode implements SvgExportable {
  private Deque<Group> panes = new Function0<Deque<Group>>() {
    public Deque<Group> apply() {
      LinkedList<Group> _linkedList = new LinkedList<Group>();
      return _linkedList;
    }
  }.apply();
  
  public RecursiveImageNode(final Image image, final double x, final double y, final double scale) {
    this.setImage(image);
    this.setX(x);
    this.setY(y);
    this.setScale(scale);
    final Group rootPane = this.createPane();
    this.setNode(rootPane);
    this.panes.push(rootPane);
  }
  
  public void doActivate() {
    super.doActivate();
    this.updateChildPanes();
    XRootDiagram _rootDiagram = Extensions.getRootDiagram(this);
    DoubleProperty _scaleProperty = _rootDiagram.scaleProperty();
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
      public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
        RecursiveImageNode.this.updateChildPanes();
      }
    };
    _scaleProperty.addListener(_function);
    AddRapidButtonBehavior _addRapidButtonBehavior = new AddRapidButtonBehavior(this);
    final AddRapidButtonBehavior rapidButtonBehavior = _addRapidButtonBehavior;
    rapidButtonBehavior.activate();
  }
  
  protected Group createPane() {
    Group _xblockexpression = null;
    {
      Group _group = new Group();
      final Procedure1<Group> _function = new Procedure1<Group>() {
        public void apply(final Group it) {
          ObservableList<Node> _children = it.getChildren();
          ImageView _imageView = new ImageView();
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
          ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
          _children.add(_doubleArrow);
          Rectangle _rectangle = new Rectangle();
          final Procedure1<Rectangle> _function_1 = new Procedure1<Rectangle>() {
            public void apply(final Rectangle it) {
              it.setX(0);
              it.setY(0);
              DoubleProperty _widthProperty = it.widthProperty();
              DoubleProperty _widthProperty_1 = RecursiveImageNode.this.widthProperty();
              _widthProperty.bind(_widthProperty_1);
              DoubleProperty _heightProperty = it.heightProperty();
              DoubleProperty _heightProperty_1 = RecursiveImageNode.this.heightProperty();
              _heightProperty.bind(_heightProperty_1);
            }
          };
          Rectangle _doubleArrow_1 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_1);
          it.setClip(_doubleArrow_1);
        }
      };
      final Group pane = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      _xblockexpression = (pane);
    }
    return _xblockexpression;
  }
  
  public void updateChildPanes() {
    boolean _isEmpty = this.panes.isEmpty();
    boolean _not = (!_isEmpty);
    boolean _while = _not;
    while (_while) {
      {
        final Group child = this.panes.pop();
        final Group parent = this.panes.peek();
        Bounds _boundsInLocal = child.getBoundsInLocal();
        final Bounds bounds = child.localToScene(_boundsInLocal);
        double _width = bounds.getWidth();
        double _height = bounds.getHeight();
        final double area = (_width * _height);
        boolean _lessEqualsThan = (area <= 10);
        if (_lessEqualsThan) {
          boolean _notEquals = (!Objects.equal(parent, null));
          if (_notEquals) {
            ObservableList<Node> _children = parent.getChildren();
            _children.remove(child);
          } else {
            this.panes.push(child);
            return;
          }
        } else {
          boolean _greaterThan = (area > 500);
          if (_greaterThan) {
            Group _createPane = this.createPane();
            final Procedure1<Group> _function = new Procedure1<Group>() {
              public void apply(final Group it) {
                DoubleProperty _scaleXProperty = it.scaleXProperty();
                _scaleXProperty.bind(RecursiveImageNode.this.scaleProperty);
                DoubleProperty _scaleYProperty = it.scaleYProperty();
                _scaleYProperty.bind(RecursiveImageNode.this.scaleProperty);
                DoubleProperty _layoutXProperty = it.layoutXProperty();
                _layoutXProperty.bind(RecursiveImageNode.this.xProperty);
                DoubleProperty _layoutYProperty = it.layoutYProperty();
                _layoutYProperty.bind(RecursiveImageNode.this.yProperty);
              }
            };
            final Group grandChild = ObjectExtensions.<Group>operator_doubleArrow(_createPane, _function);
            ObservableList<Node> _children_1 = child.getChildren();
            _children_1.add(grandChild);
            this.panes.push(child);
            this.panes.push(grandChild);
          } else {
            this.panes.push(child);
            return;
          }
        }
      }
      boolean _isEmpty_1 = this.panes.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      _while = _not_1;
    }
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
