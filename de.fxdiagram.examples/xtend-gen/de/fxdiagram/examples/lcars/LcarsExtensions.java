package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class LcarsExtensions {
  private static Map<Double,Font> cache = new Function0<Map<Double,Font>>() {
    public Map<Double,Font> apply() {
      HashMap<Double,Font> _newHashMap = CollectionLiterals.<Double, Font>newHashMap();
      return _newHashMap;
    }
  }.apply();
  
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
      _xblockexpression = (font);
    }
    return _xblockexpression;
  }
  
  public final static Color ORANGE = new Function0<Color>() {
    public Color apply() {
      Color _rgbColor = LcarsExtensions.rgbColor(251, 134, 9);
      return _rgbColor;
    }
  }.apply();
  
  public final static Color DARKBLUE = new Function0<Color>() {
    public Color apply() {
      Color _rgbColor = LcarsExtensions.rgbColor(135, 132, 194);
      return _rgbColor;
    }
  }.apply();
  
  public final static Color FLESH = new Function0<Color>() {
    public Color apply() {
      Color _rgbColor = LcarsExtensions.rgbColor(253, 193, 137);
      return _rgbColor;
    }
  }.apply();
  
  public final static Color DARKFLESH = new Function0<Color>() {
    public Color apply() {
      Color _rgbColor = LcarsExtensions.rgbColor(251, 135, 84);
      return _rgbColor;
    }
  }.apply();
  
  public final static Color VIOLET = new Function0<Color>() {
    public Color apply() {
      Color _rgbColor = LcarsExtensions.rgbColor(190, 131, 192);
      return _rgbColor;
    }
  }.apply();
  
  public final static Color RED = new Function0<Color>() {
    public Color apply() {
      Color _rgbColor = LcarsExtensions.rgbColor(192, 80, 85);
      return _rgbColor;
    }
  }.apply();
  
  public final static Color BLUE = new Function0<Color>() {
    public Color apply() {
      Color _rgbColor = LcarsExtensions.rgbColor(136, 130, 254);
      return _rgbColor;
    }
  }.apply();
  
  public final static Color MAGENTA = new Function0<Color>() {
    public Color apply() {
      Color _rgbColor = LcarsExtensions.rgbColor(190, 78, 134);
      return _rgbColor;
    }
  }.apply();
  
  public static Color rgbColor(final int red, final int green, final int blue) {
    double _divide = (red / 255.0);
    double _divide_1 = (green / 255.0);
    double _divide_2 = (blue / 255.0);
    Color _color = new Color(_divide, _divide_1, _divide_2, 1);
    return _color;
  }
}
