package com.cedarsoft.codegen.model.test;

/**
 *
 */
public class Door {
  private final String description;

  public Door( String description ) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Door ) ) return false;

    Door door = ( Door ) o;

    if ( description != null ? !description.equals( door.description ) : door.description != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return description != null ? description.hashCode() : 0;
  }
}
