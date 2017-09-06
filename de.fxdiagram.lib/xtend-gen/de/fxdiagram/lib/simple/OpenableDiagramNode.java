package de.fxdiagram.lib.simple;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDiagramContainer;
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
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.viewport.ViewportMemento;
import de.fxdiagram.core.viewport.ViewportTransform;
import de.fxdiagram.core.viewport.ViewportTransition;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.lib.simple.CloseDiagramCommand;
import de.fxdiagram.lib.simple.DiagramScaler;
import de.fxdiagram.lib.simple.OpenDiagramCommand;
import eu.hansolo.enzo.radialmenu.SymbolCanvas;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * An {@link XNode} containing a diagram that is shown when the user double-clicks.
 * 
 * A smooth transition zooms in, embedds the diagram, and zooms on until the inner
 * diagram has scale 1:1. Then the inner diagram becomes the new visible diagram.
 * The inner diagram can be left by clicking on the button in the top-right corner,
 * once again with a smooth transition.
 * 
 * This class requires Java SDK 7 or Java SDK 1.8.0_40 build &gt;5 to work.
 */
@Logging
@ModelNode("innerDiagram")
@SuppressWarnings("all")
public class OpenableDiagramNode extends XNode implements XDiagramContainer {
  private XRoot root;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private RectangleBorderPane pane = new RectangleBorderPane();
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private Node textNode;
  
  private double delayFactor = 0.1;
  
  private Point2D nodeCenterInDiagram;
  
  private DiagramScaler diagramScaler;
  
  private BoundingBox nodeBounds;
  
  private ViewportMemento viewportBeforeOpen;
  
  private boolean isOpen = false;
  
  public OpenableDiagramNode(final String name) {
    super(name);
  }
  
  public OpenableDiagramNode(final DomainObjectDescriptor domainObject) {
    super(domainObject);
  }
  
  @Override
  protected Node createNode() {
    final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
      ObservableList<Node> _children = it.getChildren();
      Node _createTextNode = this.createTextNode();
      _children.add(_createTextNode);
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(
      this.pane, _function);
  }
  
  protected Node createTextNode() {
    Text _text = new Text();
    final Procedure1<Text> _function = (Text it) -> {
      it.setTextOrigin(VPos.TOP);
      it.setText(this.getName());
      Insets _insets = new Insets(10, 20, 10, 20);
      StackPane.setMargin(it, _insets);
    };
    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
    return this.textNode = _doubleArrow;
  }
  
