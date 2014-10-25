package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.ESetting;
import de.fxdiagram.xtext.glue.mapping.MappedElement;
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
  
  public DomainObjectDescriptor createDescriptor(final Object handle) {
    if ((handle instanceof MappedElement<?>)) {
      Object _element = ((MappedElement<?>)handle).getElement();
      final Object it = _element;
      boolean _matched = false;
      if (!_matched) {
        if (it instanceof JvmIdentifiableElement) {
          _matched=true;
          Resource _eResource = ((JvmIdentifiableElement)it).eResource();
          URI _uRI = _eResource.getURI();
          String _scheme = _uRI.scheme();
          boolean _endsWith = _scheme.endsWith("java");
          if (_endsWith) {
            Resource _eResource_1 = ((JvmIdentifiableElement)it).eResource();
            URI _uRI_1 = _eResource_1.getURI();
            JvmDomainUtil _jvmDomainUtil = this.getJvmDomainUtil(_uRI_1);
            final IJavaElement javaElement = _jvmDomainUtil.getJavaElement(((JvmIdentifiableElement)it));
            URI _uRI_2 = EcoreUtil.getURI(((EObject)it));
            String _string = _uRI_2.toString();
            String _fullyQualifiedName = this.getFullyQualifiedName(((EObject)it));
            String _handleIdentifier = javaElement.getHandleIdentifier();
            AbstractMapping<?> _mapping = ((MappedElement<?>)handle).getMapping();
            XDiagramConfig _config = _mapping.getConfig();
            String _iD = _config.getID();
            AbstractMapping<?> _mapping_1 = ((MappedElement<?>)handle).getMapping();
            String _iD_1 = _mapping_1.getID();
            return new JavaElementDescriptor(_string, _fullyQualifiedName, _handleIdentifier, _iD, _iD_1, this);
          }
          URI _uRI_3 = EcoreUtil.getURI(((EObject)it));
          String _string_1 = _uRI_3.toString();
          String _fullyQualifiedName_1 = this.getFullyQualifiedName(((EObject)it));
          AbstractMapping<?> _mapping_2 = ((MappedElement<?>)handle).getMapping();
          XDiagramConfig _config_1 = _mapping_2.getConfig();
          String _iD_2 = _config_1.getID();
          AbstractMapping<?> _mapping_3 = ((MappedElement<?>)handle).getMapping();
          String _iD_3 = _mapping_3.getID();
          return new JvmEObjectDescriptor<EObject>(_string_1, _fullyQualifiedName_1, _iD_2, _iD_3, this);
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
          AbstractMapping<?> _mapping = ((MappedElement<?>)handle).getMapping();
          XDiagramConfig _config = _mapping.getConfig();
          String _iD = _config.getID();
          AbstractMapping<?> _mapping_1 = ((MappedElement<?>)handle).getMapping();
          String _iD_1 = _mapping_1.getID();
          return new JvmESettingDescriptor<EObject>(_string, _fullyQualifiedName, _reference, _index, _iD, _iD_1, this);
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
          AbstractMapping<?> _mapping = ((MappedElement<?>)handle).getMapping();
          XDiagramConfig _config = _mapping.getConfig();
          String _iD = _config.getID();
          AbstractMapping<?> _mapping_1 = ((MappedElement<?>)handle).getMapping();
          String _iD_1 = _mapping_1.getID();
          return new JavaElementDescriptor(_string, _fullyQualifiedName, _handleIdentifier, _iD, _iD_1, this);
        }
      }
      return null;
    }
    return null;
  }
}
