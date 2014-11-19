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
import de.fxdiagram.pde.BundleDependency;
import de.fxdiagram.pde.BundleDescriptor;
import de.fxdiagram.pde.BundleDescriptorProvider;
import de.fxdiagram.pde.BundleNode;
import de.fxdiagram.pde.BundleUtil;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.text.Text;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class BundleDiagramConfig extends AbstractDiagramConfig {
  protected IMappedElementDescriptorProvider createDomainObjectProvider() {
    return new BundleDescriptorProvider();
  }
  
  private final NodeMapping<BundleDescription> pluginNode = new NodeMapping<BundleDescription>(this, "pluginNode") {
    public XNode createNode(final IMappedElementDescriptor<BundleDescription> descriptor) {
      return new BundleNode(((BundleDescriptor) descriptor));
    }
    
    public void calls() {
      final Function1<BundleDescription, Iterable<? extends BundleDependency>> _function = new Function1<BundleDescription, Iterable<? extends BundleDependency>>() {
        public Iterable<? extends BundleDependency> apply(final BundleDescription it) {
          return BundleUtil.getBundleDependencies(it);
        }
      };
      MultiConnectionMappingCall<BundleDependency, BundleDescription> _outConnectionForEach = this.<BundleDependency>outConnectionForEach(BundleDiagramConfig.this.dependencyConnection, _function);
      final Function1<Side, Node> _function_1 = new Function1<Side, Node>() {
        public Node apply(final Side it) {
          return ButtonExtensions.getArrowButton(it, "Add dependency");
        }
      };
      _outConnectionForEach.makeLazy(_function_1);
      final Function1<BundleDescription, Iterable<? extends BundleDependency>> _function_2 = new Function1<BundleDescription, Iterable<? extends BundleDependency>>() {
        public Iterable<? extends BundleDependency> apply(final BundleDescription it) {
          return BundleUtil.getInverseBundleDependencies(it);
        }
      };
      MultiConnectionMappingCall<BundleDependency, BundleDescription> _inConnectionForEach = this.<BundleDependency>inConnectionForEach(BundleDiagramConfig.this.inverseDependencyConnection, _function_2);
      final Function1<Side, Node> _function_3 = new Function1<Side, Node>() {
        public Node apply(final Side it) {
          return ButtonExtensions.getInverseArrowButton(it, "Add inverse dependency");
        }
      };
      _inConnectionForEach.makeLazy(_function_3);
    }
  };
  
  public NodeMapping<BundleDescription> getPluginNode() {
    return this.pluginNode;
  }
  
  private final ConnectionMapping<BundleDependency> dependencyConnection = new ConnectionMapping<BundleDependency>(this, "dependencyConnection") {
    public void calls() {
      final Function1<BundleDependency, BundleDescription> _function = new Function1<BundleDependency, BundleDescription>() {
        public BundleDescription apply(final BundleDependency it) {
          return it.getDependency();
        }
      };
      this.<BundleDescription>target(BundleDiagramConfig.this.pluginNode, _function);
    }
    
    public XConnection createConnection(final IMappedElementDescriptor<BundleDependency> descriptor) {
      return BundleDiagramConfig.this.createPluginImportConnection(descriptor);
    }
  };
  
  public ConnectionMapping<BundleDependency> getDependencyConnection() {
    return this.dependencyConnection;
  }
  
  private final ConnectionMapping<BundleDependency> inverseDependencyConnection = new ConnectionMapping<BundleDependency>(this, "inverseDependencyConnection") {
    public void calls() {
      final Function1<BundleDependency, BundleDescription> _function = new Function1<BundleDependency, BundleDescription>() {
        public BundleDescription apply(final BundleDependency it) {
          return it.getOwner();
        }
      };
      this.<BundleDescription>source(BundleDiagramConfig.this.pluginNode, _function);
    }
    
    public XConnection createConnection(final IMappedElementDescriptor<BundleDependency> descriptor) {
      return BundleDiagramConfig.this.createPluginImportConnection(descriptor);
    }
  };
  
  public ConnectionMapping<BundleDependency> getInverseDependencyConnection() {
    return this.inverseDependencyConnection;
  }
  
  protected BaseConnection<BundleDependency> createPluginImportConnection(final IMappedElementDescriptor<BundleDependency> descriptor) {
    BaseConnection<BundleDependency> _baseConnection = new BaseConnection<BundleDependency>(descriptor);
    final Procedure1<BaseConnection<BundleDependency>> _function = new Procedure1<BaseConnection<BundleDependency>>() {
      public void apply(final BaseConnection<BundleDependency> connection) {
        final XConnectionLabel label = new XConnectionLabel(connection);
        final Function1<BundleDependency, Object> _function = new Function1<BundleDependency, Object>() {
          public Object apply(final BundleDependency it) {
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
    return ObjectExtensions.<BaseConnection<BundleDependency>>operator_doubleArrow(_baseConnection, _function);
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
