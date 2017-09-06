package de.fxdiagram.examples.slides;

import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.NavigationBehavior;
import de.fxdiagram.examples.slides.ClickThroughSlide;

@SuppressWarnings("all")
public class RevealBehavior extends AbstractHostBehavior<ClickThroughSlide> implements NavigationBehavior {
  public RevealBehavior(final ClickThroughSlide slide) {
    super(slide);
  }
  
  @Override
  protected void doActivate() {
  }
  
  @Override
  public Class<? extends Behavior> getBehaviorKey() {
    return NavigationBehavior.class;
  }
  
  @Override
  public boolean next() {
    return this.getHost().next();
  }
  
  @Override
  public boolean previous() {
    return this.getHost().previous();
  }
}
