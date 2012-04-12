package com.cedarsoft.exceptions;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TestException extends ApplicationException {
  @Nonnull
  public static final ErrorCode.Prefix PREFIX = new ErrorCode.Prefix( "TD" );

  public TestException( @Nonnull TestExceptionDetails exceptionDetails, @Nonnull Object... messageArguments ) {
    super( exceptionDetails, messageArguments );
  }

}
