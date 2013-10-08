package de.fxdiagram.core.extensions

import javafx.beans.binding.Bindings
import javafx.beans.binding.StringExpression

class StringExpressionExtensions {
	
	def static operator_plus(StringExpression left, String right) {
		return Bindings.concat(left, right)
	}

	def static operator_plus(String left, StringExpression right) {
		return Bindings.concat(left, right)
	}

	def static operator_plus(StringExpression left, Object right) {
		return Bindings.concat(left, right)
	}

	def static operator_plus(Object left, StringExpression right) {
		return Bindings.concat(left, right)
	}

	def static operator_plus(StringExpression left, StringExpression right) {
		return Bindings.concat(left, right)
	}

	def static operator_equals(StringExpression left, StringExpression right) {
		left.value == right.value
	}

	def static operator_equals(StringExpression left, String right) {
		left.value == right
	}

	def static operator_notEquals(StringExpression left, StringExpression right) {
		left.value != right.value
	}

	def static operator_notEquals(StringExpression left, String right) {
		left.value != right
	}

}