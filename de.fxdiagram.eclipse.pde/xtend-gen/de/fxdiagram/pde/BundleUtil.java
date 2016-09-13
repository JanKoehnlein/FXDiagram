package de.fxdiagram.pde;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.pde.BundleDependency;
import de.fxdiagram.pde.BundleDependencyPath;
import de.fxdiagram.pde.FragmentHost;
import de.fxdiagram.pde.PackageImport;
import de.fxdiagram.pde.RequireBundle;
import de.fxdiagram.pde.UnqualifiedDependency;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.log4j.Logger;
import org.eclipse.osgi.service.resolver.BaseDescription;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.osgi.service.resolver.HostSpecification;
import org.eclipse.osgi.service.resolver.ImportPackageSpecification;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.pde.core.plugin.IMatchRules;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class BundleUtil {
  private final static Logger LOG = Logger.getLogger(BundleUtil.class);
  
  private static boolean isConsiderFragments;
  
  public static boolean isConsiderFragments() {
    return BundleUtil.isConsiderFragments;
  }
  
  public static boolean setConsiderFragments(final boolean considerFragments) {
    return BundleUtil.isConsiderFragments = considerFragments;
  }
  
  private static boolean isConsiderOptional;
  
  public static boolean isConsiderOptional() {
    return BundleUtil.isConsiderOptional;
  }
  
  public static boolean setConsiderOptional(final boolean considerOptional) {
    return BundleUtil.isConsiderOptional = considerOptional;
  }
  
  public static HashSet<BundleDescription> getAllDependencyBundles(final BundleDescription bundle) {
    HashSet<BundleDescription> _xblockexpression = null;
    {
      final HashSet<BundleDescription> result = CollectionLiterals.<BundleDescription>newHashSet();
      BundleUtil.addDependencies(bundle, result);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  protected static void addDependencies(final BundleDescription bundle, final Set<BundleDescription> dependencies) {
    Iterable<BundleDescription> _dependencyBundles = BundleUtil.getDependencyBundles(bundle);
    final Consumer<BundleDescription> _function = (BundleDescription it) -> {
      boolean _add = dependencies.add(it);
      if (_add) {
        BundleUtil.addDependencies(it, dependencies);
      }
    };
    _dependencyBundles.forEach(_function);
  }
  
  public static Iterable<BundleDescription> getDependencyBundles(final BundleDescription bundle) {
    Iterable<BundleDescription> _xblockexpression = null;
    {
      BundleDescription[] _resolvedRequires = bundle.getResolvedRequires();
      ExportPackageDescription[] _resolvedImports = bundle.getResolvedImports();
      final Function1<ExportPackageDescription, BundleDescription> _function = (ExportPackageDescription it) -> {
        return bundle;
      };
      List<BundleDescription> _map = ListExtensions.<ExportPackageDescription, BundleDescription>map(((List<ExportPackageDescription>)Conversions.doWrapArray(_resolvedImports)), _function);
      final Iterable<BundleDescription> dependencies = Iterables.<BundleDescription>concat(((Iterable<? extends BundleDescription>)Conversions.doWrapArray(_resolvedRequires)), _map);
      Iterable<BundleDescription> _xifexpression = null;
      boolean _isConsiderFragments = BundleUtil.isConsiderFragments();
      if (_isConsiderFragments) {
        BundleDescription[] _fragments = bundle.getFragments();
        _xifexpression = Iterables.<BundleDescription>concat(dependencies, ((Iterable<? extends BundleDescription>)Conversions.doWrapArray(_fragments)));
      } else {
        _xifexpression = dependencies;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public static HashSet<BundleDescription> getAllDependentBundles(final BundleDescription bundle) {
    HashSet<BundleDescription> _xblockexpression = null;
    {
      final HashSet<BundleDescription> result = CollectionLiterals.<BundleDescription>newHashSet();
      BundleUtil.addInverseDependencies(bundle, result);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  protected static void addInverseDependencies(final BundleDescription bundle, final Set<BundleDescription> inverseDependencies) {
    Iterable<BundleDescription> _dependentBundles = BundleUtil.getDependentBundles(bundle);
    final Consumer<BundleDescription> _function = (BundleDescription it) -> {
      boolean _add = inverseDependencies.add(it);
      if (_add) {
        BundleUtil.addInverseDependencies(it, inverseDependencies);
      }
    };
    _dependentBundles.forEach(_function);
  }
  
  public static Iterable<BundleDescription> getDependentBundles(final BundleDescription bundle) {
    BundleDescription[] _dependents = bundle.getDependents();
    final Function1<BundleDescription, Boolean> _function = (BundleDescription it) -> {
      HostSpecification _host = it.getHost();
      BaseDescription _supplier = null;
      if (_host!=null) {
        _supplier=_host.getSupplier();
      }
      BundleDescription _supplier_1 = null;
      if (_supplier!=null) {
        _supplier_1=_supplier.getSupplier();
      }
      return Boolean.valueOf((!Objects.equal(_supplier_1, bundle)));
    };
    return IterableExtensions.<BundleDescription>filter(((Iterable<BundleDescription>)Conversions.doWrapArray(_dependents)), _function);
  }
  
  public static ArrayList<BundleDependency> getAllBundleDependencies(final BundleDescription source, final BundleDescription target) {
    ArrayList<BundleDependency> _xblockexpression = null;
    {
      final LinkedList<BundleDependencyPath> paths = CollectionLiterals.<BundleDependencyPath>newLinkedList();
      BundleDependencyPath _bundleDependencyPath = new BundleDependencyPath();
      HashSet<BundleDependency> _newHashSet = CollectionLiterals.<BundleDependency>newHashSet();
      BundleUtil.addBundleDependencies(source, _bundleDependencyPath, paths, _newHashSet);
      final ArrayList<BundleDependency> result = CollectionLiterals.<BundleDependency>newArrayList();
      Set<BundleDescription> endNodes = Collections.<BundleDescription>unmodifiableSet(CollectionLiterals.<BundleDescription>newHashSet(target));
      final HashSet<BundleDescription> processedEndNodes = CollectionLiterals.<BundleDescription>newHashSet();
      do {
        {
          int _size = paths.size();
          String _plus = ("#paths: " + Integer.valueOf(_size));
          String _plus_1 = (_plus + "  #endnodes: ");
          int _size_1 = endNodes.size();
          String _plus_2 = (_plus_1 + Integer.valueOf(_size_1));
          BundleUtil.LOG.info(_plus_2);
          final HashSet<BundleDescription> newEndNodes = CollectionLiterals.<BundleDescription>newHashSet();
          for (Iterator<BundleDependencyPath> i = paths.iterator(); i.hasNext();) {
            {
              final BundleDependencyPath path = i.next();
              List<? extends BundleDependency> _elements = path.getElements();
              BundleDependency _last = IterableExtensions.last(_elements);
              BundleDescription _dependency = _last.getDependency();
              boolean _contains = endNodes.contains(_dependency);
              if (_contains) {
                List<? extends BundleDependency> _elements_1 = path.getElements();
                Iterables.<BundleDependency>addAll(result, _elements_1);
                i.remove();
                List<? extends BundleDependency> _elements_2 = path.getElements();
                final Function1<BundleDependency, BundleDescription> _function = (BundleDependency it) -> {
                  return it.getOwner();
                };
                List<BundleDescription> _map = ListExtensions.map(_elements_2, _function);
                final Function1<BundleDescription, Boolean> _function_1 = (BundleDescription it) -> {
                  boolean _contains_1 = processedEndNodes.contains(it);
                  return Boolean.valueOf((!_contains_1));
                };
                Iterable<BundleDescription> _filter = IterableExtensions.<BundleDescription>filter(_map, _function_1);
                Iterables.<BundleDescription>addAll(newEndNodes, _filter);
              }
            }
          }
          Iterables.<BundleDescription>addAll(processedEndNodes, endNodes);
          endNodes = newEndNodes;
        }
      } while((!endNodes.isEmpty()));
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  protected static void addBundleDependencies(final BundleDescription bundle, final BundleDependencyPath currentPath, final List<BundleDependencyPath> pathes, final Set<BundleDependency> processed) {
    Iterable<BundleDependency> _bundleDependencies = BundleUtil.getBundleDependencies(bundle);
    final Consumer<BundleDependency> _function = (BundleDependency it) -> {
      boolean _add = processed.add(it);
      if (_add) {
        final BundleDependencyPath newPath = currentPath.append(it);
        pathes.add(newPath);
        BundleDescription _dependency = it.getDependency();
        BundleUtil.addBundleDependencies(_dependency, newPath, pathes, processed);
      }
    };
    _bundleDependencies.forEach(_function);
  }
  
  public static Iterable<BundleDependency> getBundleDependencies(final BundleDescription bundle) {
    final ArrayList<BundleDependency> result = CollectionLiterals.<BundleDependency>newArrayList();
    BundleSpecification[] _requiredBundles = bundle.getRequiredBundles();
    final Consumer<BundleSpecification> _function = (BundleSpecification it) -> {
      RequireBundle _requireBundle = new RequireBundle(bundle, it);
      result.add(_requireBundle);
    };
    ((List<BundleSpecification>)Conversions.doWrapArray(_requiredBundles)).forEach(_function);
    ImportPackageSpecification[] _importPackages = bundle.getImportPackages();
    final Consumer<ImportPackageSpecification> _function_1 = (ImportPackageSpecification it) -> {
      PackageImport _packageImport = new PackageImport(bundle, it);
      result.add(_packageImport);
    };
    ((List<ImportPackageSpecification>)Conversions.doWrapArray(_importPackages)).forEach(_function_1);
    boolean _isConsiderFragments = BundleUtil.isConsiderFragments();
    if (_isConsiderFragments) {
      BundleDescription[] _fragments = bundle.getFragments();
      final Consumer<BundleDescription> _function_2 = (BundleDescription it) -> {
        FragmentHost _fragmentHost = new FragmentHost(bundle, it);
        result.add(_fragmentHost);
      };
      ((List<BundleDescription>)Conversions.doWrapArray(_fragments)).forEach(_function_2);
    }
    final Function1<BundleDependency, Boolean> _function_3 = (BundleDependency it) -> {
      return Boolean.valueOf(((!Objects.equal(it.getDependency(), null)) && (BundleUtil.isConsiderOptional() || (!it.isOptional()))));
    };
    return IterableExtensions.<BundleDependency>filter(result, _function_3);
  }
  
  public static Iterable<BundleDependency> getInverseBundleDependencies(final BundleDescription bundle) {
    final ArrayList<BundleDependency> result = CollectionLiterals.<BundleDependency>newArrayList();
    BundleDescription[] _dependents = bundle.getDependents();
    final Consumer<BundleDescription> _function = (BundleDescription owner) -> {
      BundleSpecification[] _requiredBundles = owner.getRequiredBundles();
      final Function1<BundleSpecification, Boolean> _function_1 = (BundleSpecification it) -> {
        BaseDescription _supplier = it.getSupplier();
        BundleDescription _supplier_1 = null;
        if (_supplier!=null) {
          _supplier_1=_supplier.getSupplier();
        }
        return Boolean.valueOf(Objects.equal(_supplier_1, bundle));
      };
      Iterable<BundleSpecification> _filter = IterableExtensions.<BundleSpecification>filter(((Iterable<BundleSpecification>)Conversions.doWrapArray(_requiredBundles)), _function_1);
      final Consumer<BundleSpecification> _function_2 = (BundleSpecification it) -> {
        RequireBundle _requireBundle = new RequireBundle(owner, it);
        result.add(_requireBundle);
      };
      _filter.forEach(_function_2);
      ImportPackageSpecification[] _importPackages = owner.getImportPackages();
      final Function1<ImportPackageSpecification, Boolean> _function_3 = (ImportPackageSpecification it) -> {
        BaseDescription _supplier = it.getSupplier();
        BundleDescription _supplier_1 = null;
        if (_supplier!=null) {
          _supplier_1=_supplier.getSupplier();
        }
        return Boolean.valueOf(Objects.equal(_supplier_1, bundle));
      };
      Iterable<ImportPackageSpecification> _filter_1 = IterableExtensions.<ImportPackageSpecification>filter(((Iterable<ImportPackageSpecification>)Conversions.doWrapArray(_importPackages)), _function_3);
      final Consumer<ImportPackageSpecification> _function_4 = (ImportPackageSpecification it) -> {
        PackageImport _packageImport = new PackageImport(owner, it);
        result.add(_packageImport);
      };
      _filter_1.forEach(_function_4);
      boolean _and = false;
      boolean _isConsiderFragments = BundleUtil.isConsiderFragments();
      if (!_isConsiderFragments) {
        _and = false;
      } else {
        HostSpecification _host = bundle.getHost();
        BaseDescription _supplier = null;
        if (_host!=null) {
          _supplier=_host.getSupplier();
        }
        BundleDescription _supplier_1 = null;
        if (_supplier!=null) {
          _supplier_1=_supplier.getSupplier();
        }
        boolean _equals = Objects.equal(_supplier_1, owner);
        _and = _equals;
      }
      if (_and) {
        FragmentHost _fragmentHost = new FragmentHost(owner, bundle);
        result.add(_fragmentHost);
      }
    };
    ((List<BundleDescription>)Conversions.doWrapArray(_dependents)).forEach(_function);
    final Function1<BundleDependency, Boolean> _function_1 = (BundleDependency it) -> {
      return Boolean.valueOf(((!Objects.equal(it.getDependency(), null)) && (BundleUtil.isConsiderOptional() || (!it.isOptional()))));
    };
    return IterableExtensions.<BundleDependency>filter(result, _function_1);
  }
  
  public static List<BundleDescription> allBundles() {
    IPluginModelBase[] _allModels = PluginRegistry.getAllModels();
    final Function1<IPluginModelBase, BundleDescription> _function = (IPluginModelBase it) -> {
      return it.getBundleDescription();
    };
    return ListExtensions.<IPluginModelBase, BundleDescription>map(((List<IPluginModelBase>)Conversions.doWrapArray(_allModels)), _function);
  }
  
  public static BundleDescription findBundle(final String id, final String version) {
    IPluginModelBase _findModel = PluginRegistry.findModel(id, version, IMatchRules.PERFECT, null);
    return _findModel.getBundleDescription();
  }
  
  public static BundleDescription findCompatibleBundle(final String id, final String versionRange) {
    IPluginModelBase _findModel = PluginRegistry.findModel(id, versionRange, IMatchRules.COMPATIBLE, null);
    return _findModel.getBundleDescription();
  }
  
  public static BundleDependency findBundleDependency(final BundleDependency.Kind kind, final String ownerID, final String ownerVersion, final String dependencyID, final String dependencyVersionRange) {
    final BundleDescription owner = BundleUtil.findBundle(ownerID, ownerVersion);
    VersionRange _versionRange = new VersionRange(dependencyVersionRange);
    return BundleUtil.findBundleDependency(kind, owner, dependencyID, _versionRange);
  }
  
  public static BundleDependency findBundleDependency(final BundleDependency.Kind kind, final BundleDescription owner, final String dependencyID, final VersionRange dependencyVersionRange) {
    if (kind != null) {
      switch (kind) {
        case REQUIRE_BUNDLE:
          BundleSpecification[] _requiredBundles = owner.getRequiredBundles();
          final Function1<BundleSpecification, Boolean> _function = (BundleSpecification it) -> {
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
          };
          final BundleSpecification requireBundle = IterableExtensions.<BundleSpecification>findFirst(((Iterable<BundleSpecification>)Conversions.doWrapArray(_requiredBundles)), _function);
          boolean _notEquals = (!Objects.equal(requireBundle, null));
          if (_notEquals) {
            return new RequireBundle(owner, requireBundle);
          }
          break;
        case PACKAGE_IMPORT:
          ImportPackageSpecification[] _importPackages = owner.getImportPackages();
          final Function1<ImportPackageSpecification, Boolean> _function_1 = (ImportPackageSpecification it) -> {
            return Boolean.valueOf((Objects.equal(((ExportPackageDescription) it.getSupplier()).getExporter().getName(), dependencyID) && Objects.equal(it.getVersionRange(), dependencyVersionRange)));
          };
          final ImportPackageSpecification packageImport = IterableExtensions.<ImportPackageSpecification>findFirst(((Iterable<ImportPackageSpecification>)Conversions.doWrapArray(_importPackages)), _function_1);
          boolean _notEquals_1 = (!Objects.equal(packageImport, null));
          if (_notEquals_1) {
            return new PackageImport(owner, packageImport);
          }
          break;
        case FRAGMENT_HOST:
          BundleDescription[] _fragments = owner.getFragments();
          final Function1<BundleDescription, Boolean> _function_2 = (BundleDescription it) -> {
            String _symbolicName = it.getSymbolicName();
            return Boolean.valueOf(Objects.equal(_symbolicName, dependencyID));
          };
          final BundleDescription fragment = IterableExtensions.<BundleDescription>findFirst(((Iterable<BundleDescription>)Conversions.doWrapArray(_fragments)), _function_2);
          boolean _notEquals_2 = (!Objects.equal(fragment, null));
          if (_notEquals_2) {
            return new FragmentHost(owner, fragment);
          }
          break;
        case UNQUALIFIED:
          String _string = dependencyVersionRange.toString();
          BundleDescription _findCompatibleBundle = BundleUtil.findCompatibleBundle(dependencyID, _string);
          new UnqualifiedDependency(owner, _findCompatibleBundle);
          break;
        default:
          break;
      }
    }
    return null;
  }
}
