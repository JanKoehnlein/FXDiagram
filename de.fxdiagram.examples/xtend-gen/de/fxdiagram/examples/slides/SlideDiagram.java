package de.fxdiagram.examples.slides;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.examples.slides.Slide;
import de.fxdiagram.examples.slides.SlideNavigation;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.property.ObjectProperty;
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

@SuppressWarnings("all")
public class SlideDiagram extends XDiagram {
  public boolean operator_add(final Slide slide) {
    List<Slide> _slides = this.getSlides();
    boolean _add = _slides.add(slide);
    return _add;
  }
  
  public void doActivate() {
    super.doActivate();
    this.setBackgroundPaint(Color.BLACK);
    SlideNavigation _slideNavigation = new SlideNavigation(this);
    this.addBehavior(_slideNavigation);
    List<Slide> _slides = this.getSlides();
    Slide _head = IterableExtensions.<Slide>head(_slides);
    this.showSlide(_head);
  }
  
  public void showSlide(final Slide newSlide) {
    final Slide oldSlide = this.getCurrentSlide();
    FadeTransition _xifexpression = null;
    boolean _notEquals = (!Objects.equal(oldSlide, null));
    if (_notEquals) {
      FadeTransition _fadeTransition = new FadeTransition();
      final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
        public void apply(final FadeTransition it) {
          it.setNode(oldSlide);
          Duration _millis = DurationExtensions.millis(200);
          it.setDuration(_millis);
          it.setFromValue(1);
          it.setToValue(0);
          final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent it) {
              ObservableList<XNode> _nodes = SlideDiagram.this.getNodes();
              _nodes.remove(oldSlide);
            }
          };
          it.setOnFinished(_function);
        }
      };
      FadeTransition _doubleArrow = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
      _xifexpression = _doubleArrow;
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
        final Procedure1<FadeTransition> _function_1 = new Procedure1<FadeTransition>() {
          public void apply(final FadeTransition it) {
            it.setNode(newSlide);
            Duration _millis = DurationExtensions.millis(200);
            it.setDuration(_millis);
            it.setFromValue(0);
            it.setToValue(1);
          }
        };
        FadeTransition _doubleArrow_1 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition_1, _function_1);
        _xblockexpression = (_doubleArrow_1);
      }
      _xifexpression_1 = _xblockexpression;
    } else {
      _xifexpression_1 = null;
    }
    final FadeTransition appear = _xifexpression_1;
    boolean _and = false;
    boolean _notEquals_2 = (!Objects.equal(fade, null));
    if (!_notEquals_2) {
      _and = false;
    } else {
      boolean _notEquals_3 = (!Objects.equal(appear, null));
      _and = (_notEquals_2 && _notEquals_3);
    }
    if (_and) {
      ParallelTransition _parallelTransition = new ParallelTransition();
      final Procedure1<ParallelTransition> _function_1 = new Procedure1<ParallelTransition>() {
        public void apply(final ParallelTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          _children.add(fade);
          ObservableList<Animation> _children_1 = it.getChildren();
          _children_1.add(appear);
          it.play();
        }
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
  
  private SimpleObjectProperty<List<Slide>> slidesProperty = new SimpleObjectProperty<List<Slide>>(this, "slides",_initSlides());
  
  private static final List<Slide> _initSlides() {
    ObservableList<Slide> _observableArrayList = FXCollections.<Slide>observableArrayList();
    return _observableArrayList;
  }
  
  public List<Slide> getSlides() {
    return this.slidesProperty.get();
  }
  
  public void setSlides(final List<Slide> slides) {
    this.slidesProperty.set(slides);
  }
  
  public ObjectProperty<List<Slide>> slidesProperty() {
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
