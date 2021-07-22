package edu.tec.ic6821.fulltextsearch.index;

import edu.tec.ic6821.fulltextsearch.trie.HashTrie;
import org.junit.Test;

import java.io.File;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TrieIndexTest {

    @Test
    public void indexNewTerm() {
        // given
        final String term = "term1";
        final File file = new File(".file1");

        final File location = new File("/tmp/.trieindextest");
        final TrieIndex trieIndex = new TrieIndex(location, new HashTrie<>());

        // when
        trieIndex.index(term, file);

        // then
        final Set<File> files = trieIndex.search(term);
        assertEquals(1, files.size());
        assertTrue(files.contains(file));
    }

    @Test
    public void indexExistingTerm() {
        // given
        final String term = "term1";
        final File file1 = new File(".file1");
        final File file2 = new File(".file2");

        final File location = new File("/tmp/.trieindextest");
        final TrieIndex trieIndex = new TrieIndex(location, new HashTrie<>());

        trieIndex.index(term, file1);

        // when
        trieIndex.index(term, file2);

        // then
        final Set<File> files = trieIndex.search(term);
        assertEquals(2, files.size());
        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));
    }

    @Test
    public void searchNonExistingTerm() {
        // given
        final String term = "term";

        final File location = new File("/tmp/.trieindextest");
        final TrieIndex trieIndex = new TrieIndex(location, new HashTrie<>());

        // when
        final Set<File> files = trieIndex.search(term);

        // then
        assertTrue(files.isEmpty());
    }

    @Test
    public void searchExistingTerm() {
        // given
        final String term = "term";
        final File file = new File(".file1");

        final File location = new File("/tmp/.trieindextest");
        final TrieIndex trieIndex = new TrieIndex(location, new HashTrie<>());

        trieIndex.index(term, file);

        // when
        final Set<File> files = trieIndex.search(term);

        // then
        assertEquals(1, files.size());
        assertTrue(files.contains(file));
    }

    @Test
    public void persistAndRestore() {
        // given
        final String term1 = "term1";
        final String term2 = "term2";
        final String term3 = "term3";
        final File file1 = new File(".file1");
        final File file2 = new File(".file2");
        final File file3 = new File(".file3");

        final File location = new File("/tmp/.trieindextest");
        if (location.exists()) {
            location.delete();
        }
        final TrieIndex trieIndex = new TrieIndex(location, new HashTrie<>());

        trieIndex.index(term1, file1);
        trieIndex.index(term1, file2);
        trieIndex.index(term2, file3);

        // when
        trieIndex.persist();
        final TrieIndex newTrieIndex = new TrieIndex(location, new HashTrie<>());
        final boolean restored = newTrieIndex.restore();

        // then
        assertTrue(restored);

        final Set<File> term1Files = newTrieIndex.search(term1);
        assertEquals(2, term1Files.size());
        assertTrue(term1Files.contains(file1));
        assertTrue(term1Files.contains(file2));

        final Set<File> term2Files = newTrieIndex.search(term2);
        assertEquals(1, term2Files.size());
        assertTrue(term2Files.contains(file3));

        final Set<File> term3Files = newTrieIndex.search(term3);
        assertTrue(term3Files.isEmpty());
    }

    @Test
    public void restoreNonExistingFile() {
        // given
        final File location = new File("/tmp/.trieindextest");
        if (location.exists()) {
            location.delete();
        }
        final TrieIndex trieIndex = new TrieIndex(location, new HashTrie<>());

        // when
        final boolean restored = trieIndex.restore();

        // then
        assertFalse(restored);
    }
}
