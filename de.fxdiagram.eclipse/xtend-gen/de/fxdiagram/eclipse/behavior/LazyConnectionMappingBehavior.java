package de.fxdiagram.eclipse.behavior;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.eclipse.behavior.LazyConnectionRapidButtonAction;
import de.fxdiagram.eclipse.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.eclipse.mapping.AbstractMapping;
import de.fxdiagram.eclipse.mapping.ConnectionMapping;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.eclipse.mapping.NodeMapping;
import de.fxdiagram.eclipse.mapping.XDiagramConfigInterpreter;
import de.fxdiagram.eclipse.shapes.INodeWithLazyMappings;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonBehavior;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Side;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * A {@link RapidButtonBehavior} to add lazy connection mappings to a node.
 * 
 * @see AbstractConnectionMappingCall
 */
@SuppressWarnings("all")
public class LazyConnectionMappingBehavior<ARG extends Object> extends RapidButtonBehavior<XNode> {
  private List<LazyConnectionRapidButtonAction<?, ARG>> actions = CollectionLiterals.<LazyConnectionRapidButtonAction<?, ARG>>newArrayList();
  
  public static <T extends Object> Behavior addLazyBehavior(final XNode node, final IMappedElementDescriptor<T> descriptor) {
    Behavior _xifexpression = null;
    AbstractMapping<T> _mapping = descriptor.getMapping();
    if ((_mapping instanceof NodeMapping<?>)) {
      Behavior _xblockexpression = null;
      {
        AbstractMapping<T> _mapping_1 = descriptor.getMapping();
        final NodeMapping<T> nodeMapping = ((NodeMapping<T>) _mapping_1);
        LazyConnectionMappingBehavior<T> lazyBehavior = null;
        List<AbstractConnectionMappingCall<?, T>> _outgoing = nodeMapping.getOutgoing();
        final Function1<AbstractConnectionMappingCall<?, T>, Boolean> _function = new Function1<AbstractConnectionMappingCall<?, T>, Boolean>() {
          @Override
          public Boolean apply(final AbstractConnectionMappingCall<?, T> it) {
            return Boolean.valueOf(it.isButton());
          }
        };
        final Iterable<AbstractConnectionMappingCall<?, T>> lazyOutgoing = IterableExtensions.<AbstractConnectionMappingCall<?, T>>filter(_outgoing, _function);
        boolean _isEmpty = IterableExtensions.isEmpty(lazyOutgoing);
        boolean _not = (!_isEmpty);
        if (_not) {
          LazyConnectionMappingBehavior<T> _elvis = null;
          if (lazyBehavior != null) {
            _elvis = lazyBehavior;
          } else {
            LazyConnectionMappingBehavior<T> _lazyConnectionMappingBehavior = new LazyConnectionMappingBehavior<T>(node);
            _elvis = _lazyConnectionMappingBehavior;
          }
          lazyBehavior = _elvis;
          for (final AbstractConnectionMappingCall<?, T> out : lazyOutgoing) {
            XDiagramConfigInterpreter _xDiagramConfigInterpreter = new XDiagramConfigInterpreter();
            List<Side> _buttonSides = LazyConnectionMappingBehavior.getButtonSides(node, out);
            lazyBehavior.addConnectionMappingCall(out, _xDiagramConfigInterpreter, true, ((Side[])Conversions.unwrapArray(_buttonSides, Side.class)));
          }
        }
        List<AbstractConnectionMappingCall<?, T>> _incoming = nodeMapping.getIncoming();
        final Function1<AbstractConnectionMappingCall<?, T>, Boolean> _function_1 = new Function1<AbstractConnectionMappingCall<?, T>, Boolean>() {
          @Override
          public Boolean apply(final AbstractConnectionMappingCall<?, T> it) {
            return Boolean.valueOf(it.isButton());
          }
        };
        final Iterable<AbstractConnectionMappingCall<?, T>> lazyIncoming = IterableExtensions.<AbstractConnectionMappingCall<?, T>>filter(_incoming, _function_1);
        boolean _isEmpty_1 = IterableExtensions.isEmpty(lazyIncoming);
        boolean _not_1 = (!_isEmpty_1);
        if (_not_1) {
          LazyConnectionMappingBehavior<T> _elvis_1 = null;
          if (lazyBehavior != null) {
            _elvis_1 = lazyBehavior;
          } else {
            LazyConnectionMappingBehavior<T> _lazyConnectionMappingBehavior_1 = new LazyConnectionMappingBehavior<T>(node);
            _elvis_1 = _lazyConnectionMappingBehavior_1;
          }
          lazyBehavior = _elvis_1;
          for (final AbstractConnectionMappingCall<?, T> in : lazyIncoming) {
            XDiagramConfigInterpreter _xDiagramConfigInterpreter_1 = new XDiagramConfigInterpreter();
            List<Side> _buttonSides_1 = LazyConnectionMappingBehavior.getButtonSides(node, in);
            lazyBehavior.addConnectionMappingCall(in, _xDiagramConfigInterpreter_1, false, ((Side[])Conversions.unwrapArray(_buttonSides_1, Side.class)));
          }
        }
        Behavior _xifexpression_1 = null;
        boolean _notEquals = (!Objects.equal(lazyBehavior, null));
        if (_notEquals) {
          _xifexpression_1 = node.addBehavior(lazyBehavior);
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  public static List<Side> getButtonSides(final XNode node, final AbstractConnectionMappingCall<?, ?> out) {
    List<Side> _xifexpression = null;
    if ((node instanceof INodeWithLazyMappings)) {
      ConnectionMapping<?> _connectionMapping = out.getConnectionMapping();
      _xifexpression = ((INodeWithLazyMappings)node).getButtonSides(_connectionMapping);
    } else {
      _xifexpression = Collections.<Side>unmodifiableList(CollectionLiterals.<Side>newArrayList(Side.TOP, Side.BOTTOM, Side.LEFT, Side.RIGHT));
    }
    return _xifexpression;
  }
  
  public LazyConnectionMappingBehavior(final XNode host) {
    super(host);
  }
  
  public boolean addConnectionMappingCall(final AbstractConnectionMappingCall<?, ARG> mappingCall, final XDiagramConfigInterpreter configInterpreter, final boolean hostIsSource, final Side... sides) {
    LazyConnectionRapidButtonAction<?, ARG> _createAction = this.createAction(mappingCall, configInterpreter, hostIsSource, sides);
    return this.actions.add(_createAction);
  }
  
  protected LazyConnectionRapidButtonAction<?, ARG> createAction(final AbstractConnectionMappingCall<?, ARG> mappingCall, final XDiagramConfigInterpreter configInterpreter, final boolean hostIsSource, final Side... sides) {
    LazyConnectionRapidButtonAction<?, ARG> _xblockexpression = null;
    {
      final LazyConnectionRapidButtonAction<?, ARG> action = new LazyConnectionRapidButtonAction(mappingCall, configInterpreter, hostIsSource);
      for (final Side side : sides) {
        XNode _host = this.getHost();
        Node _image = mappingCall.getImage(side);
        RapidButton _rapidButton = new RapidButton(_host, side, _image, action);
        this.add(_rapidButton);
      }
      _xblockexpression = action;
    }
    return _xblockexpression;
  }
  
  @Override
  protected void doActivate() {
    super.doActivate();
  }
}
