package de.fxdiagram.core.model

import de.fxdiagram.annotations.properties.FxProperty
import java.io.StringReader
import org.junit.Test
import static extension org.junit.Assert.*

class ModelPersistenceTest {
	
	@Test
	def void testReadEnum() {
		val object = new ModelLoad().load(new StringReader('''
					{
						"class":"de.fxdiagram.core.model.TestBean",
						"testEnum":"BAR"
					}
				'''))
		assertTrue(object instanceof TestBean)
		TestEnum.BAR.assertEquals((object as TestBean).testEnum)
	}
}

class TestBean implements XModelProvider {
	@FxProperty TestEnum testEnum
	
	override populate(ModelElement it) {
		addProperty(testEnumProperty, TestEnum)
	}
}

enum TestEnum {
	FOO, BAR
}