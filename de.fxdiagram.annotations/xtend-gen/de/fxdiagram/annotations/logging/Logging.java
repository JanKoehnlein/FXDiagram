package de.fxdiagram.annotations.logging;

import de.fxdiagram.annotations.logging.LoggingProcessor;
import java.util.logging.Logger;
import org.eclipse.xtend.lib.macro.Active;

/**
 * Active annotation that adds a static final {@link Logger} to the annotated type.
 */
@Active(LoggingProcessor.class)
public @interface Logging {
}
