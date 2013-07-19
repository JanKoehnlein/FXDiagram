package de.fxdiagram.annotations.logging;

import java.util.logging.Logger;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy.CompilationContext;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Type;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LoggingProcessor extends AbstractClassProcessor {
  public void doTransform(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
    final Procedure1<MutableFieldDeclaration> _function = new Procedure1<MutableFieldDeclaration>() {
        public void apply(final MutableFieldDeclaration it) {
          it.setStatic(true);
          Type _findTypeGlobally = context.findTypeGlobally(Logger.class);
          TypeReference _newTypeReference = context.newTypeReference(_findTypeGlobally);
          it.setType(_newTypeReference);
          final CompilationStrategy _function = new CompilationStrategy() {
              public CharSequence compile(final CompilationContext it) {
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("Logger.getLogger(\"");
                String _qualifiedName = annotatedClass.getQualifiedName();
                _builder.append(_qualifiedName, "");
                _builder.append("\");");
                _builder.newLineIfNotEmpty();
                return _builder;
              }
            };
          it.setInitializer(_function);
        }
      };
    annotatedClass.addField("LOG", _function);
  }
}
