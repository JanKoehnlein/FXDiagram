package de.fxdiagram.pde;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.mapping.AbstractMappedElementDescriptor;
import de.fxdiagram.pde.BundleDescriptorProvider;
import de.fxdiagram.pde.BundleUtil;
import java.util.NoSuchElementException;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.apache.log4j.Logger;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.plugin.IMatchRules;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.ui.editor.plugin.ManifestEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.osgi.framework.Version;

@ModelNode({ "symbolicName", "version" })
@SuppressWarnings("all")
public class BundleDescriptor extends AbstractMappedElementDescriptor<BundleDescription> {
  private final static Logger LOG = Logger.getLogger(BundleDescriptor.class);
  
  public BundleDescriptor(final String symbolicName, final String version, final String mappingConfigID, final String mappingID, final BundleDescriptorProvider provider) {
    super(mappingConfigID, mappingID, provider);
    this.symbolicNameProperty.set(symbolicName);
    this.versionProperty.set(version);
  }
  
  @Override
  public <U extends Object> U withDomainObject(final Function1<? super BundleDescription, ? extends U> lambda) {
    U _xblockexpression = null;
    {
      String _symbolicName = this.getSymbolicName();
      String _version = this.getVersion();
      final BundleDescription bundle = BundleUtil.findBundle(_symbolicName, _version);
      boolean _equals = Objects.equal(bundle, null);
      if (_equals) {
        String _symbolicName_1 = this.getSymbolicName();
        String _plus = ("Bundle " + _symbolicName_1);
        String _plus_1 = (_plus + " not found");
        throw new NoSuchElementException(_plus_1);
      }
      _xblockexpression = lambda.apply(bundle);
    }
    return _xblockexpression;
  }
  
  @Override
  public String getName() {
    return this.getSymbolicName();
  }
  
  public <U extends Object> U withPlugin(final Function1<? super IPluginModelBase, ? extends U> lambda) {
    U _xblockexpression = null;
    {
      String _symbolicName = this.getSymbolicName();
      String _version = this.getVersion();
      final IPluginModelBase plugin = PluginRegistry.findModel(_symbolicName, _version, IMatchRules.PERFECT, null);
      U _xifexpression = null;
      boolean _notEquals = (!Objects.equal(plugin, null));
      if (_notEquals) {
        _xifexpression = lambda.apply(plugin);
      } else {
        Object _xblockexpression_1 = null;
        {
          BundleDescriptor.LOG.warn(("Invalid BundleDescriptor " + this));
          _xblockexpression_1 = null;
        }
        _xifexpression = ((U)_xblockexpression_1);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Override
  public Object openInEditor(final boolean select) {
    final Function1<BundleDescription, IEditorPart> _function = (BundleDescription it) -> {
      IEditorPart _xblockexpression = null;
      {
        String _symbolicName = it.getSymbolicName();
        Version _version = it.getVersion();
        String _string = _version.toString();
        final IPluginModelBase plugin = PluginRegistry.findModel(_symbolicName, _string, IMatchRules.PERFECT, null);
        _xblockexpression = ManifestEditor.openPluginEditor(plugin);
      }
      return _xblockexpression;
    };
    return this.<IEditorPart>withDomainObject(_function);
  }
  
  @Override
  public boolean equals(final Object obj) {
    if ((obj instanceof BundleDescriptor)) {
      boolean _and = false;
      boolean _and_1 = false;
      boolean _equals = super.equals(obj);
      if (!_equals) {
        _and_1 = false;
      } else {
        String _symbolicName = ((BundleDescriptor)obj).getSymbolicName();
        String _symbolicName_1 = this.getSymbolicName();
        boolean _equals_1 = Objects.equal(_symbolicName, _symbolicName_1);
        _and_1 = _equals_1;
      }
      if (!_and_1) {
        _and = false;
      } else {
        String _version = ((BundleDescriptor)obj).getVersion();
        String _version_1 = this.getVersion();
        boolean _equals_2 = Objects.equal(_version, _version_1);
        _and = _equals_2;
      }
      return _and;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    int _hashCode = super.hashCode();
    String _symbolicName = this.getSymbolicName();
    int _hashCode_1 = _symbolicName.hashCode();
    int _multiply = (57 * _hashCode_1);
    int _plus = (_hashCode + _multiply);
    String _version = this.getVersion();
    int _hashCode_2 = _version.hashCode();
    int _multiply_1 = (67 * _hashCode_2);
    return (_plus + _multiply_1);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public BundleDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(symbolicNameProperty, String.class);
    modelElement.addProperty(versionProperty, String.class);
  }
  
  private ReadOnlyStringWrapper symbolicNameProperty = new ReadOnlyStringWrapper(this, "symbolicName");
  
  public String getSymbolicName() {
    return this.symbolicNameProperty.get();
  }
  
  public ReadOnlyStringProperty symbolicNameProperty() {
    return this.symbolicNameProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper versionProperty = new ReadOnlyStringWrapper(this, "version");
  
  public String getVersion() {
    return this.versionProperty.get();
  }
  
  public ReadOnlyStringProperty versionProperty() {
    return this.versionProperty.getReadOnlyProperty();
  }
}
