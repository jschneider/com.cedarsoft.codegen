package com.cedarsoft.exceptions;


import org.junit.*;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ApplicationExceptionTest {
  @Nonnull
  public static final String BUNDLE = "com.cedarsoft.exceptions.testmessages";

  @Test
  public void testSetup() throws Exception {
    assertThat( ResourceBundle.getBundle( BUNDLE ) ).isNotNull();
  }

  @Test
  public void testDefaultLocale() throws Exception {
    Locale.setDefault( Locale.ENGLISH );

    assertThat( new TestException( TestExceptionDetails.ERROR_1, "a" ).getLocalizedMessage() ).isEqualTo( "The Value 1 en: <a>" );
    assertThat( new TestException( TestExceptionDetails.ERROR_1, "b" ).getMessage() ).isEqualTo( "TD-701" );

    assertThat( new TestException( TestExceptionDetails.ERROR_2, "c" ).getLocalizedMessage() ).isEqualTo( "The Value 2 en: <c>" );
    assertThat( new TestException( TestExceptionDetails.ERROR_2, "d" ).getMessage() ).isEqualTo( "TD-702" );
  }

  @Test
  public void testLocales() throws Exception {
    assertThat( new TestException( TestExceptionDetails.ERROR_1, "asdf" ).getLocalizedMessage( Locale.ENGLISH ) ).isEqualTo( "The Value 1 en: <asdf>" );
    assertThat( new TestException( TestExceptionDetails.ERROR_1, "asdf" ).getLocalizedMessage( Locale.GERMAN ) ).isEqualTo( "The Value 1 de: <asdf>" );
    assertThat( new TestException( TestExceptionDetails.ERROR_1, "asdf" ).getMessage() ).isEqualTo( "TD-701" );
    assertThat( new TestException( TestExceptionDetails.ERROR_2, "asdf" ).getLocalizedMessage( Locale.ENGLISH ) ).isEqualTo( "The Value 2 en: <asdf>" );
    assertThat( new TestException( TestExceptionDetails.ERROR_2, "asdf" ).getLocalizedMessage( Locale.GERMAN ) ).isEqualTo( "The Value 2 de: <asdf>" );
    assertThat( new TestException( TestExceptionDetails.ERROR_2, "asdf" ).getMessage() ).isEqualTo( "TD-702" );
  }

}
