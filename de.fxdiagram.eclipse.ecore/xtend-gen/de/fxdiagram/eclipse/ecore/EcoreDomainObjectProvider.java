package de.fxdiagram.eclipse.ecore;

import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.eclipse.ecore.EcoreDomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.XDiagramConfig;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.util.EcoreUtil;

@SuppressWarnings("all")
public class EcoreDomainObjectProvider implements IMappedElementDescriptorProvider {
  @Override
  public <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<? extends T> mapping) {
    IMappedElementDescriptor<T> _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (domainObject instanceof ENamedElement) {
        _matched=true;
        URI _uRI = EcoreUtil.getURI(((ENamedElement)domainObject));
        String _string = _uRI.toString();
        String _name = ((ENamedElement)domainObject).getName();
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        EcoreDomainObjectDescriptor _ecoreDomainObjectDescriptor = new EcoreDomainObjectDescriptor(_string, _name, _iD, _iD_1);
        _switchResult = ((IMappedElementDescriptor<T>) _ecoreDomainObjectDescriptor);
      }
    }
    if (!_matched) {
      _switchResult = null;
    }
    return _switchResult;
  }
  
  @Override
  public <T extends Object> DomainObjectDescriptor createDescriptor(final T domainObject) {
    return null;
  }
}
