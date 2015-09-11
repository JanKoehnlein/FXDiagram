package de.fxdiagram.pde;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.mapping.AbstractMappedElementDescriptor;
import de.fxdiagram.pde.BundleDependency;
import de.fxdiagram.pde.BundleDescriptorProvider;
import de.fxdiagram.pde.BundleUtil;
import java.util.NoSuchElementException;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@ModelNode({ "kind", "ownerSymbolicName", "ownerVersion", "importSymbolicName", "importVersionRange" })
@SuppressWarnings("all")
public class BundleDependencyDescriptor extends AbstractMappedElementDescriptor<BundleDependency> {
  public BundleDependencyDescriptor(final BundleDependency.Kind kind, final String ownerSymbolicName, final String ownerVersion, final String importSymbolicName, final String importVersionRange, final String mappingConfigID, final String mappingID, final BundleDescriptorProvider provider) {
    super(mappingConfigID, mappingID, provider);
    this.kindProperty.set(kind);
    this.ownerSymbolicNameProperty.set(ownerSymbolicName);
    this.ownerVersionProperty.set(ownerVersion);
    this.importSymbolicNameProperty.set(importSymbolicName);
    this.importVersionRangeProperty.set(importVersionRange);
  }
  
  @Override
  public <U extends Object> U withDomainObject(final Function1<? super BundleDependency, ? extends U> lambda) {
    U _xblockexpression = null;
    {
      BundleDependency.Kind _kind = this.getKind();
      String _ownerSymbolicName = this.getOwnerSymbolicName();
      String _ownerVersion = this.getOwnerVersion();
      String _importSymbolicName = this.getImportSymbolicName();
      String _importVersionRange = this.getImportVersionRange();
      final BundleDependency dependency = BundleUtil.findBundleDependency(_kind, _ownerSymbolicName, _ownerVersion, _importSymbolicName, _importVersionRange);
      boolean _equals = Objects.equal(dependency, null);
      if (_equals) {
        String _ownerSymbolicName_1 = this.getOwnerSymbolicName();
        String _plus = ("Bundle dependency from " + _ownerSymbolicName_1);
        String _plus_1 = (_plus + " to ");
        String _importSymbolicName_1 = this.getImportSymbolicName();
        String _plus_2 = (_plus_1 + _importSymbolicName_1);
        String _plus_3 = (_plus_2 + " not found");
        throw new NoSuchElementException(_plus_3);
      }
      _xblockexpression = lambda.apply(dependency);
    }
    return _xblockexpression;
  }
  
  @Override
  public String getName() {
    String _ownerSymbolicName = this.getOwnerSymbolicName();
    String _plus = (_ownerSymbolicName + "->");
    String _importSymbolicName = this.getImportSymbolicName();
    return (_plus + _importSymbolicName);
  }
  
