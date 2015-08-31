package de.fxdiagram.core.behavior;

import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.DirtyStateBehavior;

@SuppressWarnings("all")
public abstract class AbstractDirtyStateBehavior<T extends XShape> extends AbstractHostBehavior<T> implements DirtyStateBehavior {
  private DirtyState shownState = DirtyState.CLEAN;
  
  public AbstractDirtyStateBehavior(final T host) {
    super(host);
  }
  
  @Override
  public Class<? extends Behavior> getBehaviorKey() {
    return DirtyStateBehavior.class;
  }
  
  @Override
  protected void doActivate() {
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
