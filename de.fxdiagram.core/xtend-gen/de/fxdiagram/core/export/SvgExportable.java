package de.fxdiagram.core.export;

import de.fxdiagram.core.export.SvgExporter;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public interface SvgExportable {
  public abstract CharSequence toSvgElement(@Extension final SvgExporter exporter);
}
