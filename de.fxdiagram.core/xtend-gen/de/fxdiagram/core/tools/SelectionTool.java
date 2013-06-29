package de.fxdiagram.core.tools;

import com.google.common.base.Objects;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.behavior.SelectionBehavior;
import de.fxdiagram.core.tools.XDiagramTool;
import java.util.List;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
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
          EventTarget _target = event.getTarget();
          boolean _not = (!(_target instanceof XRapidButton));
          if (_not) {
            final XNode targetShape = Extensions.getTargetNode(event);
            SelectionBehavior _selectionBehavior = null;
            if (targetShape!=null) {
              _selectionBehavior=targetShape.getSelectionBehavior();
            }
            boolean _notEquals = (!Objects.equal(_selectionBehavior, null));
            if (_notEquals) {
              boolean _and = false;
              SelectionBehavior _selectionBehavior_1 = targetShape.getSelectionBehavior();
              boolean _selected = _selectionBehavior_1.getSelected();
              boolean _not_1 = (!_selected);
              if (!_not_1) {
                _and = false;
              } else {
                boolean _isShortcutDown = event.isShortcutDown();
                boolean _not_2 = (!_isShortcutDown);
                _and = (_not_1 && _not_2);
              }
              if (_and) {
                Iterable<XNode> _selection = SelectionTool.this.getSelection();
                final Procedure1<XNode> _function = new Procedure1<XNode>() {
                    public void apply(final XNode it) {
                      SelectionBehavior _selectionBehavior = it.getSelectionBehavior();
                      _selectionBehavior.setSelected(false);
                    }
                  };
                IterableExtensions.<XNode>forEach(_selection, _function);
              }
              Iterable<XNode> _selection_1 = SelectionTool.this.getSelection();
              final Function1<XNode,Boolean> _function_1 = new Function1<XNode,Boolean>() {
                  public Boolean apply(final XNode it) {
                    XAbstractDiagram _diagram = Extensions.getDiagram(it);
                    XAbstractDiagram _diagram_1 = Extensions.getDiagram(targetShape);
                    boolean _notEquals = (!Objects.equal(_diagram, _diagram_1));
                    return Boolean.valueOf(_notEquals);
                  }
                };
              Iterable<XNode> _filter = IterableExtensions.<XNode>filter(_selection_1, _function_1);
              final Procedure1<XNode> _function_2 = new Procedure1<XNode>() {
                  public void apply(final XNode it) {
                    SelectionBehavior _selectionBehavior = it.getSelectionBehavior();
                    _selectionBehavior.setSelected(false);
                  }
                };
              IterableExtensions.<XNode>forEach(_filter, _function_2);
              Iterable<XNode> _selection_2 = SelectionTool.this.getSelection();
              final Procedure1<XNode> _function_3 = new Procedure1<XNode>() {
                  public void apply(final XNode it) {
                    MoveBehavior _moveBehavior = it.getMoveBehavior();
                    if (_moveBehavior!=null) {
                      _moveBehavior.mousePressed(event);
                    }
                  }
                };
              IterableExtensions.<XNode>forEach(_selection_2, _function_3);
              MoveBehavior _moveBehavior = targetShape.getMoveBehavior();
              if (_moveBehavior!=null) {
                _moveBehavior.mousePressed(event);
              }
              SelectionBehavior _selectionBehavior_2 = targetShape.getSelectionBehavior();
              _selectionBehavior_2.mousePressed(event);
            }
            event.consume();
          }
        }
      };
    this.mousePressedHandler = _function;
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent it) {
          Iterable<XNode> _selection = SelectionTool.this.getSelection();
          for (final XNode shape : _selection) {
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
  
  public Iterable<XNode> getSelection() {
    List<XNode> _nodes = this.rootDiagram.getNodes();
    final Function1<XNode,Boolean> _function = new Function1<XNode,Boolean>() {
        public Boolean apply(final XNode it) {
          SelectionBehavior _selectionBehavior = it.getSelectionBehavior();
          boolean _selected = false;
          if (_selectionBehavior!=null) {
            _selected=_selectionBehavior.getSelected();
          }
          return Boolean.valueOf(_selected);
        }
      };
    Iterable<XNode> _filter = IterableExtensions.<XNode>filter(_nodes, _function);
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
