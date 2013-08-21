package de.fxdiagram.lib.simple;

import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.lib.simple.AddRapidButtonBehavior;
import de.fxdiagram.lib.simple.DiagramScaler;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LevelOfDetailDiagramNode extends XNode {
  private String name;
  
  private RectangleBorderPane pane;
  
  private Node label;
  
  private Group innerDiagramGroup;
  
  private XDiagram innerDiagram;
  
  private DiagramScaler diagramScaler;
  
  public LevelOfDetailDiagramNode(final String name, final XDiagram innerDiagram) {
    this.name = name;
    this.innerDiagram = innerDiagram;
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    this.pane = _rectangleBorderPane;
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
        Node _label = LevelOfDetailDiagramNode.this.label = _doubleArrow;
        _children.add(_label);
        ObservableList<Node> _children_1 = it.getChildren();
        Group _group = new Group();
        final Procedure1<Group> _function_1 = new Procedure1<Group>() {
          public void apply(final Group it) {
            ObservableList<Node> _children = it.getChildren();
            _children.add(innerDiagram);
            DiagramScaler _diagramScaler = new DiagramScaler(innerDiagram);
            LevelOfDetailDiagramNode.this.diagramScaler = _diagramScaler;
          }
        };
        Group _doubleArrow_1 = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_1);
        Group _innerDiagramGroup = LevelOfDetailDiagramNode.this.innerDiagramGroup = _doubleArrow_1;
        _children_1.add(_innerDiagramGroup);
      }
    };
    RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(this.pane, _function);
    this.setNode(_doubleArrow);
    this.setKey(name);
  }
  
  protected Anchors createAnchors() {
    RoundedRectangleAnchors _roundedRectangleAnchors = new RoundedRectangleAnchors(this, 12, 12);
    return _roundedRectangleAnchors;
  }
  
  public void doActivate() {
    super.doActivate();
    XRoot _root = Extensions.getRoot(this);
    DoubleProperty _diagramScaleProperty = _root.diagramScaleProperty();
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
      public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
        Bounds _boundsInLocal = LevelOfDetailDiagramNode.this.getBoundsInLocal();
        final Bounds bounds = LevelOfDetailDiagramNode.this.localToScene(_boundsInLocal);
        double _width = bounds.getWidth();
        double _height = bounds.getHeight();
        final double area = (_width * _height);
        boolean _lessEqualsThan = (area <= 100000);
        if (_lessEqualsThan) {
          LevelOfDetailDiagramNode.this.label.setVisible(true);
          LevelOfDetailDiagramNode.this.innerDiagramGroup.setVisible(false);
          LevelOfDetailDiagramNode.this.pane.setBackgroundPaint(RectangleBorderPane.DEFAULT_BACKGROUND);
        } else {
          LevelOfDetailDiagramNode.this.label.setVisible(false);
          LevelOfDetailDiagramNode.this.innerDiagramGroup.setVisible(true);
          LevelOfDetailDiagramNode.this.innerDiagram.activate();
          final Procedure1<DiagramScaler> _function = new Procedure1<DiagramScaler>() {
            public void apply(final DiagramScaler it) {
              Bounds _layoutBounds = LevelOfDetailDiagramNode.this.label.getLayoutBounds();
              double _width = _layoutBounds.getWidth();
              double _plus = (_width + 40);
              it.setWidth(_plus);
              Bounds _layoutBounds_1 = LevelOfDetailDiagramNode.this.label.getLayoutBounds();
              double _height = _layoutBounds_1.getHeight();
              double _plus_1 = (_height + 20);
              it.setHeight(_plus_1);
              it.activate();
            }
          };
          ObjectExtensions.<DiagramScaler>operator_doubleArrow(
            LevelOfDetailDiagramNode.this.diagramScaler, _function);
          Paint _backgroundPaint = LevelOfDetailDiagramNode.this.innerDiagram.getBackgroundPaint();
          LevelOfDetailDiagramNode.this.pane.setBackgroundPaint(_backgroundPaint);
        }
      }
    };
    _diagramScaleProperty.addListener(_function);
    AddRapidButtonBehavior<LevelOfDetailDiagramNode> _addRapidButtonBehavior = new AddRapidButtonBehavior<LevelOfDetailDiagramNode>(this);
    final AddRapidButtonBehavior<LevelOfDetailDiagramNode> rapidButtonBehavior = _addRapidButtonBehavior;
    rapidButtonBehavior.activate();
  }
}
