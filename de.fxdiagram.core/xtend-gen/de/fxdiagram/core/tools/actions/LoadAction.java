package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.model.ModelLoad;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class LoadAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    return (it.isShortcutDown() && Objects.equal(it.getCode(), KeyCode.O));
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.LOAD;
  }
  
  @Override
  public String getTooltip() {
    return "Load diagram";
  }
  
  @Override
  public void perform(final XRoot root) {
    try {
      final FileChooser fileChooser = new FileChooser();
      ObservableList<FileChooser.ExtensionFilter> _extensionFilters = fileChooser.getExtensionFilters();
      FileChooser.ExtensionFilter _extensionFilter = new FileChooser.ExtensionFilter("FXDiagram", "*.fxd");
      _extensionFilters.add(_extensionFilter);
      final File file = fileChooser.showOpenDialog(root.getScene().getWindow());
      boolean _notEquals = (!Objects.equal(file, null));
      if (_notEquals) {
        ModelLoad _modelLoad = new ModelLoad();
        FileInputStream _fileInputStream = new FileInputStream(file);
        InputStreamReader _inputStreamReader = new InputStreamReader(_fileInputStream, "UTF-8");
        final Object node = _modelLoad.load(_inputStreamReader);
        if ((node instanceof XRoot)) {
          root.replaceDomainObjectProviders(((XRoot)node).getDomainObjectProviders());
          root.setRootDiagram(((XRoot)node).getDiagram());
          root.setFileName(file.getPath());
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
