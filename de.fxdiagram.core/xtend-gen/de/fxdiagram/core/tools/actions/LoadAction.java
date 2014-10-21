package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelLoad;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.Symbol;
import java.io.File;
import java.io.FileReader;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class LoadAction implements DiagramAction {
  public boolean matches(final KeyEvent it) {
    boolean _and = false;
    boolean _isShortcutDown = it.isShortcutDown();
    if (!_isShortcutDown) {
      _and = false;
    } else {
      KeyCode _code = it.getCode();
      boolean _equals = Objects.equal(_code, KeyCode.O);
      _and = _equals;
    }
    return _and;
  }
  
  public Symbol.Type getSymbol() {
    return Symbol.Type.CLOUD;
  }
  
  public String getTooltip() {
    return "Load diagram";
  }
  
  public void perform(final XRoot root) {
    try {
      final FileChooser fileChooser = new FileChooser();
      ObservableList<FileChooser.ExtensionFilter> _extensionFilters = fileChooser.getExtensionFilters();
      FileChooser.ExtensionFilter _extensionFilter = new FileChooser.ExtensionFilter("FX Diagram", "*.fxd");
      _extensionFilters.add(_extensionFilter);
      Scene _scene = root.getScene();
      Window _window = _scene.getWindow();
      final File file = fileChooser.showOpenDialog(_window);
      boolean _notEquals = (!Objects.equal(file, null));
      if (_notEquals) {
        ModelLoad _modelLoad = new ModelLoad();
        FileReader _fileReader = new FileReader(file);
        final Object node = _modelLoad.load(_fileReader);
        if ((node instanceof XRoot)) {
          ObservableList<DomainObjectProvider> _domainObjectProviders = ((XRoot)node).getDomainObjectProviders();
          root.replaceDomainObjectProviders(_domainObjectProviders);
          XDiagram _diagram = ((XRoot)node).getDiagram();
          root.setDiagram(_diagram);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
