package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
import de.fxdiagram.eclipse.selection.ISelectionExtractor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class JvmAssociationSelectionExtractor implements ISelectionExtractor {
  @Override
  public boolean addSelectedElement(final IWorkbenchPart activePart, final ISelectionExtractor.Acceptor acceptor) {
    if ((activePart instanceof XtextEditor)) {
      ISelection _selection = ((XtextEditor)activePart).getSelectionProvider().getSelection();
      final ITextSelection selection = ((ITextSelection) _selection);
      final IUnitOfWork<Boolean, XtextResource> _function = (XtextResource it) -> {
        try {
          final IGrammarAccess grammarAccess = it.getResourceServiceProvider().<IGrammarAccess>get(IGrammarAccess.class);
          boolean _usesXbase = this.usesXbase(grammarAccess.getGrammar());
          if (_usesXbase) {
            final IJvmModelAssociations associations = it.getResourceServiceProvider().<IJvmModelAssociations>get(IJvmModelAssociations.class);
            final EObjectAtOffsetHelper eObjectAtOffsetHelper = it.getResourceServiceProvider().<EObjectAtOffsetHelper>get(EObjectAtOffsetHelper.class);
            final EObject selectedElement = eObjectAtOffsetHelper.resolveElementAt(it, selection.getOffset());
            if ((selectedElement != null)) {
              final EObject primary = associations.getPrimaryJvmElement(selectedElement);
              if ((primary != null)) {
                return Boolean.valueOf(acceptor.accept(primary));
              }
            }
          }
        } catch (final Throwable _t) {
          if (_t instanceof Exception) {
            final Exception exc = (Exception)_t;
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
        return Boolean.valueOf(false);
      };
      return (((XtextEditor)activePart).getDocument().<Boolean>readOnly(_function)).booleanValue();
    }
    return false;
  }
  
  protected boolean usesXbase(final Grammar it) {
    return (Objects.equal(it.getName(), "org.eclipse.xtext.xbase.Xbase") || IterableExtensions.<Grammar>exists(it.getUsedGrammars(), ((Function1<Grammar, Boolean>) (Grammar it_1) -> {
      return Boolean.valueOf(this.usesXbase(it_1));
    })));
  }
}
