package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import java.util.Queue;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AnimationQueue {
  private Queue<Function0<? extends Animation>> queue = CollectionLiterals.<Function0<? extends Animation>>newLinkedList();
  
  public void enqueue(final Function0<? extends Animation> animationFactory) {
    boolean _notEquals = (!Objects.equal(animationFactory, null));
    if (_notEquals) {
      final boolean isEmpty = this.queue.isEmpty();
      this.queue.add(animationFactory);
      if (isEmpty) {
        this.executeNext();
      }
    }
  }
  
  protected void executeNext() {
    final Function0<? extends Animation> next = this.queue.peek();
    boolean _notEquals = (!Objects.equal(next, null));
    if (_notEquals) {
      final Animation animation = next.apply();
      boolean _notEquals_1 = (!Objects.equal(animation, null));
      if (_notEquals_1) {
        SequentialTransition _sequentialTransition = new SequentialTransition();
        final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
          public void apply(final SequentialTransition it) {
            ObservableList<Animation> _children = it.getChildren();
            _children.add(animation);
            final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
              public void handle(final ActionEvent it) {
                AnimationQueue.this.queue.poll();
                AnimationQueue.this.executeNext();
              }
            };
            it.setOnFinished(_function);
            it.play();
          }
        };
        ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
      } else {
        this.queue.poll();
        this.executeNext();
      }
    }
  }
}
