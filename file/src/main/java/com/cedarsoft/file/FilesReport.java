package com.cedarsoft.file;

import com.cedarsoft.file.FileName;
import com.cedarsoft.provider.Provider;
import com.cedarsoft.utils.StillContainedException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Represents a report about the files within a directory sorted by common base names
 */
public class FilesReport {
  @NotNull
  private final SortedMap<String, BaseNameEntry> entries = new TreeMap<String, BaseNameEntry>();

  public void add( @NotNull FileName fileName, @NotNull Provider<? extends InputStream, IOException> inputStreamProvider ) {
    getEntry( fileName.getBaseName().getName() ).add( fileName, inputStreamProvider );
  }

  @NotNull
  public Collection<? extends BaseNameEntry> getEntries() {
    return Collections.unmodifiableCollection( entries.values() );
  }

  public BaseNameEntry getEntry( @NotNull @NonNls String baseName ) {
    BaseNameEntry found = entries.get( baseName );
    if ( found != null ) {
      return found;
    }

    BaseNameEntry baseNameEntry = new BaseNameEntry( baseName );
    entries.put( baseName, baseNameEntry );
    return baseNameEntry;
  }

  public int size() {
    return entries.size();
  }

  public static class BaseNameEntry {
    @NotNull
    private final List<Entry> entries = new ArrayList<Entry>();

    @NotNull
    @NonNls
    private final String baseName;

    public BaseNameEntry( @NotNull @NonNls String baseName ) {
      this.baseName = baseName;
    }

    @NotNull
    @NonNls
    public String getBaseName() {
      return baseName;
    }

    @NotNull
    public List<? extends Entry> getEntries() {
      return Collections.unmodifiableList( entries );
    }

    public void add( @NotNull FileName fileName, @NotNull Provider<? extends InputStream, IOException> inputStreamProvider ) {
      if ( containsEntryFor( fileName ) ) {
        throw new StillContainedException( fileName );
      }

      entries.add( new Entry( fileName, inputStreamProvider ) );
    }

    private boolean containsEntryFor( @NotNull FileName fileName ) {
      for ( Entry entry : entries ) {
        if ( entry.getFileName().equals( fileName ) ) {
          return true;
        }
      }
      return false;
    }
  }

  public static class Entry {
    @NotNull
    private final FileName fileName;

    private final Provider<? extends InputStream, IOException> provider;

    public Entry( @NotNull FileName fileName, Provider<? extends InputStream, IOException> provider ) {
      this.fileName = fileName;
      this.provider = provider;
    }

    @NotNull
    public FileName getFileName() {
      return fileName;
    }

    public Provider<? extends InputStream, IOException> getProvider() {
      return provider;
    }
  }
}
