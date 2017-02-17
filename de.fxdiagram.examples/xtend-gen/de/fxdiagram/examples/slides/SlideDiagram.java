package de.fxdiagram.examples.slides;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.behavior.CloseBehavior;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.examples.slides.Slide;
import de.fxdiagram.examples.slides.SlideNavigation;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode("slides")
@SuppressWarnings("all")
public class SlideDiagram extends XDiagram {
  public SlideDiagram() {
  }
  
  public boolean operator_add(final Slide slide) {
    ObservableList<Slide> _slides = this.getSlides();
    return _slides.add(slide);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    this.setBackgroundPaint(Color.BLACK);
    SlideNavigation _slideNavigation = new SlideNavigation(this);
    this.addBehavior(_slideNavigation);
    ObservableList<Slide> _slides = this.getSlides();
    Slide _head = IterableExtensions.<Slide>head(_slides);
    this.showSlide(_head);
  }
  
  public boolean next() {
    boolean _xblockexpression = false;
    {
      Slide _xifexpression = null;
      Slide _currentSlide = this.getCurrentSlide();
      boolean _notEquals = (!Objects.equal(_currentSlide, null));
      if (_notEquals) {
        Slide _xblockexpression_1 = null;
        {
          ObservableList<Slide> _slides = this.getSlides();
          Slide _currentSlide_1 = this.getCurrentSlide();
          int _indexOf = _slides.indexOf(_currentSlide_1);
          final int nextIndex = (_indexOf + 1);
          ObservableList<Slide> _slides_1 = this.getSlides();
          int _size = _slides_1.size();
          boolean _equals = (nextIndex == _size);
          if (_equals) {
            CloseBehavior _behavior = this.<CloseBehavior>getBehavior(CloseBehavior.class);
            if (_behavior!=null) {
              _behavior.close();
            }
            return false;
          }
          ObservableList<Slide> _slides_2 = this.getSlides();
          _xblockexpression_1 = _slides_2.get(nextIndex);
        }
        _xifexpression = _xblockexpression_1;
      } else {
        ObservableList<Slide> _slides = this.getSlides();
        _xifexpression = IterableExtensions.<Slide>head(_slides);
      }
      final Slide newSlide = _xifexpression;
      this.showSlide(newSlide);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  public boolean previous() {
    boolean _xblockexpression = false;
    {
      final ObservableList<Slide> slides = this.getSlides();
      Slide _xifexpression = null;
      Slide _currentSlide = this.getCurrentSlide();
      boolean _notEquals = (!Objects.equal(_currentSlide, null));
      if (_notEquals) {
        Slide _xblockexpression_1 = null;
        {
          Slide _currentSlide_1 = this.getCurrentSlide();
          int _indexOf = slides.indexOf(_currentSlide_1);
          final int previousIndex = (_indexOf - 1);
          if ((previousIndex < 0)) {
            CloseBehavior _behavior = this.<CloseBehavior>getBehavior(CloseBehavior.class);
            if (_behavior!=null) {
              _behavior.close();
            }
            return false;
          }
          _xblockexpression_1 = slides.get(previousIndex);
        }
        _xifexpression = _xblockexpression_1;
      } else {
        _xifexpression = IterableExtensions.<Slide>head(slides);
      }
      final Slide newSlide = _xifexpression;
      this.showSlide(newSlide);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  protected void showSlide(final Slide newSlide) {
    final Slide oldSlide = this.getCurrentSlide();
    FadeTransition _xifexpression = null;
    boolean _notEquals = (!Objects.equal(oldSlide, null));
    if (_notEquals) {
      FadeTransition _fadeTransition = new FadeTransition();
      final Procedure1<FadeTransition> _function = (FadeTransition it) -> {
        it.setNode(oldSlide);
        Duration _millis = DurationExtensions.millis(200);
        it.setDuration(_millis);
        it.setFromValue(1);
        it.setToValue(0);
        final EventHandler<ActionEvent> _function_1 = (ActionEvent it_1) -> {
          ObservableList<XNode> _nodes = this.getNodes();
          _nodes.remove(oldSlide);
        };
        it.setOnFinished(_function_1);
      };
      _xifexpression = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
    } else {
      _xifexpression = null;
    }
    final FadeTransition fade = _xifexpression;
    FadeTransition _xifexpression_1 = null;
    boolean _notEquals_1 = (!Objects.equal(newSlide, null));
    if (_notEquals_1) {
      FadeTransition _xblockexpression = null;
      {
        ObservableList<XNode> _nodes = this.getNodes();
        _nodes.add(newSlide);
        newSlide.setSelected(true);
        FadeTransition _fadeTransition_1 = new FadeTransition();
        final Procedure1<FadeTransition> _function_1 = (FadeTransition it) -> {
          it.setNode(newSlide);
          Duration _millis = DurationExtensions.millis(200);
          it.setDuration(_millis);
          it.setFromValue(0);
          it.setToValue(1);
        };
        _xblockexpression = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition_1, _function_1);
      }
      _xifexpression_1 = _xblockexpression;
    } else {
      _xifexpression_1 = null;
    }
    final FadeTransition appear = _xifexpression_1;
    if (((!Objects.equal(fade, null)) && (!Objects.equal(appear, null)))) {
      ParallelTransition _parallelTransition = new ParallelTransition();
      final Procedure1<ParallelTransition> _function_1 = (ParallelTransition it) -> {
        ObservableList<Animation> _children = it.getChildren();
        _children.add(fade);
        ObservableList<Animation> _children_1 = it.getChildren();
        _children_1.add(appear);
        it.play();
      };
      ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_1);
    } else {
      if (fade!=null) {
        fade.play();
      }
      if (appear!=null) {
        appear.play();
      }
    }
    this.setCurrentSlide(newSlide);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(slidesProperty, Slide.class);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private SimpleListProperty<Slide> slidesProperty = new SimpleListProperty<Slide>(this, "slides",_initSlides());
  
  private static final ObservableList<Slide> _initSlides() {
    ObservableList<Slide> _observableArrayList = FXCollections.<Slide>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<Slide> getSlides() {
    return this.slidesProperty.get();
  }
  
  public ListProperty<Slide> slidesProperty() {
    return this.slidesProperty;
  }
  
  private SimpleObjectProperty<Slide> currentSlideProperty = new SimpleObjectProperty<Slide>(this, "currentSlide");
  
  public Slide getCurrentSlide() {
    return this.currentSlideProperty.get();
  }
  
  public void setCurrentSlide(final Slide currentSlide) {
    this.currentSlideProperty.set(currentSlide);
  }
  
  public ObjectProperty<Slide> currentSlideProperty() {
    return this.currentSlideProperty;
  }
}
