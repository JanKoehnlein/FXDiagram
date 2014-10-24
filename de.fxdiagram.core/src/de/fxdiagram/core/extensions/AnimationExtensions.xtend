package de.fxdiagram.core.extensions

import javafx.animation.Animation

class AnimationExtensions {
	
	static def chain(()=>Animation first, ()=>Animation second) {
		return [|
			val animation = first.apply
			val orig = animation.onFinished
			animation.onFinished = [
				orig.handle(it)
				second.apply().play
			]  
			animation
		]
	}
}