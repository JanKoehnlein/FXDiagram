package de.fxdiagram.core.tools.actions;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.export.SvgExporter;
import de.fxdiagram.core.tools.actions.DiagramAction;
import java.io.File;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class ExportSvgAction implements DiagramAction {
  public void perform(final XRoot root) {
    try {
      SvgExporter _svgExporter = new SvgExporter();
      XDiagram _diagram = root.getDiagram();
      final CharSequence svgCode = _svgExporter.toSvg(_diagram);
      File _file = new File("Diagram.svg");
      Files.write(svgCode, _file, Charsets.UTF_8);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
