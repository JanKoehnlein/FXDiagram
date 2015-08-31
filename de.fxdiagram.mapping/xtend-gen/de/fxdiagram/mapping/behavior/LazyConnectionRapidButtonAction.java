package de.fxdiagram.mapping.behavior;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import de.fxdiagram.lib.chooser.CarusselChoice;
import de.fxdiagram.lib.chooser.ChooserConnectionProvider;
import de.fxdiagram.lib.chooser.ConnectedNodeChooser;
import de.fxdiagram.lib.chooser.CoverFlowChoice;
import de.fxdiagram.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.XDiagramConfigInterpreter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LazyConnectionRapidButtonAction<MODEL extends Object, ARG extends Object> extends RapidButtonAction {
  private XDiagramConfigInterpreter configInterpreter;
  
  private AbstractConnectionMappingCall<MODEL, ARG> mappingCall;
  
  private boolean hostIsSource;
  
  public LazyConnectionRapidButtonAction(final AbstractConnectionMappingCall<MODEL, ARG> mappingCall, final XDiagramConfigInterpreter configInterpreter, final boolean hostIsSource) {
    this.mappingCall = mappingCall;
    this.configInterpreter = configInterpreter;
    this.hostIsSource = hostIsSource;
  }
  
  @Override
  public boolean isEnabled(final XNode host) {
    DomainObjectDescriptor _domainObject = host.getDomainObject();
    final IMappedElementDescriptor<ARG> hostDescriptor = ((IMappedElementDescriptor<ARG>) _domainObject);
    final XDiagram diagram = CoreExtensions.getDiagram(host);
    boolean _equals = Objects.equal(diagram, null);
    if (_equals) {
      return false;
    }
    ObservableList<XConnection> _connections = diagram.getConnections();
    final Function1<XConnection, DomainObjectDescriptor> _function = (XConnection it) -> {
      return it.getDomainObject();
    };
    List<DomainObjectDescriptor> _map = ListExtensions.<XConnection, DomainObjectDescriptor>map(_connections, _function);
    final Set<DomainObjectDescriptor> existingConnectionDescriptors = IterableExtensions.<DomainObjectDescriptor>toSet(_map);
    try {
      final Function1<ARG, Boolean> _function_1 = (ARG domainArgument) -> {
        final Iterable<MODEL> connectionDomainObjects = this.configInterpreter.<MODEL, ARG>select(this.mappingCall, domainArgument);
        for (final MODEL connectionDomainObject : connectionDomainObjects) {
          {
            ConnectionMapping<MODEL> _connectionMapping = this.mappingCall.getConnectionMapping();
            final IMappedElementDescriptor<MODEL> connectionDescriptor = this.configInterpreter.<MODEL>getDescriptor(connectionDomainObject, _connectionMapping);
            boolean _add = existingConnectionDescriptors.add(connectionDescriptor);
            if (_add) {
              NodeMappingCall<?, MODEL> _xifexpression = null;
              if (this.hostIsSource) {
                ConnectionMapping<MODEL> _connectionMapping_1 = this.mappingCall.getConnectionMapping();
                _xifexpression = _connectionMapping_1.getTarget();
              } else {
                ConnectionMapping<MODEL> _connectionMapping_2 = this.mappingCall.getConnectionMapping();
                _xifexpression = _connectionMapping_2.getSource();
              }
              final NodeMappingCall<?, MODEL> nodeMappingCall = _xifexpression;
              final Iterable<?> nodeDomainObjects = this.configInterpreter.select(nodeMappingCall, connectionDomainObject);
              boolean _isEmpty = IterableExtensions.isEmpty(nodeDomainObjects);
              boolean _not = (!_isEmpty);
              if (_not) {
                return Boolean.valueOf(true);
              }
            }
          }
        }
        return Boolean.valueOf(false);
      };
      final Boolean result = hostDescriptor.<Boolean>withDomainObject(_function_1);
      Boolean _xifexpression = null;
      boolean _equals_1 = Objects.equal(result, null);
      if (_equals_1) {
        _xifexpression = Boolean.valueOf(false);
      } else {
        _xifexpression = result;
      }
      return (_xifexpression).booleanValue();
    } catch (final Throwable _t) {
      if (_t instanceof NoSuchElementException) {
        final NoSuchElementException e = (NoSuchElementException)_t;
        return false;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  @Override
  public void perform(final RapidButton button) {
    final ConnectedNodeChooser chooser = this.createChooser(button);
    XNode _host = button.getHost();
    this.populateChooser(chooser, _host);
    XNode _host_1 = button.getHost();
    XRoot _root = CoreExtensions.getRoot(_host_1);
    _root.setCurrentTool(chooser);
  }
  
  protected ConnectedNodeChooser createChooser(final RapidButton button) {
    ConnectedNodeChooser _xblockexpression = null;
    {
      final Side position = button.getPosition();
      ConnectedNodeChooser _xifexpression = null;
      boolean _isVertical = position.isVertical();
      if (_isVertical) {
        XNode _host = button.getHost();
        CarusselChoice _carusselChoice = new CarusselChoice();
        _xifexpression = new ConnectedNodeChooser(_host, position, _carusselChoice);
      } else {
        XNode _host_1 = button.getHost();
        CoverFlowChoice _coverFlowChoice = new CoverFlowChoice();
        _xifexpression = new ConnectedNodeChooser(_host_1, position, _coverFlowChoice);
      }
      final ConnectedNodeChooser chooser = _xifexpression;
      _xblockexpression = chooser;
    }
    return _xblockexpression;
  }
  
  protected Object populateChooser(final ConnectedNodeChooser chooser, final XNode host) {
    Object _xblockexpression = null;
    {
      DomainObjectDescriptor _domainObject = host.getDomainObject();
      final IMappedElementDescriptor<ARG> hostDescriptor = ((IMappedElementDescriptor<ARG>) _domainObject);
      XDiagram _diagram = CoreExtensions.getDiagram(host);
      ObservableList<XConnection> _connections = _diagram.getConnections();
      final Function1<XConnection, DomainObjectDescriptor> _function = (XConnection it) -> {
        return it.getDomainObject();
      };
      List<DomainObjectDescriptor> _map = ListExtensions.<XConnection, DomainObjectDescriptor>map(_connections, _function);
      final Set<DomainObjectDescriptor> existingConnectionDescriptors = IterableExtensions.<DomainObjectDescriptor>toSet(_map);
      final Function1<ARG, Object> _function_1 = (ARG domainArgument) -> {
        Object _xblockexpression_1 = null;
        {
          final Iterable<MODEL> connectionDomainObjects = this.configInterpreter.<MODEL, ARG>select(this.mappingCall, domainArgument);
          final Consumer<MODEL> _function_2 = (MODEL connectionDomainObject) -> {
            ConnectionMapping<MODEL> _connectionMapping = this.mappingCall.getConnectionMapping();
            final IMappedElementDescriptor<MODEL> connectionDescriptor = this.configInterpreter.<MODEL>getDescriptor(connectionDomainObject, _connectionMapping);
            boolean _add = existingConnectionDescriptors.add(connectionDescriptor);
            if (_add) {
              NodeMappingCall<?, MODEL> _xifexpression = null;
              if (this.hostIsSource) {
                ConnectionMapping<MODEL> _connectionMapping_1 = this.mappingCall.getConnectionMapping();
                _xifexpression = _connectionMapping_1.getTarget();
              } else {
                ConnectionMapping<MODEL> _connectionMapping_2 = this.mappingCall.getConnectionMapping();
                _xifexpression = _connectionMapping_2.getSource();
              }
              final NodeMappingCall<?, MODEL> nodeMappingCall = _xifexpression;
              final Iterable<?> nodeDomainObjects = this.configInterpreter.select(nodeMappingCall, connectionDomainObject);
              final Consumer<Object> _function_3 = (Object it) -> {
                NodeMapping<?> _nodeMapping = nodeMappingCall.getNodeMapping();
                XNode _createNode = this.<Object>createNode(it, _nodeMapping);
                chooser.addChoice(_createNode, connectionDescriptor);
              };
              nodeDomainObjects.forEach(_function_3);
            }
          };
          connectionDomainObjects.forEach(_function_2);
          final ChooserConnectionProvider _function_3 = (XNode thisNode, XNode thatNode, DomainObjectDescriptor connectionDesc) -> {
            XConnection _xblockexpression_2 = null;
            {
              final IMappedElementDescriptor<MODEL> descriptor = ((IMappedElementDescriptor<MODEL>) connectionDesc);
              ConnectionMapping<MODEL> _connectionMapping = this.mappingCall.getConnectionMapping();
              XConnection _createConnection = _connectionMapping.createConnection(descriptor);
              final Procedure1<XConnection> _function_4 = (XConnection it) -> {
                if (this.hostIsSource) {
                  it.setSource(thisNode);
                  it.setTarget(thatNode);
                } else {
                  it.setSource(thatNode);
                  it.setTarget(thisNode);
                }
                ConnectionMapping<MODEL> _connectionMapping_1 = this.mappingCall.getConnectionMapping();
                XDiagramConfig _config = _connectionMapping_1.getConfig();
                _config.initialize(it);
              };
              _xblockexpression_2 = ObjectExtensions.<XConnection>operator_doubleArrow(_createConnection, _function_4);
            }
            return _xblockexpression_2;
          };
          chooser.setConnectionProvider(_function_3);
          _xblockexpression_1 = null;
        }
        return _xblockexpression_1;
      };
      _xblockexpression = hostDescriptor.<Object>withDomainObject(_function_1);
    }
    return _xblockexpression;
  }
  
  protected <NODE extends Object> XNode createNode(final Object nodeDomainObject, final NodeMapping<?> nodeMapping) {
    XNode _xifexpression = null;
    boolean _isApplicable = nodeMapping.isApplicable(nodeDomainObject);
    if (_isApplicable) {
      XNode _xblockexpression = null;
      {
        final NodeMapping<NODE> nodeMappingCasted = ((NodeMapping<NODE>) nodeMapping);
        final IMappedElementDescriptor<NODE> descriptor = this.configInterpreter.<NODE>getDescriptor(((NODE) nodeDomainObject), nodeMappingCasted);
        final XNode node = nodeMappingCasted.createNode(descriptor);
        XDiagramConfig _config = nodeMappingCasted.getConfig();
        _config.initialize(node);
        _xblockexpression = node;
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
}
