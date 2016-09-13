package de.fxdiagram.core.tools.actions;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.Files;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.export.SvgExporter;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.io.File;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class ExportSvgAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    return (it.isShortcutDown() && Objects.equal(it.getCode(), KeyCode.E));
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.CAMERA;
  }
  
  @Override
  public String getTooltip() {
    return "Export to SVG";
  }
  
  @Override
  public void perform(final XRoot root) {
    try {
      final FileChooser fileChooser = new FileChooser();
      ObservableList<FileChooser.ExtensionFilter> _extensionFilters = fileChooser.getExtensionFilters();
      FileChooser.ExtensionFilter _extensionFilter = new FileChooser.ExtensionFilter("FXDiagram", "*.svg");
      _extensionFilters.add(_extensionFilter);
      Scene _scene = root.getScene();
      Window _window = _scene.getWindow();
      final File file = fileChooser.showSaveDialog(_window);
      boolean _notEquals = (!Objects.equal(file, null));
      if (_notEquals) {
        SvgExporter _svgExporter = new SvgExporter();
        XDiagram _diagram = root.getDiagram();
        File _parentFile = file.getParentFile();
        final CharSequence svgCode = _svgExporter.toSvg(_diagram, _parentFile);
        Files.write(svgCode, file, Charsets.UTF_8);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
