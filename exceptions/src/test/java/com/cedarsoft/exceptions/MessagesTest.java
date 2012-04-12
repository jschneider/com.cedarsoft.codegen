package com.cedarsoft.exceptions;

import com.cedarsoft.test.utils.LocaleRule;
import org.junit.*;

import java.util.Locale;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class MessagesTest {
  @Test
  public void testBundles() throws Exception {
    Messages messages = new Messages( ApplicationExceptionTest.BUNDLE );
    assertThat( messages ).isNotNull();

    assertThat( messages.getBundleName() ).isEqualTo( ApplicationExceptionTest.BUNDLE );

    assertThat( messages.getString( "ERROR_1", Locale.GERMAN ) ).isEqualTo( "The Value 1 de: <{0}>" );
    assertThat( messages.getString( "ERROR_1", Locale.ENGLISH ) ).isEqualTo( "The Value 1 en: <{0}>" );

    assertThat( messages.getString( "ERROR_1", Locale.FRENCH ) ).isEqualTo( "The Value 1 default: <{0}>" );
  }

  @Rule
  public LocaleRule localeRule = new LocaleRule( Locale.FRENCH );
}
