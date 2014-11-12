package de.fxdiagram.pde;

import com.google.common.base.Objects;
import de.fxdiagram.pde.PluginDependencyPath;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.plugin.IMatchRules;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.osgi.framework.Version;

@SuppressWarnings("all")
public class PluginUtil {
  public static ArrayList<PluginDependencyPath> getAllDependentsPaths(final IPluginModelBase plugin) {
    ArrayList<PluginDependencyPath> _xblockexpression = null;
    {
      final ArrayList<PluginDependencyPath> result = CollectionLiterals.<PluginDependencyPath>newArrayList();
      PluginDependencyPath _pluginDependencyPath = new PluginDependencyPath();
      HashSet<IPluginModelBase> _newHashSet = CollectionLiterals.<IPluginModelBase>newHashSet();
      PluginUtil.addDependentsPaths(plugin, _pluginDependencyPath, result, _newHashSet);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  protected static void addDependentsPaths(final IPluginModelBase plugin, final PluginDependencyPath currentPath, final List<PluginDependencyPath> pathes, final Set<IPluginModelBase> processed) {
    final BundleDescription dependency = plugin.getBundleDescription();
    BundleDescription[] _dependents = dependency.getDependents();
    final Consumer<BundleDescription> _function = new Consumer<BundleDescription>() {
      public void accept(final BundleDescription it) {
        String _symbolicName = it.getSymbolicName();
        Version _version = it.getVersion();
        String _string = _version.toString();
        final IPluginModelBase importer = PluginUtil.findPlugin(_symbolicName, _string);
        boolean _notEquals = (!Objects.equal(importer, null));
        if (_notEquals) {
          String _symbolicName_1 = dependency.getSymbolicName();
          final IPluginImport pluginImport = PluginUtil.findPluginImport(importer, _symbolicName_1);
          boolean _notEquals_1 = (!Objects.equal(pluginImport, null));
          if (_notEquals_1) {
            PluginDependencyPath.PathElement _pathElement = new PluginDependencyPath.PathElement(pluginImport, importer);
            final PluginDependencyPath newPath = currentPath.append(_pathElement);
            pathes.add(newPath);
            boolean _add = processed.add(importer);
            if (_add) {
              PluginUtil.addDependentsPaths(importer, newPath, pathes, processed);
            }
          }
        }
      }
    };
    ((List<BundleDescription>)Conversions.doWrapArray(_dependents)).forEach(_function);
  }
  
  public static ArrayList<PluginDependencyPath> getAllImportPaths(final IPluginModelBase plugin) {
    ArrayList<PluginDependencyPath> _xblockexpression = null;
    {
      final ArrayList<PluginDependencyPath> result = CollectionLiterals.<PluginDependencyPath>newArrayList();
      PluginDependencyPath _pluginDependencyPath = new PluginDependencyPath();
      HashSet<IPluginModelBase> _newHashSet = CollectionLiterals.<IPluginModelBase>newHashSet();
      PluginUtil.addImportPaths(plugin, _pluginDependencyPath, result, _newHashSet);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  protected static void addImportPaths(final IPluginModelBase plugin, final PluginDependencyPath currentPath, final List<PluginDependencyPath> pathes, final Set<IPluginModelBase> processed) {
    IPluginBase _pluginBase = plugin.getPluginBase();
    IPluginImport[] _imports = _pluginBase.getImports();
    final Consumer<IPluginImport> _function = new Consumer<IPluginImport>() {
      public void accept(final IPluginImport it) {
        String _id = it.getId();
        String _version = it.getVersion();
        final IPluginModelBase imported = PluginUtil.findPlugin(_id, _version);
        boolean _notEquals = (!Objects.equal(imported, null));
        if (_notEquals) {
          PluginDependencyPath.PathElement _pathElement = new PluginDependencyPath.PathElement(it, imported);
          final PluginDependencyPath newPath = currentPath.append(_pathElement);
          pathes.add(newPath);
          boolean _add = processed.add(imported);
          if (_add) {
            PluginUtil.addImportPaths(imported, newPath, pathes, processed);
          }
        }
      }
    };
    ((List<IPluginImport>)Conversions.doWrapArray(_imports)).forEach(_function);
  }
  
  public static IPluginModelBase[] allPlugins() {
    return PluginRegistry.getAllModels();
  }
  
  public static IPluginModelBase findPlugin(final String id, final String version) {
    return PluginRegistry.findModel(id, version, IMatchRules.GREATER_OR_EQUAL, null);
  }
  
  public static IPluginImport findPluginImport(final String id, final String version, final String importName) {
    final IPluginModelBase owner = PluginUtil.findPlugin(id, version);
    return PluginUtil.findPluginImport(owner, importName);
  }
  
  public static IPluginImport findPluginImport(final IPluginModelBase owner, final String importName) {
    IPluginBase _pluginBase = owner.getPluginBase();
    IPluginImport[] _imports = _pluginBase.getImports();
    final Function1<IPluginImport, Boolean> _function = new Function1<IPluginImport, Boolean>() {
      public Boolean apply(final IPluginImport it) {
        String _id = it.getId();
        return Boolean.valueOf(Objects.equal(_id, importName));
      }
    };
    return IterableExtensions.<IPluginImport>findFirst(((Iterable<IPluginImport>)Conversions.doWrapArray(_imports)), _function);
  }
}
