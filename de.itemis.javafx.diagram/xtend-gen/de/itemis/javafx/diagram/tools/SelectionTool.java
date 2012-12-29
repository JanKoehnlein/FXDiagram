package de.itemis.javafx.diagram.tools;

import de.itemis.javafx.diagram.Extensions;
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
  private XRootDiagram diagram;
  
  public SelectionTool(final XRootDiagram diagram) {
    this.diagram = diagram;
    final Procedure1<MouseEvent> _function = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          EventTarget _target = it.getTarget();
          boolean _not = (!(_target instanceof XRapidButton));
          if (_not) {
            final XNode targetShape = Extensions.getTargetShape(it);
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
                boolean _isShortcutDown = it.isShortcutDown();
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
              for (final XNode shape : _selection_1) {
                MoveBehavior _moveBehavior = shape==null?(MoveBehavior)null:shape.getMoveBehavior();
                if (_moveBehavior!=null) _moveBehavior.mousePressed(it);
              }
              MoveBehavior _moveBehavior_1 = targetShape.getMoveBehavior();
              if (_moveBehavior_1!=null) _moveBehavior_1.mousePressed(it);
              SelectionBehavior _selectionBehavior_2 = targetShape.getSelectionBehavior();
              _selectionBehavior_2.mousePressed(it);
              it.consume();
            }
          }
        }
      };
    diagram.<MouseEvent>addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
          _function.apply(event);
        }
    });
    final Procedure1<MouseEvent> _function_1 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          Iterable<XNode> _selection = SelectionTool.this.getSelection();
          for (final XNode shape : _selection) {
            MoveBehavior _moveBehavior = shape==null?(MoveBehavior)null:shape.getMoveBehavior();
            if (_moveBehavior!=null) _moveBehavior.mouseDragged(it);
          }
          it.consume();
        }
      };
    diagram.<MouseEvent>addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
          _function_1.apply(event);
        }
    });
  }
  
  public Iterable<XNode> getSelection() {
    List<XNode> _nodes = this.diagram.getNodes();
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
