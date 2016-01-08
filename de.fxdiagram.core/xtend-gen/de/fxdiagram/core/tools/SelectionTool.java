package de.fxdiagram.core.tools;

import com.google.common.base.Objects;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XButton;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.auxlines.AuxiliaryLinesSupport;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.SoftTooltip;
import de.fxdiagram.core.extensions.TimerExtensions;
import de.fxdiagram.core.tools.XDiagramTool;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class SelectionTool implements XDiagramTool {
  private XRoot root;
  
  private EventHandler<MouseEvent> mousePressedHandler;
  
  private EventHandler<MouseEvent> mouseDraggedHandler;
  
  private EventHandler<MouseEvent> mouseReleasedHandler;
  
  private SoftTooltip positionTip;
  
  private boolean isActionOnDiagram = false;
  
  private boolean hasDragged = false;
  
  public SelectionTool(final XRoot root) {
    this.root = root;
    final EventHandler<MouseEvent> _function = (MouseEvent event) -> {
      Iterable<XShape> _currentSelection = root.getCurrentSelection();
      final Set<XShape> selection = IterableExtensions.<XShape>toSet(_currentSelection);
      this.hasDragged = false;
      EventTarget _target = event.getTarget();
      Pane _diagramCanvas = root.getDiagramCanvas();
      boolean _equals = Objects.equal(_target, _diagramCanvas);
      if (_equals) {
        this.isActionOnDiagram = true;
      } else {
        XButton _targetButton = ButtonExtensions.getTargetButton(event);
        boolean _not = (!(_targetButton instanceof XButton));
        if (_not) {
          final XShape targetShape = this.getTargetShape(event);
          boolean _isSelectable = false;
          if (targetShape!=null) {
            _isSelectable=targetShape.isSelectable();
          }
          if (_isSelectable) {
            final boolean targetWasSelected = targetShape.getSelected();
            boolean _isShortcutDown = event.isShortcutDown();
            if (_isShortcutDown) {
              boolean _selected = targetShape.getSelected();
              boolean _not_1 = (!_selected);
              targetShape.setSelected(_not_1);
            } else {
              targetShape.select(event);
            }
            boolean _or = false;
            if (targetWasSelected) {
              _or = true;
            } else {
              boolean _isShortcutDown_1 = event.isShortcutDown();
              _or = _isShortcutDown_1;
            }
            if (_or) {
              final Function1<XShape, Boolean> _function_1 = (XShape it) -> {
                XDiagram _diagram = CoreExtensions.getDiagram(it);
                XDiagram _diagram_1 = CoreExtensions.getDiagram(targetShape);
                return Boolean.valueOf((!Objects.equal(_diagram, _diagram_1)));
              };
              this.deselect(selection, _function_1);
            } else {
              final Function1<XShape, Boolean> _function_2 = (XShape it) -> {
                return Boolean.valueOf(true);
              };
              this.deselect(selection, _function_2);
            }
            boolean _selected_1 = targetShape.getSelected();
            if (_selected_1) {
              selection.add(targetShape);
            }
            final Consumer<XShape> _function_3 = (XShape it) -> {
              MoveBehavior _behavior = it.<MoveBehavior>getBehavior(MoveBehavior.class);
              if (_behavior!=null) {
                _behavior.mousePressed(event);
              }
            };
            selection.forEach(_function_3);
            MoveBehavior _behavior = targetShape.<MoveBehavior>getBehavior(MoveBehavior.class);
            if (_behavior!=null) {
              _behavior.mousePressed(event);
            }
            double _sceneX = event.getSceneX();
            double _sceneY = event.getSceneY();
            this.updatePositionTooltip(selection, _sceneX, _sceneY);
            final Runnable _function_4 = () -> {
              this.showPositionTooltip();
            };
            Duration _millis = DurationExtensions.millis(200);
            TimerExtensions.defer(_function_4, _millis);
          }
          this.isActionOnDiagram = false;
        }
      }
    };
    this.mousePressedHandler = _function;
    final EventHandler<MouseEvent> _function_1 = (MouseEvent it) -> {
      this.hasDragged = true;
      if ((!this.isActionOnDiagram)) {
        final Iterable<XShape> selection = root.getCurrentSelection();
        for (final XShape shape : selection) {
          MoveBehavior _behavior = null;
          if (shape!=null) {
            _behavior=shape.<MoveBehavior>getBehavior(MoveBehavior.class);
          }
          if (_behavior!=null) {
            _behavior.mouseDragged(it);
          }
        }
        XDiagram _diagram = root.getDiagram();
        AuxiliaryLinesSupport _auxiliaryLinesSupport = _diagram.getAuxiliaryLinesSupport();
        if (_auxiliaryLinesSupport!=null) {
          _auxiliaryLinesSupport.show(selection);
        }
        double _sceneX = it.getSceneX();
        double _sceneY = it.getSceneY();
        this.updatePositionTooltip(selection, _sceneX, _sceneY);
        this.showPositionTooltip();
        it.consume();
      }
    };
    this.mouseDraggedHandler = _function_1;
    final EventHandler<MouseEvent> _function_2 = (MouseEvent it) -> {
      boolean _and = false;
      if (!(this.isActionOnDiagram && (!this.hasDragged))) {
        _and = false;
      } else {
        MouseButton _button = it.getButton();
        boolean _equals = Objects.equal(_button, MouseButton.PRIMARY);
        _and = _equals;
      }
      if (_and) {
        Iterable<XShape> _currentSelection = root.getCurrentSelection();
        final Consumer<XShape> _function_3 = (XShape it_1) -> {
          it_1.setSelected(false);
        };
        _currentSelection.forEach(_function_3);
      }
      XDiagram _diagram = root.getDiagram();
      AuxiliaryLinesSupport _auxiliaryLinesSupport = _diagram.getAuxiliaryLinesSupport();
      if (_auxiliaryLinesSupport!=null) {
        _auxiliaryLinesSupport.hide();
      }
      this.hidePositionTooltip();
    };
    this.mouseReleasedHandler = _function_2;
  }
  
  protected void updatePositionTooltip(final Iterable<? extends XShape> selection, final double screenX, final double screenY) {
    final Function1<XShape, Bounds> _function = (XShape it) -> {
      Bounds _snapBounds = it.getSnapBounds();
      return CoreExtensions.localToRootDiagram(it, _snapBounds);
    };
    Iterable<Bounds> _map = IterableExtensions.map(selection, _function);
    final Function2<Bounds, Bounds, Bounds> _function_1 = (Bounds a, Bounds b) -> {
      return BoundsExtensions.operator_plus(a, b);
    };
    Bounds selectionBounds = IterableExtensions.<Bounds>reduce(_map, _function_1);
    boolean _notEquals = (!Objects.equal(selectionBounds, null));
    if (_notEquals) {
      double _minX = selectionBounds.getMinX();
      double _minY = selectionBounds.getMinY();
      final String positionString = String.format("(%.3f : %.3f)", Double.valueOf(_minX), Double.valueOf(_minY));
      SoftTooltip _elvis = null;
      if (this.positionTip != null) {
        _elvis = this.positionTip;
      } else {
        HeadsUpDisplay _headsUpDisplay = this.root.getHeadsUpDisplay();
        SoftTooltip _softTooltip = new SoftTooltip(_headsUpDisplay, positionString);
        _elvis = _softTooltip;
      }
      this.positionTip = _elvis;
      this.positionTip.setReferencePosition(screenX, screenY);
      this.positionTip.setText(positionString);
    }
  }
  
  protected boolean showPositionTooltip() {
    boolean _xifexpression = false;
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(this.positionTip, null));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _isShowing = this.positionTip.isShowing();
      boolean _not = (!_isShowing);
      _and = _not;
    }
    if (_and) {
      _xifexpression = this.positionTip.show();
    }
    return _xifexpression;
  }
  
  protected SoftTooltip hidePositionTooltip() {
    SoftTooltip _xblockexpression = null;
    {
      boolean _isShowing = false;
      if (this.positionTip!=null) {
        _isShowing=this.positionTip.isShowing();
      }
      if (_isShowing) {
        this.positionTip.hide();
      }
      _xblockexpression = this.positionTip = null;
    }
    return _xblockexpression;
  }
  
  protected void deselect(final Collection<XShape> selection, final Function1<? super XShape, ? extends Boolean> filter) {
    final Iterator<XShape> i = selection.iterator();
    while (i.hasNext()) {
      {
        final XShape element = i.next();
        Boolean _apply = filter.apply(element);
        if ((_apply).booleanValue()) {
          element.setSelected(false);
          i.remove();
        }
      }
    }
  }
  
  @Override
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      this.root.<MouseEvent>addEventFilter(MouseEvent.MOUSE_PRESSED, this.mousePressedHandler);
      this.root.<MouseEvent>addEventFilter(MouseEvent.MOUSE_DRAGGED, this.mouseDraggedHandler);
      this.root.<MouseEvent>addEventFilter(MouseEvent.MOUSE_RELEASED, this.mouseReleasedHandler);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      this.root.<MouseEvent>removeEventFilter(MouseEvent.MOUSE_PRESSED, this.mousePressedHandler);
      this.root.<MouseEvent>removeEventFilter(MouseEvent.MOUSE_DRAGGED, this.mouseDraggedHandler);
      this.root.<MouseEvent>removeEventFilter(MouseEvent.MOUSE_RELEASED, this.mouseReleasedHandler);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  private XShape getTargetShape(final MouseEvent event) {
    XShape _xblockexpression = null;
    {
      final EventTarget target = event.getTarget();
      XShape _xifexpression = null;
      if ((target instanceof Node)) {
        _xifexpression = this.getTargetShape(((Node)target));
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  private XShape getTargetShape(final Node it) {
    XShape _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (it instanceof XShape) {
        _matched=true;
        _switchResult = ((XShape)it);
      }
    }
    if (!_matched) {
      if (it instanceof ArrowHead) {
        _matched=true;
        _switchResult = ((ArrowHead)it).getConnection();
      }
    }
    if (!_matched) {
      if (Objects.equal(it, null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      _switchResult = this.getTargetShape(_parent);
    }
    return _switchResult;
  }
}
