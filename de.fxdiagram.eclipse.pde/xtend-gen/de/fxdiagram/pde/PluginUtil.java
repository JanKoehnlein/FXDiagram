package de.fxdiagram.pde;

import com.google.common.base.Objects;
import de.fxdiagram.pde.FragmentHost;
import de.fxdiagram.pde.PackageImport;
import de.fxdiagram.pde.PluginDependency;
import de.fxdiagram.pde.PluginDependencyPath;
import de.fxdiagram.pde.RequireBundle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.osgi.service.resolver.BaseDescription;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ImportPackageSpecification;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.pde.core.plugin.IFragment;
import org.eclipse.pde.core.plugin.IFragmentModel;
import org.eclipse.pde.core.plugin.IMatchRules;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.osgi.framework.Version;

@SuppressWarnings("all")
public class PluginUtil {
  public static Iterable<PluginDependency> getInverseDependencies(final IPluginModelBase plugin) {
    Iterable<PluginDependency> _xblockexpression = null;
    {
      final BundleDescription dependency = plugin.getBundleDescription();
      BundleDescription[] _dependents = dependency.getDependents();
      final Function1<BundleDescription, PluginDependency> _function = new Function1<BundleDescription, PluginDependency>() {
        public PluginDependency apply(final BundleDescription it) {
          PluginDependency _xblockexpression = null;
          {
            String _symbolicName = it.getSymbolicName();
            Version _version = it.getVersion();
            String _string = _version.toString();
            final IPluginModelBase importer = PluginUtil.findPlugin(_symbolicName, _string);
            String _symbolicName_1 = dependency.getSymbolicName();
            Version _version_1 = dependency.getVersion();
            _xblockexpression = PluginUtil.findPluginDependency(importer, _symbolicName_1, _version_1);
          }
          return _xblockexpression;
        }
      };
      List<PluginDependency> _map = ListExtensions.<BundleDescription, PluginDependency>map(((List<BundleDescription>)Conversions.doWrapArray(_dependents)), _function);
      _xblockexpression = IterableExtensions.<PluginDependency>filterNull(_map);
    }
    return _xblockexpression;
  }
  
