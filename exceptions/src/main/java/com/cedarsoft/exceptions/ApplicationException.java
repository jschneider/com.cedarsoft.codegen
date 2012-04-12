package com.cedarsoft.exceptions;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Locale;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 * @noinspection AbstractClassWithoutAbstractMethods
 */
@SuppressWarnings( {"AbstractClassExtendsConcreteClass", "AbstractClassWithoutAbstractMethods"} )
public abstract class ApplicationException extends RuntimeException {
  @Nonnull
  private final Details details;
  @Nonnull
  private final Object[] messageArguments;

  /**
   * Creates a new ApplicationException
   *
   * @param details          the details (an enum that implements {@link Details}
   * @param messageArguments the (optional) message arguments
   * @param <T>              the details type
   */
  protected <T extends Enum<?> & Details> ApplicationException( @Nonnull T details, @Nonnull Object... messageArguments ) {
    this( null, details, messageArguments );
  }

  /**
   * Creates a new ApplicationException including a cause (which is never presented to the user).
   *
   * @param cause            the cause
   * @param details          the details
   * @param messageArguments the (optional) message arguments
   * @param <T>              the details type
   */
  protected <T extends Enum<?> & Details> ApplicationException( @Nullable Throwable cause, @Nonnull T details, @Nonnull Object... messageArguments ) {
    super( cause );
    this.details = details;
    this.messageArguments = messageArguments.clone();
  }

  /**
   * Returns the details
   *
   * @return the details
   */
  @Nonnull
  public Details getDetails() {
    return details;
  }

  /**
   * Returns the message arguments.
   *
   * @return the message arguments
   */
  @Nonnull
  public Object[] getMessageArguments() {
    return messageArguments.clone();
  }

  /**
   * @return the message (based on the error code)
   */
  @SuppressWarnings( "RefusedBequest" )
  @Override
  public String getMessage() {
    return details.getErrorCode().toString();
  }

  /**
   * Returns the error code
   *
   * @return the error code
   */
  @Nonnull
  public ErrorCode getErrorCode() {
    return details.getErrorCode();
  }

  /**
   * Returns the localized message for the default locale
   *
   * @return the localized message for the default locale
   */
  @SuppressWarnings( "RefusedBequest" )
  @Override
  @Nonnull
  public String getLocalizedMessage() {
    return details.getLocalizedMessage( messageArguments );
  }

  /**
   * Returns the localized message for the given locale
   *
   * @param locale the locale
   * @return the localized message for the given locale
   */
  @SuppressWarnings( "RefusedBequest" )
  @Nonnull
  public String getLocalizedMessage( @Nonnull Locale locale ) {
    return details.getLocalizedMessage( locale, messageArguments );
  }

  /**
   * Returns the title for the default locale
   *
   * @return the title
   */
  @Nonnull
  public String getTitle() {
    return details.getTitle( messageArguments );
  }

  /**
   * Returns the title for the given locale
   *
   * @param locale the locale
   * @return the title for the locale
   */
  @Nonnull
  public String getTitle( @Nonnull Locale locale ) {
    return details.getTitle( locale, messageArguments );
  }

  /**
   *
   */
  public interface Details {
    /**
     * Returns the localized message (without the error code)
     *
     * @param messageArguments the message arguments
     * @return the localized message
     */
    @Nonnull
    String getLocalizedMessage( @Nullable Object... messageArguments );

    /**
     * Returns the localized message (without the error code)
     *
     * @param locale           the locale
     * @param messageArguments the message arguments
     * @return the localized message
     */
    @Nonnull
    String getLocalizedMessage( @Nonnull Locale locale, @Nullable Object... messageArguments );

    /**
     * Returns the title
     *
     * @param messageArguments the message arguments
     * @return the title
     */
    @Nonnull
    String getTitle( @Nullable Object... messageArguments );

    /**
     * Returns the title
     *
     * @param locale           the locale
     * @param messageArguments the message arguments
     * @return the title
     */
    @Nonnull
    String getTitle( @Nonnull Locale locale, @Nullable Object... messageArguments );

    /**
     * Returns the error code
     *
     * @return the error code
     */
    @Nonnull
    ErrorCode getErrorCode();
  }
}
