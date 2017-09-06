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
      this.remove.apply(c.getKey(), c.getValueRemoved());
    }
    if (((!Objects.equal(this.put, null)) && c.wasAdded())) {
      this.put.apply(c.getKey(), c.getValueAdded());
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
