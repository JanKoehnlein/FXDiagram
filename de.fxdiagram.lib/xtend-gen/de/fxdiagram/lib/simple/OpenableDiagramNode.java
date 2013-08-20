package de.fxdiagram.lib.simple;

import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.geometry.BoundsExtensions;
import de.fxdiagram.core.geometry.DurationExtensions;
import de.fxdiagram.core.tools.actions.ScrollToAndScaleTransition;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.lib.simple.DiagramScaler;
import eu.hansolo.enzo.radialmenu.Symbol.Type;
import eu.hansolo.enzo.radialmenu.SymbolCanvas;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class OpenableDiagramNode extends XNode {
  private XDiagram nestedDiagram;
  
  private XDiagram parentDiagram;
  
  private XRoot root;
  
  private RectangleBorderPane pane;
  
  private Text textNode;
  
  private DiagramScaler diagramScaler;
  
  private Duration _transitionDuration = new Function0<Duration>() {
    public Duration apply() {
      Duration _millis = DurationExtensions.millis(1000);
      return _millis;
    }
  }.apply();
  
  public Duration getTransitionDuration() {
    return this._transitionDuration;
  }
  
  public void setTransitionDuration(final Duration transitionDuration) {
    this._transitionDuration = transitionDuration;
  }
  
  private Duration _transitionDelay = new Function0<Duration>() {
    public Duration apply() {
      Duration _millis = DurationExtensions.millis(100);
      return _millis;
    }
  }.apply();
  
  public Duration getTransitionDelay() {
    return this._transitionDelay;
  }
  
  public void setTransitionDelay(final Duration transitionDelay) {
    this._transitionDelay = transitionDelay;
  }
  
  public OpenableDiagramNode(final String name, final XDiagram nestedDiagram) {
    this.nestedDiagram = nestedDiagram;
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
      public void apply(final RectangleBorderPane it) {
        ObservableList<Node> _children = it.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            it.setText(name);
            it.setTextOrigin(VPos.TOP);
            Insets _insets = new Insets(10, 20, 10, 20);
            StackPane.setMargin(it, _insets);
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
        Text _textNode = OpenableDiagramNode.this.textNode = _doubleArrow;
        _children.add(_textNode);
      }
    };
    RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
    RectangleBorderPane _pane = this.pane = _doubleArrow;
    this.setNode(_pane);
    this.setKey(name);
    this.setCursor(Cursor.HAND);
  }
  
  public Anchors createAnchors() {
    RoundedRectangleAnchors _roundedRectangleAnchors = new RoundedRectangleAnchors(this, 12, 12);
    return _roundedRectangleAnchors;
  }
  
  public void doActivate() {
    super.doActivate();
    XRoot _root = Extensions.getRoot(this);
    this.root = _root;
    Node _node = this.getNode();
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        int _clickCount = it.getClickCount();
        boolean _equals = (_clickCount == 2);
        if (_equals) {
          OpenableDiagramNode.this.openDiagram();
        }
      }
    };
    _node.setOnMouseClicked(_function);
  }
  
  protected ParallelTransition openDiagram() {
    ParallelTransition _xblockexpression = null;
    {
      Bounds _layoutBounds = this.getLayoutBounds();
      Insets _insets = new Insets(5, 5, 5, 5);
      final BoundingBox nodeBounds = BoundsExtensions.operator_minus(_layoutBounds, _insets);
      Point2D _center = BoundsExtensions.center(nodeBounds);
      final Point2D targetInDiagram = Extensions.localToRootDiagram(this, _center);
      ObservableList<Transform> _transforms = this.nestedDiagram.getTransforms();
      _transforms.clear();
      this.nestedDiagram.setOpacity(0);
      ObservableList<Node> _children = this.pane.getChildren();
      Group _group = new Group();
      final Procedure1<Group> _function = new Procedure1<Group>() {
        public void apply(final Group it) {
          ObservableList<Node> _children = it.getChildren();
          _children.add(OpenableDiagramNode.this.nestedDiagram);
        }
      };
      Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      _children.add(_doubleArrow);
      this.nestedDiagram.activate();
      this.nestedDiagram.layout();
      DiagramScaler _diagramScaler = new DiagramScaler(this.nestedDiagram);
      final Procedure1<DiagramScaler> _function_1 = new Procedure1<DiagramScaler>() {
        public void apply(final DiagramScaler it) {
          double _width = nodeBounds.getWidth();
          it.setWidth(_width);
          double _height = nodeBounds.getHeight();
          it.setHeight(_height);
          it.activate();
        }
      };
      DiagramScaler _doubleArrow_1 = ObjectExtensions.<DiagramScaler>operator_doubleArrow(_diagramScaler, _function_1);
      this.diagramScaler = _doubleArrow_1;
      BoundingBox _boundingBox = new BoundingBox(0, 0, 1, 0);
      Bounds _localToScene = this.nestedDiagram.localToScene(_boundingBox);
      final double initialScale = _localToScene.getWidth();
      final Bounds diagramBoundsInLocal = this.nestedDiagram.getBoundsInLocal();
      Scene _scene = this.getScene();
      double _width = _scene.getWidth();
      double _width_1 = diagramBoundsInLocal.getWidth();
      double _divide = (_width / _width_1);
      Scene _scene_1 = this.getScene();
      double _height = _scene_1.getHeight();
      double _height_1 = diagramBoundsInLocal.getHeight();
      double _divide_1 = (_height / _height_1);
      double _min = Math.min(_divide, _divide_1);
      double _min_1 = Math.min(1, _min);
      double _divide_2 = (_min_1 / initialScale);
      double _max = Math.max(XRoot.MIN_SCALE, _divide_2);
      double _diagramScale = this.root.getDiagramScale();
      final double targetScale = (_max * _diagramScale);
      ParallelTransition _parallelTransition = new ParallelTransition();
      final Procedure1<ParallelTransition> _function_2 = new Procedure1<ParallelTransition>() {
        public void apply(final ParallelTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          ScrollToAndScaleTransition _scrollToAndScaleTransition = new ScrollToAndScaleTransition(OpenableDiagramNode.this.root, targetInDiagram, targetScale);
          final Procedure1<ScrollToAndScaleTransition> _function = new Procedure1<ScrollToAndScaleTransition>() {
            public void apply(final ScrollToAndScaleTransition it) {
              Duration _transitionDuration = OpenableDiagramNode.this.getTransitionDuration();
              it.setDuration(_transitionDuration);
              final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                public void handle(final ActionEvent it) {
                  OpenableDiagramNode.this.diagramScaler.deactivate();
                  XDiagram _diagram = OpenableDiagramNode.this.root.getDiagram();
                  OpenableDiagramNode.this.parentDiagram = _diagram;
                  ObservableList<Transform> _transforms = OpenableDiagramNode.this.nestedDiagram.getTransforms();
                  _transforms.clear();
                  ObservableList<Node> _children = OpenableDiagramNode.this.pane.getChildren();
                  _children.setAll(OpenableDiagramNode.this.textNode);
                  OpenableDiagramNode.this.root.setDiagram(OpenableDiagramNode.this.nestedDiagram);
                  HeadsUpDisplay _headsUpDisplay = OpenableDiagramNode.this.root.getHeadsUpDisplay();
                  Canvas _symbol = SymbolCanvas.getSymbol(Type.ZOOM_OUT, 32, Color.GRAY);
                  final Procedure1<Canvas> _function = new Procedure1<Canvas>() {
                    public void apply(final Canvas it) {
                      final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
                        public void handle(final MouseEvent it) {
                          HeadsUpDisplay _headsUpDisplay = OpenableDiagramNode.this.root.getHeadsUpDisplay();
                          ObservableList<Node> _children = _headsUpDisplay.getChildren();
                          EventTarget _target = it.getTarget();
                          _children.remove(((Node) _target));
                          OpenableDiagramNode.this.closeDiagram(targetInDiagram);
                        }
                      };
                      it.setOnMouseClicked(_function);
                    }
                  };
                  Canvas _doubleArrow = ObjectExtensions.<Canvas>operator_doubleArrow(_symbol, _function);
                  _headsUpDisplay.add(_doubleArrow, Pos.TOP_RIGHT);
                }
              };
              it.setOnFinished(_function);
            }
          };
          ScrollToAndScaleTransition _doubleArrow = ObjectExtensions.<ScrollToAndScaleTransition>operator_doubleArrow(_scrollToAndScaleTransition, _function);
          _children.add(_doubleArrow);
          ObservableList<Animation> _children_1 = it.getChildren();
          FadeTransition _fadeTransition = new FadeTransition();
          final Procedure1<FadeTransition> _function_1 = new Procedure1<FadeTransition>() {
            public void apply(final FadeTransition it) {
              Duration _transitionDuration = OpenableDiagramNode.this.getTransitionDuration();
              Duration _transitionDelay = OpenableDiagramNode.this.getTransitionDelay();
              Duration _minus = DurationExtensions.operator_minus(_transitionDuration, _transitionDelay);
              it.setDuration(_minus);
              it.setFromValue(1);
              it.setToValue(0);
              it.setNode(OpenableDiagramNode.this.textNode);
            }
          };
          FadeTransition _doubleArrow_1 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function_1);
          _children_1.add(_doubleArrow_1);
          ObservableList<Animation> _children_2 = it.getChildren();
          FadeTransition _fadeTransition_1 = new FadeTransition();
          final Procedure1<FadeTransition> _function_2 = new Procedure1<FadeTransition>() {
            public void apply(final FadeTransition it) {
              Duration _transitionDelay = OpenableDiagramNode.this.getTransitionDelay();
              it.setDelay(_transitionDelay);
              Duration _transitionDuration = OpenableDiagramNode.this.getTransitionDuration();
              Duration _transitionDelay_1 = OpenableDiagramNode.this.getTransitionDelay();
              Duration _minus = DurationExtensions.operator_minus(_transitionDuration, _transitionDelay_1);
              it.setDuration(_minus);
              it.setFromValue(0);
              it.setToValue(1);
              it.setNode(OpenableDiagramNode.this.nestedDiagram);
            }
          };
          FadeTransition _doubleArrow_2 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition_1, _function_2);
          _children_2.add(_doubleArrow_2);
          it.play();
        }
      };
      ParallelTransition _doubleArrow_2 = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_2);
      _xblockexpression = (_doubleArrow_2);
    }
    return _xblockexpression;
  }
  
  protected ParallelTransition closeDiagram(final Point2D targetInDiagram) {
    ParallelTransition _xblockexpression = null;
    {
      this.root.setDiagram(this.parentDiagram);
      ObservableList<Node> _children = this.pane.getChildren();
      Group _group = new Group();
      final Procedure1<Group> _function = new Procedure1<Group>() {
        public void apply(final Group it) {
          ObservableList<Node> _children = it.getChildren();
          _children.add(OpenableDiagramNode.this.nestedDiagram);
        }
      };
      Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      _children.add(_doubleArrow);
      this.nestedDiagram.activate();
      this.nestedDiagram.layout();
      this.diagramScaler.activate();
      ParallelTransition _parallelTransition = new ParallelTransition();
      final Procedure1<ParallelTransition> _function_1 = new Procedure1<ParallelTransition>() {
        public void apply(final ParallelTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          ScrollToAndScaleTransition _scrollToAndScaleTransition = new ScrollToAndScaleTransition(OpenableDiagramNode.this.root, targetInDiagram, 1);
          final Procedure1<ScrollToAndScaleTransition> _function = new Procedure1<ScrollToAndScaleTransition>() {
            public void apply(final ScrollToAndScaleTransition it) {
              Duration _transitionDuration = OpenableDiagramNode.this.getTransitionDuration();
              it.setDuration(_transitionDuration);
              final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                public void handle(final ActionEvent it) {
                  OpenableDiagramNode.this.diagramScaler.deactivate();
                  XDiagram _diagram = OpenableDiagramNode.this.root.getDiagram();
                  OpenableDiagramNode.this.parentDiagram = _diagram;
                  ObservableList<Transform> _transforms = OpenableDiagramNode.this.nestedDiagram.getTransforms();
                  _transforms.clear();
                  ObservableList<Node> _children = OpenableDiagramNode.this.pane.getChildren();
                  _children.setAll(OpenableDiagramNode.this.textNode);
                }
              };
              it.setOnFinished(_function);
            }
          };
          ScrollToAndScaleTransition _doubleArrow = ObjectExtensions.<ScrollToAndScaleTransition>operator_doubleArrow(_scrollToAndScaleTransition, _function);
          _children.add(_doubleArrow);
          ObservableList<Animation> _children_1 = it.getChildren();
          FadeTransition _fadeTransition = new FadeTransition();
          final Procedure1<FadeTransition> _function_1 = new Procedure1<FadeTransition>() {
            public void apply(final FadeTransition it) {
              Duration _transitionDelay = OpenableDiagramNode.this.getTransitionDelay();
              it.setDelay(_transitionDelay);
              Duration _transitionDuration = OpenableDiagramNode.this.getTransitionDuration();
              Duration _transitionDelay_1 = OpenableDiagramNode.this.getTransitionDelay();
              Duration _minus = DurationExtensions.operator_minus(_transitionDuration, _transitionDelay_1);
              it.setDuration(_minus);
              it.setFromValue(0);
              it.setToValue(1);
              it.setNode(OpenableDiagramNode.this.textNode);
            }
          };
          FadeTransition _doubleArrow_1 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function_1);
          _children_1.add(_doubleArrow_1);
          ObservableList<Animation> _children_2 = it.getChildren();
          FadeTransition _fadeTransition_1 = new FadeTransition();
          final Procedure1<FadeTransition> _function_2 = new Procedure1<FadeTransition>() {
            public void apply(final FadeTransition it) {
              Duration _transitionDuration = OpenableDiagramNode.this.getTransitionDuration();
              Duration _transitionDelay = OpenableDiagramNode.this.getTransitionDelay();
              Duration _minus = DurationExtensions.operator_minus(_transitionDuration, _transitionDelay);
              it.setDuration(_minus);
              it.setFromValue(1);
              it.setToValue(0);
              it.setNode(OpenableDiagramNode.this.nestedDiagram);
            }
          };
          FadeTransition _doubleArrow_2 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition_1, _function_2);
          _children_2.add(_doubleArrow_2);
          it.play();
        }
      };
      ParallelTransition _doubleArrow_1 = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_1);
      _xblockexpression = (_doubleArrow_1);
    }
    return _xblockexpression;
  }
}
