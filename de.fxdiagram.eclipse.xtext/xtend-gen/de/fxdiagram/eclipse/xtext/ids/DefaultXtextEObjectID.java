package de.fxdiagram.eclipse.xtext.ids;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.xtext.ids.AbstractXtextEObjectID;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@ModelNode("nameSegments")
@SuppressWarnings("all")
public class DefaultXtextEObjectID extends AbstractXtextEObjectID {
  private QualifiedName qualifiedName;
  
  public DefaultXtextEObjectID(final QualifiedName qualifiedName, final EClass eClass, final URI elementURI) {
    super(eClass, elementURI);
    this.qualifiedName = qualifiedName;
    List<String> _segments = qualifiedName.getSegments();
    this.nameSegmentsProperty.setAll(_segments);
  }
  
  @Override
  public QualifiedName getQualifiedName() {
    QualifiedName _elvis = null;
    if (this.qualifiedName != null) {
      _elvis = this.qualifiedName;
    } else {
      QualifiedName _xifexpression = null;
      ObservableList<String> _nameSegments = this.getNameSegments();
      boolean _isEmpty = _nameSegments.isEmpty();
      if (_isEmpty) {
        _xifexpression = null;
      } else {
        ObservableList<String> _nameSegments_1 = this.getNameSegments();
        QualifiedName _create = QualifiedName.create(_nameSegments_1);
        _xifexpression = this.qualifiedName = _create;
      }
      _elvis = _xifexpression;
    }
    return _elvis;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if ((obj instanceof DefaultXtextEObjectID)) {
      boolean _and = false;
      boolean _equals = super.equals(obj);
      if (!_equals) {
        _and = false;
      } else {
        boolean _equals_1 = Objects.equal(((DefaultXtextEObjectID)obj).qualifiedName, this.qualifiedName);
        _and = _equals_1;
      }
      return _and;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    int _hashCode = super.hashCode();
    QualifiedName _qualifiedName = this.getQualifiedName();
    int _hashCode_1 = _qualifiedName.hashCode();
    int _multiply = (_hashCode_1 * 101);
    return (_hashCode + _multiply);
  }
  
  @Override
  public String toString() {
    String _string = this.qualifiedName.toString();
    String _plus = (_string + "||");
    String _uriAsString = this.getUriAsString();
    return (_plus + _uriAsString);
  }
  
  @Override
  public EObject resolve(final ResourceSet resourceSet) {
    URI _uRI = this.getURI();
    final URI resourceURI = _uRI.trimFragment();
    final Resource resource = resourceSet.getResource(resourceURI, true);
    boolean _equals = Objects.equal(resource, null);
    if (_equals) {
      throw new NoSuchElementException(("Cannot load resource " + resourceURI));
    }
    IResourceServiceProvider _resourceServiceProvider = this.getResourceServiceProvider();
    IResourceDescription.Manager _resourceDescriptionManager = _resourceServiceProvider.getResourceDescriptionManager();
    final IResourceDescription resourceDescription = _resourceDescriptionManager.getResourceDescription(resource);
    EClass _eClass = this.getEClass();
    QualifiedName _qualifiedName = this.getQualifiedName();
    final Iterable<IEObjectDescription> eObjectDescriptions = resourceDescription.getExportedObjects(_eClass, _qualifiedName, false);
    int _size = IterableExtensions.size(eObjectDescriptions);
    boolean _notEquals = (_size != 1);
    if (_notEquals) {
      int _size_1 = IterableExtensions.size(eObjectDescriptions);
      String _plus = ("Expected a single element but got " + Integer.valueOf(_size_1));
      throw new NoSuchElementException(_plus);
    }
    final IEObjectDescription eObjectDescription = IterableExtensions.<IEObjectDescription>head(eObjectDescriptions);
    EObject _eObjectOrProxy = eObjectDescription.getEObjectOrProxy();
    final EObject element = EcoreUtil.resolve(_eObjectOrProxy, resource);
    boolean _or = false;
    boolean _equals_1 = Objects.equal(element, null);
    if (_equals_1) {
      _or = true;
    } else {
      boolean _eIsProxy = element.eIsProxy();
      _or = _eIsProxy;
    }
    if (_or) {
      URI _eObjectURI = eObjectDescription.getEObjectURI();
      String _plus_1 = ("Cannot resolve element " + _eObjectURI);
      throw new NoSuchElementException(_plus_1);
    }
    return element;
  }
  
  @Override
  public IEObjectDescription findInIndex(final IResourceDescriptions index) {
    IEObjectDescription _xblockexpression = null;
    {
      URI _uRI = this.getURI();
      URI _trimFragment = _uRI.trimFragment();
      final IResourceDescription resourceDescription = index.getResourceDescription(_trimFragment);
      EClass _eClass = this.getEClass();
      Iterable<IEObjectDescription> _exportedObjects = resourceDescription.getExportedObjects(_eClass, this.qualifiedName, false);
      _xblockexpression = IterableExtensions.<IEObjectDescription>head(_exportedObjects);
    }
    return _xblockexpression;
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public DefaultXtextEObjectID() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(nameSegmentsProperty, String.class);
  }
  
  private ReadOnlyListWrapper<String> nameSegmentsProperty = new ReadOnlyListWrapper<String>(this, "nameSegments",_initNameSegments());
  
  private static final ObservableList<String> _initNameSegments() {
    ObservableList<String> _observableArrayList = FXCollections.<String>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<String> getNameSegments() {
    return this.nameSegmentsProperty.get();
  }
  
  public ReadOnlyListProperty<String> nameSegmentsProperty() {
    return this.nameSegmentsProperty.getReadOnlyProperty();
  }
}
