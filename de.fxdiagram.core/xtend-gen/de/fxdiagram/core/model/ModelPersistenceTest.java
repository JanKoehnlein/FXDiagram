package de.fxdiagram.core.model;

import de.fxdiagram.core.model.ModelLoad;
import de.fxdiagram.core.model.TestBean;
import de.fxdiagram.core.model.TestEnum;
import java.io.StringReader;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class ModelPersistenceTest {
  @Test
  public void testReadEnum() {
    ModelLoad _modelLoad = new ModelLoad();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\"class\":\"de.fxdiagram.core.model.TestBean\",");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\"testEnum\":\"BAR\"");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringReader _stringReader = new StringReader(_builder.toString());
    Class<? extends ModelPersistenceTest> _class = this.getClass();
    ClassLoader _classLoader = _class.getClassLoader();
    final Object object = _modelLoad.load(_stringReader, _classLoader);
    Assert.assertTrue((object instanceof TestBean));
    TestEnum _testEnum = ((TestBean) object).getTestEnum();
    Assert.assertEquals(TestEnum.BAR, _testEnum);
  }
}
