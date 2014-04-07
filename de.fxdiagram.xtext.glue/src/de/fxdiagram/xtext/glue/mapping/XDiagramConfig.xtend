package de.fxdiagram.xtext.glue.mapping

import java.util.List

interface XDiagramConfig {

	def <T> List<? extends BaseMapping<T>> getMappings(T domainObject)
}