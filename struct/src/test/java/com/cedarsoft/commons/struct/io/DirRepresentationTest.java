package com.cedarsoft.commons.struct.io;

import com.cedarsoft.MockitoTemplate;
import com.cedarsoft.TestUtils;
import com.cedarsoft.commons.struct.DefaultNode;
import com.cedarsoft.commons.struct.Node;
import com.cedarsoft.commons.struct.Path;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class DirRepresentationTest {

  private Node root;

  @BeforeMethod
  public void setup() {
    root = new DefaultNode( "root" );

    DefaultNode a = new DefaultNode( "a" );
    root.addChild( a );
    a.addChild( new DefaultNode( "aa" ) );
    a.addChild( new DefaultNode( "ab" ) );

    root.addChild( new DefaultNode( "b" ) );
  }

  @Test
  public void testRootInvisible() {
    File baseDir = TestUtils.createEmptyTmpDir();
    assertEquals( baseDir.list().length, 0 );

    DirRepresenter representer = new DirRepresenter( root, false );
    representer.store( baseDir, null );

    List<File> firstLevels = Arrays.asList( baseDir.listFiles() );
    Collections.sort( firstLevels );


    assertEquals( firstLevels.size(), 2 );

    assertTrue( new File( baseDir, "a/aa" ).isDirectory() );
    assertTrue( new File( baseDir, "a/ab" ).isDirectory() );
    assertTrue( new File( baseDir, "b" ).isDirectory() );
  }

  @Test
  public void testRootVisible() {
    File baseDir = TestUtils.createEmptyTmpDir();
    assertEquals( baseDir.list().length, 0 );

    DirRepresenter representer = new DirRepresenter( root, true );
    representer.store( baseDir, null );

    assertEquals( baseDir.listFiles().length, 1 );

    assertTrue( new File( baseDir, "root/a/aa" ).isDirectory() );
    assertTrue( new File( baseDir, "root/a/ab" ).isDirectory() );
    assertTrue( new File( baseDir, "root/b" ).isDirectory() );


    
  }

  @Test
  public void testCallbackCreation() throws Exception {
    final File baseDir = TestUtils.createEmptyTmpDir();

    assertEquals( baseDir.list().length, 0 );

    final DirRepresenter representer = new DirRepresenter( root, true );

    new MockitoTemplate() {
      @Mock
      private DirRepresenter.Callback callback;

      @Override
      protected void stub() throws Exception {
      }

      @Override
      protected void execute() throws Exception {
        representer.store( baseDir, callback );
      }

      @Override
      protected void verifyMocks() throws Exception {
        verify( callback ).dirCreated( root, new Path( "root" ), new File( baseDir, "root" ) );
        verify( callback ).dirCreated( root.findChild( "a" ), Path.createPath( "root/a" ), new File( baseDir, "root/a" ) );
        verify( callback ).dirCreated( root.findChild( "b" ), Path.createPath( "root/b" ), new File( baseDir, "root/b" ) );
        verify( callback ).dirCreated( root.findChild( "a" ).findChild( "aa" ), Path.createPath( "root/a/aa" ), new File( baseDir, "root/a/aa" ) );
        verify( callback ).dirCreated( root.findChild( "a" ).findChild( "ab" ), Path.createPath( "root/a/ab" ), new File( baseDir, "root/a/ab" ) );
      }
    }.run();
  }
}
