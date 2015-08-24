package de.fxdiagram.core.behavior;

import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.DirtyStateBehavior;
import de.fxdiagram.core.extensions.DurationExtensions;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class AbstractDirtyStateBehavior<T extends XShape> extends AbstractHostBehavior<T> implements DirtyStateBehavior {
  private Animation modifiedAnimation;
  
  public AbstractDirtyStateBehavior(final T host) {
    super(host);
    SequentialTransition _sequentialTransition = new SequentialTransition();
    final Procedure1<SequentialTransition> _function = (SequentialTransition it) -> {
      ObservableList<Animation> _children = it.getChildren();
      RotateTransition _rotateTransition = new RotateTransition();
      final Procedure1<RotateTransition> _function_1 = (RotateTransition it_1) -> {
        it_1.setFromAngle((-5));
        it_1.setToAngle(5);
        Duration _millis = DurationExtensions.millis(170);
        it_1.setDuration(_millis);
        it_1.setCycleCount(Animation.INDEFINITE);
        it_1.setAutoReverse(true);
        it_1.setNode(host);
      };
      RotateTransition _doubleArrow = ObjectExtensions.<RotateTransition>operator_doubleArrow(_rotateTransition, _function_1);
      _children.add(_doubleArrow);
    };
    SequentialTransition _doubleArrow = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
    this.modifiedAnimation = _doubleArrow;
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
    if (state != null) {
      switch (state) {
        case CLEAN:
          this.showAsClean();
          break;
        case DIRTY:
          this.showAsDirty();
          break;
        case DANGLING:
          this.showAsDangling();
          break;
        default:
          break;
      }
    }
  }
  
  protected void showAsClean() {
    T _host = this.getHost();
    _host.setOpacity(1);
    this.modifiedAnimation.stop();
    T _host_1 = this.getHost();
    _host_1.setRotate(0);
  }
  
  protected void showAsDirty() {
    T _host = this.getHost();
    _host.setOpacity(1);
    this.modifiedAnimation.play();
  }
  
  protected void showAsDangling() {
    T _host = this.getHost();
    Node _node = _host.getNode();
    _node.setOpacity(0.5);
    this.modifiedAnimation.stop();
    T _host_1 = this.getHost();
    _host_1.setRotate(0);
  }
}
