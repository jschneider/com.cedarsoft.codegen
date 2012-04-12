package com.cedarsoft.codegen.model.test;

/**
 *
 */
public class Window {
  /**
   * the comment for field width
   */
  private final double width;
  private final double height;
  private final String description;

  /**
   * the constructor
   *
   * @param description the descri
   * @param width       the width
   * @param height      the height
   */
  public Window( String description, double width, double height ) {
    this.width = width;
    this.height = height;
    this.description = description;
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Window ) ) return false;

    Window window = ( Window ) o;

    if ( Double.compare( window.height, height ) != 0 ) return false;
    if ( Double.compare( window.width, width ) != 0 ) return false;
    if ( description != null ? !description.equals( window.description ) : window.description != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = width != +0.0d ? Double.doubleToLongBits( width ) : 0L;
    result = ( int ) ( temp ^ ( temp >>> 32 ) );
    temp = height != +0.0d ? Double.doubleToLongBits( height ) : 0L;
    result = 31 * result + ( int ) ( temp ^ ( temp >>> 32 ) );
    result = 31 * result + ( description != null ? description.hashCode() : 0 );
    return result;
  }
}
