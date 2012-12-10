package de.itemis.javafx.diagram.tools;

import de.itemis.javafx.diagram.Diagram;
import de.itemis.javafx.diagram.GraphUtil;
import de.itemis.javafx.diagram.ShapeContainer;
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
  private Diagram diagram;
  
  public SelectionTool(final Diagram diagram) {
    this.diagram = diagram;
    Group _rootPane = diagram.getRootPane();
    final Procedure1<MouseEvent> _function = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          final ShapeContainer targetShape = GraphUtil.getTargetShape(it);
          boolean _or = false;
          boolean _equals = ObjectExtensions.operator_equals(targetShape, null);
          if (_equals) {
            _or = true;
          } else {
            boolean _and = false;
            boolean _isSelected = targetShape.isSelected();
            boolean _not = (!_isSelected);
            if (!_not) {
              _and = false;
            } else {
              boolean _isShortcutDown = it.isShortcutDown();
              boolean _not_1 = (!_isShortcutDown);
              _and = (_not && _not_1);
            }
            _or = (_equals || _and);
          }
          if (_or) {
            Iterable<ShapeContainer> _selection = SelectionTool.this.getSelection();
            final Procedure1<ShapeContainer> _function = new Procedure1<ShapeContainer>() {
                public void apply(final ShapeContainer it) {
                  it.setSelected(false);
                }
              };
            IterableExtensions.<ShapeContainer>forEach(_selection, _function);
          }
          Iterable<ShapeContainer> _selection_1 = SelectionTool.this.getSelection();
          for (final ShapeContainer shape : _selection_1) {
            SelectionBehavior _selectionBehavior = shape.getSelectionBehavior();
            _selectionBehavior.mousePressed(it);
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
          Iterable<ShapeContainer> _selection = SelectionTool.this.getSelection();
          for (final ShapeContainer shape : _selection) {
            SelectionBehavior _selectionBehavior = shape.getSelectionBehavior();
            _selectionBehavior.mouseDragged(it);
          }
        }
      };
    _rootPane_1.<MouseEvent>addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_1.apply(arg0);
        }
    });
  }
  
  public Iterable<ShapeContainer> getSelection() {
    List<ShapeContainer> _shapes = this.diagram.getShapes();
    final Function1<ShapeContainer,Boolean> _function = new Function1<ShapeContainer,Boolean>() {
        public Boolean apply(final ShapeContainer it) {
          boolean _isSelected = it.isSelected();
          return Boolean.valueOf(_isSelected);
        }
      };
    Iterable<ShapeContainer> _filter = IterableExtensions.<ShapeContainer>filter(_shapes, _function);
    return _filter;
  }
}
