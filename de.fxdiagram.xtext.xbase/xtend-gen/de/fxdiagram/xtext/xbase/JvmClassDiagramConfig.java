package de.fxdiagram.xtext.xbase;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.eclipse.xtext.mapping.AbstractXtextDiagramConfig;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.DiagramMappingCall;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.MappingAcceptor;
import de.fxdiagram.mapping.MultiConnectionMappingCall;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.shapes.BaseConnection;
import de.fxdiagram.mapping.shapes.BaseDiagramNode;
import de.fxdiagram.xtext.xbase.JvmDomainObjectProvider;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
import de.fxdiagram.xtext.xbase.JvmTypeNode;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IType;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.example.domainmodel.domainmodel.AbstractElement;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class JvmClassDiagramConfig extends AbstractXtextDiagramConfig {
  @Inject
  @Extension
  private JvmDomainUtil _jvmDomainUtil;
  
  @Inject
  @Extension
  private IResourceServiceProvider.Registry _registry;
  
  private final NodeMapping<JvmDeclaredType> typeNode = new NodeMapping<JvmDeclaredType>(this, "typeNode", "Type") {
    @Override
    public XNode createNode(final IMappedElementDescriptor<JvmDeclaredType> descriptor) {
      return new JvmTypeNode(((JvmEObjectDescriptor<JvmDeclaredType>) descriptor));
    }
    
    @Override
    public void calls() {
      final Function1<JvmDeclaredType, Iterable<? extends JvmField>> _function = (JvmDeclaredType it) -> {
        return JvmClassDiagramConfig.this._jvmDomainUtil.getReferences(it);
      };
      MultiConnectionMappingCall<JvmField, JvmDeclaredType> _outConnectionForEach = this.<JvmField>outConnectionForEach(JvmClassDiagramConfig.this.referenceConnection, _function);
      final Function1<Side, Node> _function_1 = (Side it) -> {
        return ButtonExtensions.getArrowButton(it, "Add reference");
      };
      _outConnectionForEach.asButton(_function_1);
      final Function1<JvmDeclaredType, Iterable<? extends JvmTypeReference>> _function_2 = (JvmDeclaredType it) -> {
        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
        final Function1<JvmTypeReference, Boolean> _function_3 = (JvmTypeReference it_1) -> {
          JvmType _type = it_1.getType();
          return Boolean.valueOf((_type instanceof JvmDeclaredType));
        };
        return IterableExtensions.<JvmTypeReference>filter(_superTypes, _function_3);
      };
      MultiConnectionMappingCall<JvmTypeReference, JvmDeclaredType> _outConnectionForEach_1 = this.<JvmTypeReference>outConnectionForEach(JvmClassDiagramConfig.this.superTypeConnection, _function_2);
      final Function1<Side, Node> _function_3 = (Side it) -> {
        return ButtonExtensions.getTriangleButton(it, "Add supertype");
      };
      _outConnectionForEach_1.asButton(_function_3);
    }
  };
  
  private final ConnectionMapping<JvmField> referenceConnection = new ConnectionMapping<JvmField>(this, "referenceConnection", "Reference") {
    @Override
    public XConnection createConnection(final IMappedElementDescriptor<JvmField> descriptor) {
      BaseConnection<JvmField> _baseConnection = new BaseConnection<JvmField>(descriptor);
      final Procedure1<BaseConnection<JvmField>> _function = (BaseConnection<JvmField> it) -> {
        LineArrowHead _lineArrowHead = new LineArrowHead(it, false);
        it.setTargetArrowHead(_lineArrowHead);
        XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
        final Procedure1<XConnectionLabel> _function_1 = (XConnectionLabel label) -> {
          Text _text = label.getText();
          final Function1<JvmField, String> _function_2 = (JvmField it_1) -> {
            return it_1.getSimpleName();
          };
          String _withDomainObject = descriptor.<String>withDomainObject(_function_2);
          _text.setText(_withDomainObject);
        };
        ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function_1);
      };
      return ObjectExtensions.<BaseConnection<JvmField>>operator_doubleArrow(_baseConnection, _function);
    }
    
    @Override
    public void calls() {
      final Function1<JvmField, JvmDeclaredType> _function = (JvmField it) -> {
        JvmTypeReference _type = it.getType();
        JvmTypeReference _componentType = JvmClassDiagramConfig.this._jvmDomainUtil.getComponentType(_type);
        JvmType _type_1 = _componentType.getType();
        return JvmClassDiagramConfig.this._jvmDomainUtil.getOriginalJvmType(((JvmDeclaredType) _type_1));
      };
      this.<JvmDeclaredType>target(JvmClassDiagramConfig.this.typeNode, _function);
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
      final Function1<JvmTypeReference, JvmDeclaredType> _function = (JvmTypeReference it) -> {
        JvmType _type = it.getType();
        return JvmClassDiagramConfig.this._jvmDomainUtil.getOriginalJvmType(((JvmDeclaredType) _type));
      };
      this.<JvmDeclaredType>target(JvmClassDiagramConfig.this.typeNode, _function);
    }
  };
  
  private final DiagramMapping<PackageDeclaration> packageDiagram = new DiagramMapping<PackageDeclaration>(this, "packageDiagram", "Package diagram") {
    @Override
    public void calls() {
      final Function1<PackageDeclaration, Iterable<? extends JvmDeclaredType>> _function = (PackageDeclaration it) -> {
        EList<AbstractElement> _elements = it.getElements();
        Iterable<Entity> _filter = Iterables.<Entity>filter(_elements, Entity.class);
        final Function1<Entity, EObject> _function_1 = (Entity it_1) -> {
          return JvmClassDiagramConfig.this.getPrimaryJvmElement(it_1);
        };
        Iterable<EObject> _map = IterableExtensions.<Entity, EObject>map(_filter, _function_1);
        return Iterables.<JvmDeclaredType>filter(_map, JvmDeclaredType.class);
      };
      this.<JvmDeclaredType>nodeForEach(JvmClassDiagramConfig.this.typeNode, _function);
      final Function1<PackageDeclaration, Iterable<? extends PackageDeclaration>> _function_1 = (PackageDeclaration it) -> {
        EList<AbstractElement> _elements = it.getElements();
        return Iterables.<PackageDeclaration>filter(_elements, PackageDeclaration.class);
      };
      this.<PackageDeclaration>nodeForEach(JvmClassDiagramConfig.this.packageNode, _function_1);
      this.eagerly(JvmClassDiagramConfig.this.superTypeConnection, JvmClassDiagramConfig.this.referenceConnection);
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
      DiagramMappingCall<?, PackageDeclaration> _nestedDiagramFor = this.<PackageDeclaration>nestedDiagramFor(JvmClassDiagramConfig.this.packageDiagram, _function);
      _nestedDiagramFor.onOpen();
    }
  };
  
  protected EObject getPrimaryJvmElement(final EObject element) {
    Resource _eResource = element.eResource();
    URI _uRI = _eResource.getURI();
    IResourceServiceProvider _resourceServiceProvider = this._registry.getResourceServiceProvider(_uRI);
    IJvmModelAssociations _get = null;
    if (_resourceServiceProvider!=null) {
      _get=_resourceServiceProvider.<IJvmModelAssociations>get(IJvmModelAssociations.class);
    }
    EObject _primaryJvmElement = null;
    if (_get!=null) {
      _primaryJvmElement=_get.getPrimaryJvmElement(element);
    }
    return _primaryJvmElement;
  }
  
  @Override
  protected <ARG extends Object> void entryCalls(final ARG domainArgument, @Extension final MappingAcceptor<ARG> acceptor) {
    boolean _matched = false;
    if (!_matched) {
      if (domainArgument instanceof JvmDeclaredType) {
        _matched=true;
      }
      if (!_matched) {
        if (domainArgument instanceof IType) {
          _matched=true;
        }
      }
      if (_matched) {
        acceptor.add(this.typeNode);
      }
    }
    if (!_matched) {
      if (domainArgument instanceof PackageDeclaration) {
        _matched=true;
        acceptor.add(this.packageNode);
      }
    }
  }
  
  @Override
  protected IMappedElementDescriptorProvider createDomainObjectProvider() {
    return new JvmDomainObjectProvider();
  }
}
