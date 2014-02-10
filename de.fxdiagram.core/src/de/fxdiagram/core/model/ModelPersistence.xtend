package de.fxdiagram.core.model

import java.util.List

class ModelPersistence {
	
	static def findConstructor(Class<?> clazz, List<Class<?>> givenParamTypes) {
		clazz.constructors.findFirst[
			if(givenParamTypes.size == parameterTypes.length) {
				for(i: 0..<parameterTypes.length) {
					if(!parameterTypes.get(i).isAssignableFrom(givenParamTypes.get(i)))	
						return false
				} 
				return true
			} else {
				return false
			}
		]
	}
	
}