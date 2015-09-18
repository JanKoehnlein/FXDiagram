---
layout: default
title: Mapping API
---
# {{page.title}} {#diagram-config}

This section guides you to create a FXDiagram view for your own models using the high-level mapping API. Both, the Eclipse integration and the IDEA integration, offer an extension point to register your mapping configuration and pick it up automatically. That is, in most cases all you have to do to get a working diagram tool is to write a mapping config.


### Diagram Configuration

A diagram configuration serves three purposes:

1. Define how domain objects are mapped to nodes, edges and diagrams.
2. Define on which domain objects a *Show in FXDiagram* action is available.
3. Define how the domain objects can be accessed and serialized.

It is specified in a class extending the `XDiagramConfig` interface. Diagram configs can be most conveniently implemented in Xtend, but Java is of course possible as well. This is how a statemachine diagram configuration could look like

```xtend
class StatemachineDiagramConfig extends AbstractDiagramConfig {
  // fields to define mappings (1)
  val stateNode = new NodeMapping<State>...  
  val stateLabel = new NodeHeadingMapping<State>...
  val transitionConnection = new ConnectionMapping<Transition>...
  val eventLabel = new ConnectionLabelMapping<Event>...

  // method to define entry points (2)
  override protected <ARG> entryCalls...

  // method defining the domain object access (3)
  override protected createDomainObjectProvider...
}
```

For your convenience, there are a bunch of abstract superclasses you can extend for certain use cases, such as Xtext-based models, Eclipse-based models, and IDEA's PSI models.


#### Mappings

A mapping describes how a domain object is mapped to a diagram element. There are four types of mappings: node mapping, connection mapping, label mapping and diagram mapping. A mapping is  implemented as a field of the respective class in the diagram configuration class.

```xtend
val stateNode = new NodeMapping<State>(this, 'stateNode', 'State')
```

The type parameter describes the type of the domain object. The constructor arguments are the configuration this mapping belongs to, an identifier and a human readable name for the mapping.

Most mappings have a `calls()` method that describes what else to create when this mapping is executed. This is usually done by calling another mapping in a certain role for a given single/collection of related domain object/s. e.g.

```xtend
val stateNode = new NodeMapping<State>(...) {
  override calls() {
    transitionConnection.outConnectionForEach[transitions]
    stateLabel.labelFor[it]
  }
```

The connection mapping `transitionConnection` is executed for all `transitions` to create a an outgoing connection from the current node. Note that the `transitions` is inside a lambda expression that is executed with the domain object of the current node, so `[transitions]` is in fact a very sugared Xtend version of `State s -> s.getTransitions()` in Java 8 syntax.

By using `outConnectionForEach` the transition that is created will already have its `source` set. Do not forget to set the `target`:

```xtend
val transitionConnection = new ConnectionMapping<Transition>(...) {
  ...
  override protected calls() {
    stateNode.target[state] // Transition t -> t.getState()
    eventLabel.labelFor[event] 
  }
}
```

Some mapping calls can be further customized, e.g. `ConnectionMappingCall#asButton` to add a popup button that allows the user to decide whether this mapping should be executed, or `DiagramMappingCall#onOpen` for drill-down diagrams. See the example mappings for further details.

When you want to use your own implementation of a node, connection or diagram, you can override the respective `createXXX` method. E.g.

```xtend
val transitionConnection = new ConnectionMapping<Transition>(...) {
  override createConnection(
        IMappedElementDescriptor<Transition> descriptor) {
    // create a connection with a label for the event name
    super.createConnection(descriptor) => [
      new XConnectionLabel(it) => [ label |
        label.text.text = descriptor.withDomainObject[event.name]
      ]
    ]
  }
  ...
}
```

This allows you to add all kind of fancy JavaFX behavior in your nodes and connections. See e.g. the `JvmTypeNode` for some freaky inflate, flip and customization effects.


#### Entry Point

The IDE has to know on what kind of elements the user could execute mappings. This is what the `entryCalls()` method is about. It usually boils down to implement a switch with type guards, i.e.

```xtend
override protected <ARG> entryCalls(
      ARG element,
      extension MappingAcceptor<ARG> acceptor) {
  switch element {
    State: {
      add(stateNode, [element])
      add(statemachineDiagram,
        [element.eContainerOfType(Statemachine)])
    }
  }
}
```

Note that once again, a lambda expression allows you to execute a completely different mapping for some other model element.


#### Registration

Eventually, the runtime infrastructure of your IDE has to pick your configuration mapping up somehow. In Eclipse, diagram configurations are registered to the extension point `de.fxdiagram.mapping.fxDiagramConfig`, e.g.

```xml
<extension point="de.fxdiagram.mapping.fxDiagramConfig">
  <config
    id="de.fxdiagram.xtext.fowlerdsl.StatemachineDiagramConfig"
    label="Statemachine"
    class="...StatemachineDiagramConfig">
  </config>
</extension>
```

in the *plugin.xml* or *fragment.xml*. Note that for Xtext-based models you have to use the extension factory of your language, e.g.

```xml
class="....MyDslExecutableExtensionFactory:....MyDslDiagramConfig"
```

to get all required dependencies injected.

In IDEA, put

```xml
<extensions defaultExtensionNs="de.fxdiagram.idea">
  <fxDiagramConfig implementation="...MyDiagramConfig"/>
</extensions>
```
in your *plugin.xml*.

For standalone Java applications, there is `XDiagramConfig.Registry.instance#addConfig()` but there is no additional tooling picking that up.
