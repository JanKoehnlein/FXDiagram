package de.fxdiagram.examples.slides;

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
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode("slides")
@SuppressWarnings("all")
public class SlideDiagram extends XDiagram {
  public SlideDiagram() {
  }
  
  public boolean operator_add(final Slide slide) {
    return this.getSlides().add(slide);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    this.setBackgroundPaint(Color.BLACK);
    SlideNavigation _slideNavigation = new SlideNavigation(this);
    this.addBehavior(_slideNavigation);
    this.showSlide(IterableExtensions.<Slide>head(this.getSlides()));
  }
  
  public boolean next() {
    boolean _xblockexpression = false;
    {
      Slide _xifexpression = null;
      Slide _currentSlide = this.getCurrentSlide();
      boolean _tripleNotEquals = (_currentSlide != null);
      if (_tripleNotEquals) {
        Slide _xblockexpression_1 = null;
        {
          int _indexOf = this.getSlides().indexOf(this.getCurrentSlide());
          final int nextIndex = (_indexOf + 1);
          int _size = this.getSlides().size();
          boolean _equals = (nextIndex == _size);
          if (_equals) {
            CloseBehavior _behavior = this.<CloseBehavior>getBehavior(CloseBehavior.class);
            if (_behavior!=null) {
              _behavior.close();
            }
            return false;
          }
          _xblockexpression_1 = this.getSlides().get(nextIndex);
        }
        _xifexpression = _xblockexpression_1;
      } else {
        _xifexpression = IterableExtensions.<Slide>head(this.getSlides());
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
      boolean _tripleNotEquals = (_currentSlide != null);
      if (_tripleNotEquals) {
        Slide _xblockexpression_1 = null;
        {
          int _indexOf = slides.indexOf(this.getCurrentSlide());
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
    if ((oldSlide != null)) {
      FadeTransition _fadeTransition = new FadeTransition();
      final Procedure1<FadeTransition> _function = (FadeTransition it) -> {
        it.setNode(oldSlide);
        it.setDuration(DurationExtensions.millis(200));
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
    if ((newSlide != null)) {
      FadeTransition _xblockexpression = null;
      {
        ObservableList<XNode> _nodes = this.getNodes();
        _nodes.add(newSlide);
        newSlide.setSelected(true);
        FadeTransition _fadeTransition_1 = new FadeTransition();
        final Procedure1<FadeTransition> _function_1 = (FadeTransition it) -> {
          it.setNode(newSlide);
          it.setDuration(DurationExtensions.millis(200));
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
    if (((fade != null) && (appear != null))) {
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
