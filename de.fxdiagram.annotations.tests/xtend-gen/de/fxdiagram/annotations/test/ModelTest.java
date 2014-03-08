package de.fxdiagram.annotations.test;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.XModelProvider;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.shape.Polygon;

@ModelNode({ "layoutX", "string", "integer", "long", "bool", "unprecise", "names", "selfRef" })
@SuppressWarnings("all")
public abstract class ModelTest extends Polygon implements XModelProvider {
  private IntegerProperty integer;
  
  private LongProperty longProperty;
  
  private BooleanProperty bool;
  
  private FloatProperty unprecise;
  
  private ObjectProperty<String> stringProperty;
  
  private StringProperty string1;
  
  private ObjectProperty<ModelTest> selfRef;
  
  private ListProperty<String> names;
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(layoutXProperty(), Double.class);
    modelElement.addProperty(stringProperty, String.class);
    modelElement.addProperty(integer, Integer.class);
    modelElement.addProperty(longProperty, Long.class);
    modelElement.addProperty(bool, Boolean.class);
    modelElement.addProperty(unprecise, Float.class);
    modelElement.addProperty(names, String.class);
    modelElement.addProperty(selfRef, ModelTest.class);
  }
}
