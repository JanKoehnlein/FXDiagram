package de.itemis.javafx.diagram.transform

import org.junit.Test
import javafx.scene.transform.Translate
import javafx.scene.transform.Rotate
import javafx.geometry.Point3D
import javafx.scene.transform.Scale
import javafx.scene.transform.Shear
import static extension de.itemis.javafx.diagram.transform.TransformExtensions.*
import static org.junit.Assert.*
import javafx.scene.transform.Affine

class TransformExtensionsTest {
	@Test
	def test0() {
		val matrices = newArrayList(
			new Translate(1,2), new Translate(1,2,3),
			new Rotate(90), new Rotate(5,6,7), new Rotate(8,9,10,11), 
			new Rotate(12,13,14,15, new Point3D(16,17,18)), new Rotate(19, new Point3D(20,21,22)),
			new Scale(23,24), new Scale(25,26,27), new Scale(28,29,30,31), new Scale(32,33,34,35,36,37),
			new Shear(38,39), new Shear(40,41)
		)
		val operations = <(Affine)=>void> newArrayList(
			[translate(1,2)], [translate(1,2,3)],
			[rotate(90)], [rotate(5,6,7)], [rotate(8,9,10,11)], 
			[rotate(12,13,14,15, new Point3D(16,17,18))], [rotate(19, new Point3D(20,21,22))],
			[scale(23,24)], [scale(25,26,27)], [scale(28,29,30,31)], [scale(32,33,34,35,36,37)],
			[shear(38,39)], [shear(40,41)]
		)
		val vectors = newArrayList(
			new Point3D(1,0,0), new Point3D(0,1,0), new Point3D(0,0,1),
			new Point3D(0,1,1), new Point3D(1,0,1), new Point3D(1,1,0)
		)
		for(l: matrices) {
			for(r: matrices) {
				val affine = new Affine()
				(affine).leftMultiply(r)
				affine => operations.get(matrices.indexOf(l))
				for(v: vectors) {
			 		assertTrue((l*(r*v)).distance((l*r)*v) < 1e-10)
			 		assertTrue((affine*v).distance(l*r*v) < 1e-10)
				}
			}
		}
	}
}