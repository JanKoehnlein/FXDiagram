package de.fxdiagram.lib.simple;

import de.fxdiagram.core.XRoot;
import de.fxdiagram.lib.simple.DiagramScaler;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import javafx.geometry.Point2D;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class OpenDiagramParameters {
  private final OpenableDiagramNode _host;
  
  public OpenableDiagramNode getHost() {
    return this._host;
  }
  
  private final XRoot _root;
  
  public XRoot getRoot() {
    return this._root;
  }
  
  private final DiagramScaler _diagramScaler;
  
  public DiagramScaler getDiagramScaler() {
    return this._diagramScaler;
  }
  
  private final Point2D _nodeCenterInDiagram;
  
  public Point2D getNodeCenterInDiagram() {
    return this._nodeCenterInDiagram;
  }
  
  public OpenDiagramParameters(final OpenableDiagramNode host, final XRoot root, final DiagramScaler diagramScaler, final Point2D nodeCenterInDiagram) {
    super();
    this._host = host;
    this._root = root;
    this._diagramScaler = diagramScaler;
    this._nodeCenterInDiagram = nodeCenterInDiagram;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_host== null) ? 0 : _host.hashCode());
    result = prime * result + ((_root== null) ? 0 : _root.hashCode());
    result = prime * result + ((_diagramScaler== null) ? 0 : _diagramScaler.hashCode());
    result = prime * result + ((_nodeCenterInDiagram== null) ? 0 : _nodeCenterInDiagram.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    OpenDiagramParameters other = (OpenDiagramParameters) obj;
    if (_host == null) {
      if (other._host != null)
        return false;
    } else if (!_host.equals(other._host))
      return false;
    if (_root == null) {
      if (other._root != null)
        return false;
    } else if (!_root.equals(other._root))
      return false;
    if (_diagramScaler == null) {
      if (other._diagramScaler != null)
        return false;
    } else if (!_diagramScaler.equals(other._diagramScaler))
      return false;
    if (_nodeCenterInDiagram == null) {
      if (other._nodeCenterInDiagram != null)
        return false;
    } else if (!_nodeCenterInDiagram.equals(other._nodeCenterInDiagram))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
