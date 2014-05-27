package de.fxdiagram.xtext.domainmodel;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.xtext.domainmodel.DomainModelUtil;
import de.fxdiagram.xtext.domainmodel.EntityNode;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
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
  
  public DomainmodelDiagramConfig() {
    final DiagramMapping<PackageDeclaration> packageDiagram = new DiagramMapping<PackageDeclaration>(PackageDeclaration.class);
    NodeMapping<PackageDeclaration> _nodeMapping = new NodeMapping<PackageDeclaration>("packageNode", PackageDeclaration.class);
    final Procedure1<NodeMapping<PackageDeclaration>> _function = new Procedure1<NodeMapping<PackageDeclaration>>() {
      public void apply(final NodeMapping<PackageDeclaration> it) {
        final Function1<XtextDomainObjectDescriptor<PackageDeclaration>, BaseDiagramNode<PackageDeclaration>> _function = new Function1<XtextDomainObjectDescriptor<PackageDeclaration>, BaseDiagramNode<PackageDeclaration>>() {
          public BaseDiagramNode<PackageDeclaration> apply(final XtextDomainObjectDescriptor<PackageDeclaration> descriptor) {
            return new BaseDiagramNode<PackageDeclaration>(descriptor);
          }
        };
        it.setCreateNode(_function);
      }
    };
    final NodeMapping<PackageDeclaration> packageNode = ObjectExtensions.<NodeMapping<PackageDeclaration>>operator_doubleArrow(_nodeMapping, _function);
    NodeMapping<Entity> _nodeMapping_1 = new NodeMapping<Entity>(Entity.class);
    final Procedure1<NodeMapping<Entity>> _function_1 = new Procedure1<NodeMapping<Entity>>() {
      public void apply(final NodeMapping<Entity> it) {
        final Function1<XtextDomainObjectDescriptor<Entity>, EntityNode> _function = new Function1<XtextDomainObjectDescriptor<Entity>, EntityNode>() {
          public EntityNode apply(final XtextDomainObjectDescriptor<Entity> descriptor) {
            return new EntityNode(descriptor, DomainmodelDiagramConfig.this.domainModelUtil);
          }
        };
        it.setCreateNode(_function);
      }
    };
    final NodeMapping<Entity> entityNode = ObjectExtensions.<NodeMapping<Entity>>operator_doubleArrow(_nodeMapping_1, _function_1);
    ConnectionMapping<Property> _connectionMapping = new ConnectionMapping<Property>(Property.class);
    final Procedure1<ConnectionMapping<Property>> _function_2 = new Procedure1<ConnectionMapping<Property>>() {
      public void apply(final ConnectionMapping<Property> it) {
        final Function1<XtextDomainObjectDescriptor<Property>, XConnection> _function = new Function1<XtextDomainObjectDescriptor<Property>, XConnection>() {
          public XConnection apply(final XtextDomainObjectDescriptor<Property> descriptor) {
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
        };
        it.setCreateConnection(_function);
      }
    };
    final ConnectionMapping<Property> propertyConnection = ObjectExtensions.<ConnectionMapping<Property>>operator_doubleArrow(_connectionMapping, _function_2);
    final Procedure1<DiagramMapping<PackageDeclaration>> _function_3 = new Procedure1<DiagramMapping<PackageDeclaration>>() {
      public void apply(final DiagramMapping<PackageDeclaration> it) {
        final Function1<PackageDeclaration, Iterable<Entity>> _function = new Function1<PackageDeclaration, Iterable<Entity>>() {
          public Iterable<Entity> apply(final PackageDeclaration it) {
            EList<AbstractElement> _elements = it.getElements();
            return Iterables.<Entity>filter(_elements, Entity.class);
          }
        };
        it.<Entity>nodeForEach(entityNode, _function);
        final Function1<PackageDeclaration, Iterable<PackageDeclaration>> _function_1 = new Function1<PackageDeclaration, Iterable<PackageDeclaration>>() {
          public Iterable<PackageDeclaration> apply(final PackageDeclaration it) {
            EList<AbstractElement> _elements = it.getElements();
            return Iterables.<PackageDeclaration>filter(_elements, PackageDeclaration.class);
          }
        };
        it.<PackageDeclaration>nodeForEach(packageNode, _function_1);
      }
    };
    DiagramMapping<PackageDeclaration> _doubleArrow = ObjectExtensions.<DiagramMapping<PackageDeclaration>>operator_doubleArrow(packageDiagram, _function_3);
    this.addMapping(_doubleArrow);
    final Procedure1<NodeMapping<Entity>> _function_4 = new Procedure1<NodeMapping<Entity>>() {
      public void apply(final NodeMapping<Entity> it) {
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
        MultiConnectionMappingCall<Property, Entity> _outConnectionForEach = it.<Property>outConnectionForEach(propertyConnection, _function);
        _outConnectionForEach.makeLazy();
      }
    };
    NodeMapping<Entity> _doubleArrow_1 = ObjectExtensions.<NodeMapping<Entity>>operator_doubleArrow(entityNode, _function_4);
    this.addMapping(_doubleArrow_1);
    final Procedure1<NodeMapping<PackageDeclaration>> _function_5 = new Procedure1<NodeMapping<PackageDeclaration>>() {
      public void apply(final NodeMapping<PackageDeclaration> it) {
        final Function1<PackageDeclaration, PackageDeclaration> _function = new Function1<PackageDeclaration, PackageDeclaration>() {
          public PackageDeclaration apply(final PackageDeclaration it) {
            return it;
          }
        };
        it.<PackageDeclaration>nestedDiagramFor(packageDiagram, _function);
      }
    };
    NodeMapping<PackageDeclaration> _doubleArrow_2 = ObjectExtensions.<NodeMapping<PackageDeclaration>>operator_doubleArrow(packageNode, _function_5);
    this.addMapping(_doubleArrow_2);
    final Procedure1<ConnectionMapping<Property>> _function_6 = new Procedure1<ConnectionMapping<Property>>() {
      public void apply(final ConnectionMapping<Property> it) {
        final Function1<Property, Entity> _function = new Function1<Property, Entity>() {
          public Entity apply(final Property it) {
            JvmTypeReference _type = it.getType();
            return DomainmodelDiagramConfig.this.domainModelUtil.getReferencedEntity(_type);
          }
        };
        it.<Entity>target(entityNode, _function);
      }
    };
    ConnectionMapping<Property> _doubleArrow_3 = ObjectExtensions.<ConnectionMapping<Property>>operator_doubleArrow(propertyConnection, _function_6);
    this.addMapping(_doubleArrow_3);
  }
}
