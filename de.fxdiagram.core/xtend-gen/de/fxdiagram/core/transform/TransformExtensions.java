package de.fxdiagram.core.transform;

import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

@SuppressWarnings("all")
public class TransformExtensions {
  /**
   * Accumulates another {@link Transform} in a given {@link Affine} using matrix multiplication.
   * When applying the combined transform, the original trafo is applied first.
   */
  public static void leftMultiply(final Affine it, final Transform l) {
    double _mxx = l.getMxx();
    double _mxx_1 = it.getMxx();
    double _multiply = (_mxx * _mxx_1);
    double _mxy = l.getMxy();
    double _myx = it.getMyx();
    double _multiply_1 = (_mxy * _myx);
    double _plus = (_multiply + _multiply_1);
    double _mxz = l.getMxz();
    double _mzx = it.getMzx();
    double _multiply_2 = (_mxz * _mzx);
    final double mxx_ = (_plus + _multiply_2);
    double _mxx_2 = l.getMxx();
    double _mxy_1 = it.getMxy();
    double _multiply_3 = (_mxx_2 * _mxy_1);
    double _mxy_2 = l.getMxy();
    double _myy = it.getMyy();
    double _multiply_4 = (_mxy_2 * _myy);
    double _plus_1 = (_multiply_3 + _multiply_4);
    double _mxz_1 = l.getMxz();
    double _mzy = it.getMzy();
    double _multiply_5 = (_mxz_1 * _mzy);
    final double mxy_ = (_plus_1 + _multiply_5);
    double _mxx_3 = l.getMxx();
    double _mxz_2 = it.getMxz();
    double _multiply_6 = (_mxx_3 * _mxz_2);
    double _mxy_3 = l.getMxy();
    double _myz = it.getMyz();
    double _multiply_7 = (_mxy_3 * _myz);
    double _plus_2 = (_multiply_6 + _multiply_7);
    double _mxz_3 = l.getMxz();
    double _mzz = it.getMzz();
    double _multiply_8 = (_mxz_3 * _mzz);
    final double mxz_ = (_plus_2 + _multiply_8);
    double _mxx_4 = l.getMxx();
    double _tx = it.getTx();
    double _multiply_9 = (_mxx_4 * _tx);
    double _mxy_4 = l.getMxy();
    double _ty = it.getTy();
    double _multiply_10 = (_mxy_4 * _ty);
    double _plus_3 = (_multiply_9 + _multiply_10);
    double _mxz_4 = l.getMxz();
    double _tz = it.getTz();
    double _multiply_11 = (_mxz_4 * _tz);
    double _plus_4 = (_plus_3 + _multiply_11);
    double _tx_1 = l.getTx();
    final double tx_ = (_plus_4 + _tx_1);
    double _myx_1 = l.getMyx();
    double _mxx_5 = it.getMxx();
    double _multiply_12 = (_myx_1 * _mxx_5);
    double _myy_1 = l.getMyy();
    double _myx_2 = it.getMyx();
    double _multiply_13 = (_myy_1 * _myx_2);
    double _plus_5 = (_multiply_12 + _multiply_13);
    double _myz_1 = l.getMyz();
    double _mzx_1 = it.getMzx();
    double _multiply_14 = (_myz_1 * _mzx_1);
    final double myx_ = (_plus_5 + _multiply_14);
    double _myx_3 = l.getMyx();
    double _mxy_5 = it.getMxy();
    double _multiply_15 = (_myx_3 * _mxy_5);
    double _myy_2 = l.getMyy();
    double _myy_3 = it.getMyy();
    double _multiply_16 = (_myy_2 * _myy_3);
    double _plus_6 = (_multiply_15 + _multiply_16);
    double _myz_2 = l.getMyz();
    double _mzy_1 = it.getMzy();
    double _multiply_17 = (_myz_2 * _mzy_1);
    final double myy_ = (_plus_6 + _multiply_17);
    double _myx_4 = l.getMyx();
    double _mxz_5 = it.getMxz();
    double _multiply_18 = (_myx_4 * _mxz_5);
    double _myy_4 = l.getMyy();
    double _myz_3 = it.getMyz();
    double _multiply_19 = (_myy_4 * _myz_3);
    double _plus_7 = (_multiply_18 + _multiply_19);
    double _myz_4 = l.getMyz();
    double _mzz_1 = it.getMzz();
    double _multiply_20 = (_myz_4 * _mzz_1);
    final double myz_ = (_plus_7 + _multiply_20);
    double _myx_5 = l.getMyx();
    double _tx_2 = it.getTx();
    double _multiply_21 = (_myx_5 * _tx_2);
    double _myy_5 = l.getMyy();
    double _ty_1 = it.getTy();
    double _multiply_22 = (_myy_5 * _ty_1);
    double _plus_8 = (_multiply_21 + _multiply_22);
    double _myz_5 = l.getMyz();
    double _tz_1 = it.getTz();
    double _multiply_23 = (_myz_5 * _tz_1);
    double _plus_9 = (_plus_8 + _multiply_23);
    double _ty_2 = l.getTy();
    final double ty_ = (_plus_9 + _ty_2);
    double _mzx_2 = l.getMzx();
    double _mxx_6 = it.getMxx();
    double _multiply_24 = (_mzx_2 * _mxx_6);
    double _mzy_2 = l.getMzy();
    double _myx_6 = it.getMyx();
    double _multiply_25 = (_mzy_2 * _myx_6);
    double _plus_10 = (_multiply_24 + _multiply_25);
    double _mzz_2 = l.getMzz();
    double _mzx_3 = it.getMzx();
    double _multiply_26 = (_mzz_2 * _mzx_3);
    final double mzx_ = (_plus_10 + _multiply_26);
    double _mzx_4 = l.getMzx();
    double _mxy_6 = it.getMxy();
    double _multiply_27 = (_mzx_4 * _mxy_6);
    double _mzy_3 = l.getMzy();
    double _myy_6 = it.getMyy();
    double _multiply_28 = (_mzy_3 * _myy_6);
    double _plus_11 = (_multiply_27 + _multiply_28);
    double _mzz_3 = l.getMzz();
    double _mzy_4 = it.getMzy();
    double _multiply_29 = (_mzz_3 * _mzy_4);
    final double mzy_ = (_plus_11 + _multiply_29);
    double _mzx_5 = l.getMzx();
    double _mxz_6 = it.getMxz();
    double _multiply_30 = (_mzx_5 * _mxz_6);
    double _mzy_5 = l.getMzy();
    double _myz_6 = it.getMyz();
    double _multiply_31 = (_mzy_5 * _myz_6);
    double _plus_12 = (_multiply_30 + _multiply_31);
    double _mzz_4 = l.getMzz();
    double _mzz_5 = it.getMzz();
    double _multiply_32 = (_mzz_4 * _mzz_5);
    final double mzz_ = (_plus_12 + _multiply_32);
    double _mzx_6 = l.getMzx();
    double _tx_3 = it.getTx();
    double _multiply_33 = (_mzx_6 * _tx_3);
    double _mzy_6 = l.getMzy();
    double _ty_3 = it.getTy();
    double _multiply_34 = (_mzy_6 * _ty_3);
    double _plus_13 = (_multiply_33 + _multiply_34);
    double _mzz_6 = l.getMzz();
    double _tz_2 = it.getTz();
    double _multiply_35 = (_mzz_6 * _tz_2);
    double _plus_14 = (_plus_13 + _multiply_35);
    double _tz_3 = l.getTz();
    final double tz_ = (_plus_14 + _tz_3);
    it.setMxx(mxx_);
    it.setMxy(mxy_);
    it.setMxz(mxz_);
    it.setMyx(myx_);
    it.setMyy(myy_);
    it.setMyz(myz_);
    it.setMzx(mzx_);
    it.setMzy(mzy_);
    it.setMzz(mzz_);
    it.setTx(tx_);
    it.setTy(ty_);
    it.setTz(tz_);
  }
  
