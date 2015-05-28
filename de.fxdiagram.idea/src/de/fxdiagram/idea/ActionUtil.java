package de.fxdiagram.idea;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author Jan Koehnlein
 */
public class ActionUtil {

    public static PsiElement getPsiElement(AnActionEvent event) {
        Editor editor = getEditor(event);
        if (editor == null)
            return null;
        final PsiFile psiFile = getPsiFile(event);
        if (psiFile == null)
            return null;
        return psiFile.findElementAt(editor.getCaretModel().getOffset());
    }

    public static Project getProject(AnActionEvent event) {
        return PlatformDataKeys.PROJECT.getData(event.getDataContext());
    }

    public static Editor getEditor(AnActionEvent event) {
        return PlatformDataKeys.EDITOR.getData(event.getDataContext());
    }

    public static PsiFile getPsiFile(AnActionEvent event) {
        return LangDataKeys.PSI_FILE.getData(event.getDataContext());
    }
}
