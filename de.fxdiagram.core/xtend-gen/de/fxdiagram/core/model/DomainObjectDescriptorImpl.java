package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import java.util.NoSuchElementException;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

/**
 * Base implementation of a {@link DomainObjectDescriptor} that can be serialized and
 * uses its {@link DomainObjectProvider} to recover the domain object and execute a
 * lambda expression on it.
 */
@ModelNode({ "id", "name", "provider" })
@SuppressWarnings("all")
public abstract class DomainObjectDescriptorImpl<T extends Object> implements DomainObjectDescriptor {
  public DomainObjectDescriptorImpl() {
  }
  
  public DomainObjectDescriptorImpl(final String id, final String name, final DomainObjectProvider provider) {
    this.idProperty.set(id);
    this.nameProperty.set(name);
    this.providerProperty.set(provider);
  }
  
  @Override
  public boolean equals(final Object obj) {
    boolean _and = false;
    boolean _and_1 = false;
    boolean _notEquals = (!Objects.equal(obj, null));
    if (!_notEquals) {
      _and_1 = false;
    } else {
      Class<? extends DomainObjectDescriptorImpl> _class = this.getClass();
      Class<?> _class_1 = obj.getClass();
      boolean _equals = Objects.equal(_class, _class_1);
      _and_1 = _equals;
    }
    if (!_and_1) {
      _and = false;
    } else {
      String _id = this.getId();
      String _id_1 = ((DomainObjectDescriptor) obj).getId();
      boolean _equals_1 = _id.equals(_id_1);
      _and = _equals_1;
    }
    return _and;
  }
  
  @Override
  public int hashCode() {
    String _id = this.getId();
    return _id.hashCode();
  }
  
  /**
   * Recover the domain object and execute the lambda expression on it.
   * 
   * @throws {@link NoSuchElementException} if the object cannot be recovered.
   */
  public abstract <U extends Object> U withDomainObject(final Function1<? super T, ? extends U> lambda);
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(idProperty, String.class);
    modelElement.addProperty(nameProperty, String.class);
    modelElement.addProperty(providerProperty, DomainObjectProvider.class);
  }
  
  private ReadOnlyStringWrapper idProperty = new ReadOnlyStringWrapper(this, "id");
  
  public String getId() {
    return this.idProperty.get();
  }
  
  public ReadOnlyStringProperty idProperty() {
    return this.idProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper nameProperty = new ReadOnlyStringWrapper(this, "name");
  
  public String getName() {
    return this.nameProperty.get();
  }
  
  public ReadOnlyStringProperty nameProperty() {
    return this.nameProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyObjectWrapper<DomainObjectProvider> providerProperty = new ReadOnlyObjectWrapper<DomainObjectProvider>(this, "provider");
  
  public DomainObjectProvider getProvider() {
    return this.providerProperty.get();
  }
  
  public ReadOnlyObjectProperty<DomainObjectProvider> providerProperty() {
    return this.providerProperty.getReadOnlyProperty();
  }
}
