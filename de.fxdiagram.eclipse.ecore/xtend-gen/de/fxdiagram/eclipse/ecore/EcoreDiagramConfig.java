package de.fxdiagram.eclipse.ecore;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.DiamondArrowHead;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.eclipse.ecore.EReferenceWithOpposite;
import de.fxdiagram.eclipse.ecore.ESuperType;
import de.fxdiagram.eclipse.ecore.EcoreDomainObjectProvider;
import de.fxdiagram.eclipse.mapping.AbstractEclipseDiagramConfig;
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
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
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
  private final static double EREFERENCE_LABEL_POS = 0.2;
  
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
      this.eagerly(EcoreDiagramConfig.this.eSuperTypeConnection, EcoreDiagramConfig.this.eReferenceConnection, EcoreDiagramConfig.this.eContainmentReferenceConnection);
    }
  };
  
  private final DiagramMapping<EPackage> ePackageInheritanceDiagram = new DiagramMapping<EPackage>(this, "ePackageInheritanceDiagram", "EPackage inheritance diagram") {
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
      this.eagerly(EcoreDiagramConfig.this.eSuperTypeConnection);
    }
  };
  
  private final DiagramMapping<EPackage> ePackageContainmentDiagram = new DiagramMapping<EPackage>(this, "ePackageContainmentDiagram", "EPackage containment diagram") {
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
      this.eagerly(EcoreDiagramConfig.this.eContainmentReferenceConnection);
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
      final Function1<EClass, Iterable<? extends EReferenceWithOpposite>> _function_3 = (EClass it) -> {
        EList<EReference> _eReferences = it.getEReferences();
        final Function1<EReference, Boolean> _function_4 = (EReference it_1) -> {
          return Boolean.valueOf(((!it_1.isContainment()) && (!it_1.isContainer())));
        };
        Iterable<EReference> _filter = IterableExtensions.<EReference>filter(_eReferences, _function_4);
        final Function1<EReference, EReferenceWithOpposite> _function_5 = (EReference it_1) -> {
          return new EReferenceWithOpposite(it_1);
        };
        return IterableExtensions.<EReference, EReferenceWithOpposite>map(_filter, _function_5);
      };
      MultiConnectionMappingCall<EReferenceWithOpposite, EClass> _outConnectionForEach = this.<EReferenceWithOpposite>outConnectionForEach(EcoreDiagramConfig.this.eReferenceConnection, _function_3);
      final Function1<Side, Node> _function_4 = (Side it) -> {
        return ButtonExtensions.getArrowButton(it, "Add cross EReference");
      };
      _outConnectionForEach.asButton(_function_4);
      final Function1<EClass, Iterable<? extends EReferenceWithOpposite>> _function_5 = (EClass it) -> {
        EList<EReference> _eReferences = it.getEReferences();
        final Function1<EReference, Boolean> _function_6 = (EReference it_1) -> {
          return Boolean.valueOf((it_1.isContainment() || it_1.isContainer()));
        };
        Iterable<EReference> _filter = IterableExtensions.<EReference>filter(_eReferences, _function_6);
        final Function1<EReference, EReferenceWithOpposite> _function_7 = (EReference it_1) -> {
          return new EReferenceWithOpposite(it_1);
        };
        return IterableExtensions.<EReference, EReferenceWithOpposite>map(_filter, _function_7);
      };
      MultiConnectionMappingCall<EReferenceWithOpposite, EClass> _outConnectionForEach_1 = this.<EReferenceWithOpposite>outConnectionForEach(EcoreDiagramConfig.this.eContainmentReferenceConnection, _function_5);
      final Function1<Side, Node> _function_6 = (Side it) -> {
        return ButtonExtensions.getArrowButton(it, "Add containment EReference");
      };
      _outConnectionForEach_1.asButton(_function_6);
      final Function1<EClass, Iterable<? extends ESuperType>> _function_7 = (EClass subType) -> {
        ArrayList<ESuperType> _xblockexpression = null;
        {
          final ArrayList<ESuperType> superTypes = CollectionLiterals.<ESuperType>newArrayList();
          EList<EClass> _eSuperTypes = subType.getESuperTypes();
          final Procedure2<EClass, Integer> _function_8 = (EClass superType, Integer i) -> {
            boolean _notEquals = (!Objects.equal(subType, superType));
            if (_notEquals) {
              ESuperType _eSuperType = new ESuperType(subType, superType);
              superTypes.add(_eSuperType);
            }
          };
          IterableExtensions.<EClass>forEach(_eSuperTypes, _function_8);
          _xblockexpression = superTypes;
        }
        return _xblockexpression;
      };
      MultiConnectionMappingCall<ESuperType, EClass> _outConnectionForEach_2 = this.<ESuperType>outConnectionForEach(EcoreDiagramConfig.this.eSuperTypeConnection, _function_7);
      final Function1<Side, Node> _function_8 = (Side it) -> {
        return ButtonExtensions.getTriangleButton(it, "Add ESuperClass");
      };
      _outConnectionForEach_2.asButton(_function_8);
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
      String _name_1 = null;
      if (_eType!=null) {
        _name_1=_eType.getName();
      }
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
  
  private final ConnectionMapping<EReferenceWithOpposite> eReferenceConnection = new ConnectionMapping<EReferenceWithOpposite>(this, "crossReferenceConnection", "Cross EReference") {
    @Override
    public XConnection createConnection(final IMappedElementDescriptor<EReferenceWithOpposite> descriptor) {
      BaseConnection<EReferenceWithOpposite> _baseConnection = new BaseConnection<EReferenceWithOpposite>(descriptor);
      final Procedure1<BaseConnection<EReferenceWithOpposite>> _function = (BaseConnection<EReferenceWithOpposite> conn) -> {
        final Function1<EReferenceWithOpposite, Object> _function_1 = (EReferenceWithOpposite it) -> {
          Object _xblockexpression = null;
          {
            LineArrowHead _lineArrowHead = new LineArrowHead(conn, false);
            conn.setTargetArrowHead(_lineArrowHead);
            _xblockexpression = null;
          }
          return _xblockexpression;
        };
        descriptor.<Object>withDomainObject(_function_1);
      };
      return ObjectExtensions.<BaseConnection<EReferenceWithOpposite>>operator_doubleArrow(_baseConnection, _function);
    }
    
    @Override
    public void calls() {
      final Function1<EReferenceWithOpposite, EReference> _function = (EReferenceWithOpposite it) -> {
        return it.getTo();
      };
      this.<EReference>labelFor(EcoreDiagramConfig.this.eReferenceToName, _function);
      final Function1<EReferenceWithOpposite, EReference> _function_1 = (EReferenceWithOpposite it) -> {
        return it.getFro();
      };
      this.<EReference>labelFor(EcoreDiagramConfig.this.eReferenceFroName, _function_1);
      final Function1<EReferenceWithOpposite, EClass> _function_2 = (EReferenceWithOpposite it) -> {
        EReference _to = it.getTo();
        EClassifier _eType = _to.getEType();
        return ((EClass) _eType);
      };
      this.<EClass>target(EcoreDiagramConfig.this.eClassNode, _function_2);
    }
  };
  
  private final ConnectionMapping<EReferenceWithOpposite> eContainmentReferenceConnection = new ConnectionMapping<EReferenceWithOpposite>(this, "containmentReferenceConnection", "Containment EReference") {
    @Override
    public XConnection createConnection(final IMappedElementDescriptor<EReferenceWithOpposite> descriptor) {
      BaseConnection<EReferenceWithOpposite> _baseConnection = new BaseConnection<EReferenceWithOpposite>(descriptor);
      final Procedure1<BaseConnection<EReferenceWithOpposite>> _function = (BaseConnection<EReferenceWithOpposite> conn) -> {
        final Function1<EReferenceWithOpposite, Object> _function_1 = (EReferenceWithOpposite it) -> {
          Object _xblockexpression = null;
          {
            EReference _to = it.getTo();
            boolean _isContainment = _to.isContainment();
            if (_isContainment) {
              DiamondArrowHead _diamondArrowHead = new DiamondArrowHead(conn, true);
              conn.setSourceArrowHead(_diamondArrowHead);
              conn.setTargetArrowHead(null);
            } else {
              EReference _to_1 = it.getTo();
              boolean _isContainer = _to_1.isContainer();
              if (_isContainer) {
                DiamondArrowHead _diamondArrowHead_1 = new DiamondArrowHead(conn, false);
                conn.setTargetArrowHead(_diamondArrowHead_1);
                conn.setSourceArrowHead(null);
              }
            }
            _xblockexpression = null;
          }
          return _xblockexpression;
        };
        descriptor.<Object>withDomainObject(_function_1);
      };
      return ObjectExtensions.<BaseConnection<EReferenceWithOpposite>>operator_doubleArrow(_baseConnection, _function);
    }
    
    @Override
    public void calls() {
      final Function1<EReferenceWithOpposite, EReference> _function = (EReferenceWithOpposite it) -> {
        return it.getTo();
      };
      this.<EReference>labelFor(EcoreDiagramConfig.this.eReferenceToName, _function);
      final Function1<EReferenceWithOpposite, EReference> _function_1 = (EReferenceWithOpposite it) -> {
        return it.getFro();
      };
      this.<EReference>labelFor(EcoreDiagramConfig.this.eReferenceFroName, _function_1);
      final Function1<EReferenceWithOpposite, EClass> _function_2 = (EReferenceWithOpposite it) -> {
        EReference _to = it.getTo();
        EClassifier _eType = _to.getEType();
        return ((EClass) _eType);
      };
      this.<EClass>target(EcoreDiagramConfig.this.eClassNode, _function_2);
    }
  };
  
  private final ConnectionLabelMapping<EReference> eReferenceToName = new ConnectionLabelMapping<EReference>(this, "eReferenceToName") {
    @Override
    public XConnectionLabel createLabel(final IMappedElementDescriptor<EReference> descriptor, final EReference labelElement) {
      XConnectionLabel _createLabel = super.createLabel(descriptor, labelElement);
      final Procedure1<XConnectionLabel> _function = (XConnectionLabel it) -> {
        EReference _eOpposite = labelElement.getEOpposite();
        boolean _equals = Objects.equal(_eOpposite, null);
        if (_equals) {
          it.setPosition(0.5);
        } else {
          it.setPosition((1 - EcoreDiagramConfig.EREFERENCE_LABEL_POS));
        }
      };
      return ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_createLabel, _function);
    }
    
    @Override
    public String getText(final EReference it) {
      return it.getName();
    }
  };
  
  private final ConnectionLabelMapping<EReference> eReferenceFroName = new ConnectionLabelMapping<EReference>(this, "eReferenceFroName") {
    @Override
    public XConnectionLabel createLabel(final IMappedElementDescriptor<EReference> descriptor, final EReference labelElement) {
      XConnectionLabel _createLabel = super.createLabel(descriptor, labelElement);
      final Procedure1<XConnectionLabel> _function = (XConnectionLabel it) -> {
        EReference _eOpposite = labelElement.getEOpposite();
        boolean _equals = Objects.equal(_eOpposite, null);
        if (_equals) {
          it.setPosition(0.5);
        } else {
          it.setPosition(EcoreDiagramConfig.EREFERENCE_LABEL_POS);
        }
      };
      return ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_createLabel, _function);
    }
    
    @Override
    public String getText(final EReference it) {
      return it.getName();
    }
  };
  
  private final ConnectionMapping<ESuperType> eSuperTypeConnection = new ConnectionMapping<ESuperType>(this, "eSuperTypeConnection", "ESuperType") {
    @Override
    public XConnection createConnection(final IMappedElementDescriptor<ESuperType> descriptor) {
      BaseConnection<ESuperType> _baseConnection = new BaseConnection<ESuperType>(descriptor);
      final Procedure1<BaseConnection<ESuperType>> _function = (BaseConnection<ESuperType> it) -> {
        TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, 10, 15, null, Color.WHITE, false);
        it.setTargetArrowHead(_triangleArrowHead);
      };
      return ObjectExtensions.<BaseConnection<ESuperType>>operator_doubleArrow(_baseConnection, _function);
    }
    
    @Override
    public void calls() {
      final Function1<ESuperType, EClass> _function = (ESuperType it) -> {
        return it.getSuperType();
      };
      this.<EClass>target(EcoreDiagramConfig.this.eClassNode, _function);
    }
  };
  
  @Override
  protected <ARG extends Object> void entryCalls(final ARG domainArgument, @Extension final MappingAcceptor<ARG> acceptor) {
    boolean _matched = false;
    if (domainArgument instanceof EClass) {
      _matched=true;
      acceptor.add(this.eClassNode);
    }
    if (!_matched) {
      if (domainArgument instanceof EPackage) {
        _matched=true;
        acceptor.add(this.ePackageNode);
        acceptor.add(this.ePackageDiagram);
        acceptor.add(this.ePackageInheritanceDiagram);
        acceptor.add(this.ePackageContainmentDiagram);
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
