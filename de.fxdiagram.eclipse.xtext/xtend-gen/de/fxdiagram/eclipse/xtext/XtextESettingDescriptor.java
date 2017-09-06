package de.fxdiagram.eclipse.xtext;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.eclipse.xtext.AbstractXtextDescriptor;
import de.fxdiagram.eclipse.xtext.ESetting;
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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
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
    this.eReferenceURIProperty.set(EcoreUtil.getURI(reference).toString());
    this.indexProperty.set(index);
  }
  
  @Override
  public <U extends Object> U withDomainObject(final Function1<? super ESetting<ECLASS>, ? extends U> lambda) {
    U _xblockexpression = null;
    {
      final IEditorPart editor = this.getProvider().getCachedEditor(this.getSourceID(), false, false);
      U _xifexpression = null;
      if ((editor instanceof XtextEditor)) {
        final IUnitOfWork<U, XtextResource> _function = (XtextResource it) -> {
          U _xblockexpression_1 = null;
          {
            final EObject source = this.getSourceID().resolve(it.getResourceSet());
            final EObject storedTarget = this.getTargetID().resolve(it.getResourceSet());
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
        _xifexpression = ((XtextEditor)editor).getDocument().<U>readOnly(_function);
      } else {
        XtextEObjectID _sourceID = this.getSourceID();
        String _plus = ("Cannot open an Xtext editor for " + _sourceID);
        throw new NoSuchElementException(_plus);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Override
  public Object openInEditor(final boolean select) {
    return this.getProvider().getCachedEditor(this.getSourceID(), select, select);
  }
  
  @Override
  public String getName() {
    String _lastSegment = this.getSourceID().getQualifiedName().getLastSegment();
    String _plus = (_lastSegment + "--");
    String _name = this.getEReference().getName();
    String _plus_1 = (_plus + _name);
    String _plus_2 = (_plus_1 + "-->");
    String _lastSegment_1 = this.getTargetID().getQualifiedName().getLastSegment();
    return (_plus_2 + _lastSegment_1);
  }
  
  public EReference getEReference() {
    EReference _elvis = null;
    if (this.eReference != null) {
      _elvis = this.eReference;
    } else {
      _elvis = (this.eReference = this.resolveReference());
    }
    return _elvis;
  }
  
  private EReference resolveReference() {
    final URI uri = URI.createURI(this.getEReferenceURI());
    final EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(uri.trimFragment().toString());
    Resource _eResource = null;
    if (ePackage!=null) {
      _eResource=ePackage.eResource();
    }
    EObject _eObject = null;
    if (_eResource!=null) {
      _eObject=_eResource.getEObject(uri.fragment());
    }
    final EObject reference = _eObject;
    if ((reference instanceof EReference)) {
      return ((EReference)reference);
    } else {
      String _eReferenceURI = this.getEReferenceURI();
      String _plus = ("Cannot resolve EReference " + _eReferenceURI);
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
    int _hashCode_1 = this.getSourceID().hashCode();
    int _multiply = (13 * _hashCode_1);
    int _plus = (_hashCode + _multiply);
    int _hashCode_2 = this.getTargetID().hashCode();
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
    return this.getSourceID().getResourceServiceProvider();
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
  
  public void postLoad() {
    
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
