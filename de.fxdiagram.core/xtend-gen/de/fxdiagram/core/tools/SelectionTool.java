package de.fxdiagram.core.tools;

import com.google.common.base.Objects;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.auxlines.AuxiliaryLinesSupport;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.tools.XDiagramTool;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class SelectionTool implements XDiagramTool {
  private XRootDiagram rootDiagram;
  
  private EventHandler<MouseEvent> mousePressedHandler;
  
  private EventHandler<MouseEvent> mouseDraggedHandler;
  
  private EventHandler<MouseEvent> mouseReleasedHandler;
  
  public SelectionTool(final XRootDiagram rootDiagram) {
    this.rootDiagram = rootDiagram;
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent event) {
        boolean _and = false;
        EventTarget _target = event.getTarget();
        if (!(_target instanceof Scene)) {
          _and = false;
        } else {
          MouseButton _button = event.getButton();
          boolean _equals = Objects.equal(_button, MouseButton.PRIMARY);
          _and = ((_target instanceof Scene) && _equals);
        }
        if (_and) {
          Iterable<? extends XShape> _selection = SelectionTool.this.getSelection();
          final Procedure1<XShape> _function = new Procedure1<XShape>() {
            public void apply(final XShape it) {
              it.setSelected(false);
            }
          };
          IterableExtensions.forEach(_selection, _function);
        } else {
          XRapidButton _targetButton = Extensions.getTargetButton(event);
          boolean _not = (!(_targetButton instanceof XRapidButton));
          if (_not) {
            final XShape targetShape = Extensions.getTargetShape(event);
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
                    XShape _containerShape = Extensions.getContainerShape(_parent);
                    _switchResult = _containerShape;
                  }
                }
                if (!_matched) {
                  _switchResult = null;
                }
                final XShape skip = _switchResult;
                Iterable<? extends XShape> _selection_1 = SelectionTool.this.getSelection();
                final Function1<XShape,Boolean> _function_1 = new Function1<XShape,Boolean>() {
                  public Boolean apply(final XShape it) {
                    boolean _notEquals = (!Objects.equal(it, skip));
                    return Boolean.valueOf(_notEquals);
                  }
                };
                Iterable<? extends XShape> _filter = IterableExtensions.filter(_selection_1, _function_1);
                final Procedure1<XShape> _function_2 = new Procedure1<XShape>() {
                  public void apply(final XShape it) {
                    it.setSelected(false);
                  }
                };
                IterableExtensions.forEach(_filter, _function_2);
              }
              Iterable<? extends XShape> _selection_2 = SelectionTool.this.getSelection();
              final Function1<XShape,Boolean> _function_3 = new Function1<XShape,Boolean>() {
                public Boolean apply(final XShape it) {
                  XAbstractDiagram _diagram = Extensions.getDiagram(it);
                  XAbstractDiagram _diagram_1 = Extensions.getDiagram(targetShape);
                  boolean _notEquals = (!Objects.equal(_diagram, _diagram_1));
                  return Boolean.valueOf(_notEquals);
                }
              };
              Iterable<? extends XShape> _filter_1 = IterableExtensions.filter(_selection_2, _function_3);
              final Procedure1<XShape> _function_4 = new Procedure1<XShape>() {
                public void apply(final XShape it) {
                  it.setSelected(false);
                }
              };
              IterableExtensions.forEach(_filter_1, _function_4);
              boolean _isShortcutDown_1 = event.isShortcutDown();
              if (_isShortcutDown_1) {
                targetShape.toggleSelect(event);
              } else {
                targetShape.select(event);
              }
              Iterable<? extends XShape> _selection_3 = SelectionTool.this.getSelection();
              final Procedure1<XShape> _function_5 = new Procedure1<XShape>() {
                public void apply(final XShape it) {
                  MoveBehavior<? extends XShape> _moveBehavior = it.getMoveBehavior();
                  if (_moveBehavior!=null) {
                    _moveBehavior.mousePressed(event);
                  }
                }
              };
              IterableExtensions.forEach(_selection_3, _function_5);
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
        Iterable<? extends XShape> _selection = SelectionTool.this.getSelection();
        for (final XShape shape : _selection) {
          MoveBehavior<? extends XShape> _moveBehavior = null;
          if (shape!=null) {
            _moveBehavior=shape.getMoveBehavior();
          }
          if (_moveBehavior!=null) {
            _moveBehavior.mouseDragged(it);
          }
        }
        AuxiliaryLinesSupport _auxiliaryLinesSupport = rootDiagram.getAuxiliaryLinesSupport();
        if (_auxiliaryLinesSupport!=null) {
          Iterable<? extends XShape> _selection_1 = SelectionTool.this.getSelection();
          _auxiliaryLinesSupport.show(_selection_1);
        }
        it.consume();
      }
    };
    this.mouseDraggedHandler = _function_1;
    final EventHandler<MouseEvent> _function_2 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        AuxiliaryLinesSupport _auxiliaryLinesSupport = rootDiagram.getAuxiliaryLinesSupport();
        if (_auxiliaryLinesSupport!=null) {
          _auxiliaryLinesSupport.hide();
        }
      }
    };
    this.mouseReleasedHandler = _function_2;
  }
  
  public Iterable<? extends XShape> getSelection() {
    Iterable<? extends XShape> _allShapes = this.rootDiagram.getAllShapes();
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
    Iterable<? extends XShape> _filter = IterableExtensions.filter(_allShapes, _function);
    return _filter;
  }
  
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      Scene _scene = this.rootDiagram.getScene();
      _scene.<MouseEvent>addEventFilter(MouseEvent.MOUSE_PRESSED, this.mousePressedHandler);
      this.rootDiagram.<MouseEvent>addEventFilter(MouseEvent.MOUSE_DRAGGED, this.mouseDraggedHandler);
      this.rootDiagram.<MouseEvent>addEventFilter(MouseEvent.MOUSE_RELEASED, this.mouseReleasedHandler);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
  
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      Scene _scene = this.rootDiagram.getScene();
      _scene.<MouseEvent>removeEventFilter(MouseEvent.MOUSE_PRESSED, this.mousePressedHandler);
      this.rootDiagram.<MouseEvent>removeEventFilter(MouseEvent.MOUSE_DRAGGED, this.mouseDraggedHandler);
      this.rootDiagram.<MouseEvent>removeEventFilter(MouseEvent.MOUSE_RELEASED, this.mouseReleasedHandler);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
}
