package de.fxdiagram.eclipse.actions;

import java.io.File;
import java.nio.file.Path;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspace;
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
      IWorkspace _workspace = ResourcesPlugin.getWorkspace();
      final IWorkspaceRoot workspaceRoot = _workspace.getRoot();
      IPath _location = workspaceRoot.getLocation();
      final File workspaceJavaFile = _location.toFile();
      final Path workspaceFullPath = workspaceJavaFile.toPath();
      final Path fullPath = javaFile.toPath();
      IFile _xifexpression = null;
      boolean _startsWith = fullPath.startsWith(workspaceFullPath);
      if (_startsWith) {
        Path _relativize = workspaceFullPath.relativize(fullPath);
        String _string = _relativize.toString();
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
      IWorkspace _workspace = file.getWorkspace();
      final IWorkspaceRoot workspaceRoot = _workspace.getRoot();
      for (int i = 2; (i < path.segmentCount()); i++) {
        {
          IPath _uptoSegment = path.uptoSegment(i);
          final IFolder folder = workspaceRoot.getFolder(_uptoSegment);
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
