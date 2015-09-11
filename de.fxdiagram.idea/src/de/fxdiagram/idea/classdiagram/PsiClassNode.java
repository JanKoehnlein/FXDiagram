package de.fxdiagram.idea.classdiagram;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.idea.psi.PsiDomainObjectDescriptor;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.animations.Inflator;
import de.fxdiagram.lib.nodes.AbstractClassNode;
import de.fxdiagram.lib.nodes.ClassModel;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.shapes.BaseClassNode;
import de.fxdiagram.mapping.shapes.BaseFlipNode;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

import java.util.function.Consumer;

/**
 * Copied and adapted from JvmTypeNode
 * TODO: extract a good super class
 */
@ModelNode({ "showPackage", "showAttributes", "showMethods", "bgColor" })
public class PsiClassNode extends BaseClassNode<PsiClass> {

  public PsiClassNode(final IMappedElementDescriptor<PsiClass> descriptor) {
    super(descriptor);
  }
  
  @Override
  public PsiDomainObjectDescriptor<PsiClass> getDomainObjectDescriptor() {
    return (PsiDomainObjectDescriptor<PsiClass>) super.getDomainObjectDescriptor();
  }

  @Override
  public ClassModel inferClassModel() {
      return getDomainObjectDescriptor().withDomainObject( psiClass -> {
        ClassModel classModel = new ClassModel();
        classModel.setFileName(psiClass.getContainingFile().getName());
        String fqn = psiClass.getQualifiedName();
        final int lastIndexOf = fqn.lastIndexOf(".");
        if (lastIndexOf != -1) {
          classModel.setNamespace(fqn.substring(0, lastIndexOf));
        } else {
          classModel.setNamespace("<default>");
        }
        classModel.setName(psiClass.getName());
        for(PsiField field: PsiJavaUtil.getAttributes(psiClass)) {
          classModel.getAttributes().add(field.getName() + ": " +field.getType().getPresentableText());
        }
        for(PsiMethod operation: PsiJavaUtil.getOperations(psiClass)) {
          classModel.getOperations().add(operation.getName() + "() : " + operation.getReturnType().getPresentableText());
        }
        return classModel;
      });
  }
}
