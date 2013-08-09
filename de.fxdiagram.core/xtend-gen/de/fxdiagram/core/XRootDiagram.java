package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.binding.NumberExpressionExtensions;
import de.fxdiagram.core.tools.CompositeTool;
import de.fxdiagram.core.tools.DiagramGestureTool;
import de.fxdiagram.core.tools.MenuTool;
import de.fxdiagram.core.tools.SelectionTool;
import de.fxdiagram.core.tools.XDiagramTool;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@Logging
@SuppressWarnings("all")
public class XRootDiagram extends XAbstractDiagram {
  private Group nodeLayer = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private Group buttonLayer = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  public final static double MIN_SCALE = NumberExpressionExtensions.EPSILON;
  
  private List<XDiagramTool> tools = new Function0<List<XDiagramTool>>() {
    public List<XDiagramTool> apply() {
      ArrayList<XDiagramTool> _newArrayList = CollectionLiterals.<XDiagramTool>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private CompositeTool defaultTool;
  
  private XDiagramTool _currentTool;
  
  private XRoot root;
  
  private Affine canvasTransform;
  
  public XRootDiagram(final XRoot root) {
    this.root = root;
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.buttonLayer);
    Affine _affine = new Affine();
    this.canvasTransform = _affine;
    ObservableList<Transform> _transforms = this.getTransforms();
    _transforms.setAll(this.canvasTransform);
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
    _stylesheets.add("de/fxdiagram/core/XRootDiagram.css");
  }
  
  public void doActivate() {
    super.doActivate();
    this.setCurrentTool(this.defaultTool);
  }
  
  public XRoot getRoot() {
    return this.root;
  }
  
  public Group getNodeLayer() {
    return this.nodeLayer;
  }
  
  public Group getConnectionLayer() {
    return this.nodeLayer;
  }
  
  public Group getButtonLayer() {
    return this.buttonLayer;
  }
  
  public Affine getCanvasTransform() {
    return this.canvasTransform;
  }
  
  public void setCurrentTool(final XDiagramTool tool) {
    XDiagramTool previousTool = this._currentTool;
    boolean _notEquals = (!Objects.equal(previousTool, null));
    if (_notEquals) {
      boolean _deactivate = previousTool.deactivate();
      boolean _not = (!_deactivate);
      if (_not) {
        XRootDiagram.LOG.severe("Could not deactivate active tool");
      }
    }
    this._currentTool = tool;
    boolean _notEquals_1 = (!Objects.equal(tool, null));
    if (_notEquals_1) {
      boolean _activate = tool.activate();
      boolean _not_1 = (!_activate);
      if (_not_1) {
        this._currentTool = previousTool;
        boolean _activate_1 = false;
        if (previousTool!=null) {
          _activate_1=previousTool.activate();
        }
        boolean _not_2 = (!_activate_1);
        if (_not_2) {
          XRootDiagram.LOG.severe("Could not reactivate tool");
        }
      }
    }
  }
  
  public void restoreDefaultTool() {
    this.setCurrentTool(this.defaultTool);
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XRootDiagram");
    ;
  
  private SimpleDoubleProperty scaleProperty = new SimpleDoubleProperty(this, "scale",_initScale());
  
  private static final double _initScale() {
    return 1.0;
  }
  
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
