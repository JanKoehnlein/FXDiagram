package de.fxdiagram.examples.slides.democamp;

import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.examples.lcars.LcarsExtensions;
import de.fxdiagram.examples.slides.Slide;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DemoCampSlideFactory {
  public static Slide createSlide(final String slideName) {
    Image _backgroundImage = DemoCampSlideFactory.getBackgroundImage();
    Slide _slide = new Slide(slideName, _backgroundImage);
    return _slide;
  }
  
  public static Text createText(final String text, final double size) {
    Text _text = new Text();
    final Procedure1<Text> _function = new Procedure1<Text>() {
      public void apply(final Text it) {
        it.setText(text);
        Font _lcarsFont = LcarsExtensions.lcarsFont(size);
        it.setFont(_lcarsFont);
        Color _textColor = DemoCampSlideFactory.getTextColor();
        it.setFill(_textColor);
      }
    };
    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
    return _doubleArrow;
  }
  
  public static Color getTextColor() {
    Color _rgb = Color.rgb(238, 191, 171);
    return _rgb;
  }
  
  public static Color getDarkTextColor() {
    Color _rgb = Color.rgb(156, 124, 114);
    return _rgb;
  }
  
  public static Image getBackgroundImage() {
    ImageCache _get = ImageCache.get();
    Image _image = _get.getImage(DemoCampSlideFactory.class, "images/planet.jpg");
    return _image;
  }
}
