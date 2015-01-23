package de.fxdiagram.core.model

import de.fxdiagram.annotations.properties.FxProperty
import java.io.StringReader
import org.junit.Test

import static extension org.junit.Assert.*
import de.fxdiagram.annotations.properties.ModelNode

class ModelPersistenceTest {
	
	@Test
	def void testReadEnum() {
		val object = new ModelLoad().load(new StringReader('''
					{
						"__class":"de.fxdiagram.core.model.TestBean",
						"testEnum":"BAR"
					}
				'''))
		assertTrue(object instanceof TestBean)
		TestEnum.BAR.assertEquals((object as TestBean).testEnum)
	}
}

@ModelNode(inherit=false, value=#['testEnum'])
class TestBean {
	@FxProperty TestEnum testEnum
}

enum TestEnum {
	FOO, BAR
}