package com.cedarsoft.codegen.model.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CollectionsObject {
  private final List<String> strings = new ArrayList<String>();

  public List<? extends String> getStrings() {
    return Collections.unmodifiableList( strings );
  }

  public void addString( String string ) {
    this.strings.add( string );
  }

  public void removeString( String string ) {
    this.strings.remove( string );
  }
}
