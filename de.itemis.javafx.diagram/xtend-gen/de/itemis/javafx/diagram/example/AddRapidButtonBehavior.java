package de.itemis.javafx.diagram.example;

import de.itemis.javafx.diagram.Extensions;
import de.itemis.javafx.diagram.Placer;
import de.itemis.javafx.diagram.XAbstractDiagram;
import de.itemis.javafx.diagram.XConnection;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.XRapidButton;
import de.itemis.javafx.diagram.behavior.AbstractBehavior;
import de.itemis.javafx.diagram.example.MyContainerNode;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddRapidButtonBehavior extends AbstractBehavior {
  private List<XRapidButton> rapidButtons;
  
  public AddRapidButtonBehavior(final XNode host) {
    super(host);
  }
  
  public void doActivate() {
    final Procedure1<XRapidButton> _function = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          MyContainerNode _myContainerNode = new MyContainerNode("new");
          final MyContainerNode target = _myContainerNode;
          final XNode source = button.getHost();
          XConnection _xConnection = new XConnection(source, target);
          final XConnection connection = _xConnection;
          XNode _host = AddRapidButtonBehavior.this.getHost();
          XAbstractDiagram _diagram = Extensions.getDiagram(_host);
          _diagram.addNode(target);
          XNode _host_1 = AddRapidButtonBehavior.this.getHost();
          XAbstractDiagram _diagram_1 = Extensions.getDiagram(_host_1);
          _diagram_1.addConnection(connection);
          Placer _placer = button.getPlacer();
          double _xPos = _placer.getXPos();
          double _minus = (_xPos - 0.5);
          double _multiply = (200 * _minus);
          double _layoutX = source.getLayoutX();
          double _plus = (_multiply + _layoutX);
          target.setLayoutX(_plus);
          Placer _placer_1 = button.getPlacer();
          double _yPos = _placer_1.getYPos();
          double _minus_1 = (_yPos - 0.5);
          double _multiply_1 = (150 * _minus_1);
          double _layoutY = source.getLayoutY();
          double _plus_1 = (_multiply_1 + _layoutY);
          target.setLayoutY(_plus_1);
          return;
        }
      };
    final Procedure1<XRapidButton> addAction = _function;
    XNode _host = this.getHost();
    XRapidButton _xRapidButton = new XRapidButton(_host, 0.5, 0, "icons/add_16.png", addAction);
    XNode _host_1 = this.getHost();
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 0.5, 1, "icons/add_16.png", addAction);
    XNode _host_2 = this.getHost();
    XRapidButton _xRapidButton_2 = new XRapidButton(_host_2, 0, 0.5, "icons/add_16.png", addAction);
    XNode _host_3 = this.getHost();
    XRapidButton _xRapidButton_3 = new XRapidButton(_host_3, 1, 0.5, "icons/add_16.png", addAction);
    ArrayList<XRapidButton> _newArrayList = CollectionLiterals.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1, _xRapidButton_2, _xRapidButton_3);
    this.rapidButtons = _newArrayList;
    final Procedure1<XRapidButton> _function_1 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton it) {
          XNode _host = it.getHost();
          XAbstractDiagram _diagram = Extensions.getDiagram(_host);
          _diagram.addButton(it);
        }
      };
    IterableExtensions.<XRapidButton>forEach(this.rapidButtons, _function_1);
    XNode _host_4 = this.getHost();
    Node _node = _host_4.getNode();
    final Procedure1<MouseEvent> _function_2 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          final Procedure1<XRapidButton> _function = new Procedure1<XRapidButton>() {
              public void apply(final XRapidButton it) {
                it.show();
              }
            };
          IterableExtensions.<XRapidButton>forEach(AddRapidButtonBehavior.this.rapidButtons, _function);
        }
      };
    _node.setOnMouseEntered(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_2.apply(arg0);
        }
    });
    XNode _host_5 = this.getHost();
    Node _node_1 = _host_5.getNode();
    final Procedure1<MouseEvent> _function_3 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          final Procedure1<XRapidButton> _function = new Procedure1<XRapidButton>() {
              public void apply(final XRapidButton it) {
                it.fade();
              }
            };
          IterableExtensions.<XRapidButton>forEach(AddRapidButtonBehavior.this.rapidButtons, _function);
        }
      };
    _node_1.setOnMouseExited(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_3.apply(arg0);
        }
    });
  }
}
