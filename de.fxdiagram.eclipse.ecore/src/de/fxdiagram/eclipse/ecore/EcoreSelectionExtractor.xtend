package de.fxdiagram.eclipse.ecore

import de.fxdiagram.eclipse.changes.IChangeSource
import de.fxdiagram.eclipse.changes.ModelChangeBroker
import de.fxdiagram.eclipse.selection.ISelectionExtractor
import org.eclipse.emf.common.notify.Notification
import org.eclipse.emf.common.notify.impl.AdapterImpl
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.emf.edit.domain.IEditingDomainProvider
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

class EcoreSelectionExtractor implements ISelectionExtractor, IChangeSource {
	
	override addSelectedElement(IWorkbenchPart activePart, Acceptor acceptor) {
		if(activePart instanceof IEditingDomainProvider) {
			val selection = activePart?.site?.selectionProvider?.selection
			if (selection instanceof IStructuredSelection) 
				acceptor.accept(selection.firstElement)
		}
	}
	
	override addChangeListener(IWorkbenchPart part, ModelChangeBroker broker) {
		if(part instanceof IEditingDomainProvider) 
			part.editingDomain.resourceSet.eAdapters.add(new FXDiagramAdapter(part, broker))
	}
	
	override removeChangeListener(IWorkbenchPart part, ModelChangeBroker broker) {
		if(part instanceof IEditingDomainProvider) {
			val adapters = part.editingDomain.resourceSet.eAdapters
			val adapter = EcoreUtil.getAdapter(adapters, FXDiagramAdapter)
			adapters.remove(adapter)
		}
	}
	
	@FinalFieldsConstructor
	static class FXDiagramAdapter extends AdapterImpl {

		val IWorkbenchPart part
		val ModelChangeBroker broker
		
		override notifyChanged(Notification msg) {
			broker.partChanged(part)
		}
		
		override isAdapterForType(Object type) {
			type == FXDiagramAdapter
		}
	}
	
}