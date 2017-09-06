package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Clients should not access this class.
 */
@SuppressWarnings("all")
public class AnimationQueue {
  public interface Listener {
    public abstract void handleQueueEmpty();
  }
  
  private Queue<Function0<? extends Animation>> queue = CollectionLiterals.<Function0<? extends Animation>>newLinkedList();
  
  private List<AnimationQueue.Listener> listeners = CollectionLiterals.<AnimationQueue.Listener>newArrayList();
  
  public void addListener(final AnimationQueue.Listener listener) {
    this.listeners.add(listener);
  }
  
  public void removeListener(final AnimationQueue.Listener listener) {
    this.listeners.remove(listener);
  }
  
  public void enqueue(final Function0<? extends Animation> animationFactory) {
    boolean _notEquals = (!Objects.equal(animationFactory, null));
    if (_notEquals) {
      synchronized (this.queue) {
        final boolean isEmpty = this.queue.isEmpty();
        this.queue.add(animationFactory);
        if (isEmpty) {
          this.executeNext();
        }
      }
    }
  }
  
  protected void executeNext() {
    Function0<? extends Animation> _xsynchronizedexpression = null;
    synchronized (this.queue) {
      _xsynchronizedexpression = this.queue.peek();
    }
    final Function0<? extends Animation> next = _xsynchronizedexpression;
    boolean _notEquals = (!Objects.equal(next, null));
    if (_notEquals) {
      final Animation animation = next.apply();
      boolean _notEquals_1 = (!Objects.equal(animation, null));
      if (_notEquals_1) {
        SequentialTransition _sequentialTransition = new SequentialTransition();
        final Procedure1<SequentialTransition> _function = (SequentialTransition it) -> {
          ObservableList<Animation> _children = it.getChildren();
          _children.add(animation);
          final EventHandler<ActionEvent> _function_1 = (ActionEvent it_1) -> {
            synchronized (this.queue) {
              this.queue.poll();
            }
            this.executeNext();
          };
          it.setOnFinished(_function_1);
          it.play();
        };
        ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
      } else {
        synchronized (this.queue) {
          this.queue.poll();
        }
        this.executeNext();
      }
    } else {
      final Consumer<AnimationQueue.Listener> _function_1 = (AnimationQueue.Listener it) -> {
        it.handleQueueEmpty();
      };
      Lists.<AnimationQueue.Listener>newArrayList(this.listeners).forEach(_function_1);
    }
  }
}
