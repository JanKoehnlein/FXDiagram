package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.model.ModelSave;
import de.fxdiagram.core.tools.actions.DiagramAction;
import java.io.File;
import java.io.FileWriter;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class SaveAction implements DiagramAction {
  public void perform(final XRoot root) {
    try {
      XDiagram _diagram = root.getDiagram();
      boolean _notEquals = (!Objects.equal(_diagram, null));
      if (_notEquals) {
        FileChooser _fileChooser = new FileChooser();
        final FileChooser fileChooser = _fileChooser;
        ObservableList<FileChooser.ExtensionFilter> _extensionFilters = fileChooser.getExtensionFilters();
        FileChooser.ExtensionFilter _extensionFilter = new FileChooser.ExtensionFilter("FX Diagram", "*.fxd");
        _extensionFilters.add(_extensionFilter);
        Scene _scene = root.getScene();
        Window _window = _scene.getWindow();
        final File file = fileChooser.showSaveDialog(_window);
        boolean _notEquals_1 = (!Objects.equal(file, null));
        if (_notEquals_1) {
          ModelSave _modelSave = new ModelSave();
          FileWriter _fileWriter = new FileWriter(file);
          _modelSave.save(root, _fileWriter);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
