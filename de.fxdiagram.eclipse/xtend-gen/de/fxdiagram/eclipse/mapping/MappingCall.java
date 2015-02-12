package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.eclipse.mapping.AbstractMapping;

/**
 * The execution of a {@link AbstractMapping} in a certain context.
 * 
 * Usually created in the {@link AbstractMapping#calls()} method by calling
 * factory methods
 * <ul>
 * <li>{@link DiagramMapping#nodeFor()},</li>
 * <li>{@link DiagramMapping#nodeForEach()},</li>
 * <li>{@link DiagramMapping#connectionFor()},</li>
 * <li>{@link DiagramMapping#connectionForEach()},</li>
 * <li>{@link NodeMapping#inConnectionFor()},</li>
 * <li>{@link NodeMapping#inConnectionForEach()},</li>
 * <li>{@link NodeMapping#outConnectionFor()},</li>
 * <li>{@link NodeMapping#outConnectionForEach()},</li>
 * <li>{@link NodeMapping#nestedDiagramFor()},</li>
 * <li>{@link ConnectionMapping#source()}, or</li>
 * <li>{@link ConnectionMapping#target()}.</li>
 * </ul>
 * 
 * Connection mappings can also be defined as lazy, deferring their execution to a user action.
 */
@SuppressWarnings("all")
public interface MappingCall<RESULT extends Object, ARG extends Object> {
  public abstract AbstractMapping<RESULT> getMapping();
}
