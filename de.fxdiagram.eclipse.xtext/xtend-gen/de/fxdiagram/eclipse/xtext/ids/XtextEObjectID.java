package de.fxdiagram.eclipse.xtext.ids;

import com.google.common.base.Objects;
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
      boolean _notEquals = (!Objects.equal(currentName, null));
      if (_notEquals) {
        EClass _eClass = it.eClass();
        URI _uRI = EcoreUtil.getURI(it);
        return new DefaultXtextEObjectID(currentName, _eClass, _uRI);
      }
      EObject currentParent = it;
      do {
        {
          EObject _eContainer = currentParent.eContainer();
          currentParent = _eContainer;
          boolean _equals = Objects.equal(currentParent, null);
          if (_equals) {
            EClass _eClass_1 = it.eClass();
            URI _uRI_1 = EcoreUtil.getURI(it);
            return new UnnamedXtextEObjectID(_eClass_1, _uRI_1);
          }
          QualifiedName _qualifiedName = this.getQualifiedName(currentParent);
          currentName = _qualifiedName;
        }
      } while(Objects.equal(currentName, null));
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
      Resource _eResource = domainObject.eResource();
      IResourceServiceProvider _resourceServiceProvider = this.getResourceServiceProvider(_eResource);
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
          URI _uRI = resource.getURI();
          _xifexpression = IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(_uRI);
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
