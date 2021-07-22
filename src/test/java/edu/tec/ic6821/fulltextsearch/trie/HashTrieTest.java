package edu.tec.ic6821.fulltextsearch.trie;

import org.junit.Test;

import java.io.File;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HashTrieTest {

    @Test
    public void insertNewKey() {
        // given
        final String key = "key1";
        final File file = new File(".file1");
        final HashTrie<File> hashTrie = new HashTrie<>();

        // when
        hashTrie.insert(key, file);

        // then
        final Set<File> files = hashTrie.find(key);
        assertEquals(1, files.size());
        assertTrue(files.contains(file));
    }

    @Test
    public void insertExistingKey() {
        // given
        final String key = "key1";
        final File file1 = new File(".file1");
        final File file2 = new File(".file2");

        final HashTrie<File> hashTrie = new HashTrie<>();
        hashTrie.insert(key, file1);

        // when
        hashTrie.insert(key, file2);

        // then
        final Set<File> files = hashTrie.find(key);
        assertEquals(2, files.size());
        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));
    }

    @Test
    public void findNonExistingKey() {
        // given
        final String key = "key";
        final HashTrie<File> hashTrie = new HashTrie<>();

        // when
        final Set<File> files = hashTrie.find(key);

        // then
        assertTrue(files.isEmpty());
    }

    @Test
    public void findExistingKey() {
        // given
        final String key = "key";
        final File file = new File(".file1");
        final HashTrie<File> hashTrie = new HashTrie<>();
        hashTrie.insert(key, file);

        // when
        final Set<File> files = hashTrie.find(key);

        // then
        assertEquals(1, files.size());
        assertTrue(files.contains(file));
    }

}
