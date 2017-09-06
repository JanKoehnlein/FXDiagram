package de.fxdiagram.eclipse.actions;

import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.model.ModelLoad;
import de.fxdiagram.core.tools.actions.LoadAction;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class EclipseLoadAction extends LoadAction {
  @Override
  public void perform(final XRoot root) {
    try {
      final Shell shell = Display.getDefault().getActiveShell();
      final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
      final ResourceListSelectionDialog dialog = new ResourceListSelectionDialog(shell, workspaceRoot, IResource.FILE) {
        @Override
        protected Control createDialogArea(final Composite parent) {
          Control _xblockexpression = null;
          {
            final Control control = super.createDialogArea(parent);
            Text _findTextWidget = EclipseLoadAction.this.findTextWidget(parent);
            if (_findTextWidget!=null) {
              _findTextWidget.setText("*.fxd");
            }
            _xblockexpression = control;
          }
          return _xblockexpression;
        }
      };
      boolean _and = false;
      int _open = dialog.open();
      boolean _equals = (_open == Window.OK);
      if (!_equals) {
        _and = false;
      } else {
        Object[] _result = dialog.getResult();
        int _length = 0;
        if (_result!=null) {
          _length=_result.length;
        }
        boolean _equals_1 = (_length == 1);
        _and = _equals_1;
      }
      if (_and) {
        Object _head = IterableExtensions.<Object>head(((Iterable<Object>)Conversions.doWrapArray(dialog.getResult())));
        final IFile file = ((IFile) _head);
        NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
        file.refreshLocal(IResource.DEPTH_ONE, _nullProgressMonitor);
        boolean _exists = false;
        if (file!=null) {
          _exists=file.exists();
        }
        if (_exists) {
          ModelLoad _modelLoad = new ModelLoad();
          InputStream _contents = file.getContents();
          String _charset = file.getCharset();
          InputStreamReader _inputStreamReader = new InputStreamReader(_contents, _charset);
          final Object node = _modelLoad.load(_inputStreamReader);
          if ((node instanceof XRoot)) {
            root.replaceDomainObjectProviders(((XRoot)node).getDomainObjectProviders());
            root.setRootDiagram(((XRoot)node).getDiagram());
            root.setFileName(file.getFullPath().toOSString());
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private Text findTextWidget(final Composite composite) {
    final Function1<Control, Text> _function = (Control it) -> {
      Text _switchResult = null;
      boolean _matched = false;
      if (it instanceof Text) {
        _matched=true;
        _switchResult = ((Text)it);
      }
      if (!_matched) {
        if (it instanceof Composite) {
          _matched=true;
          _switchResult = this.findTextWidget(((Composite)it));
        }
      }
      if (!_matched) {
        _switchResult = null;
      }
      return _switchResult;
    };
    return IterableExtensions.<Text>head(IterableExtensions.<Text>filterNull(ListExtensions.<Control, Text>map(((List<Control>)Conversions.doWrapArray(composite.getChildren())), _function)));
  }
}
