---
layout: default
title: Introduction
---
# {{page.title}} {#introduction}

FXDiagram is a framework for creating graph diagrams (nodes and edges) based on JavaFX. It is open source and licensed under the [Eclipse Public License 1.0](https://www.eclipse.org/legal/epl-v10.html).


## Mission Statement

These are the values that guide me in the development of FXDiagram:

- **Focus on user experience:** No surprises. Use modern visuals and real-life metaphors fot the most intuitive behavior. No flickering, guide the user's eye with short transitions instead. 
- **Target the real use cases:** Choose diagram contents, layout elements, drill-down, produce scalable pictures.
- **Skip the bogus use cases:** Diagrams with more than 100 nodes and thousands of connections  which are unreadable anyway. Resizable nodes that only necessary when the automatic layout is broken.
- **Stay graphical:** Avoid dialogs, property panes etc at all cost. Use the maximum amount of screen space for the diagram.
- **Do not hide JavaFX:** Allow the developer to leverage JavaFX as much as possible. Extend existing JavaFX base classes. Don't enforce additional abstractions.
- **Avoid model reconciliation:** Synching changes between different models is a source of eternal trouble. Store selective properties of the scene graph instead of an additional diagram model. Focus on graphical model views that do not change the underlying domain model.
- **Allow various kinds of domain models:** Believe it or not, many models out there are not based on EMF. Use a Java based domain model abstraction and allow to customize transactionality.


## A Short History

Having worked in the area of modeling tools for several years, I noticed a certain stagnation in the area of graphical editing frameworks.

In the mid 2000s, the advent of tablets and smartphones has changed the way user interfaces of applications are designed significantly. They not only allow new ways of interaction, they also have to be self-explanatory as they are focussing a broader audience. Unfortunately, none of the new ideas was ever picked up by the major diagram frameworks. Many contemporary diagram tools offer a user experience that has not changed since the late 90s.

Even worse, more and more higher-level abstractions were built on top of stale base frameworks in order to get graphical editors with even more features faster allegedly easier. From my experience, every customer for diagram editors had a very strong and unique opinion on how the tool should behave. But the frameworks were not really built for easy extensibility. As an effect the amount of work on customization grew enormously, while the reachable result was not even close to the expectation.

JavaFX 2 was a game changer. For the first time it was relatively easy for Java developers (and as such in a portable way) to create modern UIs, including all kinds of animations and effects but still fast and responsive because it used hardware accelleration. But of course JavaFX is not a diagram framework. Something was missing. Into the bargain, creating JavaFX applications with Java 7 was a bit bulky.

As a committer to the Xtend language, I decided to take it for a spin and implement a thin diagram layer on top of JavaFX using Xtend. This combination was so efficient that I had a cool looking demo within days. I presented my first prototype at EclipseCon Europe 2013:  It already included multi-touch support, spline connections, animated drill-down, graphical choice of content, a discovery diagram for Ecore models and a StarTrek-themed demo. It had been developed during 10 months mostly in my spare time.

FXDiagram has grown ever since. It got animated undo/redo, diagram persistance, a high-level mapping API, integration for Xtext, Eclipse and IDEA and much more.
