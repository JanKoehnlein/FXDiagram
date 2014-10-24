package de.fxdiagram.xtext.xbase;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
import de.fxdiagram.xtext.glue.mapping.ESetting;
import de.fxdiagram.xtext.glue.mapping.MappingAcceptor;
import de.fxdiagram.xtext.glue.mapping.MultiConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.mapping.XtextDomainObjectProvider;
import de.fxdiagram.xtext.glue.shapes.BaseDiagramNode;
import de.fxdiagram.xtext.xbase.JvmDomainObjectProvider;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
import de.fxdiagram.xtext.xbase.JvmTypeNode;
import java.util.ArrayList;
import javafx.geometry.Side;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
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
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.example.domainmodel.domainmodel.AbstractElement;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class JvmClassDiagramConfig extends AbstractDiagramConfig {
  @Inject
  @Extension
  private JvmDomainUtil _jvmDomainUtil;
  
  @Inject
  @Extension
  private IResourceServiceProvider.Registry _registry;
  
  private final NodeMapping<JvmDeclaredType> typeNode = new NodeMapping<JvmDeclaredType>(this, "typeNode") {
    public XNode createNode(final AbstractXtextDescriptor<JvmDeclaredType> descriptor) {
      return new JvmTypeNode(((JvmEObjectDescriptor<JvmDeclaredType>) descriptor));
    }
    
    public void calls() {
      final Function1<JvmDeclaredType, Iterable<JvmField>> _function = new Function1<JvmDeclaredType, Iterable<JvmField>>() {
        public Iterable<JvmField> apply(final JvmDeclaredType it) {
          return JvmClassDiagramConfig.this._jvmDomainUtil.getReferences(it);
        }
      };
      MultiConnectionMappingCall<JvmField, JvmDeclaredType> _outConnectionForEach = this.<JvmField>outConnectionForEach(JvmClassDiagramConfig.this.referenceConnection, _function);
      final Function1<Side, SVGPath> _function_1 = new Function1<Side, SVGPath>() {
        public SVGPath apply(final Side it) {
          return ButtonExtensions.getArrowButton(it, "Add reference");
        }
      };
      _outConnectionForEach.makeLazy(_function_1);
      final Function1<JvmDeclaredType, ArrayList<ESetting<JvmDeclaredType>>> _function_2 = new Function1<JvmDeclaredType, ArrayList<ESetting<JvmDeclaredType>>>() {
        public ArrayList<ESetting<JvmDeclaredType>> apply(final JvmDeclaredType it) {
          ArrayList<ESetting<JvmDeclaredType>> _xblockexpression = null;
          {
            final ArrayList<ESetting<JvmDeclaredType>> result = CollectionLiterals.<ESetting<JvmDeclaredType>>newArrayList();
            for (int i = 0; (i < it.getSuperTypes().size()); i++) {
              {
                EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                final JvmTypeReference superType = _superTypes.get(i);
                JvmType _type = superType.getType();
                if ((_type instanceof JvmDeclaredType)) {
                  ESetting<JvmDeclaredType> _eSetting = new ESetting<JvmDeclaredType>(it, TypesPackage.Literals.JVM_DECLARED_TYPE__SUPER_TYPES, i);
                  result.add(_eSetting);
                }
              }
            }
            _xblockexpression = result;
          }
          return _xblockexpression;
        }
      };
      MultiConnectionMappingCall<ESetting<JvmDeclaredType>, JvmDeclaredType> _outConnectionForEach_1 = this.<ESetting<JvmDeclaredType>>outConnectionForEach(JvmClassDiagramConfig.this.superTypeConnection, _function_2);
      final Function1<Side, SVGPath> _function_3 = new Function1<Side, SVGPath>() {
        public SVGPath apply(final Side it) {
          return ButtonExtensions.getTriangleButton(it, "Add supertype");
        }
      };
      _outConnectionForEach_1.makeLazy(_function_3);
    }
  };
  
  private final ConnectionMapping<JvmField> referenceConnection = new ConnectionMapping<JvmField>(this, "referenceConnection") {
    public XConnection createConnection(final AbstractXtextDescriptor<JvmField> descriptor) {
      XConnection _xConnection = new XConnection(descriptor);
      final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
        public void apply(final XConnection it) {
          LineArrowHead _lineArrowHead = new LineArrowHead(it, false);
          it.setTargetArrowHead(_lineArrowHead);
          XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
          final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
            public void apply(final XConnectionLabel label) {
              Text _text = label.getText();
              final Function1<JvmField, String> _function = new Function1<JvmField, String>() {
                public String apply(final JvmField it) {
                  return it.getSimpleName();
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
      final Function1<JvmField, JvmDeclaredType> _function = new Function1<JvmField, JvmDeclaredType>() {
        public JvmDeclaredType apply(final JvmField it) {
          JvmTypeReference _type = it.getType();
          JvmTypeReference _componentType = JvmClassDiagramConfig.this._jvmDomainUtil.getComponentType(_type);
          JvmType _type_1 = _componentType.getType();
          return JvmClassDiagramConfig.this._jvmDomainUtil.getOriginalJvmType(((JvmDeclaredType) _type_1));
        }
      };
      this.<JvmDeclaredType>target(JvmClassDiagramConfig.this.typeNode, _function);
    }
  };
  
  private final ConnectionMapping<ESetting<JvmDeclaredType>> superTypeConnection = new ConnectionMapping<ESetting<JvmDeclaredType>>(this, "superTypeConnection") {
    public XConnection createConnection(final AbstractXtextDescriptor<ESetting<JvmDeclaredType>> descriptor) {
      XConnection _xConnection = new XConnection(descriptor);
      final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
        public void apply(final XConnection it) {
          TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, 10, 15, null, Color.WHITE, false);
          it.setTargetArrowHead(_triangleArrowHead);
        }
      };
      return ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
    }
    
    public void calls() {
      final Function1<ESetting<JvmDeclaredType>, JvmDeclaredType> _function = new Function1<ESetting<JvmDeclaredType>, JvmDeclaredType>() {
        public JvmDeclaredType apply(final ESetting<JvmDeclaredType> it) {
          Object _target = it.getTarget();
          JvmType _type = ((JvmTypeReference) _target).getType();
          return JvmClassDiagramConfig.this._jvmDomainUtil.getOriginalJvmType(((JvmDeclaredType) _type));
        }
      };
      this.<JvmDeclaredType>target(JvmClassDiagramConfig.this.typeNode, _function);
    }
  };
  
  private final DiagramMapping<PackageDeclaration> packageDiagram = new DiagramMapping<PackageDeclaration>(this, "packageDiagram") {
    public void calls() {
      final Function1<PackageDeclaration, Iterable<JvmDeclaredType>> _function = new Function1<PackageDeclaration, Iterable<JvmDeclaredType>>() {
        public Iterable<JvmDeclaredType> apply(final PackageDeclaration it) {
          EList<AbstractElement> _elements = it.getElements();
          Iterable<Entity> _filter = Iterables.<Entity>filter(_elements, Entity.class);
          final Function1<Entity, EObject> _function = new Function1<Entity, EObject>() {
            public EObject apply(final Entity it) {
              return JvmClassDiagramConfig.this.getPrimaryJvmElement(it);
            }
          };
          Iterable<EObject> _map = IterableExtensions.<Entity, EObject>map(_filter, _function);
          return Iterables.<JvmDeclaredType>filter(_map, JvmDeclaredType.class);
        }
      };
      this.<JvmDeclaredType>nodeForEach(JvmClassDiagramConfig.this.typeNode, _function);
      final Function1<PackageDeclaration, Iterable<PackageDeclaration>> _function_1 = new Function1<PackageDeclaration, Iterable<PackageDeclaration>>() {
        public Iterable<PackageDeclaration> apply(final PackageDeclaration it) {
          EList<AbstractElement> _elements = it.getElements();
          return Iterables.<PackageDeclaration>filter(_elements, PackageDeclaration.class);
        }
      };
      this.<PackageDeclaration>nodeForEach(JvmClassDiagramConfig.this.packageNode, _function_1);
    }
  };
  
  private final NodeMapping<PackageDeclaration> packageNode = new NodeMapping<PackageDeclaration>(this, "packageNode") {
    public XNode createNode(final AbstractXtextDescriptor<PackageDeclaration> descriptor) {
      return new BaseDiagramNode<PackageDeclaration>(descriptor);
    }
    
    public void calls() {
      final Function1<PackageDeclaration, PackageDeclaration> _function = new Function1<PackageDeclaration, PackageDeclaration>() {
        public PackageDeclaration apply(final PackageDeclaration it) {
          return it;
        }
      };
      this.<PackageDeclaration>nestedDiagramFor(JvmClassDiagramConfig.this.packageDiagram, _function);
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
  
  protected XtextDomainObjectProvider createDomainObjectProvider() {
    return new JvmDomainObjectProvider();
  }
}
