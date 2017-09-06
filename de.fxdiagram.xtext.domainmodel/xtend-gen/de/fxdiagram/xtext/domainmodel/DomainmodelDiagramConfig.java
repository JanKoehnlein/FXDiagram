package de.fxdiagram.xtext.domainmodel;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.eclipse.xtext.mapping.AbstractXtextDiagramConfig;
import de.fxdiagram.mapping.ConnectionLabelMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.MappingAcceptor;
import de.fxdiagram.mapping.NodeHeadingMapping;
import de.fxdiagram.mapping.NodeLabelMapping;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.shapes.BaseConnection;
import de.fxdiagram.mapping.shapes.BaseDiagramNode;
import de.fxdiagram.xtext.domainmodel.DomainModelUtil;
import de.fxdiagram.xtext.domainmodel.EntityNode;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javax.inject.Inject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.domainmodel.Operation;
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration;
import org.eclipse.xtext.example.domainmodel.domainmodel.Property;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DomainmodelDiagramConfig extends AbstractXtextDiagramConfig {
  @Inject
  @Extension
  private DomainModelUtil domainModelUtil;
  
  private final DiagramMapping<PackageDeclaration> packageDiagram = new DiagramMapping<PackageDeclaration>(this, "packageDiagram", "Package diagram") {
    @Override
    public void calls() {
      final Function1<PackageDeclaration, Iterable<? extends Entity>> _function = (PackageDeclaration it) -> {
        return Iterables.<Entity>filter(it.getElements(), Entity.class);
      };
      this.<Entity>nodeForEach(DomainmodelDiagramConfig.this.entityNode, _function);
      final Function1<PackageDeclaration, Iterable<? extends PackageDeclaration>> _function_1 = (PackageDeclaration it) -> {
        return Iterables.<PackageDeclaration>filter(it.getElements(), PackageDeclaration.class);
      };
      this.<PackageDeclaration>nodeForEach(DomainmodelDiagramConfig.this.packageNode, _function_1);
      this.eagerly(DomainmodelDiagramConfig.this.superTypeConnection, DomainmodelDiagramConfig.this.propertyConnection);
    }
  };
  
  private final NodeMapping<PackageDeclaration> packageNode = new NodeMapping<PackageDeclaration>(this, "packageNode", "Package node") {
    @Override
    public XNode createNode(final IMappedElementDescriptor<PackageDeclaration> descriptor) {
      return new BaseDiagramNode<PackageDeclaration>(descriptor);
    }
    
    @Override
    public void calls() {
      final Function1<PackageDeclaration, PackageDeclaration> _function = (PackageDeclaration it) -> {
        return it;
      };
      this.<PackageDeclaration>labelFor(DomainmodelDiagramConfig.this.packageNodeName, _function);
      final Function1<PackageDeclaration, PackageDeclaration> _function_1 = (PackageDeclaration it) -> {
        return it;
      };
      this.<PackageDeclaration>nestedDiagramFor(DomainmodelDiagramConfig.this.packageDiagram, _function_1).onOpen();
    }
  };
  
  private final NodeHeadingMapping<PackageDeclaration> packageNodeName = new NodeHeadingMapping<PackageDeclaration>(this, BaseDiagramNode.NODE_HEADING) {
    @Override
    public String getText(final PackageDeclaration element) {
      return IterableExtensions.<String>last(((Iterable<String>)Conversions.doWrapArray(element.getName().split("\\."))));
    }
  };
  
  private final NodeMapping<Entity> entityNode = new NodeMapping<Entity>(this, "entityNode", "Entity") {
    @Override
    public XNode createNode(final IMappedElementDescriptor<Entity> descriptor) {
      return new EntityNode(descriptor);
    }
    
    @Override
    public void calls() {
      final Function1<Entity, Entity> _function = (Entity it) -> {
        return it;
      };
      this.<Entity>labelFor(DomainmodelDiagramConfig.this.entityName, _function);
      final Function1<Entity, Iterable<? extends Property>> _function_1 = (Entity it) -> {
        final Function1<Property, Boolean> _function_2 = (Property it_1) -> {
          Entity _referencedEntity = DomainmodelDiagramConfig.this.domainModelUtil.getReferencedEntity(it_1.getType());
          return Boolean.valueOf((_referencedEntity == null));
        };
        return IterableExtensions.<Property>filter(Iterables.<Property>filter(it.getFeatures(), Property.class), _function_2);
      };
      this.<Property>labelForEach(DomainmodelDiagramConfig.this.attribute, _function_1);
      final Function1<Entity, Iterable<? extends Operation>> _function_2 = (Entity it) -> {
        return Iterables.<Operation>filter(it.getFeatures(), Operation.class);
      };
      this.<Operation>labelForEach(DomainmodelDiagramConfig.this.operation, _function_2);
      final Function1<Entity, Iterable<? extends Property>> _function_3 = (Entity it) -> {
        final Function1<Property, Boolean> _function_4 = (Property it_1) -> {
          Entity _referencedEntity = DomainmodelDiagramConfig.this.domainModelUtil.getReferencedEntity(it_1.getType());
          return Boolean.valueOf((_referencedEntity != null));
        };
        return IterableExtensions.<Property>filter(Iterables.<Property>filter(it.getFeatures(), Property.class), _function_4);
      };
      final Function1<Side, Node> _function_4 = (Side it) -> {
        return ButtonExtensions.getArrowButton(it, "Add property");
      };
      this.<Property>outConnectionForEach(DomainmodelDiagramConfig.this.propertyConnection, _function_3).asButton(_function_4);
      final Function1<Entity, JvmTypeReference> _function_5 = (Entity it) -> {
        JvmParameterizedTypeReference _xifexpression = null;
        Entity _referencedEntity = DomainmodelDiagramConfig.this.domainModelUtil.getReferencedEntity(it.getSuperType());
        boolean _tripleNotEquals = (_referencedEntity != null);
        if (_tripleNotEquals) {
          _xifexpression = it.getSuperType();
        } else {
          _xifexpression = null;
        }
        return _xifexpression;
      };
      final Function1<Side, Node> _function_6 = (Side it) -> {
        return ButtonExtensions.getTriangleButton(it, "Add superclass");
      };
      this.<JvmTypeReference>outConnectionFor(DomainmodelDiagramConfig.this.superTypeConnection, _function_5).asButton(_function_6);
    }
  };
  
  private final NodeHeadingMapping<Entity> entityName = new NodeHeadingMapping<Entity>(this, EntityNode.ENTITY_NAME) {
    @Override
    public String getText(final Entity it) {
      return it.getName();
    }
  };
  
  private final NodeLabelMapping<Property> attribute = new NodeLabelMapping<Property>(this, EntityNode.ATTRIBUTE) {
    @Override
    public String getText(final Property it) {
      StringConcatenation _builder = new StringConcatenation();
      String _name = it.getName();
      _builder.append(_name);
      _builder.append(": ");
      String _simpleName = it.getType().getSimpleName();
      _builder.append(_simpleName);
      return _builder.toString();
    }
  };
  
  private final NodeLabelMapping<Operation> operation = new NodeLabelMapping<Operation>(this, EntityNode.OPERATION) {
    @Override
    public String getText(final Operation it) {
      String _name = it.getName();
      return (_name + "()");
    }
  };
  
  private final ConnectionMapping<Property> propertyConnection = new ConnectionMapping<Property>(this, "propertyConnection", "Property") {
    @Override
    public XConnection createConnection(final IMappedElementDescriptor<Property> descriptor) {
      BaseConnection<Property> _baseConnection = new BaseConnection<Property>(descriptor);
      final Procedure1<BaseConnection<Property>> _function = (BaseConnection<Property> it) -> {
        LineArrowHead _lineArrowHead = new LineArrowHead(it, false);
        it.setTargetArrowHead(_lineArrowHead);
      };
      return ObjectExtensions.<BaseConnection<Property>>operator_doubleArrow(_baseConnection, _function);
    }
    
    @Override
    public void calls() {
      final Function1<Property, Property> _function = (Property it) -> {
        return it;
      };
      this.<Property>labelFor(DomainmodelDiagramConfig.this.propertyName, _function);
      final Function1<Property, Entity> _function_1 = (Property it) -> {
        return DomainmodelDiagramConfig.this.domainModelUtil.getReferencedEntity(it.getType());
      };
      this.<Entity>target(DomainmodelDiagramConfig.this.entityNode, _function_1);
    }
  };
  
  private final ConnectionLabelMapping<Property> propertyName = new ConnectionLabelMapping<Property>(this, "propertyName") {
    @Override
    public String getText(final Property it) {
      return it.getName();
    }
  };
  
  private final ConnectionMapping<JvmTypeReference> superTypeConnection = new ConnectionMapping<JvmTypeReference>(this, "superTypeConnection", "Supertype") {
    @Override
    public XConnection createConnection(final IMappedElementDescriptor<JvmTypeReference> descriptor) {
      BaseConnection<JvmTypeReference> _baseConnection = new BaseConnection<JvmTypeReference>(descriptor);
      final Procedure1<BaseConnection<JvmTypeReference>> _function = (BaseConnection<JvmTypeReference> it) -> {
        TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, 10, 15, null, Color.WHITE, false);
        it.setTargetArrowHead(_triangleArrowHead);
      };
      return ObjectExtensions.<BaseConnection<JvmTypeReference>>operator_doubleArrow(_baseConnection, _function);
    }
    
    @Override
    public void calls() {
      final Function1<JvmTypeReference, Entity> _function = (JvmTypeReference it) -> {
        return DomainmodelDiagramConfig.this.domainModelUtil.getReferencedEntity(it);
      };
      this.<Entity>target(DomainmodelDiagramConfig.this.entityNode, _function);
    }
  };
  
  @Override
  protected <ARG extends Object> void entryCalls(final ARG domainArgument, @Extension final MappingAcceptor<ARG> acceptor) {
    boolean _matched = false;
    if (domainArgument instanceof Entity) {
      _matched=true;
      acceptor.add(this.entityNode);
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
