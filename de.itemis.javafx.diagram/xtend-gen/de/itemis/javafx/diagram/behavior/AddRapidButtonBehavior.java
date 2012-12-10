package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.Connection;
import de.itemis.javafx.diagram.Diagram;
import de.itemis.javafx.diagram.MyNode;
import de.itemis.javafx.diagram.ShapeContainer;
import de.itemis.javafx.diagram.behavior.AbstractBehavior;
import de.itemis.javafx.diagram.behavior.RapidButton;
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
  private List<RapidButton> rapidButtons;
  
  public AddRapidButtonBehavior(final ShapeContainer host) {
    super(host);
  }
  
  public void activate(final Diagram diagram) {
    final Procedure1<ShapeContainer> _function = new Procedure1<ShapeContainer>() {
        public void apply(final ShapeContainer source) {
          MyNode _myNode = new MyNode("new");
          final MyNode target = _myNode;
          Connection _connection = new Connection(source, target);
          final Connection connection = _connection;
          diagram.addShape(target);
          diagram.addConnection(connection);
          return;
        }
      };
    final Procedure1<ShapeContainer> addAction = _function;
    ShapeContainer _host = this.getHost();
    RapidButton _rapidButton = new RapidButton(_host, 0.5, 0, "icons/add_16.png", addAction);
    ShapeContainer _host_1 = this.getHost();
    RapidButton _rapidButton_1 = new RapidButton(_host_1, 0.5, 1, "icons/add_16.png", addAction);
    ShapeContainer _host_2 = this.getHost();
    RapidButton _rapidButton_2 = new RapidButton(_host_2, 0, 0.5, "icons/add_16.png", addAction);
    ShapeContainer _host_3 = this.getHost();
    RapidButton _rapidButton_3 = new RapidButton(_host_3, 1, 0.5, "icons/add_16.png", addAction);
    ArrayList<RapidButton> _newArrayList = CollectionLiterals.<RapidButton>newArrayList(_rapidButton, _rapidButton_1, _rapidButton_2, _rapidButton_3);
    this.rapidButtons = _newArrayList;
    final Procedure1<RapidButton> _function_1 = new Procedure1<RapidButton>() {
        public void apply(final RapidButton it) {
          diagram.addButton(it);
        }
      };
    IterableExtensions.<RapidButton>forEach(this.rapidButtons, _function_1);
    ShapeContainer _host_4 = this.getHost();
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
    ShapeContainer _host_5 = this.getHost();
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
