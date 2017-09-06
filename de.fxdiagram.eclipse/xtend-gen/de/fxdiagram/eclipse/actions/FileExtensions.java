package de.fxdiagram.eclipse.actions;

import java.io.File;
import java.nio.file.Path;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class FileExtensions {
  public static IFile toWorkspaceFile(final File javaFile) {
    IFile _xblockexpression = null;
    {
      final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
      final File workspaceJavaFile = workspaceRoot.getLocation().toFile();
      final Path workspaceFullPath = workspaceJavaFile.toPath();
      final Path fullPath = javaFile.toPath();
      IFile _xifexpression = null;
      boolean _startsWith = fullPath.startsWith(workspaceFullPath);
      if (_startsWith) {
        String _string = workspaceFullPath.relativize(fullPath).toString();
        org.eclipse.core.runtime.Path _path = new org.eclipse.core.runtime.Path(_string);
        _xifexpression = workspaceRoot.getFile(_path);
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public static void createParents(final IFile file) {
    try {
      final IPath path = file.getFullPath();
      final IWorkspaceRoot workspaceRoot = file.getWorkspace().getRoot();
      for (int i = 2; (i < path.segmentCount()); i++) {
        {
          final IFolder folder = workspaceRoot.getFolder(path.uptoSegment(i));
          boolean _exists = folder.exists();
          boolean _not = (!_exists);
          if (_not) {
            NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
            folder.create(true, true, _nullProgressMonitor);
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
