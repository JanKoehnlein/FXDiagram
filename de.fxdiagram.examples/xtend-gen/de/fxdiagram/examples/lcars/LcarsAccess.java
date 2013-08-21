package de.fxdiagram.examples.lcars;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LcarsAccess {
  private Mongo mongo;
  
  private DB db;
  
  private DBCollection lcars;
  
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
}
