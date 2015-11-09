package de.fxdiagram.mapping.reconcile

import de.fxdiagram.core.XDomainObjectShape
import de.fxdiagram.core.XLabel
import de.fxdiagram.core.behavior.AbstractReconcileBehavior
import de.fxdiagram.core.behavior.DirtyState
import de.fxdiagram.core.behavior.ReconcileBehavior
import de.fxdiagram.mapping.AbstractLabelMappingCall
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter
import de.fxdiagram.mapping.reconcile.AbstractLabelOwnerReconcileBehavior.AddKeepRemoveAcceptor
import java.util.NoSuchElementException
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static de.fxdiagram.core.behavior.DirtyState.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

abstract class AbstractLabelOwnerReconcileBehavior<T, SHAPE extends XDomainObjectShape> extends AbstractReconcileBehavior<SHAPE> {

	@Accessors(PROTECTED_GETTER)
	val interpreter = new XDiagramConfigInterpreter

	new(SHAPE host) {
		super(host)
	}

	override DirtyState getDirtyState() {
		if (host.domainObjectDescriptor instanceof IMappedElementDescriptor<?>) {
			try {
				val descriptor = host.domainObjectDescriptor as IMappedElementDescriptor<T>
				return descriptor.withDomainObject [
					descriptor.mapping.getLabelsDirtyState(it)
				]
			} catch (NoSuchElementException e) {
				return DANGLING
			}
		}
		return CLEAN
	}

	protected def getLabelsDirtyState(AbstractMapping<T> mapping, T domainObject) {
		val toBeAdded = newArrayList
		compareLabels(mapping, domainObject, new AddKeepRemoveAcceptor {
			override add(XLabel label) {
				toBeAdded.add(label)
			}

			override remove(XLabel label) {
			}

			override keep(XLabel label) {
			}
		})
		if (!toBeAdded.empty)
			return DIRTY
		else
			return CLEAN
	}

	override showDirtyState(DirtyState dirtyState) {
		super.showDirtyState(dirtyState)
		existingLabels.forEach [
			if (host.domainObjectDescriptor instanceof IMappedElementDescriptor<?>) {
				try {
					val descriptor = host.domainObjectDescriptor as IMappedElementDescriptor<T>
					descriptor.withDomainObject [ domainObject |
						compareLabels(descriptor.mapping, domainObject, new AddKeepRemoveAcceptor {
							override add(XLabel label) {
								AbstractLabelOwnerReconcileBehavior.super.showDirtyState(DIRTY)
							}

							override remove(XLabel label) {
								val behavior = label.getBehavior(ReconcileBehavior)
								if (behavior != null) {
									switch behavior.dirtyState {
										case DANGLING:
											behavior.showDirtyState(DANGLING)
										default:
											behavior.showDirtyState(DIRTY)
									}
								}
							}

							override keep(XLabel label) {
								label.getBehavior(ReconcileBehavior)?.showDirtyState(CLEAN)
							}
						})
						null
					]
				} catch (NoSuchElementException e) {
					// ignore
				}
			}
		]
	}

	override hideDirtyState() {
		super.hideDirtyState()
		existingLabels.forEach [
			getBehavior(ReconcileBehavior)?.hideDirtyState
		]
	}

	override reconcile(UpdateAcceptor acceptor) {
		if (host.domainObjectDescriptor instanceof IMappedElementDescriptor<?>) {
			try {
				val descriptor = host.domainObjectDescriptor as IMappedElementDescriptor<T>
				descriptor.withDomainObject [ domainObject |
					reconcile(descriptor.mapping, domainObject, acceptor)
					null
				]
			} catch (NoSuchElementException exc) {
				acceptor.delete(host, host.diagram)
			}
		} 
	}

	protected def compareLabels(AbstractMapping<T> mapping, T domainObject, AddKeepRemoveAcceptor acceptor) {
		val existingLabels = getExistingLabels
			.map[createLabelEntry]
			.filterNull
			.toMap[it]
		val resolvedLabels = mapping
			.labelMappingCalls
			.map[interpreter.execute(it as AbstractLabelMappingCall<?, T>, domainObject)]
			.flatten
			.map[createLabelEntry]
			.filterNull
		resolvedLabels.forEach [ resolved, i |
			val existing = existingLabels.get(resolved)
			if (existing == null) {
				acceptor.add(resolved.label)
			} else {
				existingLabels.remove(existing)
				acceptor.keep(existing.label)
			}
		]
		existingLabels.keySet.forEach [ acceptor.remove(label) ]
	}
	
	protected def createLabelEntry(XLabel label) {
		if(label == null)
			null
		else
			new LabelEntry(label)
	}
	
	@FinalFieldsConstructor
	static class LabelEntry {
		val XLabel label
		
		override equals(Object obj) {
			if(obj instanceof LabelEntry) 
				return obj.label.domainObjectDescriptor == label.domainObjectDescriptor
					&& obj.label.text.text == label.text.text 
			else
				return false
		}
		
		override hashCode() {
			label.domainObjectDescriptor?.hashCode + 37 * label.text.text.hashCode 
		}
	}

	protected def Iterable<? extends XLabel> getExistingLabels()

	protected def Iterable<? extends AbstractLabelMappingCall<?, T>> getLabelMappingCalls(AbstractMapping<T> mapping)

	protected def void reconcile(AbstractMapping<T> mapping, T domainObject, UpdateAcceptor acceptor) 

	static interface AddKeepRemoveAcceptor {
		def void add(XLabel label)
		def void keep(XLabel label)
		def void remove(XLabel label)
	}
}


