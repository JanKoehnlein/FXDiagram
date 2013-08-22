package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.css.JavaToCss;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.TransformExtensions;
import de.fxdiagram.core.tools.CompositeTool;
import de.fxdiagram.core.tools.DiagramGestureTool;
import de.fxdiagram.core.tools.MenuTool;
import de.fxdiagram.core.tools.SelectionTool;
import de.fxdiagram.core.tools.XDiagramTool;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@SuppressWarnings("all")
public class XRoot extends Parent implements XActivatable {
  private HeadsUpDisplay headsUpDisplay = new Function0<HeadsUpDisplay>() {
    public HeadsUpDisplay apply() {
      HeadsUpDisplay _headsUpDisplay = new HeadsUpDisplay();
      return _headsUpDisplay;
    }
  }.apply();
  
  private Pane diagramCanvas = new Function0<Pane>() {
    public Pane apply() {
      Pane _pane = new Pane();
      return _pane;
    }
  }.apply();
  
  public final static double MIN_SCALE = NumberExpressionExtensions.EPSILON;
  
  private Affine diagramTransform;
  
  private List<XDiagramTool> tools = new Function0<List<XDiagramTool>>() {
    public List<XDiagramTool> apply() {
      ArrayList<XDiagramTool> _newArrayList = CollectionLiterals.<XDiagramTool>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private CompositeTool defaultTool;
  
  private XDiagramTool currentTool;
  
  public XRoot() {
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.diagramCanvas);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.headsUpDisplay);
    CompositeTool _compositeTool = new CompositeTool();
    this.defaultTool = _compositeTool;
    SelectionTool _selectionTool = new SelectionTool(this);
    this.defaultTool.operator_add(_selectionTool);
    DiagramGestureTool _diagramGestureTool = new DiagramGestureTool(this);
    this.defaultTool.operator_add(_diagramGestureTool);
    MenuTool _menuTool = new MenuTool(this);
    this.defaultTool.operator_add(_menuTool);
    this.tools.add(this.defaultTool);
    ObservableList<String> _stylesheets = this.getStylesheets();
    _stylesheets.add("de/fxdiagram/core/XRoot.css");
  }
  
  public boolean setDiagram(final XDiagram newDiagram) {
    boolean _xblockexpression = false;
    {
      XDiagram _diagram = this.getDiagram();
      boolean _notEquals = (!Objects.equal(_diagram, null));
      if (_notEquals) {
        ObservableList<Node> _children = this.diagramCanvas.getChildren();
        XDiagram _diagram_1 = this.getDiagram();
        _children.remove(_diagram_1);
      }
      this.diagramProperty.set(newDiagram);
      ObservableList<Node> _children_1 = this.diagramCanvas.getChildren();
      XDiagram _diagram_2 = this.getDiagram();
      _children_1.add(_diagram_2);
      boolean _isActive = this.getIsActive();
      if (_isActive) {
        newDiagram.activate();
      }
      Affine _affine = new Affine();
      this.diagramTransform = _affine;
      XDiagram _diagram_3 = this.getDiagram();
      ObservableList<Transform> _transforms = _diagram_3.getTransforms();
      boolean _isEmpty = _transforms.isEmpty();
      if (_isEmpty) {
        this.centerDiagram();
      } else {
        XDiagram _diagram_4 = this.getDiagram();
        ObservableList<Transform> _transforms_1 = _diagram_4.getTransforms();
        final Procedure1<Transform> _function = new Procedure1<Transform>() {
          public void apply(final Transform it) {
            TransformExtensions.leftMultiply(XRoot.this.diagramTransform, it);
          }
        };
        IterableExtensions.<Transform>forEach(_transforms_1, _function);
        double _mxx = this.diagramTransform.getMxx();
        double _mxx_1 = this.diagramTransform.getMxx();
        double _multiply = (_mxx * _mxx_1);
        double _mxy = this.diagramTransform.getMxy();
        double _mxy_1 = this.diagramTransform.getMxy();
        double _multiply_1 = (_mxy * _mxy_1);
        double _plus = (_multiply + _multiply_1);
        double _sqrt = Math.sqrt(_plus);
        this.setDiagramScale(_sqrt);
      }
      XDiagram _diagram_5 = this.getDiagram();
      Paint _backgroundPaint = _diagram_5.getBackgroundPaint();
      CharSequence _css = JavaToCss.toCss(_backgroundPaint);
      String _plus_1 = ("-fx-background-color: " + _css);
      String _plus_2 = (_plus_1 + ";");
      this.diagramCanvas.setStyle(_plus_2);
      XDiagram _diagram_6 = this.getDiagram();
      ObservableList<Transform> _transforms_2 = _diagram_6.getTransforms();
      boolean _setAll = _transforms_2.setAll(this.diagramTransform);
      _xblockexpression = (_setAll);
    }
    return _xblockexpression;
  }
  
