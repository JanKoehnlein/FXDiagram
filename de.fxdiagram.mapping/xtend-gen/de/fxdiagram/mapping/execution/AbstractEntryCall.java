package de.fxdiagram.mapping.execution;

import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.EntryCall;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.Pure;

@FinalFieldsConstructor
@Accessors(AccessorType.PUBLIC_GETTER)
@SuppressWarnings("all")
public abstract class AbstractEntryCall<ARG extends Object> implements EntryCall<ARG> {
  private final XDiagramConfig config;
  
  private final String text;
  
  public AbstractEntryCall(final XDiagramConfig config, final String text) {
    super();
    this.config = config;
    this.text = text;
  }
  
  @Pure
  public XDiagramConfig getConfig() {
    return this.config;
  }
  
  @Pure
  public String getText() {
    return this.text;
  }
}
