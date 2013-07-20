package de.fxdiagram.lib.media;

import com.google.common.base.Objects;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.export.SvgExportable;
import de.fxdiagram.core.export.SvgExporter;
import de.fxdiagram.core.geometry.TransformExtensions;
import de.fxdiagram.lib.simple.AddRapidButtonBehavior;
import java.util.Deque;
import java.util.LinkedList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class RecursiveImageNode extends XNode implements SvgExportable {
  private Image image;
  
  private double x;
  
  private double y;
  
  private double scale;
  
  private Deque<Pane> panes = new Function0<Deque<Pane>>() {
    public Deque<Pane> apply() {
      LinkedList<Pane> _linkedList = new LinkedList<Pane>();
      return _linkedList;
    }
  }.apply();
  
  public RecursiveImageNode(final Image image, final double x, final double y, final double scale) {
    this.image = image;
    this.x = x;
    this.y = y;
    this.scale = scale;
    Pane _createPane = this.createPane();
    this.setNode(_createPane);
  }
  
  public void doActivate() {
    super.doActivate();
    this.updateChildPanes();
    XRootDiagram _rootDiagram = Extensions.getRootDiagram(this);
    ReadOnlyObjectProperty<Bounds> _boundsInParentProperty = _rootDiagram.boundsInParentProperty();
    final ChangeListener<Bounds> _function = new ChangeListener<Bounds>() {
        public void changed(final ObservableValue<? extends Bounds> prop, final Bounds oldVal, final Bounds newVal) {
          RecursiveImageNode.this.updateChildPanes();
        }
      };
    _boundsInParentProperty.addListener(_function);
    AddRapidButtonBehavior _addRapidButtonBehavior = new AddRapidButtonBehavior(this);
    final AddRapidButtonBehavior rapidButtonBehavior = _addRapidButtonBehavior;
    rapidButtonBehavior.activate();
  }
  
  public void setHeight(final double height) {
    super.setHeight(height);
    final Procedure1<Pane> _function = new Procedure1<Pane>() {
        public void apply(final Pane it) {
          ObservableList<Node> _children = it.getChildren();
          Node _head = IterableExtensions.<Node>head(_children);
          ObservableList<Node> _children_1 = it.getChildren();
          Node _head_1 = IterableExtensions.<Node>head(_children);
          if (((ImageView) _head_1)!=null) {
            ((ImageView) _head_1).setFitHeight(height);
          }
        }
      };
    IterableExtensions.<Pane>forEach(this.panes, _function);
  }
  
  public void setWidth(final double width) {
    super.setWidth(width);
    final Procedure1<Pane> _function = new Procedure1<Pane>() {
        public void apply(final Pane it) {
          ObservableList<Node> _children = it.getChildren();
          Node _head = IterableExtensions.<Node>head(_children);
          ObservableList<Node> _children_1 = it.getChildren();
          Node _head_1 = IterableExtensions.<Node>head(_children);
          if (((ImageView) _head_1)!=null) {
            ((ImageView) _head_1).setFitWidth(width);
          }
        }
      };
    IterableExtensions.<Pane>forEach(this.panes, _function);
  }
  
  protected Pane createPane() {
    Pane _xblockexpression = null;
    {
      Pane _pane = new Pane();
      final Procedure1<Pane> _function = new Procedure1<Pane>() {
          public void apply(final Pane it) {
            ObservableList<Node> _children = it.getChildren();
            ImageView _imageView = new ImageView();
            final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
                public void apply(final ImageView it) {
                  it.setImage(RecursiveImageNode.this.image);
                  it.setPreserveRatio(true);
                  int _minus = (-1);
                  double _prefWidth = RecursiveImageNode.this.prefWidth(_minus);
                  it.setFitWidth(_prefWidth);
                  int _minus_1 = (-1);
                  double _prefHeight = RecursiveImageNode.this.prefHeight(_minus_1);
                  it.setFitHeight(_prefHeight);
                  DoubleProperty _layoutXProperty = it.layoutXProperty();
                  final InvalidationListener _function = new InvalidationListener() {
                      public void invalidated(final Observable it) {
                        InputOutput.<String>println("foo");
                      }
                    };
                  _layoutXProperty.addListener(_function);
                  DoubleProperty _translateXProperty = it.translateXProperty();
                  final InvalidationListener _function_1 = new InvalidationListener() {
                      public void invalidated(final Observable it) {
                        InputOutput.<String>println("bar");
                      }
                    };
                  _translateXProperty.addListener(_function_1);
                  DoubleProperty _xProperty = it.xProperty();
                  final InvalidationListener _function_2 = new InvalidationListener() {
                      public void invalidated(final Observable it) {
                        InputOutput.<String>println("foobar");
                      }
                    };
                  _xProperty.addListener(_function_2);
                }
              };
            ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
            _children.add(_doubleArrow);
          }
        };
      final Pane pane = ObjectExtensions.<Pane>operator_doubleArrow(_pane, _function);
      this.panes.push(pane);
      _xblockexpression = (pane);
    }
    return _xblockexpression;
  }
  
  public void updateChildPanes() {
    int _size = this.panes.size();
    boolean _greaterThan = (_size > 0);
    boolean _while = _greaterThan;
    while (_while) {
      {
        final Pane child = this.panes.pop();
        final Pane parent = this.panes.peek();
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
          boolean _greaterThan_1 = (area > 500);
          if (_greaterThan_1) {
            Pane _createPane = this.createPane();
            final Procedure1<Pane> _function = new Procedure1<Pane>() {
                public void apply(final Pane it) {
                  it.setScaleX(RecursiveImageNode.this.scale);
                  it.setScaleY(RecursiveImageNode.this.scale);
                  it.relocate(RecursiveImageNode.this.x, RecursiveImageNode.this.y);
                  DoubleProperty _layoutXProperty = it.layoutXProperty();
                  final InvalidationListener _function = new InvalidationListener() {
                      public void invalidated(final Observable it) {
                        InputOutput.<String>println("foo");
                      }
                    };
                  _layoutXProperty.addListener(_function);
                  DoubleProperty _translateXProperty = it.translateXProperty();
                  final InvalidationListener _function_1 = new InvalidationListener() {
                      public void invalidated(final Observable it) {
                        InputOutput.<String>println("bar");
                      }
                    };
                  _translateXProperty.addListener(_function_1);
                }
              };
            final Pane grandChild = ObjectExtensions.<Pane>operator_doubleArrow(_createPane, _function);
            ObservableList<Node> _children_1 = child.getChildren();
            _children_1.add(grandChild);
            this.panes.push(child);
            this.panes.push(grandChild);
          } else {
            return;
          }
        }
      }
      int _size_1 = this.panes.size();
      boolean _greaterThan_1 = (_size_1 > 0);
      _while = _greaterThan_1;
    }
  }
  
  public CharSequence toSvgElement(@Extension final SvgExporter exporter) {
    String _xblockexpression = null;
    {
      Node _head = null;
      Pane _head_1 = null;
      if (this.panes!=null) {
        _head_1=IterableExtensions.<Pane>head(this.panes);
      }
      ObservableList<Node> _children = null;
      if (_head_1!=null) {
        _children=_head_1.getChildren();
      }
      if (_children!=null) {
        _head=IterableExtensions.<Node>head(_children);
      }
      final ImageView view = ((ImageView) _head);
      boolean _notEquals = (!Objects.equal(view, null));
      if (_notEquals) {
        double _fitWidth = view.getFitWidth();
        double _width = this.image.getWidth();
        double _divide = (_fitWidth / _width);
        double _fitHeight = view.getFitHeight();
        double _height = this.image.getHeight();
        double _divide_1 = (_fitHeight / _height);
        final double imageScale = Math.min(_divide, _divide_1);
        double _width_1 = this.image.getWidth();
        double _divide_2 = (_width_1 / imageScale);
        final int imageWidth = ((int) _divide_2);
        double _height_1 = this.image.getHeight();
        double _divide_3 = (_height_1 / imageScale);
        final int imageHeight = ((int) _divide_3);
        WritableImage _writableImage = new WritableImage(imageWidth, imageHeight);
        final WritableImage image = _writableImage;
        Bounds _layoutBounds = this.getLayoutBounds();
        double _width_2 = _layoutBounds.getWidth();
        final double renderScale = (imageWidth / _width_2);
        Transform _localToDiagramTransform = Extensions.localToDiagramTransform(this);
        Scale _scale = new Scale(renderScale, renderScale, renderScale);
        final Transform t = TransformExtensions.operator_multiply(_localToDiagramTransform, _scale);
        SnapshotParameters _snapshotParameters = new SnapshotParameters();
        final Procedure1<SnapshotParameters> _function = new Procedure1<SnapshotParameters>() {
            public void apply(final SnapshotParameters it) {
              it.setTransform(t);
            }
          };
        SnapshotParameters _doubleArrow = ObjectExtensions.<SnapshotParameters>operator_doubleArrow(_snapshotParameters, _function);
        this.snapshot(_doubleArrow, image);
        return exporter.toSvgImage(this, image);
      }
      _xblockexpression = ("");
    }
    return _xblockexpression;
  }
}
