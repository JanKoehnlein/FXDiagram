package de.fxdiagram.eclipse.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.model.ModelLoad
import de.fxdiagram.core.tools.actions.LoadAction
import java.io.InputStreamReader
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.jface.window.Window
import org.eclipse.swt.widgets.Display
import org.eclipse.ui.dialogs.ResourceListSelectionDialog
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Text

class EclipseLoadAction extends LoadAction {
	
	override perform(XRoot root) {
        val shell = Display.^default.activeShell
		val workspaceRoot = ResourcesPlugin.workspace.root
		val dialog = new ResourceListSelectionDialog(shell, workspaceRoot, IResource.FILE) {
			override protected createDialogArea(Composite parent) {
				val control = super.createDialogArea(parent)
				parent.findTextWidget?.setText('*.fxd')
				control 
			}
		}
        if (dialog.open == Window.OK && dialog.result?.length == 1) {
            val file = dialog.result.head as IFile
            file.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor)
            if (file?.exists) {
                val node = new ModelLoad().load(new InputStreamReader(file.contents, file.charset))
                if (node instanceof XRoot) {
                    root.replaceDomainObjectProviders(node.domainObjectProviders)
                    root.rootDiagram = node.diagram
                    root.fileName = file.fullPath.toOSString
                }
            }
        }
    }
    
    private def Text findTextWidget(Composite composite) {
    	composite.children.map[
    		switch it {
    			Text: it
    			Composite: findTextWidget
    			default: null
    		}
   		].filterNull.head
    }
}