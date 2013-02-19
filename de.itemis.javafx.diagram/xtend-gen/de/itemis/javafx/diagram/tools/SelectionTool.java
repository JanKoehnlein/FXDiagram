package de.itemis.javafx.diagram.tools;

import de.itemis.javafx.diagram.Extensions;
import de.itemis.javafx.diagram.XAbstractDiagram;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.XRapidButton;
import de.itemis.javafx.diagram.XRootDiagram;
import de.itemis.javafx.diagram.behavior.MoveBehavior;
import de.itemis.javafx.diagram.behavior.SelectionBehavior;
import java.util.List;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class SelectionTool {
  private XRootDiagram rootDiagram;
  
  public SelectionTool(final XRootDiagram rootDiagram) {
    this.rootDiagram = rootDiagram;
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent event) {
          EventTarget _target = event.getTarget();
          boolean _not = (!(_target instanceof XRapidButton));
          if (_not) {
            final XNode targetShape = Extensions.getTargetShape(event);
            SelectionBehavior _selectionBehavior = targetShape==null?(SelectionBehavior)null:targetShape.getSelectionBehavior();
            boolean _notEquals = ObjectExtensions.operator_notEquals(_selectionBehavior, null);
            if (_notEquals) {
              boolean _and = false;
              SelectionBehavior _selectionBehavior_1 = targetShape.getSelectionBehavior();
              boolean _isSelected = _selectionBehavior_1.isSelected();
              boolean _not_1 = (!_isSelected);
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
                    boolean _notEquals = ObjectExtensions.operator_notEquals(_diagram, _diagram_1);
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
                    if (_moveBehavior!=null) _moveBehavior.mousePressed(event);
                  }
                };
              IterableExtensions.<XNode>forEach(_selection_2, _function_3);
              MoveBehavior _moveBehavior = targetShape.getMoveBehavior();
              if (_moveBehavior!=null) _moveBehavior.mousePressed(event);
              SelectionBehavior _selectionBehavior_2 = targetShape.getSelectionBehavior();
              _selectionBehavior_2.mousePressed(event);
            }
            event.consume();
          }
        }
      };
    rootDiagram.<MouseEvent>addEventFilter(MouseEvent.MOUSE_PRESSED, _function);
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent it) {
          Iterable<XNode> _selection = SelectionTool.this.getSelection();
          for (final XNode shape : _selection) {
            MoveBehavior _moveBehavior = shape==null?(MoveBehavior)null:shape.getMoveBehavior();
            if (_moveBehavior!=null) _moveBehavior.mouseDragged(it);
          }
          it.consume();
        }
      };
    rootDiagram.<MouseEvent>addEventFilter(MouseEvent.MOUSE_DRAGGED, _function_1);
  }
  
  public Iterable<XNode> getSelection() {
    List<XNode> _nodes = this.rootDiagram.getNodes();
    final Function1<XNode,Boolean> _function = new Function1<XNode,Boolean>() {
        public Boolean apply(final XNode it) {
          SelectionBehavior _selectionBehavior = it.getSelectionBehavior();
          boolean _isSelected = _selectionBehavior==null?false:_selectionBehavior.isSelected();
          return Boolean.valueOf(_isSelected);
        }
      };
    Iterable<XNode> _filter = IterableExtensions.<XNode>filter(_nodes, _function);
    return _filter;
  }
}
