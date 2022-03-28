package studio.archetype.hologui2.utils.file;

import java.io.File;
import java.io.FileNotFoundException;

public class ReloadableFile {

    private final File file;
    private final OnReload reload;

    private long lastModified;

    public ReloadableFile(File file, OnReload reload) {
        this.file = file;
        this.reload = reload;

        this.lastModified = file.lastModified();
    }

    public void checkChange() throws FileNotFoundException {
        if(!file.exists())
            throw new FileNotFoundException("Unable to find reloadable file at \"" + file.getAbsolutePath() + "\"");
        long newLastModified = file.lastModified();
        if(newLastModified == lastModified)
            return;
        this.lastModified = newLastModified;
        this.reload.onReload(file);
    }

    @FunctionalInterface
    public interface OnReload {
        void onReload(File file);
    }
}
