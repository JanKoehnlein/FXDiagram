package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.eclipse.mapping.XDiagramConfig;

/**
 * Describes a fixed mapping of a domain object represented by a {@link IMappedElementDescriptor}
 * to a diagram element.
 * 
 * Mappings are collected in an {@link XDiagramConfiguration}.
 */
@SuppressWarnings("all")
public abstract class AbstractMapping<T extends Object> {
  private String id;
  
  private String displayName;
  
  private XDiagramConfig config;
  
  public AbstractMapping(final XDiagramConfig config, final String id, final String displayName) {
    this.config = config;
    this.id = id;
    this.displayName = displayName;
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
  
  /**
   * Executed when a mapping of this type is established. Add calls to other mappings here,
   * if you want to recursively add further elements automatically.
   */
  protected void calls() {
  }
  
  public boolean isApplicable(final Object domainObject) {
    return true;
  }
  
  public String getID() {
    return this.id;
  }
  
  public String getDisplayName() {
    return this.displayName;
  }
}