  public void centerDiagram() {
    XDiagram _diagram = this.getDiagram();
    _diagram.layout();
    XDiagram _diagram_1 = this.getDiagram();
    final Bounds diagramBounds = _diagram_1.getLayoutBounds();
    double _width = diagramBounds.getWidth();
    double _height = diagramBounds.getHeight();
    double _multiply = (_width * _height);
    boolean _greaterThan = (_multiply > 1);
    if (_greaterThan) {
      Scene _scene = this.getScene();
      double _width_1 = _scene.getWidth();
      double _width_2 = diagramBounds.getWidth();
      double _divide = (_width_1 / _width_2);
      Scene _scene_1 = this.getScene();
      double _height_1 = _scene_1.getHeight();
      double _height_2 = diagramBounds.getHeight();
      double _divide_1 = (_height_1 / _height_2);
      double _min = Math.min(_divide, _divide_1);
      double _min_1 = Math.min(1, _min);
      final double scale = Math.max(XRoot.MIN_SCALE, _min_1);
      this.setDiagramScale(scale);
      TransformExtensions.scale(this.diagramTransform, scale, scale);
      XDiagram _diagram_2 = this.getDiagram();
      XDiagram _diagram_3 = this.getDiagram();
      Bounds _boundsInLocal = _diagram_3.getBoundsInLocal();
      Bounds _localToScene = _diagram_2.localToScene(_boundsInLocal);
      final Point2D centerInScene = BoundsExtensions.center(_localToScene);
      Scene _scene_2 = this.getScene();
      double _width_3 = _scene_2.getWidth();
      double _multiply_1 = (0.5 * _width_3);
      double _x = centerInScene.getX();
      double _minus = (_multiply_1 - _x);
      Scene _scene_3 = this.getScene();
      double _height_3 = _scene_3.getHeight();
      double _multiply_2 = (0.5 * _height_3);
      double _y = centerInScene.getY();
      double _minus_1 = (_multiply_2 - _y);
      TransformExtensions.translate(this.diagramTransform, _minus, _minus_1);
    }
  }
  
  public HeadsUpDisplay getHeadsUpDisplay() {
    return this.headsUpDisplay;
  }
  
  public Pane getDiagramCanvas() {
    return this.diagramCanvas;
  }
  
  public Affine getDiagramTransform() {
    return this.diagramTransform;
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActiveProperty.set(true);
  }
  
  public void doActivate() {
    XDiagram _diagram = this.getDiagram();
    if (_diagram!=null) {
      _diagram.activate();
    }
    final Procedure1<Pane> _function = new Procedure1<Pane>() {
      public void apply(final Pane it) {
        DoubleProperty _prefWidthProperty = it.prefWidthProperty();
        Scene _scene = it.getScene();
        ReadOnlyDoubleProperty _widthProperty = _scene.widthProperty();
        _prefWidthProperty.bind(_widthProperty);
        DoubleProperty _prefHeightProperty = it.prefHeightProperty();
        Scene _scene_1 = it.getScene();
        ReadOnlyDoubleProperty _heightProperty = _scene_1.heightProperty();
        _prefHeightProperty.bind(_heightProperty);
      }
    };
    ObjectExtensions.<Pane>operator_doubleArrow(
      this.diagramCanvas, _function);
    this.setCurrentTool(this.defaultTool);
  }
  
  public void setCurrentTool(final XDiagramTool tool) {
    XDiagramTool previousTool = this.currentTool;
    boolean _notEquals = (!Objects.equal(previousTool, null));
    if (_notEquals) {
      boolean _deactivate = previousTool.deactivate();
      boolean _not = (!_deactivate);
      if (_not) {
        XRoot.LOG.severe("Could not deactivate active tool");
      }
    }
    this.currentTool = tool;
    boolean _notEquals_1 = (!Objects.equal(tool, null));
    if (_notEquals_1) {
      boolean _activate = tool.activate();
      boolean _not_1 = (!_activate);
      if (_not_1) {
        this.currentTool = previousTool;
        boolean _activate_1 = false;
        if (previousTool!=null) {
          _activate_1=previousTool.activate();
        }
        boolean _not_2 = (!_activate_1);
        if (_not_2) {
          XRoot.LOG.severe("Could not reactivate tool");
        }
      }
    }
  }
  
  public void restoreDefaultTool() {
    this.setCurrentTool(this.defaultTool);
  }
  
  public Iterable<XShape> getCurrentSelection() {
    XDiagram _diagram = this.getDiagram();
    Iterable<XShape> _allShapes = _diagram.getAllShapes();
    final Function1<XShape,Boolean> _function = new Function1<XShape,Boolean>() {
      public Boolean apply(final XShape it) {
        boolean _and = false;
        boolean _isSelectable = it.isSelectable();
        if (!_isSelectable) {
          _and = false;
        } else {
          boolean _selected = it.getSelected();
          _and = (_isSelectable && _selected);
        }
        return Boolean.valueOf(_and);
      }
    };
    Iterable<XShape> _filter = IterableExtensions.<XShape>filter(_allShapes, _function);
    return _filter;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XRoot");
    ;
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
  
  private SimpleDoubleProperty diagramScaleProperty = new SimpleDoubleProperty(this, "diagramScale",_initDiagramScale());
  
  private static final double _initDiagramScale() {
    return 1.0;
  }
  
  public double getDiagramScale() {
    return this.diagramScaleProperty.get();
  }
  
  public void setDiagramScale(final double diagramScale) {
    this.diagramScaleProperty.set(diagramScale);
  }
  
  public DoubleProperty diagramScaleProperty() {
    return this.diagramScaleProperty;
  }
  
  private ReadOnlyObjectWrapper<XDiagram> diagramProperty = new ReadOnlyObjectWrapper<XDiagram>(this, "diagram");
  
  public XDiagram getDiagram() {
    return this.diagramProperty.get();
  }
  
  public ReadOnlyObjectProperty<XDiagram> diagramProperty() {
    return this.diagramProperty.getReadOnlyProperty();
  }
}
