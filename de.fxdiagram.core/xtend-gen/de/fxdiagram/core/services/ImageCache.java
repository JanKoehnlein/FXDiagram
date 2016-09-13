package de.fxdiagram.core.services;

import com.google.common.base.Objects;
import de.fxdiagram.core.extensions.ClassLoaderExtensions;
import java.io.ByteArrayInputStream;
import java.util.Map;
import javafx.scene.image.Image;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class ImageCache {
  private Map<String, Image> cache = CollectionLiterals.<String, Image>newHashMap();
  
  private static ImageCache INSTANCE;
  
  public static ImageCache get() {
    ImageCache _elvis = null;
    if (ImageCache.INSTANCE != null) {
      _elvis = ImageCache.INSTANCE;
    } else {
      ImageCache _imageCache = new ImageCache();
      ImageCache _INSTANCE = (ImageCache.INSTANCE = _imageCache);
      _elvis = _INSTANCE;
    }
    return _elvis;
  }
  
  public Image getImage(final Object context, final String file) {
    Image _xblockexpression = null;
    {
      final Image cachedImage = this.cache.get(file);
      Image _xifexpression = null;
      boolean _notEquals = (!Objects.equal(cachedImage, null));
      if (_notEquals) {
        _xifexpression = cachedImage;
      } else {
        Image _xblockexpression_1 = null;
        {
          String _uRI = ClassLoaderExtensions.toURI(context, file);
          final Image image = new Image(_uRI);
          this.put(file, image);
          _xblockexpression_1 = image;
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public Image getImage(final String file, final Function0<? extends byte[]> dataAccess) {
    Image _xblockexpression = null;
    {
      final Image cachedImage = this.cache.get(file);
      Image _xifexpression = null;
      boolean _notEquals = (!Objects.equal(cachedImage, null));
      if (_notEquals) {
        _xifexpression = cachedImage;
      } else {
        Image _xblockexpression_1 = null;
        {
          byte[] _apply = dataAccess.apply();
          ByteArrayInputStream _byteArrayInputStream = new ByteArrayInputStream(_apply);
          final Image image = new Image(_byteArrayInputStream);
          this.put(file, image);
          _xblockexpression_1 = image;
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public boolean containsImage(final String file) {
    return this.cache.containsKey(file);
  }
  
  public Image put(final String key, final Image image) {
    return this.cache.put(key, image);
  }
}
