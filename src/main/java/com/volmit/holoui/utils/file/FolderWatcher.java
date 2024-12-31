package com.volmit.holoui.utils.file;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.io.File;
import java.util.List;
import java.util.Map;

@Getter
public class FolderWatcher extends FileWatcher {
    private Map<File, FolderWatcher> watchers;
    private List<File> changed;
    private List<File> created;
    private List<File> deleted;

    public FolderWatcher(File file) {
        super(file);
    }

    protected void readProperties() {
        if (watchers == null) {
            watchers = Maps.newConcurrentMap();
            changed = Lists.newArrayList();
            created = Lists.newArrayList();
            deleted = Lists.newArrayList();
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (watchers.containsKey(f))
                        continue;
                    watchers.put(f, new FolderWatcher(f));
                }
            }

            watchers.values().removeIf(FileWatcher::wasDeleted);
        } else {
            super.readProperties();
        }
    }

    public boolean checkModified() {
        changed.clear();
        created.clear();
        deleted.clear();

        if (file.isDirectory()) {
            Map<File, FolderWatcher> w = Map.copyOf(watchers);
            readProperties();

            for (File i : w.keySet()) {
                if (!watchers.containsKey(i)) {
                    deleted.add(i);
                }
            }

            for (File i : watchers.keySet()) {
                if (!w.containsKey(i)) {
                    created.add(i);
                } else {
                    FolderWatcher fw = watchers.get(i);
                    if (fw.checkModified()) {
                        changed.add(fw.file);
                    }

                    changed.addAll(fw.getChanged());
                    created.addAll(fw.getCreated());
                    deleted.addAll(fw.getDeleted());
                }
            }

            return !changed.isEmpty() || !created.isEmpty() || !deleted.isEmpty();
        }

        return super.checkModified();
    }

    public boolean checkModifiedFast() {
        if (watchers == null || watchers.isEmpty()) {
            return checkModified();
        }

        changed.clear();
        created.clear();
        deleted.clear();

        if (file.isDirectory()) {
            for (File i : watchers.keySet()) {
                FolderWatcher fw = watchers.get(i);
                if (fw.checkModifiedFast()) {
                    changed.add(fw.file);
                }

                changed.addAll(fw.getChanged());
                created.addAll(fw.getCreated());
                deleted.addAll(fw.getDeleted());
            }

            return !changed.isEmpty() || !created.isEmpty() || !deleted.isEmpty();
        }

        return super.checkModified();
    }

    public void clear() {
        watchers.clear();
        changed.clear();
        deleted.clear();
        created.clear();
    }
}