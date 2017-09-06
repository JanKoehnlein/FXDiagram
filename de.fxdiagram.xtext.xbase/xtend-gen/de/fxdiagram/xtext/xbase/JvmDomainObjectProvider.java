package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.xtext.xbase.JavaElementDescriptor;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
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
    if (it instanceof EObject) {
      _matched=true;
      final XtextEObjectID elementID = this.createXtextEObjectID(((EObject)it));
      boolean _endsWith = ((EObject)it).eResource().getURI().scheme().endsWith("java");
      if (_endsWith) {
        final JvmIdentifiableElement identifiableJvmElement = EcoreUtil2.<JvmIdentifiableElement>getContainerOfType(((EObject)it), JvmIdentifiableElement.class);
        final IJavaElement javaElement = this.getJvmDomainUtil(((EObject)it).eResource().getURI()).getJavaElement(identifiableJvmElement);
        if ((javaElement != null)) {
          String _handleIdentifier = javaElement.getHandleIdentifier();
          String _iD = mapping.getConfig().getID();
          String _iD_1 = mapping.getID();
          JavaElementDescriptor<EObject> _javaElementDescriptor = new JavaElementDescriptor<EObject>(elementID, _handleIdentifier, _iD, _iD_1);
          return ((IMappedElementDescriptor<T>) _javaElementDescriptor);
        }
      }
      String _iD_2 = mapping.getConfig().getID();
      String _iD_3 = mapping.getID();
      JvmEObjectDescriptor<EObject> _jvmEObjectDescriptor = new JvmEObjectDescriptor<EObject>(elementID, _iD_2, _iD_3);
      return ((IMappedElementDescriptor<T>) _jvmEObjectDescriptor);
    }
    return null;
  }
}
