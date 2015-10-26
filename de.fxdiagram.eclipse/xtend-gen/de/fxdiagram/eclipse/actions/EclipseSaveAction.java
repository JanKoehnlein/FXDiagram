package de.fxdiagram.eclipse.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.model.ModelSave;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.eclipse.actions.FileExtensions;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.eclipse.core.resources.IFile;
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
          final IWorkspaceRoot workspaceDir = _workspace.getRoot();
          IFile _xifexpression = null;
          String _fileName = root.getFileName();
          boolean _notEquals_1 = (!Objects.equal(_fileName, null));
          if (_notEquals_1) {
            String _fileName_1 = root.getFileName();
            Path _path = new Path(_fileName_1);
            _xifexpression = workspaceDir.getFile(_path);
          } else {
            IFile _xblockexpression = null;
            {
              IPath _location = workspaceDir.getLocation();
              final File workspaceJavaFile = _location.toFile();
              FileChooser _fileChooser = new FileChooser();
              final Procedure1<FileChooser> _function_1 = (FileChooser it) -> {
                ObservableList<FileChooser.ExtensionFilter> _extensionFilters = it.getExtensionFilters();
                FileChooser.ExtensionFilter _extensionFilter = new FileChooser.ExtensionFilter("FXDiagram", "*.fxd");
                _extensionFilters.add(_extensionFilter);
                it.setInitialDirectory(workspaceJavaFile);
              };
              final FileChooser fileChooser = ObjectExtensions.<FileChooser>operator_doubleArrow(_fileChooser, _function_1);
              Scene _scene = root.getScene();
              Window _window = _scene.getWindow();
              File _showSaveDialog = fileChooser.showSaveDialog(_window);
              _xblockexpression = FileExtensions.toWorkspaceFile(_showSaveDialog);
            }
            _xifexpression = _xblockexpression;
          }
          final IFile file = _xifexpression;
          boolean _notEquals_2 = (!Objects.equal(file, null));
          if (_notEquals_2) {
            FileExtensions.createParents(file);
            final StringWriter writer = new StringWriter();
            ModelSave _modelSave = new ModelSave();
            _modelSave.save(root, writer);
            String _string = writer.toString();
            String _charset = file.getCharset(true);
            byte[] _bytes = _string.getBytes(_charset);
            ByteArrayInputStream _byteArrayInputStream = new ByteArrayInputStream(_bytes);
            NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
            file.create(_byteArrayInputStream, true, _nullProgressMonitor);
            IPath _fullPath = file.getFullPath();
            String _oSString = _fullPath.toOSString();
            root.setFileName(_oSString);
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
