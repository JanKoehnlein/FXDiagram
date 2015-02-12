package de.fxdiagram.examples.slides

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.behavior.CloseBehavior
import de.fxdiagram.core.behavior.NavigationBehavior
import javafx.animation.FadeTransition
import javafx.animation.ParallelTransition
import javafx.collections.ObservableList
import javafx.scene.paint.Color

import static javafx.collections.FXCollections.*

import static extension de.fxdiagram.core.extensions.DurationExtensions.*

@ModelNode('slides')
class SlideDiagram extends XDiagram {
	
	@FxProperty ObservableList<Slide> slides = observableArrayList
	
	@FxProperty Slide currentSlide

	new() {}

	def operator_add(Slide slide) {
		slides.add(slide)
	}
	
	override doActivate() {
		super.doActivate()
		backgroundPaint = Color.BLACK
		addBehavior(new SlideNavigation(this))
		showSlide(slides.head)
	}
	
	def boolean next() {
		val newSlide = if(currentSlide != null) {
				val nextIndex = slides.indexOf(currentSlide) + 1
				if(nextIndex == slides.size) {
					getBehavior(CloseBehavior)?.close					
					return false
				}
				slides.get(nextIndex)
			}
			else 			
				slides.head
		showSlide(newSlide)
		true
	}
	
	def boolean previous() {
		val slides = slides
		val newSlide = if(currentSlide != null) {
				val previousIndex = slides.indexOf(currentSlide) - 1
				if(previousIndex < 0) {
					getBehavior(CloseBehavior)?.close					
					return false
				}
				slides.get(previousIndex)
			}
			else 			
				slides.head
		showSlide(newSlide)
		true
	}
	
	protected def showSlide(Slide newSlide) {
		val oldSlide = currentSlide
		val fade = if(oldSlide != null) {
						new FadeTransition => [
							node = oldSlide
							duration = 200.millis
							fromValue = 1
							toValue = 0
							onFinished = [ nodes -= oldSlide ]
						]
					} else null
		val appear = if(newSlide != null) {
						nodes += newSlide
						newSlide.selected = true
						new FadeTransition => [
							node = newSlide
							duration = 200.millis
							fromValue = 0
							toValue = 1
						]
					} else null
		if(fade != null && appear != null) {
			new ParallelTransition => [
				children += fade
				children += appear
				play
			]				
		} else {
			fade?.play
			appear?.play
		}
		currentSlide = newSlide
	}
}

class SlideNavigation extends AbstractHostBehavior<SlideDiagram> implements NavigationBehavior {
	
	new(SlideDiagram host) {
		super(host)
	}
	
	override protected doActivate() {
	}
	
	override getBehaviorKey() {
		NavigationBehavior
	}
	
	override next() {
		host.next
	}
	
	override previous() {
		host.previous	
	}
	
}