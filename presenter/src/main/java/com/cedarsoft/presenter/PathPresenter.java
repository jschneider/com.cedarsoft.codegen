package com.cedarsoft.presenter;

import com.cedarsoft.commons.struct.Node;
import com.cedarsoft.commons.struct.StructPart;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;

/**
 * This presenter creates a string representation of the path of the given node.
 */
public class PathPresenter implements Presenter<String> {
  @Override
  @NotNull
  public String present( @NotNull StructPart struct ) {
    if ( struct instanceof Node ) {
      return ( ( Node ) struct ).getPath().toString();
    }
    throw new IllegalArgumentException( "Need a node" );
  }
}
