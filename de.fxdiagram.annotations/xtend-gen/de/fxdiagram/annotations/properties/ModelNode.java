package de.fxdiagram.annotations.properties;

import de.fxdiagram.annotations.properties.ModelNodeProcessor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.eclipse.xtend.lib.macro.Active;

/**
 * An active annotation that makes the annotated type as serializable by implementing
 * {@link XModelProvider} and adding all properties listed in the {@link value} field.
 * The properties' types must be primitive or implement {@link XModelProvider}.
 * 
 * @see de.fxdiagram.core.model
 */
@Active(ModelNodeProcessor.class)
@Target(ElementType.TYPE)
public @interface ModelNode {
  public String[] value() default {};
  public boolean inherit() default true;
}
