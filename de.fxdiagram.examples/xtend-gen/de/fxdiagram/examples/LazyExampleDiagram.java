package de.fxdiagram.examples;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.lib.chooser.ConnectedNodeChooser;
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

@ModelNode("nameSuffix")
@SuppressWarnings("all")
public class LazyExampleDiagram extends XDiagram {
  public LazyExampleDiagram(final String nameSuffix) {
    this.setNameSuffix(nameSuffix);
  }
  
  @Override
  public void doActivate() {
    ObservableList<XNode> _nodes = this.getNodes();
    boolean _isEmpty = _nodes.isEmpty();
    if (_isEmpty) {
      final Procedure1<XDiagram> _function = (XDiagram it) -> {
        String _nameSuffix = this.getNameSuffix();
        final SimpleNode simple = this.newSimpleNode(_nameSuffix);
        String _nameSuffix_1 = this.getNameSuffix();
        final OpenableDiagramNode openable = this.newOpenableDiagramNode(_nameSuffix_1);
        String _nameSuffix_2 = this.getNameSuffix();
        final LevelOfDetailDiagramNode levelOfDetail = this.newEmbeddedDiagramNode(_nameSuffix_2);
        String _nameSuffix_3 = this.getNameSuffix();
        final SimpleNode other = this.newSimpleNode(_nameSuffix_3);
        ObservableList<XNode> _nodes_1 = it.getNodes();
        final Procedure1<SimpleNode> _function_1 = (SimpleNode it_1) -> {
          it_1.setLayoutX((-150));
          it_1.setLayoutY((-150));
        };
        SimpleNode _doubleArrow = ObjectExtensions.<SimpleNode>operator_doubleArrow(simple, _function_1);
        _nodes_1.add(_doubleArrow);
        ObservableList<XNode> _nodes_2 = it.getNodes();
        final Procedure1<OpenableDiagramNode> _function_2 = (OpenableDiagramNode it_1) -> {
          it_1.setLayoutX(150);
          it_1.setLayoutY((-150));
        };
        OpenableDiagramNode _doubleArrow_1 = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(openable, _function_2);
        _nodes_2.add(_doubleArrow_1);
        ObservableList<XNode> _nodes_3 = it.getNodes();
        final Procedure1<SimpleNode> _function_3 = (SimpleNode it_1) -> {
          it_1.setLayoutX((-150));
          it_1.setLayoutY(150);
        };
        SimpleNode _doubleArrow_2 = ObjectExtensions.<SimpleNode>operator_doubleArrow(other, _function_3);
        _nodes_3.add(_doubleArrow_2);
        ObservableList<XNode> _nodes_4 = it.getNodes();
        final Procedure1<LevelOfDetailDiagramNode> _function_4 = (LevelOfDetailDiagramNode it_1) -> {
          it_1.setLayoutX(150);
          it_1.setLayoutY(150);
        };
        LevelOfDetailDiagramNode _doubleArrow_3 = ObjectExtensions.<LevelOfDetailDiagramNode>operator_doubleArrow(levelOfDetail, _function_4);
        _nodes_4.add(_doubleArrow_3);
        ObservableList<XConnection> _connections = it.getConnections();
        XConnection _xConnection = new XConnection(simple, openable);
        final Procedure1<XConnection> _function_5 = (XConnection it_1) -> {
          XConnectionLabel _xConnectionLabel = new XConnectionLabel(it_1);
          final Procedure1<XConnectionLabel> _function_6 = (XConnectionLabel it_2) -> {
            Text _text = it_2.getText();
            _text.setText("polyline");
          };
          ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function_6);
        };
        XConnection _doubleArrow_4 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function_5);
        _connections.add(_doubleArrow_4);
        ObservableList<XConnection> _connections_1 = it.getConnections();
        XConnection _xConnection_1 = new XConnection(openable, levelOfDetail);
        final Procedure1<XConnection> _function_6 = (XConnection it_1) -> {
          it_1.setKind(XConnection.Kind.QUAD_CURVE);
          XConnectionLabel _xConnectionLabel = new XConnectionLabel(it_1);
          final Procedure1<XConnectionLabel> _function_7 = (XConnectionLabel it_2) -> {
            Text _text = it_2.getText();
            _text.setText("quadratic");
          };
          ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function_7);
        };
        XConnection _doubleArrow_5 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection_1, _function_6);
        _connections_1.add(_doubleArrow_5);
        ObservableList<XConnection> _connections_2 = it.getConnections();
        XConnection _xConnection_2 = new XConnection(levelOfDetail, other);
        final Procedure1<XConnection> _function_7 = (XConnection it_1) -> {
          it_1.setKind(XConnection.Kind.CUBIC_CURVE);
          XConnectionLabel _xConnectionLabel = new XConnectionLabel(it_1);
          final Procedure1<XConnectionLabel> _function_8 = (XConnectionLabel it_2) -> {
            Text _text = it_2.getText();
            _text.setText("cubic");
          };
          ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function_8);
        };
        XConnection _doubleArrow_6 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection_2, _function_7);
        _connections_2.add(_doubleArrow_6);
        ObservableList<XConnection> _connections_3 = it.getConnections();
        XConnection _xConnection_3 = new XConnection(other, simple);
        final Procedure1<XConnection> _function_8 = (XConnection it_1) -> {
          it_1.setKind(XConnection.Kind.RECTILINEAR);
          XConnectionLabel _xConnectionLabel = new XConnectionLabel(it_1);
          final Procedure1<XConnectionLabel> _function_9 = (XConnectionLabel it_2) -> {
            Text _text = it_2.getText();
            _text.setText("rectilinear");
          };
          ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function_9);
        };
        XConnection _doubleArrow_7 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection_3, _function_8);
        _connections_3.add(_doubleArrow_7);
      };
      this.setContentsInitializer(_function);
    } else {
      ObservableList<XNode> _nodes_1 = this.getNodes();
      final Consumer<XNode> _function_1 = (XNode it) -> {
        if (((!this.getNameSuffix().isEmpty()) || (!(it.getNode() instanceof SimpleNode)))) {
          String _nameSuffix = this.getNameSuffix();
          this.addRapidButtons(it, _nameSuffix);
        }
      };
      _nodes_1.forEach(_function_1);
    }
    super.doActivate();
  }
  
  protected void addRapidButtons(final XNode node, final String nameSuffix) {
    AddRapidButtonBehavior<XNode> _addRapidButtonBehavior = new AddRapidButtonBehavior<XNode>(node);
    final Procedure1<AddRapidButtonBehavior<XNode>> _function = (AddRapidButtonBehavior<XNode> it) -> {
      final Procedure1<ConnectedNodeChooser> _function_1 = (ConnectedNodeChooser it_1) -> {
        IntegerRange _upTo = new IntegerRange(5, 20);
        for (final Integer i : _upTo) {
          SimpleNode _newSimpleNode = this.newSimpleNode((" " + i));
          it_1.addChoice(_newSimpleNode);
        }
        SimpleNode _newSimpleNode_1 = this.newSimpleNode(nameSuffix);
        it_1.addChoice(_newSimpleNode_1);
        OpenableDiagramNode _newOpenableDiagramNode = this.newOpenableDiagramNode(nameSuffix);
        it_1.addChoice(_newOpenableDiagramNode);
        LevelOfDetailDiagramNode _newEmbeddedDiagramNode = this.newEmbeddedDiagramNode(nameSuffix);
        it_1.addChoice(_newEmbeddedDiagramNode);
        IntegerRange _upTo_1 = new IntegerRange(1, 4);
        for (final Integer i_1 : _upTo_1) {
          SimpleNode _newSimpleNode_2 = this.newSimpleNode((" " + i_1));
          it_1.addChoice(_newSimpleNode_2);
        }
        SimpleNode _newSimpleNode_3 = this.newSimpleNode(nameSuffix);
        it_1.addChoice(_newSimpleNode_3);
      };
      it.setChoiceInitializer(_function_1);
    };
    AddRapidButtonBehavior<XNode> _doubleArrow = ObjectExtensions.<AddRapidButtonBehavior<XNode>>operator_doubleArrow(_addRapidButtonBehavior, _function);
    node.addBehavior(_doubleArrow);
  }
  
  public SimpleNode newSimpleNode(final String nameSuffix) {
    SimpleNode _simpleNode = new SimpleNode(("Node" + nameSuffix));
    final Procedure1<SimpleNode> _function = (SimpleNode it) -> {
      boolean _isEmpty = nameSuffix.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        this.addRapidButtons(it, nameSuffix);
      }
    };
    return ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode, _function);
  }
  
  public OpenableDiagramNode newOpenableDiagramNode(final String nameSuffix) {
    OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode(("Nested" + nameSuffix));
    final Procedure1<OpenableDiagramNode> _function = (OpenableDiagramNode it) -> {
      LazyExampleDiagram _lazyExampleDiagram = new LazyExampleDiagram((nameSuffix + " (n)"));
      it.setInnerDiagram(_lazyExampleDiagram);
      this.addRapidButtons(it, nameSuffix);
    };
    return ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
  }
  
  public LevelOfDetailDiagramNode newEmbeddedDiagramNode(final String nameSuffix) {
    LevelOfDetailDiagramNode _levelOfDetailDiagramNode = new LevelOfDetailDiagramNode(("Embedded" + nameSuffix));
    final Procedure1<LevelOfDetailDiagramNode> _function = (LevelOfDetailDiagramNode it) -> {
      LazyExampleDiagram _lazyExampleDiagram = new LazyExampleDiagram((nameSuffix + " (e)"));
      it.setInnerDiagram(_lazyExampleDiagram);
      this.addRapidButtons(it, nameSuffix);
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
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
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
