package de.fxdiagram.eclipse.xtext.ids;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.eclipse.xtext.ids.AbstractXtextEObjectID;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

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
      return (super.equals(obj) && Objects.equal(((DefaultXtextEObjectID)obj).getQualifiedName(), this.getQualifiedName()));
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
    boolean _greaterThan = (_size > 1);
    if (_greaterThan) {
      int _size_1 = IterableExtensions.size(eObjectDescriptions);
      String _plus = ("Expected a single element but got " + Integer.valueOf(_size_1));
      throw new NoSuchElementException(_plus);
    }
    boolean _isEmpty = IterableExtensions.isEmpty(eObjectDescriptions);
    if (_isEmpty) {
      if ((resource instanceof XtextResource)) {
        IResourceServiceProvider _resourceServiceProvider_1 = ((XtextResource) resource).getResourceServiceProvider();
        @Extension
        final IQualifiedNameProvider qualifiedNameProvider = _resourceServiceProvider_1.<IQualifiedNameProvider>get(IQualifiedNameProvider.class);
        TreeIterator<EObject> _allContents = ((XtextResource)resource).getAllContents();
        final Function1<EObject, Boolean> _function = (EObject it) -> {
          EClass _eClass_1 = it.eClass();
          EClass _eClass_2 = this.getEClass();
          return Boolean.valueOf(Objects.equal(_eClass_1, _eClass_2));
        };
        Iterator<EObject> _filter = IteratorExtensions.<EObject>filter(_allContents, _function);
        final Function1<EObject, Boolean> _function_1 = (EObject it) -> {
          QualifiedName _fullyQualifiedName = qualifiedNameProvider.getFullyQualifiedName(it);
          return Boolean.valueOf(Objects.equal(_fullyQualifiedName, this.qualifiedName));
        };
        final EObject elementByName = IteratorExtensions.<EObject>findFirst(_filter, _function_1);
        boolean _notEquals = (!Objects.equal(elementByName, null));
        if (_notEquals) {
          return elementByName;
        }
      }
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Cannot find element named ");
      _builder.append(this.qualifiedName, "");
      _builder.append(" of type ");
      EClass _eClass_1 = this.getEClass();
      String _name = _eClass_1.getName();
      _builder.append(_name, "");
      _builder.append(" in ");
      URI _uRI_1 = resource.getURI();
      _builder.append(_uRI_1, "");
      throw new NoSuchElementException(_builder.toString());
    }
    final IEObjectDescription eObjectDescription = IterableExtensions.<IEObjectDescription>head(eObjectDescriptions);
    EObject _eObjectOrProxy = eObjectDescription.getEObjectOrProxy();
    final EObject element = EcoreUtil.resolve(_eObjectOrProxy, resource);
    if ((Objects.equal(element, null) || element.eIsProxy())) {
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
      boolean _equals = Objects.equal(resourceDescription, null);
      if (_equals) {
        URI _uRI_1 = this.getURI();
        URI _trimFragment_1 = _uRI_1.trimFragment();
        String _plus = ("Resource " + _trimFragment_1);
        String _plus_1 = (_plus + " does not exist in index");
        throw new NoSuchElementException(_plus_1);
      }
      EClass _eClass = this.getEClass();
      QualifiedName _qualifiedName = this.getQualifiedName();
      Iterable<IEObjectDescription> _exportedObjects = resourceDescription.getExportedObjects(_eClass, _qualifiedName, false);
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
  
  public String toString() {
    return ToString.toString(this);
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
