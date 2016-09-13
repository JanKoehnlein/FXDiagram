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
  
  @Override
  public void onChanged(final MapChangeListener.Change<? extends T, ? extends U> c) {
    if (((!Objects.equal(this.remove, null)) && c.wasRemoved())) {
      T _key = c.getKey();
      U _valueRemoved = c.getValueRemoved();
      this.remove.apply(_key, _valueRemoved);
    }
    if (((!Objects.equal(this.put, null)) && c.wasAdded())) {
      T _key_1 = c.getKey();
      U _valueAdded = c.getValueAdded();
      this.put.apply(_key_1, _valueAdded);
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
