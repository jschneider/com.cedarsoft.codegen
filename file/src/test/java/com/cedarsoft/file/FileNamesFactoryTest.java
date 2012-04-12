package com.cedarsoft.file;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import javax.annotation.Nonnull;
import org.fest.assertions.Condition;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class FileNamesFactoryTest {
  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  private FileTypeRegistry fileTypeRegistry;
  private FileNamesFactory factory;
  private File baseDir;

  @Before
  public void setUp() throws Exception {
    fileTypeRegistry = new FileTypeRegistry( true );
    factory = new FileNamesFactory( fileTypeRegistry );
    baseDir = folder.newFolder( "myFolder" );
  }

  @Test
  public void testBaseNameAware() throws Exception {
    File f1 = new File( baseDir, "A.JPG" );
    f1.createNewFile();
    File f2 = new File( baseDir, "A.cr2" );
    f2.createNewFile();

    BaseNameAwareFileNames baseNameAware = factory.createBaseNameAware( baseDir );
    assertThat( baseNameAware ).isNotNull();
    assertThat( baseNameAware.getEntries() ).hasSize( 1 );
    Map.Entry<BaseName, FileNames> entry = baseNameAware.getEntries().iterator().next();
    assertThat( entry.getKey().getName() ).isEqualTo( "A" );
    List<? extends FileName> fileNames = entry.getValue().getFileNames();
    assertThat(fileNames).hasSize(2);

    Condition<String> condition = new MyStringCondition("A.JPG","A.cr2");
    assertThat( fileNames.get(0).getName() ).satisfies(condition);
    assertThat( fileNames.get(1).getName() ).satisfies(condition);

    for ( FileName fileName : fileNames) {
      File f = new File( baseDir, fileName.getName() );
      assertThat( f ).exists();
    }
  }

  @Test
  public void testLowerCase() throws Exception {
    {
      FileNames fileNames = factory.create( baseDir );
      assertThat( fileNames ).isNotNull();
      assertThat( fileNames.getFileNames() ).isEmpty();
    }

    File f1 = new File( baseDir, "a.jpg" );
    f1.createNewFile();
    File f2 = new File( baseDir, "a.cr2" );
    f2.createNewFile();

    {
      FileNames fileNames = factory.create( baseDir );
      assertThat( fileNames ).isNotNull();
      assertThat( fileNames.getFileNames() ).hasSize( 2 );

      Set<String> names = new HashSet<String>( );
      for ( FileName fileName : fileNames.getFileNames( ) ) {
        names.add( fileName.getName( ) );
      }

      assertThat( names ).contains( "a.jpg" );
      assertThat( names ).contains( "a.cr2" );
    }
  }

  @Test
  public void testMixedCase() throws Exception {
    {
      FileNames fileNames = factory.create( baseDir );
      assertThat( fileNames ).isNotNull();
      assertThat( fileNames.getFileNames() ).isEmpty();
    }

    File f1 = new File( baseDir, "A.JPG" );
    f1.createNewFile();
    File f2 = new File( baseDir, "A.cr2" );
    f2.createNewFile();

    {
      FileNames fileNames = factory.create( baseDir );
      assertThat( fileNames ).isNotNull();
      assertThat( fileNames.getFileNames() ).hasSize( 2 );

      assertThat( fileNames.getFileNames().get( 0 ).getName() ).satisfies( new MyStringCondition( "A.JPG", "A.cr2" ) );
      assertThat( fileNames.getFileNames().get( 1 ).getName() ).satisfies( new MyStringCondition( "A.JPG", "A.cr2" ) );
    }
  }

  @Test
  public void testUpperCase() throws Exception {
    {
      FileNames fileNames = factory.create( baseDir );
      assertThat( fileNames ).isNotNull();
      assertThat( fileNames.getFileNames() ).isEmpty();
    }

    File f1 = new File( baseDir, "A.JPG" );
    f1.createNewFile();
    File f2 = new File( baseDir, "A.CR2" );
    f2.createNewFile();

    {
      FileNames fileNames = factory.create( baseDir );
      assertThat( fileNames ).isNotNull();
      assertThat( fileNames.getFileNames() ).hasSize( 2 );

      assertThat( fileNames.getFileNames().get( 0 ).getName() ).satisfies( new MyStringCondition( "A.JPG", "A.CR2" ) );
      assertThat( fileNames.getFileNames().get( 1 ).getName() ).satisfies( new MyStringCondition( "A.JPG", "A.CR2" ) );
    }
  }

  private static class MyStringCondition extends Condition<String> {
    @Nonnull 
    private final Set<String> possibleStrings ;

    private MyStringCondition(@Nonnull String... possibleStrings) {
      this(ImmutableSet.copyOf(possibleStrings));
    }
    
    private MyStringCondition(@Nonnull Set<? extends String> possibleStrings) {
      this.possibleStrings = new HashSet<String>(possibleStrings);
    }

    @Override
    public boolean matches(String value) {
      return possibleStrings.contains(value);
    }
  }
}
