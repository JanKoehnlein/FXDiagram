package de.fxdiagram.eclipse;

import de.fxdiagram.eclipse.FXDiagramView;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class DocumentListener implements IDocumentListener {
  private final FXDiagramView view;
  
  private final IEditorPart editor;
  
  @Override
  public void documentAboutToBeChanged(final DocumentEvent event) {
  }
  
  @Override
  public void documentChanged(final DocumentEvent event) {
    this.view.editorChanged(this.editor);
  }
  
  public DocumentListener(final FXDiagramView view, final IEditorPart editor) {
    super();
    this.view = view;
    this.editor = editor;
  }
}
