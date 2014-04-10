package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.NodeMapping;

@SuppressWarnings("all")
public abstract class AbstractNodeMappingCall<T extends Object, ARG extends Object> {
  public abstract NodeMapping<T> getNodeMapping();
}
