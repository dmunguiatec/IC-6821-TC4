package edu.tec.ic6821.fulltextsearch.index;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class InMemoryMapIndex implements Index, Serializable {

    private final Map<String, Set<File>> index = new HashMap<>();

    @Override
    public void index(String term, File file) {
        final Set<File> files = this.index.computeIfAbsent(term, __ -> new HashSet<>());
        files.add(file);
    }

    @Override
    public Set<File> search(String term) {
        return this.index.getOrDefault(term, Collections.emptySet());
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void persist() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean restore() {
        throw new UnsupportedOperationException();
    }
}
