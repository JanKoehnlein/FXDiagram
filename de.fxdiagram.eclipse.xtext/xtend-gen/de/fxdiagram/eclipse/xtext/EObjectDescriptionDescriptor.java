package de.fxdiagram.eclipse.xtext;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import de.fxdiagram.mapping.AbstractMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import java.util.Map;
import java.util.NoSuchElementException;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.ui.refactoring.impl.ProjectUtil;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@ModelNode("elementID")
@SuppressWarnings("all")
public class EObjectDescriptionDescriptor extends AbstractMappedElementDescriptor<IEObjectDescription> {
  public EObjectDescriptionDescriptor(final XtextEObjectID elementID, final String mappingConfigId, final String mappingId, final XtextDomainObjectProvider provider) {
    super(mappingConfigId, mappingId, provider);
    this.elementIDProperty.set(elementID);
  }
  
  @Override
  public <U extends Object> U withDomainObject(final Function1<? super IEObjectDescription, ? extends U> lambda) {
    U _xblockexpression = null;
    {
      XtextEObjectID _elementID = this.getElementID();
      final IResourceServiceProvider rsp = _elementID.getResourceServiceProvider();
      ProjectUtil _get = rsp.<ProjectUtil>get(ProjectUtil.class);
      XtextEObjectID _elementID_1 = this.getElementID();
      URI _uRI = _elementID_1.getURI();
      final IProject project = _get.getProject(_uRI);
      boolean _equals = Objects.equal(project, null);
      if (_equals) {
        XtextEObjectID _elementID_2 = this.getElementID();
        URI _uRI_1 = _elementID_2.getURI();
        String _plus = ("Project " + _uRI_1);
        String _plus_1 = (_plus + " does not exist");
        throw new NoSuchElementException(_plus_1);
      }
      IResourceSetProvider _get_1 = rsp.<IResourceSetProvider>get(IResourceSetProvider.class);
      final ResourceSet resourceSet = _get_1.get(project);
      Map<Object, Object> _loadOptions = resourceSet.getLoadOptions();
      _loadOptions.put(ResourceDescriptionsProvider.PERSISTED_DESCRIPTIONS, Boolean.valueOf(true));
      ResourceDescriptionsProvider _get_2 = rsp.<ResourceDescriptionsProvider>get(ResourceDescriptionsProvider.class);
      final IResourceDescriptions index = _get_2.getResourceDescriptions(resourceSet);
      XtextEObjectID _elementID_3 = this.getElementID();
      EClass _eClass = _elementID_3.getEClass();
      XtextEObjectID _elementID_4 = this.getElementID();
      QualifiedName _qualifiedName = _elementID_4.getQualifiedName();
      Iterable<IEObjectDescription> _exportedObjects = index.getExportedObjects(_eClass, _qualifiedName, false);
      final Function1<IEObjectDescription, Boolean> _function = (IEObjectDescription it) -> {
        URI _eObjectURI = it.getEObjectURI();
        XtextEObjectID _elementID_5 = this.getElementID();
        URI _uRI_2 = _elementID_5.getURI();
        return Boolean.valueOf(Objects.equal(_eObjectURI, _uRI_2));
      };
      final IEObjectDescription description = IterableExtensions.<IEObjectDescription>findFirst(_exportedObjects, _function);
      boolean _equals_1 = Objects.equal(description, null);
      if (_equals_1) {
        XtextEObjectID _elementID_5 = this.getElementID();
        String _plus_2 = ("Element " + _elementID_5);
        String _plus_3 = (_plus_2 + " does not exist");
        throw new NoSuchElementException(_plus_3);
      }
      _xblockexpression = lambda.apply(description);
    }
    return _xblockexpression;
  }
  
  @Override
  public Object openInEditor(final boolean select) {
    IMappedElementDescriptorProvider _provider = this.getProvider();
    XtextEObjectID _elementID = this.getElementID();
    return ((XtextDomainObjectProvider) _provider).getCachedEditor(_elementID, true, true);
  }
  
  @Override
  public String getName() {
    XtextEObjectID _elementID = this.getElementID();
    QualifiedName _qualifiedName = _elementID.getQualifiedName();
    return _qualifiedName.getLastSegment();
  }
  
  @Override
  public boolean equals(final Object obj) {
    if ((obj instanceof EObjectDescriptionDescriptor)) {
      boolean _and = false;
      boolean _and_1 = false;
      boolean _equals = super.equals(obj);
      if (!_equals) {
        _and_1 = false;
      } else {
        XtextEObjectID _elementID = ((EObjectDescriptionDescriptor)obj).getElementID();
        XtextEObjectID _elementID_1 = this.getElementID();
        boolean _equals_1 = Objects.equal(_elementID, _elementID_1);
        _and_1 = _equals_1;
      }
      if (!_and_1) {
        _and = false;
      } else {
        XtextEObjectID _elementID_2 = ((EObjectDescriptionDescriptor)obj).getElementID();
        URI _uRI = _elementID_2.getURI();
        XtextEObjectID _elementID_3 = this.getElementID();
        URI _uRI_1 = _elementID_3.getURI();
        boolean _equals_2 = Objects.equal(_uRI, _uRI_1);
        _and = _equals_2;
      }
      return _and;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    int _hashCode = super.hashCode();
    XtextEObjectID _elementID = this.getElementID();
    int _hashCode_1 = _elementID.hashCode();
    int _multiply = (131 * _hashCode_1);
    int _plus = (_hashCode + _multiply);
    XtextEObjectID _elementID_1 = this.getElementID();
    URI _uRI = _elementID_1.getURI();
    int _hashCode_2 = _uRI.hashCode();
    int _multiply_1 = (177 * _hashCode_2);
    return (_plus + _multiply_1);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public EObjectDescriptionDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(elementIDProperty, XtextEObjectID.class);
  }
  
  private ReadOnlyObjectWrapper<XtextEObjectID> elementIDProperty = new ReadOnlyObjectWrapper<XtextEObjectID>(this, "elementID");
  
  public XtextEObjectID getElementID() {
    return this.elementIDProperty.get();
  }
  
  public ReadOnlyObjectProperty<XtextEObjectID> elementIDProperty() {
    return this.elementIDProperty.getReadOnlyProperty();
  }
}
