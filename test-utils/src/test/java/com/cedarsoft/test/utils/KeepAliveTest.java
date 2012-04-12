package com.cedarsoft.test.utils;

import com.google.common.io.ByteStreams;
import org.junit.*;
import sun.net.www.http.HttpClient;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class KeepAliveTest {
  @Rule
  public ThreadRule threadRule = new ThreadRule();
  @Rule
  public CatchAllExceptionsRule catchAllExceptionsRule = new CatchAllExceptionsRule();

  @Test
  public void testKeepAlive() throws Exception {
    try {
      HttpClient client = HttpClient.New( new URL( "http://www.google.de" ) );
      assertThat( client ).isNotNull();
      assertThat( client.isKeepingAlive() ).isFalse();
      assertThat( client.isCachedConnection() ).isFalse();
    } catch ( ConnectException e ) {
      assertThat( e ).hasMessage( "Connection refused" );
    }
  }

  @Test
  public void testTestIt() throws Exception {
    URLConnection urlConnection = new URL( "http://www.google.de" ).openConnection();
    urlConnection.connect();
    assertThat( urlConnection ).isInstanceOf( HttpURLConnection.class );
    Object content = urlConnection.getContent();
    assertThat( content ).isInstanceOf( InputStream.class );

    InputStream inputStream = ( InputStream ) content;
    byte[] bytes = ByteStreams.toByteArray( inputStream );
    assertThat( bytes.length ).isGreaterThan( 10000 );

    assertThat( inputStream.available() ).isEqualTo( 0 );

    inputStream.close();
  }
}