package de.fxdiagram.lib.anchors;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.extensions.CoreExtensions;
import java.util.ArrayList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IntegerRange;

@SuppressWarnings("all")
public class DiscreteAnchors implements Anchors {
  private XNode host;
  
  private int numAnchorsPerSide;
  
  public DiscreteAnchors(final XNode host, final int numAnchorsPerSide) {
    this.host = host;
    this.numAnchorsPerSide = numAnchorsPerSide;
  }
  
  @Override
  public Point2D getAnchor(final double x, final double y) {
    Point2D _xblockexpression = null;
    {
      double currentDistance = Double.MAX_VALUE;
      Point2D currentAnchor = null;
      ArrayList<Point2D> _calculatePoints = this.calculatePoints();
      for (final Point2D p : _calculatePoints) {
        {
          final double candidateDistance = p.distance(x, y);
          if ((candidateDistance < currentDistance)) {
            currentAnchor = p;
            currentDistance = candidateDistance;
          }
        }
      }
      _xblockexpression = currentAnchor;
    }
    return _xblockexpression;
  }
  
  protected Node getReferenceNode() {
    return this.host.getNode();
  }
  
  protected ArrayList<Point2D> calculatePoints() {
    ArrayList<Point2D> _xblockexpression = null;
    {
      Node _referenceNode = this.getReferenceNode();
      Bounds _boundsInLocal = null;
      if (_referenceNode!=null) {
        _boundsInLocal=_referenceNode.getBoundsInLocal();
      }
      final Bounds bounds = _boundsInLocal;
      ArrayList<Point2D> _xifexpression = null;
      if ((bounds != null)) {
        ArrayList<Point2D> _xblockexpression_1 = null;
        {
          double _maxX = bounds.getMaxX();
          double _minX = bounds.getMinX();
          double _minus = (_maxX - _minX);
          final double deltaX = (_minus / (this.numAnchorsPerSide + 1));
          double _maxY = bounds.getMaxY();
          double _minY = bounds.getMinY();
          double _minus_1 = (_maxY - _minY);
          final double deltaY = (_minus_1 / (this.numAnchorsPerSide + 1));
          final ArrayList<Point2D> anchors = CollectionLiterals.<Point2D>newArrayList();
          IntegerRange _upTo = new IntegerRange(1, this.numAnchorsPerSide);
          for (final Integer i : _upTo) {
            {
              Node _referenceNode_1 = this.getReferenceNode();
              double _minX_1 = bounds.getMinX();
              double _minY_1 = bounds.getMinY();
              double _plus = (_minY_1 + ((i).intValue() * deltaY));
              Point2D _localToRootDiagram = CoreExtensions.localToRootDiagram(_referenceNode_1, _minX_1, _plus);
              anchors.add(_localToRootDiagram);
              Node _referenceNode_2 = this.getReferenceNode();
              double _maxX_1 = bounds.getMaxX();
              double _minY_2 = bounds.getMinY();
              double _plus_1 = (_minY_2 + ((i).intValue() * deltaY));
              Point2D _localToRootDiagram_1 = CoreExtensions.localToRootDiagram(_referenceNode_2, _maxX_1, _plus_1);
              anchors.add(_localToRootDiagram_1);
              Node _referenceNode_3 = this.getReferenceNode();
              double _minX_2 = bounds.getMinX();
              double _plus_2 = (_minX_2 + ((i).intValue() * deltaX));
              Point2D _localToRootDiagram_2 = CoreExtensions.localToRootDiagram(_referenceNode_3, _plus_2, bounds.getMinY());
              anchors.add(_localToRootDiagram_2);
              Node _referenceNode_4 = this.getReferenceNode();
              double _minX_3 = bounds.getMinX();
              double _plus_3 = (_minX_3 + ((i).intValue() * deltaX));
              Point2D _localToRootDiagram_3 = CoreExtensions.localToRootDiagram(_referenceNode_4, _plus_3, bounds.getMaxY());
              anchors.add(_localToRootDiagram_3);
            }
          }
          _xblockexpression_1 = anchors;
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