  /**
   * Accumulates (multiplies) two {@link Transform}s into a new {@link Transform}.
   * When applying the transform, <code>r</code> is applied first.
   */
  public static Transform operator_multiply(final Transform l, final Transform r) {
    double _mxx = l.getMxx();
    double _mxx_1 = r.getMxx();
    double _multiply = (_mxx * _mxx_1);
    double _mxy = l.getMxy();
    double _myx = r.getMyx();
    double _multiply_1 = (_mxy * _myx);
    double _plus = (_multiply + _multiply_1);
    double _mxz = l.getMxz();
    double _mzx = r.getMzx();
    double _multiply_2 = (_mxz * _mzx);
    double _plus_1 = (_plus + _multiply_2);
    double _mxx_2 = l.getMxx();
    double _mxy_1 = r.getMxy();
    double _multiply_3 = (_mxx_2 * _mxy_1);
    double _mxy_2 = l.getMxy();
    double _myy = r.getMyy();
    double _multiply_4 = (_mxy_2 * _myy);
    double _plus_2 = (_multiply_3 + _multiply_4);
    double _mxz_1 = l.getMxz();
    double _mzy = r.getMzy();
    double _multiply_5 = (_mxz_1 * _mzy);
    double _plus_3 = (_plus_2 + _multiply_5);
    double _mxx_3 = l.getMxx();
    double _mxz_2 = r.getMxz();
    double _multiply_6 = (_mxx_3 * _mxz_2);
    double _mxy_3 = l.getMxy();
    double _myz = r.getMyz();
    double _multiply_7 = (_mxy_3 * _myz);
    double _plus_4 = (_multiply_6 + _multiply_7);
    double _mxz_3 = l.getMxz();
    double _mzz = r.getMzz();
    double _multiply_8 = (_mxz_3 * _mzz);
    double _plus_5 = (_plus_4 + _multiply_8);
    double _mxx_4 = l.getMxx();
    double _tx = r.getTx();
    double _multiply_9 = (_mxx_4 * _tx);
    double _mxy_4 = l.getMxy();
    double _ty = r.getTy();
    double _multiply_10 = (_mxy_4 * _ty);
    double _plus_6 = (_multiply_9 + _multiply_10);
    double _mxz_4 = l.getMxz();
    double _tz = r.getTz();
    double _multiply_11 = (_mxz_4 * _tz);
    double _plus_7 = (_plus_6 + _multiply_11);
    double _tx_1 = l.getTx();
    double _plus_8 = (_plus_7 + _tx_1);
    double _myx_1 = l.getMyx();
    double _mxx_5 = r.getMxx();
    double _multiply_12 = (_myx_1 * _mxx_5);
    double _myy_1 = l.getMyy();
    double _myx_2 = r.getMyx();
    double _multiply_13 = (_myy_1 * _myx_2);
    double _plus_9 = (_multiply_12 + _multiply_13);
    double _myz_1 = l.getMyz();
    double _mzx_1 = r.getMzx();
    double _multiply_14 = (_myz_1 * _mzx_1);
    double _plus_10 = (_plus_9 + _multiply_14);
    double _myx_3 = l.getMyx();
    double _mxy_5 = r.getMxy();
    double _multiply_15 = (_myx_3 * _mxy_5);
    double _myy_2 = l.getMyy();
    double _myy_3 = r.getMyy();
    double _multiply_16 = (_myy_2 * _myy_3);
    double _plus_11 = (_multiply_15 + _multiply_16);
    double _myz_2 = l.getMyz();
    double _mzy_1 = r.getMzy();
    double _multiply_17 = (_myz_2 * _mzy_1);
    double _plus_12 = (_plus_11 + _multiply_17);
    double _myx_4 = l.getMyx();
    double _mxz_5 = r.getMxz();
    double _multiply_18 = (_myx_4 * _mxz_5);
    double _myy_4 = l.getMyy();
    double _myz_3 = r.getMyz();
    double _multiply_19 = (_myy_4 * _myz_3);
    double _plus_13 = (_multiply_18 + _multiply_19);
    double _myz_4 = l.getMyz();
    double _mzz_1 = r.getMzz();
    double _multiply_20 = (_myz_4 * _mzz_1);
    double _plus_14 = (_plus_13 + _multiply_20);
    double _myx_5 = l.getMyx();
    double _tx_2 = r.getTx();
    double _multiply_21 = (_myx_5 * _tx_2);
    double _myy_5 = l.getMyy();
    double _ty_1 = r.getTy();
    double _multiply_22 = (_myy_5 * _ty_1);
    double _plus_15 = (_multiply_21 + _multiply_22);
    double _myz_5 = l.getMyz();
    double _tz_1 = r.getTz();
    double _multiply_23 = (_myz_5 * _tz_1);
    double _plus_16 = (_plus_15 + _multiply_23);
    double _ty_2 = l.getTy();
    double _plus_17 = (_plus_16 + _ty_2);
    double _mzx_2 = l.getMzx();
    double _mxx_6 = r.getMxx();
    double _multiply_24 = (_mzx_2 * _mxx_6);
    double _mzy_2 = l.getMzy();
    double _myx_6 = r.getMyx();
    double _multiply_25 = (_mzy_2 * _myx_6);
    double _plus_18 = (_multiply_24 + _multiply_25);
    double _mzz_2 = l.getMzz();
    double _mzx_3 = r.getMzx();
    double _multiply_26 = (_mzz_2 * _mzx_3);
    double _plus_19 = (_plus_18 + _multiply_26);
    double _mzx_4 = l.getMzx();
    double _mxy_6 = r.getMxy();
    double _multiply_27 = (_mzx_4 * _mxy_6);
    double _mzy_3 = l.getMzy();
    double _myy_6 = r.getMyy();
    double _multiply_28 = (_mzy_3 * _myy_6);
    double _plus_20 = (_multiply_27 + _multiply_28);
    double _mzz_3 = l.getMzz();
    double _mzy_4 = r.getMzy();
    double _multiply_29 = (_mzz_3 * _mzy_4);
    double _plus_21 = (_plus_20 + _multiply_29);
    double _mzx_5 = l.getMzx();
    double _mxz_6 = r.getMxz();
    double _multiply_30 = (_mzx_5 * _mxz_6);
    double _mzy_5 = l.getMzy();
    double _myz_6 = r.getMyz();
    double _multiply_31 = (_mzy_5 * _myz_6);
    double _plus_22 = (_multiply_30 + _multiply_31);
    double _mzz_4 = l.getMzz();
    double _mzz_5 = r.getMzz();
    double _multiply_32 = (_mzz_4 * _mzz_5);
    double _plus_23 = (_plus_22 + _multiply_32);
    double _mzx_6 = l.getMzx();
    double _tx_3 = r.getTx();
    double _multiply_33 = (_mzx_6 * _tx_3);
    double _mzy_6 = l.getMzy();
    double _ty_3 = r.getTy();
    double _multiply_34 = (_mzy_6 * _ty_3);
    double _plus_24 = (_multiply_33 + _multiply_34);
    double _mzz_6 = l.getMzz();
    double _tz_2 = r.getTz();
    double _multiply_35 = (_mzz_6 * _tz_2);
    double _plus_25 = (_plus_24 + _multiply_35);
    double _tz_3 = l.getTz();
    double _plus_26 = (_plus_25 + _tz_3);
    Affine _affine = Transform.affine(_plus_1, _plus_3, _plus_5, _plus_8, _plus_10, _plus_12, _plus_14, _plus_17, _plus_19, _plus_21, _plus_23, _plus_26);
    return _affine;
  }
  
