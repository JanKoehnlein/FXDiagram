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
  
  public void changed(final ObservableValue<? extends T> value, final T oldValue, final T newValue) {
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(this.unset, null));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _notEquals_1 = (!Objects.equal(oldValue, null));
      _and = _notEquals_1;
    }
    if (_and) {
      this.unset.apply(oldValue);
    }
    boolean _and_1 = false;
    boolean _notEquals_2 = (!Objects.equal(this.set, null));
    if (!_notEquals_2) {
      _and_1 = false;
    } else {
      boolean _notEquals_3 = (!Objects.equal(newValue, null));
      _and_1 = _notEquals_3;
    }
    if (_and_1) {
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
