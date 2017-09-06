package de.fxdiagram.examples.slides.democamp;

import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.examples.lcars.LcarsExtensions;
import de.fxdiagram.examples.slides.ClickThroughSlide;
import de.fxdiagram.examples.slides.Slide;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DemoCampSlideFactory {
  public static Slide createSlide(final String slideName) {
    Image _backgroundImage = DemoCampSlideFactory.getBackgroundImage();
    return new Slide(slideName, _backgroundImage);
  }
  
  public static Slide createSlideWithText(final String slideName, final String text, final int fontSize) {
    Image _backgroundImage = DemoCampSlideFactory.getBackgroundImage();
    Slide _slide = new Slide(slideName, _backgroundImage);
    final Procedure1<Slide> _function = (Slide it) -> {
      ObservableList<Node> _children = it.getStackPane().getChildren();
      Text _createText = DemoCampSlideFactory.createText(text.trim(), fontSize);
      final Procedure1<Text> _function_1 = (Text it_1) -> {
        it_1.setTextAlignment(TextAlignment.CENTER);
      };
      Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function_1);
      _children.add(_doubleArrow);
    };
    return ObjectExtensions.<Slide>operator_doubleArrow(_slide, _function);
  }
  
  public static ClickThroughSlide createClickThroughSlide(final String slideName) {
    Image _backgroundImage = DemoCampSlideFactory.getBackgroundImage();
    ClickThroughSlide _clickThroughSlide = new ClickThroughSlide(slideName, _backgroundImage);
    final Procedure1<ClickThroughSlide> _function = (ClickThroughSlide it) -> {
      it.initializeGraphics();
    };
    return ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_clickThroughSlide, _function);
  }
  
  public static Text createText(final String text, final double size) {
    Text _text = new Text();
    final Procedure1<Text> _function = (Text it) -> {
      it.setText(text);
      it.setFont(LcarsExtensions.lcarsFont(size));
      it.setFill(DemoCampSlideFactory.getTextColor());
    };
    return ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
  }
  
  public static Color getTextColor() {
    return Color.rgb(238, 191, 171);
  }
  
  public static Color getDarkTextColor() {
    return Color.rgb(156, 124, 114);
  }
  
  public static Image getBackgroundImage() {
    return ImageCache.get().getImage(DemoCampSlideFactory.class, "images/planet.jpg");
  }
}
