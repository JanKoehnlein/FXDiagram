package de.fxdiagram.core.extensions;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.geometry.Dimension2D;
import javafx.scene.text.Text;

@SuppressWarnings("all")
public class TextExtensions {
  public static Dimension2D getOfflineDimension(final Text it) {
    float _offlineWidth = TextExtensions.getOfflineWidth(it);
    float _offlineHeight = TextExtensions.getOfflineHeight(it);
    return new Dimension2D(_offlineWidth, _offlineHeight);
  }
  
  public static float getOfflineWidth(final Text it) {
    return TextExtensions.getFontLoader().computeStringWidth(it.getText(), it.getFont());
  }
  
  public static float getOfflineHeight(final Text it) {
    return TextExtensions.getFontLoader().getFontMetrics(it.getFont()).getLineHeight();
  }
  
  private static FontLoader getFontLoader() {
    return Toolkit.getToolkit().getFontLoader();
  }
}
