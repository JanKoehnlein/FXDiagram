package de.fxdiagram.lib.simple;

import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import eu.hansolo.enzo.radialmenu.Symbol.Type;
import eu.hansolo.enzo.radialmenu.SymbolCanvas;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class OpenableDiagramNode extends XNode {
  private XDiagram nestedDiagram;
  
  private XDiagram parentDiagram;
  
  private XRoot root;
  
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
        _children.add(_doubleArrow);
      }
    };
    RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
    this.setNode(_doubleArrow);
    this.setKey(name);
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
          XDiagram _diagram = OpenableDiagramNode.this.root.getDiagram();
          OpenableDiagramNode.this.parentDiagram = _diagram;
          OpenableDiagramNode.this.root.setDiagram(OpenableDiagramNode.this.nestedDiagram);
          HeadsUpDisplay _headsUpDisplay = OpenableDiagramNode.this.root.getHeadsUpDisplay();
          Canvas _symbol = SymbolCanvas.getSymbol(Type.ZOOM_OUT, 32, Color.GRAY);
          final Procedure1<Canvas> _function = new Procedure1<Canvas>() {
            public void apply(final Canvas it) {
              final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
                public void handle(final MouseEvent it) {
                  OpenableDiagramNode.this.root.setDiagram(OpenableDiagramNode.this.parentDiagram);
                  HeadsUpDisplay _headsUpDisplay = OpenableDiagramNode.this.root.getHeadsUpDisplay();
                  ObservableList<Node> _children = _headsUpDisplay.getChildren();
                  EventTarget _target = it.getTarget();
                  _children.remove(((Node) _target));
                }
              };
              it.setOnMouseClicked(_function);
            }
          };
          Canvas _doubleArrow = ObjectExtensions.<Canvas>operator_doubleArrow(_symbol, _function);
          _headsUpDisplay.add(_doubleArrow, Pos.TOP_RIGHT);
        }
      }
    };
    _node.setOnMouseClicked(_function);
  }
}