  public static ArrayList<PluginDependencyPath> getAllInverseDependencies(final IPluginModelBase plugin) {
    ArrayList<PluginDependencyPath> _xblockexpression = null;
    {
      final ArrayList<PluginDependencyPath> result = CollectionLiterals.<PluginDependencyPath>newArrayList();
      PluginDependencyPath _pluginDependencyPath = new PluginDependencyPath();
      HashSet<IPluginModelBase> _newHashSet = CollectionLiterals.<IPluginModelBase>newHashSet();
      PluginUtil.addInverseDependencies(plugin, _pluginDependencyPath, result, _newHashSet);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  protected static void addInverseDependencies(final IPluginModelBase plugin, final PluginDependencyPath currentPath, final List<PluginDependencyPath> pathes, final Set<IPluginModelBase> processed) {
    Iterable<PluginDependency> _inverseDependencies = PluginUtil.getInverseDependencies(plugin);
    final Consumer<PluginDependency> _function = new Consumer<PluginDependency>() {
      public void accept(final PluginDependency it) {
        final PluginDependencyPath newPath = currentPath.append(it);
        pathes.add(newPath);
        IPluginModelBase _owner = it.getOwner();
        boolean _add = processed.add(_owner);
        if (_add) {
          IPluginModelBase _owner_1 = it.getOwner();
          PluginUtil.addInverseDependencies(_owner_1, newPath, pathes, processed);
        }
      }
    };
    _inverseDependencies.forEach(_function);
  }
  
  public static ArrayList<PluginDependency> getDependencies(final IPluginModelBase plugin) {
    ArrayList<PluginDependency> _xblockexpression = null;
    {
      final BundleDescription bundleDescription = plugin.getBundleDescription();
      final ArrayList<PluginDependency> result = CollectionLiterals.<PluginDependency>newArrayList();
      BundleSpecification[] _requiredBundles = bundleDescription.getRequiredBundles();
      final Consumer<BundleSpecification> _function = new Consumer<BundleSpecification>() {
        public void accept(final BundleSpecification it) {
          BaseDescription _supplier = it.getSupplier();
          boolean _notEquals = (!Objects.equal(_supplier, null));
          if (_notEquals) {
            BaseDescription _supplier_1 = it.getSupplier();
            String _name = _supplier_1.getName();
            BaseDescription _supplier_2 = it.getSupplier();
            Version _version = _supplier_2.getVersion();
            String _string = _version.toString();
            final IPluginModelBase required = PluginUtil.findPlugin(_name, _string);
            boolean _notEquals_1 = (!Objects.equal(required, null));
            if (_notEquals_1) {
              RequireBundle _requireBundle = new RequireBundle(plugin, it);
              result.add(_requireBundle);
            }
          }
        }
      };
      ((List<BundleSpecification>)Conversions.doWrapArray(_requiredBundles)).forEach(_function);
      ImportPackageSpecification[] _importPackages = bundleDescription.getImportPackages();
      final Consumer<ImportPackageSpecification> _function_1 = new Consumer<ImportPackageSpecification>() {
        public void accept(final ImportPackageSpecification it) {
          BaseDescription _supplier = it.getSupplier();
          boolean _notEquals = (!Objects.equal(_supplier, null));
          if (_notEquals) {
            BaseDescription _supplier_1 = it.getSupplier();
            String _name = _supplier_1.getName();
            BaseDescription _supplier_2 = it.getSupplier();
            Version _version = _supplier_2.getVersion();
            String _string = _version.toString();
            final IPluginModelBase required = PluginUtil.findPlugin(_name, _string);
            boolean _notEquals_1 = (!Objects.equal(required, null));
            if (_notEquals_1) {
              PackageImport _packageImport = new PackageImport(plugin, it);
              result.add(_packageImport);
            }
          }
        }
      };
      ((List<ImportPackageSpecification>)Conversions.doWrapArray(_importPackages)).forEach(_function_1);
      if ((bundleDescription instanceof IFragmentModel)) {
        final IFragment fragment = ((IFragmentModel)bundleDescription).getFragment();
        String _name = fragment.getName();
        String _version = fragment.getVersion();
        final IPluginModelBase fragmentHost = PluginUtil.findPlugin(_name, _version);
        boolean _notEquals = (!Objects.equal(fragmentHost, null));
        if (_notEquals) {
          FragmentHost _fragmentHost = new FragmentHost(plugin, fragment);
          result.add(_fragmentHost);
        }
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  public static ArrayList<PluginDependencyPath> getAllDependencies(final IPluginModelBase plugin) {
    ArrayList<PluginDependencyPath> _xblockexpression = null;
    {
      final ArrayList<PluginDependencyPath> result = CollectionLiterals.<PluginDependencyPath>newArrayList();
      PluginDependencyPath _pluginDependencyPath = new PluginDependencyPath();
      HashSet<IPluginModelBase> _newHashSet = CollectionLiterals.<IPluginModelBase>newHashSet();
      PluginUtil.addDependencies(plugin, _pluginDependencyPath, result, _newHashSet);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  protected static void addDependencies(final IPluginModelBase plugin, final PluginDependencyPath currentPath, final List<PluginDependencyPath> pathes, final Set<IPluginModelBase> processed) {
    ArrayList<PluginDependency> _dependencies = PluginUtil.getDependencies(plugin);
    final Consumer<PluginDependency> _function = new Consumer<PluginDependency>() {
      public void accept(final PluginDependency it) {
        final PluginDependencyPath newPath = currentPath.append(it);
        pathes.add(newPath);
        IPluginModelBase _dependency = it.getDependency();
        boolean _add = processed.add(_dependency);
        if (_add) {
          IPluginModelBase _dependency_1 = it.getDependency();
          PluginUtil.addDependencies(_dependency_1, newPath, pathes, processed);
        }
      }
    };
    _dependencies.forEach(_function);
  }
  
  public static IPluginModelBase[] allPlugins() {
    return PluginRegistry.getAllModels();
  }
  
  public static IPluginModelBase findPlugin(final String id, final String versionRange) {
    return PluginRegistry.findModel(id, versionRange, IMatchRules.COMPATIBLE, null);
  }
  
  public static IPluginModelBase getPlugin(final BundleDescription it) {
    String _symbolicName = it.getSymbolicName();
    Version _version = it.getVersion();
    String _string = _version.toString();
    return PluginRegistry.findModel(_symbolicName, _string, IMatchRules.PERFECT, null);
  }
  
  public static PluginDependency findPluginDependency(final String ownerID, final String ownerVersion, final String dependencyID, final String dependencyVersionRange) {
    final IPluginModelBase owner = PluginUtil.findPlugin(ownerID, ownerVersion);
    VersionRange _versionRange = new VersionRange(dependencyVersionRange);
    return PluginUtil.findPluginDependency(owner, dependencyID, _versionRange);
  }
  
  public static PluginDependency findPluginDependency(final IPluginModelBase owner, final String dependencyID, final VersionRange dependencyVersionRange) {
    BundleDescription _bundleDescription = owner.getBundleDescription();
    BundleSpecification[] _requiredBundles = _bundleDescription.getRequiredBundles();
    final Function1<BundleSpecification, Boolean> _function = new Function1<BundleSpecification, Boolean>() {
      public Boolean apply(final BundleSpecification it) {
        boolean _and = false;
        BaseDescription _supplier = it.getSupplier();
        String _name = null;
        if (_supplier!=null) {
          _name=_supplier.getName();
        }
        boolean _equals = Objects.equal(_name, dependencyID);
        if (!_equals) {
          _and = false;
        } else {
          VersionRange _versionRange = it.getVersionRange();
          boolean _equals_1 = Objects.equal(_versionRange, dependencyVersionRange);
          _and = _equals_1;
        }
        return Boolean.valueOf(_and);
      }
    };
    final BundleSpecification requireBundle = IterableExtensions.<BundleSpecification>findFirst(((Iterable<BundleSpecification>)Conversions.doWrapArray(_requiredBundles)), _function);
    boolean _notEquals = (!Objects.equal(requireBundle, null));
    if (_notEquals) {
      return new RequireBundle(owner, requireBundle);
    }
    BundleDescription _bundleDescription_1 = owner.getBundleDescription();
    ImportPackageSpecification[] _importPackages = _bundleDescription_1.getImportPackages();
    final Function1<ImportPackageSpecification, Boolean> _function_1 = new Function1<ImportPackageSpecification, Boolean>() {
      public Boolean apply(final ImportPackageSpecification it) {
        boolean _and = false;
        String _name = it.getName();
        boolean _equals = Objects.equal(_name, dependencyID);
        if (!_equals) {
          _and = false;
        } else {
          VersionRange _versionRange = it.getVersionRange();
          boolean _equals_1 = Objects.equal(_versionRange, dependencyVersionRange);
          _and = _equals_1;
        }
        return Boolean.valueOf(_and);
      }
    };
    final ImportPackageSpecification packageImport = IterableExtensions.<ImportPackageSpecification>findFirst(((Iterable<ImportPackageSpecification>)Conversions.doWrapArray(_importPackages)), _function_1);
    boolean _notEquals_1 = (!Objects.equal(packageImport, null));
    if (_notEquals_1) {
      return new PackageImport(owner, packageImport);
    }
    if ((owner instanceof IFragmentModel)) {
      final IFragment fragment = ((IFragmentModel)owner).getFragment();
      boolean _and = false;
      String _pluginId = null;
      if (fragment!=null) {
        _pluginId=fragment.getPluginId();
      }
      boolean _equals = Objects.equal(_pluginId, dependencyID);
      if (!_equals) {
        _and = false;
      } else {
        String _pluginVersion = fragment.getPluginVersion();
        Version _version = new Version(_pluginVersion);
        boolean _isIncluded = dependencyVersionRange.isIncluded(_version);
        _and = _isIncluded;
      }
      if (_and) {
        return new FragmentHost(owner, fragment);
      }
    }
    return null;
  }
  
  public static PluginDependency findPluginDependency(final IPluginModelBase owner, final String dependencyID, final Version dependencyVersion) {
    BundleDescription _bundleDescription = owner.getBundleDescription();
    BundleSpecification[] _requiredBundles = _bundleDescription.getRequiredBundles();
    final Function1<BundleSpecification, Boolean> _function = new Function1<BundleSpecification, Boolean>() {
      public Boolean apply(final BundleSpecification it) {
        boolean _and = false;
        BaseDescription _supplier = it.getSupplier();
        String _name = null;
        if (_supplier!=null) {
          _name=_supplier.getName();
        }
        boolean _equals = Objects.equal(_name, dependencyID);
        if (!_equals) {
          _and = false;
        } else {
          VersionRange _versionRange = it.getVersionRange();
          boolean _isIncluded = _versionRange.isIncluded(dependencyVersion);
          _and = _isIncluded;
        }
        return Boolean.valueOf(_and);
      }
    };
    final BundleSpecification requireBundle = IterableExtensions.<BundleSpecification>findFirst(((Iterable<BundleSpecification>)Conversions.doWrapArray(_requiredBundles)), _function);
    boolean _notEquals = (!Objects.equal(requireBundle, null));
    if (_notEquals) {
      return new RequireBundle(owner, requireBundle);
    }
    BundleDescription _bundleDescription_1 = owner.getBundleDescription();
    ImportPackageSpecification[] _importPackages = _bundleDescription_1.getImportPackages();
    final Function1<ImportPackageSpecification, Boolean> _function_1 = new Function1<ImportPackageSpecification, Boolean>() {
      public Boolean apply(final ImportPackageSpecification it) {
        boolean _and = false;
        String _name = it.getName();
        boolean _equals = Objects.equal(_name, dependencyID);
        if (!_equals) {
          _and = false;
        } else {
          VersionRange _versionRange = it.getVersionRange();
          boolean _isIncluded = _versionRange.isIncluded(dependencyVersion);
          _and = _isIncluded;
        }
        return Boolean.valueOf(_and);
      }
    };
    final ImportPackageSpecification packageImport = IterableExtensions.<ImportPackageSpecification>findFirst(((Iterable<ImportPackageSpecification>)Conversions.doWrapArray(_importPackages)), _function_1);
    boolean _notEquals_1 = (!Objects.equal(packageImport, null));
    if (_notEquals_1) {
      return new PackageImport(owner, packageImport);
    }
    if ((owner instanceof IFragmentModel)) {
      final IFragment fragment = ((IFragmentModel)owner).getFragment();
      boolean _and = false;
      String _pluginId = null;
      if (fragment!=null) {
        _pluginId=fragment.getPluginId();
      }
      boolean _equals = Objects.equal(_pluginId, dependencyID);
      if (!_equals) {
        _and = false;
      } else {
        String _pluginVersion = fragment.getPluginVersion();
        boolean _equals_1 = Objects.equal(dependencyVersion, _pluginVersion);
        _and = _equals_1;
      }
      if (_and) {
        return new FragmentHost(owner, fragment);
      }
    }
    return null;
  }
}
