package de.itemis.javafx.diagram.tools;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.Files;
import de.itemis.javafx.diagram.XRootDiagram;
import de.itemis.javafx.diagram.export.SvgExporter;
import de.itemis.javafx.diagram.tools.XDiagramTool;
import java.io.File;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class KeyTool implements XDiagramTool {
  private XRootDiagram diagram;
  
  private EventHandler<KeyEvent> keyHandler;
  
  public KeyTool(final XRootDiagram diagram) {
    this.diagram = diagram;
    final EventHandler<KeyEvent> _function = new EventHandler<KeyEvent>() {
        public void handle(final KeyEvent it) {
          try {
            KeyCode _code = it.getCode();
            final KeyCode getCode = _code;
            boolean _matched = false;
            if (!_matched) {
              if (Objects.equal(getCode,KeyCode.E)) {
                _matched=true;
                boolean _isShortcutDown = it.isShortcutDown();
                if (_isShortcutDown) {
                  SvgExporter _svgExporter = new SvgExporter();
                  final CharSequence svgCode = _svgExporter.toSvg(diagram);
                  File _file = new File("Diagram.svg");
                  Files.write(svgCode, _file, Charsets.UTF_8);
                  it.consume();
                }
              }
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
    this.keyHandler = _function;
  }
  
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      Scene _scene = this.diagram.getScene();
      _scene.<KeyEvent>addEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
  
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      Scene _scene = this.diagram.getScene();
      _scene.<KeyEvent>removeEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
}