  /**
   * Applies a {@link Transform} to a {@link Point3D}.
   */
  public static Point3D operator_multiply(final Transform l, final Point3D x) {
    double _mxx = l.getMxx();
    double _x = x.getX();
    double _multiply = (_mxx * _x);
    double _mxy = l.getMxy();
    double _y = x.getY();
    double _multiply_1 = (_mxy * _y);
    double _plus = (_multiply + _multiply_1);
    double _mxz = l.getMxz();
    double _z = x.getZ();
    double _multiply_2 = (_mxz * _z);
    double _plus_1 = (_plus + _multiply_2);
    double _tx = l.getTx();
    double _plus_2 = (_plus_1 + _tx);
    double _myx = l.getMyx();
    double _x_1 = x.getX();
    double _multiply_3 = (_myx * _x_1);
    double _myy = l.getMyy();
    double _y_1 = x.getY();
    double _multiply_4 = (_myy * _y_1);
    double _plus_3 = (_multiply_3 + _multiply_4);
    double _myz = l.getMyz();
    double _z_1 = x.getZ();
    double _multiply_5 = (_myz * _z_1);
    double _plus_4 = (_plus_3 + _multiply_5);
    double _ty = l.getTy();
    double _plus_5 = (_plus_4 + _ty);
    double _mzx = l.getMzx();
    double _x_2 = x.getX();
    double _multiply_6 = (_mzx * _x_2);
    double _mzy = l.getMzy();
    double _y_2 = x.getY();
    double _multiply_7 = (_mzy * _y_2);
    double _plus_6 = (_multiply_6 + _multiply_7);
    double _mzz = l.getMzz();
    double _z_2 = x.getZ();
    double _multiply_8 = (_mzz * _z_2);
    double _plus_7 = (_plus_6 + _multiply_8);
    double _tz = l.getTz();
    double _plus_8 = (_plus_7 + _tz);
    Point3D _point3D = new Point3D(_plus_2, _plus_5, _plus_8);
    return _point3D;
  }
  
