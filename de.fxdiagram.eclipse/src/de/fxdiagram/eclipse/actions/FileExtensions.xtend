package de.fxdiagram.eclipse.actions

import java.io.File
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path

class FileExtensions {
	
	static def IFile toWorkspaceFile(File javaFile) {
		val workspaceRoot = ResourcesPlugin.workspace.root
		val workspaceJavaFile = workspaceRoot.location.toFile
		val workspaceFullPath = workspaceJavaFile.toPath
		val fullPath = javaFile.toPath
		if(fullPath.startsWith(workspaceFullPath)) {
			workspaceRoot.getFile(new Path(workspaceFullPath.relativize(fullPath).toString))
		} else {
			null
		}
	}
	
	static def createParents(IFile file) {
		val path = file.fullPath
		val workspaceRoot = file.workspace.root
		for(var i=2; i<path.segmentCount; i++) {
			val folder = workspaceRoot.getFolder(path.uptoSegment(i))
			if(!folder.exists) {
				folder.create(true, true, new NullProgressMonitor)
			}
		}
	}
}