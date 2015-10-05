---
layout: default
title: Concepts
---
# {{page.title}} {#base-abstractions}

This section describes some basic concepts you should be familiar with when developing with FXDiagram.

## Scenegraph

The baseclasses for the nodes of the scenegraph are located in the `de.fxdiagram.core` package. JavaFX is based on abstract classes, so all for the scenegraph inherit from `javafx.scene.Parent`.
A `XDiagram`is a container for `XNodes` and `XConnections`. `XDiagram#nodes` and `XDiagram#connections` are life collections: When an element is added/removed, it is automatically added/removed to/from the JavaFX scenegraph. Diagrams can only be nested when they are contained in nodes (see `OpenableDiagramNode`).

Clients usually implement their own subclasses of `XNode` for custom graphics and behavior. The class hierarchy will point you to a lot of examples.

`XConnections` can either be polylines or quadratic/cubic Bézier splines. The can have `XConnectionLabels` which are by default layouted tangent to the line and a source and target decoration. There is usually no need to extend `XConnection`.

`XShape` is the superclass of `XNode` and `XConnection`. The lifecycle methods of a shape is:

1. Creation by using a constructor.
2. The [domain object](#domain-object) is set.
3. The graphical node is created (`createNode`).
4. The shape is connected to a diagram (`initializeGraphics`).
5. The shape is activated (`activate`) to react to user actions.
6. The shape is removed from the scenegraph.

The diagram is a child of an `XRoot`. It holds an overlay heads up display, the menu or services like the [domain object providers](#domain-object). The root can be directly added to the `javafx.scene.Scene`.


## FxProperty

FXDiagram defines a number of so called *active annotations*, which allow to participate in the Xtend to Java transpilation step. One of these annotations is `@FxProperty` for fields. This will automatically create a getter and setter with the plain Java type as well as a method to access the JavaFX property. For more details have a look at the generated Java code. Examples:

```xtend
@FxProperty double width
@FxProperty String name
@FxProperty Side placementHint
@FxProperty ObservableList<XConnection> connections = FXCollections.observableArrayList
@FxProperty ObservableMap<Node, Pos> fixedButtons = FXCollections.observableMap(newHashMap)
@FxProperty(readOnly=true) boolean isActive
```

Type inference will not work for `@FxProperty` fields.


## Helper classes

The package `de.fxdiagram.core.extensions` contains classes with static helper methods. These are usually imported as `import static extension` and add functionality to JavaFX based classes, e.g. `CoreExtensions` offers methods to navigate to the root or the diagram of a shape.


## Behavior

A `Behavior` is a piece of user interaction provided by an `XShape`. The shape can have any number of behaviors. They are registered in the `doActviate` method. A behavior usually registers event listeners and binds to Java properties.


## Domain Objects {#domain-object}

Both `XNode`and `XConnection` store a reference to a domain object by means of a `DomainObjectDescriptor`. This indirection is necessary for two reasons: It is often not a good idea to store hard references e.g. to EMF or to database objects, and we need a way to persist arbitrary kinds of domain objects when we store the diagram.

To allow the use of domain objects that can only be safely read in a transaction, there is
the `DomainObjectDescriptorImpl.withDomainObject` method. It will open an appropriate transaction execute the given lambda close the transaction and return the result. Whether this method is reentrant or not depends on the specific implementation.

A `DomainObjectProvider` converts between the real domain object and its descriptor. It can store additional information needed to resolve/retrieve the object, such as a database connection or a classpath. Domain object providers are registered to the `XRoot`in order to be resolvable.


## Persistence

FXDiagram does not have its own diagram model. Instead, it uses a reflective method to serialize the JavaFX scenegraph directly in JSON notation. But not every property of a node has to be saved. Many of them can be recovered/calculated from the domain object or some defaults. As users should be able to create arbitrary shapes using the full power of JavaFX, there needs to be a way to tell the system which properties are important for saving.

This is exactly the purpose of the `@ModelNode` annotation. For example the annotation

```xtend
@ModelNode('layoutX', 'layoutY', 'type')
class XControlPoint....
```

tells FXDiagram to save the values of the properties `layoutX`, `layoutY` and `type` for an object of class `XControlPoint` in the scenegraph. By default, all model node properties of the supertype are serialized as well.

Technically, `@ModelNode` is an active annotation for classes that creates a default constructor, adds the interface `XModelProvider` and implements its method `populate()` to populate the model that is saved with the values of the specified properties. If you implement a scenegraph node in Java, you have to do all this by hand.


## Animation Commands

Undo/Redo is implemented by using the command pattern: All modifications are wrapped into command objects that can be executed, undone and redone. These are stored on a command  stack. In FXDiagram the command stack is located in the `XRoot` of the diagram.

What makes FXDiagram different is that it does not just wrap some model changes, but uses a more user-centric approach: When you undo a change, you want to rewind your work to a specific prior state. FXDiagram restores the viewport you had when that change was finished and then uses a smooth transition undoing the modification. The user does not get lost and can easily identify the right point in time.

To implement that, all commands offer three animations (execute, undo, redo). Animations are queued for sequential playback. That means that when you create your own commands, you have to be aware that the current state of the diagram may not be the same as the one when the animation actually starts. Storing and restoring the viewport happens automatically when you inherit from `AbstractAnimationCommand`.

A graphical media player panel allows to undo/redo changes. It can be reached via the context menu or by pressing `CTRL-P` (`CMD-P`on Mac).


## Menu Actions

FXDiagram has a graphical context menu that is activated by right-clicking on an empty space in the diagram. It is populated with `DiagramActions`. Actions can also be triggered by keyboard shortcuts. They are registered by adding them to the `XRoot#getDiagramActionRegistry()`.
