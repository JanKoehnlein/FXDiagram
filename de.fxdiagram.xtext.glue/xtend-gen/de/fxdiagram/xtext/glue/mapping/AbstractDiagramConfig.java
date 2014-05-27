package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@Logging
@SuppressWarnings("all")
public class AbstractDiagramConfig implements XDiagramConfig {
  private Map<String, AbstractMapping<?>> mappings = CollectionLiterals.<String, AbstractMapping<?>>newHashMap();
  
  private String _ID;
  
  public String getID() {
    return this._ID;
  }
  
  public void setID(final String ID) {
    this._ID = ID;
  }
  
  public <T extends Object> Iterable<? extends AbstractMapping<T>> getMappings(final T domainObject) {
    Collection<AbstractMapping<?>> _values = this.mappings.values();
    final Function1<AbstractMapping<?>, Boolean> _function = new Function1<AbstractMapping<?>, Boolean>() {
      public Boolean apply(final AbstractMapping<?> it) {
        return Boolean.valueOf(it.isApplicable(domainObject));
      }
    };
    Iterable<AbstractMapping<?>> _filter = IterableExtensions.<AbstractMapping<?>>filter(_values, _function);
    final Function1<AbstractMapping<?>, AbstractMapping<T>> _function_1 = new Function1<AbstractMapping<?>, AbstractMapping<T>>() {
      public AbstractMapping<T> apply(final AbstractMapping<?> it) {
        return ((AbstractMapping<T>) it);
      }
    };
    return IterableExtensions.<AbstractMapping<?>, AbstractMapping<T>>map(_filter, _function_1);
  }
  
  public AbstractMapping<?> getMappingByID(final String mappingID) {
    return this.mappings.get(mappingID);
  }
  
  public AbstractMapping<?> addMapping(final AbstractMapping<?> mapping) {
    AbstractMapping<?> _xifexpression = null;
    String _iD = mapping.getID();
    boolean _containsKey = this.mappings.containsKey(_iD);
    if (_containsKey) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Duplicate mapping id=");
      String _iD_1 = mapping.getID();
      _builder.append(_iD_1, "");
      _builder.append(" in ");
      String _iD_2 = this.getID();
      _builder.append(_iD_2, "");
      AbstractDiagramConfig.LOG.severe(_builder.toString());
    } else {
      AbstractMapping<?> _xblockexpression = null;
      {
        mapping.setConfig(this);
        String _iD_3 = mapping.getID();
        _xblockexpression = this.mappings.put(_iD_3, mapping);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig");
    ;
}
