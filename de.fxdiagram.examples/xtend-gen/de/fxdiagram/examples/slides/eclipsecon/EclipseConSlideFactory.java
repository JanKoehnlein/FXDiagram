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
    final Procedure1<Slide> _function = (Slide it) -> {
      it.initializeGraphics();
    };
    return ObjectExtensions.<Slide>operator_doubleArrow(_slide, _function);
  }
  
  public static Slide createSlide(final String text, final int fontSize) {
    Image _backgroundImage = EclipseConSlideFactory.getBackgroundImage();
    Slide _slide = new Slide(text, _backgroundImage);
    final Procedure1<Slide> _function = (Slide it) -> {
      it.initializeGraphics();
      StackPane _stackPane = it.getStackPane();
      final Procedure1<StackPane> _function_1 = (StackPane it_1) -> {
        ObservableList<Node> _children = it_1.getChildren();
        Text _createText = EclipseConSlideFactory.createText(text, fontSize);
        _children.add(_createText);
      };
      ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function_1);
    };
    return ObjectExtensions.<Slide>operator_doubleArrow(_slide, _function);
  }
  
  public static ClickThroughSlide createClickThroughSlide(final String slideName) {
    Image _backgroundImage = EclipseConSlideFactory.getBackgroundImage();
    ClickThroughSlide _clickThroughSlide = new ClickThroughSlide(slideName, _backgroundImage);
    final Procedure1<ClickThroughSlide> _function = (ClickThroughSlide it) -> {
      it.initializeGraphics();
    };
    return ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_clickThroughSlide, _function);
  }
  
  public static Image getBackgroundImage() {
    return ImageCache.get().getImage(EclipseConSlideFactory.class, "images/jungle.jpg");
  }
  
  public static Text createText(final String text, final int fontSize) {
    return EclipseConSlideFactory.createText(text, "Gill Sans", fontSize);
  }
  
  public static Text createJungleText(final String text, final int fontSize) {
    return EclipseConSlideFactory.createText(text, "Chalkduster", fontSize);
  }
  
  public static Text createText(final String text, final String fontName, final int fontSize) {
    Text _text = new Text();
    final Procedure1<Text> _function = (Text it) -> {
      it.setText(text.trim());
      it.setTextAlignment(TextAlignment.CENTER);
      Font _font = new Font(fontName, fontSize);
      it.setFont(_font);
      it.setFill(EclipseConSlideFactory.jungleGreen());
    };
    return ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
  }
  
  public static Color jungleGreen() {
    return Color.rgb(224, 237, 214);
  }
  
  public static Color jungleDarkGreen() {
    return Color.rgb(161, 171, 74);
  }
  
  public static Color jungleDarkestGreen() {
    return Color.rgb(107, 114, 51);
  }
}
