package de.fxdiagram.examples.slides

import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.behavior.NavigationBehavior

class RevealBehavior extends AbstractHostBehavior<Slide> implements NavigationBehavior {
	
	new(Slide host) {
		super(host)
	}
	
	override protected doActivate() {
	}
	
	override getBehaviorKey() {
		NavigationBehavior
	}
	
	override next() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override previous() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}