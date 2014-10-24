package de.fxdiagram.core.extensions;

import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class AnimationExtensions {
  public static Function0<Animation> chain(final Function0<? extends Animation> first, final Function0<? extends Animation> second) {
    final Function0<Animation> _function = new Function0<Animation>() {
      public Animation apply() {
        Animation _xblockexpression = null;
        {
          final Animation animation = first.apply();
          final EventHandler<ActionEvent> orig = animation.getOnFinished();
          final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent it) {
              orig.handle(it);
              Animation _apply = second.apply();
              _apply.play();
            }
          };
          animation.setOnFinished(_function);
          _xblockexpression = animation;
        }
        return _xblockexpression;
      }
    };
    return _function;
  }
}
