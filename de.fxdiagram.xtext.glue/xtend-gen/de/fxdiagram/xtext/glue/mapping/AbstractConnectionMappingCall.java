package de.fxdiagram.xtext.glue.mapping;

import com.google.common.base.Objects;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.MappingCall;
import javafx.geometry.Side;
import javafx.scene.Node;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public abstract class AbstractConnectionMappingCall<T extends Object, ARG extends Object> implements MappingCall<T, ARG> {
  private Function1<? super Side, ? extends Node> imageFactory;
  
  public boolean isLazy() {
    return (!Objects.equal(this.imageFactory, null));
  }
  
  public Function1<? super Side, ? extends Node> makeLazy(final Function1<? super Side, ? extends Node> imageFactory) {
    return this.imageFactory = imageFactory;
  }
  
  public Node getImage(final Side side) {
    return this.imageFactory.apply(side);
  }
  
  @Accessors
  private String role;
  
  public abstract ConnectionMapping<T> getConnectionMapping();
  
  public AbstractMapping<T> getMapping() {
    return this.getConnectionMapping();
  }
  
  @Pure
  public String getRole() {
    return this.role;
  }
  
  public void setRole(final String role) {
    this.role = role;
  }
}
