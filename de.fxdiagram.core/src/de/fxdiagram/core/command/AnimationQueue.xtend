package de.fxdiagram.core.command

import java.util.List
import java.util.Queue
import javafx.animation.Animation
import javafx.animation.SequentialTransition
import com.google.common.collect.Lists

class AnimationQueue {
	
	Queue<()=>Animation> queue = newLinkedList
	
	List<AnimationQueueListener> listeners = newArrayList
	
	def void addListener(AnimationQueueListener listener) {
		listeners.add(listener)
	}
	
	def void removeListener(AnimationQueueListener listener) {
		listeners.remove(listener)
	}
	
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
		} else {
			Lists.newArrayList(listeners).forEach[handleQueueEmpty]
		}
	}
}

interface AnimationQueueListener {
	def void handleQueueEmpty()
}