package de.fxdiagram.xtext.domainmodel;

import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.typesystem.legacy.StandardTypeReferenceOwner;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;
import org.eclipse.xtext.xbase.typesystem.references.OwnedConverter;
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices;

@SuppressWarnings("all")
public class DomainModelUtil {
  @Inject
  @Extension
  private IJvmModelAssociations _iJvmModelAssociations;
  
  @Inject
  private CommonTypeComputationServices services;
  
  public Entity getReferencedEntity(final JvmTypeReference it) {
    Object _xblockexpression = null;
    {
      JvmTypeReference _componentType = null;
      if (it!=null) {
        _componentType=this.getComponentType(it);
      }
      JvmType _type = null;
      if (_componentType!=null) {
        _type=_componentType.getType();
      }
      EObject _primarySourceElement = null;
      if (_type!=null) {
        _primarySourceElement=this._iJvmModelAssociations.getPrimarySourceElement(_type);
      }
      final EObject sourceType = _primarySourceElement;
      Object _xifexpression = null;
      if ((sourceType instanceof Entity)) {
        return ((Entity)sourceType);
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return ((Entity)_xblockexpression);
  }
  
  public JvmTypeReference getComponentType(final JvmTypeReference it) {
    StandardTypeReferenceOwner _standardTypeReferenceOwner = new StandardTypeReferenceOwner(this.services, it);
    OwnedConverter _ownedConverter = new OwnedConverter(_standardTypeReferenceOwner);
    final LightweightTypeReference type = _ownedConverter.apply(it);
    LightweightTypeReference _xifexpression = null;
    boolean _isArray = type.isArray();
    if (_isArray) {
      _xifexpression = type.getComponentType();
    } else {
      LightweightTypeReference _xifexpression_1 = null;
      boolean _and = false;
      boolean _isSubtypeOf = type.isSubtypeOf(Iterable.class);
      if (!_isSubtypeOf) {
        _and = false;
      } else {
        List<LightweightTypeReference> _typeArguments = type.getTypeArguments();
        boolean _isEmpty = _typeArguments.isEmpty();
        boolean _not = (!_isEmpty);
        _and = _not;
      }
      if (_and) {
        List<LightweightTypeReference> _typeArguments_1 = type.getTypeArguments();
        LightweightTypeReference _head = IterableExtensions.<LightweightTypeReference>head(_typeArguments_1);
        _xifexpression_1 = _head.getInvariantBoundSubstitute();
      } else {
        _xifexpression_1 = type;
      }
      _xifexpression = _xifexpression_1;
    }
    final LightweightTypeReference componentType = _xifexpression;
    return componentType.toJavaCompliantTypeReference();
  }
}
