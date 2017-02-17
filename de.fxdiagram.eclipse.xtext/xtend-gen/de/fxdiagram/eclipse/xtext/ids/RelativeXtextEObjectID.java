package de.fxdiagram.eclipse.xtext.ids;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.xtext.ids.AbstractXtextEObjectID;
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import java.util.NoSuchElementException;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@ModelNode({ "parentID", "relativeFragment" })
@SuppressWarnings("all")
public class RelativeXtextEObjectID extends AbstractXtextEObjectID {
  public RelativeXtextEObjectID(final XtextEObjectID parentID, final EClass eClass, final URI elementURI) {
    super(eClass, elementURI);
    this.parentIDProperty.set(parentID);
    URI _uRI = this.getURI();
    final String fragment = _uRI.fragment();
    URI _uRI_1 = parentID.getURI();
    final String parentFragment = _uRI_1.fragment();
    String _xifexpression = null;
    boolean _startsWith = fragment.startsWith(parentFragment);
    if (_startsWith) {
      int _length = parentFragment.length();
      _xifexpression = fragment.substring(_length);
    } else {
      _xifexpression = ("#" + fragment);
    }
    final String relativeFragment = _xifexpression;
    this.relativeFragmentProperty.set(relativeFragment);
  }
  
  @Override
  public QualifiedName getQualifiedName() {
    return null;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if ((obj instanceof RelativeXtextEObjectID)) {
      return ((super.equals(obj) && Objects.equal(((RelativeXtextEObjectID)obj).getParentID(), this.getParentID())) && Objects.equal(((RelativeXtextEObjectID)obj).getRelativeFragment(), this.getRelativeFragment()));
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    XtextEObjectID _parentID = this.getParentID();
    int _hashCode = _parentID.hashCode();
    int _multiply = (461 * _hashCode);
    String _relativeFragment = this.getRelativeFragment();
    int _hashCode_1 = _relativeFragment.hashCode();
    int _multiply_1 = (503 * _hashCode_1);
    int _plus = (_multiply + _multiply_1);
    int _hashCode_2 = super.hashCode();
    int _multiply_2 = (101 * _hashCode_2);
    return (_plus + _multiply_2);
  }
  
  @Override
  public String toString() {
    String _xblockexpression = null;
    {
      XtextEObjectID _parentID = this.getParentID();
      _parentID.toString();
      String _relativeFragment = this.getRelativeFragment();
      _xblockexpression = ("->" + _relativeFragment);
    }
    return _xblockexpression;
  }
  
  @Override
  public IEObjectDescription findInIndex(final IResourceDescriptions index) {
    IEObjectDescription _xblockexpression = null;
    {
      URI _uRI = this.getURI();
      URI _trimFragment = _uRI.trimFragment();
      final IResourceDescription resourceDescription = index.getResourceDescription(_trimFragment);
      EClass _eClass = this.getEClass();
      Iterable<IEObjectDescription> _exportedObjectsByType = resourceDescription.getExportedObjectsByType(_eClass);
      final Function1<IEObjectDescription, Boolean> _function = (IEObjectDescription it) -> {
        URI _uRI_1 = this.getURI();
        URI _eObjectURI = it.getEObjectURI();
        return Boolean.valueOf(Objects.equal(_uRI_1, _eObjectURI));
      };
      _xblockexpression = IterableExtensions.<IEObjectDescription>findFirst(_exportedObjectsByType, _function);
    }
    return _xblockexpression;
  }
  
  @Override
  public EObject resolve(final ResourceSet resourceSet) {
    XtextEObjectID _parentID = this.getParentID();
    final EObject parent = _parentID.resolve(resourceSet);
    String _xifexpression = null;
    String _relativeFragment = this.getRelativeFragment();
    boolean _startsWith = _relativeFragment.startsWith("#");
    if (_startsWith) {
      _xifexpression = this.getRelativeFragment();
    } else {
      URI _uRI = EcoreUtil.getURI(parent);
      String _fragment = _uRI.fragment();
      String _relativeFragment_1 = this.getRelativeFragment();
      _xifexpression = (_fragment + _relativeFragment_1);
    }
    final String elementFragment = _xifexpression;
    Resource _eResource = parent.eResource();
    final EObject element = _eResource.getEObject(elementFragment);
    if ((Objects.equal(element, null) || element.eIsProxy())) {
      String _string = this.toString();
      String _plus = ("Could not resolve element " + _string);
      throw new NoSuchElementException(_plus);
    }
    EClass _eClass = this.getEClass();
    boolean _isInstance = _eClass.isInstance(element);
    boolean _not = (!_isInstance);
    if (_not) {
      EClass _eClass_1 = this.getEClass();
      String _name = _eClass_1.getName();
      String _plus_1 = ("Expected " + _name);
      String _plus_2 = (_plus_1 + " but got ");
      EClass _eClass_2 = element.eClass();
      String _name_1 = _eClass_2.getName();
      String _plus_3 = (_plus_2 + _name_1);
      throw new NoSuchElementException(_plus_3);
    }
    return element;
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public RelativeXtextEObjectID() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(parentIDProperty, XtextEObjectID.class);
    modelElement.addProperty(relativeFragmentProperty, String.class);
  }
  
  public void postLoad() {
    
  }
  
  private ReadOnlyObjectWrapper<XtextEObjectID> parentIDProperty = new ReadOnlyObjectWrapper<XtextEObjectID>(this, "parentID");
  
  public XtextEObjectID getParentID() {
    return this.parentIDProperty.get();
  }
  
  public ReadOnlyObjectProperty<XtextEObjectID> parentIDProperty() {
    return this.parentIDProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper relativeFragmentProperty = new ReadOnlyStringWrapper(this, "relativeFragment");
  
  public String getRelativeFragment() {
    return this.relativeFragmentProperty.get();
  }
  
  public ReadOnlyStringProperty relativeFragmentProperty() {
    return this.relativeFragmentProperty.getReadOnlyProperty();
  }
}
