package de.fxdiagram.core.transform.tests;

import de.fxdiagram.core.geometry.TransformExtensions;
import java.util.ArrayList;
import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class TransformExtensionsTest {
  @Test
  public void test0() {
    Translate _translate = new Translate(1, 2);
    Translate _translate_1 = new Translate(1, 2, 3);
    Rotate _rotate = new Rotate(90);
    Rotate _rotate_1 = new Rotate(5, 6, 7);
    Rotate _rotate_2 = new Rotate(8, 9, 10, 11);
    Point3D _point3D = new Point3D(16, 17, 18);
    Rotate _rotate_3 = new Rotate(12, 13, 14, 15, _point3D);
    Point3D _point3D_1 = new Point3D(20, 21, 22);
    Rotate _rotate_4 = new Rotate(19, _point3D_1);
    Scale _scale = new Scale(23, 24);
    Scale _scale_1 = new Scale(25, 26, 27);
    Scale _scale_2 = new Scale(28, 29, 30, 31);
    Scale _scale_3 = new Scale(32, 33, 34, 35, 36, 37);
    Shear _shear = new Shear(38, 39);
    Shear _shear_1 = new Shear(40, 41);
    final ArrayList<Transform> matrices = CollectionLiterals.<Transform>newArrayList(_translate, _translate_1, _rotate, _rotate_1, _rotate_2, _rotate_3, _rotate_4, _scale, _scale_1, _scale_2, _scale_3, _shear, _shear_1);
    final Procedure1<Affine> _function = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.translate(it, 1, 2);
      }
    };
    final Procedure1<Affine> _function_1 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.translate(it, 1, 2, 3);
      }
    };
    final Procedure1<Affine> _function_2 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.rotate(it, 90);
      }
    };
    final Procedure1<Affine> _function_3 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.rotate(it, 5, 6, 7);
      }
    };
    final Procedure1<Affine> _function_4 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.rotate(it, 8, 9, 10, 11);
      }
    };
    final Procedure1<Affine> _function_5 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        Point3D _point3D = new Point3D(16, 17, 18);
        TransformExtensions.rotate(it, 12, 13, 14, 15, _point3D);
      }
    };
    final Procedure1<Affine> _function_6 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        Point3D _point3D = new Point3D(20, 21, 22);
        TransformExtensions.rotate(it, 19, _point3D);
      }
    };
    final Procedure1<Affine> _function_7 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.scale(it, 23, 24);
      }
    };
    final Procedure1<Affine> _function_8 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.scale(it, 25, 26, 27);
      }
    };
    final Procedure1<Affine> _function_9 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.scale(it, 28, 29, 30, 31);
      }
    };
    final Procedure1<Affine> _function_10 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.scale(it, 32, 33, 34, 35, 36, 37);
      }
    };
    final Procedure1<Affine> _function_11 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.shear(it, 38, 39);
      }
    };
    final Procedure1<Affine> _function_12 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.shear(it, 40, 41);
      }
    };
    final ArrayList<Procedure1<? super Affine>> operations = CollectionLiterals.<Procedure1<? super Affine>>newArrayList(_function, _function_1, _function_2, _function_3, _function_4, _function_5, _function_6, _function_7, _function_8, _function_9, _function_10, _function_11, _function_12);
    Point3D _point3D_2 = new Point3D(1, 0, 0);
    Point3D _point3D_3 = new Point3D(0, 1, 0);
    Point3D _point3D_4 = new Point3D(0, 0, 1);
    Point3D _point3D_5 = new Point3D(0, 1, 1);
    Point3D _point3D_6 = new Point3D(1, 0, 1);
    Point3D _point3D_7 = new Point3D(1, 1, 0);
    final ArrayList<Point3D> vectors = CollectionLiterals.<Point3D>newArrayList(_point3D_2, _point3D_3, _point3D_4, _point3D_5, _point3D_6, _point3D_7);
    for (final Transform l : matrices) {
      for (final Transform r : matrices) {
        {
          Affine _affine = new Affine();
          final Affine affine = _affine;
          TransformExtensions.leftMultiply(affine, r);
          int _indexOf = matrices.indexOf(l);
          Procedure1<? super Affine> _get = operations.get(_indexOf);
          ObjectExtensions.<Affine>operator_doubleArrow(affine, _get);
          for (final Point3D v : vectors) {
            {
              Point3D _multiply = TransformExtensions.operator_multiply(r, v);
              Point3D _multiply_1 = TransformExtensions.operator_multiply(l, _multiply);
              Transform _multiply_2 = TransformExtensions.operator_multiply(l, r);
              Point3D _multiply_3 = TransformExtensions.operator_multiply(_multiply_2, v);
              double _distance = _multiply_1.distance(_multiply_3);
              boolean _lessThan = (_distance < 1e-10);
              Assert.assertTrue(_lessThan);
              Point3D _multiply_4 = TransformExtensions.operator_multiply(affine, v);
              Transform _multiply_5 = TransformExtensions.operator_multiply(l, r);
              Point3D _multiply_6 = TransformExtensions.operator_multiply(_multiply_5, v);
              double _distance_1 = _multiply_4.distance(_multiply_6);
              boolean _lessThan_1 = (_distance_1 < 1e-10);
              Assert.assertTrue(_lessThan_1);
            }
          }
        }
      }
    }
  }
}
