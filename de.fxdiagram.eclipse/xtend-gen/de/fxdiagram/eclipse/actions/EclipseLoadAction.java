package de.fxdiagram.eclipse.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelLoad;
import de.fxdiagram.core.tools.actions.LoadAction;
import de.fxdiagram.eclipse.actions.FileExtensions;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class EclipseLoadAction extends LoadAction {
  @Override
  public void perform(final XRoot root) {
    try {
      IWorkspace _workspace = ResourcesPlugin.getWorkspace();
      IWorkspaceRoot _root = _workspace.getRoot();
      IPath _location = _root.getLocation();
      final File workspaceDir = _location.toFile();
      FileChooser _fileChooser = new FileChooser();
      final Procedure1<FileChooser> _function = (FileChooser it) -> {
        it.setTitle("Load diagram");
        ObservableList<FileChooser.ExtensionFilter> _extensionFilters = it.getExtensionFilters();
        FileChooser.ExtensionFilter _extensionFilter = new FileChooser.ExtensionFilter("FXDiagram", "*.fxd");
        _extensionFilters.add(_extensionFilter);
        it.setInitialDirectory(workspaceDir);
      };
      final FileChooser fileChooser = ObjectExtensions.<FileChooser>operator_doubleArrow(_fileChooser, _function);
      Scene _scene = root.getScene();
      Window _window = _scene.getWindow();
      File _showOpenDialog = fileChooser.showOpenDialog(_window);
      final IFile file = FileExtensions.toWorkspaceFile(_showOpenDialog);
      boolean _notEquals = (!Objects.equal(file, null));
      if (_notEquals) {
        ModelLoad _modelLoad = new ModelLoad();
        InputStream _contents = file.getContents();
        InputStreamReader _inputStreamReader = new InputStreamReader(_contents);
        final Object node = _modelLoad.load(_inputStreamReader);
        if ((node instanceof XRoot)) {
          ObservableList<DomainObjectProvider> _domainObjectProviders = ((XRoot)node).getDomainObjectProviders();
          root.replaceDomainObjectProviders(_domainObjectProviders);
          XDiagram _diagram = ((XRoot)node).getDiagram();
          root.setRootDiagram(_diagram);
          IPath _fullPath = file.getFullPath();
          String _oSString = _fullPath.toOSString();
          root.setFileName(_oSString);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
