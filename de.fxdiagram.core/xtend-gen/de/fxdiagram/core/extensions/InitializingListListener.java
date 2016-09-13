package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import java.util.List;
import java.util.function.Consumer;
import javafx.collections.ListChangeListener;
import org.eclipse.xtend.lib.annotations.Accessors;
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
  
  @Override
  public void onChanged(final ListChangeListener.Change<? extends T> c) {
    boolean _notEquals = (!Objects.equal(this.change, null));
    if (_notEquals) {
      this.change.apply(c);
    }
    while (c.next()) {
      if (((!Objects.equal(this.remove, null)) && c.wasRemoved())) {
        List<? extends T> _removed = c.getRemoved();
        final Consumer<T> _function = (T it) -> {
          this.remove.apply(it);
        };
        _removed.forEach(_function);
      }
    }
    if (((!Objects.equal(this.add, null)) && c.wasAdded())) {
      List<? extends T> _addedSubList = c.getAddedSubList();
      final Consumer<T> _function = (T it) -> {
        this.add.apply(it);
      };
      _addedSubList.forEach(_function);
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
