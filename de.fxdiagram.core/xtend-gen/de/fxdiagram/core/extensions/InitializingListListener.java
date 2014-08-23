package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import java.util.List;
import javafx.collections.ListChangeListener;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class InitializingListListener<T extends Object> implements ListChangeListener<T> {
  @Accessors
  private Procedure1<? super ListChangeListener.Change<? extends T>> change;
  
  @Accessors
  private Procedure1<? super T> add;
  
  @Accessors
  private Procedure1<? super T> remove;
  
  public void onChanged(final ListChangeListener.Change<? extends T> c) {
    boolean _notEquals = (!Objects.equal(this.change, null));
    if (_notEquals) {
      this.change.apply(c);
    }
    while (c.next()) {
      boolean _and = false;
      boolean _notEquals_1 = (!Objects.equal(this.add, null));
      if (!_notEquals_1) {
        _and = false;
      } else {
        boolean _wasAdded = c.wasAdded();
        _and = _wasAdded;
      }
      if (_and) {
        List<? extends T> _addedSubList = c.getAddedSubList();
        final Procedure1<T> _function = new Procedure1<T>() {
          public void apply(final T it) {
            InitializingListListener.this.add.apply(it);
          }
        };
        IterableExtensions.forEach(_addedSubList, _function);
      }
    }
    boolean _and = false;
    boolean _notEquals_1 = (!Objects.equal(this.remove, null));
    if (!_notEquals_1) {
      _and = false;
    } else {
      boolean _wasRemoved = c.wasRemoved();
      _and = _wasRemoved;
    }
    if (_and) {
      List<? extends T> _removed = c.getRemoved();
      final Procedure1<T> _function = new Procedure1<T>() {
        public void apply(final T it) {
          InitializingListListener.this.remove.apply(it);
        }
      };
      IterableExtensions.forEach(_removed, _function);
    }
  }
  
  @Pure
  public Procedure1<? super ListChangeListener.Change<? extends T>> getChange() {
    return this.change;
  }
  
  public void setChange(final Procedure1<? super ListChangeListener.Change<? extends T>> change) {
    this.change = change;
  }
  
  @Pure
  public Procedure1<? super T> getAdd() {
    return this.add;
  }
  
  public void setAdd(final Procedure1<? super T> add) {
    this.add = add;
  }
  
  @Pure
  public Procedure1<? super T> getRemove() {
    return this.remove;
  }
  
  public void setRemove(final Procedure1<? super T> remove) {
    this.remove = remove;
  }
}
