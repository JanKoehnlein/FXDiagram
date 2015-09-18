package de.fxdiagram.idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeEvent;
import de.fxdiagram.core.XRoot;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;

/**
 * @author Jan Koehnlein
 */
public class FXDiagramToolWindowFactory implements ToolWindowFactory {

    public static final String ID = "FXDiagram";

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        final JFXPanel panel = new JFXPanel();
        toolWindow.getComponent().add(panel);
        Platform.runLater(() -> initFX(panel, project));
    }

    private void initFX(JFXPanel fxPanel, Project project) {
        Scene scene = createScene(project);
        fxPanel.setScene(scene);
    }

    private Scene createScene(Project project) {
        FXDiagramPane fxDiagramPane = FXDiagramPane.getInstance(project);
        XRoot root = fxDiagramPane.createRoot();
        Scene scene = new Scene(root);
        scene.setCamera(new PerspectiveCamera());
        root.activate();
        PsiManager.getInstance(project).addPsiTreeChangeListener(new PsiTreeChangeAdapter() {
            @Override
            public void childAdded(@NotNull PsiTreeChangeEvent event) {
                fxDiagramPane.refreshUpdateState();
            }

            @Override
            public void childRemoved(@NotNull PsiTreeChangeEvent event) {
                fxDiagramPane.refreshUpdateState();
            }

            @Override
            public void childReplaced(@NotNull PsiTreeChangeEvent event) {
                fxDiagramPane.refreshUpdateState();
            }

            @Override
            public void childMoved(@NotNull PsiTreeChangeEvent event) {
                fxDiagramPane.refreshUpdateState();
            }

            @Override
            public void childrenChanged(@NotNull PsiTreeChangeEvent event) {
                fxDiagramPane.refreshUpdateState();
            }

            @Override
            public void propertyChanged(@NotNull PsiTreeChangeEvent event) {
                fxDiagramPane.refreshUpdateState();
            }
        });
        return scene;
    }
}
