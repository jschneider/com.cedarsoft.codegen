package com.cedarsoft.codegen.model.test;

import javax.annotation.Nonnull;

/**
 *
 */
public class AnotherOne {
  private final boolean dependent;

  @Nonnull
  private final Version min;
  @Nonnull
  private final Version max;

  public AnotherOne( boolean dependent, @Nonnull Version min, @Nonnull Version max ) {
    this.dependent = dependent;
    this.min = min;
    this.max = max;
  }

  @Nonnull
  public Version getMin() {
    return min;
  }

  @Nonnull
  public Version getMax() {
    return max;
  }


  public boolean isDependent() {
    return dependent;
  }

  public static class Version {
  }
}
