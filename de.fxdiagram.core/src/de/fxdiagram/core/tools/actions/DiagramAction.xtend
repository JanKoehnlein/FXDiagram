package de.fxdiagram.core.tools.actions

import com.google.common.collect.Lists
import de.fxdiagram.core.XRoot
import eu.hansolo.enzo.radialmenu.Symbol
import java.util.List
import java.util.Map
import javafx.scene.input.KeyEvent

/**
 * Some action that can happen on an {@link XDiagram}.
 * 
 * Usually triggered by the diagram's menu or a keystroke.
 */
interface DiagramAction {
	
	def boolean matches(KeyEvent event) 
	
	def Symbol.Type getSymbol() 
	
	def String getTooltip()
	
	def void perform(XRoot root)
}

class DiagramActionRegistry {
	
	List<DiagramAction> actions = newArrayList 
	
	Map<Symbol.Type, DiagramAction> symbol2action = newHashMap 
	
	def void operator_add(Iterable<? extends DiagramAction> diagramActions) {
		diagramActions.forEach[this += it]
	} 
	
	def void operator_add(DiagramAction diagramAction) {
		actions += diagramAction	
		if(diagramAction.symbol != null) 
			symbol2action.put(diagramAction.symbol, diagramAction)
	} 
	
	def void operator_remove(DiagramAction diagramAction) {
		actions -= diagramAction	
		if(diagramAction.symbol != null) 
			symbol2action.remove(diagramAction.symbol)
	} 
	
	def DiagramAction getBySymbol(Symbol.Type symbol) {
		symbol2action.get(symbol)
	}
	
	def getActions() {
		Lists.newArrayList(actions)
	}
}