package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.eclipse.xtend.lib.Property;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class InitializingListener<T extends Object> implements ChangeListener<T> {
  @Property
  private Procedure1<? super T> _set;
  
  @Property
  private Procedure1<? super T> _unset;
  
  public void changed(final ObservableValue<? extends T> value, final T oldValue, final T newValue) {
    boolean _and = false;
    Procedure1<? super T> _unset = this.getUnset();
    boolean _notEquals = (!Objects.equal(_unset, null));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _notEquals_1 = (!Objects.equal(oldValue, null));
      _and = _notEquals_1;
    }
    if (_and) {
      Procedure1<? super T> _unset_1 = this.getUnset();
      _unset_1.apply(oldValue);
    }
    boolean _and_1 = false;
    Procedure1<? super T> _set = this.getSet();
    boolean _notEquals_2 = (!Objects.equal(_set, null));
    if (!_notEquals_2) {
      _and_1 = false;
    } else {
      boolean _notEquals_3 = (!Objects.equal(newValue, null));
      _and_1 = _notEquals_3;
    }
    if (_and_1) {
      Procedure1<? super T> _set_1 = this.getSet();
      _set_1.apply(newValue);
    }
  }
  
  @Pure
  public Procedure1<? super T> getSet() {
    return this._set;
  }
  
  public void setSet(final Procedure1<? super T> set) {
    this._set = set;
  }
  
  @Pure
  public Procedure1<? super T> getUnset() {
    return this._unset;
  }
  
  public void setUnset(final Procedure1<? super T> unset) {
    this._unset = unset;
  }
}
