package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
import de.fxdiagram.eclipse.selection.ISelectionExtractor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
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
      ISelectionProvider _selectionProvider = ((XtextEditor)activePart).getSelectionProvider();
      ISelection _selection = _selectionProvider.getSelection();
      final ITextSelection selection = ((ITextSelection) _selection);
      IXtextDocument _document = ((XtextEditor)activePart).getDocument();
      final IUnitOfWork<Boolean, XtextResource> _function = (XtextResource it) -> {
        try {
          IResourceServiceProvider _resourceServiceProvider = it.getResourceServiceProvider();
          final IGrammarAccess grammarAccess = _resourceServiceProvider.<IGrammarAccess>get(IGrammarAccess.class);
          Grammar _grammar = grammarAccess.getGrammar();
          boolean _usesXbase = this.usesXbase(_grammar);
          if (_usesXbase) {
            IResourceServiceProvider _resourceServiceProvider_1 = it.getResourceServiceProvider();
            final IJvmModelAssociations associations = _resourceServiceProvider_1.<IJvmModelAssociations>get(IJvmModelAssociations.class);
            IResourceServiceProvider _resourceServiceProvider_2 = it.getResourceServiceProvider();
            final EObjectAtOffsetHelper eObjectAtOffsetHelper = _resourceServiceProvider_2.<EObjectAtOffsetHelper>get(EObjectAtOffsetHelper.class);
            int _offset = selection.getOffset();
            final EObject selectedElement = eObjectAtOffsetHelper.resolveElementAt(it, _offset);
            boolean _notEquals = (!Objects.equal(selectedElement, null));
            if (_notEquals) {
              final EObject primary = associations.getPrimaryJvmElement(selectedElement);
              boolean _notEquals_1 = (!Objects.equal(primary, null));
              if (_notEquals_1) {
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
      return (_document.<Boolean>readOnly(_function)).booleanValue();
    }
    return false;
  }
  
  protected boolean usesXbase(final Grammar it) {
    return (Objects.equal(it.getName(), "org.eclipse.xtext.xbase.Xbase") || IterableExtensions.<Grammar>exists(it.getUsedGrammars(), ((Function1<Grammar, Boolean>) (Grammar it_1) -> {
      return Boolean.valueOf(this.usesXbase(it_1));
    })));
  }
}
