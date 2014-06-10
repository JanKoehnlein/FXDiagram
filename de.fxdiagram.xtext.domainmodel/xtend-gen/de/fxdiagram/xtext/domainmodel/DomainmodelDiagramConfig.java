package de.fxdiagram.xtext.domainmodel;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.xtext.domainmodel.DomainModelUtil;
import de.fxdiagram.xtext.domainmodel.EntityNode;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
import de.fxdiagram.xtext.glue.mapping.MappingAcceptor;
import de.fxdiagram.xtext.glue.mapping.MultiConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.shapes.BaseDiagramNode;
import javafx.scene.text.Text;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.example.domainmodel.domainmodel.AbstractElement;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.domainmodel.Feature;
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration;
import org.eclipse.xtext.example.domainmodel.domainmodel.Property;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DomainmodelDiagramConfig extends AbstractDiagramConfig {
  @Inject
  @Extension
  private DomainModelUtil domainModelUtil;
  
  private final DiagramMapping<PackageDeclaration> packageDiagram = new DiagramMapping<PackageDeclaration>(this, "packageDiagram") {
    public void calls() {
      final Function1<PackageDeclaration, Iterable<Entity>> _function = new Function1<PackageDeclaration, Iterable<Entity>>() {
        public Iterable<Entity> apply(final PackageDeclaration it) {
          EList<AbstractElement> _elements = it.getElements();
          return Iterables.<Entity>filter(_elements, Entity.class);
        }
      };
      this.<Entity>nodeForEach(DomainmodelDiagramConfig.this.entityNode, _function);
      final Function1<PackageDeclaration, Iterable<PackageDeclaration>> _function_1 = new Function1<PackageDeclaration, Iterable<PackageDeclaration>>() {
        public Iterable<PackageDeclaration> apply(final PackageDeclaration it) {
          EList<AbstractElement> _elements = it.getElements();
          return Iterables.<PackageDeclaration>filter(_elements, PackageDeclaration.class);
        }
      };
      this.<PackageDeclaration>nodeForEach(DomainmodelDiagramConfig.this.packageNode, _function_1);
    }
  };
  
  private final NodeMapping<PackageDeclaration> packageNode = new NodeMapping<PackageDeclaration>(this, "packageNode") {
    public XNode createNode(final XtextDomainObjectDescriptor<PackageDeclaration> descriptor) {
      return new BaseDiagramNode<PackageDeclaration>(descriptor);
    }
    
    public void calls() {
      final Function1<PackageDeclaration, PackageDeclaration> _function = new Function1<PackageDeclaration, PackageDeclaration>() {
        public PackageDeclaration apply(final PackageDeclaration it) {
          return it;
        }
      };
      this.<PackageDeclaration>nestedDiagramFor(DomainmodelDiagramConfig.this.packageDiagram, _function);
    }
  };
  
  private final NodeMapping<Entity> entityNode = new NodeMapping<Entity>(this, "entityNode") {
    public XNode createNode(final XtextDomainObjectDescriptor<Entity> descriptor) {
      return new EntityNode(descriptor, DomainmodelDiagramConfig.this.domainModelUtil);
    }
    
    public void calls() {
      final Function1<Entity, Iterable<Property>> _function = new Function1<Entity, Iterable<Property>>() {
        public Iterable<Property> apply(final Entity it) {
          EList<Feature> _features = it.getFeatures();
          Iterable<Property> _filter = Iterables.<Property>filter(_features, Property.class);
          final Function1<Property, Boolean> _function = new Function1<Property, Boolean>() {
            public Boolean apply(final Property it) {
              JvmTypeReference _type = it.getType();
              Entity _referencedEntity = DomainmodelDiagramConfig.this.domainModelUtil.getReferencedEntity(_type);
              return Boolean.valueOf((!Objects.equal(_referencedEntity, null)));
            }
          };
          return IterableExtensions.<Property>filter(_filter, _function);
        }
      };
      MultiConnectionMappingCall<Property, Entity> _outConnectionForEach = this.<Property>outConnectionForEach(DomainmodelDiagramConfig.this.propertyConnection, _function);
      _outConnectionForEach.makeLazy();
    }
  };
  
  private final ConnectionMapping<Property> propertyConnection = new ConnectionMapping<Property>(this, "propertyConnection") {
    public XConnection createConnection(final XtextDomainObjectDescriptor<Property> descriptor) {
      XConnection _xConnection = new XConnection(descriptor);
      final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
        public void apply(final XConnection it) {
          LineArrowHead _lineArrowHead = new LineArrowHead(it, false);
          it.setTargetArrowHead(_lineArrowHead);
          XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
          final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
            public void apply(final XConnectionLabel label) {
              Text _text = label.getText();
              final Function1<Property, String> _function = new Function1<Property, String>() {
                public String apply(final Property it) {
                  return it.getName();
                }
              };
              String _withDomainObject = descriptor.<String>withDomainObject(_function);
              _text.setText(_withDomainObject);
            }
          };
          ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
        }
      };
      return ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
    }
    
    public void calls() {
      final Function1<Property, Entity> _function = new Function1<Property, Entity>() {
        public Entity apply(final Property it) {
          JvmTypeReference _type = it.getType();
          return DomainmodelDiagramConfig.this.domainModelUtil.getReferencedEntity(_type);
        }
      };
      this.<Entity>target(DomainmodelDiagramConfig.this.entityNode, _function);
    }
  };
  
  protected <ARG extends Object> void entryCalls(final ARG domainArgument, @Extension final MappingAcceptor<ARG> acceptor) {
    boolean _matched = false;
    if (!_matched) {
      if (domainArgument instanceof Entity) {
        _matched=true;
        acceptor.add(this.entityNode);
      }
    }
    if (!_matched) {
      if (domainArgument instanceof PackageDeclaration) {
        _matched=true;
        acceptor.add(this.packageNode);
        acceptor.add(this.packageDiagram);
      }
    }
    if (!_matched) {
      if (domainArgument instanceof Property) {
        _matched=true;
        acceptor.add(this.propertyConnection);
      }
    }
  }
}
