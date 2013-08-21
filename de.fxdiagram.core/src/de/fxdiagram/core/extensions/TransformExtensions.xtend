package de.fxdiagram.core.extensions

import javafx.geometry.Point3D
import javafx.scene.transform.Affine
import javafx.scene.transform.Rotate
import javafx.scene.transform.Scale
import javafx.scene.transform.Transform

class TransformExtensions {
	/**
	 * Accumulates another {@link Transform} in a given {@link Affine} using matrix multiplication. 
	 * When applying the combined transform, the original trafo is applied first.
	 */
	def static leftMultiply(Affine it, Transform l) {
		val mxx_ = l.mxx * mxx + l.mxy * myx + l.mxz * mzx
		val mxy_ = l.mxx * mxy + l.mxy * myy + l.mxz * mzy
		val mxz_ = l.mxx * mxz + l.mxy * myz + l.mxz * mzz
		val tx_ = l.mxx * tx + l.mxy * ty + l.mxz * tz + l.tx

		val myx_ = l.myx * mxx + l.myy * myx + l.myz * mzx
		val myy_ = l.myx * mxy + l.myy * myy + l.myz * mzy
		val myz_ = l.myx * mxz + l.myy * myz + l.myz * mzz
		val ty_ = l.myx * tx + l.myy * ty + l.myz * tz + l.ty

		val mzx_ = l.mzx * mxx + l.mzy * myx + l.mzz * mzx
		val mzy_ = l.mzx * mxy + l.mzy * myy + l.mzz * mzy
		val mzz_ = l.mzx * mxz + l.mzy * myz + l.mzz * mzz
		val tz_ = l.mzx * tx + l.mzy * ty + l.mzz * tz + l.tz

		mxx = mxx_
		mxy = mxy_
		mxz = mxz_
		myx = myx_
		myy = myy_
		myz = myz_
		mzx = mzx_
		mzy = mzy_
		mzz = mzz_
		tx = tx_
		ty = ty_
		tz = tz_
	}

	/**
	 * Accumulates (multiplies) two {@link Transform}s into a new {@link Transform}. 
	 * When applying the transform, <code>r</code> is applied first.
	 */
	def static Transform operator_multiply(Transform l, Transform r) {
		Transform.affine(l.mxx * r.mxx + l.mxy * r.myx + l.mxz * r.mzx, l.mxx * r.mxy + l.mxy * r.myy + l.mxz * r.mzy,
			l.mxx * r.mxz + l.mxy * r.myz + l.mxz * r.mzz, l.mxx * r.tx + l.mxy * r.ty + l.mxz * r.tz + l.tx,
			l.myx * r.mxx + l.myy * r.myx + l.myz * r.mzx, l.myx * r.mxy + l.myy * r.myy + l.myz * r.mzy,
			l.myx * r.mxz + l.myy * r.myz + l.myz * r.mzz, l.myx * r.tx + l.myy * r.ty + l.myz * r.tz + l.ty,
			l.mzx * r.mxx + l.mzy * r.myx + l.mzz * r.mzx, l.mzx * r.mxy + l.mzy * r.myy + l.mzz * r.mzy,
			l.mzx * r.mxz + l.mzy * r.myz + l.mzz * r.mzz, l.mzx * r.tx + l.mzy * r.ty + l.mzz * r.tz + l.tz)
	}

	/**
	 * Applies a {@link Transform} to a {@link Point3D}.
	 */
	def static Point3D operator_multiply(Transform l, Point3D x) {
		new Point3D(
			l.mxx * x.x + l.mxy * x.y + l.mxz * x.z + l.tx,
			l.myx * x.x + l.myy * x.y + l.myz * x.z + l.ty,
			l.mzx * x.x + l.mzy * x.y + l.mzz * x.z + l.tz
		)
	}

	/**
	 * Adds a translation to the given {@link Affine}
	 */
	def static translate(Affine it, double x, double y) {
		tx = tx + x
		ty = ty + y
	}

	def static translate(Affine it, double x, double y, double z) {
		tx = tx + x
		ty = ty + y
		tz = tz + z
	}

	def static rotate(Affine it, double angle) {
		leftMultiply(it, new Rotate(angle))
	}

	def static rotate(Affine it, double angle, double pivotX, double pivotY) {
		leftMultiply(it, new Rotate(angle, pivotX, pivotY))
	}

	def static rotate(Affine it, double angle, double pivotX, double pivotY, double pivotZ) {
		leftMultiply(it, new Rotate(angle, pivotX, pivotY, pivotZ))
	}

	def static rotate(Affine it, double angle, double pivotX, double pivotY, double pivotZ, Point3D axis) {
		leftMultiply(it, new Rotate(angle, pivotX, pivotY, pivotZ, axis))
	}

	def static rotate(Affine it, double angle, Point3D axis) {
		leftMultiply(it, new Rotate(angle, axis))
	}

	def static scale(Affine it, double x, double y) {
		mxx = x * mxx
		mxy = x * mxy
		mxz = x * mxz
		tx = x * tx

		myx = y * myx
		myy = y * myy
		myz = y * myz
		ty = y * ty
	}

	def static scale(Affine it, double x, double y, double z) {
		mxx = x * mxx
		mxy = x * mxy
		mxz = x * mxz
		tx = x * tx

		myx = y * myx
		myy = y * myy
		myz = y * myz
		ty = y * ty

		mzx = z * mzx
		mzy = z * mzy
		mzz = z * mzz
		tz = z * tz
	}

	def static scale(Affine it, double x, double y, double pivotX, double pivotY) {
		leftMultiply(it, new Scale(x, y, pivotX, pivotY));
	}

	def static scale(Affine it, double x, double y, double z, double pivotX, double pivotY, double pivotZ) {
		leftMultiply(it, new Scale(x, y, z, pivotX, pivotY, pivotZ));
	}

	def static shear(Affine it, double x, double y) {
		val mxx_ = mxx + x * myx
		val mxy_ = mxy + x * myy
		val mxz_ = mxz + x * myz
		val tx_ = tx + x * ty

		val myx_ = y * mxx + myx
		val myy_ = y * mxy + myy
		val myz_ = y * mxz + myz
		val ty_ = y * tx + ty

		mxx = mxx_
		mxy = mxy_
		mxz = mxz_
		myx = myx_
		myy = myy_
		myz = myz_
		tx = tx_
		ty = ty_
	}

	def static shear(Affine it, double x, double y, double pivotX, double pivotY) {
		val mxx_ = mxx + x * myx
		val mxy_ = mxy + x * myy
		val mxz_ = mxz + x * myz
		val tx_ = tx + x * ty - x * pivotY

		val myx_ = y * mxx + myx
		val myy_ = y * mxy + myy
		val myz_ = y * mxz + myz
		val ty_ = y * tx + ty - y * pivotX

		mxx = mxx_
		mxy = mxy_
		mxz = mxz_
		myx = myx_
		myy = myy_
		myz = myz_
		tx = tx_
		ty = ty_
	}
}
