package de.fxdiagram.idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import de.fxdiagram.core.XRoot;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;

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
        XRoot root = FXDiagramPane.getInstance(project).createRoot();
        Scene scene = new Scene(root);
        scene.setCamera(new PerspectiveCamera());
        root.activate();
        return scene;
    }
}
