package de.fxdiagram.annotations.properties;

import org.eclipse.xtend.lib.Data;
import org.eclipse.xtend.lib.macro.declaration.MemberDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class PropertyAccessor {
  private final MemberDeclaration _member;
  
  public MemberDeclaration getMember() {
    return this._member;
  }
  
  private final TypeReference _componentType;
  
  public TypeReference getComponentType() {
    return this._componentType;
  }
  
  public String asCall() {
    MemberDeclaration _member = this.getMember();
    String _simpleName = _member.getSimpleName();
    String _xifexpression = null;
    MemberDeclaration _member_1 = this.getMember();
    if ((_member_1 instanceof MethodDeclaration)) {
      _xifexpression = "()";
    } else {
      _xifexpression = "";
    }
    return (_simpleName + _xifexpression);
  }
  
  public PropertyAccessor(final MemberDeclaration member, final TypeReference componentType) {
    super();
    this._member = member;
    this._componentType = componentType;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_member== null) ? 0 : _member.hashCode());
    result = prime * result + ((_componentType== null) ? 0 : _componentType.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PropertyAccessor other = (PropertyAccessor) obj;
    if (_member == null) {
      if (other._member != null)
        return false;
    } else if (!_member.equals(other._member))
      return false;
    if (_componentType == null) {
      if (other._componentType != null)
        return false;
    } else if (!_componentType.equals(other._componentType))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
