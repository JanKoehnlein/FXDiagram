package de.fxdiagram.examples.lcars;

import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.lcars.LcarsField;
import javafx.concurrent.Task;

@SuppressWarnings("all")
public class LcarsQueryTask extends Task<Void> {
  private LcarsField host;
  
  private String fieldName;
  
  private String fieldValue;
  
  private ChooserConnectionProvider connectionProvider;
  
  public LcarsQueryTask(final LcarsField host, final String fieldName, final String fieldValue, final ChooserConnectionProvider connectionProvider) {
    this.host = host;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
    this.connectionProvider = connectionProvider;
  }
  
  protected Void call() throws Exception {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field key is undefined for the type LcarsQueryTask"
      + "\nThe method or field key is undefined for the type LcarsQueryTask"
      + "\n== cannot be resolved"
      + "\n== cannot be resolved");
  }
}
