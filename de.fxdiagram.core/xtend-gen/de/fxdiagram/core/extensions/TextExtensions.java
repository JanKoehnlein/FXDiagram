package de.fxdiagram.core.extensions;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.geometry.Dimension2D;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

@SuppressWarnings("all")
public class TextExtensions {
  public static Dimension2D getOfflineDimension(final Text it) {
    float _offlineWidth = TextExtensions.getOfflineWidth(it);
    float _offlineHeight = TextExtensions.getOfflineHeight(it);
    return new Dimension2D(_offlineWidth, _offlineHeight);
  }
  
  public static float getOfflineWidth(final Text it) {
    FontLoader _fontLoader = TextExtensions.getFontLoader();
    String _text = it.getText();
    Font _font = it.getFont();
    return _fontLoader.computeStringWidth(_text, _font);
  }
  
  public static float getOfflineHeight(final Text it) {
    FontLoader _fontLoader = TextExtensions.getFontLoader();
    Font _font = it.getFont();
    FontMetrics _fontMetrics = _fontLoader.getFontMetrics(_font);
    return _fontMetrics.getLineHeight();
  }
  
  private static FontLoader getFontLoader() {
    Toolkit _toolkit = Toolkit.getToolkit();
    return _toolkit.getFontLoader();
  }
}
