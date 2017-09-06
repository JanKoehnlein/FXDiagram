package de.fxdiagram.eclipse.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.model.ModelSave;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.eclipse.actions.FileExtensions;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class EclipseSaveAction implements DiagramAction {
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
    XDiagram _diagram = root.getDiagram();
    boolean _notEquals = (!Objects.equal(_diagram, null));
    if (_notEquals) {
      final Runnable _function = () -> {
        this.doSave(root);
      };
      Display.getDefault().asyncExec(_function);
    }
  }
  
  /**
   * Must be run in Display thread
   */
  public void doSave(final XRoot root) {
    try {
      final IWorkspaceRoot workspaceDir = ResourcesPlugin.getWorkspace().getRoot();
      IFile _xifexpression = null;
      String _fileName = root.getFileName();
      boolean _notEquals = (!Objects.equal(_fileName, null));
      if (_notEquals) {
        String _fileName_1 = root.getFileName();
        Path _path = new Path(_fileName_1);
        _xifexpression = workspaceDir.getFile(_path);
      } else {
        IFile _xblockexpression = null;
        {
          Shell _activeShell = Display.getDefault().getActiveShell();
          final SaveAsDialog saveAsDialog = new SaveAsDialog(_activeShell);
          saveAsDialog.setOriginalName("Diagram.fxd");
          final int result = saveAsDialog.open();
          IFile _xifexpression_1 = null;
          if ((result == SaveAsDialog.OK)) {
            _xifexpression_1 = workspaceDir.getFile(saveAsDialog.getResult());
          } else {
            _xifexpression_1 = null;
          }
          _xblockexpression = _xifexpression_1;
        }
        _xifexpression = _xblockexpression;
      }
      final IFile file = _xifexpression;
      boolean _notEquals_1 = (!Objects.equal(file, null));
      if (_notEquals_1) {
        FileExtensions.createParents(file);
        final StringWriter writer = new StringWriter();
        new ModelSave().save(root, writer);
        byte[] _bytes = writer.toString().getBytes(file.getCharset(true));
        final ByteArrayInputStream stream = new ByteArrayInputStream(_bytes);
        boolean _exists = file.exists();
        if (_exists) {
          NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
          file.setContents(stream, true, true, _nullProgressMonitor);
        } else {
          NullProgressMonitor _nullProgressMonitor_1 = new NullProgressMonitor();
          file.create(stream, true, _nullProgressMonitor_1);
        }
        root.setFileName(file.getFullPath().toOSString());
        root.setNeedsSave(false);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