  /**
   * Adds a translation to the given {@link Affine}
   */
  public static void translate(final Affine it, final double x, final double y) {
    double _tx = it.getTx();
    double _plus = (_tx + x);
    it.setTx(_plus);
    double _ty = it.getTy();
    double _plus_1 = (_ty + y);
    it.setTy(_plus_1);
  }
  
  public static void translate(final Affine it, final double x, final double y, final double z) {
    double _tx = it.getTx();
    double _plus = (_tx + x);
    it.setTx(_plus);
    double _ty = it.getTy();
    double _plus_1 = (_ty + y);
    it.setTy(_plus_1);
    double _tz = it.getTz();
    double _plus_2 = (_tz + z);
    it.setTz(_plus_2);
  }
  
  public static void rotate(final Affine it, final double angle) {
    Rotate _rotate = new Rotate(angle);
    TransformExtensions.leftMultiply(it, _rotate);
  }
  
  public static void rotate(final Affine it, final double angle, final double pivotX, final double pivotY) {
    Rotate _rotate = new Rotate(angle, pivotX, pivotY);
    TransformExtensions.leftMultiply(it, _rotate);
  }
  
  public static void rotate(final Affine it, final double angle, final double pivotX, final double pivotY, final double pivotZ) {
    Rotate _rotate = new Rotate(angle, pivotX, pivotY, pivotZ);
    TransformExtensions.leftMultiply(it, _rotate);
  }
  
