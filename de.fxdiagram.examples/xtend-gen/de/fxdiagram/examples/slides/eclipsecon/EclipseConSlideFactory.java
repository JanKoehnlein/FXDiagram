package de.fxdiagram.examples.slides.eclipsecon;

import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.examples.slides.ClickThroughSlide;
import de.fxdiagram.examples.slides.Slide;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class EclipseConSlideFactory {
  public static Slide createSlide(final String slideName) {
    Image _backgroundImage = EclipseConSlideFactory.getBackgroundImage();
    Slide _slide = new Slide(slideName, _backgroundImage);
    return _slide;
  }
  
  public static Slide createSlide(final String text, final int fontSize) {
    Image _backgroundImage = EclipseConSlideFactory.getBackgroundImage();
    Slide _slide = new Slide(text, _backgroundImage);
    final Procedure1<Slide> _function = new Procedure1<Slide>() {
      public void apply(final Slide it) {
        StackPane _stackPane = it.getStackPane();
        final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
          public void apply(final StackPane it) {
            ObservableList<Node> _children = it.getChildren();
            Text _createText = EclipseConSlideFactory.createText(text, fontSize);
            _children.add(_createText);
          }
        };
        ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
      }
    };
    Slide _doubleArrow = ObjectExtensions.<Slide>operator_doubleArrow(_slide, _function);
    return _doubleArrow;
  }
  
  public static ClickThroughSlide createClickThroughSlide(final String slideName) {
    Image _backgroundImage = EclipseConSlideFactory.getBackgroundImage();
    ClickThroughSlide _clickThroughSlide = new ClickThroughSlide(slideName, _backgroundImage);
    return _clickThroughSlide;
  }
  
  public static Image getBackgroundImage() {
    ImageCache _get = ImageCache.get();
    Image _image = _get.getImage(EclipseConSlideFactory.class, "images/jungle.jpg");
    return _image;
  }
  
  public static Text createText(final String text, final int fontSize) {
    Text _createText = EclipseConSlideFactory.createText(text, "Gill Sans", fontSize);
    return _createText;
  }
  
  public static Text createJungleText(final String text, final int fontSize) {
    Text _createText = EclipseConSlideFactory.createText(text, "Chalkduster", fontSize);
    return _createText;
  }
  
  public static Text createText(final String text, final String fontName, final int fontSize) {
    Text _text = new Text();
    final Procedure1<Text> _function = new Procedure1<Text>() {
      public void apply(final Text it) {
        String _trim = text.trim();
        it.setText(_trim);
        it.setTextAlignment(TextAlignment.CENTER);
        Font _font = new Font(fontName, fontSize);
        it.setFont(_font);
        Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
        it.setFill(_jungleGreen);
      }
    };
    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
    return _doubleArrow;
  }
  
  public static Color jungleGreen() {
    Color _rgb = Color.rgb(224, 237, 214);
    return _rgb;
  }
  
  public static Color jungleDarkGreen() {
    Color _rgb = Color.rgb(161, 171, 74);
    return _rgb;
  }
  
  public static Color jungleDarkestGreen() {
    Color _rgb = Color.rgb(107, 114, 51);
    return _rgb;
  }
}
