package com.cedarsoft.file;

import org.junit.*;
import org.junit.rules.*;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class FileNameCapitalLettersTest {
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();
  private FileTypeRegistry fileTypeRegistry;

  @Before
  public void setUp() throws Exception {
    fileTypeRegistry = new FileTypeRegistry();
  }

  @Test
  public void testIt() throws Exception {
    File dir = temporaryFolder.newFolder();

    File file = new File( dir, "Asdf.JPG" );
    assertThat( file.createNewFile() ).isTrue();

    FileNamesFactory fileNamesFactory = new FileNamesFactory( fileTypeRegistry );
    FileNames fileNames = fileNamesFactory.create( dir );

    assertThat( fileNames ).isNotNull();
    assertThat( fileNames.getFileNames() ).hasSize( 1 );

    FileName fileName = fileNames.getFileNames().get( 0 );

    assertThat( fileName.getName() ).isEqualTo( file.getName() );
  }

  @Test
  public void testPparsing() throws Exception {
    FileName parsed = fileTypeRegistry.parseFileName( "Asdf.JPG" );
    assertThat( parsed.getBaseName().getName() ).isEqualTo( "Asdf" );
    assertThat( parsed.getExtension().getDelimiter() ).isEqualTo( "." );
    assertThat( parsed.getExtension().getExtension() ).isEqualTo( "JPG" );
  }

  @Test
  public void testFileName() throws Exception {
    FileName parsed = FileTypeRegistry.JPEG.getFileName( "Asdf.JPG" );
    assertThat( parsed.getBaseName().getName() ).isEqualTo( "Asdf" );
    assertThat( parsed.getExtension().getDelimiter() ).isEqualTo( "." );
    assertThat( parsed.getExtension().getExtension() ).isEqualTo( "JPG" );
  }
}
