package de.fxdiagram.idea;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiElement;
import de.fxdiagram.mapping.MappingCall;
import javafx.application.Platform;

import static de.fxdiagram.idea.ActionUtil.getProject;

/**
 * @author Jan Koehnlein
 */
public class ShowInFXDiagramAction extends AnAction {

    private MappingCall<?, PsiElement> mappingCall;

    private PsiElement psiElement;

    public ShowInFXDiagramAction() {
    }

    public ShowInFXDiagramAction(MappingCall<?, PsiElement> mappingCall, PsiElement psiElement) {
        super(mappingCall.getMapping().getDisplayName() + " (" + mappingCall.getMapping().getConfig().getLabel() + ")");
        this.mappingCall = mappingCall;
        this.psiElement = psiElement;
    }

    @Override
    public void actionPerformed(final AnActionEvent event) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(getProject(event)).getToolWindow(FXDiagramToolWindowFactory.ID);
        toolWindow.activate(()-> {
            Platform.runLater(() ->
                ApplicationManager.getApplication().runReadAction(new Runnable() {
                    @Override
                    public void run() {
                        FXDiagramPane.getInstance(getProject(event)).revealElement(psiElement, mappingCall);
                    }
                }));
        });
    }
}
