package com.cedarsoft.presenter;

import org.jetbrains.annotations.NotNull;

import javax.swing.JMenuItem;

/**
 * This presenter creates a JMenuItem for a action.
 */
public class DefaultJMenuItemPresenter extends AbstractButtonPresenter<JMenuItem> {
  @Override
  @NotNull
  public JMenuItem createPresentation() {
    return new JMenuItem();
  }

  @Override
  protected boolean shallAddChildren() {
    return false;
  }
}
