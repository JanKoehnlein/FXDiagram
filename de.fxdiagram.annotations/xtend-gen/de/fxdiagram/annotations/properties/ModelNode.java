package de.fxdiagram.annotations.properties;

import de.fxdiagram.annotations.properties.ModelNodeProcessor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.eclipse.xtend.lib.macro.Active;

@Active(ModelNodeProcessor.class)
@Target(ElementType.TYPE)
public @interface ModelNode {
  public String[] value() default {};
}
