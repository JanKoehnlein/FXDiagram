package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import de.fxdiagram.examples.lcars.LcarsDiagram;
import java.io.InputStream;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class LcarsExtensions {
  private static Map<Double,Font> cache = CollectionLiterals.<Double, Font>newHashMap();
  
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
  
  public final static Color ORANGE = LcarsExtensions.rgbColor(251, 134, 9);
  
  public final static Color DARKBLUE = LcarsExtensions.rgbColor(135, 132, 194);
  
  public final static Color FLESH = LcarsExtensions.rgbColor(253, 193, 137);
  
  public final static Color DARKFLESH = LcarsExtensions.rgbColor(251, 135, 84);
  
  public final static Color VIOLET = LcarsExtensions.rgbColor(190, 131, 192);
  
  public final static Color RED = LcarsExtensions.rgbColor(192, 80, 85);
  
  public final static Color BLUE = LcarsExtensions.rgbColor(136, 130, 254);
  
  public final static Color MAGENTA = LcarsExtensions.rgbColor(190, 78, 134);
  
  public static Color rgbColor(final int red, final int green, final int blue) {
    Color _color = new Color((red / 255.0), (green / 255.0), (blue / 255.0), 1);
    return _color;
  }
  
  public static LcarsDiagram getLcarsDiagram(final Node node) {
    LcarsDiagram _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (node instanceof LcarsDiagram) {
        _matched=true;
        _switchResult = ((LcarsDiagram)node);
      }
    }
    if (!_matched) {
      if (Objects.equal(node,null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      Parent _parent = node.getParent();
      LcarsDiagram _lcarsDiagram = LcarsExtensions.getLcarsDiagram(_parent);
      _switchResult = _lcarsDiagram;
    }
    return _switchResult;
  }
}
