package de.fxdiagram.examples;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.tools.AbstractChooser;
import de.fxdiagram.lib.simple.AddRapidButtonBehavior;
import de.fxdiagram.lib.simple.LevelOfDetailDiagramNode;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import de.fxdiagram.lib.simple.SimpleNode;
import java.util.function.Consumer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode({ "nameSuffix" })
@SuppressWarnings("all")
public class LazyExampleDiagram extends XDiagram {
  public LazyExampleDiagram(final String nameSuffix) {
    this.setNameSuffix(nameSuffix);
  }
  
  public void doActivate() {
    ObservableList<XNode> _nodes = this.getNodes();
    boolean _isEmpty = _nodes.isEmpty();
    if (_isEmpty) {
      final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
        public void apply(final XDiagram it) {
          String _nameSuffix = LazyExampleDiagram.this.getNameSuffix();
          final SimpleNode simple = LazyExampleDiagram.this.newSimpleNode(_nameSuffix);
          String _nameSuffix_1 = LazyExampleDiagram.this.getNameSuffix();
          final OpenableDiagramNode openable = LazyExampleDiagram.this.newOpenableDiagramNode(_nameSuffix_1);
          String _nameSuffix_2 = LazyExampleDiagram.this.getNameSuffix();
          final LevelOfDetailDiagramNode levelOfDetail = LazyExampleDiagram.this.newEmbeddedDiagramNode(_nameSuffix_2);
          ObservableList<XNode> _nodes = it.getNodes();
          final Procedure1<SimpleNode> _function = new Procedure1<SimpleNode>() {
            public void apply(final SimpleNode it) {
              it.setLayoutX(75);
              it.setLayoutY(50);
            }
          };
          SimpleNode _doubleArrow = ObjectExtensions.<SimpleNode>operator_doubleArrow(simple, _function);
          _nodes.add(_doubleArrow);
          ObservableList<XNode> _nodes_1 = it.getNodes();
          final Procedure1<OpenableDiagramNode> _function_1 = new Procedure1<OpenableDiagramNode>() {
            public void apply(final OpenableDiagramNode it) {
              it.setLayoutX(350);
              it.setLayoutY(150);
            }
          };
          OpenableDiagramNode _doubleArrow_1 = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(openable, _function_1);
          _nodes_1.add(_doubleArrow_1);
          ObservableList<XNode> _nodes_2 = it.getNodes();
          final Procedure1<LevelOfDetailDiagramNode> _function_2 = new Procedure1<LevelOfDetailDiagramNode>() {
            public void apply(final LevelOfDetailDiagramNode it) {
              it.setLayoutX(50);
              it.setLayoutY(300);
            }
          };
          LevelOfDetailDiagramNode _doubleArrow_2 = ObjectExtensions.<LevelOfDetailDiagramNode>operator_doubleArrow(levelOfDetail, _function_2);
          _nodes_2.add(_doubleArrow_2);
          ObservableList<XConnection> _connections = it.getConnections();
          XConnection _xConnection = new XConnection(simple, openable);
          final Procedure1<XConnection> _function_3 = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
              XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
              final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
                public void apply(final XConnectionLabel it) {
                  Text _text = it.getText();
                  _text.setText("polyline");
                }
              };
              ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
            }
          };
          XConnection _doubleArrow_3 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function_3);
          _connections.add(_doubleArrow_3);
          ObservableList<XConnection> _connections_1 = it.getConnections();
          XConnection _xConnection_1 = new XConnection(openable, levelOfDetail);
          final Procedure1<XConnection> _function_4 = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
              it.setKind(XConnection.Kind.QUAD_CURVE);
              XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
              final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
                public void apply(final XConnectionLabel it) {
                  Text _text = it.getText();
                  _text.setText("quadratic");
                }
              };
              ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
            }
          };
          XConnection _doubleArrow_4 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection_1, _function_4);
          _connections_1.add(_doubleArrow_4);
          ObservableList<XConnection> _connections_2 = it.getConnections();
          XConnection _xConnection_2 = new XConnection(simple, levelOfDetail);
          final Procedure1<XConnection> _function_5 = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
              it.setKind(XConnection.Kind.CUBIC_CURVE);
              XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
              final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
                public void apply(final XConnectionLabel it) {
                  Text _text = it.getText();
                  _text.setText("cubic");
                }
              };
              ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
            }
          };
          XConnection _doubleArrow_5 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection_2, _function_5);
          _connections_2.add(_doubleArrow_5);
        }
      };
      this.setContentsInitializer(_function);
    } else {
      ObservableList<XNode> _nodes_1 = this.getNodes();
      final Consumer<XNode> _function_1 = new Consumer<XNode>() {
        public void accept(final XNode it) {
          boolean _or = false;
          String _nameSuffix = LazyExampleDiagram.this.getNameSuffix();
          boolean _isEmpty = _nameSuffix.isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            _or = true;
          } else {
            _or = (!(it.getNode() instanceof SimpleNode));
          }
          if (_or) {
            String _nameSuffix_1 = LazyExampleDiagram.this.getNameSuffix();
            LazyExampleDiagram.this.addRapidButtons(it, _nameSuffix_1);
          }
        }
      };
      _nodes_1.forEach(_function_1);
    }
    super.doActivate();
  }
  
  protected void addRapidButtons(final XNode node, final String nameSuffix) {
    AddRapidButtonBehavior<XNode> _addRapidButtonBehavior = new AddRapidButtonBehavior<XNode>(node);
    final Procedure1<AddRapidButtonBehavior<XNode>> _function = new Procedure1<AddRapidButtonBehavior<XNode>>() {
      public void apply(final AddRapidButtonBehavior<XNode> it) {
        final Procedure1<AbstractChooser> _function = new Procedure1<AbstractChooser>() {
          public void apply(final AbstractChooser it) {
            IntegerRange _upTo = new IntegerRange(5, 20);
            for (final Integer i : _upTo) {
              SimpleNode _newSimpleNode = LazyExampleDiagram.this.newSimpleNode((" " + i));
              it.addChoice(_newSimpleNode);
            }
            SimpleNode _newSimpleNode_1 = LazyExampleDiagram.this.newSimpleNode(nameSuffix);
            it.addChoice(_newSimpleNode_1);
            OpenableDiagramNode _newOpenableDiagramNode = LazyExampleDiagram.this.newOpenableDiagramNode(nameSuffix);
            it.addChoice(_newOpenableDiagramNode);
            LevelOfDetailDiagramNode _newEmbeddedDiagramNode = LazyExampleDiagram.this.newEmbeddedDiagramNode(nameSuffix);
            it.addChoice(_newEmbeddedDiagramNode);
            IntegerRange _upTo_1 = new IntegerRange(1, 4);
            for (final Integer i_1 : _upTo_1) {
              SimpleNode _newSimpleNode_2 = LazyExampleDiagram.this.newSimpleNode((" " + i_1));
              it.addChoice(_newSimpleNode_2);
            }
            SimpleNode _newSimpleNode_3 = LazyExampleDiagram.this.newSimpleNode(nameSuffix);
            it.addChoice(_newSimpleNode_3);
          }
        };
        it.setChoiceInitializer(_function);
      }
    };
    AddRapidButtonBehavior<XNode> _doubleArrow = ObjectExtensions.<AddRapidButtonBehavior<XNode>>operator_doubleArrow(_addRapidButtonBehavior, _function);
    node.addBehavior(_doubleArrow);
  }
  
  public SimpleNode newSimpleNode(final String nameSuffix) {
    SimpleNode _simpleNode = new SimpleNode(("Node" + nameSuffix));
    final Procedure1<SimpleNode> _function = new Procedure1<SimpleNode>() {
      public void apply(final SimpleNode it) {
        boolean _isEmpty = nameSuffix.isEmpty();
        boolean _not = (!_isEmpty);
        if (_not) {
          LazyExampleDiagram.this.addRapidButtons(it, nameSuffix);
        }
      }
    };
    return ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode, _function);
  }
  
  public OpenableDiagramNode newOpenableDiagramNode(final String nameSuffix) {
    OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode(("Nested" + nameSuffix));
    final Procedure1<OpenableDiagramNode> _function = new Procedure1<OpenableDiagramNode>() {
      public void apply(final OpenableDiagramNode it) {
        LazyExampleDiagram _lazyExampleDiagram = new LazyExampleDiagram((nameSuffix + " (n)"));
        it.setInnerDiagram(_lazyExampleDiagram);
        LazyExampleDiagram.this.addRapidButtons(it, nameSuffix);
      }
    };
    return ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
  }
  
  public LevelOfDetailDiagramNode newEmbeddedDiagramNode(final String nameSuffix) {
    LevelOfDetailDiagramNode _levelOfDetailDiagramNode = new LevelOfDetailDiagramNode(("Embedded" + nameSuffix));
    final Procedure1<LevelOfDetailDiagramNode> _function = new Procedure1<LevelOfDetailDiagramNode>() {
      public void apply(final LevelOfDetailDiagramNode it) {
        LazyExampleDiagram _lazyExampleDiagram = new LazyExampleDiagram((nameSuffix + " (e)"));
        it.setInnerDiagram(_lazyExampleDiagram);
        LazyExampleDiagram.this.addRapidButtons(it, nameSuffix);
      }
    };
    return ObjectExtensions.<LevelOfDetailDiagramNode>operator_doubleArrow(_levelOfDetailDiagramNode, _function);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public LazyExampleDiagram() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(nameSuffixProperty, String.class);
  }
  
  private SimpleStringProperty nameSuffixProperty = new SimpleStringProperty(this, "nameSuffix");
  
  public String getNameSuffix() {
    return this.nameSuffixProperty.get();
  }
  
  public void setNameSuffix(final String nameSuffix) {
    this.nameSuffixProperty.set(nameSuffix);
  }
  
  public StringProperty nameSuffixProperty() {
    return this.nameSuffixProperty;
  }
}
