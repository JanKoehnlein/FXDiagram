package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.MyNode;
import de.itemis.javafx.diagram.XConnection;
import de.itemis.javafx.diagram.XDiagram;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.behavior.AbstractBehavior;
import de.itemis.javafx.diagram.behavior.Placer;
import de.itemis.javafx.diagram.behavior.RapidButton;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddRapidButtonBehavior extends AbstractBehavior {
  private List<RapidButton> rapidButtons;
  
  public AddRapidButtonBehavior(final XNode host) {
    super(host);
  }
  
  public void activate() {
    final Procedure1<RapidButton> _function = new Procedure1<RapidButton>() {
        public void apply(final RapidButton button) {
          MyNode _myNode = new MyNode("new");
          final MyNode target = _myNode;
          final XNode source = button.getHost();
          XConnection _xConnection = new XConnection(source, target);
          final XConnection connection = _xConnection;
          XDiagram _diagram = source.getDiagram();
          _diagram.addNode(target);
          XDiagram _diagram_1 = source.getDiagram();
          _diagram_1.addConnection(connection);
          double _translateX = source.getTranslateX();
          String _plus = (Double.valueOf(_translateX) + ", ");
          double _translateY = source.getTranslateY();
          String _plus_1 = (_plus + Double.valueOf(_translateY));
          InputOutput.<String>println(_plus_1);
          Placer _placer = button.getPlacer();
          double _xPos = _placer.getXPos();
          String _plus_2 = (Double.valueOf(_xPos) + ", ");
          Placer _placer_1 = button.getPlacer();
          double _yPos = _placer_1.getYPos();
          String _plus_3 = (_plus_2 + Double.valueOf(_yPos));
          InputOutput.<String>println(_plus_3);
          Placer _placer_2 = button.getPlacer();
          double _xPos_1 = _placer_2.getXPos();
          double _minus = (_xPos_1 - 0.5);
          double _multiply = (200 * _minus);
          double _translateX_1 = source.getTranslateX();
          double _plus_4 = (_multiply + _translateX_1);
          target.setTranslateX(_plus_4);
          Placer _placer_3 = button.getPlacer();
          double _yPos_1 = _placer_3.getYPos();
          double _minus_1 = (_yPos_1 - 0.5);
          double _multiply_1 = (150 * _minus_1);
          double _translateY_1 = source.getTranslateY();
          double _plus_5 = (_multiply_1 + _translateY_1);
          target.setTranslateY(_plus_5);
          double _translateX_2 = target.getTranslateX();
          String _plus_6 = (Double.valueOf(_translateX_2) + ", ");
          double _translateY_2 = target.getTranslateY();
          String _plus_7 = (_plus_6 + Double.valueOf(_translateY_2));
          InputOutput.<String>println(_plus_7);
          return;
        }
      };
    final Procedure1<RapidButton> addAction = _function;
    XNode _host = this.getHost();
    RapidButton _rapidButton = new RapidButton(_host, 0.5, 0, "icons/add_16.png", addAction);
    XNode _host_1 = this.getHost();
    RapidButton _rapidButton_1 = new RapidButton(_host_1, 0.5, 1, "icons/add_16.png", addAction);
    XNode _host_2 = this.getHost();
    RapidButton _rapidButton_2 = new RapidButton(_host_2, 0, 0.5, "icons/add_16.png", addAction);
    XNode _host_3 = this.getHost();
    RapidButton _rapidButton_3 = new RapidButton(_host_3, 1, 0.5, "icons/add_16.png", addAction);
    ArrayList<RapidButton> _newArrayList = CollectionLiterals.<RapidButton>newArrayList(_rapidButton, _rapidButton_1, _rapidButton_2, _rapidButton_3);
    this.rapidButtons = _newArrayList;
    final Procedure1<RapidButton> _function_1 = new Procedure1<RapidButton>() {
        public void apply(final RapidButton it) {
          XNode _host = it.getHost();
          XDiagram _diagram = _host.getDiagram();
          _diagram.addButton(it);
        }
      };
    IterableExtensions.<RapidButton>forEach(this.rapidButtons, _function_1);
    XNode _host_4 = this.getHost();
    Node _node = _host_4.getNode();
    final Procedure1<MouseEvent> _function_2 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          final Procedure1<RapidButton> _function = new Procedure1<RapidButton>() {
              public void apply(final RapidButton it) {
                it.show();
              }
            };
          IterableExtensions.<RapidButton>forEach(AddRapidButtonBehavior.this.rapidButtons, _function);
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
          final Procedure1<RapidButton> _function = new Procedure1<RapidButton>() {
              public void apply(final RapidButton it) {
                it.fade();
              }
            };
          IterableExtensions.<RapidButton>forEach(AddRapidButtonBehavior.this.rapidButtons, _function);
        }
      };
    _node_1.setOnMouseExited(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_3.apply(arg0);
        }
    });
  }
}