  public static void rotate(final Affine it, final double angle, final double pivotX, final double pivotY, final double pivotZ, final Point3D axis) {
    Rotate _rotate = new Rotate(angle, pivotX, pivotY, pivotZ, axis);
    TransformExtensions.leftMultiply(it, _rotate);
  }
  
  public static void rotate(final Affine it, final double angle, final Point3D axis) {
    Rotate _rotate = new Rotate(angle, axis);
    TransformExtensions.leftMultiply(it, _rotate);
  }
  
  public static void scale(final Affine it, final double x, final double y) {
    double _mxx = it.getMxx();
    double _multiply = (x * _mxx);
    it.setMxx(_multiply);
    double _mxy = it.getMxy();
    double _multiply_1 = (x * _mxy);
    it.setMxy(_multiply_1);
    double _mxz = it.getMxz();
    double _multiply_2 = (x * _mxz);
    it.setMxz(_multiply_2);
    double _tx = it.getTx();
    double _multiply_3 = (x * _tx);
    it.setTx(_multiply_3);
    double _myx = it.getMyx();
    double _multiply_4 = (y * _myx);
    it.setMyx(_multiply_4);
    double _myy = it.getMyy();
    double _multiply_5 = (y * _myy);
    it.setMyy(_multiply_5);
    double _myz = it.getMyz();
    double _multiply_6 = (y * _myz);
    it.setMyz(_multiply_6);
    double _ty = it.getTy();
    double _multiply_7 = (y * _ty);
    it.setTy(_multiply_7);
  }
  
  public static void scale(final Affine it, final double x, final double y, final double z) {
    double _mxx = it.getMxx();
    double _multiply = (x * _mxx);
    it.setMxx(_multiply);
    double _mxy = it.getMxy();
    double _multiply_1 = (x * _mxy);
    it.setMxy(_multiply_1);
    double _mxz = it.getMxz();
    double _multiply_2 = (x * _mxz);
    it.setMxz(_multiply_2);
    double _tx = it.getTx();
    double _multiply_3 = (x * _tx);
    it.setTx(_multiply_3);
    double _myx = it.getMyx();
    double _multiply_4 = (y * _myx);
    it.setMyx(_multiply_4);
    double _myy = it.getMyy();
    double _multiply_5 = (y * _myy);
    it.setMyy(_multiply_5);
    double _myz = it.getMyz();
    double _multiply_6 = (y * _myz);
    it.setMyz(_multiply_6);
    double _ty = it.getTy();
    double _multiply_7 = (y * _ty);
    it.setTy(_multiply_7);
    double _mzx = it.getMzx();
    double _multiply_8 = (z * _mzx);
    it.setMzx(_multiply_8);
    double _mzy = it.getMzy();
    double _multiply_9 = (z * _mzy);
    it.setMzy(_multiply_9);
    double _mzz = it.getMzz();
    double _multiply_10 = (z * _mzz);
    it.setMzz(_multiply_10);
    double _tz = it.getTz();
    double _multiply_11 = (z * _tz);
    it.setTz(_multiply_11);
  }
  
