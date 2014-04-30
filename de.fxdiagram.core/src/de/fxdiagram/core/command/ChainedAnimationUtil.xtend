package de.fxdiagram.core.command

import java.util.Iterator
import javafx.animation.Animation
import javafx.animation.SequentialTransition

class ChainedAnimationUtil {
	
	static def <T> Animation createChainedAnimation(Iterable<T> iterable, (T)=>Animation animationFactory) {
		createChainedAnimation(iterable.iterator, animationFactory)
	}
	
	protected static def <T> Animation createChainedAnimation(Iterator<T> iterator, (T)=>Animation animationFactory) {
		if(!iterator.hasNext)
			return null
		val nextAnimation = animationFactory.apply(iterator.next())
		if(nextAnimation == null) 
			return createChainedAnimation(iterator, animationFactory)	
		return new SequentialTransition => [
			children += nextAnimation
			onFinished = [
				createChainedAnimation(iterator, animationFactory)?.playFromStart
			]
		]
	}
}