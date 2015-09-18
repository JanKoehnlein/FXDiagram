package de.fxdiagram.idea.classdiagram;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.idea.psi.AbstractPsiDiagramConfig;
import de.fxdiagram.mapping.*;
import de.fxdiagram.mapping.shapes.BaseClassNode;
import javafx.scene.paint.Color;
import static de.fxdiagram.mapping.shapes.BaseClassNode.*;

/**
 * @author Jan Koehnlein
 */
public class IdeaClassDiagramConfig extends AbstractPsiDiagramConfig {

    private NodeMapping<PsiClass> classNode = new NodeMapping<PsiClass>(this, "classNode", "Class Node") {
        @Override
        public XNode createNode(IMappedElementDescriptor<PsiClass> descriptor) {
            return new BaseClassNode(descriptor);
        }

        @Override
        protected void calls() {
            labelFor(typeName, it -> it);
            labelFor(packageName, it -> it);
            labelFor(fileName, it -> it);
            labelForEach(attribute, it -> PsiJavaUtil.getAttributes(it));
            labelForEach(operation, it -> PsiJavaUtil.getOperations(it));
            outConnectionForEach(referenceConnection, PsiJavaUtil::getReferences)
                    .asButton(side -> ButtonExtensions.getArrowButton(side, "Add reference"));
            outConnectionForEach(superTypeConnection, PsiJavaUtil::getSuperTypes)
                    .asButton(side -> ButtonExtensions.getTriangleButton(side, "Add superType"));
        }
    };

    private NodeLabelMapping<PsiClass> typeName = new NodeHeadingMapping<PsiClass>(this, CLASS_NAME) {
        @Override
        public String getText(PsiClass it) {
            return it.getName();
        }
    };

    private NodeLabelMapping<PsiClass>  packageName = new NodeLabelMapping<PsiClass>(this, PACKAGE) {
        @Override
        public String getText(PsiClass it) {
            String fqn = it.getQualifiedName();
            final int lastIndexOf = fqn.lastIndexOf(".");
            if (lastIndexOf != -1) {
                return fqn.substring(0, lastIndexOf);
            } else {
                return "<default>";
            }
        }
    };

    private NodeLabelMapping<PsiClass> fileName = new NodeLabelMapping<PsiClass>(this, FILE_NAME) {
        @Override
        public String getText(PsiClass it) {
            return it.getContainingFile().getName();
        }
    };

    private NodeLabelMapping<PsiField> attribute = new NodeLabelMapping<PsiField>(this, ATTRIBUTE) {
        @Override
        public String getText(PsiField it) {
            return it.getName() + ": " + it.getType().getPresentableText();
        }
    };

    private NodeLabelMapping<PsiMethod> operation = new NodeLabelMapping<PsiMethod>(this, OPERATION) {
        @Override
        public String getText(PsiMethod it) {
            return it.getName() + "() : " + it.getReturnType().getPresentableText();
        }
    };

    private ConnectionMapping<PsiField> referenceConnection = new ConnectionMapping<PsiField>(this, "fieldConnection", "Reference") {
        @Override
        protected void calls() {
            labelFor(referenceName, it -> it);
            target(classNode, psiField -> ((PsiClassType) psiField.getType()).resolve());
        }
    };

    private ConnectionLabelMapping<PsiField> referenceName = new ConnectionLabelMapping<PsiField>(this, "referenceName") {
        @Override
        public String getText(PsiField it) {
            return it.getName();
        }
    };

    private ConnectionMapping<PsiClassType> superTypeConnection = new ConnectionMapping<PsiClassType>(this, "superTypeConnection", "Super type") {
        @Override
        protected void calls() {
            target(classNode, psiType -> psiType.resolve());
        }

        @Override
        public XConnection createConnection(IMappedElementDescriptor<PsiClassType> descriptor) {
            XConnection connection = super.createConnection(descriptor);
            connection.setTargetArrowHead(new TriangleArrowHead(connection, 10, 15, null, Color.WHITE, false));
            return connection;
        }
    };

    @Override
    protected <ARG> void entryCalls(ARG arg, MappingAcceptor<ARG> mappingAcceptor) {
        if(arg instanceof PsiElement) {
            PsiClass psiClass = PsiTreeUtil.getParentOfType((PsiElement)arg, PsiClass.class, false);
            if(psiClass != null)
                mappingAcceptor.add(classNode, e -> PsiTreeUtil.getParentOfType((PsiElement)e, PsiClass.class, false));
        }
    }

    @Override
    public String getID() {
        return "ideaClassDiagram";
    }

    @Override
    public String getLabel() {
        return "Class diagram";
    }
}
