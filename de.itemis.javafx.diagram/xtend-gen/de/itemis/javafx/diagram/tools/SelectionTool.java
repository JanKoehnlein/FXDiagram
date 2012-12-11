package de.itemis.javafx.diagram.tools;

import de.itemis.javafx.diagram.Extensions;
import de.itemis.javafx.diagram.XDiagram;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.behavior.MoveBehavior;
import de.itemis.javafx.diagram.behavior.SelectionBehavior;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class SelectionTool {
  private XDiagram diagram;
  
  public SelectionTool(final XDiagram diagram) {
    this.diagram = diagram;
    Group _rootPane = diagram.getRootPane();
    final Procedure1<MouseEvent> _function = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          final XNode targetShape = Extensions.getTargetShape(it);
          SelectionBehavior _selectionBehavior = targetShape==null?(SelectionBehavior)null:targetShape.getSelectionBehavior();
          boolean _notEquals = ObjectExtensions.operator_notEquals(_selectionBehavior, null);
          if (_notEquals) {
            boolean _and = false;
            SelectionBehavior _selectionBehavior_1 = targetShape.getSelectionBehavior();
            boolean _isSelected = _selectionBehavior_1.isSelected();
            boolean _not = (!_isSelected);
            if (!_not) {
              _and = false;
            } else {
              boolean _isShortcutDown = it.isShortcutDown();
              boolean _not_1 = (!_isShortcutDown);
              _and = (_not && _not_1);
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
            for (final XNode shape : _selection_1) {
              MoveBehavior _moveBehavior = shape.getMoveBehavior();
              if (_moveBehavior!=null) _moveBehavior.mousePressed(it);
            }
            SelectionBehavior _selectionBehavior_2 = targetShape.getSelectionBehavior();
            _selectionBehavior_2.mousePressed(it);
          }
        }
      };
    _rootPane.<MouseEvent>addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function.apply(arg0);
        }
    });
    Group _rootPane_1 = diagram.getRootPane();
    final Procedure1<MouseEvent> _function_1 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          Iterable<XNode> _selection = SelectionTool.this.getSelection();
          for (final XNode shape : _selection) {
            MoveBehavior _moveBehavior = shape==null?(MoveBehavior)null:shape.getMoveBehavior();
            if (_moveBehavior!=null) _moveBehavior.mouseDragged(it);
          }
        }
      };
    _rootPane_1.<MouseEvent>addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_1.apply(arg0);
        }
    });
  }
  
  public Iterable<XNode> getSelection() {
    List<XNode> _shapes = this.diagram.getShapes();
    final Function1<XNode,Boolean> _function = new Function1<XNode,Boolean>() {
        public Boolean apply(final XNode it) {
          SelectionBehavior _selectionBehavior = it.getSelectionBehavior();
          boolean _isSelected = _selectionBehavior==null?false:_selectionBehavior.isSelected();
          return Boolean.valueOf(_isSelected);
        }
      };
    Iterable<XNode> _filter = IterableExtensions.<XNode>filter(_shapes, _function);
    return _filter;
  }
}
