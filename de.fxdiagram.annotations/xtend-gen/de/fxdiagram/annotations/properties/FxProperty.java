package de.fxdiagram.annotations.properties;

import de.fxdiagram.annotations.properties.FxPropertyCompilationParticipant;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.eclipse.xtend.lib.macro.Active;

/**
 * An active annotation which turns simple fields into
 * lazy JavaFX properties as described
 * <a href="http://blog.netopyr.com/2011/05/19/creating-javafx-properties/">here</a>.
 * 
 * That is it
 * <ul>
 *  <li> adds a field with the corresponding JavaFX property type,
 *  <li> a getter method
 *  <li> a setter method
 *  <li> and an accessor to the JavaFX property.
 * </ul>
 */
@Active(FxPropertyCompilationParticipant.class)
@Target(ElementType.FIELD)
public @interface FxProperty {
  public boolean readOnly() default false;
}
