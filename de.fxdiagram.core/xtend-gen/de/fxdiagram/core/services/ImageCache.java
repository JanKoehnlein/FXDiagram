package de.fxdiagram.core.services;

import com.google.common.base.Objects;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;

@SuppressWarnings("all")
public class ImageCache {
  private Map<String,Image> cache = new Function0<Map<String,Image>>() {
    public Map<String,Image> apply() {
      HashMap<String,Image> _newHashMap = CollectionLiterals.<String, Image>newHashMap();
      return _newHashMap;
    }
  }.apply();
  
  private static ImageCache INSTANCE;
  
  public static ImageCache get() {
    ImageCache _elvis = null;
    if (ImageCache.INSTANCE != null) {
      _elvis = ImageCache.INSTANCE;
    } else {
      ImageCache _imageCache = new ImageCache();
      ImageCache _INSTANCE = ImageCache.INSTANCE = _imageCache;
      _elvis = ObjectExtensions.<ImageCache>operator_elvis(
        ImageCache.INSTANCE, _INSTANCE);
    }
    return _elvis;
  }
  
  public Image getImage(final String file) {
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
          Image _image = new Image(file);
          final Image image = _image;
          this.put(file, image);
          _xblockexpression_1 = (image);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
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
          Image _image = new Image(_byteArrayInputStream);
          final Image image = _image;
          this.put(file, image);
          _xblockexpression_1 = (image);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public boolean containsImage(final String file) {
    boolean _containsKey = this.cache.containsKey(file);
    return _containsKey;
  }
  
  public Image put(final String key, final Image image) {
    Image _put = this.cache.put(key, image);
    return _put;
  }
}
