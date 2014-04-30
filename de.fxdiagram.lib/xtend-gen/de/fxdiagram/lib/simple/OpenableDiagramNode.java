package de.fxdiagram.lib.simple;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.behavior.AbstractCloseBehavior;
import de.fxdiagram.core.behavior.AbstractOpenBehavior;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.viewport.ViewportMemento;
import de.fxdiagram.core.viewport.ViewportTransform;
import de.fxdiagram.core.viewport.ViewportTransition;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.lib.simple.CloseDiagramCommand;
import de.fxdiagram.lib.simple.DiagramScaler;
import de.fxdiagram.lib.simple.OpenDiagramCommand;
import eu.hansolo.enzo.radialmenu.Symbol;
import eu.hansolo.enzo.radialmenu.SymbolCanvas;
import java.util.List;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
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
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@ModelNode({ "innerDiagram" })
@SuppressWarnings("all")
public class OpenableDiagramNode extends XNode {
  private XRoot root;
  
  private RectangleBorderPane pane = new RectangleBorderPane();
  
  private Text textNode;
  
  private double delayFactor = 0.1;
  
  private Point2D nodeCenterInDiagram;
  
  private DiagramScaler diagramScaler;
  
  private BoundingBox nodeBounds;
  
  private ViewportMemento viewportBeforeOpen;
  
  public OpenableDiagramNode(final String name) {
    super(name);
  }
  
  public OpenableDiagramNode(final DomainObjectDescriptor domainObject) {
    super(domainObject);
  }
  
  protected Node createNode() {
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
      public void apply(final RectangleBorderPane it) {
        ObservableList<Node> _children = it.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            it.setTextOrigin(VPos.TOP);
            String _name = OpenableDiagramNode.this.getName();
            it.setText(_name);
            Insets _insets = new Insets(10, 20, 10, 20);
            StackPane.setMargin(it, _insets);
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
        Text _textNode = OpenableDiagramNode.this.textNode = _doubleArrow;
        _children.add(_textNode);
      }
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(
      this.pane, _function);
  }
  
