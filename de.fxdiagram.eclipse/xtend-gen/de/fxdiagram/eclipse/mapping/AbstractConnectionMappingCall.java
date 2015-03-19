package de.fxdiagram.eclipse.mapping;

import com.google.common.base.Objects;
import de.fxdiagram.eclipse.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.eclipse.mapping.AbstractMapping;
import de.fxdiagram.eclipse.mapping.ConnectionMapping;
import de.fxdiagram.eclipse.mapping.MappingCall;
import de.fxdiagram.lib.buttons.RapidButton;
import javafx.geometry.Side;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public abstract class AbstractConnectionMappingCall<RESULT extends Object, ARG extends Object> implements MappingCall<RESULT, ARG> {
  private Function1<? super Side, ? extends Node> imageFactory;
  
  public boolean isLazy() {
    return (!Objects.equal(this.imageFactory, null));
  }
  
  /**
   * Instead of immediately adding this connection and the connected node, create
   * a {@link LazyConnectionMappingBehavior} that allows to explore this connection
   * using a {@link RapidButton}.
   */
  public Function1<? super Side, ? extends Node> asButton(final Function1<? super Side, ? extends Node> imageFactory) {
    return this.imageFactory = imageFactory;
  }
  
  public Node getImage(final Side side) {
    return this.imageFactory.apply(side);
  }
  
  public abstract ConnectionMapping<RESULT> getConnectionMapping();
  
  @Override
  public AbstractMapping<RESULT> getMapping() {
    return this.getConnectionMapping();
  }
}
