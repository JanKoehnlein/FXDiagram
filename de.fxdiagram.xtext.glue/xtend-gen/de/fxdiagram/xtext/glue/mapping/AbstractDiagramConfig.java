package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.MappingAcceptor;
import de.fxdiagram.xtext.glue.mapping.MappingCall;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import de.fxdiagram.xtext.glue.mapping.XtextDomainObjectProvider;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pure;

@Logging
@SuppressWarnings("all")
public abstract class AbstractDiagramConfig implements XDiagramConfig {
  private Map<String, AbstractMapping<?>> mappings = CollectionLiterals.<String, AbstractMapping<?>>newHashMap();
  
  @Accessors
  private String ID;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private XtextDomainObjectProvider domainObjectProvider = this.createDomainObjectProvider();
  
  protected XtextDomainObjectProvider createDomainObjectProvider() {
    return new XtextDomainObjectProvider();
  }
  
  public AbstractMapping<?> getMappingByID(final String mappingID) {
    return this.mappings.get(mappingID);
  }
  
  protected abstract <ARG extends Object> void entryCalls(final ARG domainArgument, final MappingAcceptor<ARG> acceptor);
  
  public <ARG extends Object> Iterable<? extends MappingCall<?, ARG>> getEntryCalls(final ARG domainArgument) {
    List<MappingCall<?, ARG>> _xblockexpression = null;
    {
      final MappingAcceptor<ARG> acceptor = new MappingAcceptor<ARG>();
      this.<ARG>entryCalls(domainArgument, acceptor);
      _xblockexpression = acceptor.getMappingCalls();
    }
    return _xblockexpression;
  }
  
  public <ARG extends Object> void addMapping(final AbstractMapping<ARG> mapping) {
    String _iD = mapping.getID();
    boolean _containsKey = this.mappings.containsKey(_iD);
    if (_containsKey) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Duplicate mapping id=");
      String _iD_1 = mapping.getID();
      _builder.append(_iD_1, "");
      _builder.append(" in ");
      _builder.append(this.ID, "");
      AbstractDiagramConfig.LOG.severe(_builder.toString());
    } else {
      String _iD_2 = mapping.getID();
      this.mappings.put(_iD_2, mapping);
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig");
    ;
  
  @Pure
  public String getID() {
    return this.ID;
  }
  
  public void setID(final String ID) {
    this.ID = ID;
  }
  
  @Pure
  public XtextDomainObjectProvider getDomainObjectProvider() {
    return this.domainObjectProvider;
  }
}
