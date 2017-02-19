package de.fxdiagram.core.auxlines;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.auxlines.AuxiliaryLine;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class AuxiliaryLineMap<T extends Object> {
  private Multimap<Long, AuxiliaryLine> store = HashMultimap.<Long, AuxiliaryLine>create();
  
  private Map<XShape, AuxiliaryLine> shape2entry = CollectionLiterals.<XShape, AuxiliaryLine>newHashMap();
  
  private double threshold;
  
  public AuxiliaryLineMap() {
    this(1);
  }
  
  public AuxiliaryLineMap(final double threshold) {
    this.threshold = threshold;
  }
  
  public void add(final AuxiliaryLine line) {
    XShape[] _relatedShapes = line.getRelatedShapes();
    if (((List<XShape>)Conversions.doWrapArray(_relatedShapes))!=null) {
      final Consumer<XShape> _function = (XShape it) -> {
        this.removeByShape(it);
      };
      ((List<XShape>)Conversions.doWrapArray(_relatedShapes)).forEach(_function);
    }
    double _position = line.getPosition();
    long _key = this.getKey(_position);
    this.store.put(Long.valueOf(_key), line);
    XShape[] _relatedShapes_1 = line.getRelatedShapes();
    if (((List<XShape>)Conversions.doWrapArray(_relatedShapes_1))!=null) {
      final Consumer<XShape> _function_1 = (XShape it) -> {
        this.shape2entry.put(it, line);
      };
      ((List<XShape>)Conversions.doWrapArray(_relatedShapes_1)).forEach(_function_1);
    }
  }
  
  public AuxiliaryLine removeByShape(final XShape shape) {
    AuxiliaryLine _xblockexpression = null;
    {
      final AuxiliaryLine line = this.getByShape(shape);
      AuxiliaryLine _xifexpression = null;
      boolean _notEquals = (!Objects.equal(line, null));
      if (_notEquals) {
        AuxiliaryLine _xblockexpression_1 = null;
        {
          double _position = line.getPosition();
          final long key = this.getKey(_position);
          this.store.remove(Long.valueOf(key), line);
          _xblockexpression_1 = this.shape2entry.remove(shape);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public Collection<AuxiliaryLine> getByPosition(final double position) {
    long _key = this.getKey(position);
    return this.store.get(Long.valueOf(_key));
  }
  
  public double getNearestLineDelta(final double position, final double maxDistance, final Set<XShape> excluded) {
    final long key = this.getKey(position);
    final int steps = ((int) (maxDistance / this.threshold));
    final Collection<AuxiliaryLine> at = this.store.get(Long.valueOf(key));
    boolean _containsUnskipped = this.containsUnskipped(at, excluded);
    if (_containsUnskipped) {
      return (position - key);
    }
    for (int i = 1; (i < steps); i++) {
      {
        final Collection<AuxiliaryLine> before = this.store.get(Long.valueOf((key - i)));
        boolean _containsUnskipped_1 = this.containsUnskipped(before, excluded);
        if (_containsUnskipped_1) {
          return ((position - key) - (((double) i) * this.threshold));
        }
        final Collection<AuxiliaryLine> after = this.store.get(Long.valueOf((key + i)));
        boolean _containsUnskipped_2 = this.containsUnskipped(after, excluded);
        if (_containsUnskipped_2) {
          return ((position - key) + (((double) i) * this.threshold));
        }
      }
    }
    return maxDistance;
  }
  
  protected boolean containsUnskipped(final Iterable<AuxiliaryLine> lines, final Set<XShape> excluded) {
    final Function1<AuxiliaryLine, Boolean> _function = (AuxiliaryLine it) -> {
      XShape[] _relatedShapes = it.getRelatedShapes();
      final Function1<XShape, Boolean> _function_1 = (XShape it_1) -> {
        boolean _contains = excluded.contains(it_1);
        return Boolean.valueOf((!_contains));
      };
      return Boolean.valueOf(IterableExtensions.<XShape>exists(((Iterable<XShape>)Conversions.doWrapArray(_relatedShapes)), _function_1));
    };
    return IterableExtensions.<AuxiliaryLine>exists(lines, _function);
  }
  
  public AuxiliaryLine getByShape(final XShape shape) {
    return this.shape2entry.get(shape);
  }
  
  public boolean containsKey(final double position) {
    long _key = this.getKey(position);
    return this.store.containsKey(Long.valueOf(_key));
  }
  
  protected long getKey(final double position) {
    return Math.round((position / this.threshold));
  }
}
