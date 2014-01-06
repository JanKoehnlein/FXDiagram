package de.fxdiagram.examples.slides;

import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.examples.slides.RevealBehavior;
import de.fxdiagram.examples.slides.Slide;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ClickThroughSlide extends Slide {
  private Group pane = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private Node currentNode;
  
  public ClickThroughSlide(final String name, final Image backgroundImage) {
    super(name, backgroundImage);
    StackPane _stackPane = this.getStackPane();
    ObservableList<Node> _children = _stackPane.getChildren();
    _children.add(this.pane);
  }
  
  public void doActivate() {
    super.doActivate();
    final Procedure1<Group> _function = new Procedure1<Group>() {
      public void apply(final Group it) {
        Scene _scene = it.getScene();
        double _width = _scene.getWidth();
        Scene _scene_1 = it.getScene();
        double _height = _scene_1.getHeight();
        Rectangle _rectangle = new Rectangle(0, 0, _width, _height);
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
    ObjectExtensions.<Group>operator_doubleArrow(
      this.pane, _function);
    ObservableList<Node> _children = this.pane.getChildren();
    Node _head = IterableExtensions.<Node>head(_children);
    this.currentNode = _head;
    RevealBehavior _revealBehavior = new RevealBehavior(this);
    this.addBehavior(_revealBehavior);
  }
  
  public FadeTransition getRevealTransition(final Node childNode) {
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
    return _doubleArrow;
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
      FadeTransition _revealTransition = this.getRevealTransition(this.currentNode);
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
  
  public Group getPane() {
    return this.pane;
  }
}
