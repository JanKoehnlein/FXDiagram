package de.fxdiagram.examples;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionKind;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.lib.simple.LevelOfDetailDiagramNode;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import de.fxdiagram.lib.simple.SimpleNode;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Main extends Application {
  public static void main(final String... args) {
    Application.launch(args);
  }
  
  private int node_nr = 0;
  
  public void start(final Stage it) {
    it.setTitle("FX Diagram Demo");
    Scene _createScene = this.createScene();
    it.setScene(_createScene);
    it.show();
  }
  
  public Scene createScene() {
    Scene _xblockexpression = null;
    {
      XRoot _xRoot = new XRoot();
      final XRoot root = _xRoot;
      Scene _scene = new Scene(root, 1024, 768);
      final Scene scene = _scene;
      PerspectiveCamera _perspectiveCamera = new PerspectiveCamera();
      scene.setCamera(_perspectiveCamera);
      root.activate();
      XDiagram _xDiagram = new XDiagram();
      final XDiagram diagram = _xDiagram;
      root.setDiagram(diagram);
      final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
        public void apply(final XDiagram it) {
          ObservableList<XNode> _nodes = it.getNodes();
          OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode("Basic");
          final Procedure1<OpenableDiagramNode> _function = new Procedure1<OpenableDiagramNode>() {
            public void apply(final OpenableDiagramNode it) {
              XDiagram _createBasicDiagram = Main.this.createBasicDiagram("");
              it.setInnerDiagram(_createBasicDiagram);
            }
          };
          OpenableDiagramNode _doubleArrow = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
          _nodes.add(_doubleArrow);
        }
      };
      ObjectExtensions.<XDiagram>operator_doubleArrow(diagram, _function);
      this.warmUpLayouter();
      root.centerDiagram();
      _xblockexpression = (scene);
    }
    return _xblockexpression;
  }
  
  public XDiagram createBasicDiagram(final String nameSuffix) {
    XDiagram _xDiagram = new XDiagram();
    final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
      public void apply(final XDiagram it) {
        final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
          public void apply(final XDiagram it) {
            String _plus = ("Linked" + nameSuffix);
            OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode(_plus);
            final OpenableDiagramNode openable = _openableDiagramNode;
            String _plus_1 = ("Embedded" + nameSuffix);
            LevelOfDetailDiagramNode _levelOfDetailDiagramNode = new LevelOfDetailDiagramNode(_plus_1);
            final LevelOfDetailDiagramNode levelOfDetail = _levelOfDetailDiagramNode;
            String _plus_2 = ("Simple" + nameSuffix);
            SimpleNode _simpleNode = new SimpleNode(_plus_2);
            final SimpleNode simple = _simpleNode;
            ObservableList<XNode> _nodes = it.getNodes();
            final Procedure1<SimpleNode> _function = new Procedure1<SimpleNode>() {
              public void apply(final SimpleNode it) {
                it.setLayoutX(50);
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
                String _plus = (nameSuffix + " (linked)");
                XDiagram _createBasicDiagram = Main.this.createBasicDiagram(_plus);
                it.setInnerDiagram(_createBasicDiagram);
              }
            };
            OpenableDiagramNode _doubleArrow_1 = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(openable, _function_1);
            _nodes_1.add(_doubleArrow_1);
            ObservableList<XNode> _nodes_2 = it.getNodes();
            final Procedure1<LevelOfDetailDiagramNode> _function_2 = new Procedure1<LevelOfDetailDiagramNode>() {
              public void apply(final LevelOfDetailDiagramNode it) {
                it.setLayoutX(50);
                it.setLayoutY(300);
                String _plus = (nameSuffix + " (embedded)");
                XDiagram _createBasicDiagram = Main.this.createBasicDiagram(_plus);
                it.setInnerDiagram(_createBasicDiagram);
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
                it.setKind(XConnectionKind.QUAD_CURVE);
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
                it.setKind(XConnectionKind.CUBIC_CURVE);
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
        it.setContentsInitializer(_function);
      }
    };
    XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function);
    return _doubleArrow;
  }
  
  protected void warmUpLayouter() {
    final Task<Void> _function = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        Void _xblockexpression = null;
        {
          new Layouter();
          _xblockexpression = (null);
        }
        return _xblockexpression;
      }
    };
    final Task<Void> task = _function;
    task.run();
  }
  
  protected XDiagram createDummyDiagram() {
    XDiagram _xDiagram = new XDiagram();
    final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
      public void apply(final XDiagram it) {
        final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
          public void apply(final XDiagram it) {
            ObservableList<XNode> _nodes = it.getNodes();
            String _plus = ("Inner " + Integer.valueOf(Main.this.node_nr));
            SimpleNode _simpleNode = new SimpleNode(_plus);
            final Procedure1<SimpleNode> _function = new Procedure1<SimpleNode>() {
              public void apply(final SimpleNode it) {
                it.relocate(0, 0);
              }
            };
            SimpleNode _doubleArrow = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode, _function);
            _nodes.add(_doubleArrow);
            ObservableList<XNode> _nodes_1 = it.getNodes();
            String _plus_1 = ("Inner " + Integer.valueOf(Main.this.node_nr));
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
            String _plus_3 = ("Nested " + Integer.valueOf(Main.this.node_nr));
            String _plus_4 = (_plus_3 + Integer.valueOf(2));
            LevelOfDetailDiagramNode _levelOfDetailDiagramNode = new LevelOfDetailDiagramNode(_plus_4);
            final Procedure1<LevelOfDetailDiagramNode> _function_2 = new Procedure1<LevelOfDetailDiagramNode>() {
              public void apply(final LevelOfDetailDiagramNode it) {
                XDiagram _createDummyDiagram = Main.this.createDummyDiagram();
                it.setInnerDiagram(_createDummyDiagram);
                it.relocate(50, 50);
              }
            };
            LevelOfDetailDiagramNode _doubleArrow_2 = ObjectExtensions.<LevelOfDetailDiagramNode>operator_doubleArrow(_levelOfDetailDiagramNode, _function_2);
            _nodes_2.add(_doubleArrow_2);
          }
        };
        it.setContentsInitializer(_function);
        int _plus = (Main.this.node_nr + 3);
        Main.this.node_nr = _plus;
      }
    };
    XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function);
    return _doubleArrow;
  }
}
