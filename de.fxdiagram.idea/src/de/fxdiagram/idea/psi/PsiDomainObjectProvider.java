package de.fxdiagram.idea.psi;

import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiNamedElement;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jan Koehnlein
 */
public class PsiDomainObjectProvider implements IMappedElementDescriptorProvider {
    @Override
    public <T> IMappedElementDescriptor<T> createMappedElementDescriptor(T domainObject, AbstractMapping<? extends T> mapping) {
        if(domainObject instanceof PsiNamedElement) {
            PsiNamedElement psiNamedElement = (PsiNamedElement) domainObject;
            return (IMappedElementDescriptor<T>)
                    new PsiDomainObjectDescriptor(getFQN(psiNamedElement), psiNamedElement, (AbstractMapping<PsiNamedElement>)mapping, this);
        }
        return null;
    }

    @Override
    public <T> DomainObjectDescriptor createDescriptor(T t) {
        return null;
    }

    @Override
    public void populate(ModelElementImpl modelElement) {
    }

    protected List<String> getFQN(PsiNamedElement element) {
        List<String> fqn = new LinkedList<String>();
        PsiNamedElement current = element;
        while(!(current instanceof PsiFile)) {
            fqn.add(0, current.getName());
            if(!(current.getParent() instanceof PsiNamedElement))
                return Collections.emptyList();
            current = (PsiNamedElement) current.getParent();
        }
        return fqn;
    }

    protected PsiNamedElement recover(String path, List<String> fqn) {
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(path);
        PsiFile psiFile = PsiManager.getInstance(ProjectUtil.guessProjectForFile(file)).findFile(file);
        PsiNamedElement current = psiFile;
        for(String name: fqn) {
            current = findNamedChild(current, name);
            if (current == null)
                return null;
        }
        return current;
    }

    private PsiNamedElement findNamedChild(PsiElement element, String name) {
        for(PsiElement child: element.getChildren()) {
            if(child instanceof PsiNamedElement && name.equals(((PsiNamedElement) child).getName())) {
                return (PsiNamedElement) child;
            }
        }
        return null;
    }

}
