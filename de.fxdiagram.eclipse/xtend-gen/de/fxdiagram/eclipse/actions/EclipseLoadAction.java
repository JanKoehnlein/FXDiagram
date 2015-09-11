package de.fxdiagram.eclipse.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelLoad;
import de.fxdiagram.core.tools.actions.LoadAction;
import de.fxdiagram.eclipse.FXDiagramView;
import java.io.File;
import java.io.FileReader;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class EclipseLoadAction extends LoadAction {
  @Override
  public void perform(final XRoot root) {
    try {
      final FileChooser fileChooser = new FileChooser();
      ObservableList<FileChooser.ExtensionFilter> _extensionFilters = fileChooser.getExtensionFilters();
      FileChooser.ExtensionFilter _extensionFilter = new FileChooser.ExtensionFilter("FXDiagram", "*.fxd");
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
          root.setRootDiagram(_diagram);
          IWorkbench _workbench = PlatformUI.getWorkbench();
          IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
          IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
          IViewPart _findView = _activePage.findView("de.fxdiagram.eclipse.FXDiagramView");
          final FXDiagramView diagramView = ((FXDiagramView) _findView);
          boolean _isLinkWithEditor = diagramView.isLinkWithEditor();
          diagramView.setLinkWithEditor(_isLinkWithEditor);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
