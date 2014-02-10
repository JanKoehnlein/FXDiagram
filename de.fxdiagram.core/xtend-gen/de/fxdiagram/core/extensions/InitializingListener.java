package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class InitializingListener<T extends Object> implements ChangeListener<T> {
  private Procedure1<? super T> _set;
  
  public Procedure1<? super T> getSet() {
    return this._set;
  }
  
  public void setSet(final Procedure1<? super T> set) {
    this._set = set;
  }
  
  private Procedure1<? super T> _unset;
  
  public Procedure1<? super T> getUnset() {
    return this._unset;
  }
  
  public void setUnset(final Procedure1<? super T> unset) {
    this._unset = unset;
  }
  
  public void changed(final ObservableValue<? extends T> value, final T oldValue, final T newValue) {
    boolean _notEquals = (!Objects.equal(oldValue, null));
    if (_notEquals) {
      Procedure1<? super T> _unset = this.getUnset();
      _unset.apply(oldValue);
    }
    boolean _notEquals_1 = (!Objects.equal(newValue, null));
    if (_notEquals_1) {
      Procedure1<? super T> _set = this.getSet();
      _set.apply(newValue);
    }
  }
}
