package edu.tec.ic6821.fulltextsearch.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

public final class MapIndex implements Index {

    private final File indexFile;
    private Index index;

    public MapIndex(final File location, final Index delegate) {
        this.indexFile = location;
        this.index = delegate;
    }

    @Override
    public void index(String term, File file) {
        this.index.index(term, file);
    }

    @Override
    public Set<File> search(String term) {
        return this.index.search(term);
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public void persist() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.indexFile))) {
            out.writeObject(this.index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean restore() {
        if (!this.indexFile.exists()) {
            return false;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.indexFile))) {
            this.index = (InMemoryMapIndex) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
