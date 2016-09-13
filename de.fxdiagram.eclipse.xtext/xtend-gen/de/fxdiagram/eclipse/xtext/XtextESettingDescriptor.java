package de.fxdiagram.eclipse.xtext;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.eclipse.xtext.AbstractXtextDescriptor;
import de.fxdiagram.eclipse.xtext.ESetting;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import java.util.NoSuchElementException;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@ModelNode({ "sourceID", "targetID", "eReferenceURI", "index" })
@SuppressWarnings("all")
public class XtextESettingDescriptor<ECLASS extends EObject> extends AbstractXtextDescriptor<ESetting<ECLASS>> {
  private EReference eReference;
  
  public XtextESettingDescriptor(final XtextEObjectID sourceID, final XtextEObjectID targetID, final EReference reference, final int index, final String mappingConfigID, final String mappingID) {
    super(mappingConfigID, mappingID);
    this.sourceIDProperty.set(sourceID);
    this.targetIDProperty.set(targetID);
    this.eReference = reference;
    URI _uRI = EcoreUtil.getURI(reference);
    String _string = _uRI.toString();
    this.eReferenceURIProperty.set(_string);
    this.indexProperty.set(index);
  }
  
  @Override
  public <U extends Object> U withDomainObject(final Function1<? super ESetting<ECLASS>, ? extends U> lambda) {
    U _xblockexpression = null;
    {
      XtextDomainObjectProvider _provider = this.getProvider();
      XtextEObjectID _sourceID = this.getSourceID();
      final IEditorPart editor = _provider.getCachedEditor(_sourceID, false, false);
      U _xifexpression = null;
      if ((editor instanceof XtextEditor)) {
        IXtextDocument _document = ((XtextEditor)editor).getDocument();
        final IUnitOfWork<U, XtextResource> _function = (XtextResource it) -> {
          U _xblockexpression_1 = null;
          {
            XtextEObjectID _sourceID_1 = this.getSourceID();
            ResourceSet _resourceSet = it.getResourceSet();
            final EObject source = _sourceID_1.resolve(_resourceSet);
            XtextEObjectID _targetID = this.getTargetID();
            ResourceSet _resourceSet_1 = it.getResourceSet();
            final EObject storedTarget = _targetID.resolve(_resourceSet_1);
            EReference _eReference = this.getEReference();
            int _index = this.getIndex();
            final ESetting<ECLASS> setting = new ESetting<ECLASS>(((ECLASS) source), _eReference, _index);
            final EObject resolvedTarget = setting.getTarget();
            boolean _notEquals = (!Objects.equal(resolvedTarget, storedTarget));
            if (_notEquals) {
              throw new NoSuchElementException("Reference target has changed");
            }
            _xblockexpression_1 = lambda.apply(((ESetting<ECLASS>) setting));
          }
          return _xblockexpression_1;
        };
        _xifexpression = _document.<U>readOnly(_function);
      } else {
        XtextEObjectID _sourceID_1 = this.getSourceID();
        String _plus = ("Cannot open an Xtext editor for " + _sourceID_1);
        throw new NoSuchElementException(_plus);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Override
  public Object openInEditor(final boolean select) {
    XtextDomainObjectProvider _provider = this.getProvider();
    XtextEObjectID _sourceID = this.getSourceID();
    return _provider.getCachedEditor(_sourceID, select, select);
  }
  
  @Override
  public String getName() {
    XtextEObjectID _sourceID = this.getSourceID();
    QualifiedName _qualifiedName = _sourceID.getQualifiedName();
    String _lastSegment = _qualifiedName.getLastSegment();
    String _plus = (_lastSegment + "--");
    EReference _eReference = this.getEReference();
    String _name = _eReference.getName();
    String _plus_1 = (_plus + _name);
    String _plus_2 = (_plus_1 + "-->");
    XtextEObjectID _targetID = this.getTargetID();
    QualifiedName _qualifiedName_1 = _targetID.getQualifiedName();
    String _lastSegment_1 = _qualifiedName_1.getLastSegment();
    return (_plus_2 + _lastSegment_1);
  }
  
  public EReference getEReference() {
    EReference _elvis = null;
    if (this.eReference != null) {
      _elvis = this.eReference;
    } else {
      EReference _resolveReference = this.resolveReference();
      EReference _eReference = (this.eReference = _resolveReference);
      _elvis = _eReference;
    }
    return _elvis;
  }
  
  private EReference resolveReference() {
    String _eReferenceURI = this.getEReferenceURI();
    final URI uri = URI.createURI(_eReferenceURI);
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
      String _eReferenceURI_1 = this.getEReferenceURI();
      String _plus = ("Cannot resolve EReference " + _eReferenceURI_1);
      throw new IllegalArgumentException(_plus);
    }
  }
  
  @Override
  public boolean equals(final Object obj) {
    if ((obj instanceof XtextESettingDescriptor<?>)) {
      return ((((super.equals(obj) && Objects.equal(this.getSourceID(), ((XtextESettingDescriptor<?>)obj).getSourceID())) && Objects.equal(this.getTargetID(), ((XtextESettingDescriptor<?>)obj).getTargetID())) && Objects.equal(this.eReference, ((XtextESettingDescriptor<?>)obj).eReference)) && (this.getIndex() == ((XtextESettingDescriptor<?>)obj).getIndex()));
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    int _hashCode = super.hashCode();
    XtextEObjectID _sourceID = this.getSourceID();
    int _hashCode_1 = _sourceID.hashCode();
    int _multiply = (13 * _hashCode_1);
    int _plus = (_hashCode + _multiply);
    XtextEObjectID _targetID = this.getTargetID();
    int _hashCode_2 = _targetID.hashCode();
    int _multiply_1 = (23 * _hashCode_2);
    int _plus_1 = (_plus + _multiply_1);
    int _hashCode_3 = this.eReference.hashCode();
    int _multiply_2 = (31 * _hashCode_3);
    int _plus_2 = (_plus_1 + _multiply_2);
    int _index = this.getIndex();
    int _multiply_3 = (37 * _index);
    return (_plus_2 + _multiply_3);
  }
  
  @Override
  protected IResourceServiceProvider getResourceServiceProvider() {
    XtextEObjectID _sourceID = this.getSourceID();
    return _sourceID.getResourceServiceProvider();
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public XtextESettingDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(sourceIDProperty, XtextEObjectID.class);
    modelElement.addProperty(targetIDProperty, XtextEObjectID.class);
    modelElement.addProperty(eReferenceURIProperty, String.class);
    modelElement.addProperty(indexProperty, Integer.class);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private ReadOnlyObjectWrapper<XtextEObjectID> sourceIDProperty = new ReadOnlyObjectWrapper<XtextEObjectID>(this, "sourceID");
  
  public XtextEObjectID getSourceID() {
    return this.sourceIDProperty.get();
  }
  
  public ReadOnlyObjectProperty<XtextEObjectID> sourceIDProperty() {
    return this.sourceIDProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyObjectWrapper<XtextEObjectID> targetIDProperty = new ReadOnlyObjectWrapper<XtextEObjectID>(this, "targetID");
  
  public XtextEObjectID getTargetID() {
    return this.targetIDProperty.get();
  }
  
  public ReadOnlyObjectProperty<XtextEObjectID> targetIDProperty() {
    return this.targetIDProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper eReferenceURIProperty = new ReadOnlyStringWrapper(this, "eReferenceURI");
  
  public String getEReferenceURI() {
    return this.eReferenceURIProperty.get();
  }
  
  public ReadOnlyStringProperty eReferenceURIProperty() {
    return this.eReferenceURIProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyIntegerWrapper indexProperty = new ReadOnlyIntegerWrapper(this, "index");
  
  public int getIndex() {
    return this.indexProperty.get();
  }
  
  public ReadOnlyIntegerProperty indexProperty() {
    return this.indexProperty.getReadOnlyProperty();
  }
}
