package de.fxdiagram.examples.slides;

import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.examples.slides.RevealBehavior;
import de.fxdiagram.examples.slides.Slide;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ClickThroughSlide extends Slide {
  private Pane pane = new Function0<Pane>() {
    public Pane apply() {
      Pane _pane = new Pane();
      return _pane;
    }
  }.apply();
  
  private Node currentNode;
  
  private Map<Node,Transition> revealTransitions = new Function0<Map<Node,Transition>>() {
    public Map<Node,Transition> apply() {
      HashMap<Node,Transition> _newHashMap = CollectionLiterals.<Node, Transition>newHashMap();
      return _newHashMap;
    }
  }.apply();
  
  public ClickThroughSlide(final String name) {
    super(name);
    StackPane _stackPane = this.getStackPane();
    ObservableList<Node> _children = _stackPane.getChildren();
    _children.add(this.pane);
  }
  
  public void doActivate() {
    super.doActivate();
    final Procedure1<Pane> _function = new Procedure1<Pane>() {
      public void apply(final Pane it) {
        Scene _scene = it.getScene();
        double _width = _scene.getWidth();
        Scene _scene_1 = it.getScene();
        double _height = _scene_1.getHeight();
        it.setPrefSize(_width, _height);
        Scene _scene_2 = it.getScene();
        double _width_1 = _scene_2.getWidth();
        Scene _scene_3 = it.getScene();
        double _height_1 = _scene_3.getHeight();
        Rectangle _rectangle = new Rectangle(0, 0, _width_1, _height_1);
        it.setClip(_rectangle);
        ObservableList<Node> _children = it.getChildren();
        Iterable<Node> _tail = IterableExtensions.<Node>tail(_children);
        final Procedure1<Node> _function = new Procedure1<Node>() {
          public void apply(final Node it) {
            it.setOpacity(0);
          }
        };
        IterableExtensions.<Node>forEach(_tail, _function);
      }
    };
    ObjectExtensions.<Pane>operator_doubleArrow(
      this.pane, _function);
    ObservableList<Node> _children = this.pane.getChildren();
    Node _head = IterableExtensions.<Node>head(_children);
    this.currentNode = _head;
    RevealBehavior _revealBehavior = new RevealBehavior(this);
    this.addBehavior(_revealBehavior);
  }
  
  public Transition setRevealTransition(final Node node, final Transition transition) {
    Transition _put = this.revealTransitions.put(node, transition);
    return _put;
  }
  
  public Transition getRevealTransition(final Node childNode) {
    Transition _xifexpression = null;
    boolean _containsKey = this.revealTransitions.containsKey(childNode);
    if (_containsKey) {
      Transition _get = this.revealTransitions.get(childNode);
      _xifexpression = _get;
    } else {
      FadeTransition _fadeTransition = new FadeTransition();
      final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
        public void apply(final FadeTransition it) {
          it.setNode(childNode);
          it.setFromValue(0);
          it.setToValue(1);
          Duration _millis = DurationExtensions.millis(200);
          it.setDuration(_millis);
        }
      };
      FadeTransition _doubleArrow = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
      _xifexpression = _doubleArrow;
    }
    return _xifexpression;
  }
  
  public boolean next() {
    final ObservableList<Node> children = this.pane.getChildren();
    boolean _isEmpty = children.isEmpty();
    if (_isEmpty) {
      return false;
    } else {
      int _indexOf = children.indexOf(this.currentNode);
      final int nextIndex = (_indexOf + 1);
      int _size = children.size();
      boolean _equals = (nextIndex == _size);
      if (_equals) {
        return false;
      }
      Node _get = children.get(nextIndex);
      this.currentNode = _get;
      Node _node = this.getNode();
      Transition _revealTransition = this.getRevealTransition(_node);
      _revealTransition.play();
      return true;
    }
  }
  
  public boolean previous() {
    final ObservableList<Node> children = this.pane.getChildren();
    boolean _isEmpty = children.isEmpty();
    if (_isEmpty) {
      return false;
    } else {
      int _indexOf = children.indexOf(this.currentNode);
      final int previousIndex = (_indexOf - 1);
      boolean _lessThan = (previousIndex < 0);
      if (_lessThan) {
        return false;
      }
      this.currentNode.setOpacity(0);
      Node _get = children.get(previousIndex);
      this.currentNode = _get;
      return true;
    }
  }
  
  public Pane getPane() {
    return this.pane;
  }
}
