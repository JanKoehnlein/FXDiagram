package de.fxdiagram.eclipse.xtext.ids;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.eclipse.xtext.ids.AbstractXtextEObjectID;
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
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
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
    this.nameSegmentsProperty.setAll(qualifiedName.getSegments());
  }
  
  @Override
  public QualifiedName getQualifiedName() {
    QualifiedName _elvis = null;
    if (this.qualifiedName != null) {
      _elvis = this.qualifiedName;
    } else {
      QualifiedName _xifexpression = null;
      boolean _isEmpty = this.getNameSegments().isEmpty();
      if (_isEmpty) {
        _xifexpression = null;
      } else {
        _xifexpression = this.qualifiedName = QualifiedName.create(this.getNameSegments());
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
    int _hashCode_1 = this.getQualifiedName().hashCode();
    int _multiply = (_hashCode_1 * 101);
    return (_hashCode + _multiply);
  }
  
  @Override
  public EObject resolve(final ResourceSet resourceSet) {
    final URI resourceURI = this.getURI().trimFragment();
    final Resource resource = resourceSet.getResource(resourceURI, true);
    if ((resource == null)) {
      throw new NoSuchElementException(("Cannot load resource " + resourceURI));
    }
    final IResourceDescription resourceDescription = this.getResourceServiceProvider().getResourceDescriptionManager().getResourceDescription(resource);
    final Iterable<IEObjectDescription> eObjectDescriptions = resourceDescription.getExportedObjects(this.getEClass(), this.getQualifiedName(), false);
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
        @Extension
        final IQualifiedNameProvider qualifiedNameProvider = ((XtextResource) resource).getResourceServiceProvider().<IQualifiedNameProvider>get(IQualifiedNameProvider.class);
        final Function1<EObject, Boolean> _function = (EObject it) -> {
          EClass _eClass = it.eClass();
          EClass _eClass_1 = this.getEClass();
          return Boolean.valueOf(Objects.equal(_eClass, _eClass_1));
        };
        final Function1<EObject, Boolean> _function_1 = (EObject it) -> {
          QualifiedName _fullyQualifiedName = qualifiedNameProvider.getFullyQualifiedName(it);
          return Boolean.valueOf(Objects.equal(_fullyQualifiedName, this.qualifiedName));
        };
        final EObject elementByName = IteratorExtensions.<EObject>findFirst(IteratorExtensions.<EObject>filter(((XtextResource)resource).getAllContents(), _function), _function_1);
        if ((elementByName != null)) {
          return elementByName;
        }
      }
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Cannot find element named ");
      _builder.append(this.qualifiedName);
      _builder.append(" of type ");
      String _name = this.getEClass().getName();
      _builder.append(_name);
      _builder.append(" in ");
      URI _uRI = resource.getURI();
      _builder.append(_uRI);
      throw new NoSuchElementException(_builder.toString());
    }
    final IEObjectDescription eObjectDescription = IterableExtensions.<IEObjectDescription>head(eObjectDescriptions);
    final EObject element = EcoreUtil.resolve(eObjectDescription.getEObjectOrProxy(), resource);
    if (((element == null) || element.eIsProxy())) {
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
      final IResourceDescription resourceDescription = index.getResourceDescription(this.getURI().trimFragment());
      if ((resourceDescription == null)) {
        URI _trimFragment = this.getURI().trimFragment();
        String _plus = ("Resource " + _trimFragment);
        String _plus_1 = (_plus + " does not exist in index");
        throw new NoSuchElementException(_plus_1);
      }
      _xblockexpression = IterableExtensions.<IEObjectDescription>head(resourceDescription.getExportedObjects(this.getEClass(), this.getQualifiedName(), false));
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
  
  public void postLoad() {
    
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
