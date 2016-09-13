package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class InitializingListener<T extends Object> implements ChangeListener<T> {
  @Accessors
  private Procedure1<? super T> set;
  
  @Accessors
  private Procedure1<? super T> unset;
  
  @Override
  public void changed(final ObservableValue<? extends T> value, final T oldValue, final T newValue) {
    if (((!Objects.equal(this.unset, null)) && (!Objects.equal(oldValue, null)))) {
      this.unset.apply(oldValue);
    }
    if (((!Objects.equal(this.set, null)) && (!Objects.equal(newValue, null)))) {
      this.set.apply(newValue);
    }
  }
  
  @Pure
  public Procedure1<? super T> getSet() {
    return this.set;
  }
  
  public void setSet(final Procedure1<? super T> set) {
    this.set = set;
  }
  
  @Pure
  public Procedure1<? super T> getUnset() {
    return this.unset;
  }
  
  public void setUnset(final Procedure1<? super T> unset) {
    this.unset = unset;
  }
}
