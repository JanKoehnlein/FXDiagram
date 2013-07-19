package de.fxdiagram.core.tools;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.tools.XDiagramTool;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class SelectionTool implements XDiagramTool {
  private XRootDiagram rootDiagram;
  
  private EventHandler<MouseEvent> mousePressedHandler;
  
  private EventHandler<MouseEvent> mouseDraggedHandler;
  
  public SelectionTool(final XRootDiagram rootDiagram) {
    this.rootDiagram = rootDiagram;
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent event) {
          XRapidButton _targetButton = Extensions.getTargetButton(event);
          boolean _not = (!(_targetButton instanceof XRapidButton));
          if (_not) {
            final XShape targetShape = Extensions.getTargetShape(event);
            boolean _isSelectable = false;
            if (targetShape!=null) {
              _isSelectable=targetShape.isSelectable();
            }
            if (_isSelectable) {
              boolean _and = false;
              boolean _selected = targetShape.getSelected();
              boolean _not_1 = (!_selected);
              if (!_not_1) {
                _and = false;
              } else {
                boolean _isShortcutDown = event.isShortcutDown();
                boolean _not_2 = (!_isShortcutDown);
                _and = (_not_1 && _not_2);
              }
              if (_and) {
                XShape _switchResult = null;
                boolean _matched = false;
                if (!_matched) {
                  if (targetShape instanceof XControlPoint) {
                    final XControlPoint _xControlPoint = (XControlPoint)targetShape;
                    _matched=true;
                    Parent _parent = _xControlPoint.getParent();
                    XShape _containerShape = Extensions.getContainerShape(_parent);
                    _switchResult = _containerShape;
                  }
                }
                if (!_matched) {
                  _switchResult = null;
                }
                final XShape skip = _switchResult;
                Iterable<XShape> _selection = SelectionTool.this.getSelection();
                final Function1<XShape,Boolean> _function = new Function1<XShape,Boolean>() {
                    public Boolean apply(final XShape it) {
                      boolean _notEquals = (!Objects.equal(it, skip));
                      return Boolean.valueOf(_notEquals);
                    }
                  };
                Iterable<XShape> _filter = IterableExtensions.<XShape>filter(_selection, _function);
                final Procedure1<XShape> _function_1 = new Procedure1<XShape>() {
                    public void apply(final XShape it) {
                      it.setSelected(false);
                    }
                  };
                IterableExtensions.<XShape>forEach(_filter, _function_1);
              }
              Iterable<XShape> _selection_1 = SelectionTool.this.getSelection();
              final Function1<XShape,Boolean> _function_2 = new Function1<XShape,Boolean>() {
                  public Boolean apply(final XShape it) {
                    XAbstractDiagram _diagram = Extensions.getDiagram(it);
                    XAbstractDiagram _diagram_1 = Extensions.getDiagram(targetShape);
                    boolean _notEquals = (!Objects.equal(_diagram, _diagram_1));
                    return Boolean.valueOf(_notEquals);
                  }
                };
              Iterable<XShape> _filter_1 = IterableExtensions.<XShape>filter(_selection_1, _function_2);
              final Procedure1<XShape> _function_3 = new Procedure1<XShape>() {
                  public void apply(final XShape it) {
                    it.setSelected(false);
                  }
                };
              IterableExtensions.<XShape>forEach(_filter_1, _function_3);
              boolean _isShortcutDown_1 = event.isShortcutDown();
              if (_isShortcutDown_1) {
                targetShape.toggleSelect(event);
              } else {
                targetShape.select(event);
              }
              Iterable<XShape> _selection_2 = SelectionTool.this.getSelection();
              final Procedure1<XShape> _function_4 = new Procedure1<XShape>() {
                  public void apply(final XShape it) {
                    MoveBehavior _moveBehavior = it.getMoveBehavior();
                    if (_moveBehavior!=null) {
                      _moveBehavior.mousePressed(event);
                    }
                  }
                };
              IterableExtensions.<XShape>forEach(_selection_2, _function_4);
              MoveBehavior _moveBehavior = targetShape.getMoveBehavior();
              if (_moveBehavior!=null) {
                _moveBehavior.mousePressed(event);
              }
            }
          }
        }
      };
    this.mousePressedHandler = _function;
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent it) {
          Iterable<XShape> _selection = SelectionTool.this.getSelection();
          for (final XShape shape : _selection) {
            MoveBehavior _moveBehavior = null;
            if (shape!=null) {
              _moveBehavior=shape.getMoveBehavior();
            }
            if (_moveBehavior!=null) {
              _moveBehavior.mouseDragged(it);
            }
          }
          it.consume();
        }
      };
    this.mouseDraggedHandler = _function_1;
  }
  
  public Iterable<XShape> getSelection() {
    List<XNode> _nodes = this.rootDiagram.getNodes();
    List<XConnection> _connections = this.rootDiagram.getConnections();
    Iterable<XShape> _plus = Iterables.<XShape>concat(_nodes, _connections);
    List<XConnection> _connections_1 = this.rootDiagram.getConnections();
    final Function1<XConnection,ObservableList<XControlPoint>> _function = new Function1<XConnection,ObservableList<XControlPoint>>() {
        public ObservableList<XControlPoint> apply(final XConnection it) {
          ObservableList<XControlPoint> _controlPoints = it.getControlPoints();
          return _controlPoints;
        }
      };
    List<ObservableList<XControlPoint>> _map = ListExtensions.<XConnection, ObservableList<XControlPoint>>map(_connections_1, _function);
    Iterable<XControlPoint> _flatten = Iterables.<XControlPoint>concat(_map);
    Iterable<XShape> _plus_1 = Iterables.<XShape>concat(_plus, _flatten);
    List<XConnection> _connections_2 = this.rootDiagram.getConnections();
    final Function1<XConnection,XConnectionLabel> _function_1 = new Function1<XConnection,XConnectionLabel>() {
        public XConnectionLabel apply(final XConnection it) {
          XConnectionLabel _label = it.getLabel();
          return _label;
        }
      };
    List<XConnectionLabel> _map_1 = ListExtensions.<XConnection, XConnectionLabel>map(_connections_2, _function_1);
    Iterable<XConnectionLabel> _filterNull = IterableExtensions.<XConnectionLabel>filterNull(_map_1);
    Iterable<XShape> _plus_2 = Iterables.<XShape>concat(_plus_1, _filterNull);
    final Function1<XShape,Boolean> _function_2 = new Function1<XShape,Boolean>() {
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
    Iterable<XShape> _filter = IterableExtensions.<XShape>filter(_plus_2, _function_2);
    return _filter;
  }
  
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      this.rootDiagram.<MouseEvent>addEventFilter(MouseEvent.MOUSE_PRESSED, this.mousePressedHandler);
      this.rootDiagram.<MouseEvent>addEventFilter(MouseEvent.MOUSE_DRAGGED, this.mouseDraggedHandler);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
  
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      this.rootDiagram.<MouseEvent>removeEventFilter(MouseEvent.MOUSE_PRESSED, this.mousePressedHandler);
      this.rootDiagram.<MouseEvent>removeEventFilter(MouseEvent.MOUSE_DRAGGED, this.mouseDraggedHandler);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
}
