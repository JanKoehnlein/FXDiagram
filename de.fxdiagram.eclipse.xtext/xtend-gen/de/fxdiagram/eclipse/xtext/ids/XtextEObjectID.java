package de.fxdiagram.eclipse.xtext.ids;

import de.fxdiagram.eclipse.xtext.ids.DefaultXtextEObjectID;
import de.fxdiagram.eclipse.xtext.ids.RelativeXtextEObjectID;
import de.fxdiagram.eclipse.xtext.ids.UnnamedXtextEObjectID;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;

@SuppressWarnings("all")
public interface XtextEObjectID {
  public static class Factory {
    public XtextEObjectID createXtextEObjectID(final EObject it) {
      QualifiedName currentName = this.getQualifiedName(it);
      if ((currentName != null)) {
        EClass _eClass = it.eClass();
        URI _uRI = EcoreUtil.getURI(it);
        return new DefaultXtextEObjectID(currentName, _eClass, _uRI);
      }
      EObject currentParent = it;
      do {
        {
          currentParent = currentParent.eContainer();
          if ((currentParent == null)) {
            EClass _eClass_1 = it.eClass();
            URI _uRI_1 = EcoreUtil.getURI(it);
            return new UnnamedXtextEObjectID(_eClass_1, _uRI_1);
          }
          currentName = this.getQualifiedName(currentParent);
        }
      } while((currentName == null));
      final XtextEObjectID parentID = this.createXtextEObjectID(currentParent);
      EClass _eClass_1 = it.eClass();
      URI _uRI_1 = EcoreUtil.getURI(it);
      return new RelativeXtextEObjectID(parentID, _eClass_1, _uRI_1);
    }
    
    public XtextEObjectID createXtextEObjectID(final IEObjectDescription it) {
      QualifiedName _qualifiedName = it.getQualifiedName();
      EClass _eClass = it.getEClass();
      URI _eObjectURI = it.getEObjectURI();
      return new DefaultXtextEObjectID(_qualifiedName, _eClass, _eObjectURI);
    }
    
    protected QualifiedName getQualifiedName(final EObject domainObject) {
      IResourceServiceProvider _resourceServiceProvider = this.getResourceServiceProvider(domainObject.eResource());
      IQualifiedNameProvider _get = null;
      if (_resourceServiceProvider!=null) {
        _get=_resourceServiceProvider.<IQualifiedNameProvider>get(IQualifiedNameProvider.class);
      }
      QualifiedName _fullyQualifiedName = null;
      if (_get!=null) {
        _fullyQualifiedName=_get.getFullyQualifiedName(domainObject);
      }
      return _fullyQualifiedName;
    }
    
    protected IResourceServiceProvider getResourceServiceProvider(final Resource resource) {
      IResourceServiceProvider _xblockexpression = null;
      {
        IResourceServiceProvider _xifexpression = null;
        if ((resource instanceof XtextResource)) {
          _xifexpression = ((XtextResource)resource).getResourceServiceProvider();
        } else {
          _xifexpression = IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(resource.getURI());
        }
        final IResourceServiceProvider resourceServiceProvider = _xifexpression;
        _xblockexpression = resourceServiceProvider;
      }
      return _xblockexpression;
    }
  }
  
  public abstract QualifiedName getQualifiedName();
  
  public abstract URI getURI();
  
  public abstract EClass getEClass();
  
  public abstract EObject resolve(final ResourceSet resourceSet);
  
  public abstract IEObjectDescription findInIndex(final IResourceDescriptions index);
  
  public abstract IResourceServiceProvider getResourceServiceProvider();
}
