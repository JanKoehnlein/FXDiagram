package de.fxdiagram.annotations.properties;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.Immutable;
import de.fxdiagram.annotations.properties.Lazy;
import de.fxdiagram.annotations.properties.ReadOnly;
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
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.TransformationParticipant;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy.CompilationContext;
import org.eclipse.xtend.lib.macro.declaration.MutableAnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Type;
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration;
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
    final Type fxImmutableAnnotation = context.findTypeGlobally(Immutable.class);
    final Type dataAnnotation = context.findTypeGlobally(Data.class);
    final Type fxReadonlyAnnotation = context.findTypeGlobally(ReadOnly.class);
    final Type fxLazyAnnotation = context.findTypeGlobally(Lazy.class);
    for (final MutableFieldDeclaration f : fields) {
      {
        final boolean readonly = this.readonly(f, fxReadonlyAnnotation);
        final boolean lazy = this.lazy(f, fxLazyAnnotation);
        final boolean immutableType = this.immutableType(f, fxImmutableAnnotation, dataAnnotation);
        TypeReference _type = f.getType();
        Type _type_1 = _type.getType();
        String _qualifiedName = _type_1.getQualifiedName();
        final boolean isList = Objects.equal(_qualifiedName, "javafx.collections.ObservableList");
        final String fieldName = f.getSimpleName();
        final TypeReference fieldType = f.getType();
        String _simpleName = f.getSimpleName();
        final String propName = (_simpleName + "Property");
        TypeReference _type_2 = f.getType();
        final TypeReference propType = this.toPropertyType(_type_2, readonly, context);
        TypeReference _type_3 = f.getType();
        final TypeReference propTypeAPI = this.toPropertyType_API(_type_3, readonly, context);
        MutableTypeDeclaration _declaringType = f.getDeclaringType();
        final MutableClassDeclaration clazz = ((MutableClassDeclaration) _declaringType);
        if (lazy) {
          this.createLazyField(immutableType, f, clazz, propName, propType, fieldName, fieldType, readonly, isList, propTypeAPI);
        } else {
          this.createNonLazyField(immutableType, f, clazz, propName, propType, fieldName, fieldType, readonly, isList, propTypeAPI);
        }
      }
    }
  }
  
  public void createNonLazyField(final boolean immutableType, final MutableFieldDeclaration f, final MutableClassDeclaration clazz, final String propName, final TypeReference propType, final String fieldName, final TypeReference fieldType, final boolean readonly, final boolean isList, final TypeReference propTypeAPI) {
    Expression _initializer = f.getInitializer();
    boolean _equals = Objects.equal(_initializer, null);
    if (_equals) {
      final Procedure1<MutableFieldDeclaration> _function = new Procedure1<MutableFieldDeclaration>() {
        public void apply(final MutableFieldDeclaration it) {
          it.setType(propType);
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationContext it) {
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
            public CharSequence compile(final CompilationContext it) {
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
          public CharSequence compile(final CompilationContext it) {
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
    boolean _and = false;
    boolean _not = (!readonly);
    if (!_not) {
      _and = false;
    } else {
      boolean _not_1 = (!isList);
      _and = (_not && _not_1);
    }
    if (_and) {
      String _firstUpper_2 = StringExtensions.toFirstUpper(fieldName);
      String _plus_2 = ("set" + _firstUpper_2);
      final Procedure1<MutableMethodDeclaration> _function_4 = new Procedure1<MutableMethodDeclaration>() {
        public void apply(final MutableMethodDeclaration it) {
          it.addParameter(fieldName, fieldType);
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationContext it) {
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
    String _plus_3 = (fieldName + "Property");
    final Procedure1<MutableMethodDeclaration> _function_5 = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.setReturnType(propTypeAPI);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("return ");
            {
              if (readonly) {
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
    clazz.addMethod(_plus_3, _function_5);
    f.remove();
  }
  
  public void createLazyField(final boolean immutableType, final MutableFieldDeclaration f, final MutableClassDeclaration clazz, final String propName, final TypeReference propType, final String fieldName, final TypeReference fieldType, final boolean readonly, final boolean isList, final TypeReference propTypeAPI) {
    if (immutableType) {
      Expression _initializer = f.getInitializer();
      boolean _equals = Objects.equal(_initializer, null);
      if (_equals) {
        String _simpleName = f.getSimpleName();
        String _upperCase = _simpleName.toUpperCase();
        String _plus = ("DEFAULT_" + _upperCase);
        final Procedure1<MutableFieldDeclaration> _function = new Procedure1<MutableFieldDeclaration>() {
          public void apply(final MutableFieldDeclaration it) {
            TypeReference _type = f.getType();
            it.setType(_type);
            final CompilationStrategy _function = new CompilationStrategy() {
              public CharSequence compile(final CompilationContext it) {
                TypeReference _type = f.getType();
                String _defaultValue = FxPropertyCompilationParticipant.this.defaultValue(_type);
                return _defaultValue;
              }
            };
            it.setInitializer(_function);
            it.setFinal(true);
            it.setStatic(true);
          }
        };
        clazz.addField(_plus, _function);
      } else {
        String _simpleName_1 = f.getSimpleName();
        String _upperCase_1 = _simpleName_1.toUpperCase();
        String _plus_1 = ("DEFAULT_" + _upperCase_1);
        final Procedure1<MutableFieldDeclaration> _function_1 = new Procedure1<MutableFieldDeclaration>() {
          public void apply(final MutableFieldDeclaration it) {
            TypeReference _type = f.getType();
            it.setType(_type);
            Expression _initializer = f.getInitializer();
            it.setInitializer(_initializer);
            it.setFinal(true);
            it.setStatic(true);
          }
        };
        clazz.addField(_plus_1, _function_1);
      }
    }
    final Procedure1<MutableFieldDeclaration> _function_2 = new Procedure1<MutableFieldDeclaration>() {
      public void apply(final MutableFieldDeclaration it) {
        it.setType(propType);
      }
    };
    clazz.addField(propName, _function_2);
    String _firstUpper = StringExtensions.toFirstUpper(fieldName);
    String _plus_2 = ("get" + _firstUpper);
    final Procedure1<MutableMethodDeclaration> _function_3 = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.setReturnType(fieldType);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("return (this.");
            _builder.append(propName, "");
            _builder.append(" != null)? this.");
            _builder.append(propName, "");
            _builder.append(".get() : ");
            {
              if (immutableType) {
                _builder.append("DEFAULT_");
                String _upperCase = fieldName.toUpperCase();
                _builder.append(_upperCase, "");
              } else {
                _builder.append("this.");
                _builder.append(fieldName, "");
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
    clazz.addMethod(_plus_2, _function_3);
    boolean _and = false;
    boolean _not = (!readonly);
    if (!_not) {
      _and = false;
    } else {
      boolean _not_1 = (!isList);
      _and = (_not && _not_1);
    }
    if (_and) {
      String _firstUpper_1 = StringExtensions.toFirstUpper(fieldName);
      String _plus_3 = ("set" + _firstUpper_1);
      final Procedure1<MutableMethodDeclaration> _function_4 = new Procedure1<MutableMethodDeclaration>() {
        public void apply(final MutableMethodDeclaration it) {
          it.addParameter(fieldName, fieldType);
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              {
                if (immutableType) {
                  _builder.append("this.");
                  _builder.append(propName, "");
                  _builder.append("().set(");
                  _builder.append(fieldName, "");
                  _builder.append(");");
                  _builder.newLineIfNotEmpty();
                } else {
                  _builder.append("if (");
                  _builder.append(propName, "");
                  _builder.append(" != null) {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("this.");
                  _builder.append(propName, "	");
                  _builder.append(".set(");
                  _builder.append(fieldName, "	");
                  _builder.append(");");
                  _builder.newLineIfNotEmpty();
                  _builder.append("} else {");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("this.");
                  _builder.append(fieldName, "	");
                  _builder.append(" = ");
                  _builder.append(fieldName, "	");
                  _builder.append(";");
                  _builder.newLineIfNotEmpty();
                  _builder.append("}");
                  _builder.newLine();
                }
              }
              return _builder;
            }
          };
          it.setBody(_function);
        }
      };
      clazz.addMethod(_plus_3, _function_4);
    }
    String _plus_4 = (fieldName + "Property");
    final Procedure1<MutableMethodDeclaration> _function_5 = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.setReturnType(propTypeAPI);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("if (this.");
            _builder.append(propName, "");
            _builder.append(" == null) { ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("this.");
            _builder.append(propName, "	");
            _builder.append(" = new ");
            String _javaCode = it.toJavaCode(propType);
            _builder.append(_javaCode, "	");
            _builder.append("(this, \"");
            _builder.append(fieldName, "	");
            _builder.append("\", ");
            {
              if (immutableType) {
                _builder.append("DEFAULT_");
                String _upperCase = fieldName.toUpperCase();
                _builder.append(_upperCase, "	");
              } else {
                _builder.append("this.");
                _builder.append(fieldName, "	");
              }
            }
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.append("return ");
            {
              if (readonly) {
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
    clazz.addMethod(_plus_4, _function_5);
    if (immutableType) {
      f.remove();
    }
  }
  
  public boolean readonly(final MutableFieldDeclaration field, final Type readonlyAnnotation) {
    MutableAnnotationReference _findAnnotation = field.findAnnotation(readonlyAnnotation);
    return (!Objects.equal(_findAnnotation, null));
  }
  
  public boolean lazy(final MutableFieldDeclaration field, final Type noneLazyAnnotation) {
    MutableAnnotationReference _findAnnotation = field.findAnnotation(noneLazyAnnotation);
    return (!Objects.equal(_findAnnotation, null));
  }
  
  public boolean immutableType(final MutableFieldDeclaration field, final Type fxImmutableAnnotation, final Type dataAnnotation) {
    boolean _switchResult = false;
    TypeReference _type = field.getType();
    String _string = _type.toString();
    final String _switchValue = _string;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,"boolean")) {
        _matched=true;
        _switchResult = true;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"double")) {
        _matched=true;
        _switchResult = true;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"float")) {
        _matched=true;
        _switchResult = true;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"long")) {
        _matched=true;
        _switchResult = true;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"String")) {
        _matched=true;
        _switchResult = true;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"int")) {
        _matched=true;
        _switchResult = true;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"javafx.collections.ObservableList")) {
        _matched=true;
        _switchResult = false;
      }
    }
    if (!_matched) {
      MutableAnnotationReference _findAnnotation = field.findAnnotation(fxImmutableAnnotation);
      boolean _notEquals = (!Objects.equal(_findAnnotation, null));
      if (_notEquals) {
        return true;
      } else {
        TypeReference _type_1 = field.getType();
        Type _type_2 = _type_1.getType();
        if ((_type_2 instanceof TypeDeclaration)) {
          TypeReference _type_3 = field.getType();
          Type _type_4 = _type_3.getType();
          final TypeDeclaration t = ((TypeDeclaration) _type_4);
          boolean _or = false;
          AnnotationReference _findAnnotation_1 = t.findAnnotation(fxImmutableAnnotation);
          boolean _notEquals_1 = (!Objects.equal(_findAnnotation_1, null));
          if (_notEquals_1) {
            _or = true;
          } else {
            AnnotationReference _findAnnotation_2 = t.findAnnotation(dataAnnotation);
            boolean _notEquals_2 = (!Objects.equal(_findAnnotation_2, null));
            _or = (_notEquals_1 || _notEquals_2);
          }
          final boolean rv = _or;
          return rv;
        } else {
          return false;
        }
      }
    }
    return _switchResult;
  }
  
  public String defaultValue(final TypeReference ref) {
    String _switchResult = null;
    String _string = ref.toString();
    final String _switchValue = _string;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,"boolean")) {
        _matched=true;
        _switchResult = "false";
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"double")) {
        _matched=true;
        _switchResult = "0d";
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"float")) {
        _matched=true;
        _switchResult = "0f";
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"long")) {
        _matched=true;
        _switchResult = "0";
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"int")) {
        _matched=true;
        _switchResult = "0";
      }
    }
    if (!_matched) {
      _switchResult = "null";
    }
    return _switchResult;
  }
  
  public TypeReference toPropertyType_API(final TypeReference ref, final boolean readonly, @Extension final TransformationContext context) {
    TypeReference _xifexpression = null;
    if (readonly) {
      TypeReference _switchResult = null;
      Type _type = ref.getType();
      String _qualifiedName = _type.getQualifiedName();
      final String _switchValue = _qualifiedName;
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(_switchValue,"boolean")) {
          _matched=true;
          TypeReference _newTypeReference = context.newTypeReference(ReadOnlyBooleanProperty.class);
          _switchResult = _newTypeReference;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,"double")) {
          _matched=true;
          TypeReference _newTypeReference_1 = context.newTypeReference(ReadOnlyDoubleProperty.class);
          _switchResult = _newTypeReference_1;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,"float")) {
          _matched=true;
          TypeReference _newTypeReference_2 = context.newTypeReference(ReadOnlyFloatProperty.class);
          _switchResult = _newTypeReference_2;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,"long")) {
          _matched=true;
          TypeReference _newTypeReference_3 = context.newTypeReference(ReadOnlyLongProperty.class);
          _switchResult = _newTypeReference_3;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,"java.lang.String")) {
          _matched=true;
          TypeReference _newTypeReference_4 = context.newTypeReference(ReadOnlyStringProperty.class);
          _switchResult = _newTypeReference_4;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,"int")) {
          _matched=true;
          TypeReference _newTypeReference_5 = context.newTypeReference(ReadOnlyIntegerProperty.class);
          _switchResult = _newTypeReference_5;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,"javafx.collections.ObservableList")) {
          _matched=true;
          List<TypeReference> _actualTypeArguments = ref.getActualTypeArguments();
          TypeReference _head = IterableExtensions.<TypeReference>head(_actualTypeArguments);
          TypeReference _newTypeReference_6 = context.newTypeReference(ReadOnlyListProperty.class, _head);
          _switchResult = _newTypeReference_6;
        }
      }
      if (!_matched) {
        TypeReference _newTypeReference_7 = context.newTypeReference(ReadOnlyObjectProperty.class, ref);
        _switchResult = _newTypeReference_7;
      }
      _xifexpression = _switchResult;
    } else {
      TypeReference _switchResult_1 = null;
      Type _type_1 = ref.getType();
      String _qualifiedName_1 = _type_1.getQualifiedName();
      final String _switchValue_1 = _qualifiedName_1;
      boolean _matched_1 = false;
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"boolean")) {
          _matched_1=true;
          TypeReference _newTypeReference_8 = context.newTypeReference(BooleanProperty.class);
          _switchResult_1 = _newTypeReference_8;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"double")) {
          _matched_1=true;
          TypeReference _newTypeReference_9 = context.newTypeReference(DoubleProperty.class);
          _switchResult_1 = _newTypeReference_9;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"float")) {
          _matched_1=true;
          TypeReference _newTypeReference_10 = context.newTypeReference(FloatProperty.class);
          _switchResult_1 = _newTypeReference_10;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"long")) {
          _matched_1=true;
          TypeReference _newTypeReference_11 = context.newTypeReference(LongProperty.class);
          _switchResult_1 = _newTypeReference_11;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"java.lang.String")) {
          _matched_1=true;
          TypeReference _newTypeReference_12 = context.newTypeReference(StringProperty.class);
          _switchResult_1 = _newTypeReference_12;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"int")) {
          _matched_1=true;
          TypeReference _newTypeReference_13 = context.newTypeReference(IntegerProperty.class);
          _switchResult_1 = _newTypeReference_13;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"javafx.collections.ObservableList")) {
          _matched_1=true;
          List<TypeReference> _actualTypeArguments_1 = ref.getActualTypeArguments();
          TypeReference _head_1 = IterableExtensions.<TypeReference>head(_actualTypeArguments_1);
          TypeReference _newTypeReference_14 = context.newTypeReference(ListProperty.class, _head_1);
          _switchResult_1 = _newTypeReference_14;
        }
      }
      if (!_matched_1) {
        TypeReference _newTypeReference_15 = context.newTypeReference(ObjectProperty.class, ref);
        _switchResult_1 = _newTypeReference_15;
      }
      _xifexpression = _switchResult_1;
    }
    return _xifexpression;
  }
  
  public TypeReference toPropertyType(final TypeReference ref, final boolean readonly, @Extension final TransformationContext context) {
    TypeReference _xifexpression = null;
    if (readonly) {
      TypeReference _switchResult = null;
      Type _type = ref.getType();
      String _qualifiedName = _type.getQualifiedName();
      final String _switchValue = _qualifiedName;
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(_switchValue,"boolean")) {
          _matched=true;
          TypeReference _newTypeReference = context.newTypeReference(ReadOnlyBooleanWrapper.class);
          _switchResult = _newTypeReference;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,"double")) {
          _matched=true;
          TypeReference _newTypeReference_1 = context.newTypeReference(ReadOnlyDoubleWrapper.class);
          _switchResult = _newTypeReference_1;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,"float")) {
          _matched=true;
          TypeReference _newTypeReference_2 = context.newTypeReference(ReadOnlyFloatWrapper.class);
          _switchResult = _newTypeReference_2;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,"long")) {
          _matched=true;
          TypeReference _newTypeReference_3 = context.newTypeReference(ReadOnlyLongWrapper.class);
          _switchResult = _newTypeReference_3;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,"java.lang.String")) {
          _matched=true;
          TypeReference _newTypeReference_4 = context.newTypeReference(ReadOnlyStringWrapper.class);
          _switchResult = _newTypeReference_4;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,"int")) {
          _matched=true;
          TypeReference _newTypeReference_5 = context.newTypeReference(ReadOnlyIntegerWrapper.class);
          _switchResult = _newTypeReference_5;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,"javafx.collections.ObservableList")) {
          _matched=true;
          List<TypeReference> _actualTypeArguments = ref.getActualTypeArguments();
          TypeReference _head = IterableExtensions.<TypeReference>head(_actualTypeArguments);
          TypeReference _newTypeReference_6 = context.newTypeReference(ReadOnlyListWrapper.class, _head);
          _switchResult = _newTypeReference_6;
        }
      }
      if (!_matched) {
        TypeReference _newTypeReference_7 = context.newTypeReference(ReadOnlyObjectWrapper.class, ref);
        _switchResult = _newTypeReference_7;
      }
      _xifexpression = _switchResult;
    } else {
      TypeReference _switchResult_1 = null;
      Type _type_1 = ref.getType();
      String _qualifiedName_1 = _type_1.getQualifiedName();
      final String _switchValue_1 = _qualifiedName_1;
      boolean _matched_1 = false;
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"boolean")) {
          _matched_1=true;
          TypeReference _newTypeReference_8 = context.newTypeReference(SimpleBooleanProperty.class);
          _switchResult_1 = _newTypeReference_8;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"double")) {
          _matched_1=true;
          TypeReference _newTypeReference_9 = context.newTypeReference(SimpleDoubleProperty.class);
          _switchResult_1 = _newTypeReference_9;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"float")) {
          _matched_1=true;
          TypeReference _newTypeReference_10 = context.newTypeReference(SimpleFloatProperty.class);
          _switchResult_1 = _newTypeReference_10;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"long")) {
          _matched_1=true;
          TypeReference _newTypeReference_11 = context.newTypeReference(SimpleLongProperty.class);
          _switchResult_1 = _newTypeReference_11;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"java.lang.String")) {
          _matched_1=true;
          TypeReference _newTypeReference_12 = context.newTypeReference(SimpleStringProperty.class);
          _switchResult_1 = _newTypeReference_12;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"int")) {
          _matched_1=true;
          TypeReference _newTypeReference_13 = context.newTypeReference(SimpleIntegerProperty.class);
          _switchResult_1 = _newTypeReference_13;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(_switchValue_1,"javafx.collections.ObservableList")) {
          _matched_1=true;
          List<TypeReference> _actualTypeArguments_1 = ref.getActualTypeArguments();
          TypeReference _head_1 = IterableExtensions.<TypeReference>head(_actualTypeArguments_1);
          TypeReference _newTypeReference_14 = context.newTypeReference(SimpleListProperty.class, _head_1);
          _switchResult_1 = _newTypeReference_14;
        }
      }
      if (!_matched_1) {
        TypeReference _newTypeReference_15 = context.newTypeReference(SimpleObjectProperty.class, ref);
        _switchResult_1 = _newTypeReference_15;
      }
      _xifexpression = _switchResult_1;
    }
    return _xifexpression;
  }
}
