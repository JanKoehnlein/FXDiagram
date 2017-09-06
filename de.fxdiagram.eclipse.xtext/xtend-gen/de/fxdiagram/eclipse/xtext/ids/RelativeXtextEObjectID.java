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
    final String fragment = this.getURI().fragment();
    final String parentFragment = parentID.getURI().fragment();
    String _xifexpression = null;
    boolean _startsWith = fragment.startsWith(parentFragment);
    if (_startsWith) {
      _xifexpression = fragment.substring(parentFragment.length());
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
    int _hashCode = this.getParentID().hashCode();
    int _multiply = (461 * _hashCode);
    int _hashCode_1 = this.getRelativeFragment().hashCode();
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
      this.getParentID().toString();
      String _relativeFragment = this.getRelativeFragment();
      _xblockexpression = ("->" + _relativeFragment);
    }
    return _xblockexpression;
  }
  
  @Override
  public IEObjectDescription findInIndex(final IResourceDescriptions index) {
    IEObjectDescription _xblockexpression = null;
    {
      final IResourceDescription resourceDescription = index.getResourceDescription(this.getURI().trimFragment());
      final Function1<IEObjectDescription, Boolean> _function = (IEObjectDescription it) -> {
        URI _uRI = this.getURI();
        URI _eObjectURI = it.getEObjectURI();
        return Boolean.valueOf(Objects.equal(_uRI, _eObjectURI));
      };
      _xblockexpression = IterableExtensions.<IEObjectDescription>findFirst(resourceDescription.getExportedObjectsByType(this.getEClass()), _function);
    }
    return _xblockexpression;
  }
  
  @Override
  public EObject resolve(final ResourceSet resourceSet) {
    final EObject parent = this.getParentID().resolve(resourceSet);
    String _xifexpression = null;
    boolean _startsWith = this.getRelativeFragment().startsWith("#");
    if (_startsWith) {
      _xifexpression = this.getRelativeFragment();
    } else {
      String _fragment = EcoreUtil.getURI(parent).fragment();
      String _relativeFragment = this.getRelativeFragment();
      _xifexpression = (_fragment + _relativeFragment);
    }
    final String elementFragment = _xifexpression;
    final EObject element = parent.eResource().getEObject(elementFragment);
    if (((element == null) || element.eIsProxy())) {
      String _string = this.toString();
      String _plus = ("Could not resolve element " + _string);
      throw new NoSuchElementException(_plus);
    }
    boolean _isInstance = this.getEClass().isInstance(element);
    boolean _not = (!_isInstance);
    if (_not) {
      String _name = this.getEClass().getName();
      String _plus_1 = ("Expected " + _name);
      String _plus_2 = (_plus_1 + " but got ");
      String _name_1 = element.eClass().getName();
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
