package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.xtext.xbase.JavaElementDescriptor;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.xtext.EcoreUtil2;
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
  
  @Override
  public <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<? extends T> mapping) {
    final T it = domainObject;
    boolean _matched = false;
    if (!_matched) {
      if (it instanceof EObject) {
        _matched=true;
        final XtextEObjectID elementID = this.createXtextEObjectID(((EObject)it));
        Resource _eResource = ((EObject)it).eResource();
        URI _uRI = _eResource.getURI();
        String _scheme = _uRI.scheme();
        boolean _endsWith = _scheme.endsWith("java");
        if (_endsWith) {
          final JvmIdentifiableElement identifiableJvmElement = EcoreUtil2.<JvmIdentifiableElement>getContainerOfType(((EObject)it), JvmIdentifiableElement.class);
          Resource _eResource_1 = ((EObject)it).eResource();
          URI _uRI_1 = _eResource_1.getURI();
          JvmDomainUtil _jvmDomainUtil = this.getJvmDomainUtil(_uRI_1);
          final IJavaElement javaElement = _jvmDomainUtil.getJavaElement(identifiableJvmElement);
          boolean _notEquals = (!Objects.equal(javaElement, null));
          if (_notEquals) {
            String _handleIdentifier = javaElement.getHandleIdentifier();
            XDiagramConfig _config = mapping.getConfig();
            String _iD = _config.getID();
            String _iD_1 = mapping.getID();
            JavaElementDescriptor<EObject> _javaElementDescriptor = new JavaElementDescriptor<EObject>(elementID, _handleIdentifier, _iD, _iD_1);
            return ((IMappedElementDescriptor<T>) _javaElementDescriptor);
          }
        }
        XDiagramConfig _config_1 = mapping.getConfig();
        String _iD_2 = _config_1.getID();
        String _iD_3 = mapping.getID();
        JvmEObjectDescriptor<EObject> _jvmEObjectDescriptor = new JvmEObjectDescriptor<EObject>(elementID, _iD_2, _iD_3);
        return ((IMappedElementDescriptor<T>) _jvmEObjectDescriptor);
      }
    }
    return null;
  }
}
