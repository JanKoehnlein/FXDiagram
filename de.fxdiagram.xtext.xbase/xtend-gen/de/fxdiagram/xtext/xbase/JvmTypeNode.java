package de.fxdiagram.xtext.xbase;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import de.fxdiagram.lib.nodes.ClassModel;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.shapes.BaseClassNode;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
import javafx.collections.ObservableList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class JvmTypeNode extends BaseClassNode<JvmDeclaredType> {
  @Inject
  @Extension
  private JvmDomainUtil _jvmDomainUtil;
  
  public JvmTypeNode(final JvmEObjectDescriptor<JvmDeclaredType> descriptor) {
    super(descriptor);
  }
  
  @Override
  public ClassModel inferClassModel() {
    IMappedElementDescriptor<JvmDeclaredType> _domainObject = this.getDomainObject();
    final Function1<JvmDeclaredType, ClassModel> _function = (JvmDeclaredType type) -> {
      ClassModel _classModel = new ClassModel();
      final Procedure1<ClassModel> _function_1 = (ClassModel it) -> {
        IMappedElementDescriptor<JvmDeclaredType> _domainObject_1 = this.getDomainObject();
        String _uri = ((JvmEObjectDescriptor<JvmDeclaredType>) _domainObject_1).getUri();
        URI _createURI = URI.createURI(_uri);
        String _lastSegment = _createURI.lastSegment();
        it.setFileName(_lastSegment);
        String _packageName = type.getPackageName();
        it.setNamespace(_packageName);
        String _simpleName = type.getSimpleName();
        it.setName(_simpleName);
        ObservableList<String> _attributes = it.getAttributes();
        Iterable<JvmField> _attributes_1 = this._jvmDomainUtil.getAttributes(type);
        final Function1<JvmField, String> _function_2 = (JvmField it_1) -> {
          String _simpleName_1 = it_1.getSimpleName();
          String _plus = (_simpleName_1 + ": ");
          String _simpleName_2 = type.getSimpleName();
          return (_plus + _simpleName_2);
        };
        Iterable<String> _map = IterableExtensions.<JvmField, String>map(_attributes_1, _function_2);
        Iterables.<String>addAll(_attributes, _map);
        ObservableList<String> _operations = it.getOperations();
        Iterable<JvmOperation> _methods = this._jvmDomainUtil.getMethods(type);
        final Function1<JvmOperation, String> _function_3 = (JvmOperation it_1) -> {
          String _simpleName_1 = it_1.getSimpleName();
          String _plus = (_simpleName_1 + "(): ");
          JvmTypeReference _returnType = it_1.getReturnType();
          String _simpleName_2 = _returnType.getSimpleName();
          return (_plus + _simpleName_2);
        };
        Iterable<String> _map_1 = IterableExtensions.<JvmOperation, String>map(_methods, _function_3);
        Iterables.<String>addAll(_operations, _map_1);
      };
      return ObjectExtensions.<ClassModel>operator_doubleArrow(_classModel, _function_1);
    };
    return _domainObject.<ClassModel>withDomainObject(_function);
  }
}
