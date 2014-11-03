package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.ESetting;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import de.fxdiagram.xtext.glue.mapping.XtextDomainObjectProvider;
import de.fxdiagram.xtext.xbase.JavaElementDescriptor;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
import de.fxdiagram.xtext.xbase.JvmESettingDescriptor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.resource.IResourceServiceProvider;

@SuppressWarnings("all")
public class JvmDomainObjectProvider extends XtextDomainObjectProvider {
  protected JvmDomainUtil getJvmDomainUtil(final URI uri) {
    JvmDomainUtil _xblockexpression = null;
    {
      URI _xifexpression = null;
      String _scheme = uri.scheme();
      boolean _equals = Objects.equal(_scheme, "java");
      if (_equals) {
        _xifexpression = URI.createURI("dummy.___xbase");
      } else {
        _xifexpression = uri;
      }
      final URI serviceLookupURI = _xifexpression;
      final IResourceServiceProvider resourceServiceProvider = IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(serviceLookupURI);
      _xblockexpression = resourceServiceProvider.<JvmDomainUtil>get(JvmDomainUtil.class);
    }
    return _xblockexpression;
  }
  
  public <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<T> mapping) {
    final T it = domainObject;
    boolean _matched = false;
    if (!_matched) {
      if (it instanceof EObject) {
        _matched=true;
        boolean _and = false;
        Resource _eResource = ((EObject)it).eResource();
        URI _uRI = _eResource.getURI();
        String _scheme = _uRI.scheme();
        boolean _endsWith = _scheme.endsWith("java");
        if (!_endsWith) {
          _and = false;
        } else {
          _and = (it instanceof JvmIdentifiableElement);
        }
        if (_and) {
          Resource _eResource_1 = ((EObject)it).eResource();
          URI _uRI_1 = _eResource_1.getURI();
          JvmDomainUtil _jvmDomainUtil = this.getJvmDomainUtil(_uRI_1);
          final IJavaElement javaElement = _jvmDomainUtil.getJavaElement(((JvmIdentifiableElement) it));
          URI _uRI_2 = EcoreUtil.getURI(((EObject)it));
          String _string = _uRI_2.toString();
          String _fullyQualifiedName = this.getFullyQualifiedName(((EObject)it));
          String _handleIdentifier = javaElement.getHandleIdentifier();
          XDiagramConfig _config = mapping.getConfig();
          String _iD = _config.getID();
          String _iD_1 = mapping.getID();
          JavaElementDescriptor _javaElementDescriptor = new JavaElementDescriptor(_string, _fullyQualifiedName, _handleIdentifier, _iD, _iD_1, this);
          return ((IMappedElementDescriptor<T>) _javaElementDescriptor);
        }
        URI _uRI_3 = EcoreUtil.getURI(((EObject)it));
        String _string_1 = _uRI_3.toString();
        String _fullyQualifiedName_1 = this.getFullyQualifiedName(((EObject)it));
        XDiagramConfig _config_1 = mapping.getConfig();
        String _iD_2 = _config_1.getID();
        String _iD_3 = mapping.getID();
        JvmEObjectDescriptor<EObject> _jvmEObjectDescriptor = new JvmEObjectDescriptor<EObject>(_string_1, _fullyQualifiedName_1, _iD_2, _iD_3, this);
        return ((IMappedElementDescriptor<T>) _jvmEObjectDescriptor);
      }
    }
    if (!_matched) {
      if (it instanceof ESetting) {
        _matched=true;
        EObject _owner = ((ESetting<?>)it).getOwner();
        URI _uRI = EcoreUtil.getURI(_owner);
        String _string = _uRI.toString();
        EObject _owner_1 = ((ESetting<?>)it).getOwner();
        String _fullyQualifiedName = this.getFullyQualifiedName(_owner_1);
        EReference _reference = ((ESetting<?>)it).getReference();
        int _index = ((ESetting<?>)it).getIndex();
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        return new JvmESettingDescriptor(_string, _fullyQualifiedName, _reference, _index, _iD, _iD_1, this);
      }
    }
    if (!_matched) {
      if (it instanceof IJavaElement) {
        _matched=true;
        URI _createURI = URI.createURI("dummy.___xbase");
        JvmDomainUtil _jvmDomainUtil = this.getJvmDomainUtil(_createURI);
        final JvmIdentifiableElement jvmType = _jvmDomainUtil.getJvmElement(((IJavaElement)it));
        URI _uRI = EcoreUtil.getURI(jvmType);
        String _string = _uRI.toString();
        String _fullyQualifiedName = this.getFullyQualifiedName(jvmType);
        String _handleIdentifier = ((IJavaElement)it).getHandleIdentifier();
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        JavaElementDescriptor _javaElementDescriptor = new JavaElementDescriptor(_string, _fullyQualifiedName, _handleIdentifier, _iD, _iD_1, this);
        return ((IMappedElementDescriptor<T>) _javaElementDescriptor);
      }
    }
    return null;
  }
}
