package de.fxdiagram.xtext.domainmodel;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
import de.fxdiagram.xtext.glue.mapping.MultiConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import javafx.scene.text.Text;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.example.domainmodel.domainmodel.AbstractElement;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.domainmodel.Feature;
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration;
import org.eclipse.xtext.example.domainmodel.domainmodel.Property;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DomainmodelDiagramConfig extends AbstractDiagramConfig {
  @Inject
  @Extension
  private IJvmModelAssociations _iJvmModelAssociations;
  
  public DomainmodelDiagramConfig() {
    final DiagramMapping<PackageDeclaration> packageDiagram = new DiagramMapping<PackageDeclaration>(PackageDeclaration.class);
    final NodeMapping<Entity> entityNode = new NodeMapping<Entity>(Entity.class);
    ConnectionMapping<Property> _connectionMapping = new ConnectionMapping<Property>(Property.class);
    final Procedure1<ConnectionMapping<Property>> _function = new Procedure1<ConnectionMapping<Property>>() {
      public void apply(final ConnectionMapping<Property> it) {
        final Function1<XtextDomainObjectDescriptor<Property>,XConnection> _function = new Function1<XtextDomainObjectDescriptor<Property>,XConnection>() {
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
                    final Function1<Property,String> _function = new Function1<Property,String>() {
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
    final ConnectionMapping<Property> propertyConnection = ObjectExtensions.<ConnectionMapping<Property>>operator_doubleArrow(_connectionMapping, _function);
    final Procedure1<DiagramMapping<PackageDeclaration>> _function_1 = new Procedure1<DiagramMapping<PackageDeclaration>>() {
      public void apply(final DiagramMapping<PackageDeclaration> it) {
        final Function1<PackageDeclaration,Iterable<Entity>> _function = new Function1<PackageDeclaration,Iterable<Entity>>() {
          public Iterable<Entity> apply(final PackageDeclaration it) {
            EList<AbstractElement> _elements = it.getElements();
            return Iterables.<Entity>filter(_elements, Entity.class);
          }
        };
        it.<Entity>nodeForEach(entityNode, _function);
      }
    };
    DiagramMapping<PackageDeclaration> _doubleArrow = ObjectExtensions.<DiagramMapping<PackageDeclaration>>operator_doubleArrow(packageDiagram, _function_1);
    this.addMapping(_doubleArrow);
    final Procedure1<NodeMapping<Entity>> _function_2 = new Procedure1<NodeMapping<Entity>>() {
      public void apply(final NodeMapping<Entity> it) {
        final Function1<Entity,Iterable<Property>> _function = new Function1<Entity,Iterable<Property>>() {
          public Iterable<Property> apply(final Entity it) {
            EList<Feature> _features = it.getFeatures();
            Iterable<Property> _filter = Iterables.<Property>filter(_features, Property.class);
            final Function1<Property,Boolean> _function = new Function1<Property,Boolean>() {
              public Boolean apply(final Property it) {
                Entity _referencedEntity = DomainmodelDiagramConfig.this.getReferencedEntity(it);
                return Boolean.valueOf((!Objects.equal(_referencedEntity, null)));
              }
            };
            return IterableExtensions.<Property>filter(_filter, _function);
          }
        };
        MultiConnectionMappingCall<Property,Entity> _outConnectionForEach = it.<Property>outConnectionForEach(propertyConnection, _function);
        _outConnectionForEach.makeLazy();
      }
    };
    NodeMapping<Entity> _doubleArrow_1 = ObjectExtensions.<NodeMapping<Entity>>operator_doubleArrow(entityNode, _function_2);
    this.addMapping(_doubleArrow_1);
    final Procedure1<ConnectionMapping<Property>> _function_3 = new Procedure1<ConnectionMapping<Property>>() {
      public void apply(final ConnectionMapping<Property> it) {
        final Function1<Property,Entity> _function = new Function1<Property,Entity>() {
          public Entity apply(final Property it) {
            return DomainmodelDiagramConfig.this.getReferencedEntity(it);
          }
        };
        it.<Entity>target(entityNode, _function);
      }
    };
    ConnectionMapping<Property> _doubleArrow_2 = ObjectExtensions.<ConnectionMapping<Property>>operator_doubleArrow(propertyConnection, _function_3);
    this.addMapping(_doubleArrow_2);
  }
  
  public Entity getReferencedEntity(final Property it) {
    Entity _xblockexpression = null;
    {
      JvmTypeReference _type = it.getType();
      JvmType _type_1 = _type.getType();
      final EObject sourceType = this._iJvmModelAssociations.getPrimarySourceElement(_type_1);
      Entity _xifexpression = null;
      if ((sourceType instanceof Entity)) {
        return ((Entity)sourceType);
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
