package de.fxdiagram.core.tools;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.Point2DExtensions;
import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.core.tools.XDiagramTool;
import de.fxdiagram.core.viewport.ViewportTransform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class DiagramMouseTool implements XDiagramTool {
  public static class DragContext {
    @Accessors
    private double sceneX;
    
    @Accessors
    private double sceneY;
    
    @Accessors
    private double screenX;
    
    @Accessors
    private double screenY;
    
    @Accessors
    private double previousScale = 1;
    
    @Accessors
    private Point2D pivotInDiagram;
    
    @Pure
    public double getSceneX() {
      return this.sceneX;
    }
    
    public void setSceneX(final double sceneX) {
      this.sceneX = sceneX;
    }
    
    @Pure
    public double getSceneY() {
      return this.sceneY;
    }
    
    public void setSceneY(final double sceneY) {
      this.sceneY = sceneY;
    }
    
    @Pure
    public double getScreenX() {
      return this.screenX;
    }
    
    public void setScreenX(final double screenX) {
      this.screenX = screenX;
    }
    
    @Pure
    public double getScreenY() {
      return this.screenY;
    }
    
    public void setScreenY(final double screenY) {
      this.screenY = screenY;
    }
    
    @Pure
    public double getPreviousScale() {
      return this.previousScale;
    }
    
    public void setPreviousScale(final double previousScale) {
      this.previousScale = previousScale;
    }
    
    @Pure
    public Point2D getPivotInDiagram() {
      return this.pivotInDiagram;
    }
    
    public void setPivotInDiagram(final Point2D pivotInDiagram) {
      this.pivotInDiagram = pivotInDiagram;
    }
  }
  
  private XRoot root;
  
  private DiagramMouseTool.DragContext dragContext;
  
  private EventHandler<MouseEvent> pressedHandler;
  
  private EventHandler<MouseEvent> dragHandler;
  
  private EventHandler<MouseEvent> releasedHandler;
  
  private final static int ZOOM_SENSITIVITY = 30;
  
  private boolean hasDragged = false;
  
  private final static ImageCursor zoomInCursor = new ImageCursor(ImageCache.get().getImage(DiagramMouseTool.class, "zoom_in.png"));
  
  private final static ImageCursor zoomOutCursor = new ImageCursor(ImageCache.get().getImage(DiagramMouseTool.class, "zoom_out.png"));
  
  public DiagramMouseTool(final XRoot root) {
    this.root = root;
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      @Override
      public void handle(final MouseEvent event) {
        DiagramMouseTool.DragContext _dragContext = new DiagramMouseTool.DragContext();
        final Procedure1<DiagramMouseTool.DragContext> _function = new Procedure1<DiagramMouseTool.DragContext>() {
          @Override
          public void apply(final DiagramMouseTool.DragContext it) {
            ViewportTransform _viewportTransform = root.getViewportTransform();
            double _translateX = _viewportTransform.getTranslateX();
            double _sceneX = event.getSceneX();
            double _minus = (_translateX - _sceneX);
            it.sceneX = _minus;
            ViewportTransform _viewportTransform_1 = root.getViewportTransform();
            double _translateY = _viewportTransform_1.getTranslateY();
            double _sceneY = event.getSceneY();
            double _minus_1 = (_translateY - _sceneY);
            it.sceneY = _minus_1;
            double _screenX = event.getScreenX();
            it.screenX = _screenX;
            double _screenY = event.getScreenY();
            it.screenY = _screenY;
            XDiagram _diagram = root.getDiagram();
            double _sceneX_1 = event.getSceneX();
            double _sceneY_1 = event.getSceneY();
            Point2D _sceneToLocal = _diagram.sceneToLocal(_sceneX_1, _sceneY_1);
            it.pivotInDiagram = _sceneToLocal;
          }
        };
        DiagramMouseTool.DragContext _doubleArrow = ObjectExtensions.<DiagramMouseTool.DragContext>operator_doubleArrow(_dragContext, _function);
        DiagramMouseTool.this.dragContext = _doubleArrow;
        MouseButton _button = event.getButton();
        boolean _equals = Objects.equal(_button, MouseButton.PRIMARY);
        if (_equals) {
          Scene _scene = root.getScene();
          _scene.setCursor(Cursor.OPEN_HAND);
        } else {
          boolean _isShortcutDown = event.isShortcutDown();
          if (_isShortcutDown) {
            Scene _scene_1 = root.getScene();
            _scene_1.setCursor(DiagramMouseTool.zoomOutCursor);
          } else {
            Scene _scene_2 = root.getScene();
            _scene_2.setCursor(DiagramMouseTool.zoomInCursor);
          }
        }
        DiagramMouseTool.this.hasDragged = false;
        event.consume();
      }
    };
    this.pressedHandler = _function;
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
      @Override
      public void handle(final MouseEvent it) {
        boolean _notEquals = (!Objects.equal(DiagramMouseTool.this.dragContext, null));
        if (_notEquals) {
          DiagramMouseTool.this.hasDragged = true;
          MouseButton _button = it.getButton();
          boolean _equals = Objects.equal(_button, MouseButton.PRIMARY);
          if (_equals) {
            ViewportTransform _viewportTransform = root.getViewportTransform();
            double _sceneX = it.getSceneX();
            double _plus = (DiagramMouseTool.this.dragContext.sceneX + _sceneX);
            _viewportTransform.setTranslateX(_plus);
            ViewportTransform _viewportTransform_1 = root.getViewportTransform();
            double _sceneY = it.getSceneY();
            double _plus_1 = (DiagramMouseTool.this.dragContext.sceneY + _sceneY);
            _viewportTransform_1.setTranslateY(_plus_1);
          } else {
            double _screenX = it.getScreenX();
            double _minus = (_screenX - DiagramMouseTool.this.dragContext.screenX);
            double _screenY = it.getScreenY();
            double _minus_1 = (_screenY - DiagramMouseTool.this.dragContext.screenY);
            double _norm = Point2DExtensions.norm(_minus, _minus_1);
            double _divide = (_norm / DiagramMouseTool.ZOOM_SENSITIVITY);
            double totalZoomFactor = (1 + _divide);
            boolean _isShortcutDown = it.isShortcutDown();
            if (_isShortcutDown) {
              totalZoomFactor = (1 / totalZoomFactor);
            }
            final double scale = (totalZoomFactor / DiagramMouseTool.this.dragContext.previousScale);
            ViewportTransform _viewportTransform_2 = root.getViewportTransform();
            _viewportTransform_2.scaleRelative(scale);
            XDiagram _diagram = root.getDiagram();
            final Point2D pivotInScene = _diagram.localToScene(DiagramMouseTool.this.dragContext.pivotInDiagram);
            ViewportTransform _viewportTransform_3 = root.getViewportTransform();
            double _sceneX_1 = it.getSceneX();
            double _x = pivotInScene.getX();
            double _minus_2 = (_sceneX_1 - _x);
            double _sceneY_1 = it.getSceneY();
            double _y = pivotInScene.getY();
            double _minus_3 = (_sceneY_1 - _y);
            _viewportTransform_3.translateRelative(_minus_2, _minus_3);
            DiagramMouseTool.this.dragContext.previousScale = totalZoomFactor;
          }
          it.consume();
        }
      }
    };
    this.dragHandler = _function_1;
    final EventHandler<MouseEvent> _function_2 = new EventHandler<MouseEvent>() {
      @Override
      public void handle(final MouseEvent it) {
        if (DiagramMouseTool.this.hasDragged) {
          DiagramMouseTool.this.hasDragged = false;
          DiagramMouseTool.this.dragContext = null;
          it.consume();
        }
        Scene _scene = root.getScene();
        _scene.setCursor(Cursor.DEFAULT);
      }
    };
    this.releasedHandler = _function_2;
  }
  
  @Override
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      final Scene scene = this.root.getScene();
      scene.<MouseEvent>addEventHandler(MouseEvent.MOUSE_PRESSED, this.pressedHandler);
      scene.<MouseEvent>addEventHandler(MouseEvent.MOUSE_DRAGGED, this.dragHandler);
      this.root.<MouseEvent>addEventHandler(MouseEvent.MOUSE_RELEASED, this.releasedHandler);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      final Scene scene = this.root.getScene();
      scene.<MouseEvent>removeEventHandler(MouseEvent.MOUSE_PRESSED, this.pressedHandler);
      scene.<MouseEvent>removeEventHandler(MouseEvent.MOUSE_DRAGGED, this.dragHandler);
      this.root.<MouseEvent>removeEventHandler(MouseEvent.MOUSE_RELEASED, this.releasedHandler);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
}
