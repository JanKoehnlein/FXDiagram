package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.tools.CompositeTool;
import de.fxdiagram.core.tools.DiagramGestureTool;
import de.fxdiagram.core.tools.MenuTool;
import de.fxdiagram.core.tools.SelectionTool;
import de.fxdiagram.core.tools.XDiagramTool;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@Logging
@SuppressWarnings("all")
public class XRoot extends Group implements XActivatable {
  private XRootDiagram diagram;
  
  private List<XDiagramTool> tools = new Function0<List<XDiagramTool>>() {
    public List<XDiagramTool> apply() {
      ArrayList<XDiagramTool> _newArrayList = CollectionLiterals.<XDiagramTool>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private CompositeTool defaultTool;
  
  private XDiagramTool _currentTool;
  
  public XRoot() {
    XRootDiagram _xRootDiagram = new XRootDiagram(this);
    this.diagram = _xRootDiagram;
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.diagram);
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
  
  public XRootDiagram getDiagram() {
    return this.diagram;
  }
  
  public void activate() {
    this.diagram.activate();
    this.setCurrentTool(this.defaultTool);
  }
  
  public void setCurrentTool(final XDiagramTool tool) {
    XDiagramTool previousTool = this._currentTool;
    boolean _notEquals = (!Objects.equal(previousTool, null));
    if (_notEquals) {
      boolean _deactivate = previousTool.deactivate();
      boolean _not = (!_deactivate);
      if (_not) {
        XRoot.LOG.severe("Could not deactivate active tool");
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
          XRoot.LOG.severe("Could not reactivate tool");
        }
      }
    }
  }
  
  public void restoreDefaultTool() {
    this.setCurrentTool(this.defaultTool);
  }
  
  public Iterable<XShape> getCurrentSelection() {
    Iterable<XShape> _allShapes = this.diagram.getAllShapes();
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
}
