package de.fxdiagram.xtext.glue.mapping;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import de.fxdiagram.xtext.glue.mapping.ESetting;
import de.fxdiagram.xtext.glue.mapping.XtextDomainObjectProvider;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@ModelNode({ "referenceURI", "index" })
@SuppressWarnings("all")
public class XtextESettingDescriptor<ECLASS extends EObject> extends AbstractXtextDescriptor<ESetting<ECLASS>> {
  private EReference reference;
  
  public XtextESettingDescriptor() {
  }
  
  public XtextESettingDescriptor(final String uri, final String fqn, final EReference reference, final int index, final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
    super(uri, fqn, mappingConfigID, mappingID, provider);
    this.reference = reference;
    URI _uRI = EcoreUtil.getURI(reference);
    String _string = _uRI.toString();
    this.referenceURIProperty.set(_string);
    this.indexProperty.set(index);
  }
  
  public EReference getReference() {
    EReference _elvis = null;
    if (this.reference != null) {
      _elvis = this.reference;
    } else {
      EReference _resolveReference = this.resolveReference();
      _elvis = (this.reference = _resolveReference);
    }
    return _elvis;
  }
  
  private EReference resolveReference() {
    String _referenceURI = this.getReferenceURI();
    final URI uri = URI.createURI(_referenceURI);
    URI _trimFragment = uri.trimFragment();
    String _string = _trimFragment.toString();
    final EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(_string);
    Resource _eResource = null;
    if (ePackage!=null) {
      _eResource=ePackage.eResource();
    }
    EObject _eObject = null;
    if (_eResource!=null) {
      String _fragment = uri.fragment();
      _eObject=_eResource.getEObject(_fragment);
    }
    final EObject reference = _eObject;
    if ((reference instanceof EReference)) {
      return ((EReference)reference);
    } else {
      String _referenceURI_1 = this.getReferenceURI();
      String _plus = ("Cannot resolve EReference " + _referenceURI_1);
      throw new IllegalArgumentException(_plus);
    }
  }
  
  public <T extends Object> T withDomainObject(final Function1<? super ESetting<ECLASS>, ? extends T> lambda) {
    T _xblockexpression = null;
    {
      String _uri = this.getUri();
      final URI uriAsURI = URI.createURI(_uri);
      final IEditorPart editor = this.openInEditor(false);
      T _xifexpression = null;
      if ((editor instanceof XtextEditor)) {
        IXtextDocument _document = ((XtextEditor)editor).getDocument();
        final IUnitOfWork<T, XtextResource> _function = new IUnitOfWork<T, XtextResource>() {
          public T exec(final XtextResource it) throws Exception {
            T _xblockexpression = null;
            {
              ResourceSet _resourceSet = it.getResourceSet();
              EObject _eObject = _resourceSet.getEObject(uriAsURI, true);
              final ECLASS owner = ((ECLASS) _eObject);
              int _index = XtextESettingDescriptor.this.getIndex();
              final ESetting<ECLASS> setting = new ESetting<ECLASS>(owner, XtextESettingDescriptor.this.reference, _index);
              _xblockexpression = lambda.apply(setting);
            }
            return _xblockexpression;
          }
        };
        _xifexpression = _document.<T>readOnly(_function);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public boolean equals(final Object obj) {
    boolean _xifexpression = false;
    if ((obj instanceof XtextESettingDescriptor<?>)) {
      boolean _and = false;
      boolean _and_1 = false;
      String _uri = ((XtextESettingDescriptor<?>)obj).getUri();
      String _uri_1 = this.getUri();
      boolean _equals = Objects.equal(_uri, _uri_1);
      if (!_equals) {
        _and_1 = false;
      } else {
        EReference _reference = this.getReference();
        EReference _reference_1 = ((XtextESettingDescriptor<?>)obj).getReference();
        boolean _equals_1 = Objects.equal(_reference, _reference_1);
        _and_1 = _equals_1;
      }
      if (!_and_1) {
        _and = false;
      } else {
        int _index = this.getIndex();
        int _index_1 = ((XtextESettingDescriptor<?>)obj).getIndex();
        boolean _equals_2 = (_index == _index_1);
        _and = _equals_2;
      }
      _xifexpression = _and;
    } else {
      _xifexpression = false;
    }
    return _xifexpression;
  }
  
  public int hashCode() {
    String _uri = this.getUri();
    int _hashCode = _uri.hashCode();
    int _multiply = (103 * _hashCode);
    EReference _reference = this.getReference();
    int _hashCode_1 = _reference.hashCode();
    int _multiply_1 = (37 * _hashCode_1);
    int _plus = (_multiply + _multiply_1);
    int _index = this.getIndex();
    int _multiply_2 = (11 * _index);
    return (_plus + _multiply_2);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(referenceURIProperty, String.class);
    modelElement.addProperty(indexProperty, Integer.class);
  }
  
  private ReadOnlyStringWrapper referenceURIProperty = new ReadOnlyStringWrapper(this, "referenceURI");
  
  public String getReferenceURI() {
    return this.referenceURIProperty.get();
  }
  
  public ReadOnlyStringProperty referenceURIProperty() {
    return this.referenceURIProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyIntegerWrapper indexProperty = new ReadOnlyIntegerWrapper(this, "index");
  
  public int getIndex() {
    return this.indexProperty.get();
  }
  
  public ReadOnlyIntegerProperty indexProperty() {
    return this.indexProperty.getReadOnlyProperty();
  }
}