  @Override
  public Object openInEditor(final boolean select) {
    return null;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if ((obj instanceof BundleDependencyDescriptor)) {
      boolean _and = false;
      boolean _and_1 = false;
      boolean _and_2 = false;
      boolean _and_3 = false;
      boolean _and_4 = false;
      boolean _equals = super.equals(obj);
      if (!_equals) {
        _and_4 = false;
      } else {
        BundleDependency.Kind _kind = ((BundleDependencyDescriptor)obj).getKind();
        BundleDependency.Kind _kind_1 = this.getKind();
        boolean _equals_1 = Objects.equal(_kind, _kind_1);
        _and_4 = _equals_1;
      }
      if (!_and_4) {
        _and_3 = false;
      } else {
        String _ownerSymbolicName = ((BundleDependencyDescriptor)obj).getOwnerSymbolicName();
        String _ownerSymbolicName_1 = this.getOwnerSymbolicName();
        boolean _equals_2 = Objects.equal(_ownerSymbolicName, _ownerSymbolicName_1);
        _and_3 = _equals_2;
      }
      if (!_and_3) {
        _and_2 = false;
      } else {
        String _ownerVersion = ((BundleDependencyDescriptor)obj).getOwnerVersion();
        String _ownerVersion_1 = this.getOwnerVersion();
        boolean _equals_3 = Objects.equal(_ownerVersion, _ownerVersion_1);
        _and_2 = _equals_3;
      }
      if (!_and_2) {
        _and_1 = false;
      } else {
        String _importSymbolicName = ((BundleDependencyDescriptor)obj).getImportSymbolicName();
        String _importSymbolicName_1 = this.getImportSymbolicName();
        boolean _equals_4 = Objects.equal(_importSymbolicName, _importSymbolicName_1);
        _and_1 = _equals_4;
      }
      if (!_and_1) {
        _and = false;
      } else {
        String _importVersionRange = ((BundleDependencyDescriptor)obj).getImportVersionRange();
        String _importVersionRange_1 = this.getImportVersionRange();
        boolean _equals_5 = Objects.equal(_importVersionRange, _importVersionRange_1);
        _and = _equals_5;
      }
      return _and;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    int _hashCode = super.hashCode();
    BundleDependency.Kind _kind = this.getKind();
    int _hashCode_1 = _kind.hashCode();
    int _multiply = (71 * _hashCode_1);
    int _plus = (_hashCode + _multiply);
    String _ownerSymbolicName = this.getOwnerSymbolicName();
    int _hashCode_2 = _ownerSymbolicName.hashCode();
    int _multiply_1 = (73 * _hashCode_2);
    int _plus_1 = (_plus + _multiply_1);
    String _ownerVersion = this.getOwnerVersion();
    int _hashCode_3 = _ownerVersion.hashCode();
    int _multiply_2 = (79 * _hashCode_3);
    int _plus_2 = (_plus_1 + _multiply_2);
    String _importSymbolicName = this.getImportSymbolicName();
    int _hashCode_4 = _importSymbolicName.hashCode();
    int _multiply_3 = (83 * _hashCode_4);
    int _plus_3 = (_plus_2 + _multiply_3);
    String _importVersionRange = this.getImportVersionRange();
    int _hashCode_5 = _importVersionRange.hashCode();
    int _multiply_4 = (89 * _hashCode_5);
    return (_plus_3 + _multiply_4);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public BundleDependencyDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(kindProperty, BundleDependency.Kind.class);
    modelElement.addProperty(ownerSymbolicNameProperty, String.class);
    modelElement.addProperty(ownerVersionProperty, String.class);
    modelElement.addProperty(importSymbolicNameProperty, String.class);
    modelElement.addProperty(importVersionRangeProperty, String.class);
  }
  
  private ReadOnlyObjectWrapper<BundleDependency.Kind> kindProperty = new ReadOnlyObjectWrapper<BundleDependency.Kind>(this, "kind");
  
  public BundleDependency.Kind getKind() {
    return this.kindProperty.get();
  }
  
  public ReadOnlyObjectProperty<BundleDependency.Kind> kindProperty() {
    return this.kindProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper ownerSymbolicNameProperty = new ReadOnlyStringWrapper(this, "ownerSymbolicName");
  
  public String getOwnerSymbolicName() {
    return this.ownerSymbolicNameProperty.get();
  }
  
  public ReadOnlyStringProperty ownerSymbolicNameProperty() {
    return this.ownerSymbolicNameProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper ownerVersionProperty = new ReadOnlyStringWrapper(this, "ownerVersion");
  
  public String getOwnerVersion() {
    return this.ownerVersionProperty.get();
  }
  
  public ReadOnlyStringProperty ownerVersionProperty() {
    return this.ownerVersionProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper importSymbolicNameProperty = new ReadOnlyStringWrapper(this, "importSymbolicName");
  
  public String getImportSymbolicName() {
    return this.importSymbolicNameProperty.get();
  }
  
  public ReadOnlyStringProperty importSymbolicNameProperty() {
    return this.importSymbolicNameProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper importVersionRangeProperty = new ReadOnlyStringWrapper(this, "importVersionRange");
  
  public String getImportVersionRange() {
    return this.importVersionRangeProperty.get();
  }
  
  public ReadOnlyStringProperty importVersionRangeProperty() {
    return this.importVersionRangeProperty.getReadOnlyProperty();
  }
}
