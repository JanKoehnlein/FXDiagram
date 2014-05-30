package de.fxdiagram.core.extensions

import javafx.util.Duration

class DurationExtensions {
	static def hours(double hours) {
		Duration.hours(hours)
	}

	static def minutes(double minutes) {
		Duration.minutes(minutes)
	}

	static def seconds(double seconds) {
		Duration.seconds(seconds)
	}

	static def millis(double millis) {
		Duration.millis(millis)
	}

	static def operator_plus(Duration left, Duration right) {
		left.add(right)
	}

	static def operator_minus(Duration left, Duration right) {
		left.subtract(right)
	}

	static def operator_multiply(Duration left, double right) {
		left.multiply(right)
	}

	static def operator_multiply(double left, Duration right) {
		right.multiply(left)
	}

	static def operator_divide(Duration left, double right) {
		left.divide(right)
	}

	static def operator_divide(double left, Duration right) {
		right.divide(left)
	}
}
