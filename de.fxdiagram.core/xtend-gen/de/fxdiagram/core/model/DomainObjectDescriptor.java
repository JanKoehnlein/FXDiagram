package de.fxdiagram.core.model;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.model.XModelProvider;

/**
 * Links a domain object (some POJO) to to an {@link XNode} or an {@link XConnection}.
 * 
 * A {@link DomainObjectProvider} translates between {@link DomainObjectDescriptor}s and
 * the real domain object. The descriptor must contain all information needed to recover
 * the domain object.
 * 
 * Subclasses should implement {@link #equals(Object)} and {@link #hashCode()} to detect
 * equality without the need of a transaction.
 * 
 * This indirection serves two purposes:
 * <ol>
 * <li>We cannot make assumptions on how a domain object can be serialized. So when we
 * store a diagram, we store the descriptors instead.</li>
 * <li>Domain objects might have different lifecycles than their associated diagram elements,
 * i.e. it may be forbidden to store a reference to the real domain object across transaction
 * boundaries.</li>
 * </ol>
 */
@SuppressWarnings("all")
public interface DomainObjectDescriptor extends XModelProvider {
  /**
   * @returns a human readable name for the associated domain object
   */
  public abstract String getName();
}
