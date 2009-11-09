package com.cedarsoft.presenter;

import org.jetbrains.annotations.NotNull;

import javax.swing.JCheckBox;
import java.lang.Override;

/**
 * <p/>
 * Date: Jun 1, 2007<br>
 * Time: 4:19:22 PM<br>
 */
public class JCheckboxButtonPresenter extends AbstractButtonPresenter<JCheckBox> {
  @Override
  @NotNull
  public JCheckBox createPresentation() {
    return new JCheckBox();
  }

  @Override
  protected boolean shallAddChildren() {
    return false;
  }
}
