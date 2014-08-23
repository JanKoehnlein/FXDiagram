package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import javafx.collections.MapChangeListener;
import org.eclipse.xtend.lib.Property;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class InitializingMapListener<T extends Object, U extends Object> implements MapChangeListener<T, U> {
  @Property
  private Procedure2<? super T, ? super U> _put;
  
  @Property
  private Procedure2<? super T, ? super U> _remove;
  
  public void onChanged(final MapChangeListener.Change<? extends T, ? extends U> c) {
    boolean _and = false;
    Procedure2<? super T, ? super U> _put = this.getPut();
    boolean _notEquals = (!Objects.equal(_put, null));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _wasAdded = c.wasAdded();
      _and = _wasAdded;
    }
    if (_and) {
      Procedure2<? super T, ? super U> _put_1 = this.getPut();
      T _key = c.getKey();
      U _valueAdded = c.getValueAdded();
      _put_1.apply(_key, _valueAdded);
    }
    boolean _and_1 = false;
    Procedure2<? super T, ? super U> _remove = this.getRemove();
    boolean _notEquals_1 = (!Objects.equal(_remove, null));
    if (!_notEquals_1) {
      _and_1 = false;
    } else {
      boolean _wasRemoved = c.wasRemoved();
      _and_1 = _wasRemoved;
    }
    if (_and_1) {
      Procedure2<? super T, ? super U> _remove_1 = this.getRemove();
      T _key_1 = c.getKey();
      U _valueRemoved = c.getValueRemoved();
      _remove_1.apply(_key_1, _valueRemoved);
    }
  }
  
  @Pure
  public Procedure2<? super T, ? super U> getPut() {
    return this._put;
  }
  
  public void setPut(final Procedure2<? super T, ? super U> put) {
    this._put = put;
  }
  
  @Pure
  public Procedure2<? super T, ? super U> getRemove() {
    return this._remove;
  }
  
  public void setRemove(final Procedure2<? super T, ? super U> remove) {
    this._remove = remove;
  }
}
