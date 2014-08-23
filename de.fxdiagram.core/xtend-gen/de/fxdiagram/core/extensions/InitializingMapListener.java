package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import javafx.collections.MapChangeListener;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class InitializingMapListener<T extends Object, U extends Object> implements MapChangeListener<T, U> {
  @Accessors
  private Procedure2<? super T, ? super U> put;
  
  @Accessors
  private Procedure2<? super T, ? super U> remove;
  
  public void onChanged(final MapChangeListener.Change<? extends T, ? extends U> c) {
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(this.put, null));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _wasAdded = c.wasAdded();
      _and = _wasAdded;
    }
    if (_and) {
      T _key = c.getKey();
      U _valueAdded = c.getValueAdded();
      this.put.apply(_key, _valueAdded);
    }
    boolean _and_1 = false;
    boolean _notEquals_1 = (!Objects.equal(this.remove, null));
    if (!_notEquals_1) {
      _and_1 = false;
    } else {
      boolean _wasRemoved = c.wasRemoved();
      _and_1 = _wasRemoved;
    }
    if (_and_1) {
      T _key_1 = c.getKey();
      U _valueRemoved = c.getValueRemoved();
      this.remove.apply(_key_1, _valueRemoved);
    }
  }
  
  @Pure
  public Procedure2<? super T, ? super U> getPut() {
    return this.put;
  }
  
  public void setPut(final Procedure2<? super T, ? super U> put) {
    this.put = put;
  }
  
  @Pure
  public Procedure2<? super T, ? super U> getRemove() {
    return this.remove;
  }
  
  public void setRemove(final Procedure2<? super T, ? super U> remove) {
    this.remove = remove;
  }
}
