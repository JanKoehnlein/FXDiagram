package de.fxdiagram.eclipse.commands;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.eclipse.FXDiagramView;
import de.fxdiagram.eclipse.selection.ISelectionExtractor;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.EntryCall;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ShowInDiagramContribution extends CompoundContributionItem {
  @Override
  protected IContributionItem[] getContributionItems() {
    ArrayList<ContributionItem> _xblockexpression = null;
    {
      IWorkbench _workbench = null;
      if (PlatformUI.class!=null) {
        _workbench=PlatformUI.getWorkbench();
      }
      IWorkbenchWindow _activeWorkbenchWindow = null;
      if (_workbench!=null) {
        _activeWorkbenchWindow=_workbench.getActiveWorkbenchWindow();
      }
      IWorkbenchPage _activePage = null;
      if (_activeWorkbenchWindow!=null) {
        _activePage=_activeWorkbenchWindow.getActivePage();
      }
      final IWorkbenchPage page = _activePage;
      IWorkbenchPart _activePart = null;
      if (page!=null) {
        _activePart=page.getActivePart();
      }
      final IWorkbenchPart activePart = _activePart;
      final ArrayList<ContributionItem> contributionItems = CollectionLiterals.<ContributionItem>newArrayList();
      final ISelectionExtractor.Acceptor acceptor = new ISelectionExtractor.Acceptor() {
        @Override
        public boolean accept(final Object selectedElement) {
          Iterable<? extends ContributionItem> _addMenuItemsForEntryCalls = ShowInDiagramContribution.this.addMenuItemsForEntryCalls(selectedElement, activePart);
          return Iterables.<ContributionItem>addAll(contributionItems, _addMenuItemsForEntryCalls);
        }
      };
      ISelectionExtractor.Registry _instance = ISelectionExtractor.Registry.getInstance();
      Iterable<ISelectionExtractor> _selectionExtractors = _instance.getSelectionExtractors();
      final Consumer<ISelectionExtractor> _function = (ISelectionExtractor it) -> {
        it.addSelectedElement(activePart, acceptor);
      };
      _selectionExtractors.forEach(_function);
      _xblockexpression = contributionItems;
    }
    return ((IContributionItem[])Conversions.unwrapArray(_xblockexpression, IContributionItem.class));
  }
  
  public Iterable<? extends ContributionItem> addMenuItemsForEntryCalls(final Object selectedElement, final IWorkbenchPart activePart) {
    boolean _notEquals = (!Objects.equal(selectedElement, null));
    if (_notEquals) {
      XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
      Iterable<? extends XDiagramConfig> _configurations = _instance.getConfigurations();
      final Function1<XDiagramConfig, Iterable<? extends EntryCall<Object>>> _function = (XDiagramConfig it) -> {
        return it.<Object>getEntryCalls(selectedElement);
      };
      Iterable<Iterable<? extends EntryCall<Object>>> _map = IterableExtensions.map(_configurations, _function);
      final Iterable<EntryCall<Object>> entryCalls = Iterables.<EntryCall<Object>>concat(_map);
      boolean _isEmpty = IterableExtensions.isEmpty(entryCalls);
      boolean _not = (!_isEmpty);
      if (_not) {
        final Function1<EntryCall<Object>, ContributionItem> _function_1 = (EntryCall<Object> call) -> {
          return new ContributionItem() {
            @Override
            public void fill(final Menu menu, final int index) {
              MenuItem _menuItem = new MenuItem(menu, SWT.CHECK, index);
              final Procedure1<MenuItem> _function = (MenuItem it) -> {
                String _text = call.getText();
                it.setText(_text);
                it.addSelectionListener(new SelectionListener() {
                  @Override
                  public void widgetDefaultSelected(final SelectionEvent e) {
                  }
                  
                  @Override
                  public void widgetSelected(final SelectionEvent e) {
                    try {
                      IWorkbenchPartSite _site = activePart.getSite();
                      IWorkbenchPage _page = _site.getPage();
                      final IViewPart view = _page.showView("de.fxdiagram.eclipse.FXDiagramView");
                      if ((view instanceof FXDiagramView)) {
                        IEditorPart _xifexpression = null;
                        if ((activePart instanceof IEditorPart)) {
                          _xifexpression = ((IEditorPart)activePart);
                        } else {
                          _xifexpression = null;
                        }
                        final IEditorPart editor = _xifexpression;
                        ((FXDiagramView)view).<Object>revealElement(selectedElement, call, editor);
                      }
                    } catch (Throwable _e) {
                      throw Exceptions.sneakyThrow(_e);
                    }
                  }
                });
              };
              ObjectExtensions.<MenuItem>operator_doubleArrow(_menuItem, _function);
            }
          };
        };
        return IterableExtensions.<EntryCall<Object>, ContributionItem>map(entryCalls, _function_1);
      }
    }
    return CollectionLiterals.<ContributionItem>emptyList();
  }
}
