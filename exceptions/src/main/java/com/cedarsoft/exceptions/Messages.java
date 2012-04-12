package com.cedarsoft.exceptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Offers access to resource bundle entries
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Messages {
  @Nonnull
  private final String bundleName;

  public Messages( @Nonnull String bundleName ) {
    this.bundleName = bundleName;
  }

  @Nonnull
  public String getBundleName() {
    return bundleName;
  }

  @Nonnull
  public String getString( @Nonnull String key, @Nonnull Locale locale, @Nullable Object... messageArguments ) {
    ResourceBundle bundle = ResourceBundle.getBundle( getBundleName(), locale );

    if ( messageArguments == null || messageArguments.length == 0 ) {
      return bundle.getString( key );
    } else {
      return MessageFormat.format( bundle.getString( key ), messageArguments );
    }
  }

  @Nonnull
  public String get( @Nonnull Enum<?> enumValue, @Nonnull Locale locale, @Nullable Object... messageArguments ) {
    return get( enumValue, null, locale, messageArguments );
  }


  public String get( @Nonnull Enum<?> enumValue, @Nullable String category, @Nonnull Locale locale, @Nullable Object... messageArguments ) {
    String baseKey = enumValue.name();

    String key;
    if ( category == null ) {
      key = baseKey;
    } else {
      key = baseKey + "." + category;
    }

    return getString( key, locale, messageArguments );
  }
}
