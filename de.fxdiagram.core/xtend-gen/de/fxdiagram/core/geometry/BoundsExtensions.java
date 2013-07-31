package de.fxdiagram.core.geometry;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;

@SuppressWarnings("all")
public class BoundsExtensions {
  public static BoundingBox operator_plus(final Bounds left, final Bounds right) {
    BoundingBox _xblockexpression = null;
    {
      double _minX = left.getMinX();
      double _minX_1 = right.getMinX();
      final double minX = Math.min(_minX, _minX_1);
      double _minY = left.getMinY();
      double _minY_1 = right.getMinY();
      final double minY = Math.min(_minY, _minY_1);
      double _minZ = left.getMinZ();
      double _minZ_1 = right.getMinZ();
      final double minZ = Math.min(_minZ, _minZ_1);
      double _maxX = left.getMaxX();
      double _maxX_1 = right.getMaxX();
      final double maxX = Math.max(_maxX, _maxX_1);
      double _maxY = left.getMaxY();
      double _maxY_1 = right.getMaxY();
      final double maxY = Math.max(_maxY, _maxY_1);
      double _maxZ = left.getMaxZ();
      double _maxZ_1 = right.getMaxZ();
      final double maxZ = Math.max(_maxZ, _maxZ_1);
      double _minus = (maxX - minX);
      double _minus_1 = (maxY - minY);
      double _minus_2 = (maxZ - minZ);
      BoundingBox _boundingBox = new BoundingBox(minX, minY, minZ, _minus, _minus_1, _minus_2);
      _xblockexpression = (_boundingBox);
    }
    return _xblockexpression;
  }
  
  public static BoundingBox operator_plus(final Bounds bounds, final Insets insets) {
    double _minX = bounds.getMinX();
    double _left = insets.getLeft();
    double _minus = (_minX - _left);
    double _minY = bounds.getMinY();
    double _top = insets.getTop();
    double _minus_1 = (_minY - _top);
    double _width = bounds.getWidth();
    double _left_1 = insets.getLeft();
    double _plus = (_width + _left_1);
    double _right = insets.getRight();
    double _plus_1 = (_plus + _right);
    double _height = bounds.getHeight();
    double _top_1 = insets.getTop();
    double _plus_2 = (_height + _top_1);
    double _bottom = insets.getBottom();
    double _plus_3 = (_plus_2 + _bottom);
    BoundingBox _boundingBox = new BoundingBox(_minus, _minus_1, _plus_1, _plus_3);
    return _boundingBox;
  }
  
  public static BoundingBox translate(final Bounds bounds, final double tx, final double ty, final double tz) {
    double _minX = bounds.getMinX();
    double _plus = (_minX + tx);
    double _minY = bounds.getMinY();
    double _plus_1 = (_minY + ty);
    double _minZ = bounds.getMinZ();
    double _plus_2 = (_minZ + tz);
    double _width = bounds.getWidth();
    double _height = bounds.getHeight();
    double _depth = bounds.getDepth();
    BoundingBox _boundingBox = new BoundingBox(_plus, _plus_1, _plus_2, _width, _height, _depth);
    return _boundingBox;
  }
  
  public static BoundingBox translate(final Bounds bounds, final double tx, final double ty) {
    double _minX = bounds.getMinX();
    double _plus = (_minX + tx);
    double _minY = bounds.getMinY();
    double _plus_1 = (_minY + ty);
    double _width = bounds.getWidth();
    double _height = bounds.getHeight();
    BoundingBox _boundingBox = new BoundingBox(_plus, _plus_1, _width, _height);
    return _boundingBox;
  }
  
  public static BoundingBox scale(final Bounds it, final double scaleX, final double scaleY, final double scaleZ) {
    double _minX = it.getMinX();
    double _width = it.getWidth();
    double _multiply = (0.5 * _width);
    double _minus = (1 - scaleX);
    double _multiply_1 = (_multiply * _minus);
    double _plus = (_minX + _multiply_1);
    double _minY = it.getMinY();
    double _height = it.getHeight();
    double _multiply_2 = (0.5 * _height);
    double _minus_1 = (1 - scaleY);
    double _multiply_3 = (_multiply_2 * _minus_1);
    double _plus_1 = (_minY + _multiply_3);
    double _minZ = it.getMinZ();
    double _depth = it.getDepth();
    double _multiply_4 = (0.5 * _depth);
    double _minus_2 = (1 - scaleZ);
    double _multiply_5 = (_multiply_4 * _minus_2);
    double _plus_2 = (_minZ + _multiply_5);
    double _width_1 = it.getWidth();
    double _multiply_6 = (_width_1 * scaleX);
    double _height_1 = it.getHeight();
    double _multiply_7 = (_height_1 * scaleY);
    double _depth_1 = it.getDepth();
    double _multiply_8 = (_depth_1 * scaleZ);
    BoundingBox _boundingBox = new BoundingBox(_plus, _plus_1, _plus_2, _multiply_6, _multiply_7, _multiply_8);
    return _boundingBox;
  }
  
  public static BoundingBox scale(final Bounds it, final double scaleX, final double scaleY) {
    double _minX = it.getMinX();
    double _width = it.getWidth();
    double _multiply = (0.5 * _width);
    double _minus = (1 - scaleX);
    double _multiply_1 = (_multiply * _minus);
    double _plus = (_minX + _multiply_1);
    double _minY = it.getMinY();
    double _height = it.getHeight();
    double _multiply_2 = (0.5 * _height);
    double _minus_1 = (1 - scaleY);
    double _multiply_3 = (_multiply_2 * _minus_1);
    double _plus_1 = (_minY + _multiply_3);
    double _minZ = it.getMinZ();
    double _width_1 = it.getWidth();
    double _multiply_4 = (_width_1 * scaleX);
    double _height_1 = it.getHeight();
    double _multiply_5 = (_height_1 * scaleY);
    double _depth = it.getDepth();
    BoundingBox _boundingBox = new BoundingBox(_plus, _plus_1, _minZ, _multiply_4, _multiply_5, _depth);
    return _boundingBox;
  }
  
  public static Point2D center(final Bounds it) {
    double _minX = it.getMinX();
    double _width = it.getWidth();
    double _multiply = (0.5 * _width);
    double _plus = (_minX + _multiply);
    double _minY = it.getMinY();
    double _height = it.getHeight();
    double _multiply_1 = (0.5 * _height);
    double _plus_1 = (_minY + _multiply_1);
    Point2D _point2D = new Point2D(_plus, _plus_1);
    return _point2D;
  }
}