  @Override
  public Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 12, 12);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    TooltipExtensions.setTooltip(this.pane, "Double-click to open");
    this.setCursor(Cursor.HAND);
    this.root = CoreExtensions.getRoot(this);
    XDiagram _innerDiagram = this.getInnerDiagram();
    boolean _tripleEquals = (_innerDiagram == null);
    if (_tripleEquals) {
      OpenableDiagramNode.LOG.severe("Nested diagram not set. Deactivating open behavior");
    } else {
      Node _node = this.getNode();
      final EventHandler<MouseEvent> _function = (MouseEvent it) -> {
        int _clickCount = it.getClickCount();
        boolean _equals = (_clickCount == 2);
        if (_equals) {
          this.openDiagram();
        }
      };
      _node.setOnMouseClicked(_function);
    }
    final AbstractOpenBehavior _function_1 = new AbstractOpenBehavior() {
      @Override
      public void open() {
        OpenableDiagramNode.this.openDiagram();
      }
    };
    final AbstractOpenBehavior openBehavior = _function_1;
    this.addBehavior(openBehavior);
  }
  
  @Override
  public Insets getInsets() {
    return null;
  }
  
  @Override
  public boolean isInnerDiagramActive() {
    return this.isOpen;
  }
  
  public boolean openDiagram() {
    boolean _xblockexpression = false;
    {
      if ((!this.isOpen)) {
        CommandStack _commandStack = this.root.getCommandStack();
        OpenDiagramCommand _openDiagramCommand = new OpenDiagramCommand(this);
        _commandStack.execute(_openDiagramCommand);
      }
      _xblockexpression = this.isOpen = true;
    }
    return _xblockexpression;
  }
  
  protected ParallelTransition openDiagram(final Duration duration) {
    ParallelTransition _xblockexpression = null;
    {
      this.viewportBeforeOpen = CoreExtensions.getDiagram(this).getViewportTransform().createMemento();
      Bounds _layoutBounds = this.getLayoutBounds();
      Insets _insets = new Insets(5, 5, 5, 5);
      BoundingBox _minus = BoundsExtensions.operator_minus(_layoutBounds, _insets);
      this.nodeBounds = _minus;
      this.nodeCenterInDiagram = CoreExtensions.localToRootDiagram(this, BoundsExtensions.center(this.nodeBounds));
      XDiagram _innerDiagram = this.getInnerDiagram();
      DiagramScaler _diagramScaler = new DiagramScaler(_innerDiagram);
      final Procedure1<DiagramScaler> _function = (DiagramScaler it) -> {
        it.setWidth(this.nodeBounds.getWidth());
        it.setHeight(this.nodeBounds.getHeight());
      };
      DiagramScaler _doubleArrow = ObjectExtensions.<DiagramScaler>operator_doubleArrow(_diagramScaler, _function);
      this.diagramScaler = _doubleArrow;
      XDiagram _innerDiagram_1 = this.getInnerDiagram();
      _innerDiagram_1.setOpacity(0);
      ObservableList<Node> _children = this.pane.getChildren();
      Group _group = new Group();
      final Procedure1<Group> _function_1 = (Group it) -> {
        ObservableList<Node> _children_1 = it.getChildren();
        XDiagram _innerDiagram_2 = this.getInnerDiagram();
        _children_1.add(_innerDiagram_2);
      };
      Group _doubleArrow_1 = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_1);
      _children.add(_doubleArrow_1);
      this.getInnerDiagram().activate();
      final AbstractCloseBehavior _function_2 = new AbstractCloseBehavior() {
        @Override
        public void close() {
          OpenableDiagramNode.this.closeDiagram();
        }
      };
      final AbstractCloseBehavior closeBehavior = _function_2;
      this.getInnerDiagram().addBehavior(closeBehavior);
      this.getInnerDiagram().layout();
      this.diagramScaler.activate();
      this.layout();
      XDiagram _innerDiagram_2 = this.getInnerDiagram();
      BoundingBox _boundingBox = new BoundingBox(0, 0, 1, 0);
      final double initialScale = _innerDiagram_2.localToScene(_boundingBox).getWidth();
      final Bounds diagramBoundsInLocal = this.getInnerDiagram().getBoundsInLocal();
      double _width = this.root.getScene().getWidth();
      double _width_1 = diagramBoundsInLocal.getWidth();
      double _divide = (_width / _width_1);
      double _height = this.root.getScene().getHeight();
      double _height_1 = diagramBoundsInLocal.getHeight();
      double _divide_1 = (_height / _height_1);
      double _min = Math.min(1, 
        Math.min(_divide, _divide_1));
      double _divide_2 = (_min / initialScale);
      double _max = Math.max(ViewportTransform.MIN_SCALE, _divide_2);
      double _scale = this.root.getViewportTransform().getScale();
      final double targetScale = (_max * _scale);
      ParallelTransition _parallelTransition = new ParallelTransition();
      final Procedure1<ParallelTransition> _function_3 = (ParallelTransition it) -> {
        ObservableList<Animation> _children_1 = it.getChildren();
        ViewportTransition _viewportTransition = new ViewportTransition(this.root, this.nodeCenterInDiagram, targetScale);
        final Procedure1<ViewportTransition> _function_4 = (ViewportTransition it_1) -> {
          it_1.setDuration(duration);
          final EventHandler<ActionEvent> _function_5 = (ActionEvent it_2) -> {
            this.diagramScaler.deactivate();
            this.setParentDiagram(this.root.getDiagram());
            this.pane.getChildren().setAll(this.getTextNode());
            Canvas _symbol = SymbolCanvas.getSymbol(SymbolType.ZOOM_OUT, 32, Color.GRAY);
            final Procedure1<Canvas> _function_6 = (Canvas it_3) -> {
              final EventHandler<MouseEvent> _function_7 = (MouseEvent it_4) -> {
                ObservableList<Node> _children_2 = this.root.getHeadsUpDisplay().getChildren();
                EventTarget _target = it_4.getTarget();
                _children_2.remove(((Node) _target));
                this.closeDiagram();
              };
              it_3.setOnMouseClicked(_function_7);
              TooltipExtensions.setTooltip(it_3, "Parent diagram");
            };
            final Canvas toParentButton = ObjectExtensions.<Canvas>operator_doubleArrow(_symbol, _function_6);
            this.getInnerDiagram().getFixedButtons().put(toParentButton, Pos.TOP_RIGHT);
            this.root.setDiagram(this.getInnerDiagram());
          };
          it_1.setOnFinished(_function_5);
        };
        ViewportTransition _doubleArrow_2 = ObjectExtensions.<ViewportTransition>operator_doubleArrow(_viewportTransition, _function_4);
        _children_1.add(_doubleArrow_2);
        ObservableList<Animation> _children_2 = it.getChildren();
        FadeTransition _fadeTransition = new FadeTransition();
        final Procedure1<FadeTransition> _function_5 = (FadeTransition it_1) -> {
          Duration _multiply = DurationExtensions.operator_multiply((1 - this.delayFactor), duration);
          it_1.setDuration(_multiply);
          it_1.setFromValue(1);
          it_1.setToValue(0);
          it_1.setNode(this.getTextNode());
        };
        FadeTransition _doubleArrow_3 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function_5);
        _children_2.add(_doubleArrow_3);
        ObservableList<Animation> _children_3 = it.getChildren();
        FadeTransition _fadeTransition_1 = new FadeTransition();
        final Procedure1<FadeTransition> _function_6 = (FadeTransition it_1) -> {
          Duration _multiply = DurationExtensions.operator_multiply(this.delayFactor, duration);
          it_1.setDelay(_multiply);
          Duration _multiply_1 = DurationExtensions.operator_multiply((1 - this.delayFactor), duration);
          it_1.setDuration(_multiply_1);
          it_1.setFromValue(0);
          it_1.setToValue(1);
          it_1.setNode(this.getInnerDiagram());
        };
        FadeTransition _doubleArrow_4 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition_1, _function_6);
        _children_3.add(_doubleArrow_4);
      };
      _xblockexpression = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_3);
    }
    return _xblockexpression;
  }
  
  protected boolean closeDiagram() {
    boolean _xblockexpression = false;
    {
      if (this.isOpen) {
        CommandStack _commandStack = this.root.getCommandStack();
        CloseDiagramCommand _closeDiagramCommand = new CloseDiagramCommand(this);
        _commandStack.execute(_closeDiagramCommand);
      }
      _xblockexpression = this.isOpen = false;
    }
    return _xblockexpression;
  }
  
  protected SequentialTransition closeDiagram(final Duration duration) {
    SequentialTransition _xblockexpression = null;
    {
      final Function1<XNode, BoundingBox> _function = (XNode it) -> {
        return BoundsExtensions.translate(it.getLayoutBounds(), it.getLayoutX(), it.getLayoutY());
      };
      final Function2<BoundingBox, BoundingBox, BoundingBox> _function_1 = (BoundingBox b0, BoundingBox b1) -> {
        return BoundsExtensions.operator_plus(b0, b1);
      };
      final Point2D innerDiagramCenter = BoundsExtensions.center(IterableExtensions.<BoundingBox>reduce(ListExtensions.<XNode, BoundingBox>map(this.getInnerDiagram().getNodes(), _function), _function_1));
      final ViewportTransition phaseOne = new ViewportTransition(this.root, innerDiagramCenter, 1);
      ParallelTransition _parallelTransition = new ParallelTransition();
      final Procedure1<ParallelTransition> _function_2 = (ParallelTransition it) -> {
        ObservableList<Animation> _children = it.getChildren();
        FadeTransition _fadeTransition = new FadeTransition();
        final Procedure1<FadeTransition> _function_3 = (FadeTransition it_1) -> {
          Duration _multiply = DurationExtensions.operator_multiply(this.delayFactor, duration);
          it_1.setDelay(_multiply);
          Duration _multiply_1 = DurationExtensions.operator_multiply((1 - this.delayFactor), duration);
          it_1.setDuration(_multiply_1);
          it_1.setFromValue(0);
          it_1.setToValue(1);
          it_1.setNode(this.getTextNode());
        };
        FadeTransition _doubleArrow = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function_3);
        _children.add(_doubleArrow);
        ObservableList<Animation> _children_1 = it.getChildren();
        FadeTransition _fadeTransition_1 = new FadeTransition();
        final Procedure1<FadeTransition> _function_4 = (FadeTransition it_1) -> {
          Duration _multiply = DurationExtensions.operator_multiply((1 - this.delayFactor), duration);
          it_1.setDuration(_multiply);
          it_1.setFromValue(1);
          it_1.setToValue(0);
          it_1.setNode(this.getInnerDiagram());
        };
        FadeTransition _doubleArrow_1 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition_1, _function_4);
        _children_1.add(_doubleArrow_1);
      };
      final ParallelTransition phaseTwo = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_2);
      SequentialTransition _sequentialTransition = new SequentialTransition();
      final Procedure1<SequentialTransition> _function_3 = (SequentialTransition it) -> {
        ObservableList<Animation> _children = it.getChildren();
        final Procedure1<ViewportTransition> _function_4 = (ViewportTransition it_1) -> {
          Duration _multiply = DurationExtensions.operator_multiply(duration, 0.3);
          it_1.setDuration(_multiply);
          final EventHandler<ActionEvent> _function_5 = (ActionEvent it_2) -> {
            this.root.setDiagram(this.getParentDiagram());
            ObservableList<Node> _children_1 = this.pane.getChildren();
            Group _group = new Group();
            final Procedure1<Group> _function_6 = (Group it_3) -> {
              ObservableList<Node> _children_2 = it_3.getChildren();
              XDiagram _innerDiagram = this.getInnerDiagram();
              _children_2.add(_innerDiagram);
            };
            Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_6);
            _children_1.add(_doubleArrow);
            this.diagramScaler.activate();
            this.layout();
            ObservableList<Animation> _children_2 = phaseTwo.getChildren();
            ViewportTransition _viewportTransition = new ViewportTransition(this.root, this.viewportBeforeOpen, duration);
            final Procedure1<ViewportTransition> _function_7 = (ViewportTransition it_3) -> {
              final EventHandler<ActionEvent> _function_8 = (ActionEvent it_4) -> {
                this.diagramScaler.deactivate();
                this.setParentDiagram(this.root.getDiagram());
                this.pane.getChildren().setAll(this.getTextNode());
              };
              it_3.setOnFinished(_function_8);
            };
            ViewportTransition _doubleArrow_1 = ObjectExtensions.<ViewportTransition>operator_doubleArrow(_viewportTransition, _function_7);
            _children_2.add(0, _doubleArrow_1);
          };
          it_1.setOnFinished(_function_5);
        };
        ViewportTransition _doubleArrow = ObjectExtensions.<ViewportTransition>operator_doubleArrow(phaseOne, _function_4);
        _children.add(_doubleArrow);
        ObservableList<Animation> _children_1 = it.getChildren();
        _children_1.add(phaseTwo);
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
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
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
  
  @Pure
  public RectangleBorderPane getPane() {
    return this.pane;
  }
  
  @Pure
  public Node getTextNode() {
    return this.textNode;
  }
}
