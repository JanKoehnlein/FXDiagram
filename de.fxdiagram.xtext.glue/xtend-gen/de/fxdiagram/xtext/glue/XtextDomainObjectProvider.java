package de.fxdiagram.xtext.glue;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.xtext.glue.MappedEObjectHandle;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

@ModelNode({ "diagramConfigs" })
@SuppressWarnings("all")
public class XtextDomainObjectProvider implements DomainObjectProvider {
  public DomainObjectDescriptor createDescriptor(final Object it) {
    if ((it instanceof MappedEObjectHandle<?>)) {
      URI _uRI = ((MappedEObjectHandle<?>)it).getURI();
      String _string = _uRI.toString();
      String _fullyQualifiedName = ((MappedEObjectHandle<?>)it).getFullyQualifiedName();
      AbstractMapping<? extends EObject> _mapping = ((MappedEObjectHandle<?>)it).getMapping();
      return new XtextDomainObjectDescriptor(_string, _fullyQualifiedName, _mapping, this);
    }
    return null;
  }
  
  public <T extends Object, U extends EObject> XtextDomainObjectDescriptor<T> createDescriptor(final T domainObject, final AbstractMapping<?> mapping) {
    MappedEObjectHandle<U> _mappedEObjectHandle = new MappedEObjectHandle<U>(((U) domainObject), ((AbstractMapping<U>) mapping));
    DomainObjectDescriptor _createDescriptor = this.createDescriptor(_mappedEObjectHandle);
    return ((XtextDomainObjectDescriptor<T>) _createDescriptor);
  }
  
  public boolean addDiagramConfig(final XDiagramConfig config) {
    ObservableList<XDiagramConfig> _diagramConfigs = this.getDiagramConfigs();
    boolean _contains = _diagramConfigs.contains(config);
    boolean _not = (!_contains);
    if (_not) {
      ObservableList<XDiagramConfig> _diagramConfigs_1 = this.getDiagramConfigs();
      return _diagramConfigs_1.add(config);
    } else {
      return false;
    }
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(diagramConfigsProperty, XDiagramConfig.class);
  }
  
  private ReadOnlyListWrapper<XDiagramConfig> diagramConfigsProperty = new ReadOnlyListWrapper<XDiagramConfig>(this, "diagramConfigs",_initDiagramConfigs());
  
  private static final ObservableList<XDiagramConfig> _initDiagramConfigs() {
    ObservableList<XDiagramConfig> _observableArrayList = FXCollections.<XDiagramConfig>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XDiagramConfig> getDiagramConfigs() {
    return this.diagramConfigsProperty.get();
  }
  
  public ReadOnlyListProperty<XDiagramConfig> diagramConfigsProperty() {
    return this.diagramConfigsProperty.getReadOnlyProperty();
  }
}
