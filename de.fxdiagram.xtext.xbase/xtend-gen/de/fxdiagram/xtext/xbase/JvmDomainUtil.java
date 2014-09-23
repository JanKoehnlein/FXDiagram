package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.ui.refactoring.participant.JvmElementFinder;
import org.eclipse.xtext.common.types.util.Primitives;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;
import org.eclipse.xtext.xbase.typesystem.references.StandardTypeReferenceOwner;
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices;

@SuppressWarnings("all")
public class JvmDomainUtil {
  @Inject
  @Extension
  private Primitives _primitives;
  
  @Inject
  private CommonTypeComputationServices services;
  
  @Inject
  @Extension
  private JvmElementFinder _jvmElementFinder;
  
  @Inject
  private IResourceSetProvider resourceSetProvider;
  
  public Iterable<JvmField> getAttributes(final JvmDeclaredType owner) {
    Iterable<JvmField> _declaredFields = owner.getDeclaredFields();
    final Function1<JvmField, Boolean> _function = new Function1<JvmField, Boolean>() {
      public Boolean apply(final JvmField it) {
        return Boolean.valueOf(JvmDomainUtil.this.isAttributeType(it));
      }
    };
    return IterableExtensions.<JvmField>filter(_declaredFields, _function);
  }
  
  public Iterable<JvmField> getReferences(final JvmDeclaredType owner) {
    Iterable<JvmField> _declaredFields = owner.getDeclaredFields();
    final Function1<JvmField, Boolean> _function = new Function1<JvmField, Boolean>() {
      public Boolean apply(final JvmField it) {
        boolean _isAttributeType = JvmDomainUtil.this.isAttributeType(it);
        return Boolean.valueOf((!_isAttributeType));
      }
    };
    return IterableExtensions.<JvmField>filter(_declaredFields, _function);
  }
  
  public boolean isAttributeType(final JvmField it) {
    boolean _xblockexpression = false;
    {
      JvmTypeReference _type = it.getType();
      final JvmTypeReference componentType = this.getComponentType(_type);
      boolean _or = false;
      boolean _isPrimitive = this._primitives.isPrimitive(componentType);
      if (_isPrimitive) {
        _or = true;
      } else {
        String _qualifiedName = componentType.getQualifiedName();
        boolean _equals = Objects.equal(_qualifiedName, "java.lang.String");
        _or = _equals;
      }
      _xblockexpression = _or;
    }
    return _xblockexpression;
  }
  
  public JvmTypeReference getComponentType(final JvmTypeReference it) {
    StandardTypeReferenceOwner _standardTypeReferenceOwner = new StandardTypeReferenceOwner(this.services, it);
    final LightweightTypeReference type = _standardTypeReferenceOwner.toLightweightTypeReference(it);
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
  
  public JvmDeclaredType getJvmType(final IJavaElement javaElement) {
    IJavaProject _javaProject = javaElement.getJavaProject();
    final IProject project = _javaProject.getProject();
    final ResourceSet resourceSet = this.resourceSetProvider.get(project);
    final EObject jvmElement = this._jvmElementFinder.getCorrespondingJvmElement(javaElement, resourceSet);
    final JvmDeclaredType jvmType = EcoreUtil2.<JvmDeclaredType>getContainerOfType(jvmElement, JvmDeclaredType.class);
    return this.getOriginalJvmType(jvmType);
  }
  
  public JvmDeclaredType getOriginalJvmType(final JvmDeclaredType jvmType) {
    Resource _eResource = jvmType.eResource();
    final ResourceSet resourceSet = _eResource.getResourceSet();
    final EObject indexedJvmType = this._jvmElementFinder.findJvmElementDeclarationInIndex(jvmType, null, resourceSet);
    JvmDeclaredType _xifexpression = null;
    if ((indexedJvmType instanceof JvmDeclaredType)) {
      _xifexpression = ((JvmDeclaredType)indexedJvmType);
    } else {
      _xifexpression = jvmType;
    }
    return _xifexpression;
  }
}
