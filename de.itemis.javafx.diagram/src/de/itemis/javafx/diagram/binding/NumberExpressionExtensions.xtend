package de.itemis.javafx.diagram.binding

import org.eclipse.xtext.xbase.lib.Pure
import javafx.beans.binding.NumberExpressionBase
import javafx.beans.value.ObservableNumberValue

class NumberExpressionExtensions {
		
	static double EPSILON = 1e-9
	
	@Pure
	def static operator_lessThan(NumberExpressionBase a, ObservableNumberValue b) {
		a.lessThan(b)
	}
	
	@Pure
	def static operator_lessEqualsThan(NumberExpressionBase a, ObservableNumberValue b) {
		a.lessThanOrEqualTo(b)
	}
	
	@Pure
	def static operator_greaterThan(NumberExpressionBase a, ObservableNumberValue b) {
		a.greaterThan(b)
	}
	
	@Pure
	def static operator_greaterEqualsThan(NumberExpressionBase a, ObservableNumberValue b) {
		a.greaterThanOrEqualTo(b)
	}
	
	@Pure
	def static operator_equals(NumberExpressionBase a, ObservableNumberValue b) {
		a.isEqualTo(b)
	}
	
	@Pure
	def static operator_notEquals(NumberExpressionBase a, ObservableNumberValue b) {
		a.isNotEqualTo(b)
	}
	
	
	@Pure
	def static operator_lessThan(double a, NumberExpressionBase b) {
		new DoubleConstant(a).lessThan(b)
	}
	
	@Pure
	def static operator_lessEqualsThan(double a, NumberExpressionBase b) {
		new DoubleConstant(a).lessThanOrEqualTo(b)
	}
	
	@Pure
	def static operator_greaterThan(double a, NumberExpressionBase b) {
		new DoubleConstant(a).greaterThan(b)
	}
	
	@Pure
	def static operator_greaterEqualsThan(double a, NumberExpressionBase b) {
		new DoubleConstant(a).greaterThanOrEqualTo(b)
	}
	
	@Pure
	def static operator_equals(double a, NumberExpressionBase b) {
		new DoubleConstant(a).isEqualTo(b)
	}
	
	@Pure
	def static operator_notEquals(double a, NumberExpressionBase b) {
		new DoubleConstant(a).isNotEqualTo(b)
	}
	
	
	@Pure
	def static operator_lessThan(NumberExpressionBase a, double b) {
		a.lessThan(b)
	}
	
	@Pure
	def static operator_lessEqualsThan(NumberExpressionBase a, double b) {
		a.lessThanOrEqualTo(b)
	}
	
	@Pure
	def static operator_greaterThan(NumberExpressionBase a, double b) {
		a.greaterThan(b)
	}
	
	@Pure
	def static operator_greaterEqualsThan(NumberExpressionBase a, double b) {
		a.greaterThanOrEqualTo(b)
	}
	
	@Pure
	def static operator_equals(NumberExpressionBase a, double b) {
		a.isEqualTo(b, EPSILON)
	}
	
	@Pure
	def static operator_notEquals(NumberExpressionBase a, double b) {
		a.isNotEqualTo(b, EPSILON)
	}
	
	
}