  public static void scale(final Affine it, final double x, final double y, final double pivotX, final double pivotY) {
    Scale _scale = new Scale(x, y, pivotX, pivotY);
    TransformExtensions.leftMultiply(it, _scale);
  }
  
  public static void scale(final Affine it, final double x, final double y, final double z, final double pivotX, final double pivotY, final double pivotZ) {
    Scale _scale = new Scale(x, y, z, pivotX, pivotY, pivotZ);
    TransformExtensions.leftMultiply(it, _scale);
  }
  
  public static void shear(final Affine it, final double x, final double y) {
    double _mxx = it.getMxx();
    double _myx = it.getMyx();
    double _multiply = (x * _myx);
    final double mxx_ = (_mxx + _multiply);
    double _mxy = it.getMxy();
    double _myy = it.getMyy();
    double _multiply_1 = (x * _myy);
    final double mxy_ = (_mxy + _multiply_1);
    double _mxz = it.getMxz();
    double _myz = it.getMyz();
    double _multiply_2 = (x * _myz);
    final double mxz_ = (_mxz + _multiply_2);
    double _tx = it.getTx();
    double _ty = it.getTy();
    double _multiply_3 = (x * _ty);
    final double tx_ = (_tx + _multiply_3);
    double _mxx_1 = it.getMxx();
    double _multiply_4 = (y * _mxx_1);
    double _myx_1 = it.getMyx();
    final double myx_ = (_multiply_4 + _myx_1);
    double _mxy_1 = it.getMxy();
    double _multiply_5 = (y * _mxy_1);
    double _myy_1 = it.getMyy();
    final double myy_ = (_multiply_5 + _myy_1);
    double _mxz_1 = it.getMxz();
    double _multiply_6 = (y * _mxz_1);
    double _myz_1 = it.getMyz();
    final double myz_ = (_multiply_6 + _myz_1);
    double _tx_1 = it.getTx();
    double _multiply_7 = (y * _tx_1);
    double _ty_1 = it.getTy();
    final double ty_ = (_multiply_7 + _ty_1);
    it.setMxx(mxx_);
    it.setMxy(mxy_);
    it.setMxz(mxz_);
    it.setMyx(myx_);
    it.setMyy(myy_);
    it.setMyz(myz_);
    it.setTx(tx_);
    it.setTy(ty_);
  }
  
  public static void shear(final Affine it, final double x, final double y, final double pivotX, final double pivotY) {
    double _mxx = it.getMxx();
    double _myx = it.getMyx();
    double _multiply = (x * _myx);
    final double mxx_ = (_mxx + _multiply);
    double _mxy = it.getMxy();
    double _myy = it.getMyy();
    double _multiply_1 = (x * _myy);
    final double mxy_ = (_mxy + _multiply_1);
    double _mxz = it.getMxz();
    double _myz = it.getMyz();
    double _multiply_2 = (x * _myz);
    final double mxz_ = (_mxz + _multiply_2);
    double _tx = it.getTx();
    double _ty = it.getTy();
    double _multiply_3 = (x * _ty);
    double _plus = (_tx + _multiply_3);
    double _multiply_4 = (x * pivotY);
    final double tx_ = (_plus - _multiply_4);
    double _mxx_1 = it.getMxx();
    double _multiply_5 = (y * _mxx_1);
    double _myx_1 = it.getMyx();
    final double myx_ = (_multiply_5 + _myx_1);
    double _mxy_1 = it.getMxy();
    double _multiply_6 = (y * _mxy_1);
    double _myy_1 = it.getMyy();
    final double myy_ = (_multiply_6 + _myy_1);
    double _mxz_1 = it.getMxz();
    double _multiply_7 = (y * _mxz_1);
    double _myz_1 = it.getMyz();
    final double myz_ = (_multiply_7 + _myz_1);
    double _tx_1 = it.getTx();
    double _multiply_8 = (y * _tx_1);
    double _ty_1 = it.getTy();
    double _plus_1 = (_multiply_8 + _ty_1);
    double _multiply_9 = (y * pivotX);
    final double ty_ = (_plus_1 - _multiply_9);
    it.setMxx(mxx_);
    it.setMxy(mxy_);
    it.setMxz(mxz_);
    it.setMyx(myx_);
    it.setMyy(myy_);
    it.setMyz(myz_);
    it.setTx(tx_);
    it.setTy(ty_);
  }
}
