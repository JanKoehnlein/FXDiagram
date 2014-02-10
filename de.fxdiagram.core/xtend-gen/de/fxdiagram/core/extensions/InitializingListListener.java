package de.fxdiagram.core.extensions;

import java.util.List;
import javafx.collections.ListChangeListener;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class InitializingListListener<T extends Object> implements ListChangeListener<T> {
  private Procedure1<? super T> _add;
  
  public Procedure1<? super T> getAdd() {
    return this._add;
  }
  
  public void setAdd(final Procedure1<? super T> add) {
    this._add = add;
  }
  
  private Procedure1<? super T> _remove;
  
  public Procedure1<? super T> getRemove() {
    return this._remove;
  }
  
  public void setRemove(final Procedure1<? super T> remove) {
    this._remove = remove;
  }
  
  public void onChanged(final ListChangeListener.Change<? extends T> c) {
    boolean _next = c.next();
    boolean _while = _next;
    while (_while) {
      boolean _wasAdded = c.wasAdded();
      if (_wasAdded) {
        List<? extends T> _addedSubList = c.getAddedSubList();
        final Procedure1<T> _function = new Procedure1<T>() {
          public void apply(final T it) {
            Procedure1<? super T> _add = InitializingListListener.this.getAdd();
            _add.apply(it);
          }
        };
        IterableExtensions.forEach(_addedSubList, _function);
      }
      boolean _next_1 = c.next();
      _while = _next_1;
    }
    boolean _wasRemoved = c.wasRemoved();
    if (_wasRemoved) {
      List<? extends T> _removed = c.getRemoved();
      final Procedure1<T> _function = new Procedure1<T>() {
        public void apply(final T it) {
          Procedure1<? super T> _remove = InitializingListListener.this.getRemove();
          _remove.apply(it);
        }
      };
      IterableExtensions.forEach(_removed, _function);
    }
  }
}
