package de.fxdiagram.eclipse.ecore;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.DiamondArrowHead;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.eclipse.ecore.EcoreDomainObjectProvider;
import de.fxdiagram.eclipse.mapping.AbstractEclipseDiagramConfig;
import de.fxdiagram.eclipse.xtext.ESetting;
import de.fxdiagram.mapping.ConnectionLabelMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.DiagramMappingCall;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.MappingAcceptor;
import de.fxdiagram.mapping.MultiConnectionMappingCall;
import de.fxdiagram.mapping.NodeHeadingMapping;
import de.fxdiagram.mapping.NodeLabelMapping;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.shapes.BaseClassNode;
import de.fxdiagram.mapping.shapes.BaseConnection;
import de.fxdiagram.mapping.shapes.BaseDiagramNode;
import java.util.ArrayList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class EcoreDiagramConfig extends AbstractEclipseDiagramConfig {
  private final DiagramMapping<EPackage> ePackageDiagram = new DiagramMapping<EPackage>(this, "ePackageDiagram", "EPackage diagram") {
    @Override
    public void calls() {
      final Function1<EPackage, Iterable<? extends EClass>> _function = (EPackage it) -> {
        EList<EClassifier> _eClassifiers = it.getEClassifiers();
        return Iterables.<EClass>filter(_eClassifiers, EClass.class);
      };
      this.<EClass>nodeForEach(EcoreDiagramConfig.this.eClassNode, _function);
      final Function1<EPackage, Iterable<? extends EPackage>> _function_1 = (EPackage it) -> {
        return it.getESubpackages();
      };
      this.<EPackage>nodeForEach(EcoreDiagramConfig.this.ePackageNode, _function_1);
      this.eagerly(EcoreDiagramConfig.this.eSuperTypeConnection, EcoreDiagramConfig.this.eReferenceConnection);
    }
  };
  
  private final NodeMapping<EPackage> ePackageNode = new NodeMapping<EPackage>(this, "ePackageNode", "EPackage node") {
    @Override
    public XNode createNode(final IMappedElementDescriptor<EPackage> descriptor) {
      return new BaseDiagramNode<EPackage>(descriptor);
    }
    
    @Override
    public void calls() {
      final Function1<EPackage, EPackage> _function = (EPackage it) -> {
        return it;
      };
      this.<EPackage>labelFor(EcoreDiagramConfig.this.ePackageNodeName, _function);
      final Function1<EPackage, EPackage> _function_1 = (EPackage it) -> {
        return it;
      };
      DiagramMappingCall<?, EPackage> _nestedDiagramFor = this.<EPackage>nestedDiagramFor(EcoreDiagramConfig.this.ePackageDiagram, _function_1);
      _nestedDiagramFor.onOpen();
    }
  };
  
  private final NodeHeadingMapping<EPackage> ePackageNodeName = new NodeHeadingMapping<EPackage>(this, BaseDiagramNode.NODE_HEADING) {
    @Override
    public String getText(final EPackage element) {
      return element.getName();
    }
  };
  
  private final NodeMapping<EClass> eClassNode = new NodeMapping<EClass>(this, "eClassNode", "EClass") {
    @Override
    public XNode createNode(final IMappedElementDescriptor<EClass> descriptor) {
      return new BaseClassNode<EClass>(descriptor);
    }
    
    @Override
    public void calls() {
      final Function1<EClass, EClass> _function = (EClass it) -> {
        return it;
      };
      this.<EClass>labelFor(EcoreDiagramConfig.this.eClassName, _function);
      final Function1<EClass, Iterable<? extends EAttribute>> _function_1 = (EClass it) -> {
        return it.getEAttributes();
      };
      this.<EAttribute>labelForEach(EcoreDiagramConfig.this.eAttribute, _function_1);
      final Function1<EClass, Iterable<? extends EOperation>> _function_2 = (EClass it) -> {
        return it.getEOperations();
      };
      this.<EOperation>labelForEach(EcoreDiagramConfig.this.eOperation, _function_2);
      final Function1<EClass, Iterable<? extends EReference>> _function_3 = (EClass it) -> {
        return it.getEReferences();
      };
      MultiConnectionMappingCall<EReference, EClass> _outConnectionForEach = this.<EReference>outConnectionForEach(EcoreDiagramConfig.this.eReferenceConnection, _function_3);
      final Function1<Side, Node> _function_4 = (Side it) -> {
        return ButtonExtensions.getArrowButton(it, "Add EReference");
      };
      _outConnectionForEach.asButton(_function_4);
      final Function1<EClass, Iterable<? extends ESetting<EClass>>> _function_5 = (EClass subType) -> {
        ArrayList<ESetting<EClass>> _xblockexpression = null;
        {
          final ArrayList<ESetting<EClass>> superTypes = CollectionLiterals.<ESetting<EClass>>newArrayList();
          EList<EClass> _eSuperTypes = subType.getESuperTypes();
          final Procedure2<EClass, Integer> _function_6 = (EClass superType, Integer i) -> {
            boolean _notEquals = (!Objects.equal(subType, superType));
            if (_notEquals) {
              ESetting<EClass> _eSetting = new ESetting<EClass>(subType, EcorePackage.Literals.ECLASS__ESUPER_TYPES, (i).intValue());
              superTypes.add(_eSetting);
            }
          };
          IterableExtensions.<EClass>forEach(_eSuperTypes, _function_6);
          _xblockexpression = superTypes;
        }
        return _xblockexpression;
      };
      MultiConnectionMappingCall<ESetting<EClass>, EClass> _outConnectionForEach_1 = this.<ESetting<EClass>>outConnectionForEach(EcoreDiagramConfig.this.eSuperTypeConnection, _function_5);
      final Function1<Side, Node> _function_6 = (Side it) -> {
        return ButtonExtensions.getTriangleButton(it, "Add ESuperClass");
      };
      _outConnectionForEach_1.asButton(_function_6);
    }
  };
  
  private final NodeHeadingMapping<EClass> eClassName = new NodeHeadingMapping<EClass>(this, BaseClassNode.CLASS_NAME) {
    @Override
    public String getText(final EClass it) {
      return it.getName();
    }
  };
  
  private final NodeLabelMapping<EAttribute> eAttribute = new NodeLabelMapping<EAttribute>(this, BaseClassNode.ATTRIBUTE) {
    @Override
    public String getText(final EAttribute it) {
      StringConcatenation _builder = new StringConcatenation();
      String _name = it.getName();
      _builder.append(_name, "");
      _builder.append(": ");
      EClassifier _eType = it.getEType();
      String _name_1 = _eType.getName();
      _builder.append(_name_1, "");
      return _builder.toString();
    }
  };
  
  private final NodeLabelMapping<EOperation> eOperation = new NodeLabelMapping<EOperation>(this, BaseClassNode.OPERATION) {
    @Override
    public String getText(final EOperation it) {
      String _name = it.getName();
      String _plus = (_name + "() : ");
      String _elvis = null;
      EClassifier _eType = it.getEType();
      String _name_1 = null;
      if (_eType!=null) {
        _name_1=_eType.getName();
      }
      if (_name_1 != null) {
        _elvis = _name_1;
      } else {
        _elvis = "void";
      }
      return (_plus + _elvis);
    }
  };
  
  private final ConnectionMapping<EReference> eReferenceConnection = new ConnectionMapping<EReference>(this, "eReferenceConnection", "EReference") {
    @Override
    public XConnection createConnection(final IMappedElementDescriptor<EReference> descriptor) {
      BaseConnection<EReference> _baseConnection = new BaseConnection<EReference>(descriptor);
      final Procedure1<BaseConnection<EReference>> _function = (BaseConnection<EReference> conn) -> {
        final Function1<EReference, Object> _function_1 = (EReference it) -> {
          Object _xblockexpression = null;
          {
            boolean _isContainment = it.isContainment();
            if (_isContainment) {
              DiamondArrowHead _diamondArrowHead = new DiamondArrowHead(conn, false);
              conn.setTargetArrowHead(_diamondArrowHead);
            } else {
              boolean _isContainer = it.isContainer();
              if (_isContainer) {
                DiamondArrowHead _diamondArrowHead_1 = new DiamondArrowHead(conn, true);
                conn.setSourceArrowHead(_diamondArrowHead_1);
              } else {
                LineArrowHead _lineArrowHead = new LineArrowHead(conn, false);
                conn.setTargetArrowHead(_lineArrowHead);
              }
            }
            _xblockexpression = null;
          }
          return _xblockexpression;
        };
        descriptor.<Object>withDomainObject(_function_1);
      };
      return ObjectExtensions.<BaseConnection<EReference>>operator_doubleArrow(_baseConnection, _function);
    }
    
    @Override
    public void calls() {
      final Function1<EReference, EReference> _function = (EReference it) -> {
        return it;
      };
      this.<EReference>labelFor(EcoreDiagramConfig.this.eReferenceName, _function);
      final Function1<EReference, EClass> _function_1 = (EReference it) -> {
        EClassifier _eType = it.getEType();
        return ((EClass) _eType);
      };
      this.<EClass>target(EcoreDiagramConfig.this.eClassNode, _function_1);
    }
  };
  
  private final ConnectionLabelMapping<EReference> eReferenceName = new ConnectionLabelMapping<EReference>(this, "eReferenceName") {
    @Override
    public String getText(final EReference it) {
      return it.getName();
    }
  };
  
  private final ConnectionMapping<ESetting<EClass>> eSuperTypeConnection = new ConnectionMapping<ESetting<EClass>>(this, "eSuperTypeConnection", "ESuperType") {
    @Override
    public XConnection createConnection(final IMappedElementDescriptor<ESetting<EClass>> descriptor) {
      BaseConnection<ESetting<EClass>> _baseConnection = new BaseConnection<ESetting<EClass>>(descriptor);
      final Procedure1<BaseConnection<ESetting<EClass>>> _function = (BaseConnection<ESetting<EClass>> it) -> {
        TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, 10, 15, null, Color.WHITE, false);
        it.setTargetArrowHead(_triangleArrowHead);
      };
      return ObjectExtensions.<BaseConnection<ESetting<EClass>>>operator_doubleArrow(_baseConnection, _function);
    }
    
    @Override
    public void calls() {
      final Function1<ESetting<EClass>, EClass> _function = (ESetting<EClass> it) -> {
        EObject _target = it.getTarget();
        return ((EClass) _target);
      };
      this.<EClass>target(EcoreDiagramConfig.this.eClassNode, _function);
    }
  };
  
  @Override
  protected <ARG extends Object> void entryCalls(final ARG domainArgument, @Extension final MappingAcceptor<ARG> acceptor) {
    boolean _matched = false;
    if (!_matched) {
      if (domainArgument instanceof EClass) {
        _matched=true;
        acceptor.add(this.eClassNode);
      }
    }
    if (!_matched) {
      if (domainArgument instanceof EPackage) {
        _matched=true;
        acceptor.add(this.ePackageNode);
        acceptor.add(this.ePackageDiagram);
      }
    }
    if (!_matched) {
      if (domainArgument instanceof EReference) {
        _matched=true;
        acceptor.add(this.eReferenceConnection);
      }
    }
  }
  
  @Override
  protected IMappedElementDescriptorProvider createDomainObjectProvider() {
    return new EcoreDomainObjectProvider();
  }
}
