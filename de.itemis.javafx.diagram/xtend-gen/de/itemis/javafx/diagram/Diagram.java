package de.itemis.javafx.diagram;

import com.google.common.base.Objects;
import de.itemis.javafx.diagram.Connection;
import de.itemis.javafx.diagram.ShapeContainer;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Diagram {
  private Group rootPane = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private Group nodeLayer = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private Group connectionLayer = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private List<ShapeContainer> shapes = new Function0<List<ShapeContainer>>() {
    public List<ShapeContainer> apply() {
      ArrayList<ShapeContainer> _newArrayList = CollectionLiterals.<ShapeContainer>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private List<Connection> connections = new Function0<List<Connection>>() {
    public List<Connection> apply() {
      ArrayList<Connection> _newArrayList = CollectionLiterals.<Connection>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  public Diagram() {
    ObservableList<Node> _children = this.rootPane.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.rootPane.getChildren();
    _children_1.add(this.connectionLayer);
  }
  
  public void addShape(final ShapeContainer shape) {
    ObservableList<Node> _children = this.nodeLayer.getChildren();
    _children.add(shape);
    this.shapes.add(shape);
    final Procedure1<MouseEvent> _function = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          final ShapeContainer targetShape = Diagram.this.getTargetShape(it);
          boolean _or = false;
          boolean _equals = Objects.equal(targetShape, null);
          if (_equals) {
            _or = true;
          } else {
            boolean _and = false;
            boolean _isSelected = targetShape.isSelected();
            boolean _not = (!_isSelected);
            if (!_not) {
              _and = false;
            } else {
              boolean _isControlDown = it.isControlDown();
              boolean _not_1 = (!_isControlDown);
              _and = (_not && _not_1);
            }
            _or = (_equals || _and);
          }
          if (_or) {
            final Function1<ShapeContainer,Boolean> _function = new Function1<ShapeContainer,Boolean>() {
                public Boolean apply(final ShapeContainer it) {
                  boolean _isSelected = it.isSelected();
                  return Boolean.valueOf(_isSelected);
                }
              };
            Iterable<ShapeContainer> _filter = IterableExtensions.<ShapeContainer>filter(Diagram.this.shapes, _function);
            final Procedure1<ShapeContainer> _function_1 = new Procedure1<ShapeContainer>() {
                public void apply(final ShapeContainer it) {
                  it.setSelected(false);
                }
              };
            IterableExtensions.<ShapeContainer>forEach(_filter, _function_1);
          }
          final Function1<ShapeContainer,Boolean> _function_2 = new Function1<ShapeContainer,Boolean>() {
              public Boolean apply(final ShapeContainer it) {
                boolean _isSelected = it.isSelected();
                return Boolean.valueOf(_isSelected);
              }
            };
          Iterable<ShapeContainer> _filter_1 = IterableExtensions.<ShapeContainer>filter(Diagram.this.shapes, _function_2);
          for (final ShapeContainer s : _filter_1) {
            s.mousePressed(it);
          }
        }
      };
    this.rootPane.<MouseEvent>addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function.apply(arg0);
        }
    });
    final Procedure1<MouseEvent> _function_1 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          final Function1<ShapeContainer,Boolean> _function = new Function1<ShapeContainer,Boolean>() {
              public Boolean apply(final ShapeContainer it) {
                boolean _isSelected = it.isSelected();
                return Boolean.valueOf(_isSelected);
              }
            };
          Iterable<ShapeContainer> _filter = IterableExtensions.<ShapeContainer>filter(Diagram.this.shapes, _function);
          for (final ShapeContainer s : _filter) {
            s.mouseDragged(it);
          }
        }
      };
    this.rootPane.<MouseEvent>addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_1.apply(arg0);
        }
    });
  }
  
  protected ShapeContainer getTargetShape(final MouseEvent event) {
    ShapeContainer _xifexpression = null;
    EventTarget _target = event.getTarget();
    if ((_target instanceof Node)) {
      EventTarget _target_1 = event.getTarget();
      ShapeContainer _containerShape = this.getContainerShape(((Node) _target_1));
      _xifexpression = _containerShape;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  protected ShapeContainer getContainerShape(final Node it) {
    ShapeContainer _xifexpression = null;
    boolean _equals = Objects.equal(it, null);
    if (_equals) {
      _xifexpression = null;
    } else {
      ShapeContainer _xifexpression_1 = null;
      boolean _contains = this.shapes.contains(it);
      if (_contains) {
        _xifexpression_1 = ((ShapeContainer) it);
      } else {
        Parent _parent = it.getParent();
        ShapeContainer _containerShape = this.getContainerShape(_parent);
        _xifexpression_1 = _containerShape;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public boolean addConnection(final Connection connection) {
    boolean _xblockexpression = false;
    {
      ObservableList<Node> _children = this.connectionLayer.getChildren();
      _children.add(connection);
      boolean _add = this.connections.add(connection);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
  
  public Group getRootPane() {
    return this.rootPane;
  }
}
