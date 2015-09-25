package de.fxdiagram.eclipse.commands

import de.fxdiagram.eclipse.FXDiagramView
import de.fxdiagram.eclipse.selection.ISelectionExtractor
import de.fxdiagram.mapping.XDiagramConfig
import org.eclipse.jface.action.ContributionItem
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.widgets.Menu
import org.eclipse.swt.widgets.MenuItem
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.actions.CompoundContributionItem

class ShowInDiagramContribution extends CompoundContributionItem {

	override protected getContributionItems() {
		val page = PlatformUI?.workbench?.activeWorkbenchWindow?.activePage
		val activePart = page?.activePart
		val contributionItems = newArrayList
		val acceptor = new ISelectionExtractor.Acceptor() {
			override accept(Object selectedElement) {
				contributionItems += addMenuItemsForEntryCalls(selectedElement, activePart)
			}
		}
		ISelectionExtractor.Registry.instance.selectionExtractors.forEach [
			addSelectedElement(activePart, acceptor)
		]
		contributionItems
	}

	def addMenuItemsForEntryCalls(Object selectedElement, IWorkbenchPart activePart) {
		if (selectedElement != null) {
			val entryCalls = XDiagramConfig.Registry.instance.configurations.map [
				getEntryCalls(selectedElement)
			].flatten
			if (!entryCalls.empty) {
				return entryCalls.map [ call |
					new ContributionItem() {
						override fill(Menu menu, int index) {
							new MenuItem(menu, SWT.CHECK, index) => [
								text = call.text
								addSelectionListener(new SelectionListener() {
									override widgetDefaultSelected(SelectionEvent e) {}

									override widgetSelected(SelectionEvent e) {
										val view = activePart.site.page.showView("de.fxdiagram.eclipse.FXDiagramView")
										if (view instanceof FXDiagramView) {
											val editor = if (activePart instanceof IEditorPart)
													activePart
												else
													null
											view.revealElement(selectedElement, call, editor)
										}
									}
								})
							]
						}
					}
				]
			}
		}
		return emptyList
	}
}