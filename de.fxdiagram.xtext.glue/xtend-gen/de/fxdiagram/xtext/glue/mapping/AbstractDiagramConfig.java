package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.XModelProvider;
import de.fxdiagram.xtext.glue.mapping.BaseMapping;
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
  public <T extends Object> List<? extends BaseMapping<T>> getMappings(final T domainObject) {
    ObservableList<BaseMapping<?>> _mappings = this.getMappings();
    final Function1<BaseMapping<?>,Boolean> _function = new Function1<BaseMapping<?>,Boolean>() {
      public Boolean apply(final BaseMapping<?> it) {
        return Boolean.valueOf(it.isApplicable(domainObject));
      }
    };
    Iterable<BaseMapping<?>> _filter = IterableExtensions.<BaseMapping<?>>filter(_mappings, _function);
    final Function1<BaseMapping<?>,BaseMapping<T>> _function_1 = new Function1<BaseMapping<?>,BaseMapping<T>>() {
      public BaseMapping<T> apply(final BaseMapping<?> it) {
        return ((BaseMapping<T>) it);
      }
    };
    Iterable<BaseMapping<T>> _map = IterableExtensions.<BaseMapping<?>, BaseMapping<T>>map(_filter, _function_1);
    return IterableExtensions.<BaseMapping<T>>toList(_map);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(mappingsProperty, BaseMapping.class);
  }
  
  private SimpleListProperty<BaseMapping<?>> mappingsProperty = new SimpleListProperty<BaseMapping<?>>(this, "mappings",_initMappings());
  
  private static final ObservableList<BaseMapping<?>> _initMappings() {
    ObservableList<BaseMapping<?>> _observableArrayList = FXCollections.<BaseMapping<?>>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<BaseMapping<?>> getMappings() {
    return this.mappingsProperty.get();
  }
  
  public ListProperty<BaseMapping<?>> mappingsProperty() {
    return this.mappingsProperty;
  }
}
