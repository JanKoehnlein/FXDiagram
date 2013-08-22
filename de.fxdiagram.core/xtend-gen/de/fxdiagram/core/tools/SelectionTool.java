package de.fxdiagram.core.tools;

import com.google.common.base.Objects;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.auxlines.AuxiliaryLinesSupport;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.XDiagramTool;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class SelectionTool implements XDiagramTool {
  private XRoot root;
  
  private EventHandler<MouseEvent> mousePressedHandler;
  
  private EventHandler<MouseEvent> mouseDraggedHandler;
  
  private EventHandler<MouseEvent> mouseReleasedHandler;
  
  public SelectionTool(final XRoot root) {
    this.root = root;
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent event) {
        Iterable<XShape> _currentSelection = root.getCurrentSelection();
        final Set<XShape> selection = IterableExtensions.<XShape>toSet(_currentSelection);
        boolean _and = false;
        EventTarget _target = event.getTarget();
        Pane _diagramCanvas = root.getDiagramCanvas();
        boolean _equals = Objects.equal(_target, _diagramCanvas);
        if (!_equals) {
          _and = false;
        } else {
          MouseButton _button = event.getButton();
          boolean _equals_1 = Objects.equal(_button, MouseButton.PRIMARY);
          _and = (_equals && _equals_1);
        }
        if (_and) {
          final Function1<XShape,Boolean> _function = new Function1<XShape,Boolean>() {
            public Boolean apply(final XShape it) {
              return Boolean.valueOf(true);
            }
          };
          SelectionTool.this.deselect(selection, _function);
        } else {
          XRapidButton _targetButton = CoreExtensions.getTargetButton(event);
          boolean _not = (!(_targetButton instanceof XRapidButton));
          if (_not) {
            final XShape targetShape = CoreExtensions.getTargetShape(event);
            boolean _isSelectable = false;
            if (targetShape!=null) {
              _isSelectable=targetShape.isSelectable();
            }
            if (_isSelectable) {
              boolean _and_1 = false;
              boolean _selected = targetShape.getSelected();
              boolean _not_1 = (!_selected);
              if (!_not_1) {
                _and_1 = false;
              } else {
                boolean _isShortcutDown = event.isShortcutDown();
                boolean _not_2 = (!_isShortcutDown);
                _and_1 = (_not_1 && _not_2);
              }
              if (_and_1) {
                XShape _switchResult = null;
                boolean _matched = false;
                if (!_matched) {
                  if (targetShape instanceof XControlPoint) {
                    final XControlPoint _xControlPoint = (XControlPoint)targetShape;
                    _matched=true;
                    Parent _parent = _xControlPoint.getParent();
                    XShape _containerShape = CoreExtensions.getContainerShape(_parent);
                    _switchResult = _containerShape;
                  }
                }
                if (!_matched) {
                  _switchResult = null;
                }
                final XShape skip = _switchResult;
                final Function1<XShape,Boolean> _function_1 = new Function1<XShape,Boolean>() {
                  public Boolean apply(final XShape it) {
                    boolean _notEquals = (!Objects.equal(it, skip));
                    return Boolean.valueOf(_notEquals);
                  }
                };
                SelectionTool.this.deselect(selection, _function_1);
              }
              final Function1<XShape,Boolean> _function_2 = new Function1<XShape,Boolean>() {
                public Boolean apply(final XShape it) {
                  XDiagram _diagram = CoreExtensions.getDiagram(it);
                  XDiagram _diagram_1 = CoreExtensions.getDiagram(targetShape);
                  boolean _notEquals = (!Objects.equal(_diagram, _diagram_1));
                  return Boolean.valueOf(_notEquals);
                }
              };
              SelectionTool.this.deselect(selection, _function_2);
              boolean _isShortcutDown_1 = event.isShortcutDown();
              if (_isShortcutDown_1) {
                targetShape.toggleSelect(event);
              } else {
                targetShape.select(event);
              }
              final Procedure1<XShape> _function_3 = new Procedure1<XShape>() {
                public void apply(final XShape it) {
                  MoveBehavior<? extends XShape> _moveBehavior = it.getMoveBehavior();
                  if (_moveBehavior!=null) {
                    _moveBehavior.mousePressed(event);
                  }
                }
              };
              IterableExtensions.<XShape>forEach(selection, _function_3);
              MoveBehavior<? extends XShape> _moveBehavior = targetShape.getMoveBehavior();
              if (_moveBehavior!=null) {
                _moveBehavior.mousePressed(event);
              }
            }
          }
        }
      }
    };
    this.mousePressedHandler = _function;
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        final Iterable<XShape> selection = root.getCurrentSelection();
        for (final XShape shape : selection) {
          MoveBehavior<? extends XShape> _moveBehavior = null;
          if (shape!=null) {
            _moveBehavior=shape.getMoveBehavior();
          }
          if (_moveBehavior!=null) {
            _moveBehavior.mouseDragged(it);
          }
        }
        XDiagram _diagram = root.getDiagram();
        AuxiliaryLinesSupport _auxiliaryLinesSupport = _diagram.getAuxiliaryLinesSupport();
        if (_auxiliaryLinesSupport!=null) {
          _auxiliaryLinesSupport.show(selection);
        }
        it.consume();
      }
    };
    this.mouseDraggedHandler = _function_1;
    final EventHandler<MouseEvent> _function_2 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        XDiagram _diagram = root.getDiagram();
        AuxiliaryLinesSupport _auxiliaryLinesSupport = _diagram.getAuxiliaryLinesSupport();
        if (_auxiliaryLinesSupport!=null) {
          _auxiliaryLinesSupport.hide();
        }
      }
    };
    this.mouseReleasedHandler = _function_2;
  }
  
  protected void deselect(final Collection<XShape> selection, final Function1<? super XShape,? extends Boolean> filter) {
    final Iterator<XShape> i = selection.iterator();
    boolean _hasNext = i.hasNext();
    boolean _while = _hasNext;
    while (_while) {
      {
        final XShape element = i.next();
        Boolean _apply = filter.apply(element);
        if ((_apply).booleanValue()) {
          element.setSelected(false);
          i.remove();
        }
      }
      boolean _hasNext_1 = i.hasNext();
      _while = _hasNext_1;
    }
  }
  
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      this.root.<MouseEvent>addEventFilter(MouseEvent.MOUSE_PRESSED, this.mousePressedHandler);
      this.root.<MouseEvent>addEventFilter(MouseEvent.MOUSE_DRAGGED, this.mouseDraggedHandler);
      this.root.<MouseEvent>addEventFilter(MouseEvent.MOUSE_RELEASED, this.mouseReleasedHandler);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
  
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      this.root.<MouseEvent>removeEventFilter(MouseEvent.MOUSE_PRESSED, this.mousePressedHandler);
      this.root.<MouseEvent>removeEventFilter(MouseEvent.MOUSE_DRAGGED, this.mouseDraggedHandler);
      this.root.<MouseEvent>removeEventFilter(MouseEvent.MOUSE_RELEASED, this.mouseReleasedHandler);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
}
