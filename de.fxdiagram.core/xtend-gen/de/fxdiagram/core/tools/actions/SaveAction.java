package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.model.ModelSave;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class SaveAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    return (it.isShortcutDown() && Objects.equal(it.getCode(), KeyCode.S));
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.SAVE;
  }
  
  @Override
  public String getTooltip() {
    return "Save diagram";
  }
  
  @Override
  public void perform(final XRoot root) {
    try {
      XDiagram _diagram = root.getDiagram();
      boolean _notEquals = (!Objects.equal(_diagram, null));
      if (_notEquals) {
        File _xifexpression = null;
        String _fileName = root.getFileName();
        boolean _notEquals_1 = (!Objects.equal(_fileName, null));
        if (_notEquals_1) {
          String _fileName_1 = root.getFileName();
          _xifexpression = new File(_fileName_1);
        } else {
          File _xblockexpression = null;
          {
            final FileChooser fileChooser = new FileChooser();
            ObservableList<FileChooser.ExtensionFilter> _extensionFilters = fileChooser.getExtensionFilters();
            FileChooser.ExtensionFilter _extensionFilter = new FileChooser.ExtensionFilter("FXDiagram", "*.fxd");
            _extensionFilters.add(_extensionFilter);
            Scene _scene = root.getScene();
            Window _window = _scene.getWindow();
            _xblockexpression = fileChooser.showSaveDialog(_window);
          }
          _xifexpression = _xblockexpression;
        }
        final File file = _xifexpression;
        boolean _notEquals_2 = (!Objects.equal(file, null));
        if (_notEquals_2) {
          File _parentFile = file.getParentFile();
          _parentFile.mkdirs();
          ModelSave _modelSave = new ModelSave();
          FileOutputStream _fileOutputStream = new FileOutputStream(file);
          OutputStreamWriter _outputStreamWriter = new OutputStreamWriter(_fileOutputStream, "UTF-8");
          _modelSave.save(root, _outputStreamWriter);
          String _path = file.getPath();
          root.setFileName(_path);
          root.setNeedsSave(false);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
