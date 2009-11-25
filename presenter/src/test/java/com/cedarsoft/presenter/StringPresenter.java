package com.cedarsoft.presenter;

import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.lookup.Lookup;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Very simple implementation that is able to present a string.
 */
public class StringPresenter extends AbstractPresenter<String> {
  @NotNull
  @NonNls
  private final String value;

  public StringPresenter( @NotNull String value ) {
    this.value = value;
  }

  @Override
  protected void bind( @NotNull String presentation, @NotNull StructPart struct, @NotNull Lookup lookup ) {
  }

  @Override
  protected boolean addChildPresentation( @NotNull String presentation, @NotNull StructPart child, int index ) {
    return false;
  }

  @Override
  protected void removeChildPresentation( @NotNull String presentation, @NotNull StructPart child, int index ) {
  }

  @Override
  protected boolean shallAddChildren() {
    return false;
  }

  @Override
  @NotNull
  protected String createPresentation() {
    return value;
  }
}
