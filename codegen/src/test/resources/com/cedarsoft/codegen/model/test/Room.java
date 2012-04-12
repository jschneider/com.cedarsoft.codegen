package com.cedarsoft.codegen.model.test;


import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Room {
  @Nonnull
  private final String description;

  @Nonnull
  private final List<Window> windows = new ArrayList<Window>();

  @Nonnull
  private final List<Door> doors = new ArrayList<Door>();

  public Room( @Nonnull String description ) {
    this.description = description;
  }

  public Room( @Nonnull String description, Collection<? extends Window> windows, Collection<? extends Door> doors ) {
    this.description = description;
    this.windows.addAll( windows );
    this.doors.addAll( doors );
  }

  public void addDoor( @Nonnull Door door ) {
    this.doors.add( door );
  }

  public void addWindow( @Nonnull Window window ) {
    this.windows.add( window );
  }

  @Nonnull
  public String getDescription() {
    return description;
  }

  @Nonnull
  public List<? extends Window> getWindows() {
    return Collections.unmodifiableList( windows );
  }

  @Nonnull
  public List<? extends Door> getDoors() {
    return Collections.unmodifiableList( doors );
  }

  public void setDoors( @Nonnull List<? extends Door> doors ) {
    this.doors.clear();
    this.doors.addAll( doors );
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Room ) ) return false;

    Room room = ( Room ) o;

    if ( !description.equals( room.description ) ) return false;
    if ( !doors.equals( room.doors ) ) return false;
    if ( !windows.equals( room.windows ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = description.hashCode();
    result = 31 * result + windows.hashCode();
    result = 31 * result + doors.hashCode();
    return result;
  }
}
