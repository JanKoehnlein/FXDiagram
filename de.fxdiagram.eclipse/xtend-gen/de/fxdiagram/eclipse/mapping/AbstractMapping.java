package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.eclipse.mapping.XDiagramConfig;

@SuppressWarnings("all")
public abstract class AbstractMapping<T extends Object> {
  private String id;
  
  private XDiagramConfig config;
  
  public AbstractMapping(final XDiagramConfig config, final String id) {
    this.config = config;
    this.id = id;
    config.<T>addMapping(this);
  }
  
  public XDiagramConfig getConfig() {
    return this.config;
  }
  
  private boolean initialized = false;
  
  protected final boolean initialize() {
    boolean _xblockexpression = false;
    {
      if ((!this.initialized)) {
        this.calls();
      }
      _xblockexpression = this.initialized = true;
    }
    return _xblockexpression;
  }
  
  protected void calls() {
  }
  
  public boolean isApplicable(final Object domainObject) {
    return true;
  }
  
  public String getID() {
    return this.id;
  }
}
