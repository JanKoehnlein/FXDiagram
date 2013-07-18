package de.fxdiagram.examples.lcars;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LcarsAccess {
  private Mongo mongo;
  
  private DB db;
  
  private DBCollection lcars;
  
  private Map<String,Image> imageCache = new Function0<Map<String,Image>>() {
    public Map<String,Image> apply() {
      HashMap<String,Image> _newHashMap = CollectionLiterals.<String, Image>newHashMap();
      return _newHashMap;
    }
  }.apply();
  
  private static LcarsAccess INSTANCE;
  
  public static LcarsAccess get() {
    LcarsAccess _elvis = null;
    if (LcarsAccess.INSTANCE != null) {
      _elvis = LcarsAccess.INSTANCE;
    } else {
      LcarsAccess _lcarsAccess = new LcarsAccess();
      LcarsAccess _INSTANCE = LcarsAccess.INSTANCE = _lcarsAccess;
      _elvis = ObjectExtensions.<LcarsAccess>operator_elvis(
        LcarsAccess.INSTANCE, _INSTANCE);
    }
    return _elvis;
  }
  
  public LcarsAccess() {
    try {
      Mongo _mongo = new Mongo();
      this.mongo = _mongo;
      DB _dB = this.mongo.getDB("startrek");
      this.db = _dB;
      DBCollection _collection = this.db.getCollection("lcars");
      this.lcars = _collection;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public List<DBObject> query(final String fieldName, final Object fieldValue) {
    BasicDBObject _basicDBObject = new BasicDBObject();
    final Procedure1<BasicDBObject> _function = new Procedure1<BasicDBObject>() {
        public void apply(final BasicDBObject it) {
          it.put(fieldName, fieldValue);
        }
      };
    BasicDBObject _doubleArrow = ObjectExtensions.<BasicDBObject>operator_doubleArrow(_basicDBObject, _function);
    DBCursor _find = this.lcars.find(_doubleArrow);
    List<DBObject> _list = IterableExtensions.<DBObject>toList(_find);
    return _list;
  }
  
  public Image getImage(final String url, final byte[] data) {
    Image _xifexpression = null;
    boolean _containsKey = this.imageCache.containsKey(url);
    if (_containsKey) {
      Image _get = this.imageCache.get(url);
      _xifexpression = _get;
    } else {
      Image _xblockexpression = null;
      {
        ByteArrayInputStream _byteArrayInputStream = new ByteArrayInputStream(data);
        Image _image = new Image(_byteArrayInputStream);
        final Image image = _image;
        this.imageCache.put(url, image);
        _xblockexpression = (image);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
}
