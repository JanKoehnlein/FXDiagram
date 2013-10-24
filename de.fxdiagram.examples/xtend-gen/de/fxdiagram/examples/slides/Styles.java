package de.fxdiagram.examples.slides;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Styles {
  public static Text createText(final String text, final int fontSize) {
    Text _createText = Styles.createText(text, "Gill Sans", fontSize);
    return _createText;
  }
  
  public static Text createJungleText(final String text, final int fontSize) {
    Text _createText = Styles.createText(text, "Chalkduster", fontSize);
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
        Color _jungleGreen = Styles.jungleGreen();
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
}
