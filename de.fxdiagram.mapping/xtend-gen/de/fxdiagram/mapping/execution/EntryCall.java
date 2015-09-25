package de.fxdiagram.mapping.execution;

import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.InterpreterContext;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;

@SuppressWarnings("all")
public interface EntryCall<ARG extends Object> {
  public abstract XDiagramConfig getConfig();
  
  public abstract void execute(final ARG domainObject, final XDiagramConfigInterpreter interpreter, final InterpreterContext context);
  
  public abstract String getText();
}
