package de.fxdiagram.core.extensions;

import javafx.collections.MapChangeListener;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class InitializingMapListener<T extends Object, U extends Object> implements MapChangeListener<T,U> {
  private Procedure2<? super T,? super U> _put;
  
  public Procedure2<? super T,? super U> getPut() {
    return this._put;
  }
  
  public void setPut(final Procedure2<? super T,? super U> put) {
    this._put = put;
  }
  
  private Procedure2<? super T,? super U> _remove;
  
  public Procedure2<? super T,? super U> getRemove() {
    return this._remove;
  }
  
  public void setRemove(final Procedure2<? super T,? super U> remove) {
    this._remove = remove;
  }
  
  public void onChanged(final MapChangeListener.Change<? extends T,? extends U> c) {
    boolean _wasAdded = c.wasAdded();
    if (_wasAdded) {
      Procedure2<? super T,? super U> _put = this.getPut();
      T _key = c.getKey();
      U _valueAdded = c.getValueAdded();
      _put.apply(_key, _valueAdded);
    }
    boolean _wasRemoved = c.wasRemoved();
    if (_wasRemoved) {
      Procedure2<? super T,? super U> _remove = this.getRemove();
      T _key_1 = c.getKey();
      U _valueRemoved = c.getValueRemoved();
      _remove.apply(_key_1, _valueRemoved);
    }
  }
}
