package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.ui.refactoring.participant.JvmElementFinder;
import org.eclipse.xtext.common.types.util.Primitives;
import org.eclipse.xtext.common.types.util.jdt.JavaElementFinder;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
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
  @Extension
  private JavaElementFinder _javaElementFinder;
  
  @Inject
  private IResourceSetProvider resourceSetProvider;
  
  public Iterable<JvmField> getAttributes(final JvmDeclaredType owner) {
    Iterable<JvmField> _declaredFields = owner.getDeclaredFields();
    final Function1<JvmField, Boolean> _function = (JvmField it) -> {
      return Boolean.valueOf(this.isAttributeType(it));
    };
    return IterableExtensions.<JvmField>filter(_declaredFields, _function);
  }
  
  public Iterable<JvmOperation> getMethods(final JvmDeclaredType owner) {
    Iterable<JvmOperation> _xblockexpression = null;
    {
      Iterable<JvmField> _declaredFields = owner.getDeclaredFields();
      final Function1<JvmField, String> _function = (JvmField it) -> {
        return it.getSimpleName();
      };
      final Map<String, JvmField> fields = IterableExtensions.<String, JvmField>toMap(_declaredFields, _function);
      Iterable<JvmOperation> _declaredOperations = owner.getDeclaredOperations();
      final Function1<JvmOperation, Boolean> _function_1 = (JvmOperation it) -> {
        return Boolean.valueOf(((!this.isSetter(it, fields)) && (!this.isGetter(it, fields))));
      };
      _xblockexpression = IterableExtensions.<JvmOperation>filter(_declaredOperations, _function_1);
    }
    return _xblockexpression;
  }
  
  public String getSignature(final JvmField it) {
    StringConcatenation _builder = new StringConcatenation();
    JvmVisibility _visibility = it.getVisibility();
    String _string = _visibility.toString();
    String _lowerCase = _string.toLowerCase();
    _builder.append(_lowerCase, "");
    _builder.append(" ");
    {
      boolean _isStatic = it.isStatic();
      if (_isStatic) {
        _builder.append("static ");
      }
    }
    {
      boolean _isFinal = it.isFinal();
      if (_isFinal) {
        _builder.append("final ");
      }
    }
    {
      boolean _isVolatile = it.isVolatile();
      if (_isVolatile) {
        _builder.append("volatile ");
      }
    }
    JvmTypeReference _type = it.getType();
    String _qualifiedName = _type.getQualifiedName();
    _builder.append(_qualifiedName, "");
    _builder.append(" ");
    String _simpleName = it.getSimpleName();
    _builder.append(_simpleName, "");
    return _builder.toString();
  }
  
  protected boolean isSetter(final JvmOperation it, final Map<String, JvmField> fields) {
    if ((it.getSimpleName().startsWith("set") && (it.getParameters().size() == 1))) {
      String _simpleName = it.getSimpleName();
      String _substring = _simpleName.substring(3);
      String _firstLower = StringExtensions.toFirstLower(_substring);
      final JvmField field = fields.get(_firstLower);
      JvmTypeReference _type = null;
      if (field!=null) {
        _type=field.getType();
      }
      LightweightTypeReference _lightweight = null;
      if (_type!=null) {
        _lightweight=this.getLightweight(_type);
      }
      final LightweightTypeReference fieldType = _lightweight;
      boolean _notEquals = (!Objects.equal(fieldType, null));
      if (_notEquals) {
        EList<JvmFormalParameter> _parameters = it.getParameters();
        JvmFormalParameter _head = IterableExtensions.<JvmFormalParameter>head(_parameters);
        JvmTypeReference _parameterType = _head.getParameterType();
        LightweightTypeReference _lightweight_1 = this.getLightweight(_parameterType);
        return fieldType.isAssignableFrom(_lightweight_1);
      }
    }
    return false;
  }
  
  protected boolean isGetter(final JvmOperation it, final Map<String, JvmField> fields) {
    if (((it.getParameters().size() == 0) && (!Objects.equal(it.getReturnType(), null)))) {
      JvmField _xifexpression = null;
      String _simpleName = it.getSimpleName();
      boolean _startsWith = _simpleName.startsWith("get");
      if (_startsWith) {
        String _simpleName_1 = it.getSimpleName();
        String _substring = _simpleName_1.substring(3);
        String _firstLower = StringExtensions.toFirstLower(_substring);
        _xifexpression = fields.get(_firstLower);
      } else {
        JvmField _xifexpression_1 = null;
        String _simpleName_2 = it.getSimpleName();
        boolean _startsWith_1 = _simpleName_2.startsWith("is");
        if (_startsWith_1) {
          String _simpleName_3 = it.getSimpleName();
          String _substring_1 = _simpleName_3.substring(2);
          String _firstLower_1 = StringExtensions.toFirstLower(_substring_1);
          _xifexpression_1 = fields.get(_firstLower_1);
        } else {
          return false;
        }
        _xifexpression = _xifexpression_1;
      }
      final JvmField field = _xifexpression;
      boolean _notEquals = (!Objects.equal(field, null));
      if (_notEquals) {
        JvmTypeReference _returnType = it.getReturnType();
        LightweightTypeReference _lightweight = this.getLightweight(_returnType);
        JvmTypeReference _type = null;
        if (field!=null) {
          _type=field.getType();
        }
        LightweightTypeReference _lightweight_1 = this.getLightweight(_type);
        return _lightweight.isAssignableFrom(_lightweight_1);
      }
    }
    return false;
  }
  
  public Iterable<JvmField> getReferences(final JvmDeclaredType owner) {
    Iterable<JvmField> _declaredFields = owner.getDeclaredFields();
    final Function1<JvmField, Boolean> _function = (JvmField it) -> {
      boolean _isAttributeType = this.isAttributeType(it);
      return Boolean.valueOf((!_isAttributeType));
    };
    return IterableExtensions.<JvmField>filter(_declaredFields, _function);
  }
  
  public boolean isAttributeType(final JvmField it) {
    boolean _xblockexpression = false;
    {
      JvmTypeReference _type = it.getType();
      final JvmTypeReference componentType = this.getComponentType(_type);
      _xblockexpression = (this._primitives.isPrimitive(componentType) || Objects.equal(componentType.getQualifiedName(), "java.lang.String"));
    }
    return _xblockexpression;
  }
  
  public JvmTypeReference getComponentType(final JvmTypeReference it) {
    final LightweightTypeReference type = this.getLightweight(it);
    LightweightTypeReference _xifexpression = null;
    boolean _isArray = type.isArray();
    if (_isArray) {
      _xifexpression = type.getComponentType();
    } else {
      LightweightTypeReference _xifexpression_1 = null;
      if ((type.isSubtypeOf(Iterable.class) && (!type.getTypeArguments().isEmpty()))) {
        List<LightweightTypeReference> _typeArguments = type.getTypeArguments();
        LightweightTypeReference _head = IterableExtensions.<LightweightTypeReference>head(_typeArguments);
        _xifexpression_1 = _head.getInvariantBoundSubstitute();
      } else {
        _xifexpression_1 = type;
      }
      _xifexpression = _xifexpression_1;
    }
    final LightweightTypeReference componentType = _xifexpression;
    return componentType.toJavaCompliantTypeReference();
  }
  
  protected LightweightTypeReference getLightweight(final JvmTypeReference it) {
    LightweightTypeReference _xifexpression = null;
    boolean _equals = Objects.equal(it, null);
    if (_equals) {
      _xifexpression = null;
    } else {
      StandardTypeReferenceOwner _standardTypeReferenceOwner = new StandardTypeReferenceOwner(this.services, it);
      _xifexpression = _standardTypeReferenceOwner.toLightweightTypeReference(it);
    }
    return _xifexpression;
  }
  
  public JvmIdentifiableElement getJvmElement(final IJavaElement javaElement) {
    IJavaProject _javaProject = javaElement.getJavaProject();
    final IProject project = _javaProject.getProject();
    final ResourceSet resourceSet = this.resourceSetProvider.get(project);
    EObject _correspondingJvmElement = this._jvmElementFinder.getCorrespondingJvmElement(javaElement, resourceSet);
    final JvmIdentifiableElement jvmElement = ((JvmIdentifiableElement) _correspondingJvmElement);
    final JvmDeclaredType jvmType = EcoreUtil2.<JvmDeclaredType>getContainerOfType(jvmElement, JvmDeclaredType.class);
    final JvmDeclaredType originalType = this.getOriginalJvmType(jvmType);
    boolean _notEquals = (!Objects.equal(jvmElement, jvmType));
    if (_notEquals) {
      boolean _notEquals_1 = (!Objects.equal(originalType, jvmType));
      if (_notEquals_1) {
        return null;
      } else {
        return jvmElement;
      }
    }
    return originalType;
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
  
  public IJavaElement getJavaElement(final JvmIdentifiableElement type) {
    return this._javaElementFinder.findElementFor(type);
  }
}
