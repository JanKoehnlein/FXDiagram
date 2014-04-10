package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.XModelProvider;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@ModelNode({ "mappings" })
@SuppressWarnings("all")
public class AbstractDiagramConfig implements XDiagramConfig, XModelProvider {
  public <T extends Object> List<? extends AbstractMapping<T>> getMappings(final T domainObject) {
    ObservableList<AbstractMapping<?>> _mappings = this.getMappings();
    final Function1<AbstractMapping<?>,Boolean> _function = new Function1<AbstractMapping<?>,Boolean>() {
      public Boolean apply(final AbstractMapping<?> it) {
        return Boolean.valueOf(it.isApplicable(domainObject));
      }
    };
    Iterable<AbstractMapping<?>> _filter = IterableExtensions.<AbstractMapping<?>>filter(_mappings, _function);
    final Function1<AbstractMapping<?>,AbstractMapping<T>> _function_1 = new Function1<AbstractMapping<?>,AbstractMapping<T>>() {
      public AbstractMapping<T> apply(final AbstractMapping<?> it) {
        return ((AbstractMapping<T>) it);
      }
    };
    Iterable<AbstractMapping<T>> _map = IterableExtensions.<AbstractMapping<?>, AbstractMapping<T>>map(_filter, _function_1);
    return IterableExtensions.<AbstractMapping<T>>toList(_map);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(mappingsProperty, AbstractMapping.class);
  }
  
  private SimpleListProperty<AbstractMapping<?>> mappingsProperty = new SimpleListProperty<AbstractMapping<?>>(this, "mappings",_initMappings());
  
  private static final ObservableList<AbstractMapping<?>> _initMappings() {
    ObservableList<AbstractMapping<?>> _observableArrayList = FXCollections.<AbstractMapping<?>>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<AbstractMapping<?>> getMappings() {
    return this.mappingsProperty.get();
  }
  
  public ListProperty<AbstractMapping<?>> mappingsProperty() {
    return this.mappingsProperty;
  }
}
