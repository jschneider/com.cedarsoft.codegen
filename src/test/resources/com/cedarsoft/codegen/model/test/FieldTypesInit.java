package com.cedarsoft.codegen.model.test;

/**
 *
 */
public class FieldTypesInit {
  public static final String CONSTANT = "asdf";
  /**
   * the comment for field width
   */
  private final double width;
  private int height;
  private final String description;

  /**
   * the constructor
   *
   * @param description the descri
   * @param width       the width
   */
  public FieldTypesInit( String description, double width ) {
    this.width = width;
    this.description = description;
  }

  public double getWidth() {
    return width;
  }

  public String getDescription() {
    return description;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight( int height ) {
    this.height = height;
  }
}
