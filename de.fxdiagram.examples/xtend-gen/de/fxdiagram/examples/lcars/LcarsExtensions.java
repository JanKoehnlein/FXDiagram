package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import java.io.InputStream;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class LcarsExtensions {
  private static Map<Double, Font> cache = CollectionLiterals.<Double, Font>newHashMap();
  
  public static Font lcarsFont(final double size) {
    Font _xblockexpression = null;
    {
      final Font cachedFont = LcarsExtensions.cache.get(Double.valueOf(size));
      boolean _notEquals = (!Objects.equal(cachedFont, null));
      if (_notEquals) {
        return cachedFont;
      }
      ClassLoader _classLoader = LcarsExtensions.class.getClassLoader();
      final InputStream input = _classLoader.getResourceAsStream("de/fxdiagram/examples/lcars/LCARSGTJ3.ttf");
      final Font font = Font.loadFont(input, size);
      LcarsExtensions.cache.put(Double.valueOf(size), font);
      _xblockexpression = font;
    }
    return _xblockexpression;
  }
  
  public final static Color ORANGE = LcarsExtensions.rgbColor(251, 134, 9);
  
  public final static Color DARKBLUE = LcarsExtensions.rgbColor(135, 132, 194);
  
  public final static Color FLESH = LcarsExtensions.rgbColor(253, 193, 137);
  
  public final static Color DARKFLESH = LcarsExtensions.rgbColor(251, 135, 84);
  
  public final static Color VIOLET = LcarsExtensions.rgbColor(190, 131, 192);
  
  public final static Color RED = LcarsExtensions.rgbColor(192, 80, 85);
  
  public final static Color BLUE = LcarsExtensions.rgbColor(136, 130, 254);
  
  public final static Color MAGENTA = LcarsExtensions.rgbColor(190, 78, 134);
  
  private static Color rgbColor(final int red, final int green, final int blue) {
    return new Color((red / 255.0), (green / 255.0), (blue / 255.0), 1);
  }
}
