package de.fxdiagram.examples.slides;

import com.google.common.base.Objects;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.CloseBehavior;
import de.fxdiagram.core.behavior.NavigationBehavior;
import de.fxdiagram.examples.slides.Slide;
import de.fxdiagram.examples.slides.SlideDiagram;
import java.util.List;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class SlideNavigation extends AbstractHostBehavior<SlideDiagram> implements NavigationBehavior {
  public SlideNavigation(final SlideDiagram host) {
    super(host);
  }
  
  protected void doActivate() {
  }
  
  public Class<? extends Behavior> getBehaviorKey() {
    return NavigationBehavior.class;
  }
  
  public boolean next() {
    boolean _xblockexpression = false;
    {
      SlideDiagram _host = this.getHost();
      final List<Slide> slides = _host.getSlides();
      Slide _xifexpression = null;
      SlideDiagram _host_1 = this.getHost();
      Slide _currentSlide = _host_1.getCurrentSlide();
      boolean _notEquals = (!Objects.equal(_currentSlide, null));
      if (_notEquals) {
        Slide _xblockexpression_1 = null;
        {
          SlideDiagram _host_2 = this.getHost();
          Slide _currentSlide_1 = _host_2.getCurrentSlide();
          int _indexOf = slides.indexOf(_currentSlide_1);
          final int nextIndex = (_indexOf + 1);
          int _size = slides.size();
          boolean _equals = (nextIndex == _size);
          if (_equals) {
            SlideDiagram _host_3 = this.getHost();
            CloseBehavior _behavior = _host_3.<CloseBehavior>getBehavior(CloseBehavior.class);
            if (_behavior!=null) {
              _behavior.close();
            }
            return false;
          }
          Slide _get = slides.get(nextIndex);
          _xblockexpression_1 = (_get);
        }
        _xifexpression = _xblockexpression_1;
      } else {
        Slide _head = IterableExtensions.<Slide>head(slides);
        _xifexpression = _head;
      }
      final Slide newSlide = _xifexpression;
      SlideDiagram _host_2 = this.getHost();
      _host_2.showSlide(newSlide);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
  
  public boolean previous() {
    boolean _xblockexpression = false;
    {
      SlideDiagram _host = this.getHost();
      final List<Slide> slides = _host.getSlides();
      Slide _xifexpression = null;
      SlideDiagram _host_1 = this.getHost();
      Slide _currentSlide = _host_1.getCurrentSlide();
      boolean _notEquals = (!Objects.equal(_currentSlide, null));
      if (_notEquals) {
        Slide _xblockexpression_1 = null;
        {
          SlideDiagram _host_2 = this.getHost();
          Slide _currentSlide_1 = _host_2.getCurrentSlide();
          int _indexOf = slides.indexOf(_currentSlide_1);
          final int previousIndex = (_indexOf - 1);
          boolean _lessThan = (previousIndex < 0);
          if (_lessThan) {
            SlideDiagram _host_3 = this.getHost();
            CloseBehavior _behavior = _host_3.<CloseBehavior>getBehavior(CloseBehavior.class);
            if (_behavior!=null) {
              _behavior.close();
            }
            return false;
          }
          Slide _get = slides.get(previousIndex);
          _xblockexpression_1 = (_get);
        }
        _xifexpression = _xblockexpression_1;
      } else {
        Slide _head = IterableExtensions.<Slide>head(slides);
        _xifexpression = _head;
      }
      final Slide newSlide = _xifexpression;
      SlideDiagram _host_2 = this.getHost();
      _host_2.showSlide(newSlide);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
}
