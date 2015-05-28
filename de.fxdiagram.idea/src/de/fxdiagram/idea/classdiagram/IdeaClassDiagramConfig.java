package de.fxdiagram.idea.classdiagram;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.util.PsiTreeUtil;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.idea.psi.AbstractPsiDiagramConfig;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.MappingAcceptor;
import de.fxdiagram.mapping.NodeMapping;
import javafx.scene.paint.Color;

/**
 * @author Jan Koehnlein
 */
public class IdeaClassDiagramConfig extends AbstractPsiDiagramConfig {

    private NodeMapping<PsiClass> classNode = new NodeMapping<PsiClass>(this, "classNode", "Class Node") {
        @Override
        protected void calls() {
            outConnectionForEach(referenceConnection, PsiJavaUtil::getReferences)
                    .asButton(side -> ButtonExtensions.getArrowButton(side, "Add reference"));
            outConnectionForEach(superTypeConnection, PsiJavaUtil::getSuperTypes)
                    .asButton(side -> ButtonExtensions.getTriangleButton(side, "Add superType"));
        }

        @Override
        public XNode createNode(IMappedElementDescriptor<PsiClass> descriptor) {
            return new PsiClassNode(descriptor);
        }
    };

    private ConnectionMapping<PsiField> referenceConnection = new ConnectionMapping<PsiField>(this, "fieldConnection", "Reference") {
        @Override
        protected void calls() {
            target(classNode, psiField -> ((PsiClassType) psiField.getType()).resolve());
        }

        @Override
        public XConnection createConnection(IMappedElementDescriptor<PsiField> descriptor) {
            XConnection connection = super.createConnection(descriptor);
            XConnectionLabel label = new XConnectionLabel(connection);
            label.getText().setText(descriptor.getName());
            return connection;
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
            PsiClass psiClass = PsiTreeUtil.getParentOfType((PsiElement)arg, PsiClass.class);
            if(psiClass != null)
                mappingAcceptor.add(classNode, e -> PsiTreeUtil.getParentOfType((PsiElement)e, PsiClass.class));
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
