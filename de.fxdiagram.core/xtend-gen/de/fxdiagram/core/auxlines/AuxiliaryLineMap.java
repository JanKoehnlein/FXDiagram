package de.fxdiagram.core.auxlines;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.auxlines.AuxiliaryLine;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class AuxiliaryLineMap<T extends Object> {
  private Multimap<Integer, AuxiliaryLine> store = HashMultimap.<Integer, AuxiliaryLine>create();
  
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
    int _key = this.getKey(_position);
    this.store.put(Integer.valueOf(_key), line);
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
          final int key = this.getKey(_position);
          this.store.remove(Integer.valueOf(key), line);
          _xblockexpression_1 = this.shape2entry.remove(shape);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public Collection<AuxiliaryLine> getByPosition(final double position) {
    int _key = this.getKey(position);
    return this.store.get(Integer.valueOf(_key));
  }
  
  public AuxiliaryLine getByShape(final XShape shape) {
    return this.shape2entry.get(shape);
  }
  
  public boolean containsKey(final double position) {
    int _key = this.getKey(position);
    return this.store.containsKey(Integer.valueOf(_key));
  }
  
  protected int getKey(final double position) {
    return ((int) ((position / this.threshold) + 0.5));
  }
}