  public Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 12, 12);
  }
  
  public void doActivate() {
    super.doActivate();
    TooltipExtensions.setTooltip(this.pane, "Double-click to open");
    this.setCursor(Cursor.HAND);
    String _name = this.getName();
    this.textNode.setText(_name);
    XRoot _root = CoreExtensions.getRoot(this);
    this.root = _root;
    XDiagram _innerDiagram = this.getInnerDiagram();
    boolean _equals = Objects.equal(_innerDiagram, null);
    if (_equals) {
      OpenableDiagramNode.LOG.severe("Nested diagram not set. Deactivating open behavior");
    } else {
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
    final AbstractOpenBehavior _function_1 = new AbstractOpenBehavior() {
      public void open() {
        OpenableDiagramNode.this.openDiagram();
      }
    };
    final AbstractOpenBehavior openBehavior = _function_1;
    this.addBehavior(openBehavior);
  }
  
  public RectangleBorderPane getPane() {
    return this.pane;
  }
  
  public Text getTextNode() {
    return this.textNode;
  }
  
  public void openDiagram() {
    CommandStack _commandStack = this.root.getCommandStack();
    OpenDiagramCommand _openDiagramCommand = new OpenDiagramCommand(this);
    _commandStack.execute(_openDiagramCommand);
  }
  
  protected ParallelTransition openDiagram(final Duration duration) {
    ParallelTransition _xblockexpression = null;
    {
      XDiagram _diagram = CoreExtensions.getDiagram(this);
      ViewportTransform _viewportTransform = _diagram.getViewportTransform();
      ViewportMemento _createMemento = _viewportTransform.createMemento();
      this.viewportBeforeOpen = _createMemento;
      Bounds _layoutBounds = this.getLayoutBounds();
      Insets _insets = new Insets(5, 5, 5, 5);
      BoundingBox _minus = BoundsExtensions.operator_minus(_layoutBounds, _insets);
      this.nodeBounds = _minus;
      Point2D _center = BoundsExtensions.center(this.nodeBounds);
      Point2D _localToRootDiagram = CoreExtensions.localToRootDiagram(this, _center);
      this.nodeCenterInDiagram = _localToRootDiagram;
      XDiagram _innerDiagram = this.getInnerDiagram();
      DiagramScaler _diagramScaler = new DiagramScaler(_innerDiagram);
      final Procedure1<DiagramScaler> _function = new Procedure1<DiagramScaler>() {
        public void apply(final DiagramScaler it) {
          double _width = OpenableDiagramNode.this.nodeBounds.getWidth();
          it.setWidth(_width);
          double _height = OpenableDiagramNode.this.nodeBounds.getHeight();
          it.setHeight(_height);
        }
      };
      DiagramScaler _doubleArrow = ObjectExtensions.<DiagramScaler>operator_doubleArrow(_diagramScaler, _function);
      this.diagramScaler = _doubleArrow;
      XDiagram _innerDiagram_1 = this.getInnerDiagram();
      _innerDiagram_1.setOpacity(0);
      ObservableList<Node> _children = this.pane.getChildren();
      Group _group = new Group();
      final Procedure1<Group> _function_1 = new Procedure1<Group>() {
        public void apply(final Group it) {
          ObservableList<Node> _children = it.getChildren();
          XDiagram _innerDiagram = OpenableDiagramNode.this.getInnerDiagram();
          _children.add(_innerDiagram);
        }
      };
      Group _doubleArrow_1 = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_1);
      _children.add(_doubleArrow_1);
      XDiagram _innerDiagram_2 = this.getInnerDiagram();
      _innerDiagram_2.activate();
      final AbstractCloseBehavior _function_2 = new AbstractCloseBehavior() {
        public void close() {
          CommandStack _commandStack = OpenableDiagramNode.this.root.getCommandStack();
          CloseDiagramCommand _closeDiagramCommand = new CloseDiagramCommand(OpenableDiagramNode.this);
          _commandStack.execute(_closeDiagramCommand);
        }
      };
      final AbstractCloseBehavior closeBehavior = _function_2;
      XDiagram _innerDiagram_3 = this.getInnerDiagram();
      _innerDiagram_3.addBehavior(closeBehavior);
      XDiagram _innerDiagram_4 = this.getInnerDiagram();
      _innerDiagram_4.layout();
      this.diagramScaler.activate();
      XDiagram _innerDiagram_5 = this.getInnerDiagram();
      BoundingBox _boundingBox = new BoundingBox(0, 0, 1, 0);
      Bounds _localToScene = _innerDiagram_5.localToScene(_boundingBox);
      final double initialScale = _localToScene.getWidth();
      XDiagram _innerDiagram_6 = this.getInnerDiagram();
      final Bounds diagramBoundsInLocal = _innerDiagram_6.getBoundsInLocal();
      Scene _scene = this.root.getScene();
      double _width = _scene.getWidth();
      double _width_1 = diagramBoundsInLocal.getWidth();
      double _divide = (_width / _width_1);
      Scene _scene_1 = this.root.getScene();
      double _height = _scene_1.getHeight();
      double _height_1 = diagramBoundsInLocal.getHeight();
      double _divide_1 = (_height / _height_1);
      double _min = Math.min(_divide, _divide_1);
      double _min_1 = Math.min(1, _min);
      double _divide_2 = (_min_1 / initialScale);
      double _max = Math.max(ViewportTransform.MIN_SCALE, _divide_2);
      ViewportTransform _viewportTransform_1 = this.root.getViewportTransform();
      double _scale = _viewportTransform_1.getScale();
      final double targetScale = (_max * _scale);
      ParallelTransition _parallelTransition = new ParallelTransition();
      final Procedure1<ParallelTransition> _function_3 = new Procedure1<ParallelTransition>() {
        public void apply(final ParallelTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          ViewportTransition _viewportTransition = new ViewportTransition(OpenableDiagramNode.this.root, OpenableDiagramNode.this.nodeCenterInDiagram, targetScale);
          final Procedure1<ViewportTransition> _function = new Procedure1<ViewportTransition>() {
            public void apply(final ViewportTransition it) {
              it.setDuration(duration);
              final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                public void handle(final ActionEvent it) {
                  OpenableDiagramNode.this.diagramScaler.deactivate();
                  XDiagram _diagram = OpenableDiagramNode.this.root.getDiagram();
                  OpenableDiagramNode.this.setParentDiagram(_diagram);
                  ObservableList<Node> _children = OpenableDiagramNode.this.pane.getChildren();
                  _children.setAll(OpenableDiagramNode.this.textNode);
                  Canvas _symbol = SymbolCanvas.getSymbol(Symbol.Type.ZOOM_OUT, 32, Color.GRAY);
                  final Procedure1<Canvas> _function = new Procedure1<Canvas>() {
                    public void apply(final Canvas it) {
                      final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
                        public void handle(final MouseEvent it) {
                          HeadsUpDisplay _headsUpDisplay = OpenableDiagramNode.this.root.getHeadsUpDisplay();
                          ObservableList<Node> _children = _headsUpDisplay.getChildren();
                          EventTarget _target = it.getTarget();
                          _children.remove(((Node) _target));
                          CommandStack _commandStack = OpenableDiagramNode.this.root.getCommandStack();
                          CloseDiagramCommand _closeDiagramCommand = new CloseDiagramCommand(OpenableDiagramNode.this);
                          _commandStack.execute(_closeDiagramCommand);
                        }
                      };
                      it.setOnMouseClicked(_function);
                      TooltipExtensions.setTooltip(it, "Parent diagram");
                    }
                  };
                  final Canvas toParentButton = ObjectExtensions.<Canvas>operator_doubleArrow(_symbol, _function);
                  XDiagram _innerDiagram = OpenableDiagramNode.this.getInnerDiagram();
                  ObservableMap<Node,Pos> _fixedButtons = _innerDiagram.getFixedButtons();
                  _fixedButtons.put(toParentButton, Pos.TOP_RIGHT);
                  XDiagram _innerDiagram_1 = OpenableDiagramNode.this.getInnerDiagram();
                  OpenableDiagramNode.this.root.setDiagram(_innerDiagram_1);
                }
              };
              it.setOnFinished(_function);
            }
          };
          ViewportTransition _doubleArrow = ObjectExtensions.<ViewportTransition>operator_doubleArrow(_viewportTransition, _function);
          _children.add(_doubleArrow);
          ObservableList<Animation> _children_1 = it.getChildren();
          FadeTransition _fadeTransition = new FadeTransition();
          final Procedure1<FadeTransition> _function_1 = new Procedure1<FadeTransition>() {
            public void apply(final FadeTransition it) {
              Duration _multiply = DurationExtensions.operator_multiply((1 - OpenableDiagramNode.this.delayFactor), duration);
              it.setDuration(_multiply);
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
              Duration _multiply = DurationExtensions.operator_multiply(OpenableDiagramNode.this.delayFactor, duration);
              it.setDelay(_multiply);
              Duration _multiply_1 = DurationExtensions.operator_multiply((1 - OpenableDiagramNode.this.delayFactor), duration);
              it.setDuration(_multiply_1);
              it.setFromValue(0);
              it.setToValue(1);
              XDiagram _innerDiagram = OpenableDiagramNode.this.getInnerDiagram();
              it.setNode(_innerDiagram);
            }
          };
          FadeTransition _doubleArrow_2 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition_1, _function_2);
          _children_2.add(_doubleArrow_2);
        }
      };
      _xblockexpression = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_3);
    }
    return _xblockexpression;
  }
  
  protected SequentialTransition closeDiagram(final Duration duration) {
    SequentialTransition _xblockexpression = null;
    {
      XDiagram _innerDiagram = this.getInnerDiagram();
      ObservableList<XNode> _nodes = _innerDiagram.getNodes();
      final Function1<XNode,BoundingBox> _function = new Function1<XNode,BoundingBox>() {
        public BoundingBox apply(final XNode it) {
          Bounds _layoutBounds = it.getLayoutBounds();
          double _layoutX = it.getLayoutX();
          double _layoutY = it.getLayoutY();
          return BoundsExtensions.translate(_layoutBounds, _layoutX, _layoutY);
        }
      };
      List<BoundingBox> _map = ListExtensions.<XNode, BoundingBox>map(_nodes, _function);
      final Function2<BoundingBox,BoundingBox,BoundingBox> _function_1 = new Function2<BoundingBox,BoundingBox,BoundingBox>() {
        public BoundingBox apply(final BoundingBox b0, final BoundingBox b1) {
          return BoundsExtensions.operator_plus(b0, b1);
        }
      };
      BoundingBox _reduce = IterableExtensions.<BoundingBox>reduce(_map, _function_1);
      final Point2D innerDiagramCenter = BoundsExtensions.center(_reduce);
      final ViewportTransition phaseOne = new ViewportTransition(this.root, innerDiagramCenter, 1);
      ParallelTransition _parallelTransition = new ParallelTransition();
      final Procedure1<ParallelTransition> _function_2 = new Procedure1<ParallelTransition>() {
        public void apply(final ParallelTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          FadeTransition _fadeTransition = new FadeTransition();
          final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
            public void apply(final FadeTransition it) {
              Duration _multiply = DurationExtensions.operator_multiply(OpenableDiagramNode.this.delayFactor, duration);
              it.setDelay(_multiply);
              Duration _multiply_1 = DurationExtensions.operator_multiply((1 - OpenableDiagramNode.this.delayFactor), duration);
              it.setDuration(_multiply_1);
              it.setFromValue(0);
              it.setToValue(1);
              it.setNode(OpenableDiagramNode.this.textNode);
            }
          };
          FadeTransition _doubleArrow = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
          _children.add(_doubleArrow);
          ObservableList<Animation> _children_1 = it.getChildren();
          FadeTransition _fadeTransition_1 = new FadeTransition();
          final Procedure1<FadeTransition> _function_1 = new Procedure1<FadeTransition>() {
            public void apply(final FadeTransition it) {
              Duration _multiply = DurationExtensions.operator_multiply((1 - OpenableDiagramNode.this.delayFactor), duration);
              it.setDuration(_multiply);
              it.setFromValue(1);
              it.setToValue(0);
              XDiagram _innerDiagram = OpenableDiagramNode.this.getInnerDiagram();
              it.setNode(_innerDiagram);
            }
          };
          FadeTransition _doubleArrow_1 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition_1, _function_1);
          _children_1.add(_doubleArrow_1);
        }
      };
      final ParallelTransition phaseTwo = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_2);
      SequentialTransition _sequentialTransition = new SequentialTransition();
      final Procedure1<SequentialTransition> _function_3 = new Procedure1<SequentialTransition>() {
        public void apply(final SequentialTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          final Procedure1<ViewportTransition> _function = new Procedure1<ViewportTransition>() {
            public void apply(final ViewportTransition it) {
              Duration _multiply = DurationExtensions.operator_multiply(duration, 0.3);
              it.setDuration(_multiply);
              final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                public void handle(final ActionEvent it) {
                  XDiagram _parentDiagram = OpenableDiagramNode.this.getParentDiagram();
                  OpenableDiagramNode.this.root.setDiagram(_parentDiagram);
                  ObservableList<Node> _children = OpenableDiagramNode.this.pane.getChildren();
                  Group _group = new Group();
                  final Procedure1<Group> _function = new Procedure1<Group>() {
                    public void apply(final Group it) {
                      ObservableList<Node> _children = it.getChildren();
                      XDiagram _innerDiagram = OpenableDiagramNode.this.getInnerDiagram();
                      _children.add(_innerDiagram);
                    }
                  };
                  Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
                  _children.add(_doubleArrow);
                  OpenableDiagramNode.this.diagramScaler.activate();
                  ObservableList<Animation> _children_1 = phaseTwo.getChildren();
                  ViewportTransition _viewportTransition = new ViewportTransition(OpenableDiagramNode.this.root, OpenableDiagramNode.this.viewportBeforeOpen, duration);
                  final Procedure1<ViewportTransition> _function_1 = new Procedure1<ViewportTransition>() {
                    public void apply(final ViewportTransition it) {
                      final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                        public void handle(final ActionEvent it) {
                          OpenableDiagramNode.this.diagramScaler.deactivate();
                          XDiagram _diagram = OpenableDiagramNode.this.root.getDiagram();
                          OpenableDiagramNode.this.setParentDiagram(_diagram);
                          ObservableList<Node> _children = OpenableDiagramNode.this.pane.getChildren();
                          _children.setAll(OpenableDiagramNode.this.textNode);
                        }
                      };
                      it.setOnFinished(_function);
                    }
                  };
                  ViewportTransition _doubleArrow_1 = ObjectExtensions.<ViewportTransition>operator_doubleArrow(_viewportTransition, _function_1);
                  _children_1.add(0, _doubleArrow_1);
                }
              };
              it.setOnFinished(_function);
            }
          };
          ViewportTransition _doubleArrow = ObjectExtensions.<ViewportTransition>operator_doubleArrow(phaseOne, _function);
          _children.add(_doubleArrow);
          ObservableList<Animation> _children_1 = it.getChildren();
          _children_1.add(phaseTwo);
        }
      };
      _xblockexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function_3);
    }
    return _xblockexpression;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.lib.simple.OpenableDiagramNode");
    ;
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public OpenableDiagramNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(innerDiagramProperty, XDiagram.class);
  }
  
  private SimpleObjectProperty<XDiagram> innerDiagramProperty = new SimpleObjectProperty<XDiagram>(this, "innerDiagram");
  
  public XDiagram getInnerDiagram() {
    return this.innerDiagramProperty.get();
  }
  
  public void setInnerDiagram(final XDiagram innerDiagram) {
    this.innerDiagramProperty.set(innerDiagram);
  }
  
  public ObjectProperty<XDiagram> innerDiagramProperty() {
    return this.innerDiagramProperty;
  }
  
  private SimpleObjectProperty<XDiagram> parentDiagramProperty = new SimpleObjectProperty<XDiagram>(this, "parentDiagram");
  
  public XDiagram getParentDiagram() {
    return this.parentDiagramProperty.get();
  }
  
  public void setParentDiagram(final XDiagram parentDiagram) {
    this.parentDiagramProperty.set(parentDiagram);
  }
  
  public ObjectProperty<XDiagram> parentDiagramProperty() {
    return this.parentDiagramProperty;
  }
}
