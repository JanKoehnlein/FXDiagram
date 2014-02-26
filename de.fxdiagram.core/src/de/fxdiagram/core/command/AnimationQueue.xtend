package de.fxdiagram.core.command

import java.util.Queue
import javafx.animation.Animation
import javafx.animation.SequentialTransition

class AnimationQueue {
	
	Queue<()=>Animation> queue = newLinkedList
	
	def enqueue(()=>Animation animationFactory) {
		if(animationFactory != null) {
			val isEmpty = queue.empty
			queue.add(animationFactory)
			if(isEmpty) 
				executeNext
		}
	}

	protected def void executeNext() {
		val next = queue.peek
		if(next != null) {
			val animation = next.apply
			if(animation != null) {
				new SequentialTransition => [
					children += animation
					onFinished = [
						queue.poll
						executeNext
					]
					play
				]
			} else {
				queue.poll
				executeNext
			} 
		}
	}
}