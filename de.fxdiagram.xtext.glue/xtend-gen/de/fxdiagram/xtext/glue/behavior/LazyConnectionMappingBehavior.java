package de.fxdiagram.xtext.glue.behavior;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonBehavior;
import de.fxdiagram.xtext.glue.behavior.LazyConnectionRapidButtonAction;
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfigInterpreter;
import java.util.List;
import javafx.geometry.Side;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class LazyConnectionMappingBehavior<ARG extends Object> extends RapidButtonBehavior<XNode> {
  private List<LazyConnectionRapidButtonAction<?, ARG>> actions = CollectionLiterals.<LazyConnectionRapidButtonAction<?, ARG>>newArrayList();
  
  public static <T extends Object> Behavior addLazyBehavior(final XNode node, final IMappedElementDescriptor descriptor) {
    Behavior _xifexpression = null;
    AbstractMapping _mapping = descriptor.getMapping();
    if ((_mapping instanceof NodeMapping<?>)) {
      Behavior _xblockexpression = null;
      {
        AbstractMapping _mapping_1 = descriptor.getMapping();
        final NodeMapping<T> nodeMapping = ((NodeMapping<T>) _mapping_1);
        LazyConnectionMappingBehavior<T> lazyBehavior = null;
        List<AbstractConnectionMappingCall<?, T>> _outgoing = nodeMapping.getOutgoing();
        final Function1<AbstractConnectionMappingCall<?, T>, Boolean> _function = new Function1<AbstractConnectionMappingCall<?, T>, Boolean>() {
          public Boolean apply(final AbstractConnectionMappingCall<?, T> it) {
            return Boolean.valueOf(it.isLazy());
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
            lazyBehavior.addConnectionMappingCall(out, _xDiagramConfigInterpreter, true);
          }
        }
        List<AbstractConnectionMappingCall<?, T>> _incoming = nodeMapping.getIncoming();
        final Function1<AbstractConnectionMappingCall<?, T>, Boolean> _function_1 = new Function1<AbstractConnectionMappingCall<?, T>, Boolean>() {
          public Boolean apply(final AbstractConnectionMappingCall<?, T> it) {
            return Boolean.valueOf(it.isLazy());
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
            lazyBehavior.addConnectionMappingCall(in, _xDiagramConfigInterpreter_1, false);
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
  
  public LazyConnectionMappingBehavior(final XNode host) {
    super(host);
  }
  
  public boolean addConnectionMappingCall(final AbstractConnectionMappingCall<?, ARG> mappingCall, final XDiagramConfigInterpreter configInterpreter, final boolean hostIsSource) {
    LazyConnectionRapidButtonAction<?, ARG> _createAction = this.createAction(mappingCall, configInterpreter, hostIsSource);
    return this.actions.add(_createAction);
  }
  
  protected LazyConnectionRapidButtonAction<?, ARG> createAction(final AbstractConnectionMappingCall<?, ARG> mappingCall, final XDiagramConfigInterpreter configInterpreter, final boolean hostIsSource) {
    LazyConnectionRapidButtonAction<?, ARG> _xblockexpression = null;
    {
      final LazyConnectionRapidButtonAction<?, ARG> action = new LazyConnectionRapidButtonAction(mappingCall, configInterpreter, hostIsSource);
      XNode _host = this.getHost();
      Node _image = mappingCall.getImage(Side.LEFT);
      RapidButton _rapidButton = new RapidButton(_host, Side.LEFT, _image, action);
      this.add(_rapidButton);
      XNode _host_1 = this.getHost();
      Node _image_1 = mappingCall.getImage(Side.RIGHT);
      RapidButton _rapidButton_1 = new RapidButton(_host_1, Side.RIGHT, _image_1, action);
      this.add(_rapidButton_1);
      XNode _host_2 = this.getHost();
      Node _image_2 = mappingCall.getImage(Side.TOP);
      RapidButton _rapidButton_2 = new RapidButton(_host_2, Side.TOP, _image_2, action);
      this.add(_rapidButton_2);
      XNode _host_3 = this.getHost();
      Node _image_3 = mappingCall.getImage(Side.BOTTOM);
      RapidButton _rapidButton_3 = new RapidButton(_host_3, Side.BOTTOM, _image_3, action);
      this.add(_rapidButton_3);
      _xblockexpression = action;
    }
    return _xblockexpression;
  }
  
  protected void doActivate() {
    super.doActivate();
  }
}
