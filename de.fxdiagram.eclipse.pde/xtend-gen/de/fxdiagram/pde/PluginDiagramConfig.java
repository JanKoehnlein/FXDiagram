package de.fxdiagram.pde;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.eclipse.mapping.AbstractDiagramConfig;
import de.fxdiagram.eclipse.mapping.ConnectionMapping;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.eclipse.mapping.MappingAcceptor;
import de.fxdiagram.eclipse.mapping.MultiConnectionMappingCall;
import de.fxdiagram.eclipse.mapping.NodeMapping;
import de.fxdiagram.eclipse.shapes.BaseConnection;
import de.fxdiagram.pde.PluginDependency;
import de.fxdiagram.pde.PluginDescriptor;
import de.fxdiagram.pde.PluginDescriptorProvider;
import de.fxdiagram.pde.PluginNode;
import de.fxdiagram.pde.PluginUtil;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.text.Text;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class PluginDiagramConfig extends AbstractDiagramConfig {
  protected IMappedElementDescriptorProvider createDomainObjectProvider() {
    return new PluginDescriptorProvider();
  }
  
  private final NodeMapping<IPluginModelBase> pluginNode = new NodeMapping<IPluginModelBase>(this, "pluginNode") {
    public XNode createNode(final IMappedElementDescriptor<IPluginModelBase> descriptor) {
      return new PluginNode(((PluginDescriptor) descriptor));
    }
    
    public void calls() {
      final Function1<IPluginModelBase, Iterable<? extends PluginDependency>> _function = new Function1<IPluginModelBase, Iterable<? extends PluginDependency>>() {
        public Iterable<? extends PluginDependency> apply(final IPluginModelBase it) {
          return PluginUtil.getDependencies(it);
        }
      };
      MultiConnectionMappingCall<PluginDependency, IPluginModelBase> _outConnectionForEach = this.<PluginDependency>outConnectionForEach(PluginDiagramConfig.this.dependencyConnection, _function);
      final Function1<Side, Node> _function_1 = new Function1<Side, Node>() {
        public Node apply(final Side it) {
          return ButtonExtensions.getArrowButton(it, "Add dependency");
        }
      };
      _outConnectionForEach.makeLazy(_function_1);
      final Function1<IPluginModelBase, Iterable<? extends PluginDependency>> _function_2 = new Function1<IPluginModelBase, Iterable<? extends PluginDependency>>() {
        public Iterable<? extends PluginDependency> apply(final IPluginModelBase it) {
          return PluginUtil.getInverseDependencies(it);
        }
      };
      MultiConnectionMappingCall<PluginDependency, IPluginModelBase> _inConnectionForEach = this.<PluginDependency>inConnectionForEach(PluginDiagramConfig.this.inverseDependencyConnection, _function_2);
      final Function1<Side, Node> _function_3 = new Function1<Side, Node>() {
        public Node apply(final Side it) {
          return ButtonExtensions.getInverseArrowButton(it, "Add inverse dependency");
        }
      };
      _inConnectionForEach.makeLazy(_function_3);
    }
  };
  
  public NodeMapping<IPluginModelBase> getPluginNode() {
    return this.pluginNode;
  }
  
  private final ConnectionMapping<PluginDependency> dependencyConnection = new ConnectionMapping<PluginDependency>(this, "dependencyConnection") {
    public void calls() {
      final Function1<PluginDependency, IPluginModelBase> _function = new Function1<PluginDependency, IPluginModelBase>() {
        public IPluginModelBase apply(final PluginDependency it) {
          return it.getDependency();
        }
      };
      this.<IPluginModelBase>target(PluginDiagramConfig.this.pluginNode, _function);
    }
    
    public XConnection createConnection(final IMappedElementDescriptor<PluginDependency> descriptor) {
      return PluginDiagramConfig.this.createPluginImportConnection(descriptor);
    }
  };
  
  public ConnectionMapping<PluginDependency> getDependencyConnection() {
    return this.dependencyConnection;
  }
  
  private final ConnectionMapping<PluginDependency> inverseDependencyConnection = new ConnectionMapping<PluginDependency>(this, "inverseDependencyConnection") {
    public void calls() {
      final Function1<PluginDependency, IPluginModelBase> _function = new Function1<PluginDependency, IPluginModelBase>() {
        public IPluginModelBase apply(final PluginDependency it) {
          return it.getOwner();
        }
      };
      this.<IPluginModelBase>source(PluginDiagramConfig.this.pluginNode, _function);
    }
    
    public XConnection createConnection(final IMappedElementDescriptor<PluginDependency> descriptor) {
      return PluginDiagramConfig.this.createPluginImportConnection(descriptor);
    }
  };
  
  public ConnectionMapping<PluginDependency> getInverseDependencyConnection() {
    return this.inverseDependencyConnection;
  }
  
  protected BaseConnection<PluginDependency> createPluginImportConnection(final IMappedElementDescriptor<PluginDependency> descriptor) {
    BaseConnection<PluginDependency> _baseConnection = new BaseConnection<PluginDependency>(descriptor);
    final Procedure1<BaseConnection<PluginDependency>> _function = new Procedure1<BaseConnection<PluginDependency>>() {
      public void apply(final BaseConnection<PluginDependency> connection) {
        final XConnectionLabel label = new XConnectionLabel(connection);
        final Function1<PluginDependency, Object> _function = new Function1<PluginDependency, Object>() {
          public Object apply(final PluginDependency it) {
            Object _xblockexpression = null;
            {
              VersionRange _versionRange = it.getVersionRange();
              boolean _isEmpty = _versionRange.isEmpty();
              boolean _not = (!_isEmpty);
              if (_not) {
                Text _text = label.getText();
                VersionRange _versionRange_1 = it.getVersionRange();
                String _string = _versionRange_1.toString();
                _text.setText(_string);
              }
              boolean _isOptional = it.isOptional();
              if (_isOptional) {
                ObservableList<Double> _strokeDashArray = connection.getStrokeDashArray();
                _strokeDashArray.setAll(Double.valueOf(5.0), Double.valueOf(5.0));
              }
              boolean _isReexport = it.isReexport();
              boolean _not_1 = (!_isReexport);
              if (_not_1) {
                LineArrowHead _lineArrowHead = new LineArrowHead(connection, false);
                connection.setTargetArrowHead(_lineArrowHead);
              }
              _xblockexpression = null;
            }
            return _xblockexpression;
          }
        };
        descriptor.<Object>withDomainObject(_function);
      }
    };
    return ObjectExtensions.<BaseConnection<PluginDependency>>operator_doubleArrow(_baseConnection, _function);
  }
  
  protected <ARG extends Object> void entryCalls(final ARG domainArgument, @Extension final MappingAcceptor<ARG> acceptor) {
    boolean _matched = false;
    if (!_matched) {
      if (domainArgument instanceof IPluginModelBase) {
        _matched=true;
        acceptor.add(this.pluginNode);
      }
    }
  }
}
