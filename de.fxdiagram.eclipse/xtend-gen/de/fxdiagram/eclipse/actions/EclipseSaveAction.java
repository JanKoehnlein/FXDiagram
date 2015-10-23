package de.fxdiagram.eclipse.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.model.ModelSave;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.io.File;
import java.io.FileWriter;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class EclipseSaveAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    boolean _and = false;
    boolean _isShortcutDown = it.isShortcutDown();
    if (!_isShortcutDown) {
      _and = false;
    } else {
      KeyCode _code = it.getCode();
      boolean _equals = Objects.equal(_code, KeyCode.S);
      _and = _equals;
    }
    return _and;
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
    XDiagram _diagram = root.getDiagram();
    boolean _notEquals = (!Objects.equal(_diagram, null));
    if (_notEquals) {
      Display _default = Display.getDefault();
      final Runnable _function = () -> {
        try {
          IWorkspace _workspace = ResourcesPlugin.getWorkspace();
          IWorkspaceRoot _root = _workspace.getRoot();
          IPath _location = _root.getLocation();
          final File workspaceDir = _location.toFile();
          File _xifexpression = null;
          String _fileName = root.getFileName();
          boolean _notEquals_1 = (!Objects.equal(_fileName, null));
          if (_notEquals_1) {
            String _fileName_1 = root.getFileName();
            _xifexpression = new File(workspaceDir, _fileName_1);
          } else {
            File _xblockexpression = null;
            {
              FileChooser _fileChooser = new FileChooser();
              final Procedure1<FileChooser> _function_1 = (FileChooser it) -> {
                ObservableList<FileChooser.ExtensionFilter> _extensionFilters = it.getExtensionFilters();
                FileChooser.ExtensionFilter _extensionFilter = new FileChooser.ExtensionFilter("FXDiagram", "*.fxd");
                _extensionFilters.add(_extensionFilter);
                it.setInitialDirectory(workspaceDir);
              };
              final FileChooser fileChooser = ObjectExtensions.<FileChooser>operator_doubleArrow(_fileChooser, _function_1);
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
            FileWriter _fileWriter = new FileWriter(file);
            _modelSave.save(root, _fileWriter);
            final String fileName = file.getAbsolutePath();
            final String workspaceDirName = workspaceDir.getAbsolutePath();
            boolean _startsWith = fileName.startsWith((workspaceDirName + File.separator));
            if (_startsWith) {
              int _length = workspaceDirName.length();
              String _substring = fileName.substring(_length);
              root.setFileName(_substring);
              IWorkspace _workspace_1 = ResourcesPlugin.getWorkspace();
              IWorkspaceRoot _root_1 = _workspace_1.getRoot();
              String _fileName_2 = root.getFileName();
              Path _path = new Path(_fileName_2);
              final IFile iFile = _root_1.getFile(_path);
              NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
              iFile.refreshLocal(IResource.DEPTH_ONE, _nullProgressMonitor);
            } else {
              root.setFileName(fileName);
            }
            root.setNeedsSave(false);
          }
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      };
      _default.asyncExec(_function);
    }
  }
}
