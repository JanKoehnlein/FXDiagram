package de.fxdiagram.core.behavior;

import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.ReconcileBehavior;
import de.fxdiagram.core.extensions.DurationExtensions;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class AbstractReconcileBehavior<T extends XShape> extends AbstractHostBehavior<T> implements ReconcileBehavior {
  private DirtyState shownState = DirtyState.CLEAN;
  
  private Animation dirtyAnimation;
  
  public AbstractReconcileBehavior(final T host) {
    super(host);
  }
  
  @Override
  public Class<? extends Behavior> getBehaviorKey() {
    return ReconcileBehavior.class;
  }
  
  @Override
  protected void doActivate() {
    SequentialTransition _sequentialTransition = new SequentialTransition();
    final Procedure1<SequentialTransition> _function = (SequentialTransition it) -> {
      ObservableList<Animation> _children = it.getChildren();
      FadeTransition _fadeTransition = new FadeTransition();
      final Procedure1<FadeTransition> _function_1 = (FadeTransition it_1) -> {
        T _host = this.getHost();
        it_1.setNode(_host);
        Duration _millis = DurationExtensions.millis(300);
        it_1.setDuration(_millis);
        it_1.setFromValue(1);
        it_1.setToValue(0.2);
      };
      FadeTransition _doubleArrow = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function_1);
      _children.add(_doubleArrow);
      ObservableList<Animation> _children_1 = it.getChildren();
      FadeTransition _fadeTransition_1 = new FadeTransition();
      final Procedure1<FadeTransition> _function_2 = (FadeTransition it_1) -> {
        T _host = this.getHost();
        it_1.setNode(_host);
        Duration _millis = DurationExtensions.millis(300);
        it_1.setDuration(_millis);
        it_1.setFromValue(0.2);
        it_1.setToValue(1);
      };
      FadeTransition _doubleArrow_1 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition_1, _function_2);
      _children_1.add(_doubleArrow_1);
      it.setCycleCount(Animation.INDEFINITE);
    };
    SequentialTransition _doubleArrow = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
    this.dirtyAnimation = _doubleArrow;
    DirtyState _dirtyState = this.getDirtyState();
    this.showDirtyState(_dirtyState);
  }
  
  @Override
  public void showDirtyState(final DirtyState state) {
    this.feedback(false);
    this.shownState = state;
    this.feedback(true);
  }
  
  protected void feedback(final boolean show) {
    final DirtyState shownState = this.shownState;
    if (shownState != null) {
      switch (shownState) {
        case CLEAN:
          this.cleanFeedback(show);
          break;
        case DIRTY:
          this.dirtyFeedback(show);
          break;
        case DANGLING:
          this.danglingFeedback(show);
          break;
        default:
          break;
      }
    }
  }
  
  protected void cleanFeedback(final boolean isClean) {
  }
  
  protected void dirtyFeedback(final boolean isDirty) {
    if (isDirty) {
      this.dirtyAnimation.play();
    } else {
      this.dirtyAnimation.stop();
      T _host = this.getHost();
      _host.setOpacity(1);
    }
  }
  
  protected void danglingFeedback(final boolean isDangling) {
    if (isDangling) {
      T _host = this.getHost();
      _host.setOpacity(0.2);
    } else {
      T _host_1 = this.getHost();
      _host_1.setOpacity(1);
    }
  }
}
