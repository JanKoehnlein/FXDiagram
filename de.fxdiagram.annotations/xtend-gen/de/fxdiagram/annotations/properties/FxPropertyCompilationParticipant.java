package de.fxdiagram.annotations.properties;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.FxProperty;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyFloatProperty;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.TransformationParticipant;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Type;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend.lib.macro.declaration.Visibility;
import org.eclipse.xtend.lib.macro.expression.Expression;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class FxPropertyCompilationParticipant implements TransformationParticipant<MutableFieldDeclaration> {
  public void doTransform(final List<? extends MutableFieldDeclaration> fields, @Extension final TransformationContext context) {
    final Type annotationType = context.findTypeGlobally(FxProperty.class);
    for (final MutableFieldDeclaration f : fields) {
      {
        final AnnotationReference annotation = f.findAnnotation(annotationType);
        Object _value = annotation.getValue("readOnly");
        final boolean isReadOnly = (!Objects.equal(_value, Boolean.FALSE));
        TypeReference _type = f.getType();
        Type _type_1 = _type.getType();
        String _qualifiedName = _type_1.getQualifiedName();
        final boolean isList = Objects.equal(_qualifiedName, "javafx.collections.ObservableList");
        final String fieldName = f.getSimpleName();
        final TypeReference fieldType = f.getType();
        String _simpleName = f.getSimpleName();
        final String propName = (_simpleName + "Property");
        TypeReference _type_2 = f.getType();
        final TypeReference propType = this.toPropertyType(_type_2, isReadOnly, context);
        TypeReference _type_3 = f.getType();
        final TypeReference propTypeAPI = this.toPropertyType_API(_type_3, isReadOnly, context);
        MutableTypeDeclaration _declaringType = f.getDeclaringType();
        final MutableClassDeclaration clazz = ((MutableClassDeclaration) _declaringType);
        this.createField(f, clazz, propName, propType, fieldName, fieldType, isReadOnly, isList, propTypeAPI);
      }
    }
  }
  
  public void createField(final MutableFieldDeclaration f, final MutableClassDeclaration clazz, final String propName, final TypeReference propType, final String fieldName, final TypeReference fieldType, final boolean isReadOnly, final boolean isList, final TypeReference propTypeAPI) {
    Expression _initializer = f.getInitializer();
    boolean _equals = Objects.equal(_initializer, null);
    if (_equals) {
      final Procedure1<MutableFieldDeclaration> _function = new Procedure1<MutableFieldDeclaration>() {
        public void apply(final MutableFieldDeclaration it) {
          it.setType(propType);
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("new ");
              String _javaCode = it.toJavaCode(propType);
              _builder.append(_javaCode, "");
              _builder.append("(this, \"");
              _builder.append(fieldName, "");
              _builder.append("\")");
              return _builder;
            }
          };
          it.setInitializer(_function);
        }
      };
      clazz.addField(propName, _function);
    } else {
      final Procedure1<MutableFieldDeclaration> _function_1 = new Procedure1<MutableFieldDeclaration>() {
        public void apply(final MutableFieldDeclaration it) {
          it.setType(propType);
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("new ");
              String _javaCode = it.toJavaCode(propType);
              _builder.append(_javaCode, "");
              _builder.append("(this, \"");
              _builder.append(fieldName, "");
              _builder.append("\",_init");
              String _firstUpper = StringExtensions.toFirstUpper(fieldName);
              _builder.append(_firstUpper, "");
              _builder.append("())");
              return _builder;
            }
          };
          it.setInitializer(_function);
        }
      };
      clazz.addField(propName, _function_1);
      String _firstUpper = StringExtensions.toFirstUpper(fieldName);
      String _plus = ("_init" + _firstUpper);
      final Procedure1<MutableMethodDeclaration> _function_2 = new Procedure1<MutableMethodDeclaration>() {
        public void apply(final MutableMethodDeclaration it) {
          it.setReturnType(fieldType);
          it.setVisibility(Visibility.PRIVATE);
          it.setStatic(true);
          it.setFinal(true);
          Expression _initializer = f.getInitializer();
          it.setBody(_initializer);
        }
      };
      clazz.addMethod(_plus, _function_2);
    }
    String _firstUpper_1 = StringExtensions.toFirstUpper(fieldName);
    String _plus_1 = ("get" + _firstUpper_1);
    final Procedure1<MutableMethodDeclaration> _function_3 = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.setReturnType(fieldType);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("return this.");
            _builder.append(propName, "");
            _builder.append(".get();");
            _builder.newLineIfNotEmpty();
            return _builder;
          }
        };
        it.setBody(_function);
      }
    };
    clazz.addMethod(_plus_1, _function_3);
    if (((!isReadOnly) && (!isList))) {
      String _firstUpper_2 = StringExtensions.toFirstUpper(fieldName);
      String _plus_2 = ("set" + _firstUpper_2);
      final Procedure1<MutableMethodDeclaration> _function_4 = new Procedure1<MutableMethodDeclaration>() {
        public void apply(final MutableMethodDeclaration it) {
          it.addParameter(fieldName, fieldType);
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("this.");
              _builder.append(propName, "");
              _builder.append(".set(");
              _builder.append(fieldName, "");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              return _builder;
            }
          };
          it.setBody(_function);
        }
      };
      clazz.addMethod(_plus_2, _function_4);
    }
    final Procedure1<MutableMethodDeclaration> _function_5 = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.setReturnType(propTypeAPI);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("return ");
            {
              if (isReadOnly) {
                _builder.append("this.");
                _builder.append(propName, "");
                _builder.append(".getReadOnlyProperty()");
              } else {
                _builder.append("this.");
                _builder.append(propName, "");
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            return _builder;
          }
        };
        it.setBody(_function);
      }
    };
    clazz.addMethod((fieldName + "Property"), _function_5);
    f.remove();
  }
  
  public String defaultValue(final TypeReference ref) {
    String _switchResult = null;
    String _string = ref.toString();
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_string, "boolean")) {
        _matched=true;
        _switchResult = "false";
      }
    }
    if (!_matched) {
      if (Objects.equal(_string, "double")) {
        _matched=true;
        _switchResult = "0d";
      }
    }
    if (!_matched) {
      if (Objects.equal(_string, "float")) {
        _matched=true;
        _switchResult = "0f";
      }
    }
    if (!_matched) {
      if (Objects.equal(_string, "long")) {
        _matched=true;
        _switchResult = "0";
      }
    }
    if (!_matched) {
      if (Objects.equal(_string, "int")) {
        _matched=true;
        _switchResult = "0";
      }
    }
    if (!_matched) {
      _switchResult = "null";
    }
    return _switchResult;
  }
  
  public TypeReference toPropertyType_API(final TypeReference ref, final boolean isReadOnly, @Extension final TransformationContext context) {
    TypeReference _xifexpression = null;
    if (isReadOnly) {
      TypeReference _switchResult = null;
      Type _type = ref.getType();
      String _qualifiedName = _type.getQualifiedName();
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "boolean")) {
          _matched=true;
          _switchResult = context.newTypeReference(ReadOnlyBooleanProperty.class);
        }
      }
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "double")) {
          _matched=true;
          _switchResult = context.newTypeReference(ReadOnlyDoubleProperty.class);
        }
      }
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "float")) {
          _matched=true;
          _switchResult = context.newTypeReference(ReadOnlyFloatProperty.class);
        }
      }
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "long")) {
          _matched=true;
          _switchResult = context.newTypeReference(ReadOnlyLongProperty.class);
        }
      }
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "java.lang.String")) {
          _matched=true;
          _switchResult = context.newTypeReference(ReadOnlyStringProperty.class);
        }
      }
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "int")) {
          _matched=true;
          _switchResult = context.newTypeReference(ReadOnlyIntegerProperty.class);
        }
      }
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "javafx.collections.ObservableList")) {
          _matched=true;
          List<TypeReference> _actualTypeArguments = ref.getActualTypeArguments();
          TypeReference _head = IterableExtensions.<TypeReference>head(_actualTypeArguments);
          _switchResult = context.newTypeReference(ReadOnlyListProperty.class, _head);
        }
      }
      if (!_matched) {
        _switchResult = context.newTypeReference(ReadOnlyObjectProperty.class, ref);
      }
      _xifexpression = _switchResult;
    } else {
      TypeReference _switchResult_1 = null;
      Type _type_1 = ref.getType();
      String _qualifiedName_1 = _type_1.getQualifiedName();
      boolean _matched_1 = false;
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "boolean")) {
          _matched_1=true;
          _switchResult_1 = context.newTypeReference(BooleanProperty.class);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "double")) {
          _matched_1=true;
          _switchResult_1 = context.newTypeReference(DoubleProperty.class);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "float")) {
          _matched_1=true;
          _switchResult_1 = context.newTypeReference(FloatProperty.class);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "long")) {
          _matched_1=true;
          _switchResult_1 = context.newTypeReference(LongProperty.class);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "java.lang.String")) {
          _matched_1=true;
          _switchResult_1 = context.newTypeReference(StringProperty.class);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "int")) {
          _matched_1=true;
          _switchResult_1 = context.newTypeReference(IntegerProperty.class);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "javafx.collections.ObservableList")) {
          _matched_1=true;
          List<TypeReference> _actualTypeArguments_1 = ref.getActualTypeArguments();
          TypeReference _head_1 = IterableExtensions.<TypeReference>head(_actualTypeArguments_1);
          _switchResult_1 = context.newTypeReference(ListProperty.class, _head_1);
        }
      }
      if (!_matched_1) {
        _switchResult_1 = context.newTypeReference(ObjectProperty.class, ref);
      }
      _xifexpression = _switchResult_1;
    }
    return _xifexpression;
  }
  
  public TypeReference toPropertyType(final TypeReference ref, final boolean isReadOnly, @Extension final TransformationContext context) {
    TypeReference _xifexpression = null;
    if (isReadOnly) {
      TypeReference _switchResult = null;
      Type _type = ref.getType();
      String _qualifiedName = _type.getQualifiedName();
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "boolean")) {
          _matched=true;
          _switchResult = context.newTypeReference(ReadOnlyBooleanWrapper.class);
        }
      }
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "double")) {
          _matched=true;
          _switchResult = context.newTypeReference(ReadOnlyDoubleWrapper.class);
        }
      }
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "float")) {
          _matched=true;
          _switchResult = context.newTypeReference(ReadOnlyFloatWrapper.class);
        }
      }
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "long")) {
          _matched=true;
          _switchResult = context.newTypeReference(ReadOnlyLongWrapper.class);
        }
      }
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "java.lang.String")) {
          _matched=true;
          _switchResult = context.newTypeReference(ReadOnlyStringWrapper.class);
        }
      }
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "int")) {
          _matched=true;
          _switchResult = context.newTypeReference(ReadOnlyIntegerWrapper.class);
        }
      }
      if (!_matched) {
        if (Objects.equal(_qualifiedName, "javafx.collections.ObservableList")) {
          _matched=true;
          List<TypeReference> _actualTypeArguments = ref.getActualTypeArguments();
          TypeReference _head = IterableExtensions.<TypeReference>head(_actualTypeArguments);
          _switchResult = context.newTypeReference(ReadOnlyListWrapper.class, _head);
        }
      }
      if (!_matched) {
        _switchResult = context.newTypeReference(ReadOnlyObjectWrapper.class, ref);
      }
      _xifexpression = _switchResult;
    } else {
      TypeReference _switchResult_1 = null;
      Type _type_1 = ref.getType();
      String _qualifiedName_1 = _type_1.getQualifiedName();
      boolean _matched_1 = false;
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "boolean")) {
          _matched_1=true;
          _switchResult_1 = context.newTypeReference(SimpleBooleanProperty.class);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "double")) {
          _matched_1=true;
          _switchResult_1 = context.newTypeReference(SimpleDoubleProperty.class);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "float")) {
          _matched_1=true;
          _switchResult_1 = context.newTypeReference(SimpleFloatProperty.class);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "long")) {
          _matched_1=true;
          _switchResult_1 = context.newTypeReference(SimpleLongProperty.class);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "java.lang.String")) {
          _matched_1=true;
          _switchResult_1 = context.newTypeReference(SimpleStringProperty.class);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "int")) {
          _matched_1=true;
          _switchResult_1 = context.newTypeReference(SimpleIntegerProperty.class);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_qualifiedName_1, "javafx.collections.ObservableList")) {
          _matched_1=true;
          List<TypeReference> _actualTypeArguments_1 = ref.getActualTypeArguments();
          TypeReference _head_1 = IterableExtensions.<TypeReference>head(_actualTypeArguments_1);
          _switchResult_1 = context.newTypeReference(SimpleListProperty.class, _head_1);
        }
      }
      if (!_matched_1) {
        _switchResult_1 = context.newTypeReference(SimpleObjectProperty.class, ref);
      }
      _xifexpression = _switchResult_1;
    }
    return _xifexpression;
  }
}
