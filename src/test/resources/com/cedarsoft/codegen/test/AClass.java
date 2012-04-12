package com.cedarsoft.codegen.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class AClass {
  private String single;
  private List<String> theStrings = new ArrayList<String>();
  private List<? extends String> wildcardStrings;

  private Set<String> set = new HashSet<String>( );
  private Set<? extends String> wildcardSet = new HashSet<String>( );

  public String getSingle() {
    return single;
  }

  public void setSingle( String single ) {
    this.single = single;
  }

  public List<String> getTheStrings() {
    return theStrings;
  }

  public void setTheStrings( List<String> theStrings ) {
    this.theStrings = theStrings;
  }

  public List<? extends String> getWildcardStrings() {
    return wildcardStrings;
  }

  public void setWildcardStrings( List<? extends String> wildcardStrings ) {
    this.wildcardStrings = wildcardStrings;
  }

  public void setSet( Set<String> set ) {
    this.set = set;
  }

  public void setWildcardSet( Set<? extends String> wildcardSet ) {
    this.wildcardSet = wildcardSet;
  }

  public Set<String> getSet() {

    return set;
  }

  public Set<? extends String> getWildcardSet() {
    return wildcardSet;
  }
}
