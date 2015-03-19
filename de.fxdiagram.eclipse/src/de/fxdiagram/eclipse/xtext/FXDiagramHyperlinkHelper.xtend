package de.fxdiagram.eclipse.xtext

import com.google.inject.Inject
import com.google.inject.name.Named
import de.fxdiagram.eclipse.FXDiagramView
import de.fxdiagram.eclipse.mapping.AbstractMapping
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor
import de.fxdiagram.eclipse.mapping.MappingCall
import de.fxdiagram.eclipse.mapping.XDiagramConfig
import de.fxdiagram.eclipse.mapping.XtextDomainObjectProvider
import org.eclipse.emf.ecore.EObject
import org.eclipse.jface.text.Region
import org.eclipse.jface.text.hyperlink.IHyperlink
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.IWorkbench
import org.eclipse.xtend.lib.annotations.Data
import org.eclipse.xtext.resource.ILocationInFileProvider
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.ui.editor.hyperlinking.HyperlinkHelper
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkHelper
import org.eclipse.xtext.util.ITextRegion
import java.util.List

class FXDiagramHyperlinkHelper extends HyperlinkHelper {
	
	public static val DELEGATE = 'de.fxdiagram.eclipse.xtext.FXDiagramHyperlinkHelper.Delegate'

	@Inject@Named(DELEGATE) IHyperlinkHelper delegate

	@Inject IWorkbench workbench

	@Inject ILocationInFileProvider locationInFileProvider

	override createHyperlinksByOffset(XtextResource resource, int offset, boolean createMultipleHyperlinks) {
		val hyperlinks = delegate.createHyperlinksByOffset(resource, offset, createMultipleHyperlinks).emptyListIfNull
			+ super.createHyperlinksByOffset(resource, offset, createMultipleHyperlinks).emptyListIfNull
		if(hyperlinks.empty)
			null
		else 
			hyperlinks
	}
	
	protected def <T> List<T> emptyListIfNull(T[] array) {
		array as List<T> ?: emptyList
	}
	
	override createHyperlinksByOffset(XtextResource resource, int offset, IHyperlinkAcceptor acceptor) {
		val selectedElement = EObjectAtOffsetHelper.resolveElementAt(resource, offset)
		val editor = workbench?.activeWorkbenchWindow?.activePage?.activeEditor
		if (selectedElement != null) {
			val mappingCalls = XDiagramConfig.Registry.instance.configurations
				.map[getEntryCalls(selectedElement)]
				.flatten
			if (!mappingCalls.empty) {
				val region = locationInFileProvider.getSignificantTextRegion(selectedElement)
				for (mappingCall : mappingCalls) {
					val domainObjectProvider = mappingCall.mapping.config.domainObjectProvider
					if(domainObjectProvider instanceof XtextDomainObjectProvider) {
						val descriptor = domainObjectProvider
							.createMappedElementDescriptor(selectedElement, mappingCall.mapping as AbstractMapping<EObject>)
						acceptor.accept(new FXDiagramHyperlink(descriptor, mappingCall, region, editor))
					}
				}
			}
		}
	}

	@Data
	static class FXDiagramHyperlink implements IHyperlink {

		val IMappedElementDescriptor<EObject> descriptor
		val MappingCall<?, ? super EObject> mappingCall
		val ITextRegion region
		val IEditorPart editor

		override getHyperlinkRegion() {
			new Region(region.offset, region.length)
		}

		override getHyperlinkText() {
			'Show in FXDiagram as ' + mappingCall.mapping.ID
		}

		override getTypeLabel() {
			'FXDiagram'
		}

		override open() {
			val view = editor?.site?.workbenchWindow?.workbench?.activeWorkbenchWindow?.activePage
				?.showView("de.fxdiagram.eclipse.FXDiagramView")
			if (view instanceof FXDiagramView) {
				descriptor.withDomainObject [
					view.revealElement(it, mappingCall, editor)
					null
				]
			}
		}
	}
}