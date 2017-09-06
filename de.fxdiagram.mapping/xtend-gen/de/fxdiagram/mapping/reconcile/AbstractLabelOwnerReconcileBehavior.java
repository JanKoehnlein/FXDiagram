package de.fxdiagram.mapping.reconcile;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.behavior.AbstractReconcileBehavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.ReconcileBehavior;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractLabelMappingCall;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public abstract class AbstractLabelOwnerReconcileBehavior<T extends Object, SHAPE extends XDomainObjectShape> extends AbstractReconcileBehavior<SHAPE> {
  @FinalFieldsConstructor
  public static class LabelEntry {
    private final XLabel label;
    
    @Override
    public boolean equals(final Object obj) {
      if ((obj instanceof AbstractLabelOwnerReconcileBehavior.LabelEntry)) {
        return (Objects.equal(((AbstractLabelOwnerReconcileBehavior.LabelEntry)obj).label.getDomainObjectDescriptor(), this.label.getDomainObjectDescriptor()) && Objects.equal(((AbstractLabelOwnerReconcileBehavior.LabelEntry)obj).label.getText().getText(), this.label.getText().getText()));
      } else {
        return false;
      }
    }
    
    @Override
    public int hashCode() {
      DomainObjectDescriptor _domainObjectDescriptor = this.label.getDomainObjectDescriptor();
      int _hashCode = 0;
      if (_domainObjectDescriptor!=null) {
        _hashCode=_domainObjectDescriptor.hashCode();
      }
      int _hashCode_1 = this.label.getText().getText().hashCode();
      int _multiply = (37 * _hashCode_1);
      return (_hashCode + _multiply);
    }
    
    public LabelEntry(final XLabel label) {
      super();
      this.label = label;
    }
  }
  
  public interface AddKeepRemoveAcceptor {
    public abstract void add(final XLabel label);
    
    public abstract void keep(final XLabel label);
    
    public abstract void remove(final XLabel label);
  }
  
  @Accessors(AccessorType.PROTECTED_GETTER)
  private final XDiagramConfigInterpreter interpreter = new XDiagramConfigInterpreter();
  
  public AbstractLabelOwnerReconcileBehavior(final SHAPE host) {
    super(host);
  }
  
  @Override
  public DirtyState getDirtyState() {
    DomainObjectDescriptor _domainObjectDescriptor = this.getHost().getDomainObjectDescriptor();
    if ((_domainObjectDescriptor instanceof IMappedElementDescriptor<?>)) {
      try {
        DomainObjectDescriptor _domainObjectDescriptor_1 = this.getHost().getDomainObjectDescriptor();
        final IMappedElementDescriptor<T> descriptor = ((IMappedElementDescriptor<T>) _domainObjectDescriptor_1);
        final Function1<T, DirtyState> _function = (T it) -> {
          return this.getLabelsDirtyState(descriptor.getMapping(), it);
        };
        return descriptor.<DirtyState>withDomainObject(_function);
      } catch (final Throwable _t) {
        if (_t instanceof NoSuchElementException) {
          final NoSuchElementException e = (NoSuchElementException)_t;
          return DirtyState.DANGLING;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    return DirtyState.CLEAN;
  }
  
  protected DirtyState getLabelsDirtyState(final AbstractMapping<T> mapping, final T domainObject) {
    final ArrayList<XLabel> toBeAdded = CollectionLiterals.<XLabel>newArrayList();
    this.compareLabels(mapping, domainObject, new AbstractLabelOwnerReconcileBehavior.AddKeepRemoveAcceptor() {
      @Override
      public void add(final XLabel label) {
        toBeAdded.add(label);
      }
      
      @Override
      public void remove(final XLabel label) {
      }
      
      @Override
      public void keep(final XLabel label) {
      }
    });
    boolean _isEmpty = toBeAdded.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      return DirtyState.DIRTY;
    } else {
      return DirtyState.CLEAN;
    }
  }
  
  @Override
  public void showDirtyState(final DirtyState dirtyState) {
    super.showDirtyState(dirtyState);
    final Consumer<XLabel> _function = (XLabel it) -> {
      DomainObjectDescriptor _domainObjectDescriptor = this.getHost().getDomainObjectDescriptor();
      if ((_domainObjectDescriptor instanceof IMappedElementDescriptor<?>)) {
        try {
          DomainObjectDescriptor _domainObjectDescriptor_1 = this.getHost().getDomainObjectDescriptor();
          final IMappedElementDescriptor<T> descriptor = ((IMappedElementDescriptor<T>) _domainObjectDescriptor_1);
          final Function1<T, Object> _function_1 = (T domainObject) -> {
            Object _xblockexpression = null;
            {
              AbstractMapping<T> _mapping = descriptor.getMapping();
              this.compareLabels(_mapping, domainObject, new AbstractLabelOwnerReconcileBehavior.AddKeepRemoveAcceptor() {
                @Override
                public void add(final XLabel label) {
                  AbstractLabelOwnerReconcileBehavior.super.showDirtyState(DirtyState.DIRTY);
                }
                
                @Override
                public void remove(final XLabel label) {
                  final ReconcileBehavior behavior = label.<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
                  boolean _notEquals = (!Objects.equal(behavior, null));
                  if (_notEquals) {
                    DirtyState _dirtyState = behavior.getDirtyState();
                    if (_dirtyState != null) {
                      switch (_dirtyState) {
                        case DANGLING:
                          behavior.showDirtyState(DirtyState.DANGLING);
                          break;
                        default:
                          behavior.showDirtyState(DirtyState.DIRTY);
                          break;
                      }
                    } else {
                      behavior.showDirtyState(DirtyState.DIRTY);
                    }
                  }
                }
                
                @Override
                public void keep(final XLabel label) {
                  ReconcileBehavior _behavior = label.<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
                  if (_behavior!=null) {
                    _behavior.showDirtyState(DirtyState.CLEAN);
                  }
                }
              });
              _xblockexpression = null;
            }
            return _xblockexpression;
          };
          descriptor.<Object>withDomainObject(_function_1);
        } catch (final Throwable _t) {
          if (_t instanceof NoSuchElementException) {
            final NoSuchElementException e = (NoSuchElementException)_t;
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      }
    };
    this.getExistingLabels().forEach(_function);
  }
  
  @Override
  public void hideDirtyState() {
    super.hideDirtyState();
    final Consumer<XLabel> _function = (XLabel it) -> {
      ReconcileBehavior _behavior = it.<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
      if (_behavior!=null) {
        _behavior.hideDirtyState();
      }
    };
    this.getExistingLabels().forEach(_function);
  }
  
  @Override
  public void reconcile(final ReconcileBehavior.UpdateAcceptor acceptor) {
    DomainObjectDescriptor _domainObjectDescriptor = this.getHost().getDomainObjectDescriptor();
    if ((_domainObjectDescriptor instanceof IMappedElementDescriptor<?>)) {
      try {
        DomainObjectDescriptor _domainObjectDescriptor_1 = this.getHost().getDomainObjectDescriptor();
        final IMappedElementDescriptor<T> descriptor = ((IMappedElementDescriptor<T>) _domainObjectDescriptor_1);
        final Function1<T, Object> _function = (T domainObject) -> {
          Object _xblockexpression = null;
          {
            this.reconcile(descriptor.getMapping(), domainObject, acceptor);
            _xblockexpression = null;
          }
          return _xblockexpression;
        };
        descriptor.<Object>withDomainObject(_function);
      } catch (final Throwable _t) {
        if (_t instanceof NoSuchElementException) {
          final NoSuchElementException exc = (NoSuchElementException)_t;
          acceptor.delete(this.getHost(), CoreExtensions.getDiagram(this.getHost()));
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
  }
  
  protected void compareLabels(final AbstractMapping<T> mapping, final T domainObject, final AbstractLabelOwnerReconcileBehavior.AddKeepRemoveAcceptor acceptor) {
    final Function1<XLabel, AbstractLabelOwnerReconcileBehavior.LabelEntry> _function = (XLabel it) -> {
      return this.createLabelEntry(it);
    };
    final Function1<AbstractLabelOwnerReconcileBehavior.LabelEntry, AbstractLabelOwnerReconcileBehavior.LabelEntry> _function_1 = (AbstractLabelOwnerReconcileBehavior.LabelEntry it) -> {
      return it;
    };
    final Map<AbstractLabelOwnerReconcileBehavior.LabelEntry, AbstractLabelOwnerReconcileBehavior.LabelEntry> existingLabels = IterableExtensions.<AbstractLabelOwnerReconcileBehavior.LabelEntry, AbstractLabelOwnerReconcileBehavior.LabelEntry>toMap(IterableExtensions.<AbstractLabelOwnerReconcileBehavior.LabelEntry>filterNull(IterableExtensions.map(this.getExistingLabels(), _function)), _function_1);
    final Function1<AbstractLabelMappingCall<?, T>, Iterable<? extends XLabel>> _function_2 = (AbstractLabelMappingCall<?, T> it) -> {
      return this.interpreter.execute(((AbstractLabelMappingCall<?, T>) it), domainObject);
    };
    final Function1<XLabel, AbstractLabelOwnerReconcileBehavior.LabelEntry> _function_3 = (XLabel it) -> {
      return this.createLabelEntry(it);
    };
    final Iterable<AbstractLabelOwnerReconcileBehavior.LabelEntry> resolvedLabels = IterableExtensions.<AbstractLabelOwnerReconcileBehavior.LabelEntry>filterNull(IterableExtensions.<XLabel, AbstractLabelOwnerReconcileBehavior.LabelEntry>map(Iterables.<XLabel>concat(IterableExtensions.map(this.getLabelMappingCalls(mapping), _function_2)), _function_3));
    final Procedure2<AbstractLabelOwnerReconcileBehavior.LabelEntry, Integer> _function_4 = (AbstractLabelOwnerReconcileBehavior.LabelEntry resolved, Integer i) -> {
      final AbstractLabelOwnerReconcileBehavior.LabelEntry existing = existingLabels.get(resolved);
      boolean _equals = Objects.equal(existing, null);
      if (_equals) {
        acceptor.add(resolved.label);
      } else {
        existingLabels.remove(existing);
        acceptor.keep(existing.label);
      }
    };
    IterableExtensions.<AbstractLabelOwnerReconcileBehavior.LabelEntry>forEach(resolvedLabels, _function_4);
    final Consumer<AbstractLabelOwnerReconcileBehavior.LabelEntry> _function_5 = (AbstractLabelOwnerReconcileBehavior.LabelEntry it) -> {
      acceptor.remove(it.label);
    };
    existingLabels.keySet().forEach(_function_5);
  }
  
  protected AbstractLabelOwnerReconcileBehavior.LabelEntry createLabelEntry(final XLabel label) {
    AbstractLabelOwnerReconcileBehavior.LabelEntry _xifexpression = null;
    boolean _equals = Objects.equal(label, null);
    if (_equals) {
      _xifexpression = null;
    } else {
      _xifexpression = new AbstractLabelOwnerReconcileBehavior.LabelEntry(label);
    }
    return _xifexpression;
  }
  
  protected abstract Iterable<? extends XLabel> getExistingLabels();
  
  protected abstract Iterable<? extends AbstractLabelMappingCall<?, T>> getLabelMappingCalls(final AbstractMapping<T> mapping);
  
  protected abstract void reconcile(final AbstractMapping<T> mapping, final T domainObject, final ReconcileBehavior.UpdateAcceptor acceptor);
  
  @Pure
  protected XDiagramConfigInterpreter getInterpreter() {
    return this.interpreter;
  }
}
