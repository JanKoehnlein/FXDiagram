package de.fxdiagram.annotations.logging

import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.TransformationContext
import java.util.logging.Logger

@Active(LoggingProcessor)
annotation Logging {
	
}

class LoggingProcessor extends AbstractClassProcessor {
	
	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		annotatedClass.addField("LOG", [
			static = true
			type = Logger.findTypeGlobally.newTypeReference
			initializer = ['''
				Logger.getLogger("«annotatedClass.qualifiedName»");
			''']
		]) 
	}
} 