package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelLoad;
import de.fxdiagram.core.tools.actions.DiagramAction;
import java.io.File;
import java.io.FileReader;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class LoadAction implements DiagramAction {
  public void perform(final XRoot root) {
    try {
      FileChooser _fileChooser = new FileChooser();
      final FileChooser fileChooser = _fileChooser;
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
          ObservableList<DomainObjectProvider> _domainObjectProviders = root.getDomainObjectProviders();
          ObservableList<DomainObjectProvider> _domainObjectProviders_1 = ((XRoot)node).getDomainObjectProviders();
          _domainObjectProviders.setAll(_domainObjectProviders_1);
          XDiagram _diagram = ((XRoot)node).getDiagram();
          root.setDiagram(_diagram);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
