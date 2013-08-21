package de.fxdiagram.core.extensions

import javafx.beans.binding.Bindings
import javafx.beans.binding.StringExpression

class StringExpressionExtensions {
	
	def static StringExpression operator_plus(StringExpression left, Object right) {
		return Bindings.concat(left, right)
	}

	def static StringExpression operator_plus(Object left, StringExpression right) {
		return Bindings.concat(left, right)
	}
}