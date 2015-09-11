package de.fxdiagram.idea.psi;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.XModelProvider;
import de.fxdiagram.mapping.AbstractMappedElementDescriptor;
import de.fxdiagram.mapping.AbstractMapping;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.lib.Functions;

import javax.swing.*;
import java.util.List;

/**
 * @author Jan Koehnlein
 */
@ModelNode({"fqn","path"})
public class PsiDomainObjectDescriptor<T extends PsiNamedElement> extends AbstractMappedElementDescriptor<T> implements XModelProvider {

    private ReadOnlyListWrapper<String> fqnProperty = new ReadOnlyListWrapper<String>(this, "fqn", _initFqn());
    private ReadOnlyStringWrapper pathProperty = new ReadOnlyStringWrapper(this, "path", _initPath());

    private T psiElement;

    public PsiDomainObjectDescriptor() {
    }

    public PsiDomainObjectDescriptor(List<String> fqn, T psiElement, AbstractMapping<PsiNamedElement> mapping, PsiDomainObjectProvider provider) {
        super(mapping.getConfig().getID(), mapping.getID(), provider);
        this.fqnProperty.setAll(fqn);
        this.pathProperty.set(psiElement.getContainingFile().getVirtualFile().getCanonicalPath());
        this.psiElement = psiElement;
    }

    @Override
    public String getName() {
        return getPsiElement().getName();
    }

    @Override
    public <U> U withDomainObject(Functions.Function1<? super T, ? extends U> function1) {
        return ApplicationManager.getApplication().runReadAction(new Computable<U>() {
            @Override
            public U compute() {
                return function1.apply(getPsiElement());
            }
        });
    }

    @Override
    public Object openInEditor(boolean isSelect) {
        PsiFile psiFile = getPsiElement().getContainingFile();
        if (psiFile != null) {
            VirtualFile file = psiFile.getVirtualFile();
            if (file != null) {
                Project project = ProjectUtil.guessProjectForFile(file);
                if (project != null) {
                    int offset = getPsiElement() instanceof PsiFile ? -1 : getPsiElement().getTextOffset();
                    OpenFileDescriptor fileDesc = new OpenFileDescriptor(project, file, offset);
                    SwingUtilities.invokeLater(
                            () -> FileEditorManager.getInstance(project).openEditor(fileDesc, isSelect)
                    );
                }
            }
        }
        return null;
    }

    protected T getPsiElement() {
        if (psiElement == null)
            psiElement = (T) ((PsiDomainObjectProvider) providerProperty().get())
                    .recover(pathProperty.get(), fqnProperty.get());
        return psiElement;

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PsiDomainObjectDescriptor<?> &&
                ((PsiDomainObjectDescriptor<?>) obj).getPsiElement().isEquivalentTo(psiElement);
    }

    @Override
    public int hashCode() {
        return getPsiElement().hashCode();
    }

    @Override
    public void populate(final ModelElementImpl modelElement) {
        super.populate(modelElement);
        modelElement.addProperty(fqnProperty, String.class);
        modelElement.addProperty(pathProperty, String.class);
    }


    private static final ObservableList<String> _initFqn() {
        ObservableList<String> _observableArrayList = FXCollections.<String>observableArrayList();
        return _observableArrayList;
    }

    public ObservableList<String> getFqn() {
        return this.fqnProperty.get();
    }

    public ReadOnlyListProperty<String> fqnProperty() {
        return this.fqnProperty.getReadOnlyProperty();
    }

    private static final String _initPath() {
        return "";
    }

    public String getPath() {
        return this.pathProperty.get();
    }

    public ReadOnlyStringProperty pathProperty() {
        return this.pathProperty.getReadOnlyProperty();
    }

}
