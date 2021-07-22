package edu.tec.ic6821.fulltextsearch.index;

import org.junit.Test;

import java.io.File;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InMemoryMapIndexTest {

    @Test
    public void indexNewTerm() {
        // given
        final String term = "term1";
        final File file = new File(".file1");
        final InMemoryMapIndex inMemoryMapIndex = new InMemoryMapIndex();

        // when
        inMemoryMapIndex.index(term, file);

        // then
        final Set<File> files = inMemoryMapIndex.search(term);
        assertEquals(1, files.size());
        assertTrue(files.contains(file));
    }

    @Test
    public void indexExistingTerm() {
        // given
        final String term = "term1";
        final File file1 = new File(".file1");
        final File file2 = new File(".file2");

        final InMemoryMapIndex inMemoryMapIndex = new InMemoryMapIndex();
        inMemoryMapIndex.index(term, file1);

        // when
        inMemoryMapIndex.index(term, file2);

        // then
        final Set<File> files = inMemoryMapIndex.search(term);
        assertEquals(2, files.size());
        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));
    }

    @Test
    public void searchNonExistingTerm() {
        // given
        final String term = "term";
        final InMemoryMapIndex inMemoryMapIndex = new InMemoryMapIndex();

        // when
        final Set<File> files = inMemoryMapIndex.search(term);

        // then
        assertTrue(files.isEmpty());
    }

    @Test
    public void searchExistingTerm() {
        // given
        final String term = "term";
        final File file = new File(".file1");
        final InMemoryMapIndex inMemoryMapIndex = new InMemoryMapIndex();
        inMemoryMapIndex.index(term, file);

        // when
        final Set<File> files = inMemoryMapIndex.search(term);

        // then
        assertEquals(1, files.size());
        assertTrue(files.contains(file));
    }
}
