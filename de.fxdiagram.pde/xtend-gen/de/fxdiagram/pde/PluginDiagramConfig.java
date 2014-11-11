package de.fxdiagram.pde;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.pde.PluginDescriptor;
import de.fxdiagram.pde.PluginDescriptorProvider;
import de.fxdiagram.pde.PluginNode;
import de.fxdiagram.pde.PluginUtil;
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.xtext.glue.mapping.MappingAcceptor;
import de.fxdiagram.xtext.glue.mapping.MultiConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.shapes.BaseConnection;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.text.Text;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.osgi.framework.Version;

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
      final Function1<IPluginModelBase, Iterable<? extends IPluginImport>> _function = new Function1<IPluginModelBase, Iterable<? extends IPluginImport>>() {
        public Iterable<? extends IPluginImport> apply(final IPluginModelBase it) {
          IPluginBase _pluginBase = it.getPluginBase();
          IPluginImport[] _imports = _pluginBase.getImports();
          List<IPluginImport> _list = IterableExtensions.<IPluginImport>toList(((Iterable<IPluginImport>)Conversions.doWrapArray(_imports)));
          final Function1<IPluginImport, Boolean> _function = new Function1<IPluginImport, Boolean>() {
            public Boolean apply(final IPluginImport it) {
              String _id = it.getId();
              String _version = it.getVersion();
              IPluginModelBase _findPlugin = PluginUtil.findPlugin(_id, _version);
              return Boolean.valueOf((!Objects.equal(_findPlugin, null)));
            }
          };
          return IterableExtensions.<IPluginImport>filter(_list, _function);
        }
      };
      MultiConnectionMappingCall<IPluginImport, IPluginModelBase> _outConnectionForEach = this.<IPluginImport>outConnectionForEach(PluginDiagramConfig.this.importConnection, _function);
      final Function1<Side, Node> _function_1 = new Function1<Side, Node>() {
        public Node apply(final Side it) {
          return ButtonExtensions.getArrowButton(it, "Add dependency");
        }
      };
      _outConnectionForEach.makeLazy(_function_1);
      final Function1<IPluginModelBase, Iterable<? extends IPluginImport>> _function_2 = new Function1<IPluginModelBase, Iterable<? extends IPluginImport>>() {
        public Iterable<? extends IPluginImport> apply(final IPluginModelBase plugin) {
          BundleDescription _bundleDescription = plugin.getBundleDescription();
          BundleDescription[] _dependents = _bundleDescription.getDependents();
          final Function1<BundleDescription, IPluginImport> _function = new Function1<BundleDescription, IPluginImport>() {
            public IPluginImport apply(final BundleDescription it) {
              IPluginImport _xblockexpression = null;
              {
                String _symbolicName = it.getSymbolicName();
                Version _version = it.getVersion();
                String _string = _version.toString();
                final IPluginModelBase dependent = PluginUtil.findPlugin(_symbolicName, _string);
                IPluginBase _pluginBase = dependent.getPluginBase();
                IPluginImport[] _imports = _pluginBase.getImports();
                final Function1<IPluginImport, Boolean> _function = new Function1<IPluginImport, Boolean>() {
                  public Boolean apply(final IPluginImport it) {
                    String _id = it.getId();
                    IPluginBase _pluginBase = plugin.getPluginBase();
                    String _id_1 = _pluginBase.getId();
                    return Boolean.valueOf(Objects.equal(_id, _id_1));
                  }
                };
                _xblockexpression = IterableExtensions.<IPluginImport>findFirst(((Iterable<IPluginImport>)Conversions.doWrapArray(_imports)), _function);
              }
              return _xblockexpression;
            }
          };
          List<IPluginImport> _map = ListExtensions.<BundleDescription, IPluginImport>map(((List<BundleDescription>)Conversions.doWrapArray(_dependents)), _function);
          return IterableExtensions.<IPluginImport>filterNull(_map);
        }
      };
      MultiConnectionMappingCall<IPluginImport, IPluginModelBase> _inConnectionForEach = this.<IPluginImport>inConnectionForEach(PluginDiagramConfig.this.exportConnection, _function_2);
      final Function1<Side, Node> _function_3 = new Function1<Side, Node>() {
        public Node apply(final Side it) {
          return ButtonExtensions.getInverseArrowButton(it, "Add dependent");
        }
      };
      _inConnectionForEach.makeLazy(_function_3);
    }
  };
  
  private final ConnectionMapping<IPluginImport> importConnection = new ConnectionMapping<IPluginImport>(this, "importConnection") {
    public void calls() {
      final Function1<IPluginImport, IPluginModelBase> _function = new Function1<IPluginImport, IPluginModelBase>() {
        public IPluginModelBase apply(final IPluginImport it) {
          String _id = it.getId();
          String _version = it.getVersion();
          return PluginUtil.findPlugin(_id, _version);
        }
      };
      this.<IPluginModelBase>target(PluginDiagramConfig.this.pluginNode, _function);
    }
    
    public XConnection createConnection(final IMappedElementDescriptor<IPluginImport> descriptor) {
      return PluginDiagramConfig.this.createPluginImportConnection(descriptor);
    }
  };
  
  private final ConnectionMapping<IPluginImport> exportConnection = new ConnectionMapping<IPluginImport>(this, "exportConnection") {
    public void calls() {
      final Function1<IPluginImport, IPluginModelBase> _function = new Function1<IPluginImport, IPluginModelBase>() {
        public IPluginModelBase apply(final IPluginImport it) {
          return it.getPluginModel();
        }
      };
      this.<IPluginModelBase>source(PluginDiagramConfig.this.pluginNode, _function);
    }
    
    public XConnection createConnection(final IMappedElementDescriptor<IPluginImport> descriptor) {
      return PluginDiagramConfig.this.createPluginImportConnection(descriptor);
    }
  };
  
  protected BaseConnection<IPluginImport> createPluginImportConnection(final IMappedElementDescriptor<IPluginImport> descriptor) {
    BaseConnection<IPluginImport> _baseConnection = new BaseConnection<IPluginImport>(descriptor);
    final Procedure1<BaseConnection<IPluginImport>> _function = new Procedure1<BaseConnection<IPluginImport>>() {
      public void apply(final BaseConnection<IPluginImport> connection) {
        final XConnectionLabel label = new XConnectionLabel(connection);
        final Function1<IPluginImport, Object> _function = new Function1<IPluginImport, Object>() {
          public Object apply(final IPluginImport it) {
            Object _xblockexpression = null;
            {
              Text _text = label.getText();
              String _version = it.getVersion();
              _text.setText(_version);
              boolean _isOptional = it.isOptional();
              if (_isOptional) {
                ObservableList<Double> _strokeDashArray = connection.getStrokeDashArray();
                _strokeDashArray.setAll(Double.valueOf(5.0), Double.valueOf(5.0));
              }
              boolean _isReexported = it.isReexported();
              boolean _not = (!_isReexported);
              if (_not) {
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
    return ObjectExtensions.<BaseConnection<IPluginImport>>operator_doubleArrow(_baseConnection, _function);
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
