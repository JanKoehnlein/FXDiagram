package de.fxdiagram.lib.simple;

import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XNestedDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.lib.simple.AddRapidButtonBehavior;
import de.fxdiagram.lib.simple.SimpleNode;
import java.util.ArrayList;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class NestedDiagramNode extends XNode {
  private String name;
  
  private Pane pane;
  
  private Node label;
  
  private XNestedDiagram innerDiagram;
  
  private static int nr = 0;
  
  public final static Procedure1<? super XAbstractDiagram> dummyDiagramContent = new Function0<Procedure1<? super XAbstractDiagram>>() {
    public Procedure1<? super XAbstractDiagram> apply() {
      final Procedure1<XAbstractDiagram> _function = new Procedure1<XAbstractDiagram>() {
          public void apply(final XAbstractDiagram it) {
            ObservableList<XNode> _nodes = it.getNodes();
            String _plus = ("Inner " + Integer.valueOf(NestedDiagramNode.nr));
            SimpleNode _simpleNode = new SimpleNode(_plus);
            final Procedure1<SimpleNode> _function = new Procedure1<SimpleNode>() {
                public void apply(final SimpleNode it) {
                  it.relocate(0, 0);
                }
              };
            SimpleNode _doubleArrow = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode, _function);
            _nodes.add(_doubleArrow);
            ObservableList<XNode> _nodes_1 = it.getNodes();
            String _plus_1 = ("Inner " + Integer.valueOf(NestedDiagramNode.nr));
            String _plus_2 = (_plus_1 + Integer.valueOf(1));
            SimpleNode _simpleNode_1 = new SimpleNode(_plus_2);
            final Procedure1<SimpleNode> _function_1 = new Procedure1<SimpleNode>() {
                public void apply(final SimpleNode it) {
                  it.relocate(100, 100);
                }
              };
            SimpleNode _doubleArrow_1 = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode_1, _function_1);
            _nodes_1.add(_doubleArrow_1);
            ObservableList<XNode> _nodes_2 = it.getNodes();
            String _plus_3 = ("Nested " + Integer.valueOf(NestedDiagramNode.nr));
            String _plus_4 = (_plus_3 + Integer.valueOf(2));
            NestedDiagramNode _nestedDiagramNode = new NestedDiagramNode(_plus_4);
            final Procedure1<NestedDiagramNode> _function_2 = new Procedure1<NestedDiagramNode>() {
                public void apply(final NestedDiagramNode it) {
                  it.relocate(50, 50);
                }
              };
            NestedDiagramNode _doubleArrow_2 = ObjectExtensions.<NestedDiagramNode>operator_doubleArrow(_nestedDiagramNode, _function_2);
            _nodes_2.add(_doubleArrow_2);
            int _plus_5 = (NestedDiagramNode.nr + 3);
            NestedDiagramNode.nr = _plus_5;
          }
        };
      return _function;
    }
  }.apply();
  
  public NestedDiagramNode(final String name) {
    this(name, NestedDiagramNode.dummyDiagramContent);
  }
  
  public NestedDiagramNode(final String name, final Procedure1<? super XNestedDiagram> diagramContents) {
    this.name = name;
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    this.pane = _rectangleBorderPane;
    final Procedure1<Pane> _function = new Procedure1<Pane>() {
        public void apply(final Pane it) {
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
          Node _label = NestedDiagramNode.this.label = _doubleArrow;
          _children.add(_label);
          ObservableList<Node> _children_1 = it.getChildren();
          Group _group = new Group();
          final Procedure1<Group> _function_1 = new Procedure1<Group>() {
              public void apply(final Group it) {
                ObservableList<Node> _children = it.getChildren();
                XNestedDiagram _xNestedDiagram = new XNestedDiagram();
                final Procedure1<XNestedDiagram> _function = new Procedure1<XNestedDiagram>() {
                    public void apply(final XNestedDiagram it) {
                      it.setStyle("-fx-background-color: white;");
                      final Procedure1<XNestedDiagram> _function = new Procedure1<XNestedDiagram>() {
                          public void apply(final XNestedDiagram it) {
                            ObjectExtensions.<XNestedDiagram>operator_doubleArrow(it, diagramContents);
                            Bounds _layoutBounds = NestedDiagramNode.this.label.getLayoutBounds();
                            double _width = _layoutBounds.getWidth();
                            double _plus = (_width + 40);
                            it.setWidth(_plus);
                            Bounds _layoutBounds_1 = NestedDiagramNode.this.label.getLayoutBounds();
                            double _height = _layoutBounds_1.getHeight();
                            double _plus_1 = (_height + 20);
                            it.setHeight(_plus_1);
                          }
                        };
                      it.setContentsInitializer(_function);
                    }
                  };
                XNestedDiagram _doubleArrow = ObjectExtensions.<XNestedDiagram>operator_doubleArrow(_xNestedDiagram, _function);
                XNestedDiagram _innerDiagram = NestedDiagramNode.this.innerDiagram = _doubleArrow;
                _children.add(_innerDiagram);
                Insets _insets = new Insets(3, 3, 3, 3);
                StackPane.setMargin(it, _insets);
              }
            };
          Group _doubleArrow_1 = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_1);
          _children_1.add(_doubleArrow_1);
        }
      };
    Pane _doubleArrow = ObjectExtensions.<Pane>operator_doubleArrow(this.pane, _function);
    this.setNode(_doubleArrow);
    this.setKey(name);
  }
  
  protected Anchors createAnchors() {
    RoundedRectangleAnchors _roundedRectangleAnchors = new RoundedRectangleAnchors(this, 12, 12);
    return _roundedRectangleAnchors;
  }
  
  public void doActivate() {
    super.doActivate();
    XAbstractDiagram _diagram = Extensions.getDiagram(this);
    ObservableList<XNestedDiagram> _subDiagrams = _diagram.getSubDiagrams();
    _subDiagrams.add(this.innerDiagram);
    XRootDiagram _rootDiagram = Extensions.getRootDiagram(this);
    ReadOnlyObjectProperty<Bounds> _boundsInParentProperty = _rootDiagram.boundsInParentProperty();
    final ChangeListener<Bounds> _function = new ChangeListener<Bounds>() {
        public void changed(final ObservableValue<? extends Bounds> prop, final Bounds oldVal, final Bounds newVal) {
          Bounds _boundsInLocal = NestedDiagramNode.this.getBoundsInLocal();
          final Bounds bounds = NestedDiagramNode.this.localToScene(_boundsInLocal);
          double _width = bounds.getWidth();
          double _height = bounds.getHeight();
          final double area = (_width * _height);
          boolean _lessEqualsThan = (area <= 100000);
          if (_lessEqualsThan) {
            NestedDiagramNode.this.label.setVisible(true);
            NestedDiagramNode.this.innerDiagram.setVisible(false);
          } else {
            NestedDiagramNode.this.label.setVisible(false);
            NestedDiagramNode.this.innerDiagram.setVisible(true);
            NestedDiagramNode.this.innerDiagram.activate();
          }
        }
      };
    _boundsInParentProperty.addListener(_function);
    AddRapidButtonBehavior _addRapidButtonBehavior = new AddRapidButtonBehavior(this);
    final AddRapidButtonBehavior rapidButtonBehavior = _addRapidButtonBehavior;
    rapidButtonBehavior.activate();
  }
  
  protected LinearGradient createFill() {
    LinearGradient _xblockexpression = null;
    {
      Color _gray = Color.gray(0.6);
      Stop _stop = new Stop(0, _gray);
      Color _gray_1 = Color.gray(0.9);
      Stop _stop_1 = new Stop(1, _gray_1);
      final ArrayList<Stop> stops = CollectionLiterals.<Stop>newArrayList(_stop, _stop_1);
      LinearGradient _linearGradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
      _xblockexpression = (_linearGradient);
    }
    return _xblockexpression;
  }
  
  public String toString() {
    return this.name;
  }
}
